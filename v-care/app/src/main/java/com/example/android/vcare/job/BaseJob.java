package com.example.android.vcare.job;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.android.vcare.R;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.util.EventBusUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public abstract class BaseJob extends Job {

    protected final int hashCode;

    protected String getWentWrongMessage() {
        return getApplicationContext().getString(R.string.something_went_wrong);
    }

    protected BaseJob(Params params, int hashCode) {
        super(params);
        this.hashCode = hashCode;
    }

    protected boolean shouldRetry(Throwable throwable) {
        return throwable instanceof UnknownHostException || throwable instanceof TimeoutException;
    }

    protected void handleThrowable(Throwable throwable, int hashCode) {
        Context context = getApplicationContext();

        if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) {
            EventBusUtil.post(new ExceptionEvent(context.getString(R.string.server_busy), hashCode));
        } else if (throwable instanceof ConnectException || throwable instanceof UnknownHostException) {
            EventBusUtil.post(new ExceptionEvent(context.getString(R.string.check_connection), hashCode));
        } else {
            EventBusUtil.post(new ExceptionEvent(throwable.getCause().getMessage(), hashCode));
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        handleThrowable(throwable, hashCode);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (shouldRetry(throwable)) {
            return RetryConstraint.createExponentialBackoff(runCount, 1000);
        }

        return RetryConstraint.CANCEL;
    }


    @Override
    protected int getRetryLimit() {
        return 2;
    }
}
