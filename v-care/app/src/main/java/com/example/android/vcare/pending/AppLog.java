package com.example.android.vcare.pending;

import android.util.Log;

/**
 * Created by Mtoag on 11/2/2016.
 */
public class AppLog {
    private static final String APP_TAG = "AudioRecorder";

    public static int logString(String message) {
        return Log.i(APP_TAG, message);
    }
}