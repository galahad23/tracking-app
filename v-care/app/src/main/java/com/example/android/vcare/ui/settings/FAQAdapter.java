package com.example.android.vcare.ui.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.vcare.model.Faq;

import java.util.ArrayList;

public class FAQAdapter extends ArrayAdapter<Faq> {
    public FAQAdapter(@NonNull Context context) {
        super(context, 0, new ArrayList<Faq>());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = new FAQItemView(getContext());
        }

        ((FAQItemView) convertView).update(getItem(position));
        return convertView;
    }
}
