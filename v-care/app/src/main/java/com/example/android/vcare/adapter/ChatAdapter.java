package com.example.android.vcare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.vcare.Zoom_image;
import com.example.android.vcare.model.ChatMessage;
import com.example.android.vcare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mtoag on 10/21/2016.
 */

public class ChatAdapter extends BaseAdapter {

    public static List<ChatMessage> chatMessageList = new ArrayList<>();
    private static LayoutInflater inflater = null;
    Context context;
    // ArrayList&lt;ChatMessage&gt; chatMessageList;


    public ChatAdapter(Activity activity, List<ChatMessage> list) {
        chatMessageList = list;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = (ChatMessage) chatMessageList.get(position);

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.chating_adapter, null);


        LinearLayout layout = (LinearLayout) vi.findViewById(R.id.bubble_layout);
        final LinearLayout parent_layout = (LinearLayout) vi.findViewById(R.id.bubble_layout_parent);
        ImageView userimage_chat   = (ImageView)vi.findViewById(R.id.userimage_chat);
        ImageView userimage_chat_right   = (ImageView)vi.findViewById(R.id.userimage_chat_right);


        final ImageView imageView  = (ImageView)vi.findViewById(R.id.chatimage);
        TextView msg = (TextView) vi.findViewById(R.id.message_text);
        TextView user = (TextView) vi.findViewById(R.id.username);
        TextView time = (TextView) vi.findViewById(R.id.time);
        ImageButton audio = (ImageButton) vi.findViewById(R.id.audio);

        msg.setText(message.body);
        user.setText(message.sender);
        time.setText(message.receiver);

        String type = message.image;
        final String audiopath = message.audio;
        final String userimage = message.Video;


        if (userimage.isEmpty()){
            Picasso.with(context)
                    .load(R.drawable.header_icon)
                    .error(R.drawable.header_icon)
                    .placeholder(R.drawable.header_icon)
                    .into(userimage_chat);

            Picasso.with(context)
                    .load(R.drawable.header_icon)
                    .error(R.drawable.header_icon)
                    .placeholder(R.drawable.header_icon)
                    .into(userimage_chat_right);

        }else {
            Picasso.with(context)
                    .load(userimage)
                    .error(R.drawable.loadingicon)
                    .placeholder(R.drawable.loadingicon)
                    .into(userimage_chat);

            Picasso.with(context)
                    .load(userimage)
                    .error(R.drawable.loadingicon)
                    .placeholder(R.drawable.loadingicon)
                    .into(userimage_chat_right);

        }




        // if message is mine then align to right
        if (message.isMine) {
            userimage_chat_right.setVisibility(View.VISIBLE);
            userimage_chat.setVisibility(View.GONE);
            user.setVisibility(View.GONE);
            layout.setBackgroundResource(R.drawable.new_right);
            parent_layout.setGravity(Gravity.RIGHT);
            msg.setTextColor(Color.WHITE);

            if (audiopath.isEmpty()){
                audio.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
               // msg.setText(message.body);
            }else if (type.equals("Image")){
                 imageView.setVisibility(View.VISIBLE);
                 audio.setVisibility(View.GONE);
              //  msg.setVisibility(View.GONE);
                Picasso.with(context)
                        .load(audiopath)
                        .error(R.drawable.loadingicon)
                        .placeholder(R.drawable.loadingicon)
                        .into(imageView);


            }else if (type.equals("Audio")){
                imageView.setVisibility(View.GONE);
               // msg.setVisibility(View.GONE);
                audio.setVisibility(View.VISIBLE);
               // audio.setText("Audio file");

            }else if (type.equals("Video")){
                imageView.setVisibility(View.GONE);
               // msg.setVisibility(View.GONE);
                audio.setVisibility(View.VISIBLE);
               // audio.setText("Video file");


            }else {
                //msg.setVisibility(View.VISIBLE);
               // msg.setText(message.body);
            }

        }
        // If not mine then align to left
        else {
            userimage_chat_right.setVisibility(View.GONE);
            userimage_chat.setVisibility(View.VISIBLE);
            user.setVisibility(View.VISIBLE);
            layout.setBackgroundResource(R.drawable.new_bubble1);
            parent_layout.setGravity(Gravity.LEFT);
            msg.setTextColor(Color.parseColor("#e8e8e9"));

            if (audiopath.isEmpty()){
                audio.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
               // msg.setText(message.body);
            }else if (type.equals("Image")){
                audio.setVisibility(View.GONE);
               // msg.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(audiopath)
                        .error(R.drawable.loadingicon)
                        .placeholder(R.drawable.loadingicon)
                        .into(imageView);

            }else if (type.equals("Audio")){
                //msg.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                audio.setVisibility(View.VISIBLE);
               // audio.setText("Audio file");

            }else if (type.equals("Video")){
               // msg.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                audio.setVisibility(View.VISIBLE);
               // audio.setText("Video file");

            }else {
              //  msg.setVisibility(View.VISIBLE);
              //  msg.setText(message.body);
            }


        }



        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });


       audio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {




           }
       });

      imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(v.getContext(),Zoom_image.class);
              intent.putExtra("Image_url",audiopath);
              v.getContext().startActivity(intent);
          }
      });

         /*  if (audiopath.isEmpty()){
            audio.setVisibility(View.GONE);
            msg.setText(message.body);
        }else {
            audio.setVisibility(View.VISIBLE);
            audio.setText(audiopath);
            msg.setText(message.body);
        }*/


        return vi;
    }



    public void add(ChatMessage object) {
        chatMessageList.add(object);
    }
}
