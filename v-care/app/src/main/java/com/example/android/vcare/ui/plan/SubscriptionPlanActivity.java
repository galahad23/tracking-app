package com.example.android.vcare.ui.plan;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ActivitySubscriptionPlanBinding;
import com.example.android.vcare.ui.BaseActivity;


public class SubscriptionPlanActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, SubscriptionPlanActivity.class);
        context.startActivity(starter);
    }

    private ActivitySubscriptionPlanBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_plan);
        setDisplayHomeAsUpEnabled();
        setBackNavigation();
        setToolbarTitle(R.string.subscription_plan);

        binding.upgradePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    //    Button plan;
//    TextView planname, planprice, expryplan;
//
//    public SubscriptionPlanActivity() {
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
//        View rootView = inflater.inflate(R.layout.activity_subscription_plan, container, false);
//
//        planname = (TextView) rootView.findViewById(R.id.planname);
//        planprice = (TextView) rootView.findViewById(R.id.planprice);
//        expryplan = (TextView) rootView.findViewById(R.id.expry_plan);
//
//        expryplan.setText(Constant.notification);
//        planprice.setText("USD " + Constant.plan_price);
//        planname.setText(Constant.plan_name);
//
//        plan = (Button) rootView.findViewById(R.id.select_plan);
//
//        plan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Select_plan.class);
//                intent.putExtra("plan_id", Constant.plan_id);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });
//
//
//        return rootView;
//    }
}