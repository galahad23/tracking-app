package com.example.android.vcare.job;

import android.content.Context;

import com.birbit.android.jobqueue.Params;
import com.example.android.vcare.common.Constants;
import com.example.android.vcare.event.AccountEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.model.APIResult;
import com.example.android.vcare.model.User;
import com.example.android.vcare.retrofit.ServiceGenerator;
import com.example.android.vcare.retrofit.TaskService;
import com.example.android.vcare.util.EventBusUtil;
import com.example.android.vcare.util.UserHandler;

import retrofit2.Call;
import retrofit2.Response;

public class ResendOTPJob extends BaseJob {

    public ResendOTPJob(int hashCode) {
        super(new Params(Priority.HIGH), hashCode);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Context context = getApplicationContext();
        User user = UserHandler.getUser(context);

        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Call<APIResult> call = taskService.resendOTP(user.getParentId());
        Response<APIResult> execute = call.execute();

        if (execute.isSuccessful()) {
            APIResult result = execute.body();
            if (result.getSuccess().equalsIgnoreCase(Constants.APIStatus.SUCCESS)) {
                EventBusUtil.post(new AccountEvent.OnResendOTP(hashCode));
            } else {
                EventBusUtil.post(new ExceptionEvent(result.getMessage(), hashCode));
            }
            return;
        }
        EventBusUtil.post(new ExceptionEvent(getWentWrongMessage(), hashCode));
    }
}
