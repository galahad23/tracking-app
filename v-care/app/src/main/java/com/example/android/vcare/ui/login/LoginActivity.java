package com.example.android.vcare.ui.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;

import com.example.android.vcare.BuildConfig;
import com.example.android.vcare.MyApplication;
import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ActivityLoginBinding;
import com.example.android.vcare.event.AccountEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.job.FacebookLoginJob;
import com.example.android.vcare.job.GoogleLoginJob;
import com.example.android.vcare.job.LoginJob;
import com.example.android.vcare.model.User;
import com.example.android.vcare.pending.TermsConditionActivity;
import com.example.android.vcare.ui.BaseActivity;
import com.example.android.vcare.ui.main.MainActivity;
import com.example.android.vcare.util.EventBusUtil;
import com.example.android.vcare.util.ToastUtil;
import com.example.android.vcare.util.Util;
import com.example.android.vcare.widget.TextInputErrorWatcher;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    private static final int REQUEST_CODE_LOGIN_GOOGLE = 2;
    private GoogleSignInClient googleSignInClient;
    private ActivityLoginBinding binding;
//    ProgressDialog pDialog;
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    EditText password, forgot_email;
//    Button login, reset_password;
//    TextView signup, backtologin, forgot_password, txt_term;
//    ImageView cancel;
//    String strforgetemail = "", personName = "", gmail = "";

    //    AppController aController;
//    AsyncTask<Void, Void, Void> mRegisterTask;
//    HashMap<String, String> user;
//    public static final int REQUEST_CODE = 102;
//    String device_id = "", stremail = "", strpassword = "";
    //    String parent_id = "", namee = "", emaill = "", phone_number = "", otpstatus = "", strlat = "", strlong = "";
//    public static String status;
//    String otp_status;

    private CallbackManager callbackManager;
//    private LoginButton loginButton;
//    Button fb;
//    String user_id = "", name = "", email_id = "", gender = "", firstname = "", lastname = "", google_id = "";

    //    private static final int RC_SIGN_IN = 007;
//    private GoogleApiClient mGoogleApiClient;
    //    Button google_login;
//    GPSTracker gps;
//    int versionCode;

    /*Fire base Google login*/
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
    private final int hashCode = hashCode();

    @Override
    public void onClick(View view) {
        if (view == binding.login) {
            if (isValid()) {
                showLoadingDialog();
                MyApplication.addJobInBackground(new LoginJob(
                        binding.email.getText().toString(),
                        binding.password.getText().toString(),
                        hashCode
                ));
            }
        } else if (view == binding.fbLogin) {
            binding.facebookLoginButton.callOnClick();
        } else if (view == binding.googleLogin) {
            googleSignIn();
        } else if (view == binding.termsCondition) {
            TermsConditionActivity.start(this);
        } else if (view == binding.forgotPassword) {
            ForgotPasswordDialogFragment fragment = ForgotPasswordDialogFragment.newInstance();
            fragment.show(getFragmentManager(), "");
        } else if (view == binding.signUp) {
            SignUpActivity.start(this);
            finish();
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            isValid = false;
            binding.emailInputLayout.setError(getString(R.string.field_cannot_empty));
        }
        if (TextUtils.isEmpty(password)) {
            isValid = false;
            binding.passwordInputLayout.setError(getString(R.string.field_cannot_empty));
        }
        if (isValid) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailInputLayout.setError(getString(R.string.invalid_email_format));
                isValid = false;
            }
        }
        return isValid;
    }

    private void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_LOGIN_GOOGLE);
    }

    private void initGoogleLogin() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String googleId = account.getId();
            String email = account.getEmail();
            String name = account.getDisplayName();
