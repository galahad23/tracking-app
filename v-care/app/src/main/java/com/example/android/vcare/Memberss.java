package com.example.android.vcare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.Group_detail_list;
import com.example.android.vcare.model.User_Detail;
import com.example.android.vcare.model.User_function;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Memberss extends Fragment {
    GPSTracker gps;
    User_function user_function = new User_function();
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    PopupWindow popup;
    ProgressDialog pDialog;
    private List<Group_detail_list> helplist = new ArrayList<>();
    private Track_member_list_adapter mAdapter;
    private RecyclerView recyclerView;
    String parent_id, mobile_token;
    LinearLayout add_kids_layout;
    Button add_kids;
    String child_id, childname, childaddress, childspeed, strwifi_status, strgps_status, strbattery;
    String gps_status;
    TextView battery;
    Group_detail_list list;
    LinearLayout action_view;

    public Memberss() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_memberss, container, false);

        databaseHandler = new DatabaseHandler(getActivity());
        feeditem = new ArrayList<User_Detail>();
        Cursor cursor = databaseHandler.get_rider_detail();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                User_Detail detail = new User_Detail();
                detail.setId(cursor.getString(5));
                feeditem.add(detail);
                cursor.moveToNext();
            }
            cursor.close();
        }

        for (User_Detail userDetail : feeditem) {
            parent_id = userDetail.getId();
        }

        Cursor cursor1 = databaseHandler.get_token_detail();
        if (cursor1 != null) {
            cursor1.moveToFirst();
            for (int j = 0; j < cursor1.getCount(); j++) {
                mobile_token = cursor1.getString(0);
                cursor1.moveToNext();
            }

            cursor1.close();
        }

        Log.e("parent_id", "parent_id>>" + parent_id);
        Log.e("mobiletoken", "mobiletokenn>>" + mobile_token);


        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            gps_status = "1";

        } else {
            gps_status = "0";
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        add_kids_layout = (LinearLayout) rootView.findViewById(R.id.add_kids_layout);
        add_kids = (Button) rootView.findViewById(R.id.add_kids);

        add_kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), TrackDeviceList.class);
                startActivity(intent);

            }
        });

        add_kids_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), TrackDeviceList.class);
                startActivity(intent);

            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        mAdapter = new Track_member_list_adapter(helplist);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        return rootView;
    }

    @Override
    public void onResume() {


        helplist.clear();
        Track_member_list_detail_Api();

        super.onResume();
    }

    private void Track_member_list_detail_Api() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "childlist",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {
                                JSONArray array = objJson.getJSONArray("child");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsobj = array.getJSONObject(i);
                                    Group_detail_list item = new Group_detail_list();
                                    item.setName(jsobj.getString("name"));
                                    item.setLast(jsobj.getString("is_active"));
                                    item.setId(jsobj.getString("parent_id"));
                                    item.setAddress(jsobj.getString("address"));
                                    item.setEmail(jsobj.getString("latitude"));
                                    item.setMobile(jsobj.getString("longitude"));
                                    item.setImage(jsobj.getString("profile_image"));
                                    item.setParent_child_id(jsobj.getString("parent_child_id"));

                                    helplist.add(item);
                                }

                                mAdapter.notifyDataSetChanged();

                                add_kids_layout.setVisibility(View.GONE);

                            } else if (success == 0) {
                                user_function.logoutUser(getActivity());
                                alert();
                            } else if (success == 2) {
                                add_kids.setVisibility(View.GONE);
                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error.Response", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parent_id", parent_id);
                Log.e("Insertttt", params.toString());
                return params;
            }
        };
        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    // Alert dialouge

    private void alert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Your Session is Expired. Please Login Again");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
                getActivity().finishAffinity();

            }
        });
        alertDialogBuilder.setTitle("Alert");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public class Track_member_list_adapter extends RecyclerView.Adapter<Track_member_list_adapter.MyViewHolder> {

        private List<Group_detail_list> help_details;
        Context context;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView membername, member_addres;
            /* LinearLayout view_layout,edit_ly;*/
            ImageView on, off, userimage, img_more;

            public MyViewHolder(View view) {
                super(view);

                membername = (TextView) view.findViewById(R.id.member_name);
                member_addres = (TextView) view.findViewById(R.id.member_address);
               /* view_layout = (LinearLayout)view.findViewById(R.id.view_layout);
                edit_ly = (LinearLayout)view.findViewById(R.id.edit_ly);*/
                on = (ImageView) view.findViewById(R.id.on_device);
                off = (ImageView) view.findViewById(R.id.off_device);
                userimage = (ImageView) view.findViewById(R.id.userimage);
                img_more = (ImageView) view.findViewById(R.id.img_more);
            }
        }

        public Track_member_list_adapter(List<Group_detail_list> helplist) {
            this.help_details = helplist;
        }

        @Override
        public Track_member_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.track_members_list_adapter, parent, false);
            context = parent.getContext();
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Track_member_list_adapter.MyViewHolder holder, final int position) {

            list = help_details.get(position);

            holder.membername.setText(list.getName());

            if (list.getaddress().isEmpty()) {
                holder.member_addres.setText("Tracked user unknown location");
            } else {
                holder.member_addres.setText(list.getaddress());
            }

            final String active = list.getLast();
            String image = list.getImage();

            if (active.equals("1")) {
                holder.on.setVisibility(View.VISIBLE);
                holder.off.setVisibility(View.GONE);
//                holder.view_layout.setVisibility(View.VISIBLE);
            } else {
                holder.off.setVisibility(View.VISIBLE);
                holder.on.setVisibility(View.GONE);
             /*   holder.view_layout.setVisibility(View.GONE);*/
            }

            if (image.isEmpty()) {
                Picasso.with(context).load(R.drawable.header_icon)
                        .error(R.drawable.header_icon)
                        .placeholder(R.drawable.header_icon)
                        .into(holder.userimage);
            } else {
                Picasso.with(context).load(image)
                        .error(R.drawable.header_icon)
                        .placeholder(R.drawable.header_icon)
                        .into(holder.userimage);
            }

            holder.img_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (active.equals("1")) {

                        showPopup(holder.img_more, position, "1");
                    } else {
                        showPopup(holder.img_more, position, "0");
                    }

                }
            });
          /*  holder.view_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_black));
            holder.edit_ly.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_black));
*/
         /*   holder.view_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.view_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_green));
            child_id = list.getId();
            Child_info();

              /*      // show_popup();

                }
            });
*/
/*
            holder.edit_ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.edit_ly.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_green));
                    child_id = list.getId();

                    Intent intent = new Intent(context,Edit_chlid_info.class);
                    intent.putExtra("child_id",child_id);
                    context.startActivity(intent);
                }
            });
*/
        }

        @Override
        public int getItemCount() {
            return help_details.size();
        }
    }

    public void showPopup(View v, final int position, String show) {


        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.menu_item, null);

        LinearLayout action_view = (LinearLayout) popupView.findViewById(R.id.action_view);
        LinearLayout action_edit = (LinearLayout) popupView.findViewById(R.id.action_edit);
        LinearLayout action_del = (LinearLayout) popupView.findViewById(R.id.action_del);
        View view = (View) popupView.findViewById(R.id.view);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (show.equals("1")) {
            action_view.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        } else {
            action_view.setVisibility(View.GONE);
            view.setVisibility(View.GONE);

        }

        action_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child_id = helplist.get(position).getId();
                Child_info();
                popupWindow.dismiss();
            }
        });
        action_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child_id = helplist.get(position).getId();
                Intent intent = new Intent(getContext(), Edit_chlid_info.class);
                intent.putExtra("child_id", child_id);
                getContext().startActivity(intent);
                popupWindow.dismiss();
            }
        });

        action_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog_del(position);
                popupWindow.dismiss();


            }
        });


        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss
            }
        });

        popupWindow.showAsDropDown(v, 100, 10);
    }

    private void dailog_del(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Are You Sure, you want to delete?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                child_id = list.getId();
                deleteItem(position);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void deleteItem(final int position) {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Delete_child",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {

                                helplist.remove(position);
                                mAdapter.notifyDataSetChanged();

                            } else if (success == 0) {
                                user_function.logoutUser(getActivity());
                                alert();
                            } else if (success == 2) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error.Response", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parent_child_id", helplist.get(position).getParent_child_id());
                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }


    private void Child_info() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "childinfo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {

                                JSONObject object = objJson.getJSONObject("child");
                                childname = object.getString("name");
                                childaddress = object.getString("address");
                                childspeed = object.getString("child_max_speed");
                                strgps_status = object.getString("gps_status");
                                strwifi_status = object.getString("wifi_status");
                                strbattery = object.getString("battery");

                                show_popup();


                            } else if (success == 0) {
                                user_function.logoutUser(getActivity());
                                alert();
                            } else if (success == 2) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error.Response", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("child_id", child_id);
                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }


    private void show_popup() {
        // TODO Auto-generated method stub
        try {
            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.child_info_layout, null);


            ImageView cancel = (ImageView) layout.findViewById(R.id.cancel);
            TextView name = (TextView) layout.findViewById(R.id.name);
            TextView address = (TextView) layout.findViewById(R.id.address);
            TextView tracker = (TextView) layout.findViewById(R.id.location);
            TextView speed = (TextView) layout.findViewById(R.id.speed);
            battery = (TextView) layout.findViewById(R.id.battery);
            TextView wifi = (TextView) layout.findViewById(R.id.wifi);

            name.setText(childname);
            speed.setText(childspeed + " Km/hr");
            battery.setText(strbattery);

            if (childaddress.isEmpty()) {
                address.setText("Tracked user unknown location");
            } else {
                address.setText(childaddress);
            }


            if (strwifi_status.equals("1")) {
                wifi.setText("On");
            } else {
                wifi.setText("Off");
            }
            if (strgps_status.equals("1")) {
                tracker.setText("On");
            } else {
                tracker.setText("Off");
            }

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.notifyDataSetChanged();
                    popup.dismiss();
                }
            });

            popup = new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            popup.setFocusable(true);
            popup.update();
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            popup.setAnimationStyle(R.style.animationName);
            popup.setOutsideTouchable(true);
            popup.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popup.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


}