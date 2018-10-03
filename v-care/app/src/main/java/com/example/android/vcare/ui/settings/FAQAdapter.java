package com.example.android.vcare.ui.settings;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ViewFaqItemBinding;
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

    protected class FAQItemView extends FrameLayout {

        private ViewFaqItemBinding binding;

        public FAQItemView(@NonNull Context context) {
            super(context);
            if (!isInEditMode()) {
                binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                        R.layout.view_faq_item,
                        this,
                        true);
            }
        }

        public void update(Faq faq) {
            binding.question.setText(faq.getQuestion());
            binding.answer.setText(faq.getAnswer());

            binding.action.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.answer.getVisibility() == View.VISIBLE) {
                        binding.action.setImageResource(R.drawable.ic_add);
                        binding.answer.setVisibility(GONE);
                    } else {
                        binding.answer.setVisibility(VISIBLE);
                        binding.action.setImageResource(R.drawable.ic_cross);
                    }
                }
            });
        }
    }
}
