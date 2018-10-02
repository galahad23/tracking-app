package com.example.android.vcare.ui.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.example.android.vcare.MyApplication;
import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ActivitySignUpBinding;
import com.example.android.vcare.event.AccountEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.job.FacebookLoginJob;
import com.example.android.vcare.job.GoogleLoginJob;
import com.example.android.vcare.job.SignUpJob;
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

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, SignUpActivity.class);
        context.startActivity(starter);
    }

    public static void start(Context context, User user) {
        Intent starter = new Intent(context, SignUpActivity.class);
        starter.putExtra(USER, user.toJson());
        context.startActivity(starter);
    }

    //    GPSTracker gps;
//    ProgressDialog pDialog;
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    Toolbar toolbar;
//    EditText name, email, phone, password, confirmpass;
//    TextView back;
//    Button signup;
//    private TextInputLayout inputLayoutname, inputLayoutemail, inputLayoutmobile, inputLayouteconfirmpass, inputLayoutpassword;
//    String parent_id = "", namee = "", emaill = "", phone_number = "", jsonpassword = "", otpstatus = "";
//    String strname = "", stremail = "", strphone = "", strpassword = "", strconfirmpass = "";
//    String personName = "", gmail = "";
//    public static String status;
//    int versionCode;
//    CustomPhoneInputLayout customphone;
//    int cunterycode;
//    String cuntery_code = "";
//    String cuntery_name = "";
//    TextView txt_term;
//    // For facebook login
//    private CallbackManager callbackManager;
//    private LoginButton loginButton;
//    Button fb;
//    String user_id, fb_name = "", email_id = "", gender = "";
//
//    // for GCM notification
//    AppController aController;
//    AsyncTask<Void, Void, Void> mRegisterTask;
//    HashMap<String, String> user;
//    String device_id;
//    public static final int REQUEST_CODE = 102;
//
//    // private static final String TAG = LoginActivity.class.getSimpleName();
//    private static final int RC_SIGN_IN = 007;
//    private GoogleApiClient mGoogleApiClient;
//    private ProgressDialog mProgressDialog;
//    Button google_login;
//    String google_id;
//
//    /*Fire base Google login*/
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final int REQUEST_CODE_LOGIN_GOOGLE = 2;
    private GoogleSignInClient googleSignInClient;
    private static final String USER = "user";
    private ActivitySignUpBinding binding;
    private User user;
    private CallbackManager callbackManager;
    private final int hashCode = hashCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        setToolbarTitle(R.string.sign_up);
        setTintBackButtonIcon(R.color.white, R.drawable.ic_back_black_24dp);
        setDisplayHomeAsUpEnabled();
        setBackNavigation();

        initView();
        initTnC();
        initSocialSignUp();
        initGoogleLogin();
        initFacebookLogin();
    }

    private void initView() {
        binding.name.addTextChangedListener(new TextInputErrorWatcher(binding.nameInputLayout));
        binding.email.addTextChangedListener(new TextInputErrorWatcher(binding.emailInputLayout));
        binding.phone.addTextChangedListener(new TextInputErrorWatcher(binding.phoneInputLayout));
        binding.password.addTextChangedListener(new TextInputErrorWatcher(binding.passwordInputLayout));
        binding.confirmPassword.addTextChangedListener(new TextInputErrorWatcher(binding.confirmPasswordInputLayout));

        binding.termsCondition.setOnClickListener(this);
        binding.signUp.setOnClickListener(this);
        binding.googleLogin.setOnClickListener(this);
        binding.fbLogin.setOnClickListener(this);
    }

    private void initTnC() {
        String text = "<font color=#999999>" + getString(R.string.term_conditions_title) + "</font> <font color=#3b5998>" + getString(R.string.term_conditions) + "</font>";
        binding.termsCondition.setText(Html.fromHtml(text));
    }

    private void initSocialSignUp() {
        user = new User();
        String userJson = getIntent().getStringExtra(USER);
        if (!TextUtils.isEmpty(userJson)) {
            user = User.deserialize(userJson);
            binding.name.setText(user.getName());
            binding.email.setText(user.getEmail());

            binding.email.setEnabled(false);
            binding.fbLogin.setVisibility(View.GONE);
            binding.googleLogin.setVisibility(View.GONE);
            binding.orLayout.setVisibility(View.GONE);
            binding.passwordInputLayout.setVisibility(View.GONE);
            binding.confirmPasswordInputLayout.setVisibility(View.GONE);
        }
    }

    private void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_LOGIN_GOOGLE);
    }

    private void initGoogleLogin() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.termsCondition) {
            TermsConditionActivity.start(this);
        } else if (view == binding.signUp) {
            if (isValid()) {
                showLoadingDialog();

                String name = binding.name.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                String phone = binding.phone.getText().toString();
                String countryCode = binding.phoneCode.getSelectedCountryCodeWithPlus();
                String countryName = binding.phoneCode.getSelectedCountryNameCode();

                user.setName(name)
                        .setEmail(email)
                        .setPhoneNumber(phone)
                        .setPassword(password)
                        .setCountryCode(countryCode)
                        .setCountryName(countryName);

                MyApplication.addJobInBackground(new SignUpJob(user, hashCode));
            }
        } else if (view == binding.fbLogin) {
            binding.facebookLoginButton.callOnClick();
        } else if (view == binding.googleLogin) {
            googleSignIn();
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
                            ToastUtil.show(SignUpActivity.this, response.getError().getErrorMessage());
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
                ToastUtil.show(SignUpActivity.this, getString(R.string.fb_login_fail));
            }

            @Override
            public void onError(FacebookException exception) {
                ToastUtil.show(SignUpActivity.this, getString(R.string.fb_login_fail));
            }
        });
    }

    private boolean isValid() {
        boolean isValid = true;
        String name = binding.name.getText().toString();
        String email = binding.email.getText().toString();
        String phone = binding.phone.getText().toString();
        String password = binding.password.getText().toString();
        String confirmPassword = binding.confirmPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            isValid = false;
            binding.nameInputLayout.setError(getString(R.string.field_cannot_empty));
        }

        if (TextUtils.isEmpty(email)) {
            isValid = false;
            binding.emailInputLayout.setError(getString(R.string.field_cannot_empty));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.setError(getString(R.string.invalid_email_format));
            isValid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            isValid = false;
            binding.phoneInputLayout.setError(getString(R.string.field_cannot_empty));
        }

        if (binding.passwordInputLayout.getVisibility() == View.GONE) {
            return isValid;
        }

        if (TextUtils.isEmpty(password)) {
            isValid = false;
            binding.passwordInputLayout.setError(getString(R.string.field_cannot_empty));
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            isValid = false;
            binding.confirmPasswordInputLayout.setError(getString(R.string.field_cannot_empty));
        } else if (!password.equalsIgnoreCase(confirmPassword)) {
            isValid = false;
            binding.confirmPasswordInputLayout.setError(getString(R.string.confirm_password_is_not_same_as_password));
        }

        return isValid;
    }
