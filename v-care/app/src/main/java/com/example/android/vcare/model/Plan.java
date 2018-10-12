package com.example.android.vcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Plan {

    @SerializedName("plan_id")
    @Expose
    private int planId;
    @SerializedName("plan_name")
    @Expose
    private String planName;
    @SerializedName("plan_price")
    @Expose
    private String planPrice;
    @SerializedName("route_history")
    @Expose
    private String routeHistory;
    @SerializedName("allowed_device")
    @Expose
    private int allowedDevice;
    @SerializedName("plan_expired")
    @Expose
    private String planExpired;

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(String planPrice) {
        this.planPrice = planPrice;
    }

    public String getRouteHistory() {
        return routeHistory;
    }

    public void setRouteHistory(String routeHistory) {
        this.routeHistory = routeHistory;
    }

    public int getAllowedDevice() {
        return allowedDevice;
    }

    public void setAllowedDevice(int allowedDevice) {
        this.allowedDevice = allowedDevice;
    }

    public String getPlanExpired() {
        return planExpired;
    }

    public void setPlanExpired(String planExpired) {
        this.planExpired = planExpired;
    }
}
