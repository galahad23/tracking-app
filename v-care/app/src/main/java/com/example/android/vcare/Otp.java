package com.example.android.vcare;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.vcare.model.DBAdapter;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.User_Detail;
import com.example.android.vcare.model.User_function;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Otp extends AppCompatActivity {
    ProgressDialog pDialog;
    User_function user_function = new User_function();
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    Button login, resend;
    EditText otptext;
    private TextInputLayout inputLayoutotp;
    String strotp;
    String parent_id = "", namee = "", emaill = "", phone_number = "", jsonpassword = "", otpstatus = "";

    AppController aController;
    AsyncTask<Void, Void, Void> mRegisterTask;
    HashMap<String, String> user;
    public static final int REQUEST_CODE = 102;
    String device_id = "";
    public static String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        databaseHandler = new DatabaseHandler(this);

        databaseHandler = new DatabaseHandler(Otp.this);
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


        Log.e("parent_id", "parent_id>>" + parent_id);


        // GCM notification
        DBAdapter.init(this);
        aController = (AppController) getApplicationContext();


        // Check Marshmellow Permission on Real time
        AndroidPermissions.check(this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has " + permissions[0];
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        device_id = tm.getDeviceId();
                        Log.e("device_id", device_id);

                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];


                        ActivityCompat.requestPermissions(Otp.this
                                , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE}
                                , REQUEST_CODE);
                    }
                })
                .check();


        inputLayoutotp = (TextInputLayout) findViewById(R.id.input_layout_otp);
        otptext = (EditText) findViewById(R.id.otp);
        login = (Button) findViewById(R.id.login);
        resend = (Button) findViewById(R.id.resend);
        otptext.addTextChangedListener(new MyTextWatcher(otptext));

        // for get all phone contact
        //  get_phone_all_contact();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aController = (AppController) getApplicationContext();
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                device_id = tm.getDeviceId();
                Log.e("device_id", device_id);

                Submit_otp();

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resend_otp();
            }
        });


    }

    // Create a broadcast receiver to get message and show on screen
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);

        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);



        } catch (Exception e) {

            //Log.e("UnRegister Receiver Error", ">" + e.getMessage());

        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        AndroidPermissions.result(Otp.this)
                .addPermissions(REQUEST_CODE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE)
                .putActions(REQUEST_CODE, new com.nobrain.android.permissions.Result.Action0() {
                    @Override
                    public void call() {
                        //Device id
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        device_id = tm.getDeviceId();
                        Log.e("device_id", device_id);
                    }
                }, new com.nobrain.android.permissions.Result.Action1() {
                    @Override
                    public void call(String[] hasPermissions, String[] noPermissions) {
                    }
                })
                .result(requestCode, permissions, grantResults);
    }


    private void Submit_otp() {
        if (!validateotp()) {
            return;
        } else {

            otp_api();
        }
    }

    private boolean validateotp() {
        strotp = otptext.getText().toString().trim();
        if (strotp.length() == 0) {
            inputLayoutotp.setError("Please enter OTP received");
            requestFocus(otptext);
            return false;
        } else {
            inputLayoutotp.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.otp:
                    validateotp();
                    break;
            }
        }
    }


    private void otp_api() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(Otp.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "checkotp",
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
                                JSONArray array = objJson.getJSONArray("parentinfo");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsobj = array.getJSONObject(i);
                                    parent_id = jsobj.getString("parent_id");
                                    namee = jsobj.getString("name");
                                    emaill = jsobj.getString("email");
                                    phone_number = jsobj.getString("phone_number");
                                    otpstatus = jsobj.getString("otp_status");
                                    status = jsobj.getString("is_parent");

                                    String mobiletoken = objJson.getString("mobile_token");
                                    databaseHandler.resetTables("token");
                                    databaseHandler.add_token(mobiletoken);
                                    databaseHandler.resetTables("riderinfo");
                                    databaseHandler.Add_Rider(namee, "", emaill, phone_number, otpstatus, parent_id, status, "", "", "");


                                    Cursor cursor = databaseHandler.get_rider_detail();
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        for (int k = 0; k < cursor.getCount(); k++) {
                                            User_Detail detail = new User_Detail();
                                            detail.setAddress(cursor.getString(4));
                                            detail.setId(cursor.getString(5));
                                            feeditem.add(detail);
                                            cursor.moveToNext();
                                        }
                                        cursor.close();
                                    }

                                    for (User_Detail userDetail : feeditem) {
                                        parent_id = userDetail.getId();
                                    }
                                    // contact list api
                                    contactList_api();

                                }

                            } else if (success == 0) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


                            } else if (success == 2) {

                                otptext.setText("");
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
                params.put("otp", strotp);
                params.put("gcm_token", FirebaseInstanceId.getInstance().getToken());
                params.put("device_id", device_id);

                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }


    private void Resend_otp() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(Otp.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "resendotp",
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
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                login.setVisibility(View.VISIBLE);
                                resend.setVisibility(View.GONE);

                            } else if (success == 0) {


                            } else if (success == 2) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                               /* Snackbar snackbar = Snackbar.make(coordinatorLayout,msg, Snackbar.LENGTH_LONG);
                                snackbar.show();*/
                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                               /* Snackbar snackbar = Snackbar.make(coordinatorLayout,msg, Snackbar.LENGTH_LONG);
                                snackbar.show();*/
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

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    private void contactList_api() {
        // TODO Auto-generated method stub

        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "phone_number",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finishAffinity();

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

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parent_id", parent_id);
                params.put("phone_number", Constant.contactlist);
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
