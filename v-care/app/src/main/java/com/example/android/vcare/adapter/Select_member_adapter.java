package com.example.android.vcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.vcare.R;
import com.example.android.vcare.model2.Child_detail;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mtoag on 10/14/2016.
 */
public class Select_member_adapter extends RecyclerView.Adapter<Select_member_adapter.MyViewHolder> {

    private List<Child_detail> help_details;
       Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox member;
        TextView user;
        ImageView userimage;

        public MyViewHolder(View view) {
            super(view);

            member = (CheckBox) view.findViewById(R.id.checkbox);
            user   = (TextView)view.findViewById(R.id.username);
            userimage = (ImageView)view.findViewById(R.id.userimage);

        }
    }

    public Select_member_adapter(List<Child_detail> helplist) {
        this.help_details = helplist;
    }

    @Override
    public Select_member_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectmember_list_adapter, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Select_member_adapter.MyViewHolder holder, final int position) {
        final Child_detail list = help_details.get(position);

        final int pos = position;
        final String image = list.getParent_name();

        if (image.isEmpty()){

        }else {
            Picasso.with(context).load(image).into(holder.userimage);
        }


        holder.user.setText(list.getName());
        holder.member.setChecked(help_details.get(position).isSelected());
        holder.member.setTag(help_details.get(position));


        holder.member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Child_detail contact = (Child_detail) cb.getTag();
                contact.setSelected(cb.isChecked());
                help_details.get(pos).setSelected(cb.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return help_details.size();
    }

    public List<Child_detail> getStudentist() {
        return help_details;
    }
}

