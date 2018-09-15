//package com.example.android.vcare.pending;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.android.vcare.AppController;
//import com.example.android.vcare.R;
//import com.example.android.vcare.model.DatabaseHandler;
//import com.example.android.vcare.model.Group_detail_list;
//import com.example.android.vcare.model.User_Detail;
//import com.example.android.vcare.model.UserHandler;
//import com.example.android.vcare.ui.login.LoginActivity;
//import com.squareup.picasso.Picasso;
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
//public class Member_group extends Fragment {
//    UserHandler2 user_handler = new UserHandler2();
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    ProgressDialog pDialog;
//    private List<Group_detail_list> helplist = new ArrayList<>();
//    private Group_list_adapter mAdapter;
//    private RecyclerView recyclerView;
//    LinearLayout add_grouply;
//    String parent_id="",mobile_token="",group_id="",group_name="";
//
//    Button add_group;
//
//    public Member_group() {
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
//        View rootView = inflater.inflate(R.layout.fragment_member_group, container, false);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        databaseHandler = new DatabaseHandler(getActivity());
//        feeditem = new ArrayList<User_Detail>();
//        Cursor cursor = databaseHandler.get_rider_detail();
//        if (cursor != null){
//            cursor.moveToFirst();
//            for (int i =0 ; i< cursor.getCount(); i++){
//                User_Detail detail = new User_Detail();
//                detail.setId(cursor.getString(5));
//                feeditem.add(detail);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//
//        for (User_Detail userDetail : feeditem){
//            parent_id  = userDetail.getId();
//        }
//
//        Cursor cursor1 = databaseHandler.get_token_detail();
//        if (cursor1 != null){
//            cursor1.moveToFirst();
//            for (int j=0; j< cursor1.getCount(); j++){
//                mobile_token = cursor1.getString(0);
//                cursor1.moveToNext();
//            }
//
//            cursor1.close();
//        }
//
//        Log.e("parent_id", "parent_id>>"+ parent_id);
//        Log.e("mobiletoken", "mobiletokenn>>"+ mobile_token);
//
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
//        mAdapter = new Group_list_adapter(helplist);
//        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//
//
//        add_grouply = (LinearLayout)rootView.findViewById(R.id.add_group_layout);
//        add_group = (Button)rootView.findViewById(R.id.add_group);
//
//        add_grouply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),New_group.class);
//                startActivity(intent);
//            }
//        });
//
//        add_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),New_group.class);
//                startActivity(intent);
//            }
//        });
//
//
//        return rootView;
//    }
//
//
//    @Override
//    public void onResume() {
//        Cursor cursor1 = databaseHandler.get_token_detail();
//        if (cursor1 != null){
//            cursor1.moveToFirst();
//            for (int j=0; j< cursor1.getCount(); j++){
//                mobile_token = cursor1.getString(0);
//                cursor1.moveToNext();
//            }
//
//            cursor1.close();
//        }
//       helplist.clear();
//        mAdapter.notifyDataSetChanged();
//        Parent_All_group_Api();
//        super.onResume();
//    }
//
//    private void Parent_All_group_Api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"group",
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//                                JSONArray array = objJson.getJSONArray("grouplist");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//                                    Group_detail_list item = new Group_detail_list();
//                                    item.setName(jsobj.getString("group_name"));
//                                    item.setImage(jsobj.getString("image_url"));
//                                    item.setId(jsobj.getString("group_id"));
//
//
//                                    helplist.add(item);
//                                }
//                                mAdapter.notifyDataSetChanged();
//
//                                String mobiletoken = objJson.getString("mobile_token");
//                                databaseHandler.resetTables("token");
//                                databaseHandler.add_token(mobiletoken);
//
//                                // get mobile token from database
//                                Cursor cursor1 = databaseHandler.get_token_detail();
//                                if (cursor1 != null){
//                                    cursor1.moveToFirst();
//                                    for (int j=0; j< cursor1.getCount(); j++){
//                                        mobile_token = cursor1.getString(0);
//                                        cursor1.moveToNext();
//                                    }
//
//                                    cursor1.close();
//                                }
//
//
//                            }else if(success == 0) {
//                                user_handler.logoutUser(getActivity());
//                                alert();
//                            }
//                            else if (success==2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//                                add_grouply.setVisibility(View.VISIBLE);
//                                add_group.setVisibility(View.GONE);
//
//                            } else if (success==3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
//                            }
//
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.e("Error.Response", e.toString());
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
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
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("parent_id", parent_id);
//                params.put("mobile_token",mobile_token);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//                               DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//    // Alert dialouge
//
//    private void alert() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        alertDialogBuilder.setMessage("Your Session is Expired. Please LoginActivity Again");
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                Intent i = new Intent(getActivity(), LoginActivity.class);
//                startActivity(i);
//                getActivity().finishAffinity();
//
//            }
//        });
//        alertDialogBuilder.setTitle("Alert");
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }
//
//    public class Group_list_adapter extends RecyclerView.Adapter<Group_list_adapter.MyViewHolder> {
//
//        private List<Group_detail_list> help_details;
//        Context context;
//
//
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            public TextView groupname;
//            ImageView user_image;
//            LinearLayout view_layout,mainlayout,chat_layout;
//
//            public MyViewHolder(View view) {
//                super(view);
//
//                groupname = (TextView)view.findViewById(R.id.groupname);
//                user_image = (ImageView)view.findViewById(R.id.user_image);
//                view_layout = (LinearLayout)view.findViewById(R.id.view_layout);
//                chat_layout = (LinearLayout)view.findViewById(R.id.chat_layout);
//                mainlayout  = (LinearLayout)view.findViewById(R.id.layout);
//            }
//        }
//
//        public Group_list_adapter(List<Group_detail_list> helplist) {
//            this.help_details = helplist;
//        }
//
//        @Override
//        public Group_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.grouplist_adapter, parent, false);
//            context = parent.getContext();
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final Group_list_adapter.MyViewHolder holder, final int position) {
//
//            final Group_detail_list list = help_details.get(position);
//
//            holder.groupname.setText(list.getName());
//
//            if (list.getImage().isEmpty()){
//
//            }else {
//                Picasso.with(context)
//                        .load(list.getImage())
//                        .error(R.drawable.loadingicon)
//                        .placeholder(R.drawable.loadingicon)
//                        .into(holder.user_image);
//            }
//
//
//            if (list.getId().equals(Constant.group_id)){
//
//                if (Constant.plan_price.equals("null")) {
//                    Intent intent = new Intent(context, Select_plan.class);
//                    startActivity(intent);
//                } else {
//                    group_id = list.getId();
//                    group_name = list.getName();
//                    Intent intent = new Intent(getActivity(), Chating_class.class);
//                    intent.putExtra("group_id", group_id);
//                    intent.putExtra("group", group_name);
//                    startActivity(intent);
//                }
//            }else {
//
//            }
//
//
//            holder.view_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_black));
//            holder.chat_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_black));
//
//            holder.view_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    holder.view_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_green));
//
//                    if (Constant.plan_price.equals("null")) {
//                        Intent intent = new Intent(context, Select_plan.class);
//                        startActivity(intent);
//                    } else {
//
//                        group_id = list.getId();
//                        Intent intent = new Intent(getActivity(), Group_detail.class);
//                        intent.putExtra("group_id", group_id);
//                        intent.putExtra("parent_id", parent_id);
//                        startActivity(intent);
//                    }
//
//                }
//            });
//
//            holder.chat_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    holder.chat_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_green));
//                    if (Constant.plan_price.equals("null")) {
//                        Intent intent = new Intent(context, Select_plan.class);
//                        startActivity(intent);
//                    } else {
//                        group_id = list.getId();
//                        group_name = list.getName();
//                        Intent intent = new Intent(getActivity(), Chating_class.class);
//                        intent.putExtra("group_id", group_id);
//                        intent.putExtra("group", group_name);
//                        startActivity(intent);
//                    }
//                }
//            });
//
//            holder.mainlayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (Constant.plan_price.equals("null")) {
//                        Intent intent = new Intent(context, Select_plan.class);
//                        startActivity(intent);
//                    } else {
//                        group_id = list.getId();
//                        group_name = list.getName();
//                        Intent intent = new Intent(getActivity(), Chating_class.class);
//                        intent.putExtra("group_id", group_id);
//                        intent.putExtra("group", group_name);
//                        startActivity(intent);
//                    }
//                }
//            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return help_details.size();
//        }
//    }
//
//
//
//
//}