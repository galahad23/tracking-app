package com.example.android.vcare.widget;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

import com.example.android.vcare.MyApplication;
import com.example.android.vcare.R;

import java.lang.ref.WeakReference;

/*
 * This CountDownTimer class runs indefinitely
 * irregardless of the activity is onPause or
 * even being destroyed
 */
public class Countdown extends CountDownTimer {

    /*
     * we hold a weak reference to the button due to this
     * being an inner class, having a hard reference will cause
     * the outer class to never be destroyed due to having still
     * having a reference.
     *
     * Using a static inner class would not have such implicit reference
     */
    private WeakReference<Button> ref;
    boolean isFinish = false;

    public Countdown(long millisInFuture, Button button) {
        super(millisInFuture, 1000);
        ref = new WeakReference<Button>(button);
        button.setEnabled(false);
        button.setTextColor(0xFFFFFFFF);
    }

    public void onResume(Button button) {
        ref = new WeakReference<Button>(button);
        if (isFinish) {
            ref.get().setEnabled(true);
            ref.get().setTextColor(Color.WHITE);

        } else {
            ref.get().setEnabled(false);
            ref.get().setTextColor(0xFFFFFFFF);
        }
    }

    public boolean isFinish() {
        return isFinish;
    }

    @Override
    public void onFinish() {
        Button button = ref.get();
        if (button != null) {
            button.setText(MyApplication.getInstance().getString(R.string.resend));
            button.setTextColor(Color.WHITE);
            button.setEnabled(true);
        }
        isFinish = true;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (ref.get() != null) {
            ref.get().setText(millisUntilFinished / 1000 + " sec");
        }
    }

}
