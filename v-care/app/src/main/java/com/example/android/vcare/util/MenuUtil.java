package com.example.android.vcare.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.example.android.vcare.R;
import com.example.android.vcare.common.Constants;


public class MenuUtil {
    public static String getMenuNameFromObject(Context context, Object menuId) {
        if (TextUtils.isDigitsOnly(menuId.toString())) {
            return getMenuNameFromId(context, (Integer) menuId);
        } else {
            return "";
        }
    }

    //try not to use enum which will consume a lot of user device memory
    @SuppressLint("SwitchIntDef")
    public static String getMenuNameFromId(Context context, @Constants.MenuId int menuId) {
        int stringId;
        switch (menuId) {
            case Constants.MenuId.DASHBOARD:
                stringId = R.string.dashboard;
                break;
            case Constants.MenuId.MY_PROFILE:
                stringId = R.string.my_profile;
                break;
            case Constants.MenuId.SUBSCRIPTION_PLAN:
                stringId = R.string.subscription_plan;
                break;
            case Constants.MenuId.TRACK_MEMBER_DEVICE:
                stringId = R.string.track_member_device;
                break;
            case Constants.MenuId.SOS:
                stringId = R.string.sos;
                break;
            case Constants.MenuId.ROUTE_HISTORY:
                stringId = R.string.route_history;
                break;
            case Constants.MenuId.MEMBER_GROUP:
                stringId = R.string.member_group;
                break;
            case Constants.MenuId.FAQ:
                stringId = R.string.faq;
                break;
            case Constants.MenuId.EMERGENCY_CONTACTS:
                stringId = R.string.emergency_contacts;
                break;
            case Constants.MenuId.TERMS_CONDITION:
                stringId = R.string.term_conditions;
                break;
            case Constants.MenuId.LOGOUT:
                stringId = R.string.logout;
                break;
            //Sub Menu
            case Constants.MenuId.EDIT_PROFILE:
                stringId = R.string.edit_profile;
                break;
            case Constants.MenuId.CHANGE_PASSWORD:
                stringId = R.string.change_password;
                break;
            default:
                stringId = R.string.logout;
                break;
        }
        return context.getString(stringId);
    }
}
