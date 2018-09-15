package com.example.android.vcare.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ActivityWelcomeBinding;
import com.example.android.vcare.model2.Banner;
import com.example.android.vcare.ui.BaseActivity;
import com.example.android.vcare.ui.login.LoginActivity;
import com.example.android.vcare.ui.login.SignUpActivity;
import com.example.android.vcare.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, WelcomeActivity.class);
        context.startActivity(starter);
    }

    private ActivityWelcomeBinding binding;
    //    private ViewPager viewPager;
    private WelcomeBannerAdapter bannerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

//        prefManager = new PrefManager(this);
//        if (!prefManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
//            finish();
//        }

//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

//        layouts = new int[]{
//                R.layout.view_welcome_banner,
//                R.layout.dashboard_slide2,
//                R.layout.dashboard_slide3,
//                R.layout.dashboard_slide4,
//                R.layout.dashboard_slide5};

//        changeStatusBarColor();

        initWelcomeBanner();

        binding.login.setOnClickListener(this);
        binding.signUp.setOnClickListener(this);
    }

    private void initWelcomeBanner() {
        bannerAdapter = new WelcomeBannerAdapter(this, getBannerList());
        binding.viewPager.setAdapter(bannerAdapter);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                int current = binding.viewPager.getCurrentItem() + 1;
                if (current == bannerAdapter.getCount()) {
                    current = 0;
                }
                binding.viewPager.setCurrentItem(current, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3500);

        binding.indicator.setViewPager(binding.viewPager);
    }

    private List<Banner> getBannerList() {
        final List<Banner> bannerList = new ArrayList<>();
        bannerList.add(new Banner().setDrawableId(R.drawable.welcome_banner1).setDesciption(getString(R.string.welcome_banner_desc1)));
        bannerList.add(new Banner().setDrawableId(R.drawable.welcome_banner2).setDesciption(getString(R.string.welcome_banner_desc2)));
        bannerList.add(new Banner().setDrawableId(R.drawable.welcome_banner3).setDesciption(getString(R.string.welcome_banner_desc3)));
        bannerList.add(new Banner().setDrawableId(R.drawable.welcome_banner4).setDesciption(getString(R.string.welcome_banner_desc4)));
        bannerList.add(new Banner().setDrawableId(R.drawable.welcome_banner5).setDesciption(getString(R.string.welcome_banner_desc5)));
        return bannerList;
    }

    @Override
    public void onClick(View view) {
        if (view == binding.login) {
            LoginActivity.start(this);
        } else if (view == binding.signUp) {
            SignUpActivity.start(this);
        }
    }

    private static final int TIME_INTERVAL = 2000;
    private long backPressed;

    @Override
    public void onBackPressed() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            finishAffinity();
            return;
        } else {
            ToastUtil.show(this, getString(R.string.back_again_to_exit));
        }
        backPressed = System.currentTimeMillis();
    }

//    private int getItem(int i) {
//        return viewPager.getCurrentItem() + i;
//    }

//    private void launchHomeScreen() {
//        prefManager.setFirstTimeLaunch(true);
//        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
//        finish();
//    }
//
//    private void changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//    }

//    public class MyViewPagerAdapter extends PagerAdapter {
//        private LayoutInflater layoutInflater;
//
//        public MyViewPagerAdapter() {
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View view = layoutInflater.inflate(layouts[position], container, false);
//            container.addView(view);
//
//            return view;
//        }
//
//        @Override
//        public int getCount() {
//            return layouts.length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object obj) {
//            return view == obj;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            container.removeView(view);
//        }
//    }
}