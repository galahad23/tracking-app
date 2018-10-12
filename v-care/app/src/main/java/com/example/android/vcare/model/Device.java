package com.example.android.vcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("active_child")
    @Expose
    private int activeChild;
    @SerializedName("Inactive_child")
    @Expose
    private int inactiveChild;

    public int getActiveChild() {
        return activeChild;
    }

    public int getInactiveChild() {
        return inactiveChild;
    }
}