//        AndroidPermissions.check(SignUpActivity.this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
//                .hasPermissions(new Checker.Action0() {
//                    @Override
//                    public void call(String[] permissions) {
//                        String msg = "Permission has " + permissions[0];
//                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                        device_id = tm.getDeviceId();
//                        Log.e("device_id", "" + device_id);
//
//
//                    }
//                })
//                .noPermissions(new Checker.Action1() {
//                    @Override
//                    public void call(String[] permissions) {
//                        String msg = "Permission has no " + permissions[0];
//
//
//                        ActivityCompat.requestPermissions(SignUpActivity.this
//                                , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
//                                        android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE}
//                                , REQUEST_CODE);
//                    }
//                })
//                .check();
//
//        loginButton = new LoginButton(this);
//        databaseHandler = new DatabaseHandler(this);
//        feeditem = new ArrayList<User_Detail>();
//
//        // GCM notification
//        DBAdapter.init(this);
//        aController = (AppController) getApplicationContext();
//       /* GCMRegistrar.checkDevice(getApplicationContext());
//        GCMRegistrar.checkManifest(getApplicationContext());
//        registerReceiver(mHandleMessageReceiver, new IntentFilter(
//                Config.DISPLAY_REGISTRATION_MESSAGE_ACTION));
//        GCMRegistrar.register(getApplicationContext(), Config.GOOGLE_SENDER_ID);
//        regId = GCMRegistrar.getRegistrationId(getApplicationContext());
//        Log.e("regIdmukesh", "idd" + regId);
//*/
//        gps = new GPSTracker(SignUpActivity.this);
//      /*  gps = new GPSTracker(getApplicationContext());
//        // check if GPS enabled*/
//        if (gps.canGetLocation()) {
//
//        } else {
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }
//
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        customphone = (CustomPhoneInputLayout) findViewById(R.id.custom_phone);
//        customphone.setDefaultCountry("MY");
//
//        inputLayoutname = (TextInputLayout) findViewById(R.id.input_layout_name);
//        inputLayoutemail = (TextInputLayout) findViewById(R.id.input_layout_email);
//        inputLayoutmobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
//        inputLayoutpassword = (TextInputLayout) findViewById(R.id.input_layout_password);
//        inputLayouteconfirmpass = (TextInputLayout) findViewById(R.id.input_layout_confirm);
//
//        txt_term = (TextView) findViewById(R.id.txt_term);
//        name = (EditText) findViewById(R.id.name);
//        email = (EditText) findViewById(R.id.email);
//        phone = (EditText) findViewById(R.id.mobile);
//        password = (EditText) findViewById(R.id.password);
//        confirmpass = (EditText) findViewById(R.id.confirm);
//        back = (TextView) findViewById(R.id.back);
//        String text = "<font color=#999999>By continuing, you are indicating that you have read the that you have read the</font> <font color=#3b5998>Term And Conditions</font>";
//        txt_term.setText(Html.fromHtml(text));
//        txt_term.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SignUpActivity.this, TermsConditionActivity.class));
//            }
//        });
//        Intent intent = getIntent();
//        personName = intent.getStringExtra("person_name");
//        gmail = intent.getStringExtra("gmail");
//        google_id = intent.getStringExtra("google_id");
//
//        name.setText(personName);
//        email.setText(gmail);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        // for get all phone contact
//        //  get_phone_all_contact();
//
//        name.addTextChangedListener(new MyTextWatcher(name));
//        email.addTextChangedListener(new MyTextWatcher(email));
//        phone.addTextChangedListener(new MyTextWatcher(phone));
//        password.addTextChangedListener(new MyTextWatcher(password));
//        confirmpass.addTextChangedListener(new MyTextWatcher(confirmpass));
//
//        signup = (Button) findViewById(R.id.signup_button);
//
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                /*// GCM notification
//                aController = (AppController) getApplicationContext();
//                GCMRegistrar.checkDevice(getApplicationContext());
//                GCMRegistrar.checkManifest(getApplicationContext());
//                registerReceiver(mHandleMessageReceiver, new IntentFilter(
//                        Config.DISPLAY_REGISTRATION_MESSAGE_ACTION));
//                GCMRegistrar.register(getApplicationContext(), Config.GOOGLE_SENDER_ID);
//                regId = GCMRegistrar.getRegistrationId(getApplicationContext());
//                Log.e("regIdmukesh", "idd" + regId);*/
//
//                // Check Marshmellow Permission on Real time
//                AndroidPermissions.check(SignUpActivity.this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
//                        android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
//                        .hasPermissions(new Checker.Action0() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has " + permissions[0];
//                                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                                device_id = tm.getDeviceId();
//                                Log.e("device_id", "" + device_id);
//
//                                signup_process();
//
//                            }
//                        })
//                        .noPermissions(new Checker.Action1() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has no " + permissions[0];
//
//
//                                ActivityCompat.requestPermissions(SignUpActivity.this
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
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//
//        google_login = (Button) findViewById(R.id.google_login);
//        google_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Check Marshmellow Permission on Real time
//                AndroidPermissions.check(SignUpActivity.this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
//                        android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
//                        .hasPermissions(new Checker.Action0() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has " + permissions[0];
//                                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                                device_id = tm.getDeviceId();
//                                Log.e("device_id", "" + device_id);
//                                user_id = null;
//                                name.setText("");
//                                email.setText("");
//                                signIn();
//
//                            }
//                        })
//                        .noPermissions(new Checker.Action1() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has no " + permissions[0];
//
//
//                                ActivityCompat.requestPermissions(SignUpActivity.this
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
//                    // User is signed in
//                    Log.d("onAuthState:signed_in:", user.getUid());
//                    if (user.getDisplayName() != null) {
//                        personName = user.getDisplayName();
//                        //String personPhotoUrl = acct.getPhotoUrl().toString();
//                        gmail = user.getEmail();
//                        google_id = user.getUid();
//                        Constant.google_id = google_id;
//
//                        Log.e("personName", "Name: " + personName + ", email: " + gmail
//                                + ", google_id: " + google_id);
//                        name.setText(personName);
//                        email.setText(gmail);
//                        Gmail_login_Api_check();
//                        google_signOut();
//                    } else {
//                        Toast.makeText(SignUpActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    // User is signed out
//                    Log.d("", "onAuthStateChanged:signed_out");
//                }
//                // ...
//            }
//        };

