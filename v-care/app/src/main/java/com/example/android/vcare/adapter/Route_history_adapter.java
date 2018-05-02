package com.example.android.vcare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vcare.R;
import com.example.android.vcare.model.Group_detail_list;

import java.util.List;

/**
 * Created by mukeesh on 2/17/2017.
 */

public class Route_history_adapter extends RecyclerView.Adapter<Route_history_adapter.MyViewHolder> {

    private List<Group_detail_list> help_details;
    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sr_number,address,date;


        public MyViewHolder(View view) {
            super(view);

            sr_number = (TextView)view.findViewById(R.id.serial_no);
            address = (TextView)view.findViewById(R.id.address);
            date = (TextView)view.findViewById(R.id.date);

        }
    }

    public Route_history_adapter(List<Group_detail_list> helplist) {
        this.help_details = helplist;
    }

    @Override
    public Route_history_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_history_adaptor_list, parent, false);
        context = parent.getContext();
        return new Route_history_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Route_history_adapter.MyViewHolder holder, final int position) {

        final Group_detail_list list = help_details.get(position);
        String position1 = String.valueOf(position+1);


         String address = list.getName();
         String date = list.getImage();
         String id = list.getId();
        holder.sr_number.setText(position1);
        holder.address.setText(address);
        holder.date.setText(date);


    }

    @Override
    public int getItemCount() {
        return help_details.size();
    }
}