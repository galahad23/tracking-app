package com.example.android.vcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

    @SerializedName("total_group")
    @Expose
    private int totalGroup;

    public int getTotalGroup() {
        return totalGroup;
    }
}
