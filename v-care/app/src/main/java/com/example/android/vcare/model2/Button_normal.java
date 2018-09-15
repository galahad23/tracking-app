package com.example.android.vcare.model2;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Mtoag on 8/30/2016.
 */
public class Button_normal extends Button {

    public Button_normal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Regular.otf"));

    }
}
