//package com.example.android.vcare.pending;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.Display;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
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
//import com.example.android.vcare.model.AutoScrollViewPager;
//import com.example.android.vcare.model.CirclePageIndicator;
//import com.example.android.vcare.model.DatabaseHandler;
//import com.example.android.vcare.model.Group_detail_list;
//import com.example.android.vcare.model.UserData;
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
//
//public class Dashboard extends Fragment {
//    GPSTracker gps;
//    UserHandler2 user_handler = new UserHandler2();
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    ProgressDialog pDialog;
//    String parent_id = "", mobile_token = "", status = "", group_id = "", parent_child_id = "";
//    LinearLayout viewall_request;
//    LinearLayout add_device, add_group, main_child_layout, add_layout, notificationly, grouplayout, devicelayout;
//    LinearLayout child_status, child_device_layout, parent_device_layout, parent_grouplayout, slider_ly;
//    LinearLayout childsos, childnotification, childgroup, view_all_requestDevice;
//    TextView totalgroup, activechild, inactivechild, notification_text, active_parent, inactive_parent, total_parent_group;
//    TextView location_tracker, battery, speed, address, title, titleDevice;
//
//
//    private List<Group_detail_list> helplist = new ArrayList<>();
//    private Dashboard_request_adapter mAdapter;
//    private RecyclerView recyclerView, recycleViewDashboardDevice;
//
//    private List<UserData> helplistDevice = new ArrayList<>();
//    private Dashboard_Devicerequest_adapter deviceAdapter;
//
//    AutoScrollViewPager myPager;
//    CirclePageIndicator mIndicator;
//    private MyViewPagerAdapter myViewPagerAdapter;
//    private int[] layouts;
//
//    public Dashboard() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
//
//        databaseHandler = new DatabaseHandler(getActivity());
//        feeditem = new ArrayList<User_Detail>();
//        Cursor cursor = databaseHandler.get_rider_detail();
//        if (cursor != null) {
//            cursor.moveToFirst();
//            for (int i = 0; i < cursor.getCount(); i++) {
//                User_Detail detail = new User_Detail();
//                detail.setId(cursor.getString(5));
//                detail.setAddress(cursor.getString(6));
//
//                feeditem.add(detail);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//
//        for (User_Detail userDetail : feeditem) {
//            parent_id = userDetail.getId();
//            status = userDetail.getaddress();
//        }
//
//        Cursor cursor1 = databaseHandler.get_token_detail();
//        if (cursor1 != null) {
//            cursor1.moveToFirst();
//            for (int j = 0; j < cursor1.getCount(); j++) {
//                mobile_token = cursor1.getString(0);
//                cursor1.moveToNext();
//            }
//            cursor1.close();
//        }
//
//        Log.e("parent_id", "parent_id>>" + parent_id);
//        Log.e("mobiletoken", "mobiletokenn>>" + mobile_token);
//        Log.e("status", "status>>" + status);
//
//
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleViewDashboard);
//        mAdapter = new Dashboard_request_adapter(helplist);
//        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//
//        recycleViewDashboardDevice = (RecyclerView) rootView.findViewById(R.id.recycleViewDashboardDevice);
//        deviceAdapter = new Dashboard_Devicerequest_adapter(helplistDevice);
//        final RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
//        recycleViewDashboardDevice.setLayoutManager(mLayoutManager1);
//        recycleViewDashboardDevice.setItemAnimator(new DefaultItemAnimator());
//        recycleViewDashboardDevice.setAdapter(deviceAdapter);
//
//
//        battery = (TextView) rootView.findViewById(R.id.battry);
//        location_tracker = (TextView) rootView.findViewById(R.id.tracker);
//        speed = (TextView) rootView.findViewById(R.id.speed);
//        address = (TextView) rootView.findViewById(R.id.location);
//        title = (TextView) rootView.findViewById(R.id.title);
//        titleDevice = (TextView) rootView.findViewById(R.id.titleDevice);
//
//
//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        int SCREEN_WIDTH = display.getWidth();
//        int SCREEN_HEIGHT = display.getHeight();
//        float height = display.getHeight() / 2;
//        myPager = (AutoScrollViewPager) rootView.findViewById(R.id.mypager);
//        mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
//        myPager.setMinimumHeight(SCREEN_HEIGHT);
//
//        ViewGroup.LayoutParams params = myPager.getLayoutParams();
//        params.height = SCREEN_HEIGHT - Math.round(height);   //500px
//        // params.width = SCREEN_WIDTH;    //500px
//        myPager.requestLayout();
//
//        slider_ly = (LinearLayout) rootView.findViewById(R.id.slider_ly);
//
//        viewall_request = (LinearLayout) rootView.findViewById(R.id.view_all_request);
//        add_device = (LinearLayout) rootView.findViewById(R.id.add_device);
//        add_group = (LinearLayout) rootView.findViewById(R.id.add_group);
//        main_child_layout = (LinearLayout) rootView.findViewById(R.id.main_child_layout);
//        add_layout = (LinearLayout) rootView.findViewById(R.id.add_layout);
//        grouplayout = (LinearLayout) rootView.findViewById(R.id.group_layout);
//        devicelayout = (LinearLayout) rootView.findViewById(R.id.device_layout);
//        notificationly = (LinearLayout) rootView.findViewById(R.id.notificationly);
//        notification_text = (TextView) rootView.findViewById(R.id.notification_text);
//        activechild = (TextView) rootView.findViewById(R.id.active_child);
//        inactivechild = (TextView) rootView.findViewById(R.id.inactive_child);
//        totalgroup = (TextView) rootView.findViewById(R.id.total_group);
//        active_parent = (TextView) rootView.findViewById(R.id.active_parent);
//        inactive_parent = (TextView) rootView.findViewById(R.id.inactive_parent);
//        total_parent_group = (TextView) rootView.findViewById(R.id.total_parent_group);
//        view_all_requestDevice = (LinearLayout) rootView.findViewById(R.id.view_all_requestDevice);
//
//
//        // parent dashboard layout
//        parent_grouplayout = (LinearLayout) rootView.findViewById(R.id.parent_group_layout);
//        parent_device_layout = (LinearLayout) rootView.findViewById(R.id.parent_device_layout);
//
//        // child dashboard layout
//
//        child_status = (LinearLayout) rootView.findViewById(R.id.child_status);
//        child_device_layout = (LinearLayout) rootView.findViewById(R.id.child_device_layout);
//        childgroup = (LinearLayout) rootView.findViewById(R.id.group_layout);
//        childnotification = (LinearLayout) rootView.findViewById(R.id.notification_layout);
//        childsos = (LinearLayout) rootView.findViewById(R.id.sos_layout);
//
//        gps = new GPSTracker(getActivity());
//        if (gps.canGetLocation()) {
//
//        } else {
//
//            // gps.showSettingsAlert();
//        }
//
//        parent_device_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Constant.plan_price.equals("null")) {
//                    Intent intent = new Intent(getActivity(), Select_plan.class);
//                    startActivity(intent);
//                } else {
//                    Fragment frag = new Track_member_device();
//                    MainActivity.title.setText("Track Member Device");
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.container_body, frag);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                }
//            }
//        });
//
//        child_device_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Constant.plan_price.equals("null")) {
//                    Intent intent = new Intent(getActivity(), Select_plan.class);
//                    startActivity(intent);
//                } else {
//                    Fragment frag = new Track_member_device();
//                    MainActivity.title.setText("Track Member Device");
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.container_body, frag);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                }
//            }
//        });
//
//        childnotification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Constant.plan_price.equals("null")) {
//                    Intent intent = new Intent(getActivity(), Select_plan.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getActivity(), All_notification.class);
//                    startActivity(intent);
//                }
//            }
//        });
//
//
//        add_device.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Constant.plan_price.equals("null")) {
//                    Intent intent = new Intent(getActivity(), Select_plan.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getActivity(), TrackDeviceList.class);
//                    startActivity(intent);
//                }
//            }
//        });
//
//        add_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment frag = new Member_group();
//                MainActivity.title.setText("Group");
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.container_body, frag);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });
//
//        childgroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment frag = new Member_group();
//                MainActivity.title.setText("Group");
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.container_body, frag);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });
//
//        childsos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Constant.plan_price.equals("null")) {
//                    Intent intent = new Intent(getActivity(), Select_plan.class);
//                    startActivity(intent);
//                } else {
//                    Fragment frag = new Sos();
//                    MainActivity.title.setText("SOS");
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.container_body, frag);
//                    ft.addToBackStack(null);
//                    ft.commit();
//                }
//            }
//        });
//
//        grouplayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment frag = new Member_group();
//                MainActivity.title.setText("Group");
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.container_body, frag);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });
//
//        parent_grouplayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment frag = new Member_group();
//                MainActivity.title.setText("Group");
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.container_body, frag);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });
//
//        // layouts of all welcome sliders
//        // add few more layouts if you want
//        layouts = new int[]{
//                R.layout.view_welcome_banner,
//                R.layout.dashboard_slide2,
//                R.layout.dashboard_slide3,
//                R.layout.dashboard_slide4,
//                R.layout.dashboard_slide5};
//
//        myViewPagerAdapter = new MyViewPagerAdapter();
//        myPager.setAdapter(myViewPagerAdapter);
//        myPager.startAutoScroll();
//        mIndicator.setViewPager(myPager);
//        myPager.addOnPageChangeListener(viewPagerPageChangeListener);
//
//        return rootView;
//    }
//
//    //  viewpager change listener
//    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
//
//        @Override
//        public void onPageSelected(int position) {
//            // addBottomDots(position);
//
//            // changing the next button text 'NEXT' / 'GOT IT'
//            if (position == layouts.length - 1) {
//
//            } else {
//
//            }
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//
//        }
//    };
//
//
//    public class MyViewPagerAdapter extends PagerAdapter {
//        private LayoutInflater layoutInflater;
//
//        public MyViewPagerAdapter() {
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View view = layoutInflater.inflate(layouts[position], container, false);
//            container.addView(view);
//
//            return view;
//        }
//
//        @Override
//        public int getCount() {
//            return layouts.length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object obj) {
//            return view == obj;
//        }
//
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            container.removeView(view);
//        }
//    }
//
//    @Override
//    public void onResume() {
//        MainActivity.title.setText("Dashboard");
//
//        Cursor cursor1 = databaseHandler.get_token_detail();
//        if (cursor1 != null) {
//            cursor1.moveToFirst();
//            for (int j = 0; j < cursor1.getCount(); j++) {
//                mobile_token = cursor1.getString(0);
//                cursor1.moveToNext();
//            }
//
//            cursor1.close();
//        }
//
//        Parent_dashboard_Api();
//        super.onResume();
//    }
//
//    private void Parent_dashboard_Api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "parentdashboard",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//
//                                if (status.equals("0")) {
//                                    parent_device_layout.setVisibility(View.VISIBLE);
//                                    slider_ly.setVisibility(View.VISIBLE);
//                                } else {
//                                    slider_ly.setVisibility(View.GONE);
//                                    child_status.setVisibility(View.VISIBLE);
//                                    main_child_layout.setVisibility(View.VISIBLE);
//                                    parent_device_layout.setVisibility(View.GONE);
//
//                                }
//
//                                add_layout.setVisibility(View.GONE);
//
//                                JSONObject object = objJson.getJSONObject("Device");
//                                String active_child = object.getString("active_child");
//                                String inactive_child = object.getString("Inactive_child");
//
//                                JSONObject objectt = objJson.getJSONObject("child_speed_address");
//                                String child_speed = objectt.getString("child_max_speed");
//                                String child_address = objectt.getString("address");
//                                String strbattery = objectt.getString("battery");
//                                String strlocation_tracker = objectt.getString("gps_status");
//                                String image = objectt.getString("profile_image");
//
//
//                                if (image.isEmpty()) {
//
//                                } else {
//
//                                    Picasso.with(getActivity())
//                                            .load(image)
//                                            .placeholder(R.drawable.header_icon)
//                                            .error(R.drawable.header_icon)
//                                            .into(MainActivity.userimage);
//
//                                }
//
//                                String group = objJson.getString("Group");
//                                String mobiletoken = objJson.getString("mobile_token");
//                                Constant.notification = objJson.getString("Notification");
//                                Constant.plan_name = objJson.getString("Plan_name");
//                                Constant.plan_price = objJson.getString("Plan_price");
//                                Constant.plan_id = objJson.getString("plan_id");
//                                Constant.view_route = objJson.getString("view_route");
//                                //  Constant.allowed_device      = objJson.getInt("allowed_device");
//
//
//                                totalgroup.setText(group);
//                                total_parent_group.setText(group);
//                                active_parent.setText(active_child);
//                                inactive_parent.setText(inactive_child);
//                                activechild.setText(active_child);
//                                inactivechild.setText(inactive_child);
//                                speed.setText(child_speed + " Km/hr");
//                                address.setText(child_address);
//                                battery.setText(strbattery + "%");
//
//                                if (strlocation_tracker.equals("0")) {
//                                    location_tracker.setText("Off");
//                                } else {
//                                    location_tracker.setText("On");
//                                }
//
//
//                                if (active_child.equals("0") && inactive_child.equals("0") && group.equals("0")) {
//
//                                    add_layout.setVisibility(View.VISIBLE);
//                                    main_child_layout.setVisibility(View.GONE);
//                                    parent_device_layout.setVisibility(View.GONE);
//                                } else {
//                                    slider_ly.setVisibility(View.GONE);
//                                    // parent
//                                    if (status.equals("0")) {
//                                        slider_ly.setVisibility(View.VISIBLE);
//                                        main_child_layout.setVisibility(View.GONE);
//                                        parent_device_layout.setVisibility(View.VISIBLE);
//                                    } else {
//                                        slider_ly.setVisibility(View.GONE);
//                                        add_layout.setVisibility(View.GONE);
//                                        main_child_layout.setVisibility(View.VISIBLE);
//                                        parent_device_layout.setVisibility(View.GONE);
//                                    }
//                                }
//
//                                if (Constant.notification.equals(" ")) {
//                                    notificationly.setVisibility(View.GONE);
//                                } else {
//                                    notificationly.setVisibility(View.VISIBLE);
//                                    notification_text.setText(Constant.notification);
//                                }
//
//                                databaseHandler.resetTables("token");
//                                databaseHandler.add_token(mobiletoken);
//
//                                // clear gatter setter class
//                                helplist.clear();
//                                helplistDevice.clear();
//
//                                All_Request();
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(getActivity());
//                                alert();
//                            } else if (success == 2) {
//                                // clear gatter setter class
//                                helplist.clear();
//                                helplistDevice.clear();
//                                All_Request();
//                                slider_ly.setVisibility(View.VISIBLE);
//                                add_layout.setVisibility(View.VISIBLE);
//                                main_child_layout.setVisibility(View.GONE);
//                                parent_device_layout.setVisibility(View.GONE);
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("parent_id", parent_id);
//                params.put("mobile_token", mobile_token);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
//
//    private void All_Request() {
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "GroupRequestList",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//
//
//                                slider_ly.setVisibility(View.GONE);
//
//
//                                JSONArray array = objJson.getJSONArray("Group_request");
//                                if (array.length() == 0) {
//                                    viewall_request.setVisibility(View.GONE);
//                                    title.setText("Recent Group Request");
//                                } else {
//
//                                    viewall_request.setVisibility(View.VISIBLE);
//                                    title.setText("Recent Group Request");
//
//                                    for (int i = 0; i < array.length(); i++) {
//                                        JSONObject jsobj = array.getJSONObject(i);
//                                        Group_detail_list item = new Group_detail_list();
//                                        item.setName(jsobj.getString("group_name"));
//                                        item.setImage(jsobj.getString("request_type"));
//                                        item.setId(jsobj.getString("group_id"));
//
//                                        helplist.add(item);
//                                    }
//
//                                }
//
//                                JSONArray Member_request = objJson.getJSONArray("Member_request");
//
//                                if (Member_request.length() == 0) {
//                                    view_all_requestDevice.setVisibility(View.GONE);
//                                    titleDevice.setText("Recent Track Device Request");
//                                } else {
//
//                                    view_all_requestDevice.setVisibility(View.VISIBLE);
//                                    titleDevice.setText("Recent Track Device Request");
//
//                                    for (int i = 0; i < Member_request.length(); i++) {
//                                        JSONObject jsobj = Member_request.getJSONObject(i);
//                                        UserData item = new UserData();
//                                        item.setName(jsobj.getString("name"));
//                                        item.setID(jsobj.getInt("parent_child_id"));
//
//                                        helplistDevice.add(item);
//                                    }
//
//                                }
//
//
//                                mAdapter.notifyDataSetChanged();
//                                deviceAdapter.notifyDataSetChanged();
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(getActivity());
//                                alert();
//                            } else if (success == 2) {
//                                viewall_request.setVisibility(View.GONE);
//                                view_all_requestDevice.setVisibility(View.GONE);
//                                mAdapter.notifyDataSetChanged();
//                                String msg = objJson.getString("text");
//                                //  Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
//
//                        // error
//                        Log.e("Error.Response", error.toString());
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("parent_id", parent_id);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//    private class Dashboard_request_adapter extends RecyclerView.Adapter<Dashboard_request_adapter.MyViewHolder> {
//
//        private List<Group_detail_list> help_details;
//        Context context;
//
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            public TextView groupname;
//            LinearLayout accept, delete, list;
//
//            public MyViewHolder(View view) {
//                super(view);
//
//                groupname = (TextView) view.findViewById(R.id.groupname);
//                accept = (LinearLayout) view.findViewById(R.id.accept);
//                ic_cross = (LinearLayout) view.findViewById(R.id.ic_cross);
//                list = (LinearLayout) view.findViewById(R.id.list);
//            }
//        }
//
//        public Dashboard_request_adapter(List<Group_detail_list> helplist) {
//            this.help_details = helplist;
//        }
//
//        @Override
//        public Dashboard_request_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.dashboard_requestlist_adapter, parent, false);
//            context = parent.getContext();
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final Dashboard_request_adapter.MyViewHolder holder, final int position) {
//
//            final Group_detail_list list = help_details.get(position);
//
//            holder.groupname.setText(list.getName());
//
//            holder.accept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    group_id = list.getId();
//                    Accept_request();
//                }
//            });
//
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    group_id = list.getId();
//                    Delete_request();
//                }
//
//            });
//
//            if (position % 2 == 0) {
//                holder.list.setBackgroundResource(R.color.requestlist);
//            } else {
//                holder.list.setBackgroundResource(R.color.edit_text_linecolor);
//            }
//
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
//    private class Dashboard_Devicerequest_adapter extends RecyclerView.Adapter<Dashboard_Devicerequest_adapter.MyViewHolder> {
//
//        private List<UserData> help_details;
//        Context context;
//
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            public TextView groupname;
//            LinearLayout accept, delete, list;
//
//            public MyViewHolder(View view) {
//                super(view);
//
//                groupname = (TextView) view.findViewById(R.id.groupname);
//                accept = (LinearLayout) view.findViewById(R.id.accept);
//                ic_cross = (LinearLayout) view.findViewById(R.id.ic_cross);
//                list = (LinearLayout) view.findViewById(R.id.list);
//            }
//        }
//
//        public Dashboard_Devicerequest_adapter(List<UserData> helplist) {
//            this.help_details = helplist;
//        }
//
//        @Override
//        public Dashboard_Devicerequest_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.dashboard_requestlist_adapter, parent, false);
//            context = parent.getContext();
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final Dashboard_Devicerequest_adapter.MyViewHolder holder, final int position) {
//
//            final UserData list = help_details.get(position);
//
//            holder.groupname.setText(list.getName() + " Added to track device");
//
//            holder.accept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    parent_child_id = String.valueOf(list.getID());
//                    Accept_Devicerequest();
//                }
//            });
//
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    parent_child_id = String.valueOf(list.getID());
//                    Delete_Devicerequest();
//                }
//
//            });
//
//            if (position % 2 == 0) {
//                holder.list.setBackgroundResource(R.color.requestlist);
//            } else {
//                holder.list.setBackgroundResource(R.color.edit_text_linecolor);
//            }
//
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
//    private void Accept_request() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "AcceptRequest",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//
//                                /*// clear gatter setter class
//                                helplist.clear();
//                                All_Request();*/
//
//                                Cursor cursor1 = databaseHandler.get_token_detail();
//                                if (cursor1 != null) {
//                                    cursor1.moveToFirst();
//                                    for (int j = 0; j < cursor1.getCount(); j++) {
//                                        mobile_token = cursor1.getString(0);
//                                        cursor1.moveToNext();
//                                    }
//
//                                    cursor1.close();
//                                }
//
//                                Parent_dashboard_Api();
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(getActivity());
//                                alert();
//                            } else if (success == 2) {
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("parent_id", parent_id);
//                params.put("group_id", group_id);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//
//    private void Accept_Devicerequest() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Accept_Member_Request",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//
//
//                                Parent_dashboard_Api();
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(getActivity());
//                                alert();
//                            } else if (success == 2) {
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("parent_child_id", parent_child_id);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//
//    private void delete_request() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "RejectRequest",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//
//                                // clear gatter setter class
//                                helplist.clear();
//                                Parent_dashboard_Api();
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(getActivity());
//                                alert();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("parent_id", parent_id);
//                params.put("group_id", group_id);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//
//    private void delete_Devicerequest() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Reject_Member_Request",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//
//                                // clear gatter setter class
//                                helplist.clear();
//                                Parent_dashboard_Api();
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(getActivity());
//                                alert();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("parent_child_id", parent_child_id);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//    public void Delete_request() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Alert");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Are you sure, You want to reject request?");
//
//        // On pressing the OK button.
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                delete_request();
//            }
//        });
//
//        // On pressing the cancel button
//        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//    public void Delete_Devicerequest() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Alert");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Are you sure, You want to reject request?");
//
//        // On pressing the OK button.
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                delete_Devicerequest();
//            }
//        });
//
//        // On pressing the cancel button
//        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//
//}
