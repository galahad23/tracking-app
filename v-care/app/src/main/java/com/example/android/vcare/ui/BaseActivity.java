package com.example.android.vcare.ui;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.vcare.R;
import com.example.android.vcare.util.ToastUtil;
import com.example.android.vcare.widget.LoadingDialogFragment;

public abstract class BaseActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Toolbar actionbarToolbar;
    private LoadingDialogFragment loadingDialogFragment;
    private boolean hasBackAlert = false;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        getActionbarToolbar();
    }

    @Nullable
    protected Toolbar getActionbarToolbar() {
        if (actionbarToolbar == null) {
            actionbarToolbar = (Toolbar) findViewById(R.id.actionbar_toolbar);
            if (actionbarToolbar != null) {
                setSupportActionBar(actionbarToolbar);
            }
        }
        return actionbarToolbar;
    }

    protected void setDisplayHomeAsUpEnabled() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setHomeButtonEnabled() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    protected void setTintBackButtonIcon(@ColorRes int colorId, @DrawableRes int drawableId) {
        if (getSupportActionBar() != null) {
            Drawable drawable = ContextCompat.getDrawable(this, drawableId);
            drawable.setColorFilter(ContextCompat.getColor(this, colorId), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(drawable);
        }
    }

    protected void setBackNavigation() {
        if (actionbarToolbar != null) {
            actionbarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public void setToolbarTitle(@StringRes int titleId) {
        if (actionbarToolbar != null) {
            actionbarToolbar.setTitle(titleId);
        }
    }

    public void setToolbarTitle(String title) {
        if (actionbarToolbar != null) {
            actionbarToolbar.setTitle(title);
        }
    }

    public void showLoadingDialog() {
        if (loadingDialogFragment != null) loadingDialogFragment.dismiss();
        loadingDialogFragment = new LoadingDialogFragment();
        loadingDialogFragment.show(getSupportFragmentManager(), "");
    }

    public void dismissLoadingDialog() {
        if (loadingDialogFragment != null && loadingDialogFragment.isAdded()) {
            loadingDialogFragment.dismiss();
        }
    }

    public void setHasBackAlert() {
        this.hasBackAlert = true;
    }

    private static final int TIME_INTERVAL = 2000;
    private long backPressed;

    @Override
    public void onBackPressed() {
        if (hasBackAlert) {
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                finishAffinity();
                return;
            } else {
                ToastUtil.show(this, getString(R.string.back_again_to_exit));
            }
            backPressed = System.currentTimeMillis();
        } else {
            finishAffinity();
        }
    }
}
