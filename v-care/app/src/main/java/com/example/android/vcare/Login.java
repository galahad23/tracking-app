package com.example.android.vcare;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.android.vcare.LocationPackage.CurrentLocationService;
import com.example.android.vcare.model.DBAdapter;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.User_Detail;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private CoordinatorLayout coordinatorLayout;
    ProgressDialog pDialog;
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    Toolbar toolbar;
    EditText email, password, forgot_email;
    Button login, reset_password;
    TextView signup, backtologin, forgot_password, txt_term;
    ImageView cancel;
    String strforgetemail = "", personName = "", gmail = "";
    private TextInputLayout inputLayoutmobile, inputLayoutotp;

    AppController aController;
    AsyncTask<Void, Void, Void> mRegisterTask;
    HashMap<String, String> user;
    public static final int REQUEST_CODE = 102;
    String device_id = "", stremail = "", strpassword = "";
    String parent_id = "", namee = "", emaill = "", phone_number = "", otpstatus = "", strlat = "", strlong = "";
    public static String status;
    String otp_status;

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    Button fb;
    String user_id = "", name = "", email_id = "", gender = "", firstname = "", lastname = "", google_id = "";

    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    Button google_login;
    GPSTracker gps;
    int versionCode;

    /*Fire base Google login*/
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        AndroidPermissions.check(Login.this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has " + permissions[0];
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        device_id = tm.getDeviceId();
                        Log.e("device_id", "" + device_id);

                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];


                        ActivityCompat.requestPermissions(Login.this
                                , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE}
                                , REQUEST_CODE);
                    }
                })
                .check();


        gethashkey();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        databaseHandler = new DatabaseHandler(this);
        feeditem = new ArrayList<User_Detail>();

        gps = new GPSTracker(Login.this);


        // check if GPS enabled*/

        if (gps.canGetLocation()) {

        } else {

            gps.showSettingsAlert();
        }


        // GCM notification
        DBAdapter.init(this);
        aController = (AppController) getApplicationContext();
       /* GCMRegistrar.checkDevice(getApplicationContext());
        GCMRegistrar.checkManifest(getApplicationContext());
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                Config.DISPLAY_REGISTRATION_MESSAGE_ACTION));
        GCMRegistrar.register(getApplicationContext(), Config.GOOGLE_SENDER_ID);
        regId = GCMRegistrar.getRegistrationId(getApplicationContext());
        Log.e("regIdmukesh", "idd" + regId);*/
/*
        // for get all phone contact
        get_phone_all_contact();*/


        inputLayoutmobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inputLayoutotp = (TextInputLayout) findViewById(R.id.input_layout_otp);
        email = (EditText) findViewById(R.id.phone_number);
        password = (EditText) findViewById(R.id.otp);
        signup = (TextView) findViewById(R.id.signup);
        forgot_password = (TextView) findViewById(R.id.forgot_pasword);
        txt_term = (TextView) findViewById(R.id.txt_term);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_window();
            }
        });

        email.addTextChangedListener(new MyTextWatcher(email));
        password.addTextChangedListener(new MyTextWatcher(password));

        login = (Button) findViewById(R.id.login_button);
        String text = "<font color=#999999>By continuing, you are indicating that you have read the that you have read the</font> <font color=#3b5998>Term And Conditions</font>";
        txt_term.setText(Html.fromHtml(text));
        txt_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, TermsConditionActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aController = (AppController) getApplicationContext();
               /* GCMRegistrar.checkDevice(getApplicationContext());
                GCMRegistrar.checkManifest(getApplicationContext());
                registerReceiver(mHandleMessageReceiver, new IntentFilter(
                        Config.DISPLAY_REGISTRATION_MESSAGE_ACTION));
                GCMRegistrar.register(getApplicationContext(), Config.GOOGLE_SENDER_ID);
                regId = GCMRegistrar.getRegistrationId(getApplicationContext());
                Log.e("regIdmukesh", "idd" + regId);*/


                // Check Marshmellow Permission on Real time
                AndroidPermissions.check(Login.this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
                        .hasPermissions(new Checker.Action0() {
                            @Override
                            public void call(String[] permissions) {
                                String msg = "Permission has " + permissions[0];
                                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                device_id = tm.getDeviceId();
                                Log.e("device_id", "" + device_id);

                                submitForm();
                            }
                        })
                        .noPermissions(new Checker.Action1() {
                            @Override
                            public void call(String[] permissions) {
                                String msg = "Permission has no " + permissions[0];


                                ActivityCompat.requestPermissions(Login.this
                                        , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE}
                                        , REQUEST_CODE);
                            }
                        })
                        .check();


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);

            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        google_login = (Button) findViewById(R.id.google_login);

        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d("onAuthState:signed_in:", user.getUid());
                    if (user.getDisplayName() != null) {
                        personName = user.getDisplayName();
                        //String personPhotoUrl = acct.getPhotoUrl().toString();
                        gmail = user.getEmail();
                        google_id = user.getUid();
                        Constant.google_id = google_id;

                        Log.e("personName", "Name: " + personName + ", email: " + gmail
                                + ", google_id: " + google_id);

                        Gmail_login_Api_check();
                        google_signOut();
                    } else {
                        Toast.makeText(Login.this, "Unable to process", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    private void gethashkey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void google_signOut() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {

            }
        });
    }


    public void Alert_dialouge() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);

        // Setting Dialog Title
        alertDialog.setTitle("Do you want to update?");
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("This app use google play services only for optional features");

        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                final String appPackageName = "com.google.android.gms";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                // User cancelled the dialog
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



