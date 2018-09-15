package com.example.android.vcare.util;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtil {

    public static void register(Object object) {
        if (!EventBus.getDefault().isRegistered(object)) {
            EventBus.getDefault().register(object);
        }
    }

    public static void unregister(Object object) {
        if (EventBus.getDefault().isRegistered(object)) {
            EventBus.getDefault().unregister(object);
        }
    }

    public static void post(Object object) {
        EventBus.getDefault().post(object);
    }
}
