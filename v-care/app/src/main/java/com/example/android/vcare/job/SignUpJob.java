package com.example.android.vcare.job;

import android.content.Context;
import android.text.TextUtils;

import com.birbit.android.jobqueue.Params;
import com.example.android.vcare.common.Constants;
import com.example.android.vcare.event.AccountEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.fcm.DeviceInfo;
import com.example.android.vcare.fcm.RegistrationIntentService;
import com.example.android.vcare.model.APIResult;
import com.example.android.vcare.model.User;
import com.example.android.vcare.retrofit.ServiceGenerator;
import com.example.android.vcare.retrofit.TaskService;
import com.example.android.vcare.util.EventBusUtil;

import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Response;

public class SignUpJob extends BaseJob {

    private final int id;
    private static final AtomicInteger jobCounter = new AtomicInteger(0);
    private final User user;

    public SignUpJob(User user, int hashCode) {
        super(new Params(Priority.HIGH), hashCode);
        this.user = user;
        id = jobCounter.incrementAndGet();
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        if (id != jobCounter.get()) {
            return;
        }
        Context context = getApplicationContext();

        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Call<APIResult> call;
        if (!TextUtils.isEmpty(user.getFacebookId())) {
            call = taskService.facebookSignUp(user.getFacebookId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPhoneNumber(),
                    user.getCountryCode(),
                    user.getCountryName(),
                    RegistrationIntentService.getRegistrationId(),
                    DeviceInfo.id(context),
                    0);
        } else if (!TextUtils.isEmpty(user.getGoogleId())) {
            call = taskService.googleSignUp(user.getGoogleId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPhoneNumber(),
                    user.getCountryCode(),
                    user.getCountryName(),
                    RegistrationIntentService.getRegistrationId(),
                    DeviceInfo.id(context),
                    0);
        } else {
            call = taskService.signUp(user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getPhoneNumber(),
                    user.getCountryCode(),
                    user.getCountryName(),
                    RegistrationIntentService.getRegistrationId(),
                    DeviceInfo.id(context),
                    0);
        }

        Response<APIResult> execute = call.execute();

        if (execute.isSuccessful()) {
            APIResult result = execute.body();
            if (result.getSuccess().equalsIgnoreCase(Constants.APIStatus.SUCCESS)) {
                EventBusUtil.post(new AccountEvent.OnSignUp(hashCode));
            } else {
                EventBusUtil.post(new ExceptionEvent(result.getMessage(), hashCode));
            }
            return;
        }
        EventBusUtil.post(new ExceptionEvent(getWentWrongMessage(), hashCode));
    }
}
