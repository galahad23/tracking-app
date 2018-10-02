package com.example.android.vcare.job;

import com.birbit.android.jobqueue.Params;
import com.example.android.vcare.common.Constants;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.event.SettingEvent;
import com.example.android.vcare.model.APIResult;
import com.example.android.vcare.retrofit.ServiceGenerator;
import com.example.android.vcare.retrofit.TaskService;
import com.example.android.vcare.util.EventBusUtil;

import retrofit2.Call;
import retrofit2.Response;

public class GetFaqJob extends BaseJob {
    public GetFaqJob(int hashCode) {
        super(new Params(Priority.HIGH), hashCode);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Call<APIResult> call = taskService.getFaq();
        Response<APIResult> execute = call.execute();

        if (execute.isSuccessful()) {
            APIResult result = execute.body();
            if (result.getSuccess().equalsIgnoreCase(Constants.APIStatus.SUCCESS)) {
                EventBusUtil.post(new SettingEvent.OnGetFaq(result.getFaqList(), hashCode));
            } else {
                EventBusUtil.post(new ExceptionEvent(result.getMessage(), hashCode));
            }
            return;
        }
        EventBusUtil.post(new ExceptionEvent(getWentWrongMessage(), hashCode));
    }
}