//            String socialPicture = account.getPhotoUrl() == null ? "" : account.getPhotoUrl().toString();
            User user = new User()
                    .setName(name)
                    .setEmail(email)
                    .setGoogleId(googleId);
            MyApplication.addJobInBackground(new GoogleLoginJob(user, hashCode()));
            googleSignInClient.signOut();
        } catch (ApiException e) {
            Log.w("google", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void initFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        binding.facebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        binding.facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            ToastUtil.show(LoginActivity.this, response.getError().getErrorMessage());
                        } else {
                            if (Profile.getCurrentProfile() != null) {
                                showLoadingDialog();
                                String facebookId = Profile.getCurrentProfile().getId();
                                String email = object.optString("email");
                                String name = object.optString("name");
//                                String profilePicUrl = "https://graph.facebook.com/" + facebookId + "/picture?width=960&height=960";
                                User user = new User()
                                        .setFacebookId(facebookId)
                                        .setEmail(email)
                                        .setName(name);
                                MyApplication.addJobInBackground(new FacebookLoginJob(user, hashCode));
                            }
                        }
                        LoginManager.getInstance().logOut();
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, gender, birthday, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                ToastUtil.show(LoginActivity.this, getString(R.string.fb_login_fail));
            }

            @Override
            public void onError(FacebookException exception) {
                ToastUtil.show(LoginActivity.this, getString(R.string.fb_login_fail));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.email.addTextChangedListener(new TextInputErrorWatcher(binding.emailInputLayout));
        binding.password.addTextChangedListener(new TextInputErrorWatcher(binding.passwordInputLayout));

        initFacebookLogin();
        initGoogleLogin();
        binding.login.setOnClickListener(this);
        binding.googleLogin.setOnClickListener(this);
        binding.fbLogin.setOnClickListener(this);
        binding.termsCondition.setOnClickListener(this);
        binding.forgotPassword.setOnClickListener(this);
        binding.signUp.setOnClickListener(this);

        if (BuildConfig.DEBUG) {
            binding.email.setText("mrinkika@gmail.com");
            binding.password.setText("123456");
        }
//        setToolbarTitle(R.string.login);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        AndroidPermissions.check(LoginActivity.this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
//                .hasPermissions(new Checker.Action0() {
//                    @Override
//                    public void call(String[] permissions) {
//                        String msg = "Permission has " + permissions[0];
//                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                        device_id = tm.getDeviceId();
//                        Log.e("device_id", "" + device_id);
//
//                    }
//                })
//                .noPermissions(new Checker.Action1() {
//                    @Override
//                    public void call(String[] permissions) {
//                        String msg = "Permission has no " + permissions[0];
//
//
//                        ActivityCompat.requestPermissions(LoginActivity.this
//                                , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
//                                        android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE}
//                                , REQUEST_CODE);
//                    }
//                })
//                .check();


//        initHashKey();
//
//        databaseHandler = new DatabaseHandler(this);
//        feeditem = new ArrayList<User_Detail>();
//
//        gps = new GPSTracker(LoginActivity.this);
//
//        if (!gps.canGetLocation()) {
//            gps.showSettingsAlert();
//        }
//
//        DBAdapter.init(this);
//        aController = (AppController) getApplicationContext();
//
//        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                showForgotPasswordDialog();
//            }
//        });
//
//        binding.email.addTextChangedListener(new MyTextWatcher(binding.email));
//        binding.password.addTextChangedListener(new MyTextWatcher(binding.password));
//
        String text = "<font color=#999999>" + getString(R.string.term_conditions_title) + "</font> <font color=#3b5998>" + getString(R.string.term_conditions) + "</font>";
        binding.termsCondition.setText(Html.fromHtml(text));
//        binding.termsCondition.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, TermsConditionActivity.class));
//            }
//        });
//        binding.login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                aController = (AppController) getApplicationContext();
//
//                AndroidPermissions.check(LoginActivity.this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
//                        android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
//                        .hasPermissions(new Checker.Action0() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has " + permissions[0];
//                                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                                device_id = tm.getDeviceId();
//                                Log.e("device_id", "" + device_id);
//
//                                submitForm();
//                            }
//                        })
//                        .noPermissions(new Checker.Action1() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has no " + permissions[0];
//
//
//                                ActivityCompat.requestPermissions(LoginActivity.this
//                                        , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
//                                                android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE}
//                                        , REQUEST_CODE);
//                            }
//                        })
//                        .check();
//
//
//            }
//        });
//
//        binding.signUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
//                startActivity(intent);
//
//            }
//        });
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        binding.googleLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//                    }
//                } /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if (user != null) {
//                    if (user.getDisplayName() != null) {
//                        String name = user.getDisplayName();
//                        String email = user.getEmail();
//                        String googleId = user.getUid();
//                        Constant.google_id = googleId;
//
////                        Gmail_login_Api_check();
//                        google_signOut();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Log.d("", "onAuthStateChanged:signed_out");
//                }
//            }
//        };


    }

    //    private void initHashKey() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void google_signOut() {
