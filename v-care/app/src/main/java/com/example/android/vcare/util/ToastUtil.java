package com.example.android.vcare.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by etc03 on 09/11/2017.
 */

public class ToastUtil {

    public static void show(Context context, @StringRes int strRes) {
        Toast.makeText(context, context.getString(strRes), Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
