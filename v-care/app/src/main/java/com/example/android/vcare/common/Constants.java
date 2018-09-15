package com.example.android.vcare.common;

import android.support.annotation.StringDef;

public class Constants {
    @StringDef({APIStatus.SUCCESS, APIStatus.FAIL})
    public @interface APIStatus {
        String SUCCESS = "1";
        String FAIL = "2";
    }
}
