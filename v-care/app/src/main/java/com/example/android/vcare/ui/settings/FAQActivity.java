package com.example.android.vcare.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.android.vcare.MyApplication;
import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ActivityListViewBinding;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.event.SettingEvent;
import com.example.android.vcare.job.GetFaqJob;
import com.example.android.vcare.ui.BaseActivity;
import com.example.android.vcare.util.EventBusUtil;
import com.example.android.vcare.util.Util;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class FAQActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, FAQActivity.class);
        context.startActivity(starter);
    }

    private ActivityListViewBinding binding;
    private final int hashCode = hashCode();
    private FAQAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_view);
        setDisplayHomeAsUpEnabled();
        setBackNavigation();
        setToolbarTitle(R.string.faq);

        adapter = new FAQAdapter(this);
        binding.listView.setEmptyView(binding.empty);
        binding.listView.hasMorePages(false);
        binding.listView.setAdapter(adapter);
        binding.listView.setDividerHeight(Util.dpToPx(this, 12));

        Util.setListShown(binding.container, binding.progressContainer, false, false);
        MyApplication.addJobInBackground(new GetFaqJob(hashCode));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(SettingEvent.OnGetFaq event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            Util.setListShown(binding.container, binding.progressContainer, true, true);
            if(adapter.getCount() > 0 ){
                adapter.clear();
            }
            adapter.addAll(event.getFaqList());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(ExceptionEvent event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            Util.setListShown(binding.container, binding.progressContainer, true, true);
            Util.showOkOnlyDisableCancelAlertDialog(this, null, event.getErrorMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBusUtil.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusUtil.unregister(this);
    }

    //    private FAQAdapter mAdapter;
//    private RecyclerView recyclerView;
//    private List<Faq_Details> helplist = new ArrayList<>();
//    ProgressDialog pDialog;
//
//    public FAQActivity() {
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
//        View view = inflater.inflate(R.layout.activity_faq, container, false);
//
//        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        faq_api();
//
//        return view;
//    }
//
//
//    private void faq_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("please wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "faq",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("faqRes", response);
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            String success = obj.getString("success");
//
//                            if (success.equalsIgnoreCase("1")) {
//
//                                JSONArray array = obj.getJSONArray("faq_list");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//                                    Faq_Details item = new Faq_Details();
//                                    item.setQuestion(jsobj.getString("question"));
//                                    item.setAnswer(jsobj.getString("answer"));
//
//                                    helplist.add(item);
//
//                                }
//                                mAdapter = new FAQAdapter(helplist);
//                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                                recyclerView.setLayoutManager(mLayoutManager);
//                                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                                recyclerView.setAdapter(mAdapter);
//                                mAdapter.notifyDataSetChanged();
//
//                            }
//
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.e("Error.Response", e.toString());
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pDialog.dismiss();
//
//                        // error
//                        Log.e("Error.Response", error.toString());
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }

}
