package com.example.android.vcare;

/**
 * Created by Mtoag on 7/8/2016.
 */
public interface Config {

    static final String YOUR_API_URL = "http://go4drupal.com/vcare_app/api/parent/";
    static final String TERMS = "http://go4drupal.com/gps_tracker/termcondition/mobile";

    static final String YOUR_SOCKET_URL = "";

    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    // Google project id
    static final String GOOGLE_SENDER_ID = "616950103971";

    static final String TAG = "GCM Android";

    // Broadcast reciever name to show gcm registration messages on screen
    static final String DISPLAY_REGISTRATION_MESSAGE_ACTION =
            "com.android.rahateerider.DISPLAY_REGISTRATION_MESSAGE";

    // Broadcast reciever name to show user messages on screen
    static final String DISPLAY_MESSAGE_ACTION =
            "com.android.rahateerider.DISPLAY_MESSAGE";

    // Parse server message with this name
    static final String EXTRA_MESSAGE = "message";


    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;

    public static final String SHARED_PREF = "ah_firebase";


}