//        FirebaseAuth.getInstance().signOut();
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(Status status) {
//
//            }
//        });
//    }
//
//
//    public void showNewReleaseDialog() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Do you want to update?");
//        alertDialog.setCancelable(false);
//
//        // Setting Dialog Message
//        alertDialog.setMessage("This app use google play services only for optional features");
//
//        // On pressing Settings button
//        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User clicked OK button
//
//                final String appPackageName = "com.google.android.gms";
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                }
//            }
//        });
//        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                finish();
//                // User cancelled the dialog
//            }
//        });
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOGIN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()) {
//                Log.d("isSuccess-----", result.isSuccess() + "");
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = result.getSignInAccount();
//                firebaseAuthWithGoogle(account);
//
//            } else {
//                Log.d("Error", result.toString() + "-----------" + data.getData());
//            }
//        }
    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d("firebaseAuthWithGoogle:", acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d("signInWit:onComplete:", String.valueOf(task.isSuccessful()));
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Log.w("signInWithCredential", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
//        // be available.
//        Log.e("onConnectionFailed", "onConnectionFailed:" + connectionResult);
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // check google api version
//        try {
//            versionCode = getPackageManager().getPackageInfo("com.google.android.gms", 0).versionCode;
//            Log.e("versionCode", "versionCode>>>>  " + versionCode);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        if (versionCode > 9610000) {
//
//        } else {
//
//            showNewReleaseDialog();
//        }
//
//        callbackManager = CallbackManager.Factory.create();
//
//        binding.facebookLoginButton.setReadPermissions("public_profile", "email", "user_friends");
//        binding.facebookLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                binding.facebookLoginButton.performClick();
//
//                binding.facebookLoginButton.setPressed(true);
//
//                binding.facebookLoginButton.invalidate();
//
//                binding.facebookLoginButton.registerCallback(callbackManager, mCallBack);
//
//                binding.facebookLoginButton.setPressed(false);
//
//                binding.facebookLoginButton.invalidate();
//
//            }
//        });
//    }
//
//
//    private void submitForm() {
//        if (!validateEmail() || !validatePassword()) {
//            return;
//        }
////        Login_api();
//    }
//
//
//    private boolean validateEmail() {
//        stremail = binding.email.getText().toString().trim();
//        if (stremail.isEmpty() || !Util.isEmailValid(stremail)) {
//            binding.emailInputLayout.setError(getString(R.string.please_enter_your_email_address));
//            binding.emailInputLayout.setEnabled(true);
//            Util.requestFocus(this, binding.email);
//            return false;
//        } else {
//            binding.emailInputLayout.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private boolean validatePassword() {
//        strpassword = binding.password.getText().toString().trim();
//        if (strpassword.length() == 0) {
//            binding.passwordInputLayout.setError(getString(R.string.please_enter_your_password));
//            binding.passwordInputLayout.setErrorEnabled(true);
//            Util.requestFocus(this, binding.password);
//            return false;
//        } else {
//            binding.passwordInputLayout.setErrorEnabled(false);
//        }
//
//        return true;
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
//                case R.id.email:
//                    validateEmail();
//                    break;
//                case R.id.password:
//                    validatePassword();
//                    break;
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mRegisterTask != null) {
//            mRegisterTask.cancel(true);
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
//        AndroidPermissions.result(LoginActivity.this)
//                .addPermissions(REQUEST_CODE, android.Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
//                .putActions(REQUEST_CODE, new com.nobrain.android.permissions.Result.Action0() {
//                    @Override
//                    public void call() {
//                        //Device id
//                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                        device_id = tm.getDeviceId();
//                        Log.e("device_id", device_id);
//
//
//                    }
//                }, new com.nobrain.android.permissions.Result.Action1() {
//                    @Override
//                    public void call(String[] hasPermissions, String[] noPermissions) {
//                    }
//                })
//                .result(requestCode, permissions, grantResults);
//    }
//
//    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//
//            //  progressDialog.dismiss();
//
//            // App code
//            GraphRequest request = GraphRequest.newMeRequest(
//                    loginResult.getAccessToken(),
//                    new GraphRequest.GraphJSONObjectCallback() {
//                        @Override
//                        public void onCompleted(
//                                JSONObject object,
//                                GraphResponse response) {
//
//                            Log.e("response: ", response + "");
//                            try {
//
//                                String facebookId = object.getString("id").toString();
//                                String email = object.getString("email").toString();
//                                String name = object.getString("name").toString();
//                                String gender = object.getString("gender").toString();
//
//                                Constant.facebook_id = facebookId;
//
//                                // for facebook profile picture
//                                //URL img_value = new URL("http://graph.facebook.com/"+user_id+"/picture?type=large");
//
//                                // for logout facebook automatically
//                                LoginManager.getInstance().logOut();
//
//                                StringTokenizer tokenizer = new StringTokenizer(name, " ");
//                                String firstName = tokenizer.nextToken();
//                                String lastName = tokenizer.nextToken();
//
////                                fb_login_check();
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            //   Toast.makeText(Sign_Up_With.this, "welcome " +name, Toast.LENGTH_LONG).show();
//                        }
//
//                    });
//
//            Bundle parameters = new Bundle();
//            parameters.putString("fields", "id,name,email,gender,birthday");
//            request.setParameters(parameters);
//            request.executeAsync();
//        }
//
//        @Override
//        public void onCancel() {
//            //progressDialog.dismiss();
//        }
//
//        @Override
//        public void onError(FacebookException e) {
//            // progressDialog.dismiss();
//        }
//    };


//
//    private void contactList_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(LoginActivity.this);
//        pDialog.setMessage("Please wait....");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "phone_number",
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
//                                // for start service
//                                startService(new Intent(getApplicationContext(), CurrentLocationService.class));
//                                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
//                                startActivity(intent);
//                                finishAffinity();
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
//                        pDialog.dismiss();
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
//        req.setRetryPolicy(new DefaultRetryPolicy(30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//    }


//    PopupWindow popup;

//    private void showForgotPasswordDialog() {
//        // TODO Auto-generated method stub
//        try {
//            LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View layout = inflater.inflate(R.layout.forgot_password, null);
//            forgot_email = (EditText) layout.findViewById(R.id.forgot_email);
//            reset_password = (Button) layout.findViewById(R.id.reset_password);
//            backtologin = (TextView) layout.findViewById(R.id.backtologin);
//            cancel = (ImageView) layout.findViewById(R.id.cancel);
//
//            backtologin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popup.dismiss();
//                }
//            });
//
//            cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popup.dismiss();
//
//                }
//            });
//
//
//            reset_password.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    strforgetemail = forgot_email.getText().toString();
//
//                    if (strforgetemail.isEmpty() || !Util.isEmailValid(strforgetemail)) {
//                        Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Forgot_password();
//                    }
//                }
//            });
//
//            popup = new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//            popup.setFocusable(true);
//            popup.update();
//            popup.setBackgroundDrawable(new BitmapDrawable());
//            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//            popup.setAnimationStyle(R.style.animationName);
//            popup.setOutsideTouchable(false);
//            popup.setTouchInterceptor(new View.OnTouchListener() {
//
//                @Override
//                public boolean onTouch(View arg0, MotionEvent event) {
//                    // TODO Auto-generated method stub
//                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                        popup.dismiss();
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//
//    }

//    private void Forgot_password() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(LoginActivity.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "forgotpassword",
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
//
//                                popup.dismiss();
//
//                            } else if (success == 0) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                                popup.dismiss();
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
//                params.put("email_id", strforgetemail);
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


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        View v = getCurrentFocus();
//
//        if (v != null &&
//                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
//                v instanceof EditText &&
//                !v.getClass().getName().startsWith("android.webkit.")) {
//            int scrcoords[] = new int[2];
//            v.getLocationOnScreen(scrcoords);
//            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
//            float y = ev.getRawY() + v.getTop() - scrcoords[1];
//
//            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
//                hideKeyboard(this);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    public static void hideKeyboard(Activity activity) {
//        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
//        }
//    }


//    private void Gmail_login_Api_check() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(LoginActivity.this);
//        pDialog.setMessage("Please wait....");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "parentgmaillogincheck",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//
//                            if (success == 1) {
//
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
//                                    Cursor cursor = databaseHandler.get_rider_detail();
//                                    if (cursor != null) {
//                                        cursor.moveToFirst();
//                                        for (int k = 0; k < cursor.getCount(); k++) {
//                                            User_Detail detail = new User_Detail();
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
//                                }
//
//                            } else if (success == 0) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                //  Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
//                                intent.putExtra("person_name", personName);
//                                intent.putExtra("gmail", gmail);
//                                intent.putExtra("google_id", google_id);
//                                startActivity(intent);
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 5) {
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
//                params.put("gmail_id", google_id);
//                params.put("device_id", device_id);
//                params.put("gcm_token", FirebaseInstanceId.getInstance().getToken());
//                params.put("is_login", "0");
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }


//    private void fb_login_check() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(LoginActivity.this);
//        pDialog.setMessage("Please wait....");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "parentfacebooklogincheck",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//
//                            if (success == 1) {
//
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
//                                    Cursor cursor = databaseHandler.get_rider_detail();
//                                    if (cursor != null) {
//                                        cursor.moveToFirst();
//                                        for (int k = 0; k < cursor.getCount(); k++) {
//                                            User_Detail detail = new User_Detail();
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
//                                String msg = objJson.getString("text");
//                                // Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//                                LoginManager.getInstance().logOut();
//                                Constant.check_fb = "click";
//                                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
//                                startActivity(intent);
//
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 5) {
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
//                params.put("facebook_id", user_id);
//                params.put("device_id", device_id);
//                params.put("gsm_token", FirebaseInstanceId.getInstance().getToken());
//                params.put("is_login", "0");
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(30000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnLogin event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            MainActivity.start(this);
            finish();
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onHandle(AccountEvent.OnFacebookLogin event) {
//        if (hashCode == event.getHashCode()) {
//            ToastUtil.show(this, "Facebook Login");
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnSocialLogin event) {
        if (hashCode == event.getHashCode()) {
            MainActivity.start(this);
            dismissLoadingDialog();
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnRegisterRequire event) {
        if (hashCode == event.getHashCode()) {
            SignUpActivity.start(this, event.getUser());
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(ExceptionEvent event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            Util.showOkOnlyDisableCancelAlertDialog(this, null, event.getErrorMessage());
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
