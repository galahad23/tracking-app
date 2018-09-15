package com.example.android.vcare.model2;

import java.util.Random;

/**
 * Created by Mtoag on 10/21/2016.
 */
public class ChatMessage {

    public String body, sender, receiver, senderName,image,audio,filetypes;
    public String Date, Time;
    public String msgid;
    public String Video;
    public boolean isMine;// Did I send the message.

    public ChatMessage(String Sender, String Receiver, String messageString,
                      String strimage, String straudio,String video,String filetype, boolean isMINE,String chat_id) {
        body = messageString;
        isMine = isMINE;
        sender = Sender;
        Video = video;
        image = strimage;
        audio = straudio;
        receiver = Receiver;
        filetypes = filetype;
        senderName = sender;
        msgid = chat_id;
    }


    public void setMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));
        ;
    }
}