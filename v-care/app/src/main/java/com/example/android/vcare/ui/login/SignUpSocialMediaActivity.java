//package com.example.android.vcare.ui.login;
//
//import android.content.Context;
//import android.content.Intent;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.text.Html;
//import android.text.TextUtils;
//import android.util.Patterns;
//import android.view.View;
//
//import com.example.android.vcare.R;
//import com.example.android.vcare.databinding.ActivitySignUpBinding;
//import com.example.android.vcare.model.User;
//import com.example.android.vcare.ui.BaseActivity;
//import com.example.android.vcare.widget.TextInputErrorWatcher;
//
//public class SignUpSocialMediaActivity extends BaseActivity implements View.OnClickListener {
//
//    public static void start(Context context, User user) {
//        Intent starter = new Intent(context, SignUpSocialMediaActivity.class);
//        starter.putExtra(USER, user.toJson());
//        context.startActivity(starter);
//    }
//
//    private ActivitySignUpBinding binding;
//    private static final String USER = "user";
//    private User user;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
//        setTintBackButtonIcon(R.color.white, R.drawable.ic_back_black_24dp);
//        setDisplayHomeAsUpEnabled();
//        setBackNavigation();
//        setToolbarTitle(R.string.sign_up);
//
//        user = User.deserialize(getIntent().getStringExtra(USER));
//        initView();
//        initTnC();
//        initUserData();
//    }
//
//    private void initView() {
//        binding.name.addTextChangedListener(new TextInputErrorWatcher(binding.nameInputLayout));
//        binding.email.addTextChangedListener(new TextInputErrorWatcher(binding.emailInputLayout));
//        binding.phone.addTextChangedListener(new TextInputErrorWatcher(binding.phoneInputLayout));
//        binding.password.addTextChangedListener(new TextInputErrorWatcher(binding.passwordInputLayout));
//        binding.confirmPassword.addTextChangedListener(new TextInputErrorWatcher(binding.confirmPasswordInputLayout));
//
//        binding.signUp.setOnClickListener(this);
//        binding.signUp.setText(R.string.submit);
//
//        binding.termsCondition.setVisibility(View.GONE);
//        binding.fbLogin.setVisibility(View.GONE);
//        binding.googleLogin.setVisibility(View.GONE);
//        binding.orLayout.setVisibility(View.GONE);
//        binding.passwordInputLayout.setVisibility(View.GONE);
//        binding.confirmPasswordInputLayout.setVisibility(View.GONE);
//    }
//
//    private void initTnC() {
//        String text = "<font color=#999999>" + getString(R.string.term_conditions_title) + "</font> <font color=#3b5998>" + getString(R.string.term_conditions) + "</font>";
//        binding.termsCondition.setText(Html.fromHtml(text));
//    }
//
//    private void initUserData() {
//        binding.name.setText(user.getName());
//        binding.email.setText(user.getEmail());
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view == binding.signUp) {
//            if (isValid()) {
//
//            }
//        }
//    }
//
//    private boolean isValid() {
//        boolean isValid = true;
//        String name = binding.name.getText().toString();
//        String email = binding.email.getText().toString();
//        String phone = binding.phone.getText().toString();
//
//        if (TextUtils.isEmpty(name)) {
//            isValid = false;
//            binding.nameInputLayout.setError(getString(R.string.field_cannot_empty));
//        }
//
//        if (TextUtils.isEmpty(email)) {
//            isValid = false;
//            binding.emailInputLayout.setError(getString(R.string.field_cannot_empty));
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            binding.emailInputLayout.setError(getString(R.string.invalid_email_format));
//            isValid = false;
//        }
//
//        if (TextUtils.isEmpty(phone)) {
//            isValid = false;
//            binding.phoneInputLayout.setError(getString(R.string.field_cannot_empty));
//        }
//
//        return isValid;
//    }
//
//}