/*
    private void handleSignInResult(GoogleSignInResult result) {
        Log.e("handleSignInResult", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            personName = acct.getDisplayName();
            //String personPhotoUrl = acct.getPhotoUrl().toString();
            gmail = acct.getEmail();
            google_id = acct.getId();
            Constant.google_id = google_id;

            Log.e("personName", "Name: " + personName + ", email: " + gmail
                    + ", google_id: " + google_id);

            Gmail_login_Api_check();

          */
/*  txtName.setText(personName);
            txtEmail.setText(email);*//*


            //  updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.d("isSuccess-----", result.isSuccess() + "");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                Log.d("Error", result.toString() + "-----------" + data.getData());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebaseAuthWithGoogle:", acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("signInWit:onComplete:", String.valueOf(task.isSuccessful()));

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("signInWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.e("onConnectionFailed", "onConnectionFailed:" + connectionResult);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // check google api version
        try {
            versionCode = getPackageManager().getPackageInfo("com.google.android.gms", 0).versionCode;
            Log.e("versionCode", "versionCode>>>>  " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (versionCode > 9610000) {

        } else {

            Alert_dialouge();
        }

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.fblogin);

        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        fb = (Button) findViewById(R.id.fb_login);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });
    }


    private void submitForm() {

        if (!validatemobile()) {
            return;
        }

        if (!validatpassword()) {
            return;
        } else {

            Login_api();
        }

    }


    private boolean validatemobile() {

        stremail = email.getText().toString().trim();

        if (stremail.isEmpty() || !isValidEmail(stremail)) {
            inputLayoutmobile.setError("Please enter your email address");
            requestFocus(email);
            return false;
        } else {
            inputLayoutmobile.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatpassword() {
        strpassword = password.getText().toString().trim();
        if (strpassword.length() == 0) {
            inputLayoutotp.setError("Enter password.");
            requestFocus(password);
            return false;
        } else {
            inputLayoutotp.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
                case R.id.phone_number:
                    validatemobile();
                    break;
                case R.id.otp:
                    validatpassword();
                    break;
            }
        }
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

           /* //Clear internal resources.
            GCMRegistrar.onDestroy(this);*/

        } catch (Exception e) {

            //Log.e("UnRegister Receiver Error", ">" + e.getMessage());

        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        AndroidPermissions.result(Login.this)
                .addPermissions(REQUEST_CODE, android.Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
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

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            //  progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {

                                user_id = object.getString("id").toString();
                                email_id = object.getString("email").toString();
                                name = object.getString("name").toString();
                                gender = object.getString("gender").toString();

                                Constant.facebook_id = user_id;

                                // for facebook profile picture
                                //URL img_value = new URL("http://graph.facebook.com/"+user_id+"/picture?type=large");

                                // for logout facebook automatically
                                LoginManager.getInstance().logOut();

                                StringTokenizer tokenizer = new StringTokenizer(name, " ");
                                firstname = tokenizer.nextToken();
                                lastname = tokenizer.nextToken();

                               /* // GCM token
                                GCMRegistrar.checkDevice(getApplicationContext());
                                GCMRegistrar.checkManifest(getApplicationContext());
                                registerReceiver(mHandleMessageReceiver, new IntentFilter(
                                        Config.DISPLAY_REGISTRATION_MESSAGE_ACTION));
                                GCMRegistrar.register(getApplicationContext(), Config.GOOGLE_SENDER_ID);
                                regId = GCMRegistrar.getRegistrationId(getApplicationContext());
                                Log.e("regIdmukesh", "idd" + regId);*/

                                fb_login_check();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //   Toast.makeText(Sign_Up_With.this, "welcome " +name, Toast.LENGTH_LONG).show();
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            //progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            // progressDialog.dismiss();
        }
    };


    private void Login_api() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "login",
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
                                        otp_status = userDetail.getaddress();
                                        parent_id = userDetail.getId();

                                        Log.e("otp_status", "otp_status>>" + otp_status);

                                    }


                                    if (otp_status.equals("0")) {
                                        Intent intent = new Intent(getApplicationContext(), Otp.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        databaseHandler.resetTables("riderinfo");
                                        databaseHandler.Add_Rider(namee, "", emaill, phone_number, otpstatus, parent_id, status, "", "", "");

                                        contactList_api();
                                    }

                                }

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
                params.put("email_id", stremail);
                params.put("password", strpassword);
                params.put("gcm_token", FirebaseInstanceId.getInstance().getToken());
                params.put("device_id", device_id);
                params.put("is_login", "0");

                Log.e("Insertttt", params.toString());
                return params;
            }
        };
        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);
    }


    private void contactList_api() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Please wait....");
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
                                // for start service
                                startService(new Intent(getApplicationContext(), CurrentLocationService.class));
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
                params.put("phone_number", Constant.contactlist);
                Log.e("Insertttt", params.toString());
                return params;
            }
        };
        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);
    }


    PopupWindow popup;

    private void popup_window() {
        // TODO Auto-generated method stub
        try {
            LayoutInflater inflater = (LayoutInflater) Login.this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.forgot_password, null);
            forgot_email = (EditText) layout.findViewById(R.id.forgot_email);
            reset_password = (Button) layout.findViewById(R.id.reset_password);
            backtologin = (TextView) layout.findViewById(R.id.backtologin);
            cancel = (ImageView) layout.findViewById(R.id.cancel);

            backtologin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();

                }
            });


            reset_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strforgetemail = forgot_email.getText().toString();

                    if (strforgetemail.isEmpty() || !isValidEmail(strforgetemail)) {
                        Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    } else {
                        Forgot_password();
                    }
                }
            });

            popup = new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            popup.setFocusable(true);
            popup.update();
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            popup.setAnimationStyle(R.style.animationName);
            popup.setOutsideTouchable(false);
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

    private void Forgot_password() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "forgotpassword",
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

                                popup.dismiss();

                            } else if (success == 0) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                popup.dismiss();
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
                params.put("email_id", strforgetemail);


                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    private void Gmail_login_Api_check() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Please wait....");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "parentgmaillogincheck",
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
                                String msg = objJson.getString("text");
                                //  Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), Signup.class);
                                intent.putExtra("person_name", personName);
                                intent.putExtra("gmail", gmail);
                                intent.putExtra("google_id", google_id);
                                startActivity(intent);

                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            } else if (success == 5) {

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
                params.put("gmail_id", google_id);
                params.put("device_id", device_id);
                params.put("gcm_token", FirebaseInstanceId.getInstance().getToken());
                params.put("is_login", "0");
                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }


    private void fb_login_check() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Please wait....");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "parentfacebooklogincheck",
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
                                String msg = objJson.getString("text");
                                // Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
                                LoginManager.getInstance().logOut();
                                Constant.check_fb = "click";
                                Intent intent = new Intent(getApplicationContext(), Signup.class);
                                startActivity(intent);


                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            } else if (success == 5) {

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
                params.put("facebook_id", user_id);
                params.put("device_id", device_id);
                params.put("gsm_token", FirebaseInstanceId.getInstance().getToken());
                params.put("is_login", "0");
                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
