//package com.example.android.vcare.pending;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.example.android.vcare.R;
//
//
//public class subscription_plan extends Fragment {
//    Button plan;
//    TextView planname,planprice,expryplan;
//
//    public subscription_plan() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_subscriptio_plan, container, false);
//
//        planname = (TextView)rootView.findViewById(R.id.planname);
//        planprice = (TextView)rootView.findViewById(R.id.planprice);
//        expryplan = (TextView)rootView.findViewById(R.id.expry_plan);
//
//        expryplan.setText(Constant.notification);
//        planprice.setText("USD "+ Constant.plan_price);
//        planname.setText(Constant.plan_name);
//
//        plan = (Button)rootView.findViewById(R.id.select_plan);
//
//        plan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),Select_plan.class);
//                intent.putExtra("plan_id", Constant.plan_id);
//                startActivity(intent);
//                 getActivity().finish();
//            }
//        });
//
//
//
//        return rootView;
//    }
//}