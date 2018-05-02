package com.example.android.vcare.model;

import android.content.Context;
import android.util.Log;

import com.example.android.vcare.Constant;

/**
 * Created by Mtoag on 5/11/2016.
 */
public class User_function {




    DatabaseHandler db;



    public boolean isUserLoggedIn(Context context) {
        db = new DatabaseHandler(context);
        int count = db.getRowCount();

        Log.e("ufff", "hyyyyy" + count);
        if (count > 0) {
            // user logged in
            return true;
        }
        return false;
    }






  //   Function to logout user Reset Database

    public boolean logoutUser(Context context) {
         db = new DatabaseHandler(context);
        db.resetTables("riderinfo");
        Constant.facebook_id="";
        Constant.google_id="";
        return true;
    }




}

