package com.example.android.vcare.job;

import android.content.Context;

import com.birbit.android.jobqueue.Params;
import com.example.android.vcare.common.Constants;
import com.example.android.vcare.event.DashboardEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.model.DashboardResult;
import com.example.android.vcare.model.User;
import com.example.android.vcare.retrofit.ServiceGenerator;
import com.example.android.vcare.retrofit.TaskService;
import com.example.android.vcare.util.EventBusUtil;
import com.example.android.vcare.util.UserHandler;

import retrofit2.Call;
import retrofit2.Response;

public class GetDashboardJob extends BaseJob {

    public GetDashboardJob(int hashCode) {
        super(new Params(Priority.HIGH), hashCode);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Context context = getApplicationContext();
        User user = UserHandler.getUser(context);
        String token = UserHandler.getToken(context);

        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Call<DashboardResult> call = taskService.getDashboard(user.getId(), token);
        Response<DashboardResult> execute = call.execute();

        if (execute.isSuccessful()) {
            DashboardResult result = execute.body();
            if (result.getSuccess() == Integer.parseInt(Constants.APIStatus.SUCCESS)) {
                EventBusUtil.post(new DashboardEvent.OnGetDashboard(result, hashCode));
//            } else {
//                EventBusUtil.post(new ExceptionEvent(result.getMessage(), hashCode));
            }
            return;
        }
        EventBusUtil.post(new ExceptionEvent(getWentWrongMessage(), hashCode));
    }
}
