package com.example.android.vcare.ui.dashboard;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ViewGroupRequestItemBinding;

public class GroupRequestItemView extends FrameLayout implements View.OnClickListener {

    private ViewGroupRequestItemBinding binding;
    private DashboardItemListener listener;

    public GroupRequestItemView(@NonNull Context context, DashboardItemListener listener) {
        super(context);
        this.listener = listener;
        if (!isInEditMode()) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.view_group_request_item,
                    this,
                    true);
        }
        binding.accept.setOnClickListener(this);
        binding.reject.setOnClickListener(this);
    }

    public void update() {
    }

    @Override
    public void onClick(View view) {
        if (listener == null) {
            return;
        }
        if (view == binding.accept) {
            listener.onGroupAccept();
        } else if (view == binding.reject) {
            listener.onGroupReject();
        }
    }
}
