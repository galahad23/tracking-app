//package com.example.android.vcare.pending;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.android.vcare.AppController;
//import com.example.android.vcare.R;
//import com.example.android.vcare.adapter.Faq_Adapter;
//import com.example.android.vcare.model.Faq_Details;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by mukeesh on 8/1/2017.
// */
//
//public class Faq extends Fragment {
//    private Faq_Adapter mAdapter;
//    private RecyclerView recyclerView;
//    private List<Faq_Details> helplist = new ArrayList<>();
//    ProgressDialog pDialog;
//
//    public Faq() {
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
//        View view = inflater.inflate(R.layout.fragment_faq, container, false);
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
//                                mAdapter = new Faq_Adapter(helplist);
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
//
//}
