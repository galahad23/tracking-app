package com.example.android.vcare.job;

import android.content.Context;

import com.birbit.android.jobqueue.Params;
import com.example.android.vcare.common.Constants;
import com.example.android.vcare.event.AccountEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.fcm.DeviceInfo;
import com.example.android.vcare.fcm.RegistrationIntentService;
import com.example.android.vcare.model.APIResult;
import com.example.android.vcare.retrofit.ServiceGenerator;
import com.example.android.vcare.retrofit.TaskService;
import com.example.android.vcare.util.EventBusUtil;
import com.example.android.vcare.util.UserHandler;

import retrofit2.Call;
import retrofit2.Response;

public class ForgotPasswordJob extends BaseJob {

    private final String email;

    public ForgotPasswordJob(String email, int hashCode) {
        super(new Params(Priority.HIGH), hashCode);
        this.email = email;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Call<APIResult> call = taskService.forgotPassword(email);
        Response<APIResult> execute = call.execute();

        if (execute.isSuccessful()) {
            APIResult result = execute.body();
            if (result.getSuccess().equalsIgnoreCase(Constants.APIStatus.SUCCESS)) {
                EventBusUtil.post(new AccountEvent.OnForgotPassword(result.getMessage(), hashCode));
            } else {
                EventBusUtil.post(new ExceptionEvent(result.getMessage(), hashCode));
            }
            return;
        }
        EventBusUtil.post(new ExceptionEvent(getWentWrongMessage(), hashCode));
    }
}
