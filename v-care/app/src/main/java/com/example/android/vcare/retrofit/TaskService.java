package com.example.android.vcare.retrofit;

import com.example.android.vcare.model.APIResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TaskService {

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/singup")
    Call<APIResult> signUp(@Field("full_name") String name,
                           @Field("email_address") String email,
                           @Field("password") String password,
                           @Field("phone_number") String phoneNo,
                           @Field("country_code") String countryCode,
                           @Field("country_name") String countryName,
                           @Field("gcm_token") String gcmToken,
                           @Field("device_id") String deviceId,
                           @Field("is_login") int isLogin);
    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/parentfacebooksingup")
    Call<APIResult> facebookSignUp(@Field("facebook_id") String facebookId,
                                   @Field("full_name") String name,
                                   @Field("email_address") String email,
                                   @Field("password") String password,
                                   @Field("mobile_number") String phoneNo,
                                   @Field("country_code") String countryCode,
                                   @Field("country_name") String countryName,
                                   @Field("gcm_token") String gcmToken,
                                   @Field("device_id") String deviceId,
                                   @Field("is_login") int isLogin);
    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/parentgmailsingup")
    Call<APIResult> googleSignUp(@Field("gmail_id") String facebookId,
                                   @Field("full_name") String name,
                                   @Field("email_address") String email,
                                 @Field("password") String password,
                                   @Field("mobile_number") String phoneNo,
                                   @Field("country_code") String countryCode,
                                   @Field("country_name") String countryName,
                                   @Field("gcm_token") String gcmToken,
                                   @Field("device_id") String deviceId,
                                   @Field("is_login") int isLogin);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/login")
    Call<APIResult> login(@Field("email_id") String email,
                          @Field("password") String password,
                          @Field("gcm_token") String gcmToken,
                          @Field("device_id") String deviceId,
                          @Field("is_login") int isLogin);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/parentfacebooklogincheck")
    Call<APIResult> facebookLogin(@Field("facebook_id") String facebookId,
                                  @Field("gcm_token") String gcmToken,
                                  @Field("device_id") String deviceId,
                                  @Field("is_login") int isLogin);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/parentgmaillogincheck")
    Call<APIResult> googleLogin(@Field("gmail_id") String gmailId,
                                @Field("gcm_token") String gcmToken,
                                @Field("device_id") String deviceId,
                                @Field("is_login") int isLogin);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/resendotp")
    Call<APIResult> resendOTP(@Field("parent_id") String parentId);


    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/forgotpassword")
    Call<APIResult> forgotPassword(@Field("email_id") String email_id);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/parentprofile")
    Call<APIResult> getProfile(@Field("parent_id") String parentId,
                               @Field("mobile_token") String mobileToken);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/logout")
    Call<Void> logout(@Field("parent_id") String parentId);

    @FormUrlEncoded
    @Headers({"Accept: application/json"})
    @POST("parent/checkotp")
    Call<APIResult> submitOTP(@Field("parent_id") String parentId,
                         @Field("otp") String otp,
                         @Field("gcm_token") String gcmToken,
                         @Field("device_id") String deviceId);
}
