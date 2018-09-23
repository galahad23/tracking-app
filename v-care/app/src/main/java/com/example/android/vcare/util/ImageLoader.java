package com.example.android.vcare.util;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;



public class ImageLoader {

    public static void glideImageLoadCenterCrop(final ImageView imageView, final String imageUrl) {
        if (imageView == null) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop())
                .into(imageView);
    }

    public static void glideImageLoadCenterCrop(final ImageView imageView, @DrawableRes final int placeholderId, final String imageUrl) {
        if (imageView == null) {
            return;
        }

        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().placeholder(placeholderId))
                .into(imageView);
    }

    public static void glideImageLoad(final ImageView imageView, @DrawableRes final int placeholderId, final String imageUrl) {
        if (imageView == null) {
            return;
        }

        Glide.with((imageView.getContext()))
                .load(imageUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeholderId))
                .into(imageView);
    }
}
