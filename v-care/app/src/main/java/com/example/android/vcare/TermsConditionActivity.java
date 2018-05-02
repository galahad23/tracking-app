package com.example.android.vcare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class TermsConditionActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView termsConditions;
    private ProgressDialog progressBar;
    private static final String TAG = "WebView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = ProgressDialog.show(TermsConditionActivity.this, "", "Loading...");
        progressBar.setCancelable(false);
        termsConditions = (WebView) findViewById(R.id.termsConditions);
        termsConditions.getSettings().setJavaScriptEnabled(true);
        termsConditions.setWebViewClient(new WebViewClient());
        termsConditions.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        termsConditions.getSettings().setSupportZoom(true);
        termsConditions.getSettings().setBuiltInZoomControls(true);
        termsConditions.getSettings().setDisplayZoomControls(false);
        termsConditions.getSettings().setUseWideViewPort(true);
        termsConditions.getSettings().setLoadWithOverviewMode(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termsConditions.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();

                }
            }
        });
        termsConditions.loadUrl(Config.TERMS);
        Log.e("weburl","weburl>>>"+Config.TERMS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
