package com.example.android.vcare.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ViewDrawerHeaderBinding;
import com.example.android.vcare.model.User;
import com.example.android.vcare.util.ImageLoader;
import com.example.android.vcare.util.UserHandler;

public class DrawerHeaderView extends FrameLayout {
    public DrawerHeaderView(@NonNull Context context) {
        super(context);
        init();
    }

    public DrawerHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawerHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ViewDrawerHeaderBinding binding;

    private DrawerHeaderView init() {
        if (!isInEditMode()) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                    R.layout.view_drawer_header,
                    this,
                    true);
        }

        update(UserHandler.getUser(getContext()));
        return this;
    }

    public DrawerHeaderView update(User user) {
        binding.name.setText(user.getName());
        if (TextUtils.isEmpty(user.getProfileImage())) {

        } else {
            ImageLoader.glideImageLoad(binding.image, R.drawable.user_placeholder, user.getProfileImage());
        }
        return this;
    }
}
