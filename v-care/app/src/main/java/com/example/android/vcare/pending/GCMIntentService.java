//package com.example.android.vcare.pending;
//
//import android.app.ActivityManager;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.example.android.vcare.AppController;
//import com.example.android.vcare.R;
//import com.example.android.vcare.model.DBAdapter;
//import com.example.android.vcare.model.UserData;
//import com.example.android.vcare.model.UserHandler;
//import com.example.android.vcare.ui.SplashScreenActivity2;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//
//
//public class GCMIntentService extends FirebaseMessagingService {
//    private static final String TAG = GCMIntentService.class.getSimpleName();
//    String name, mobile, strimage, email, message, straudio, strvideo, group_request, chat, title, imei, group_name;
//    //Chat_DBHelper chat_DBHelper;
//    private AppController aController = null;
//    Bitmap bitmap;
//    UserHandler user_handler;
//    Calendar c = Calendar.getInstance();
//
//    /**
//     * Method called on Receiving a new message from GCM server
//     */
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        Log.e(TAG, "From: " + remoteMessage.getFrom());
//
//        if (aController == null)
//            aController = (AppController) getApplicationContext();
//
//        Log.e(TAG, "---------- onMessage -------------");
//
//        if (remoteMessage.getData().size() > 0) {
//            Log.e(TAG, "Data" + remoteMessage.getData().toString());
//            try {
//                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
//
//                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                message = jsonObject1.getString("message");
//                group_request = jsonObject1.getString("group_message");
//                chat = jsonObject1.getString("chat");
//                title = jsonObject1.getString("title");
//                Constant.group_id = jsonObject1.getString("group_id");
//                group_name = jsonObject1.getString("group_name");
//                strimage = jsonObject1.getString("Image");
//                straudio = jsonObject1.getString("Audio");
//                strvideo = jsonObject1.getString("Video");
//                imei = "";
//
//                bitmap = getBitmapFromURL(strimage);
//                Log.e("bitmap", "bitmap>>>" + bitmap);
//
//                // Call broadcast defined on ShowMessage.java to show message on ShowMessage.java screen
//                //TODO no longer using GCM
////                aController.displayMessageOnScreen(getApplicationContext(), title, message, imei);
//
//                // Store new message data in sqlite database
//                UserData userdata = new UserData(1, imei, name, message, email, mobile);
//                DBAdapter.addUserData(userdata);
//
//                user_handler = new UserHandler();
//
//                if (title.equals("Logout")) {
//
//                    boolean isBackground = isAppIsInBackground(getApplicationContext());
//                    boolean b = user_handler.logoutUser(getApplicationContext());
//                    if (!isBackground) {
//                        generateNotification_logout(getApplicationContext(), title, group_request, imei);
//                    } else {
//
//                        generateNotification_logout1(getApplicationContext(), title, group_request, imei);
//                    }
//
//
//                } else {
//                    if (!message.isEmpty()) {
//                        // generate notification to notify user
//                        generateNotification(getApplicationContext(), title, message, imei);
//                    } else if (bitmap != null) {
//                        generateNotification_image(getApplicationContext(), title, bitmap, imei);
//                    } else if (!straudio.isEmpty()) {
//                        generateNotification_audio(getApplicationContext(), title, straudio, imei);
//                    } else if (!strvideo.isEmpty()) {
//                        generateNotification_video(getApplicationContext(), title, strvideo, imei);
//                    } else if (!chat.isEmpty()) {
//                        generateNotification_chat(getApplicationContext(), title, chat, imei);
//                    } else if (!group_request.isEmpty()) {
//                        generateNotification_group(getApplicationContext(), title, group_request, imei);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }
//
//
//    public Bitmap getBitmapFromURL(String strURL) {
//        try {
//            URL url = new URL(strURL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    /**
//     * Create a notification to inform the user that server has sent a message.
//     */
//    private void generateNotification(Context context, String title, String message, String IMEI) {
//
//
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        String formattedDate = df.format(c.getTime());
//        Intent notificationIntent;
//        if (!Constant.group_id.isEmpty()) {
//            notificationIntent = new Intent(context, Chating_class.class);
//            notificationIntent.putExtra("group_id", Constant.group_id);
//            notificationIntent.putExtra("group", group_name);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        } else {
//            notificationIntent = new Intent(context, MainActivity.class);
//            notificationIntent.putExtra("name", title);
//            notificationIntent.putExtra("message", message);
//            notificationIntent.putExtra("imei", IMEI);
//            // set intent so it does not start a new activity
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//       /* NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);*/
//
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                context);
//        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setContentIntent(resultPendingIntent)
//                .setSound(defaultSoundUri)
//                .setSubText(formattedDate)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setLights(Color.BLUE, 3000, 3000)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                .setContentText(message).build();
//
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify((int) when, notification);
//
//    }
//
//
//    private void generateNotification_group(Context context, String title, String chat, String IMEI) {
//
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//
//        Notification.Builder builder = new Notification.Builder(context);
//        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
//
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//        int mNotificationId = 001;
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                context);
//        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(chat))
//                .setContentIntent(resultPendingIntent)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setLights(Color.BLUE, 3000, 3000)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                .setContentText(chat).build();
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + R.raw.jingle_bells_sms);
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(mNotificationId, notification);
//
//    }
//
//
//    private void generateNotification_chat(Context context, String title, String chat, String IMEI) {
//
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//
//        Notification.Builder builder = new Notification.Builder(context);
//        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
//
//
//        Intent notificationIntent;
//        if (!Constant.group_id.isEmpty()) {
//            notificationIntent = new Intent(context, Chating_class.class);
//            notificationIntent.putExtra("group_id", Constant.group_id);
//            notificationIntent.putExtra("group", group_name);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        } else {
//            notificationIntent = new Intent(context, MainActivity.class);
//            notificationIntent.putExtra("class", "CHAT");
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        }
//
//        int mNotificationId = 001;
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                context);
//        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(chat))
//                .setContentIntent(resultPendingIntent)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setLights(Color.BLUE, 3000, 3000)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                .setContentText(chat).build();
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + R.raw.jingle_bells_sms);
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(mNotificationId, notification);
//
//    }
//
//
//    private void generateNotification_image(Context context, String title, Bitmap bitmap, String IMEI) {
//        Intent notificationIntent;
//        if (!Constant.group_id.isEmpty()) {
//            notificationIntent = new Intent(context, Chating_class.class);
//            notificationIntent.putExtra("group_id", Constant.group_id);
//            notificationIntent.putExtra("group", group_name);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        } else {
//            notificationIntent = new Intent(Intent.ACTION_VIEW);
//            // notificationIntent.setData(Uri.parse("content://media/internal/images/media"));
//            notificationIntent.setData(Uri.parse(strimage));
//            // set intent so it does not start a new activity
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        }
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//
//        Notification.Builder builder = new Notification.Builder(context);
//
//
//        int mNotificationId = 001;
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
//                .setContentIntent(resultPendingIntent)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setLights(Color.BLUE, 3000, 3000)
//                .setContentText("Image").build();
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + R.raw.jingle_bells_sms);
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(mNotificationId, notification);
//
//    }
//
//
//    private void generateNotification_audio(Context context, String title, String audio, String IMEI) {
//        Intent notificationIntent;
//        if (!Constant.group_id.isEmpty()) {
//            notificationIntent = new Intent(context, Chating_class.class);
//            notificationIntent.putExtra("group_id", Constant.group_id);
//            notificationIntent.putExtra("group", group_name);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        } else {
//            notificationIntent = new Intent(Intent.ACTION_VIEW);
//            notificationIntent.setData(Uri.parse(straudio));
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        }
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//
//        Notification.Builder builder = new Notification.Builder(context);
//
//
//        int mNotificationId = 001;
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(audio))
//                .setContentIntent(resultPendingIntent)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setLights(Color.BLUE, 3000, 3000)
//                .setContentText("Audio").build();
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + R.raw.jingle_bells_sms);
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(mNotificationId, notification);
//
//
//    }
//
//
//    private void generateNotification_video(Context context, String title, String video, String IMEI) {
//        Intent notificationIntent;
//        if (!Constant.group_id.isEmpty()) {
//            notificationIntent = new Intent(context, Chating_class.class);
//            notificationIntent.putExtra("group_id", Constant.group_id);
//            notificationIntent.putExtra("group", group_name);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        } else {
//            notificationIntent = new Intent(Intent.ACTION_VIEW);
//            notificationIntent.setData(Uri.parse(strvideo));
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        }
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//
//
//        int mNotificationId = 001;
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT
//        );
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
//        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(video))
//                .setContentIntent(resultPendingIntent)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setLights(Color.BLUE, 3000, 3000)
//                .setContentText("Video").build();
//        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + R.raw.jingle_bells_sms);
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(mNotificationId, notification);
//
//    }
//
//
//    private void generateNotification_logout(Context context, String title, String message, String IMEI) {
//
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//
//       /* NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);*/
//        Intent intent = new Intent(this, SplashScreenActivity2.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                context);
//        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setContentIntent(pendingIntent)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setLights(Color.BLUE, 3000, 3000)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                .setContentText(message).build();
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(100, notification);
//    }
//
//    private void generateNotification_logout1(Context context, String title, String message, String IMEI) {
//
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//
//       /* NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);*/
//        Notification.Builder builder = new Notification.Builder(context);
//
//        Intent notificationIntent = new Intent(context, SplashScreenActivity2.class);
//        notificationIntent.putExtra("name", title);
//        notificationIntent.putExtra("message", message);
//        notificationIntent.putExtra("imei", IMEI);
//        Log.e("emailid", "" + IMEI);
//        // set intent so it does not start a new activity
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        int mNotificationId = 001;
//
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                context);
//        Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setContentIntent(resultPendingIntent)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setLights(Color.BLUE, 3000, 3000)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
//                .setContentText(message).build();
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(mNotificationId, notification);
//    }
//
//    private boolean isAppIsInBackground(Context context) {
//        boolean isInBackground = true;
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
//            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
//            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
//                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    for (String activeProcess : processInfo.pkgList) {
//                        if (activeProcess.equals(context.getPackageName())) {
//                            isInBackground = false;
//                        }
//                    }
//                }
//            }
//        } else {
//            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//            ComponentName componentInfo = taskInfo.get(0).topActivity;
//            if (componentInfo.getPackageName().equals(context.getPackageName())) {
//                isInBackground = false;
//            }
//        }
//
//        return isInBackground;
//    }
//
//
//}