/*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
*/
//    }

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
//    private void signup_process() {
//
//        Log.e("facebook_id", "facebook_id>>>" + user_id);
//        Log.e("google_id", "google_id>>>" + google_id);
//
//        if (user_id == null && google_id == null) {
//            submitForm();
//        }
//        if (user_id != null) {
//            Submit_facebook_form();
//        }
//        if (google_id != null) {
//            Submit_google_form();
//        }
//
//    }
//
//    private void submitForm() {
//
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
//        if (!validatepassword()) {
//            return;
//        }
//
//        if (!validateconfirmpas()) {
//            return;
//        } else {
//
//            // check_data();
//            Signup_api();
//        }
//    }
//
//
//    private void Submit_facebook_form() {
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
//        if (!validatepassword()) {
//            return;
//        }
//
//        if (!validateconfirmpas()) {
//            return;
//        } else {
//
//            //   check_data();
//            Signup_facebook_api();
//
//        }
//    }
//
//    private void Submit_google_form() {
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
//        if (!validatepassword()) {
//            return;
//        }
//
//        if (!validateconfirmpas()) {
//            return;
//        } else {
//
//            //  check_data();
//            Signup_gmail_api();
//
//        }
//    }
//
//    private boolean validatename() {
//        strname = name.getText().toString().trim();
//        Constant.strfullname = strname;
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
//        Constant.stremail = stremail;
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
//        boolean check = false;
//        strphone = phone.getText().toString().trim();
//        cunterycode = PhoneField.iso_code;
//        cuntery_code = "+" + cunterycode;
//        cuntery_name = PhoneField.code;
//        Constant.strmobile = strphone;
//        if (strphone.length() < 6 || strphone.length() > 13) {
//            check = false;
//            inputLayoutmobile.setError("Please enter your mobile number without country code");
//            requestFocus(phone);
//            return false;
//        } else {
//            check = true;
//            inputLayoutmobile.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private boolean validatepassword() {
//        strpassword = password.getText().toString();
//        Constant.strpassword = strpassword;
//        if (strpassword.length() == 0) {
//            inputLayoutpassword.setError("Enter password.");
//            requestFocus(password);
//            return false;
//        } else {
//            inputLayoutpassword.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private boolean validateconfirmpas() {
//        strconfirmpass = confirmpass.getText().toString();
//
//        if (strconfirmpass.length() == 0 || !strconfirmpass.equals(strpassword)) {
//            inputLayouteconfirmpass.setError("Password do not match.");
//            requestFocus(confirmpass);
//            return false;
//        } else {
//            inputLayouteconfirmpass.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//
//    private static boolean isValidEmail(String email) {
//        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
//                case R.id.password:
//                    validatepassword();
//                    break;
//                case R.id.confirm:
//                    validateconfirmpas();
//                    break;
//            }
//
//        }
//    }
//
//
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
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
//            //Clear internal resources.
//            /*GCMRegistrar.onDestroy(this);*/
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
//        AndroidPermissions.result(SignUpActivity.this)
//                .addPermissions(REQUEST_CODE, android.Manifest.permission.ACCESS_FINE_LOCATION,
//                        android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
//                .putActions(REQUEST_CODE, new com.nobrain.android.permissions.Result.Action0() {
//                    @Override
//                    public void call() {
//                        //Device id
//                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                        device_id = tm.getDeviceId();
//                        Log.e("device_id", "" + device_id);
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
//        } else {
//            Alert_dialouge();
//        }
//
//
//        callbackManager = CallbackManager.Factory.create();
//
//        loginButton = (LoginButton) findViewById(R.id.fblogin);
//
//        loginButton.setReadPermissions("public_profile", "email", "user_friends");
//
//        fb = (Button) findViewById(R.id.fb_login);
//        fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Check Marshmellow Permission on Real time
//                AndroidPermissions.check(SignUpActivity.this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION,
//                        android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE)
//                        .hasPermissions(new Checker.Action0() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has " + permissions[0];
//                                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//                                device_id = tm.getDeviceId();
//                                Log.e("device_id", "" + device_id);
//                                google_id = null;
//                                name.setText("");
//                                email.setText("");
//                                loginButton.performClick();
//
//                                loginButton.setPressed(true);
//
//                                loginButton.invalidate();
//
//                                loginButton.registerCallback(callbackManager, mCallBack);
//
//                                loginButton.setPressed(false);
//
//                                loginButton.invalidate();
//
//
//                            }
//                        })
//                        .noPermissions(new Checker.Action1() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has no " + permissions[0];
//
//
//                                ActivityCompat.requestPermissions(SignUpActivity.this
//                                        , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
//                                                android.Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_WIFI_STATE}
//                                        , REQUEST_CODE);
//                            }
//                        })
//                        .check();
//
//            }
//        });
//
//        if (Constant.check_fb.equals("click")) {
//
//            LoginManager.getInstance().logOut();
//            google_id = null;
//            name.setText("");
//            email.setText("");
//            loginButton.performClick();
//
//            loginButton.setPressed(true);
//
//            loginButton.invalidate();
//
//            loginButton.registerCallback(callbackManager, mCallBack);
//
//            loginButton.setPressed(false);
//
//            loginButton.invalidate();
//
//
//        }
//    }
//
//    private void Signup_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(SignUpActivity.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "singup",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            String msg = objJson.getString("text");
//                            if (success == 1) {
//                                JSONArray array = objJson.getJSONArray("parentinfo");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//                                    parent_id = jsobj.getString("parent_id");
//                                    namee = jsobj.getString("name");
//                                    emaill = jsobj.getString("email");
//                                    phone_number = jsobj.getString("phone_number");
//                                    jsonpassword = jsobj.getString("password");
//                                    otpstatus = jsobj.getString("otp_status");
//                                    status = jsobj.getString("is_parent");
//
//                                    databaseHandler.resetTables("riderinfo");
//                                    databaseHandler.Add_Rider(namee, jsonpassword, emaill, phone_number, otpstatus, parent_id, status, "", "", "");
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
//                                        otpstatus = userDetail.getaddress();
//                                        parent_id = userDetail.getId();
//                                    }
//
//                                    if (otpstatus.equals("0")) {
//                                        Intent intent = new Intent(getApplicationContext(), OneTimePasswordActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//                                        databaseHandler.resetTables("riderinfo");
//                                        databaseHandler.Add_Rider(namee, jsonpassword, emaill, phone_number, otpstatus, parent_id, status, "", "", "");
//                                        //upload contact list on server
//                                        contactList_api();
//                                    }
//                                }
//
//                            } else if (success == 0) {
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 2) {
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 3) {
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 4) {
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            }
//
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
//                params.put("full_name", strname);
//                params.put("email_address", stremail);
//                params.put("password", strpassword);
//                params.put("phone_number", strphone);
//                params.put("gcm_token", FirebaseInstanceId.getInstance().getToken());
//                params.put("device_id", device_id);
//                params.put("country_code", cuntery_code);
//                params.put("country_name", cuntery_name);
//                params.put("is_login", "0");
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
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOGIN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
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
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//
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
//
//    }
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
//                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
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
//                                user_id = object.getString("id").toString();
//                                email_id = object.getString("email").toString();
//                                fb_name = object.getString("name").toString();
//                                gender = object.getString("gender").toString();
//
//                                // for logout facebook automatically
//                                LoginManager.getInstance().logOut();
//
//                                Constant.check_fb = "";
//                                Constant.facebook_id = "";
//                                Constant.facebook_id = user_id;
//
//                                name.setText(fb_name);
//                                email.setText(email_id);
//
//                               /* name.setKeyListener(null);
//                                name.setClickable(false);
//                                email.setClickable(false);
//
//                                name.setFocusableInTouchMode(false);
//                                email.setFocusableInTouchMode(false);*/
//                                fb_login_check();
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
//
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
//
//    private void fb_login_check() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(SignUpActivity.this);
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
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                //  Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
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
//    }
//
//
//    private void Signup_facebook_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(SignUpActivity.this);
//        pDialog.setMessage("Please wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "parentfacebooksingup",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            String msg = objJson.getString("text");
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
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 2) {
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            }
//
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
//                params.put("full_name", strname);
//                params.put("email_address", stremail);
//                params.put("password", strpassword);
//                params.put("mobile_number", strphone);
//                params.put("country_code", cuntery_code);
//                params.put("gcm_token", FirebaseInstanceId.getInstance().getToken());
//                params.put("device_id", device_id);
//                params.put("country_name", cuntery_name);
//                params.put("is_login", "0");
//                Log.e("Insertttt", params.toString());
//
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
//
//    private void Signup_gmail_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(SignUpActivity.this);
//        pDialog.setMessage("Please wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "parentgmailsingup",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            String msg = objJson.getString("text");
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
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 2) {
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            }
//
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
//                params.put("full_name", strname);
//                params.put("email_address", stremail);
//                params.put("password", strpassword);
//                params.put("mobile_number", strphone);
//                params.put("country_code", cuntery_code);
//                params.put("country_name", cuntery_name);
//                params.put("gcm_token", FirebaseInstanceId.getInstance().getToken());
//                params.put("device_id", device_id);
//                params.put("is_login", "0");
//                Log.e("Insertttt", params.toString());
//
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
//    }
//
//
//    private void Gmail_login_Api_check() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(SignUpActivity.this);
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
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                // Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
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
//                params.put("gmail_id", Constant.google_id);
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
//
//
//    private void contactList_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(SignUpActivity.this);
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
//
//                                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
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
//
//    }
//
//
//    public void Alert_dialouge() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onHandle(AccountEvent.OnFacebookLogin event) {
//        if (hashCode == event.getHashCode()) {
//            ToastUtil.show(this, "Facebook Login");
//        }
//    }

    private void handleLoginResult(User user) {
        dismissLoadingDialog();
        if (user.getOtpStatus().equalsIgnoreCase("0")) {
            OneTimePasswordActivity.start(this, true);
        } else {
            MainActivity.restart(this);
        }
        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnSocialLogin event) {
        if (hashCode == event.getHashCode()) {
            handleLoginResult(event.getUser());
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
    public void onHandle(AccountEvent.OnSignUp event) {
        if (hashCode == event.getHashCode()) {
            handleLoginResult(event.getUser());
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
}
