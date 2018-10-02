package com.example.android.vcare.util;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

public class PermissionUtil {

    public static boolean isPermissionHasGranted(@NonNull int[] grantResults) {
        boolean granted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                granted = false;
            }
        }
        return granted;
    }
}
