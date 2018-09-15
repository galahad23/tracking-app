package com.example.android.vcare.model2;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Mtoag on 8/30/2016.
 */
public class Edit_text_normal extends EditText {

    public Edit_text_normal(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Light.otf"));

    }
}

