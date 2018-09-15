//package com.example.android.vcare.pending;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Spinner;
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
//import com.example.android.vcare.model.UserHandler;
//import com.example.android.vcare.phonefield.PhoneField;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.StringTokenizer;
//
//public class Edit_chlid_info extends AppCompatActivity {
//    ProgressDialog pDialog;
//    UserHandler2 user_handler = new UserHandler2();
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    public static final int REQUEST_CODE = 102;
//    Toolbar toolbar;
//    EditText name, email, phone, speed, radious;
//    CustomPhoneInputLayout customphone;
//    int cunterycode;
//    String cuntery_code = "";
//    Button update, add_more;
//    private TextInputLayout inputLayoutname, inputLayoutemail, inputLayoutmobile, inputLayoutspeed,
//            inputLayoutzone, inputLayoutradious;
//    ImageButton imagebutton_speed, imagebutton_zone, imagebutton_minuts, imagebutton_radius;
//    String strname, stremail, strmobile, parent_id, strspeed, strtime, strradious, child_id, parent_child_id = "", cuntery_name = "";
//    String[] time_list = {"15 min", "30 min", "45 min"};
//    Spinner time;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_chlid_info);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//
//        Intent intent = getIntent();
//        child_id = intent.getStringExtra("child_id");
//
//        customphone = (CustomPhoneInputLayout) findViewById(R.id.custom_phone);
//        customphone.setDefaultCountry("MY");
//
//
//        databaseHandler = new DatabaseHandler(this);
//        feeditem = new ArrayList<User_Detail>();
//        Cursor cursor = databaseHandler.get_rider_detail();
//        if (cursor != null) {
//            cursor.moveToFirst();
//            for (int i = 0; i < cursor.getCount(); i++) {
//                User_Detail detail = new User_Detail();
//                detail.setId(cursor.getString(5));
//                feeditem.add(detail);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//
//        for (User_Detail userDetail : feeditem) {
//            parent_id = userDetail.getId();
//        }
//
//        inputLayoutname = (TextInputLayout) findViewById(R.id.input_layout_name);
//        inputLayoutemail = (TextInputLayout) findViewById(R.id.input_layout_email);
//        inputLayoutmobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
//        inputLayoutspeed = (TextInputLayout) findViewById(R.id.input_layout_speed);
//        inputLayoutradious = (TextInputLayout) findViewById(R.id.input_layout_radious);
//
//        name = (EditText) findViewById(R.id.name);
//        email = (EditText) findViewById(R.id.email);
//        phone = (EditText) findViewById(R.id.mobile);
//        speed = (EditText) findViewById(R.id.speed);
//        radious = (EditText) findViewById(R.id.radious);
//        time = (Spinner) findViewById(R.id.time);
//        update = (Button) findViewById(R.id.update);
//
//        name.addTextChangedListener(new Edit_chlid_info.MyTextWatcher(name));
//        email.addTextChangedListener(new Edit_chlid_info.MyTextWatcher(email));
//        phone.addTextChangedListener(new Edit_chlid_info.MyTextWatcher(phone));
//        speed.addTextChangedListener(new Edit_chlid_info.MyTextWatcher(speed));
//        radious.addTextChangedListener(new Edit_chlid_info.MyTextWatcher(radious));
//
//        //Creating the ArrayAdapter instance having the country list
//        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, time_list);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //Setting the ArrayAdapter data on the Spinner
//        time.setAdapter(aa);
//
//
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Submit_detail();
//            }
//        });
//
//
//        Child_info();
//    }
//
//
//    private void Submit_detail() {
//        if (!validatename()) {
//            return;
//        }
//
//        if (!validateemail()) {
//            return;
//        }
//
//        if (!validatmobile()) {
//            return;
//        }
//
//
//        if (!validatspeed()) {
//            return;
//        }
//
//        if (!validatetime()) {
//            return;
//        }
//        if (!validateradious()) {
//            return;
//        } else {
//
//            Update_child_detail();
//
//        }
//    }
//
//
//    private class MyTextWatcher implements TextWatcher {
//
//        private View view;
//
//        private MyTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//
//                case R.id.name:
//                    validatename();
//                    break;
//
//                case R.id.email:
//                    validateemail();
//                    break;
//                case R.id.mobile:
//                    validatmobile();
//                    break;
//                case R.id.speed:
//                    validatspeed();
//                    break;
//                case R.id.time:
//                    validatetime();
//                    break;
//                case R.id.radious:
//                    validateradious();
//                    break;
//            }
//
//        }
//    }
//
//
//    private boolean validatename() {
//        strname = name.getText().toString().trim();
//        if (strname.length() == 0) {
//            inputLayoutname.setError("Please enter your full name");
//            requestFocus(name);
//            return false;
//        } else {
//            inputLayoutname.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private boolean validateemail() {
//        stremail = email.getText().toString().trim();
//
//        if (stremail.isEmpty() || !isValidEmail(stremail)) {
//            inputLayoutemail.setError("Enter valid email address");
//            requestFocus(email);
//            return false;
//        } else {
//            inputLayoutemail.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private boolean validatmobile() {
//        strmobile = phone.getText().toString().trim();
//        cunterycode = PhoneField.iso_code;
//        cuntery_code = "+" + cunterycode;
//        cuntery_name = PhoneField.code;
//        if (strmobile.length() < 6 || strmobile.length() > 13) {
//            inputLayoutmobile.setError("Please enter your mobile number without country code");
//            requestFocus(phone);
//            return false;
//        } else {
//            inputLayoutmobile.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private boolean validatspeed() {
//        strspeed = speed.getText().toString().trim();
//        if (strspeed.length() == 0) {
//            inputLayoutspeed.setError("Please enter max. speed");
//            requestFocus(speed);
//            return false;
//        } else if (Integer.parseInt(strspeed) > 300) {
//            Toast.makeText(this, "Speed limit can't be more than 300km/hr", Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//            inputLayoutspeed.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//
//    private boolean validatetime() {
//        strtime = time.getSelectedItem().toString().trim();
//        StringTokenizer tokenizer = new StringTokenizer(strtime, " ");
//        strtime = tokenizer.nextToken();
//
//        return true;
//    }
//
//    private boolean validateradious() {
//        strradious = radious.getText().toString().trim();
//        if (strradious.length() == 0) {
//            inputLayoutradious.setError("Please enter radius");
//            requestFocus(radious);
//            return false;
//        } else {
//            inputLayoutradious.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private static boolean isValidEmail(String email) {
//        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            Edit_chlid_info.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//
//    private void Child_info() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Edit_chlid_info.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Child_Detail",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            parent_child_id = objJson.getString("parent_child_id");
//                            if (success == 1) {
//                                JSONArray array = objJson.getJSONArray("child_detail");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//                                    strname = jsobj.getString(("name"));
//                                    stremail = jsobj.getString("email");
//                                    strmobile = jsobj.getString("phone_number");
//                                    strspeed = jsobj.getString("child_max_speed");
//                                    strradious = jsobj.getString("radius");
//                                    strtime = jsobj.getString("time_of_interval");
//                                    String country_name = jsobj.getString("country_name");
//
//                                    name.setText(strname);
//                                    email.setText(stremail);
//                                    phone.setText(strmobile);
//                                    speed.setText(strspeed);
//                                    radious.setText(strradious);
//                                    customphone.setDefaultCountry(country_name);
//                                    PhoneField.mSpinner.setEnabled(false);
//                                    //   time.setText(strtime);
//
//                                    if (strtime.equals("15")) {
//                                        time.setSelection(0);
//                                    } else if (strtime.equals("30")) {
//                                        time.setSelection(1);
//                                    } else {
//                                        time.setSelection(2);
//                                    }
//
//                                }
//
//
//                            } else if (success == 0) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("child_id", child_id);
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
//
//    private void Update_child_detail() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Edit_chlid_info.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Update_child_detail",
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
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(), Edit_child_zone.class);
//                                intent.putExtra("parentid", parent_id);
//                                intent.putExtra("child_id", child_id);
//                                startActivity(intent);
//
//
//                            } else if (success == 0) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("name", strname);
//                params.put("email", stremail);
//                params.put("phone_number", strmobile);
//                params.put("country_code", cuntery_code);
//                params.put("country_name", cuntery_name);
//                params.put("child_max_speed", strspeed);
//                params.put("time_of_interval", strtime);
//                params.put("radius", strradious);
//                params.put("child_id", child_id);
//                params.put("parent_child_id", parent_child_id);
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
//
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
//
//}
