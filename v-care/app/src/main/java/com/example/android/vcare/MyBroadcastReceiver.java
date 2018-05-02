package com.example.android.vcare;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

/**
 * Created by mukeesh on 7/3/2017.
 */

public class MyBroadcastReceiver extends GCMBroadcastReceiver
{
    @Override
    protected String getGCMIntentServiceClassName(Context context)
    {
        return GCMIntentService.class.getName();
    }
}