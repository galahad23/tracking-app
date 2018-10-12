package com.example.android.vcare.ui.dashboard;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ViewTrackDeviceRequestItemBinding;

public class TrackDeviceRequestItemView extends FrameLayout implements View.OnClickListener {

    private ViewTrackDeviceRequestItemBinding binding;
    private DashboardItemListener listener;

    public TrackDeviceRequestItemView(@NonNull Context context, DashboardItemListener listener) {
        super(context);
        this.listener = listener;
        if (!isInEditMode()) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.view_track_device_request_item,
                    this,
                    true);
        }
        binding.accept.setOnClickListener(this);
        binding.reject.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (listener == null) {
            return;
        }
        if (view == binding.reject) {
            listener.onTrackDeviceAccept();
        } else if (view == binding.accept) {
            listener.onTrackDeviceReject();
        }
    }
}
