package com.example.android.vcare;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.vcare.model.Child_detail;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.User_Detail;
import com.example.android.vcare.model.User_function;
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;
import com.nobrain.android.permissions.Result;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TrackDeviceList extends AppCompatActivity {
    User_function user_function = new User_function();
    ProgressDialog pDialog;
    Toolbar toolbar;
    String parent_id, strgroupname, base_64;
    String data = "", idList = "";
    Fragment fragment = null;
    LinearLayout member;
    private List<Child_detail> helplist = new ArrayList<>();
    private TrackDeviceListAdapter mAdapter;
    private ListView recyclerView;
    EditText search;
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;

    // GET CONTACT
    ArrayList<String> contactList = new ArrayList<String>();
    Set<String> hs = new HashSet<>();
    Cursor cursor;
    String phoneNumber;
    public static final int REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_device_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Member");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        databaseHandler = new DatabaseHandler(this);
        feeditem = new ArrayList<User_Detail>();
        Cursor cursor = databaseHandler.get_rider_detail();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                User_Detail detail = new User_Detail();
                detail.setId(cursor.getString(5));
                detail.setAddress(cursor.getString(6));

                feeditem.add(detail);
                cursor.moveToNext();
            }
            cursor.close();
        }

        for (User_Detail userDetail : feeditem) {
            parent_id = userDetail.getId();

        }

        member = (LinearLayout) findViewById(R.id.add_group_layout);
        search = (EditText) findViewById(R.id.edit_search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView = (ListView) findViewById(R.id.recycleview);
        mAdapter = new TrackDeviceListAdapter(this, helplist);
        recyclerView.setAdapter(mAdapter);

        AndroidPermissions.check(getApplicationContext()).permissions(android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has " + permissions[0];
                        // get phone contact number
                        getNumber(getContentResolver());
                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];

                        ActivityCompat.requestPermissions(TrackDeviceList.this
                                , new String[]{android.Manifest.permission.READ_CONTACTS,
                                        android.Manifest.permission.WRITE_CONTACTS}
                                , REQUEST_CODE);
                    }
                })
                .check();

    }

    @Override
    protected void onResume() {
        helplist.clear();
        mAdapter.notifyDataSetChanged();
        Add_New_Child_List();
        super.onResume();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        AndroidPermissions.result()
                .addPermissions(REQUEST_CODE, android.Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                .putActions(REQUEST_CODE, new Result.Action0() {
                    @Override
                    public void call() {
                        // get phone contact number
                        getNumber(getContentResolver());
                    }
                }, new Result.Action1() {
                    @Override
                    public void call(String[] hasPermissions, String[] noPermissions) {
                    }
                })
                .result(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void getNumber(ContentResolver cr) {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            System.out.println(".................." + phoneNumber);
            // contactList.add(phoneNumber);

            if (phoneNumber == null) {
                contactList.isEmpty();
            } else {
                contactList.add(phoneNumber.toString());
                hs.addAll(contactList);
                contactList.clear();
                contactList.addAll(hs);
                idList = hs.toString();
                idList = idList + ",";
                idList = idList.substring(0, idList.length() - 1).replace(", ", ",");
                Constant.contactlist = idList;
            }

        }
        phones.close();// close cursor

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);


        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.refresh:
                contactList_api();
                return true;

            case R.id.share_app:
                shareTextUrl();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void contactList_api() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(TrackDeviceList.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "phone_number",
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
                                Add_New_Child_List();
                            } else if (success == 0) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                            } else if (success == 2) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

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
                params.put("phone_number", idList);
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

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.share_contant));
        startActivity(Intent.createChooser(share, "Share via"));
    }

    public class TrackDeviceListAdapter extends BaseAdapter implements Filterable {

        TextView user;
        ImageView userimage, nextButton;
        String name, citys;
        LinearLayout mainLayout;
        private Activity activity;
        private LayoutInflater inflater;
        private List<Child_detail> help_details;
        List<Child_detail> mStringFilterList;
        ValueFilter valueFilter;

        //ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        public TrackDeviceListAdapter(Activity activity, List<Child_detail> exhiitem) {
            this.activity = activity;
            this.help_details = exhiitem;
            mStringFilterList = exhiitem;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return help_details.size();
        }

        @Override
        public Object getItem(int location) {
            // TODO Auto-generated method stub
            return help_details.get(location);
        }

        public List<Child_detail> getStudentist() {
            return help_details;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return help_details.indexOf(getItem(position));
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.trackdevicelistadapter, null);


            final Child_detail list = help_details.get(position);

            user = (TextView) convertView.findViewById(R.id.username);
            userimage = (ImageView) convertView.findViewById(R.id.userimage);
            nextButton = (ImageView) convertView.findViewById(R.id.nextButton);
            mainLayout = (LinearLayout) convertView.findViewById(R.id.mainLayout);


            final int pos = position;
            final String image = list.getProfile_image();


            if (image.isEmpty()) {

            } else {
                Picasso.with(activity)
                        .load(image)
                        .error(R.drawable.loadingicon)
                        .placeholder(R.drawable.loadingicon)
                        .into(userimage);
            }


            user.setText(list.getName());


            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), Add_device.class);
                    intent.putExtra("name", list.getName());
                    intent.putExtra("email", list.getEmail());
                    intent.putExtra("country_name", list.getCountry_name());
                    intent.putExtra("phone_number", list.getPhone_number());
                    startActivity(intent);

                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), Add_device.class);
                    intent.putExtra("name", list.getName());
                    intent.putExtra("email", list.getEmail());
                    intent.putExtra("country_name", list.getCountry_name());
                    intent.putExtra("phone_number", list.getPhone_number());
                    startActivity(intent);

                }
            });


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                final FilterResults results = new FilterResults();
                final ArrayList<Child_detail> filterList = new ArrayList<Child_detail>();
                if (constraint != null && constraint.length() > 0) {

                    for (final Child_detail g : mStringFilterList) {
                        if (g.getName().toLowerCase()
                                .contains(constraint.toString()))
                            filterList.add(g);
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;

            }


            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                help_details = (List<Child_detail>) results.values;
                notifyDataSetChanged();
            }

        }
    }

    private void Add_New_Child_List() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(TrackDeviceList.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Add_New_Child_List",
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
                                helplist.clear();
                                JSONArray array = objJson.getJSONArray("Member_list");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsobj = array.getJSONObject(i);
                                    Child_detail item = new Child_detail();


                                    item.setName(jsobj.getString("name"));
                                    item.setId(jsobj.getString("parent_id"));
                                    item.setEmail(jsobj.getString("email"));
                                    item.setCountry_name(jsobj.getString("country_name"));
                                    item.setPhone_number(jsobj.getString("phone_number"));
                                    item.setProfile_image(jsobj.getString("profile_image"));

                                    helplist.add(item);
                                }


                                mAdapter.notifyDataSetChanged();

                                member.setVisibility(View.GONE);
                                search.setVisibility(View.VISIBLE);

                            } else if (success == 0) {

                            } else if (success == 2) {
                                helplist.clear();
                                String msg = objJson.getString("text");
                                Toast.makeText(TrackDeviceList.this, msg, Toast.LENGTH_LONG).show();
                                member.setVisibility(View.VISIBLE);
                                search.setVisibility(View.GONE);

                                mAdapter.notifyDataSetChanged();

                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(TrackDeviceList.this, msg, Toast.LENGTH_LONG).show();
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

}
