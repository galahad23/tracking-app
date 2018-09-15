package com.example.android.vcare.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent_id")
    @Expose
    private String parentId;
    @SerializedName("otp_status")
    @Expose
    private String otpStatus;
    private String password;
    private String phoneNo;
    private String countryCode;
    private String countryName;
    private String facebookId;
    private String googleId;

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static User deserialize(String json) {
        return new Gson().fromJson(json, User.class);
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public User setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getOtpStatus() {
        return otpStatus;
    }

    public User setOtpStatus(String otpStatus) {
        this.otpStatus = otpStatus;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public User setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public User setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getCountryName() {
        return countryName;
    }

    public User setCountryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public User setFacebookId(String facebookId) {
        this.facebookId = facebookId;
        return this;
    }

    public String getGoogleId() {
        return googleId;
    }

    public User setGoogleId(String googleId) {
        this.googleId = googleId;
        return this;
    }
}
