package com.example.android.vcare.util;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.android.vcare.model.User;

public class UserHandler {

    private static final String USER = "USER";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    public static boolean isLogin(@NonNull Context context) {
        return !TextUtils.isEmpty(getToken(context));
    }

    public static void setToken(@NonNull Context context, String token) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(ACCESS_TOKEN, token)
                .apply();
    }

    public static String getToken(@NonNull Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(ACCESS_TOKEN, "");
    }

    public static void setUser(@NonNull Context context, User user) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(USER, user.toJson())
                .apply();
    }

    public static User getUser(@NonNull Context context) {
        return User.deserialize(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(USER, ""));
    }
}
