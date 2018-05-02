package com.example.android.vcare;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
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
import android.widget.Button;
import android.widget.CheckBox;
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

public class Select_member_group extends AppCompatActivity {
    User_function user_function = new User_function();
    ProgressDialog pDialog;
    Toolbar toolbar;
    Button create;
    String parent_id,group_id,base_64;
    String data = "",idList="";
    Fragment fragment = null;
    LinearLayout member;

    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    private List<Child_detail> helplist = new ArrayList<>();
    private Select_member_adapter mAdapter;
    private ListView recyclerView;
    EditText search;
    // GET CONTACT
    ArrayList<String> contactList= new ArrayList<String>();
    Set<String> hs = new HashSet<>();
    Cursor cursor;
    int counter;
    String phoneNumber;
    public static final int REQUEST_CODE = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member_group);


        databaseHandler = new DatabaseHandler(this);
        feeditem = new ArrayList<User_Detail>();
        Cursor cursor = databaseHandler.get_rider_detail();
        if (cursor != null){
            cursor.moveToFirst();
            for (int i =0 ; i< cursor.getCount(); i++){
                User_Detail detail = new User_Detail();
                detail.setId(cursor.getString(5));
                feeditem.add(detail);
                cursor.moveToNext();
            }
            cursor.close();
        }

        for (User_Detail userDetail : feeditem){
            parent_id  = userDetail.getId();
        }


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Member");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        group_id = intent.getStringExtra("group_id");

        create = (Button)findViewById(R.id.create);
        member = (LinearLayout)findViewById(R.id.add_group_layout);
        recyclerView = (ListView) findViewById(R.id.recycleview);
        mAdapter = new Select_member_adapter(this,helplist);
        recyclerView.setAdapter(mAdapter);

        search = (EditText)findViewById(R.id.edit_search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AndroidPermissions.check(getApplicationContext()).permissions(android.Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
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

                        ActivityCompat.requestPermissions(Select_member_group.this
                                , new String[]{android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS}
                                , REQUEST_CODE);
                    }
                })
                .check();


        selectmemberlist();




        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                data = "";
                List<Child_detail> stList = ((Select_member_adapter) mAdapter)
                        .getStudentist();

                for (int i = 0; i < stList.size(); i++) {
                    Child_detail singleStudent = stList.get(i);
                    if (singleStudent.isSelected() == true) {

                        data = data + singleStudent.getId().toString() + ",";
                    }
                }

                if (data.isEmpty()) {
                    Toast.makeText(Select_member_group.this,
                            "Please select member.", Toast.LENGTH_LONG)
                            .show();
                } else {
                    data = data.substring(0, data.length() - 1);
                    Add_new_mwmber_group_api();

               /* data = "";
                //List<Integer> list = doctor_list.getSelectedIndices();

                int size = dr_id.size();

                if (size == 0) {

                    Toast.makeText(getApplicationContext(), "Please select member", Toast.LENGTH_SHORT).show();

                } else {

                    for (int i = 0; i < size; i++) {

                        int id = dr_id.get(i);

                        data += id + ",";
                    }
                    data = data.substring(0, data.length() - 1);

                    Log.e("doctor_id", "doctor_id>> " + data);

                    Add_new_mwmber_group_api();*/

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        AndroidPermissions.result()
                .addPermissions(REQUEST_CODE, android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS)
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


    private void selectmemberlist() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(Select_member_group.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"Not_Added_Member_list",
                new Response.Listener<String>()
                {
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
                                JSONArray array = objJson.getJSONArray("group_member_list");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsobj = array.getJSONObject(i);
                                    Child_detail item = new Child_detail();
                                    String member_name = jsobj.getString("name");
                                    int    member_id =   jsobj.getInt("parent_id");

                                   // doctor_name.add(member_name);
                                   // doctors_id.add(member_id);

                                    item.setName(jsobj.getString("name"));
                                    item.setId(jsobj.getString("parent_id"));
                                    item.setParent_name(jsobj.getString("profile_image"));

                                    helplist.add(item);

                                }
                                mAdapter.notifyDataSetChanged();
                                member.setVisibility(View.GONE);
                                search.setVisibility(View.VISIBLE);
                                create.setVisibility(View.VISIBLE);


                               /* multi_slect_adapter = new ArrayAdapter<String>(Select_member_group.this,
                                        R.layout.spinner, doctor_name);

                                chip.setAdapter(multi_slect_adapter);*/

                            }else if(success == 0) {

                            }
                            else if (success==2) {
                                String msg = objJson.getString("text");
                                Toast.makeText(Select_member_group.this, msg, Toast.LENGTH_LONG).show();
                                member.setVisibility(View.VISIBLE);
                                create.setVisibility(View.GONE);
                                search.setVisibility(View.GONE);

                            } else if (success==3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(Select_member_group.this,msg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error.Response", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();



                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("group_id", group_id);
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


    private void Add_new_mwmber_group_api() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(Select_member_group.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"Add_New_Member",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {

                                finish();

                            } else if(success == 0) {
                                user_function.logoutUser(Select_member_group.this);
                                alert();
                            }
                            else if (success==2) {
                                String msg = objJson.getString("text");
                                Toast.makeText(Select_member_group.this,msg, Toast.LENGTH_LONG).show();

                            } else if (success==3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(Select_member_group.this,msg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error.Response", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("group_id", group_id);
                params.put("member_id",data);
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Select_member_group.this);
        alertDialogBuilder.setMessage("Your Session is Expired. Please Login Again");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent i = new Intent(Select_member_group.this, Login.class);
                startActivity(i);
                Select_member_group.this.finishAffinity();

            }
        });
        alertDialogBuilder.setTitle("Alert");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                // this is your adapter that will be filtered
                mAdapter.getFilter().filter(newText);
                System.out.println("on text chnge text: "+newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                // this is your adapter that will be filtered
                mAdapter.getFilter().filter(query);
                System.out.println("on query submit: "+query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);

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


    public void getNumber(ContentResolver cr)
    {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            System.out.println(".................."+phoneNumber);
            // contactList.add(phoneNumber);

            if (phoneNumber == null){
                contactList.isEmpty();
            }else {
                contactList.add(phoneNumber.toString());
                hs.addAll(contactList);
                contactList.clear();
                contactList.addAll(hs);
                idList = hs.toString();
                idList  = idList+",";
                idList = idList.substring(0, idList.length() - 1).replace(", ",",");
                Constant.contactlist = idList;
            }

        }
        phones.close();// close cursor

    }

    private void contactList_api() {
        // TODO Auto-generated method stub

        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"phone_number",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {
                                selectmemberlist();
                               /* Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finishAffinity();*/
                            } else if(success == 0) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();

                            }
                            else if (success==2) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();

                            } else if (success==3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error.Response", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("parent_id", parent_id);
                params.put("phone_number",idList);
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


    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "I would like to invite you to download this V-Care Apps, a traceability system and risk management for your love one.  It also provides a closer communication link among your friends, family in the case of emergency. Download : https://www.dropbox.com/current_speed/fdgt6btgjp95ktt/V-Care%20demo%20apps.apk?dl=0");
        startActivity(Intent.createChooser(share, "Share via"));
    }

    public class Select_member_adapter extends BaseAdapter implements Filterable {

        CheckBox member;
        TextView user;
        ImageView userimage;
        String name,citys;
        private Activity activity;
        private LayoutInflater inflater;
        private List<Child_detail> help_details;
        List<Child_detail> mStringFilterList;
        ValueFilter valueFilter;
        //ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        public Select_member_adapter(Activity activity,  List<Child_detail> exhiitem) {
            this.activity=activity;
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
            if (convertView == null)
                convertView = inflater.inflate(R.layout.selectmember_list_adapter, null);


            final Child_detail list = help_details.get(position);

            member = (CheckBox) convertView.findViewById(R.id.checkbox);
            user   = (TextView)convertView.findViewById(R.id.username);
            userimage = (ImageView)convertView.findViewById(R.id.userimage);


            final int pos = position;
            final String image = list.getParent_name();

            if (image.isEmpty()){

            }else {
                Picasso.with(activity)
                        .load(image)
                        .error(R.drawable.loadingicon)
                        .placeholder(R.drawable.loadingicon)
                        .into(userimage);
            }


            user.setText(list.getName());
            member.setChecked(help_details.get(position).isSelected());
            member.setTag(help_details.get(position));


            member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Child_detail contact = (Child_detail) cb.getTag();
                    contact.setSelected(cb.isChecked());
                    help_details.get(pos).setSelected(cb.isChecked());
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

                final   FilterResults results = new FilterResults();
                final ArrayList<Child_detail> filterList = new ArrayList<Child_detail>();
                if (constraint != null && constraint.length() > 0) {

                    for (final Child_detail g : mStringFilterList) {
                        if (g.getName().toLowerCase()
                                .contains(constraint.toString()))
                            filterList.add(g);
                       /* if (g.getApplicantName().toLowerCase()
                                .contains(constraint.toString()))
                            filterList.add(g);
                    *//*  if (g.getCetegery().toLowerCase()
                            .contains(constraint.toString()))
                        filterList.add(g);
                    if (g.getCity().toLowerCase()
                            .contains(constraint.toString()))
                        filterList.add(g);*/
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





}
