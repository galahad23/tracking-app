package com.example.android.vcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIResult {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("mobile_token")
    @Expose
    private String mobileToken;
    @SerializedName("parentinfo")
    @Expose
    private  List<User> parentInfo = null;
    @SerializedName("text")
    @Expose
    private String message = null;
//    @SerializedName("profile")
//    @Expose
//    private  User profile = null;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMobileToken() {
        return mobileToken;
    }

    public void setMobileToken(String mobileToken) {
        this.mobileToken = mobileToken;
    }

    public List<User> getParentInfo() {
        return parentInfo;
    }

    public User getUser() {
        if (parentInfo == null || parentInfo.size() == 0) {
            return new User();
        }
        return parentInfo.get(0);
    }

    public void setParentInfo(List<User> parentInfo) {
        this.parentInfo = parentInfo;
    }

    public String getMessage() {
        return message;
    }

//    public User getProfile() {
//        return profile;
//    }
}
