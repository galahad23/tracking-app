//package com.example.android.vcare;
//
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import com.example.android.vcare.model.UserData;
//import com.example.android.vcare.pending.Config;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Random;
//
//public abstract class Controller extends Application {
//    private final int MAX_ATTEMPTS = 5;
//    private final int BACKOFF_MILLI_SECONDS = 2000;
//    private final Random random = new Random();
//
//    private ArrayList<UserData> UserDataArr = new ArrayList<UserData>();
//
//
//    // Issue a POST request to the server.
//
//    private static void post(String endpoint, Map<String, String> params)
//            throws IOException {
//
//        URL url;
//        try {
//            url = new URL(endpoint);
//
//        } catch (MalformedURLException e) {
//            throw new IllegalArgumentException("invalid url: " + endpoint);
//        }
//
//        StringBuilder bodyBuilder = new StringBuilder();
//        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
//
//        // constructs the POST body using the parameters
//        while (iterator.hasNext()) {
//            Map.Entry<String, String> param = iterator.next();
//            bodyBuilder.append(param.getKey()).append('=')
//                    .append(param.getValue());
//            if (iterator.hasNext()) {
//                bodyBuilder.append('&');
//            }
//        }
//
//        String body = bodyBuilder.toString();
//
//        Log.v(Config.TAG, "Posting '" + body + "' to " + url);
//
//        byte[] bytes = body.getBytes();
//
//        HttpURLConnection conn = null;
//        try {
//
//            Log.e("URL", "> " + url);
//
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setUseCaches(false);
//            conn.setFixedLengthStreamingMode(bytes.length);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded;charset=UTF-8");
//            // post the request
//            OutputStream out = conn.getOutputStream();
//            out.write(bytes);
//            out.close();
//
//            // handle the response
//            int status = conn.getResponseCode();
//
//            // If response is not success
//            if (status != 200) {
//
//                throw new IOException("Post failed with error code " + status);
//            }
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//    }
//
////    // Notifies UI to display a message.
////    void displayRegistrationMessageOnScreen(Context context, String message) {
////
////        Intent intent = new Intent(Config.DISPLAY_REGISTRATION_MESSAGE_ACTION);
////        intent.putExtra(Config.EXTRA_MESSAGE, message);
////
////        // Send Broadcast to Broadcast receiver with message
////        context.sendBroadcast(intent);
////
////    }
////
////    // Notifies UI to display a message.
////    void displayMessageOnScreen(Context context, String title, String message, String imei) {
////
////        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
////        intent.putExtra(Config.EXTRA_MESSAGE, message);
////        intent.putExtra("name", title);
////        intent.putExtra("imei", imei);
////        // Send Broadcast to Broadcast receiver with message
////        context.sendBroadcast(intent);
////
////    }
//}
