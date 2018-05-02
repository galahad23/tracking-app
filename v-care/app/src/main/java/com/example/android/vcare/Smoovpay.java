package com.example.android.vcare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.User_Detail;
import com.example.android.vcare.model.User_function;

import java.util.ArrayList;
import java.util.List;

public class Smoovpay extends AppCompatActivity {
    User_function user_function = new User_function();
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    Toolbar toolbar;
    WebView webView;
    ProgressBar bar;
    ProgressDialog pDialog;
    String plan_id="",parent_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoovpay);


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Subscription Plan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        plan_id  = intent.getStringExtra("plan_id");


        databaseHandler = new DatabaseHandler(getApplicationContext());
        feeditem = new ArrayList<User_Detail>();
        Cursor cursor = databaseHandler.get_rider_detail();
        if (cursor != null){
            cursor.moveToFirst();
            for (int i =0 ; i< cursor.getCount(); i++){
                User_Detail detail = new User_Detail();
                detail.setId(cursor.getString(5));
                detail.setAddress(cursor.getString(6));

                feeditem.add(detail);
                cursor.moveToNext();
            }
            cursor.close();
        }

        for (User_Detail userDetail : feeditem){
            parent_id  = userDetail.getId();
        }

        Log.e("parent_id", "parent_id>>" + parent_id);




        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSetting = webView.getSettings();
        pDialog = new ProgressDialog(Smoovpay.this);
        pDialog.setMessage("Please wait...");
        pDialog.show();


        webSetting.setJavaScriptEnabled(true);
        webSetting.setPluginState(WebSettings.PluginState.ON);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
       // webView.loadUrl(Config.YOUR_API_URL + "smoovpay/" + plan_id);

       webView.loadUrl(Config.YOUR_API_URL + "smoovpay?" + "plan_id=" + plan_id +
                "&parent_id=" + parent_id);
        webView.setWebViewClient(new myWebClient());

    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);

            Log.e("url","url???"+url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            if (!pDialog.isShowing()) {
                pDialog.show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finishAffinity();
           // showalert();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void showalert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        finishAffinity();
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Warning");
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finishAffinity();
                //  showalert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
