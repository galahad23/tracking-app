package com.example.android.vcare;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

public class Zoom_image extends AppCompatActivity {

    TouchImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        Intent intent = getIntent();
        String image_url = intent.getStringExtra("Image_url");
        imageView = (TouchImageView)findViewById(R.id.zoom_image);
        Picasso.with(getApplicationContext()).load(image_url)
                .placeholder(R.drawable.loadingicon)
                .error(R.drawable.loadingicon)
                .into(imageView);


        imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                PointF point = imageView.getScrollPosition();
                RectF rect = imageView.getZoomedRect();
                float currentZoom = imageView.getCurrentZoom();
                boolean isZoomed = imageView.isZoomed();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
        }

        return true;
    }
}
