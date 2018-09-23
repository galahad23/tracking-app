package com.example.android.vcare.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.example.android.vcare.BuildConfig;
import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ViewDrawerFooterBinding;

public class DrawerFooterView extends FrameLayout {
    public DrawerFooterView(@NonNull Context context) {
        super(context);
        init();
    }

    public DrawerFooterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawerFooterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ViewDrawerFooterBinding binding;

    private void init() {
        if (!isInEditMode()) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                    R.layout.view_drawer_footer,
                    this,
                    true);
        }

        binding.appVersion.setText(String.format(getContext().getString(R.string.app_version_s), BuildConfig.VERSION_NAME));
    }
}
