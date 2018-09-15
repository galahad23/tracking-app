package com.example.android.vcare.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by etc03 on 09/11/2017.
 */

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegistrationIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        String token = getRegistrationId();
//        Log.i(TAG, "FCM Registration Token: " + token);
//        ServerUtil.register(getApplicationContext(), token);
    }

    public static String getRegistrationId() {
        return FirebaseInstanceId.getInstance().getToken();
    }
}
