package com.example.android.vcare.util;

import android.content.Context;
import android.util.Log;

import com.example.android.vcare.model2.DatabaseHandler;
import com.example.android.vcare.pending.Constant;

/**
 * Created by Mtoag on 5/11/2016.
 */
public class UserHandler2 {

    DatabaseHandler db;

    public boolean isUserLoggedIn(Context context) {
        db = new DatabaseHandler(context);
        int count = db.getRowCount();

        Log.e("ufff", "hyyyyy" + count);

        return count > 0;
    }

    public boolean logoutUser(Context context) {
        db = new DatabaseHandler(context);
        db.resetTables("riderinfo");
        Constant.facebook_id = "";
        Constant.google_id = "";
        return true;
    }
}

