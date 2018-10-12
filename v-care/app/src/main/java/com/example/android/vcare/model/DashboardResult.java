package com.example.android.vcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardResult {
    @SerializedName("success")
    @Expose
    private int success;
    @SerializedName("plan")
    @Expose
    private Plan plan;
    @SerializedName("device")
    @Expose
    private List<Device> device = null;
    @SerializedName("group")
    @Expose
    private List<Group> group = null;
    @SerializedName("child_speed_address")
    @Expose
    private List<ChildSpeedAddress> childSpeedAddress = null;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public List<Device> getDevice() {
        return device;
    }

    public void setDevice(List<Device> device) {
        this.device = device;
    }

    public List<Group> getGroup() {
        return group;
    }

    public void setGroup(List<Group> group) {
        this.group = group;
    }

    public List<ChildSpeedAddress> getChildSpeedAddress() {
        return childSpeedAddress;
    }

    public void setChildSpeedAddress(List<ChildSpeedAddress> childSpeedAddress) {
        this.childSpeedAddress = childSpeedAddress;
    }
}
