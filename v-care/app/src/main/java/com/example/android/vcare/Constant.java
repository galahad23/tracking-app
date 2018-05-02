package com.example.android.vcare;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Mtoag on 10/22/2016.
 */
public class Constant {

    public static boolean hasConnection(Context ct) {
        ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    public static String check_fb = "";
    public static String strfullname = "";
    public static String stremail = "";
    public static String strmobile = "";
    public static String strpassword = "";
    public static String facebook_id = "";
    public static String google_id = "";
    public static String notification = "";
    public static String plan_name = "";
    public static String plan_price = "";
    public static String plan_id = "";
    public static String view_route = "";
    public static String image = "";
    public static String group_id = "";
    public static String contactlist = "";
    public static String child_id;
    public static String parent_id;
    public static String parent_child_id;
    public static String LATITUDE = "";
    public static String LONGITUDE = "";
}
