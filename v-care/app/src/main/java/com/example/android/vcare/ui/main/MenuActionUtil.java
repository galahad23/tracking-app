package com.example.android.vcare.ui.main;

import android.app.Activity;
import android.content.DialogInterface;

import com.example.android.vcare.MyApplication;
import com.example.android.vcare.R;
import com.example.android.vcare.job.LogoutJob;
import com.example.android.vcare.ui.welcome.WelcomeActivity;
import com.example.android.vcare.util.UserHandler;
import com.example.android.vcare.util.Util;


public class MenuActionUtil {

    public static void logout(final Activity activity) {
        Util.showYesNoDisableCancelAlertDialog(activity,
                null,
                activity.getString(R.string.logout_warning),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserHandler.clear(activity);
                        MyApplication.addJobInBackground(new LogoutJob(0));
                        WelcomeActivity.start(activity);
                        activity.finish();
                    }
                });

    }
}
