package com.example.android.vcare.model2;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Mtoag on 8/30/2016.
 */
public class Text_view_bold  extends TextView {

    public Text_view_bold(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SourceSansPro-Bold.otf"));

    }
}
