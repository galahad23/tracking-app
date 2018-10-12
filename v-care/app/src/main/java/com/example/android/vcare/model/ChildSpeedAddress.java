package com.example.android.vcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChildSpeedAddress {

    @SerializedName("child_max_speed")
    @Expose
    private int childMaxSpeed;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("battery")
    @Expose
    private int battery;
    @SerializedName("wifi_status")
    @Expose
    private String wifiStatus;
    @SerializedName("gps_status")
    @Expose
    private String gpsStatus;
    @SerializedName("parent_id")
    @Expose
    private int parentId;

    public int getChildMaxSpeed() {
        return childMaxSpeed;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getAddress() {
        return address;
    }

    public int getBattery() {
        return battery;
    }

    public String getWifiStatus() {
        return wifiStatus;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public int getParentId() {
        return parentId;
    }
}
