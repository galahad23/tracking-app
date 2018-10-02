//package com.example.android.vcare.ui.settings;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.example.android.vcare.model2.Faq_Details;
//import com.example.android.vcare.R;
//import java.util.List;
//
///**
// * Created by Mtoag on 12/30/2016.
// */
//
//public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {
//
//    private List<Faq_Details> faq_details;
//    Context context;
//
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView question,answer;
//        ImageView add,less;
//
//
//        public ViewHolder(View view) {
//            super(view);
//
//            question = (TextView)view.findViewById(R.id.question);
//            answer = (TextView) view.findViewById(R.id.answer);
//            ic_add = (ImageView) view.findViewById(R.id.ic_add);
//            less  = (ImageView) view.findViewById(R.id.less);
//
//
//        }
//    }
//
//    public FAQAdapter(List<Faq_Details> faqDetailsList) {
//        this.faq_details = faqDetailsList;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.view_faq_item, parent, false);
//        context = parent.getContext();
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//
//        final Faq_Details list = faq_details.get(position);
//
//        holder.question.setText(list.getQuestion());
//        holder.answer.setText(list.getAnswer());
//
//        holder.add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.question.setVisibility(View.VISIBLE);
//                holder.answer.setVisibility(View.VISIBLE);
//                holder.less.setVisibility(View.VISIBLE);
//                holder.add.setVisibility(View.GONE);
//            }
//        });
//
//        holder.less.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.add.setVisibility(View.GONE);
//                holder.less.setVisibility(View.GONE);
//                holder.answer.setVisibility(View.GONE);
//                holder.add.setVisibility(View.VISIBLE);
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//
//        return faq_details.size();
//    }
//}
