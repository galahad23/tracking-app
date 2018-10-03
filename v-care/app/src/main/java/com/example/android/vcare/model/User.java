package com.example.android.vcare.model;

import android.text.TextUtils;

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

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("is_parent")
    @Expose
    private String isParent;
    @SerializedName("plan_id")
    @Expose
    private int planId;
    @SerializedName("reference_id")
    @Expose
    private int referenceId;
    @SerializedName("facebook_id")
    @Expose
    private String facebookId;
    @SerializedName("gmail_id")
    @Expose
    private String googleId;
    @SerializedName("gcm_token")
    @Expose
    private String gcmToken;
    @SerializedName("oauth_token")
    @Expose
    private String oauthToken;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("otp_time")
    @Expose
    private String otpTime;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("postcode")
    @Expose
    private int postcode;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("child_device_no")
    @Expose
    private String childDeviceNo;
    @SerializedName("battery")
    @Expose
    private int battery;
    @SerializedName("wifi_status")
    @Expose
    private String wifiStatus;
    @SerializedName("gps_status")
    @Expose
    private String gpsStatus;
    @SerializedName("child_max_speed")
    @Expose
    private int childMaxSpeed;
    @SerializedName("parent_child_max_speed")
    @Expose
    private int parentChildMaxSpeed;
    @SerializedName("zone")
    @Expose
    private String zone;
    @SerializedName("radius")
    @Expose
    private int radius;
    @SerializedName("time_of_interval")
    @Expose
    private int timeOfInterval;
    @SerializedName("decide_km")
    @Expose
    private String decideKm;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_login")
    @Expose
    private String isLogin;
    @SerializedName("is_online")
    @Expose
    private String isOnline;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("modification_date")
    @Expose
    private String modificationDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("person_name")
    @Expose
    private String personName;
    @SerializedName("emergency_number")
    @Expose
    private String emergencyNumber;


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
        if (TextUtils.isEmpty(parentId)) {
            return id;
        }
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpTime() {
        return otpTime;
    }

    public void setOtpTime(String otpTime) {
        this.otpTime = otpTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getChildDeviceNo() {
        return childDeviceNo;
    }

    public void setChildDeviceNo(String childDeviceNo) {
        this.childDeviceNo = childDeviceNo;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public String getWifiStatus() {
        return wifiStatus;
    }

    public void setWifiStatus(String wifiStatus) {
        this.wifiStatus = wifiStatus;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public int getChildMaxSpeed() {
        return childMaxSpeed;
    }

    public void setChildMaxSpeed(int childMaxSpeed) {
        this.childMaxSpeed = childMaxSpeed;
    }

    public int getParentChildMaxSpeed() {
        return parentChildMaxSpeed;
    }

    public void setParentChildMaxSpeed(int parentChildMaxSpeed) {
        this.parentChildMaxSpeed = parentChildMaxSpeed;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getTimeOfInterval() {
        return timeOfInterval;
    }

    public void setTimeOfInterval(int timeOfInterval) {
        this.timeOfInterval = timeOfInterval;
    }

    public String getDecideKm() {
        return decideKm;
    }

    public void setDecideKm(String decideKm) {
        this.decideKm = decideKm;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
