//package com.example.android.vcare.pending;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ListView;
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
//import com.example.android.vcare.model.User_Detail;
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
//public class Edit_child_zone extends AppCompatActivity {
//    ProgressDialog pDialog;
//    Button submit,addmore;
//    Toolbar toolbar;
//    Child_zone_adapter listadapter;
//    private List<User_Detail> feeditem = new ArrayList<>();
//    private ListView listView;
//    String list_id="",parent_child_id="",parent_id="",child_id="";
//    int i =1;
//
//    DatabaseHandler databaseHandler;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_child_zone);
//
//        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        databaseHandler = new DatabaseHandler(this);
//
//        final Intent intent = getIntent();
//        parent_id     = intent.getStringExtra("parentid");
//        child_id      = intent.getStringExtra("child_id");
//
//        Constant.child_id= child_id;
//        Constant.parent_id= parent_id;
//        Constant.parent_child_id= parent_child_id;
//
//        addmore = (Button) findViewById(R.id.add_more);
//        submit = (Button) findViewById(R.id.submit);
//
//        listView = (ListView) findViewById(R.id.listview);
//        feeditem = new ArrayList<User_Detail>();
//        listadapter = new Child_zone_adapter(Edit_child_zone.this, feeditem);
//        listView.setAdapter(listadapter);
//
//
//        addmore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(getApplicationContext(),Add_new_Zone.class);
//                intent1.putExtra("parent_child_id", Constant.parent_child_id);
//                startActivity(intent1);
//            }
//        });
//
//}
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Child_info();
//    }
//
//    private void Child_info() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Edit_child_zone.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"Child_Detail",
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
//
//                                feeditem.clear();
//
//                                Constant.parent_child_id = objJson.getString("parent_child_id");
//
//                                JSONArray array = objJson.getJSONArray("zone");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//                                    User_Detail item = new User_Detail();
//                                    item.setId(jsobj.getString("device_latlong_id"));
//                                    item.setName(jsobj.getString("start_date_time"));
//                                    item.setLast(jsobj.getString("end_date_time"));
//                                    item.setAddress(jsobj.getString("address"));
//                                    item.setEmail(jsobj.getString("latitude"));
//                                    item.setMobile(jsobj.getString("longitude"));
//
//                                    feeditem.add(item);
//
//                                }
//                                listadapter.notifyDataSetChanged();
//
//
//                            }else if(success == 0) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//                            }
//                            else if (success==2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//                            } else if (success==3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
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
//                params.put("parent_id", Constant.parent_id);
//                params.put("child_id", Constant.child_id);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//    public class Child_zone_adapter extends BaseAdapter {
//
//        private Activity activity;
//        private LayoutInflater inflater;
//        public List<User_Detail> galleryItems;
//
//
//        public Child_zone_adapter(Activity activity, List<User_Detail> galleryItems) {
//            this.activity = activity;
//            this.galleryItems = galleryItems;
//
//            // databaseHandler=new DatabaseHandler(activity);
//
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return galleryItems.size();
//        }
//
//        @Override
//        public Object getItem(int location) {
//            // TODO Auto-generated method stub
//            return galleryItems.get(location);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return galleryItems.indexOf(getItem(position));
//        }
//
//        class ViewHolder {
//            TextView indexing;
//            TextView start, end;
//            TextView zonee;
//            ImageButton delete,edit;
//        }
//
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            final Child_zone_adapter.ViewHolder holder = new Child_zone_adapter.ViewHolder();
//
//            inflater = (LayoutInflater) activity
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            convertView = inflater.inflate(R.layout.edit_zone_adapter, null);
//
//            holder.indexing = (TextView) convertView.findViewById(R.id.indexing);
//            holder.start = (TextView) convertView.findViewById(R.id.startdate);
//            holder.end = (TextView) convertView.findViewById(R.id.enddate);
//            holder.zonee = (TextView) convertView.findViewById(R.id.zone);
//            holder.ic_cross = (ImageButton) convertView.findViewById(R.id.ic_cross);
//            holder.edit = (ImageButton) convertView.findViewById(R.id.edit);
//
//            final User_Detail item = galleryItems.get(position);
//
//            holder.start.setText(item.getName());
//            holder.end.setText(item.getLast());
//            holder.zonee.setText(item.getaddress());
//
//            holder.edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    list_id = item.getId();
//                    Intent intent = new Intent(getApplicationContext(),Edit_single_zone.class);
//                    String startdate = item.getName();
//                    String enddate = item.getLast();
//                    String zone = item.getaddress();
//                    String latitude = item.getEmail();
//                    String longitude = item.getMobile();
//
//                    intent.putExtra("list_id",list_id);
//                    intent.putExtra("startdate",startdate);
//                    intent.putExtra("enddate",enddate);
//                    intent.putExtra("zone",zone);
//                    intent.putExtra("latitude",latitude);
//                    intent.putExtra("longitude",longitude);
//                    startActivity(intent);
//
//                }
//            });
//
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    list_id = item.getId();
//                    Delete_zone();
//                }
//            });
//
//            holder.zonee.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//                        HideKeyboard(activity);
//
//                    }
//
//                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
//
//                        HideKeyboard(activity);
//
//                    }
//
//                    return true;
//                }
//            });
//
//
//            return convertView;
//        }
//
//    }
//
//    public static void HideKeyboard(Activity activity) {
//        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
//        }
//    }
//
//
//
//    public void Delete_zone(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Edit_child_zone.this);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Alert");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Are you sure, You want to delete Zone?");
//
//        // On pressing the Settings button.
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                delete_zone();
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
//    private void delete_zone() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog( Edit_child_zone.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"Delete_child_latlong",
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
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText( Edit_child_zone.this, msg, Toast.LENGTH_LONG).show();
//                                Child_info();
//
//                            }else if(success == 0) {
//
//                            }
//                            else if (success==2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText( Edit_child_zone.this, msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success==3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText( Edit_child_zone.this,msg, Toast.LENGTH_LONG).show();
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
//                params.put("device_latlong_id", list_id);
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
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_item, menu);
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//
//            case R.id.share_app:
//                shareTextUrl();
//                return true;
//            default:
//
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
//    // Method to share either text or URL.
//    private void shareTextUrl() {
//        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//        share.setType("text/plain");
//        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//        // Add data to the intent, the receiving app will decide
//        // what to do with it.
//        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
//        share.putExtra(Intent.EXTRA_TEXT, "I would like to invite you to download this V-Care Apps, a traceability system and risk management for your love one.  It also provides a closer communication link among your friends, family in the case of emergency. Download : https://www.dropbox.com/current_speed/fdgt6btgjp95ktt/V-Care%20demo%20apps.apk?dl=0");
//
//        startActivity(Intent.createChooser(share, "Share via"));
//    }
//
//}
