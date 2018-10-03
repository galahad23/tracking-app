package com.example.android.vcare.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ActivityTermsConditionBinding;
import com.example.android.vcare.ui.BaseActivity;


public class TermsConditionActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, TermsConditionActivity.class);
        context.startActivity(starter);
    }

    private ActivityTermsConditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_condition);
        setToolbarTitle(R.string.term_conditions);
        setTintBackButtonIcon(R.color.white, R.drawable.ic_back_black_24dp);
        setDisplayHomeAsUpEnabled();
        setBackNavigation();

        showLoadingDialog();
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setDisplayZoomControls(false);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);

        binding.webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                dismissLoadingDialog();
            }
        });
        binding.webView.loadUrl("http://go4drupal.com/gps_tracker/termcondition/mobile");
    }
}
