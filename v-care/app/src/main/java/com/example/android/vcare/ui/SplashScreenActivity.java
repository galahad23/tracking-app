package com.example.android.vcare.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.android.vcare.R;
import com.example.android.vcare.ui.main.MainActivity;
import com.example.android.vcare.ui.welcome.WelcomeActivity;
import com.example.android.vcare.util.UserHandler;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UserHandler.isLogin(SplashScreenActivity.this)) {
                    MainActivity.start(SplashScreenActivity.this);
                } else {
                    WelcomeActivity.start(SplashScreenActivity.this);
                }
                finish();
            }
        }, 3000);
    }
}
