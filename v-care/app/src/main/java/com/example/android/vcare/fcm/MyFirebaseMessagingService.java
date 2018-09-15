package com.example.android.vcare.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by etc03 on 09/11/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.d(TAG, "From: " + message.getFrom());
        if (message.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + message.getData());
        }
        if (message.getNotification() != null) {
            Log.d(TAG, "Message NotificationList Body: " + message.getNotification().getBody());
        }

        Map<String, String> data = message.getData();
        String title = data.get("title");
        String body = data.get("message");
//        sendNotification(data, title, body);

//        String object = data.get("object");
//        if (!TextUtils.isEmpty(object)) {
//            NotificationList notificationList = NotificationList.deserialize(object);
//            if (notificationList.getType().equalsIgnoreCase(Constants.NotificationType.CHAT)) {
//                EventBusUtil.post(new ChatEvent.OnReceiveMessage(notificationList));
//            }
//        }
        //Start Handle NotificationList
//        Map<String, String> data = message.getData();
//        if (data.get("object") != null) {
//            String objectJson = data.get("object");
//            NotificationList notificationList = NotificationList.deserialize(objectJson);
//            sendNotification(data, notificationList);
//        }
    }

//    private void sendNotification(Map<String, String> data, String title, String message) {
//        Intent intent = generateIntent(data);
//        if (intent != null) {
//            PendingIntent pi;
//            int id = (int) (Math.random() * 100000);
//            pi = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
////                sendChatNotification(pi, data);
//            displaySimpleNotification(pi, title, message);
//        }
//    }
//
//    private void sendChatNotification(PendingIntent pi, Map<String, String> data) {
//        Context context = getApplicationContext();
//        String title = getString(R.string.app_name);
//
//        String sender = data.get("title");
//        String ticker = String.format(getString(R.string.a_new_message_from_s), sender);
//
//        Chat chat = getChatObject(data);
//        if (chat != null) {
//            EventBusUtil.post(new ChatEvent.OnReceiveNewMessage(chat));
//            ChatNotificationHandler.addUnreadChat(context, chat.getChatRoomId());
//        }
//        String bodyMsg = getChatNotificationBodyMessage(ChatNotificationHandler.getUnreadChat(context));
//
//        displayChatNotification(pi, title, bodyMsg, ticker);
//    }
//
//    private String getChatNotificationBodyMessage(@NonNull ArrayList<UnreadChat> unreadChats) {
//        String bodyMessage = getString(R.string.you_have_new_messages);
//        if (unreadChats.isEmpty()) {
//            return bodyMessage;
//        }
//        int chatRoomCount = unreadChats.size();
//        int messageCount = 0;
//        for (UnreadChat unreadChat : unreadChats) {
//            messageCount += unreadChat.getCount();
//        }
//
//        if (messageCount == 0) {
//            return bodyMessage;
//        }
//
//        bodyMessage = String.format(getString(R.string.d_messages_from_d_chats), messageCount, chatRoomCount);
//
//        return bodyMessage;
//    }
//
//    private Chat getChatObject(Map<String, String> data) {
//        if (data.get("chat_id") != null) {
//            String title = data.get("title");
//            String bodyMsg = data.get("message");
//            String chatRoomId = data.get("chat_room_id");
//            String profilePicture = data.get("profile_picture");
//            if (TextUtils.isEmpty(chatRoomId)) {
//                return null;
//            }
//
//            Chat chat = new Chat();
//            chat.setChatRoomId(Integer.parseInt(chatRoomId))
//                    .setUserName(title)
//                    .setMessage(bodyMsg)
//                    .setProfilePicture(profilePicture)
//                    .setCreatedAt(DateTime.now().toString("yyyy-MM-dd hh:mm aa"));
//
//            return chat;
//        }
//        return null;
//    }

//    private Intent generateIntent(Map<String, String> data) {
//        Intent intent = new Intent(this, NotificationProcessActivity.class);
//        Bundle bundle = new Bundle(data.size());
//        for (Map.Entry<String, String> entry : data.entrySet()) {
//            bundle.putString(entry.getKey(), entry.getValue());
//        }
//        intent.putExtras(bundle);
//        return intent;
//    }
//
//    private static NotificationHelper notificationHelper;
//
//    private void displaySimpleNotification(PendingIntent pi, String title, String message) {
//        if (!UserHandler.getEnabledNotification(getApplicationContext())) {
//            return;
//        }
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            if (notificationHelper == null) {
//                notificationHelper = new NotificationHelper(this);
//            }
//            Notification.Builder notificationBuilder = notificationHelper.getSimpleNotification(title, message, pi);
//            notificationHelper.notify(SIMPLE_NOTIFICATION_ID, notificationBuilder);
//        } else {
//            NotificationCompat.Builder mBuilder =
//                    new NotificationCompat.Builder(this, "default")
//                            .setSmallIcon(R.drawable.app_icon)
//                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
//                            .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setContentIntent(pi)
//                            .setTicker(message)
//                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                            .setAutoCancel(true)
//                            .setDefaults(Notification.DEFAULT_ALL);
//
//            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
//            manager.notify(SIMPLE_NOTIFICATION_ID, mBuilder.build());
//        }
//    }

    //
//    private void displayChatNotification(PendingIntent pi, String title, String message, String ticker) {
//        //Check is notification enable
//        if (!UserHandler.getEnabledNotification(getApplicationContext())) {
//            return;
//        }
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            if (notificationHelper == null) {
//                notificationHelper = new NotificationHelper(this);
//            }
//            NotificationList.Builder notificationBuilder = notificationHelper.getChatRoomNotification(title, message, pi, ticker);
//            notificationHelper.notify(CHAT_NOTIFICATION_ID, notificationBuilder);
//        } else {
//            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            if (notificationManager != null) {
//                notificationManager.cancel(CHAT_NOTIFICATION_ID);
//            }
//
//            NotificationCompat.Builder mBuilder =
//                    new NotificationCompat.Builder(this, "default")
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                            .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setContentIntent(pi)
//                            .setTicker(ticker)
//                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                            .setAutoCancel(true)
//                            .setDefaults(NotificationList.DEFAULT_ALL);
//
//            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
//            manager.notify(CHAT_NOTIFICATION_ID, mBuilder.build());
//        }
//    }
//
    public static final int SIMPLE_NOTIFICATION_ID = 10001;
//    public static final int CHAT_NOTIFICATION_ID = 10002;
}