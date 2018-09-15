package com.example.android.vcare.ui.login;

import android.app.Dialog;
import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.vcare.BuildConfig;
import com.example.android.vcare.MyApplication;
import com.example.android.vcare.R;
import com.example.android.vcare.databinding.DialogForgotPasswordBinding;
import com.example.android.vcare.event.AccountEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.job.ForgotPasswordJob;
import com.example.android.vcare.ui.BaseActivity;
import com.example.android.vcare.util.EventBusUtil;
import com.example.android.vcare.util.ToastUtil;
import com.example.android.vcare.util.Util;
import com.example.android.vcare.widget.TextInputErrorWatcher;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ForgotPasswordDialogFragment extends DialogFragment implements View.OnClickListener {

    public static ForgotPasswordDialogFragment newInstance() {

        Bundle args = new Bundle();

        ForgotPasswordDialogFragment fragment = new ForgotPasswordDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private DialogForgotPasswordBinding binding;
    private BaseActivity activity;
    private final int hashCode = hashCode();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_forgot_password, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.email.addTextChangedListener(new TextInputErrorWatcher(binding.emailInputLayout));

        binding.resetPassword.setOnClickListener(this);
        binding.close.setOnClickListener(this);
        binding.backToLogin.setOnClickListener(this);

        if (BuildConfig.DEBUG) {
            binding.email.setText("mrinkika@gmail.com");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == binding.resetPassword) {
            if (isValid()) {
                activity.showLoadingDialog();
                String email = binding.email.getText().toString();
                MyApplication.addJobInBackground(new ForgotPasswordJob(email, hashCode));
            }
        } else if (view == binding.close) {
            dismiss();
        } else if (view == binding.backToLogin) {
            dismiss();
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        String email = binding.email.getText().toString();

        if (TextUtils.isEmpty(email)) {
            isValid = false;
            binding.emailInputLayout.setError(getString(R.string.field_cannot_empty));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.setError(getString(R.string.invalid_email_format));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBusUtil.register(this);
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(Util.dpToPx(getActivity(), 380), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBusUtil.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnForgotPassword event) {
        if (hashCode == event.getHashCode()) {
            activity.dismissLoadingDialog();
            ToastUtil.show(activity, event.getMessage());
            dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(ExceptionEvent event) {
        if (hashCode == event.getHashCode()) {
            activity.dismissLoadingDialog();
            ToastUtil.show(activity, event.getErrorMessage());
        }
    }
}
