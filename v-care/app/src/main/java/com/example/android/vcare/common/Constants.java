package com.example.android.vcare.common;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

public class Constants {
    @StringDef({APIStatus.SUCCESS, APIStatus.FAIL})
    public @interface APIStatus {
        String SUCCESS = "1";
        String FAIL = "2";
    }

    @IntDef({})
    public @interface MenuId {
        int DASHBOARD = 1;
        int MY_PROFILE = 2;
        int SUBSCRIPTION_PLAN = 3;
        int TRACK_MEMBER_DEVICE = 4;
        int SOS = 5;
        int ROUTE_HISTORY = 6;
        int MEMBER_GROUP = 7;
        int FAQ = 8;
        int EMERGENCY_CONTACTS = 9;
        int TERMS_CONDITION = 10;
        int LOGOUT = 11;

        int EDIT_PROFILE = 21;
        int CHANGE_PASSWORD = 22;
    }
}
