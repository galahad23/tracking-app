package com.example.android.vcare;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.example.android.vcare.model.UserData;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import io.fabric.sdk.android.Fabric;

public class AppController extends Controller {

     public static final String TAG = AppController.class.getSimpleName();

     private RequestQueue mRequestQueue;
     private ImageLoader mImageLoader;

     public static AppController mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
     public void onCreate() {
         super.onCreate();
         mInstance = this;

        Fabric.with(this, new Crashlytics());
     }

     public static synchronized AppController getInstance() {
         if (mInstance == null) {
             mInstance = new AppController();
         }
         return mInstance;
     }

     public RequestQueue getRequestQueue() {
         if (mRequestQueue == null) {
             mRequestQueue = Volley.newRequestQueue(getApplicationContext());
         }

         return mRequestQueue;
     }

     public <T> void addToRequestQueue(Request<T> req, String tag) {
         // set the default tag if tag is empty
         req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
         getRequestQueue().add(req);
     }

     public <T> void addToRequestQueue(Request<T> req) {
         req.setTag(TAG);
         getRequestQueue().add(req);
     }

     public void cancelPendingRequests(Object tag) {
         if (mRequestQueue != null) {
             mRequestQueue.cancelAll(tag);
         }
     }

 }

class Controller extends Application {

   //Activity activity = (Activity) getApplicationContext();
   private final int MAX_ATTEMPTS = 5;
   private final int BACKOFF_MILLI_SECONDS = 2000;
   private final Random random = new Random();

   private ArrayList<UserData> UserDataArr = new ArrayList<UserData>();



   // Issue a POST request to the server.

   private static void post(String endpoint, Map<String, String> params)
           throws IOException {

       URL url;
       try {

           url = new URL(endpoint);

       } catch (MalformedURLException e) {
           throw new IllegalArgumentException("invalid url: " + endpoint);
       }

       StringBuilder bodyBuilder = new StringBuilder();
       Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

       // constructs the POST body using the parameters
       while (iterator.hasNext()) {
           Map.Entry<String, String> param = iterator.next();
           bodyBuilder.append(param.getKey()).append('=')
                   .append(param.getValue());
           if (iterator.hasNext()) {
               bodyBuilder.append('&');
           }
       }

       String body = bodyBuilder.toString();

       Log.v(Config.TAG, "Posting '" + body + "' to " + url);

       byte[] bytes = body.getBytes();

       HttpURLConnection conn = null;
       try {

           Log.e("URL", "> " + url);

           conn = (HttpURLConnection) url.openConnection();
           conn.setDoOutput(true);
           conn.setUseCaches(false);
           conn.setFixedLengthStreamingMode(bytes.length);
           conn.setRequestMethod("POST");
           conn.setRequestProperty("Content-Type",
                   "application/x-www-form-urlencoded;charset=UTF-8");
           // post the request
           OutputStream out = conn.getOutputStream();
           out.write(bytes);
           out.close();

           // handle the response
           int status = conn.getResponseCode();

           // If response is not success
           if (status != 200) {

               throw new IOException("Post failed with error code " + status);
           }
       } finally {
           if (conn != null) {
               conn.disconnect();
           }
       }
   }


   // Notifies UI to display a message.
   void displayRegistrationMessageOnScreen(Context context, String message) {

       Intent intent = new Intent(Config.DISPLAY_REGISTRATION_MESSAGE_ACTION);
       intent.putExtra(Config.EXTRA_MESSAGE, message);

       // Send Broadcast to Broadcast receiver with message
       context.sendBroadcast(intent);

   }

   // Notifies UI to display a message.
   void displayMessageOnScreen(Context context, String title, String message, String imei) {

       Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
       intent.putExtra(Config.EXTRA_MESSAGE, message);
       intent.putExtra("name", title);
       intent.putExtra("imei", imei);
       // Send Broadcast to Broadcast receiver with message
       context.sendBroadcast(intent);

   }

}
