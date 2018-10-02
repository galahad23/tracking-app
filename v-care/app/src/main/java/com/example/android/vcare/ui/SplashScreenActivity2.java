//package com.example.android.vcare.ui;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.ContactsContract;
//import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//
//import com.example.android.vcare.LocationPackage.CurrentLocationService;
//import com.example.android.vcare.R;
//import com.example.android.vcare.model.DatabaseHandler;
//import com.example.android.vcare.model.User_Detail;
//import com.example.android.vcare.model.UserHandler;
//import com.example.android.vcare.pending.Constant;
//import com.example.android.vcare.pending.MainActivity;
//import com.example.android.vcare.ui.login.OneTimePasswordActivity;
//import com.example.android.vcare.ui.welcome.WelcomeActivity;
//import com.nobrain.android.permissions.AndroidPermissions;
//import com.nobrain.android.permissions.Checker;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class SplashScreenActivity2 extends BaseActivity {
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    private static int SPLASH_TIME_OUT = 3000;
//    UserHandler userHandler;
//    String otp_status, name, email, password, mobile, parent_id;
//    public static final int REQUEST_CODE = 102;
//
//    // GET CONTACT
//    ArrayList<String> contactList = new ArrayList<String>();
//    Set<String> hs = new HashSet<>();
//    Cursor cursor;
//    int counter;
//    String phoneNumber;
//    /*SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
//    String showAlert = "true";*/
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
//        userHandler = new UserHandler();
//
//      /*  sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        showAlert = sharedPreferences.getString("showAlert", null);
//*/
///*
//        try {
//            Intent intent = new Intent();
//            String manufacturer = android.os.Build.MANUFACTURER;
//
//            if ("xiaomi".equalsIgnoreCase(manufacturer) || "oppo".equals(manufacturer) || "vivo".equalsIgnoreCase(manufacturer) || "Letv".equalsIgnoreCase(manufacturer) || "Honor".equalsIgnoreCase(manufacturer) || "MIUI".equalsIgnoreCase(manufacturer)) {
//
//                if (showAlert.equalsIgnoreCase("true")) {
//                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//                    alertDialogBuilder.setMessage("Your Device don't allow to run background process.If you want run your app smoth enable Vcare to run background service");
//                    alertDialogBuilder.setCancelable(false);
//                    alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            editor.putString("showAlert", "false");
//                            editor.apply();
//                            dialog.dismiss();
//                        }
//                    });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//                }
//            }
//
//
//            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//            if (list.size() > 0) {
//                startActivity(intent);
//            }
//        } catch (Exception e) {
//            Crashlytics.logException(e);
//        }
//*/
//
//        databaseHandler = new DatabaseHandler(SplashScreenActivity2.this);
//        feeditem = new ArrayList<User_Detail>();
//        Cursor cursor = databaseHandler.get_rider_detail();
//        if (cursor != null) {
//            cursor.moveToFirst();
//            for (int i = 0; i < cursor.getCount(); i++) {
//                User_Detail detail = new User_Detail();
//                detail.setAddress(cursor.getString(4));
//                detail.setName(cursor.getString(0));
//                detail.setLast(cursor.getString(1));
//                detail.setEmail(cursor.getString(2));
//                detail.setMobile(cursor.getString(3));
//                detail.setId(cursor.getString(5));
//
//                feeditem.add(detail);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//
////        for (User_Detail userDetail : feeditem) {
////            otp_status = userDetail.getaddress();
////            name = userDetail.getName();
////            mobile = userDetail.getMobile();
////            email = userDetail.getEmail();
////            password = userDetail.getLast();
////
////            Log.e("otp_status", "otp_status>>" + otp_status);
////            Log.e("name", "name>>" + name);
////            Log.e("mobile", "mobile>>" + mobile);
////            Log.e("email", "email>>" + email);
////            Log.e("password", "password>>" + password);
////        }
//    }
//
//    @Override
//    protected void onResume() {
//
//        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            //Toast.makeText(getApplication(), "GPS is disable!", Toast.LENGTH_LONG).show();
//            showSettingsAlert();
//
//
//        } else {
//            if (getLocationMode(SplashScreenActivity2.this) != 3) {
//                showSettingsAlertLocationMode();
//            } else {
//                CheckPermissions();
//            }
//        }
//        super.onResume();
//    }
//
//    private void CheckPermissions() {
//
//        // Check Marshmellow Permission on Real time
//        AndroidPermissions.check(this).permissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
//                .hasPermissions(new Checker.Action0() {
//                    @Override
//                    public void call(String[] permissions) {
//                        // get phone contact number
//                        getNumber(getContentResolver());
//                        if (hasConnection()) {
//                            new Handler().postDelayed(new Runnable() {
//
//
//                                @Override
//                                public void run() {
//
//
//                                    // This method will be executed once the timer is over
//                                    // Start your app main activity
//                                    if (userHandler.isUserLoggedIn(getApplicationContext())) {
//                                        // for start service
//                                        startService(new Intent(SplashScreenActivity2.this, CurrentLocationService.class));
//
//                                        if (otp_status.equals("0")) {
//                                            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
//                                            //Intent intent = new Intent(getApplicationContext(),OneTimePasswordActivity.class);
//                                            startActivity(intent);
//                                            finishAffinity();
//                                        }
//                                        if (otp_status.equals("1")) {
//                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                            startActivity(intent);
//                                            finishAffinity();
//                                        }
//                                    } else {
//                                        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
//                                        startActivity(i);
//                                        finishAffinity();
//                                    }
//
//                                }
//                            }, SPLASH_TIME_OUT);
//                        } else {
//                            showAlertDialog();
//                        }
//                    }
//                })
//                .noPermissions(new Checker.Action1() {
//                    @Override
//                    public void call(String[] permissions) {
//                        String msg = "Permission has no " + permissions[0];
//
//                        ActivityCompat.requestPermissions(SplashScreenActivity2.this
//                                , new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}
//                                , REQUEST_CODE);
//                    }
//                })
//                .check();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
//        AndroidPermissions.result(SplashScreenActivity2.this)
//                .addPermissions(REQUEST_CODE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
//                .putActions(REQUEST_CODE, new com.nobrain.android.permissions.Result.Action0() {
//                    @Override
//                    public void call() {
//                        //Device id
//                        // get phone contact number
//                        getNumber(getContentResolver());
//
//                        if (hasConnection()) {
//                            new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app app_icon / company
//             */
//
//                                @Override
//                                public void run() {
//
//
//                                    // This method will be executed once the timer is over
//                                    // Start your app main activity
//                                    if (userHandler.isUserLoggedIn(getApplicationContext())) {
//
//                                        if (otp_status.isEmpty()) {
//                                            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
//                                            startActivity(intent);
//                                            finishAffinity();
//                                        }
//
//                                        if (otp_status.equals("0")) {
//                                            Intent intent = new Intent(getApplicationContext(), OneTimePasswordActivity.class);
//                                            startActivity(intent);
//                                            finishAffinity();
//                                        }
//                                        if (otp_status.equals("1")) {
//                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                            intent.putExtra("class_type", "dashboard");
//                                            startActivity(intent);
//                                            finishAffinity();
//                                        }
//
//
//                                    } else {
//                                        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
//                                        startActivity(i);
//                                        finishAffinity();
//                                    }
//
//                                }
//                            }, SPLASH_TIME_OUT);
//                        } else {
//                            showAlertDialog();
//                        }
//                    }
//                }, new com.nobrain.android.permissions.Result.Action1() {
//                    @Override
//                    public void call(String[] hasPermissions, String[] noPermissions) {
//
//                        finish();
//                    }
//                })
//                .result(requestCode, permissions, grantResults);
//    }
//
//
//    public void getNumber(ContentResolver cr) {
//        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//        while (phones.moveToNext()) {
//            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            //  phoneNumber = phoneNumber.replaceAll(", ","");
//
//            System.out.println(".................." + phoneNumber);
//            // contactList.add(phoneNumber);
//
//            if (phoneNumber == null) {
//                contactList.isEmpty();
//            } else {
//                contactList.add(phoneNumber.toString());
//                hs.addAll(contactList);
//                contactList.clear();
//                contactList.addAll(hs);
//                String idList = hs.toString();
//                idList = idList + ",";
//                idList = idList.substring(0, idList.length() - 1).replace(", ", ",");
//                Constant.contactlist = idList;
//
//            }
//
//        }
//        phones.close();// close cursor
//
//    }
//
//
//    public boolean hasConnection() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        if (wifiNetwork != null && wifiNetwork.isConnected()) {
//            return true;
//        }
//
//        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if (mobileNetwork != null && mobileNetwork.isConnected()) {
//            return true;
//        }
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        if (activeNetwork != null && activeNetwork.isConnected()) {
//            return true;
//        }
//
//        return false;
//    }
//
//
//    private void showAlertDialog() {
//        AlertDialog alertDialog = new AlertDialog.Builder(
//                SplashScreenActivity2.this).create();
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Issue");
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
//        // Setting Dialog Message
//        alertDialog.setMessage("It seems that you don't have internet connection !! Please turn on cellular data");
//
//        // Setting Icon to Dialog
//        alertDialog.setIcon(R.drawable.ic_warning_24dp);
//
//        // Setting OK Button
//        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // Write your code here to execute after dialog closed
//                finish();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//
//    public void showSettingsAlert() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreenActivity2.this);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("GPS settings");
//        alertDialog.setCancelable(false);
//        // Setting Dialog Message
//        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu ?");
//
//        // On pressing Settings button
//        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//
//
//            }
//        });
//
//
//        // on pressing cancel button
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                finish();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//    public void showSettingsAlertLocationMode() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreenActivity2.this);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Warning !");
//        alertDialog.setCancelable(false);
//        // Setting Dialog Message
//        alertDialog.setMessage("Your Location mode high accuracy disable Please enable high accuracy mode. ");
//
//        // On pressing Settings button
//        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//
//
//            }
//        });
//
//
//        // on pressing cancel button
//        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//                finish();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//    public int getLocationMode(Context context) {
//        try {
//            return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return 0;
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finishAffinity();
//    }
//}
//
//
//
//
