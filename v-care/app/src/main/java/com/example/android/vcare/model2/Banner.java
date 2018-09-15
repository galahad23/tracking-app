package com.example.android.vcare.model2;

import android.support.annotation.DrawableRes;

public class Banner {
    
    @DrawableRes
    private int drawableId;
    private String desciption;

    public int getDrawableId() {
        return drawableId;
    }

    public Banner setDrawableId(int drawableId) {
        this.drawableId = drawableId;
        return this;
    }

    public String getDesciption() {
        return desciption;
    }

    public Banner setDesciption(String desciption) {
        this.desciption = desciption;
        return this;
    }
}
