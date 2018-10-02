package com.example.android.vcare.ui.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.android.vcare.MyApplication;
import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ActivityOtpBinding;
import com.example.android.vcare.event.AccountEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.job.ResendOTPJob;
import com.example.android.vcare.job.SubmitOTPJob;
import com.example.android.vcare.ui.BaseActivity;
import com.example.android.vcare.ui.main.MainActivity;
import com.example.android.vcare.util.EventBusUtil;
import com.example.android.vcare.util.UserHandler;
import com.example.android.vcare.util.Util;
import com.example.android.vcare.widget.Countdown;
import com.example.android.vcare.widget.TextInputErrorWatcher;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class OneTimePasswordActivity extends BaseActivity {
    public static void start(Context context, boolean requireResend) {
        Intent starter = new Intent(context, OneTimePasswordActivity.class);
        starter.putExtra(REQUIRE_RESEND, requireResend);
        context.startActivity(starter);
    }
//    ProgressDialog pDialog;
//    UserHandler2 user_handler = new UserHandler2();
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    Button login, resend;
//    EditText otptext;
//    private TextInputLayout inputLayoutotp;
//    String strotp;
//    String parent_id = "", namee = "", emaill = "", phone_number = "", jsonpassword = "", otpstatus = "";
//
//    AppController aController;
//    AsyncTask<Void, Void, Void> mRegisterTask;
//    HashMap<String, String> user;
//    public static final int REQUEST_CODE = 102;
//    String device_id = "";
//    public static String status;

    private static final String REQUIRE_RESEND = "require_resend";
    private ActivityOtpBinding binding;
    private final int hashCode = hashCode();
    private static final long TAC_COOLDOWN_PERIOD = 180000L;
    private static Countdown timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        setDisplayHomeAsUpEnabled();
        setBackNavigation();
        setToolbarTitle(R.string.verification);

        if (getIntent().getBooleanExtra(REQUIRE_RESEND, false)) {
            if (timer == null) {
                attemptSendOTP();
            }
        } else {
            setupTimer(TAC_COOLDOWN_PERIOD);
        }

        binding.resend.setOnClickListener(onClickListener);
        binding.submit.setOnClickListener(onClickListener);
        binding.otp.addTextChangedListener(new TextInputErrorWatcher(binding.otpInputLayout));
    }

    private void attemptSendOTP() {
        showLoadingDialog();
        MyApplication.addJobInBackground(new ResendOTPJob(hashCode));
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == binding.submit) {
                if (isValid()) {
                    attemptSubmitOTP();
                }
            } else if (view == binding.resend) {
                attemptSendOTP();
            }
        }
    };

    private void attemptSubmitOTP() {
        String otp = binding.otp.getText().toString();
        showLoadingDialog();
        MyApplication.addJobInBackground(new SubmitOTPJob(otp, hashCode));
    }


    private boolean isValid() {
        boolean isValid = true;
        String otp = binding.otp.getText().toString();

        if (TextUtils.isEmpty(otp)) {
            isValid = false;
            binding.otpInputLayout.setError(getString(R.string.field_cannot_empty));
        }

        return isValid;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding.resend != null && timer != null) timer.onResume(binding.resend);
    }

    private void setupTimer(long endTimeInMillis) {
        timer = new Countdown(endTimeInMillis, binding.resend);
        timer.start();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnSubmitOTP event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            MainActivity.restart(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnResendOTP event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            setupTimer(TAC_COOLDOWN_PERIOD);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(ExceptionEvent event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            Util.showOkOnlyDisableCancelAlertDialog(this, null, event.getErrorMessage());
        }
    }

    //        setContentView(R.layout.activity_otp);
//
//        databaseHandler = new DatabaseHandler(this);
//
//        databaseHandler = new DatabaseHandler(OneTimePasswordActivity.this);
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
//
//        Log.e("parent_id", "parent_id>>" + parent_id);
//
//
//        // GCM notification
//        DBAdapter.init(this);
//        aController = (AppController) getApplicationContext();
//
//
//        // Check Marshmellow Permission on Real time
//        AndroidPermissions.check(this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE)
//                .hasPermissions(new Checker.Action0() {
//                    @Override
//                    public void call(String[] permissions) {
//                        String msg = "Permission has " + permissions[0];
//                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                        device_id = tm.getDeviceId();
//                        Log.e("device_id", device_id);
//
//                    }
//                })
//                .noPermissions(new Checker.Action1() {
//                    @Override
//                    public void call(String[] permissions) {
//                        String msg = "Permission has no " + permissions[0];
//
//
//                        ActivityCompat.requestPermissions(OneTimePasswordActivity.this
//                                , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE}
//                                , REQUEST_CODE);
//                    }
//                })
//                .check();
//
//
//        inputLayoutotp = (TextInputLayout) findViewById(R.id.input_layout_otp);
//        otptext = (EditText) findViewById(R.id.otp);
//        login = (Button) findViewById(R.id.login);
//        resend = (Button) findViewById(R.id.resend);
//        otptext.addTextChangedListener(new MyTextWatcher(otptext));
//
//        // for get all phone contact
//        //  get_phone_all_contact();
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                aController = (AppController) getApplicationContext();
//                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                device_id = tm.getDeviceId();
//                Log.e("device_id", device_id);
//
//                Submit_otp();
//
//            }
//        });
//
//        resend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Resend_otp();
//            }
//        });
//
//
//    }
//
//    // Create a broadcast receiver to get message and show on screen
//    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
//
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        if (mRegisterTask != null) {
//            mRegisterTask.cancel(true);
//        }
//        try {
//            // Unregister Broadcast Receiver
//            unregisterReceiver(mHandleMessageReceiver);
//
//
//
//        } catch (Exception e) {
//
//            //Log.e("UnRegister Receiver Error", ">" + e.getMessage());
//
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
//        AndroidPermissions.result(OneTimePasswordActivity.this)
//                .addPermissions(REQUEST_CODE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE)
//                .putActions(REQUEST_CODE, new com.nobrain.android.permissions.Result.Action0() {
//                    @Override
//                    public void call() {
//                        //Device id
//                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                        device_id = tm.getDeviceId();
//                        Log.e("device_id", device_id);
//                    }
//                }, new com.nobrain.android.permissions.Result.Action1() {
//                    @Override
//                    public void call(String[] hasPermissions, String[] noPermissions) {
//                    }
//                })
//                .result(requestCode, permissions, grantResults);
//    }
//
//
//    private void Submit_otp() {
//        if (!validateotp()) {
//            return;
//        } else {
//
//            otp_api();
//        }
//    }
//
//    private boolean validateotp() {
//        strotp = otptext.getText().toString().trim();
//        if (strotp.length() == 0) {
//            inputLayoutotp.setError("Please enter OTP received");
//            requestFocus(otptext);
//            return false;
//        } else {
//            inputLayoutotp.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
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
//                case R.id.otp:
//                    validateotp();
//                    break;
//            }
//        }
//    }
//
//
//    private void otp_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(OneTimePasswordActivity.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "checkotp",
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
//                                JSONArray array = objJson.getJSONArray("parentinfo");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//                                    parent_id = jsobj.getString("parent_id");
//                                    namee = jsobj.getString("name");
//                                    emaill = jsobj.getString("email");
//                                    phone_number = jsobj.getString("phone_number");
//                                    otpstatus = jsobj.getString("otp_status");
//                                    status = jsobj.getString("is_parent");
//
//                                    String mobiletoken = objJson.getString("mobile_token");
//                                    databaseHandler.resetTables("token");
//                                    databaseHandler.add_token(mobiletoken);
//                                    databaseHandler.resetTables("riderinfo");
//                                    databaseHandler.Add_Rider(namee, "", emaill, phone_number, otpstatus, parent_id, status, "", "", "");
//
//
//                                    Cursor cursor = databaseHandler.get_rider_detail();
//                                    if (cursor != null) {
//                                        cursor.moveToFirst();
//                                        for (int k = 0; k < cursor.getCount(); k++) {
//                                            User_Detail detail = new User_Detail();
//                                            detail.setAddress(cursor.getString(4));
//                                            detail.setId(cursor.getString(5));
//                                            feeditem.add(detail);
//                                            cursor.moveToNext();
//                                        }
//                                        cursor.close();
//                                    }
//
//                                    for (User_Detail userDetail : feeditem) {
//                                        parent_id = userDetail.getId();
//                                    }
//                                    // contact list api
//                                    contactList_api();
//
//                                }
//
//                            } else if (success == 0) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//
//
//                            } else if (success == 2) {
//
//                                otptext.setText("");
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("parent_id", parent_id);
//                params.put("otp", strotp);
//                params.put("gcm_token", FirebaseInstanceId.getInstance().getToken());
//                params.put("device_id", device_id);
//
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
//
//    private void Resend_otp() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(OneTimePasswordActivity.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "resendotp",
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
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                                login.setVisibility(View.VISIBLE);
//                                resend.setVisibility(View.GONE);
//
//                            } else if (success == 0) {
//
//
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                               /* Snackbar snackbar = Snackbar.make(coordinatorLayout,msg, Snackbar.LENGTH_LONG);
//                                snackbar.show();*/
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                               /* Snackbar snackbar = Snackbar.make(coordinatorLayout,msg, Snackbar.LENGTH_LONG);
//                                snackbar.show();*/
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
//
//
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
//    private void contactList_api() {
//        // TODO Auto-generated method stub
//
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "phone_number",
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
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
//                                finishAffinity();
//
//                            } else if (success == 0) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("phone_number", Constant.contactlist);
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

    @Override
    public void finish() {
        super.finish();
        timer.onFinish();
        UserHandler.setToken(this, "");
    }
}
