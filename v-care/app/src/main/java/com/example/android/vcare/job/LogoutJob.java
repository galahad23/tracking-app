package com.example.android.vcare.job;

import android.content.Context;

import com.birbit.android.jobqueue.Params;
import com.example.android.vcare.model.User;
import com.example.android.vcare.retrofit.ServiceGenerator;
import com.example.android.vcare.retrofit.TaskService;
import com.example.android.vcare.util.UserHandler;

import retrofit2.Call;

public class LogoutJob extends BaseJob {

    public LogoutJob(int hashCode) {
        super(new Params(Priority.HIGH), hashCode);
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        Context context = getApplicationContext();
        User user = UserHandler.getUser(context);
        String userId = user.getParentId();

        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Call<Void> call = taskService.logout(userId);
        call.execute();
    }
}
