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

public class UpdateEmergencyNumberJob extends BaseJob {
    private final String personName;
    private final String emergencyNumber;
    private final int hashCode;

    public UpdateEmergencyNumberJob(String personName, String emergencyNumber, int hashCode) {
        super(new Params(Priority.HIGH), hashCode);
        this.personName = personName;
        this.emergencyNumber = emergencyNumber;
        this.hashCode = hashCode;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        Context context = getApplicationContext();
        User user = UserHandler.getUser(context);

        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Call<APIResult> call = taskService.updateEmergencyNumber(user.getId(),
                personName,
                emergencyNumber);
        Response<APIResult> execute = call.execute();

        if (execute.isSuccessful()) {
            APIResult result = execute.body();
            if (result.getSuccess().equalsIgnoreCase(Constants.APIStatus.SUCCESS)) {
                EventBusUtil.post(new AccountEvent.OnUpdateEmergencyContact(result.getMessage(), hashCode));
            } else {
                EventBusUtil.post(new ExceptionEvent(result.getMessage(), hashCode));
            }
            return;
        }
        EventBusUtil.post(new ExceptionEvent(getWentWrongMessage(), hashCode));
    }
}
