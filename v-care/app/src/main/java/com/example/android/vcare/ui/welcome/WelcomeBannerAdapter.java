package com.example.android.vcare.ui.welcome;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.vcare.R;
import com.example.android.vcare.model2.Banner;

import java.util.List;

public class WelcomeBannerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private List<Banner> bannerList;

    public WelcomeBannerAdapter(Context context, List<Banner> bannerList) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bannerList = bannerList;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.view_welcome_banner, container, false);
        container.addView(view);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView description = (TextView) view.findViewById(R.id.description);

        Banner banner = bannerList.get(position);
        image.setImageResource(banner.getDrawableId());
        description.setText(banner.getDesciption());

        return view;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}