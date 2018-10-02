package com.example.android.vcare.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.android.vcare.R;
import com.example.android.vcare.common.Constants;
import com.example.android.vcare.databinding.ActivityMainBinding;
import com.example.android.vcare.ui.BaseActivity;
import com.example.android.vcare.ui.login.ChangePasswordActivity;
import com.example.android.vcare.ui.profile.EditProfileActivity;
import com.example.android.vcare.ui.settings.FAQActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends BaseActivity {

    public static void restart(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    //    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
//    static final Double EARTH_RADIUS = 6371.00;
//    private static final String TAG = MainActivity2.class.getSimpleName();
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//    public static TextView title, username;
//    public static ImageView userimage;
//    public static String parent_id = "", gps_status = "", wifi_status = "", position_class = "", address = "";
//    public static int level;
//    public static SpannableString current_speed = SpannableString.valueOf("0km/h");
//    private static Data data;
//    // Location updates intervals in sec
//    private static int UPDATE_INTERVAL = 10000; // 10 sec
//    private static int FATEST_INTERVAL = 5000; // 5 sec
//    private static int DISPLACEMENT = 10; // 0 meters
//    final Handler handler = new Handler();
//    ProgressDialog pDialog;
//    UserHandler user_handler;
//    DatabaseHandler databaseHandler;
//    ExpandableListView expListView;
//    HashMap<String, List<String>> listDataChild;
    private MenuExpandableListAdapter listAdapter;
    //    List<String> listDataHeader;
//    Fragment fragment = null;
//    RelativeLayout rel;
//    FrameLayout lframe;
//    Toolbar toolbar;
//    String strusername;
//    GPSTracker gps;
//    String strlat, strlong;
//    NetworkInfo mWifi;
//    String pversioncode;
//    PackageInfo pInfo;
//    double old_lat = 0.0, old_long = 0.0;
//    double time = 10;
//    boolean timer1Cancel = false, timerCancel = false;
//    PowerManager powerManager;
//    PowerManager.WakeLock wl;
//    private Timer timer = new Timer();
//    private Timer timer1 = new Timer();
//    private List<User_Detail> feeditem;
//    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
//    private Data.onGpsServiceUpdate onGpsServiceUpdate;
//    private LocationManager mLocationManager;
//    private boolean firstfix;
//    private Location mLastLocation;
//    // Google client to interact with Google API
//    private GoogleApiClient mGoogleApiClient;
//    // boolean flag to toggle periodic location updates
//    private boolean mRequestingLocationUpdates = false;
//    private LocationRequest mLocationRequest;
//    // for battery level
//    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context ctxt, Intent intent) {
//            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
//        }
//    };

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setToolbarTitle(R.string.dashboard);
        setDisplayHomeAsUpEnabled();
        setHomeButtonEnabled();
        setHasBackAlert();
        initDrawer();
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        data = new Data(onGpsServiceUpdate);
//        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//        wl = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");
//        Intent intent = getIntent();
//        position_class = intent.getStringExtra("class");
//
//
//        // for battery status
//        MainActivity2.this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//
//        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//        if (mWifi.isConnected()) {
//            wifi_status = "1";
//        } else {
//            wifi_status = "0";
//        }
//
//        // for start service
//        startService(new Intent(MainActivity2.this, CurrentLocationService.class));
//
//        gps = new GPSTracker(MainActivity2.this);
//
//
//        user_handler = new UserHandler();
//        databaseHandler = new DatabaseHandler(MainActivity2.this);
//        feeditem = new ArrayList<User_Detail>();
//        Cursor cursor = databaseHandler.get_rider_detail();
//        if (cursor != null) {
//            cursor.moveToFirst();
//            for (int i = 0; i < cursor.getCount(); i++) {
//                User_Detail detail = new User_Detail();
//                detail.setName(cursor.getString(0));
//                detail.setId(cursor.getString(5));
//                feeditem.add(detail);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//
//        for (User_Detail userDetail : feeditem) {
//            strusername = userDetail.getName();
//            parent_id = userDetail.getId();
//        }
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        title = (TextView) findViewById(R.id.action_bar_text);
//        rel = (RelativeLayout) findViewById(R.id.relative_layout);
//        lframe = (FrameLayout) findViewById(R.id.container_body);
//
//
//        // check for app update
//
//        try {
//            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        pversioncode = pInfo.versionName;
//        Log.e("version", "code" + pversioncode);
//        updateversion();
//
//        if (gps.canGetLocation()) {
//            gps_status = "1";
//
//        } else {
//            gps_status = "0";
//            // gps.showSettingsAlert();
//        }
//
//        if (checkPlayServices()) {
//
//            // Building the GoogleApi client
//            buildGoogleApiClient();
//
//            createLocationRequest();
//        }
//
//        // for nevigation drawer
//        initDrawer();
//
//        callAsynchronousTask();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                callAsynchronousTask1();
//                handler.postDelayed(this, 900000);
//            }
//        }, 900000);  //the time is in miliseconds
//
//
//    }
//
//    private void callAsynchronousTask() {
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (mWifi.isConnected()) {
//                            wifi_status = "1";
//                        } else {
//                            wifi_status = "0";
//                        }
//                        // update latlong
//
//
//                        if (CurrentLocationService.strlat == 0.0) {
//                            address = "";
//                        } else {
//                            address = addressFromlatlng(CurrentLocationService.strlat, CurrentLocationService.strlong);
//                        }
//
//                        update_latlong();
//
//                    }
//                });
//            }
//        };
//        if (timer != null && !timerCancel) {
//            timer.schedule(timerTask, 0, 90000); //Intervel for 90 second
//        }
//
//
//    }
//
//    private void callAsynchronousTask1() {
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        /*  try {*/
//                        // update latlong api
//
//                        if (CurrentLocationService.strlat == 0.0) {
//                            address = "";
//                        } else {
//                            address = addressFromlatlng(CurrentLocationService.strlat, CurrentLocationService.strlong);
//                        }
//
//
//                        if (CurrentLocationService.strlat != 0.0 || CurrentLocationService.strlong != 0.0) {
//                            update_route_latlong();
//
//                        } else {
//                            // for start service
//                            startService(new Intent(MainActivity2.this, CurrentLocationService.class));
//                        }
//
//                        /*} catch (Exception e) {
//                            e.printStackTrace();
//                        }*/
//                    }
//                });
//            }
//        };
//        if (timer1 != null && !timer1Cancel) {
//            timer1.schedule(timerTask, 0, 900000);  //Intervel for 15 minute
//        }
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//
//        checkPlayServices();
//
//        // Resuming the periodic location updates
//        if (mGoogleApiClient != null) {
//            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
//                startLocationUpdates();
//            }
//        }
//        super.onResume();
//
//
//        if (data == null) {
//            data = new Data(onGpsServiceUpdate);
//        } else {
//            data.setOnGpsServiceUpdate(onGpsServiceUpdate);
//        }
//
//    }
//
//    /**
//     * Method to display the location on UI
//     */
//    private void displayLocation() {
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi
//                .getLastLocation(mGoogleApiClient);
//
//        if (mLastLocation != null) {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();
//
//
//            strlat = "";
//            strlong = "";
//            strlat = String.valueOf(latitude);
//            strlong = String.valueOf(longitude);
//
//            LatLng ll = new LatLng(latitude, longitude);
//            //  Log.e("latlong", "latlong.change>>>>......." + ll);
//            gps_status = "1";
//            address = addressFromlatlng(latitude, longitude);
//
//        } else {
//            gps_status = "0";
//        }
//    }
//
//    public String addressFromlatlng(double lat, double lon) {
//
//        String strAdd = "";
//        try {
//
//            Geocoder geocoder = new Geocoder(MainActivity2.this);
//            Log.d("Geocoder value", geocoder.toString());
//
//            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//                ArrayList<String> addressFragments = new ArrayList<String>();
//                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
//
//                    addressFragments.add(returnedAddress.getAddressLine(i));
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
//                }
//                strAdd = strReturnedAddress.toString();
//                Log.w("Current loction postal", "" + strAdd);
//
//                Log.w("My Current loction", "" + strAdd);
//            } else {
//                Log.w("Current loction address", "No Address returned from Geo Coder");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.w("Current loction address", "Exception in Geo Coder");
//
//        }
//
//        return strAdd.trim();
//    }
//
//    /**
//     * Creating google api client object
//     */
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//    }
//
//    /**
//     * Creating location request object
//     */
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
//        builder.setAlwaysShow(true);
//
//        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        Log.i(TAG, "All location settings are satisfied.");
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
//
//                        try {
//                            // Show the dialog by calling startResolutionForResult(), and check the result
//                            // in onActivityResult().
//                            status.startResolutionForResult(MainActivity2.this, REQUEST_CHECK_SETTINGS);
//                        } catch (IntentSender.SendIntentException e) {
//                            Log.i(TAG, "PendingIntent unable to execute request.");
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
//                        break;
//                }
//            }
//        });
//
//    }
//
//    /**
//     * Method to verify google play services on the device
//     */
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Starting the location updates
//     */
//    protected void startLocationUpdates() {
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
//
//    }
//
//    /**
//     * Google api callback methods
//     */
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + result.getErrorCode());
//    }
//
//    @Override
//    public void onConnected(Bundle arg0) {
//
//        // Once connected with google api, get the location
//        displayLocation();
//
//        if (mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        // Assign the new location
//        mLastLocation = location;
//        try {
//
//            if (location.hasAccuracy()) {
//                SpannableString s = new SpannableString(String.format("%.0f", location.getAccuracy()) + "m");
//                s.setSpan(new RelativeSizeSpan(0.75f), s.length() - 1, s.length(), 0);
//
//                if (firstfix) {
//
//                    firstfix = false;
//                }
//            } else {
//                firstfix = true;
//            }
//
//            if (location.hasSpeed()) {
//                if (mLastLocation.getLatitude() != location.getLatitude() || mLastLocation.getLongitude() != location.getLongitude()) {
//                    String speed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed()) + "km/h";
//                    current_speed = new SpannableString(speed);
//                    current_speed.setSpan(new RelativeSizeSpan(0.25f), current_speed.length() - 4, current_speed.length(), 0);
//                } else {
//                    String speed = String.format(Locale.ENGLISH, "%.0f", 0) + "km/h";
//                    current_speed = new SpannableString(speed);
//                    Log.d("Speed", speed);
//                    current_speed.setSpan(new RelativeSizeSpan(0.25f), current_speed.length() - 4, current_speed.length(), 0);
//                }
//            } else {
//                if (mLastLocation.getLatitude() == location.getLatitude() || mLastLocation.getLongitude() == location.getLongitude()) {
//                    String speed = String.format(Locale.ENGLISH, "%.0f", 0) + "km/h";
//                    current_speed = new SpannableString(speed);
//                    Log.d("LocationNotChange", speed);
//                    current_speed.setSpan(new RelativeSizeSpan(0.25f), current_speed.length() - 4, current_speed.length(), 0);
//                } else {
//                    current_speed = current_speed_service;
//                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("Exception", e.toString());
//        }
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//
//    public void onGpsStatusChanged(int event) {
//        switch (event) {
//            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
//                int satsInView = 0;
//                int satsUsed = 0;
//                Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
//                for (GpsSatellite sat : sats) {
//                    satsInView++;
//                    if (sat.usedInFix()) {
//                        satsUsed++;
//                    }
//                }
//                if (satsUsed == 0) {
//                    data.setRunning(false);
//
//                    stopService(new Intent(getBaseContext(), GpsServices.class));
//                    firstfix = true;
//                }
//                break;
//
//            case GpsStatus.GPS_EVENT_STOPPED:
//                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//
//                    if (!((Activity) MainActivity2.this).isFinishing()) {
//                        gps.showSettingsAlert();
//                    }
//
//                }
//                break;
//            case GpsStatus.GPS_EVENT_FIRST_FIX:
//                break;
//        }
//    }
    }

    private DrawerHeaderView headerView;
    private DrawerFooterView footerView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initDrawer() {

        headerView = new DrawerHeaderView(this);
        footerView = new DrawerFooterView(this);

        List<Integer> parentList = getParentList();
        final HashMap<Integer, List<Integer>> childList = getChildList(parentList);
        listAdapter = new MenuExpandableListAdapter(this, parentList, childList);
        binding.exListView.setDividerHeight(0);
        binding.exListView.addHeaderView(headerView);
        binding.exListView.addFooterView(footerView);
        binding.exListView.setAdapter(listAdapter);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new Fragment());
        fragmentTransaction.commit();

        drawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                R.string.app_name,
                R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        binding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        binding.exListView.setOnGroupExpandListener(onGroupExpandListener);
        binding.exListView.setOnChildClickListener(onChildClickListener);
        binding.exListView.setOnGroupClickListener(onGroupClickListener);
    }

    private ExpandableListView.OnGroupExpandListener onGroupExpandListener = new ExpandableListView.OnGroupExpandListener() {
        int previousItem = -1;

        @Override
        public void onGroupExpand(int groupPosition) {
            if (groupPosition != previousItem) {
                binding.exListView.collapseGroup(previousItem);
            }
            previousItem = groupPosition;
        }
    };

    private ExpandableListView.OnChildClickListener onChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            performItemClickAction(listAdapter.getChildMenuId(groupPosition, childPosition));
            parent.collapseGroup(groupPosition);
            binding.drawerLayout.closeDrawers();
            return false;
        }
    };

    private ExpandableListView.OnGroupClickListener onGroupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
            if (listAdapter.getChildrenCount(i) == 0) {
                performItemClickAction(listAdapter.getGroupMenuId(i));
                binding.drawerLayout.closeDrawers();
                return true;
            }
            return false;
        }
    };

    @SuppressLint("SwitchIntDef")
    private void performItemClickAction(@Constants.MenuId int menuId) {
        switch (menuId) {
            case Constants.MenuId.DASHBOARD:
                break;
            case Constants.MenuId.MY_PROFILE:
                break;
            case Constants.MenuId.SUBSCRIPTION_PLAN:
                break;
            case Constants.MenuId.TRACK_MEMBER_DEVICE:
                break;
            case Constants.MenuId.SOS:
                break;
            case Constants.MenuId.ROUTE_HISTORY:
                break;
            case Constants.MenuId.MEMBER_GROUP:
                break;
            case Constants.MenuId.FAQ:
                FAQActivity.start(this);
                break;
            case Constants.MenuId.EMERGENCY_CONTACTS:
                break;
            case Constants.MenuId.TERMS_CONDITION:
                break;
            case Constants.MenuId.LOGOUT:
                MenuActionUtil.logout(this);
                break;
            //Sub Menu
            case Constants.MenuId.EDIT_PROFILE:
                EditProfileActivity.start(this);
                break;
            case Constants.MenuId.CHANGE_PASSWORD:
                ChangePasswordActivity.start(this);
                break;
            default:
                break;
        }
    }

    private HashMap<Integer, List<Integer>> getChildList(List<Integer> parentList) {
        HashMap<Integer, List<Integer>> childList = new HashMap<>();

        List<Integer> account = new ArrayList<>();
        account.add(Constants.MenuId.EDIT_PROFILE);
        account.add(Constants.MenuId.CHANGE_PASSWORD);

        childList.put(parentList.get(0), new ArrayList<Integer>());
        childList.put(parentList.get(1), account);
        childList.put(parentList.get(2), new ArrayList<Integer>());
        childList.put(parentList.get(3), new ArrayList<Integer>());
        childList.put(parentList.get(4), new ArrayList<Integer>());
        childList.put(parentList.get(5), new ArrayList<Integer>());
        childList.put(parentList.get(6), new ArrayList<Integer>());
        childList.put(parentList.get(7), new ArrayList<Integer>());
        childList.put(parentList.get(8), new ArrayList<Integer>());
        childList.put(parentList.get(9), new ArrayList<Integer>());
        childList.put(parentList.get(10), new ArrayList<Integer>());

        return childList;
    }

    private List<Integer> getParentList() {

        List<Integer> parentList = new ArrayList<>();

        parentList.add(Constants.MenuId.DASHBOARD);
        parentList.add(Constants.MenuId.MY_PROFILE);
        parentList.add(Constants.MenuId.SUBSCRIPTION_PLAN);
        parentList.add(Constants.MenuId.TRACK_MEMBER_DEVICE);
        parentList.add(Constants.MenuId.SOS);
        parentList.add(Constants.MenuId.ROUTE_HISTORY);
        parentList.add(Constants.MenuId.MEMBER_GROUP);
        parentList.add(Constants.MenuId.FAQ);
        parentList.add(Constants.MenuId.EMERGENCY_CONTACTS);
        parentList.add(Constants.MenuId.TERMS_CONDITION);
        parentList.add(Constants.MenuId.LOGOUT);

        return parentList;
    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // if nav drawer is opened, hide the action items
//        @SuppressWarnings("unused")
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(rel);
//        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//        drawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        // Pass any configuration change to the drawer toggls
//        drawerToggle.syncState();
//    }
//
//    private void Alert() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Are You Sure, You Want to Log out");
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                Logout_user();
//            }
//        });
//
//        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }
//
//
//    private void update_latlong() {
//        // TODO Auto-generated method stub
//
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Update_Member_Latlong",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.e("Update_Latlong Res", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//                                current_speed = SpannableString.valueOf("0km/h");
//                            } else if (success == 0) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(MainActivity2.this, LoginActivity.class));
//                                finish();
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
//                params.put("lataitude", String.valueOf(CurrentLocationService.strlat));
//                params.put("longiitude", String.valueOf(CurrentLocationService.strlong));
//                params.put("battery", String.valueOf(level));
//                params.put("gps_status", gps_status);
//                params.put("childmaxspeed", String.valueOf(current_speed));
//                params.put("wifi_status", wifi_status);
//                params.put("addres", address);
//                Log.e("Update_Latlong Req", params.toString());
//                return params;
//            }
//        };
//
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//    private void update_route_latlong() {
//        // TODO Auto-generated method stub
//
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "UPDATE_CHILD_HISTORY",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.e("UPDATE_CHILD Response", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//
//                                String msg = objJson.getString("text");
//                                // Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 0) {
//                                String msg = objJson.getString("text");
//                                // Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                //  Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                // Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
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
//                params.put("child_id", parent_id);
//                params.put("latitude", String.valueOf(CurrentLocationService.strlat));
//                params.put("longitude", String.valueOf(CurrentLocationService.strlong));
//                params.put("address", address);
//                Log.e("UPDATE_CHILD Req", params.toString());
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
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_item, menu);
//        return true;
//
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.share_app:
//                shareTextUrl();
//                return true;
//        }
//
//        return false;
//    }
//
//    private void Logout_user() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(MainActivity2.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "logout",
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
//                                boolean b = user_handler.logoutUser(getApplicationContext());
//                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                                startActivity(i);
//                                finishAffinity();
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
//    private void updateversion() {
//        // TODO Auto-generated method stub
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Update_Android_Version",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//
//                            String vrsncode = objJson.getString("version");
//                            Log.e("version", vrsncode);
//                            if (pversioncode.equals(vrsncode)) {
//
//                            } else {
//
//                                showAlertDialog();
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
//
//                        // error
//                        Log.e("Error.Response", error.toString());
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                //  params.put("user_id", user_id);
//
//
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
//    private void showAlertDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//
//        //Setting message manually and performing action on button click
//        builder.setMessage("There is a newer version of V care available.")
//                .setCancelable(false)
//                .setPositiveButton("Upgrade Now", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.example.android.vcareparent&hl=en"));
//                        startActivity(intent);
//                    }
//                });
//        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//            }
//        });
//
//
//        //Creating dialog box
//        AlertDialog alert = builder.create();
//        //Setting the title manually
//        alert.setTitle("Upgrade v2care");
//        alert.show();
//    }
//
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
//        share.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.share_contant));
//
//        startActivity(Intent.createChooser(share, "Share via"));
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        try {
//            if (timer != null) {
//                timer.cancel();
//                timerCancel = true;
//            }
//            if (timer1 != null) {
//                timer1.cancel();
//                timer1Cancel = true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        stopService(new Intent(MainActivity2.this, CurrentLocationService.class));
//        super.onDestroy();
//    }
//
//    @Override
//    public void onBackPressed() {
//        try {
//            if (timer != null) {
//                timer.cancel();
//                timerCancel = true;
//            }
//            if (timer1 != null) {
//                timer1.cancel();
//                timer1Cancel = true;
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        stopService(new Intent(MainActivity2.this, CurrentLocationService.class));
//        super.onBackPressed();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // If the screen is off then the device has been locked
//
//        boolean isScreenOn;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
//            assert powerManager != null;
//            isScreenOn = powerManager.isInteractive();
//        } else {
//            assert powerManager != null;
//            isScreenOn = powerManager.isScreenOn();
//        }
//
//        if (!isScreenOn && wl != null) {
//
//            wl.acquire(5 * 60 * 1000L /*10 minutes*/);
//            wl.release();
//
//        }
//    }
//
//    public class ExpandableListAdapter extends BaseExpandableListAdapter {
//        private Context _context;
//        private List<String> _listDataHeader; // header titles
//        // child data in format of header title, child title
//        private HashMap<String, List<String>> _listDataChild;
//
//        public ExpandableListAdapter(Context context, List<String> listDataHeader,
//                                     HashMap<String, List<String>> listChildData) {
//            this._context = context;
//            this._listDataHeader = listDataHeader;
//            this._listDataChild = listChildData;
//        }
//
//        @Override
//        public Object getChild(int groupPosition, int childPosititon) {
//            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                    .get(childPosititon);
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//
//        @Override
//        public View getChildView(int groupPosition, final int childPosition,
//                                 boolean isLastChild, View convertView, ViewGroup parent) {
//
//            final String childText = (String) getChild(groupPosition, childPosition);
//
//            if (convertView == null) {
//                LayoutInflater infalInflater = (LayoutInflater) this._context
//                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = infalInflater.inflate(R.layout.list_item, null);
//            }
//
//            TextView txtListChild = (TextView) convertView
//                    .findViewById(R.id.lblListItem);
//
//            txtListChild.setText(childText);
//            return convertView;
//        }
//
//        @Override
//        public int getChildrenCount(int groupPosition) {
//            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                    .size();
//        }
//
//        @Override
//        public Object getGroup(int groupPosition) {
//            return this._listDataHeader.get(groupPosition);
//        }
//
//        @Override
//        public int getGroupCount() {
//            return this._listDataHeader.size();
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public View getGroupView(int groupPosition, boolean isExpanded,
//                                 View convertView, ViewGroup parent) {
//            String headerTitle = (String) getGroup(groupPosition);
//
//            LayoutInflater infalInflater = (LayoutInflater) this._context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.list_group, null);
//
//
//            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
//            ImageView img1 = (ImageView) convertView.findViewById(R.id.imageView2);
//
//            //lblListHeader.setTypeface(null);
//            lblListHeader.setText(headerTitle);
//
//
//            if (isExpanded) {
//                img1.setImageResource(R.drawable.ic_keyboard_arrow_down_24_1);
//
//            } else if (headerTitle.equals("D A S H B O A R D")) {
//
//            } else if (headerTitle.equals("M Y  P R O F I L E")) {
//                img1.setImageResource(R.drawable.ic_keyboard_arrow_right_24);
//            } else if (headerTitle.equals("S U B S C R I P T I O N   P L A N")) {
//
//            } else if (headerTitle.equals("T R A C K   M E M B E R   D E V I C E")) {
//
//            } else if (headerTitle.equals("S O S")) {
//
//            } else if (headerTitle.equals("R O U T E   H I S T O R Y")) {
//
//            } else if (headerTitle.equals("M E M B E R   G R O U P")) {
//            } else if (headerTitle.equals("F A Q")) {
//
//            } else if (headerTitle.equals("T E R M S  &  C O N D I T I O N S")) {
//
//            } else if (headerTitle.equals("E M E R G E N C Y  C O N T A C T S")) {
//
//            } else if (headerTitle.equals("L O G  O U T")) {
//
//            } else {
//                img1.setImageResource(R.drawable.ic_keyboard_arrow_right_24);
//            }
//
//            if (groupPosition == 0) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        fragment = new Dashboard();
//                        title.setText("Dashboard");
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            fragmentTransaction.commit();
//                        }
//                        mDrawerLayout.closeDrawer(rel);
//
//                    }
//                });
//            }
//
//            if (groupPosition == 2) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("Plan_price", "Plan>>>>?" + Constant.plan_price);
//
//                        if (Constant.plan_price.equals("null")) {
//                            Intent intent = new Intent(getApplicationContext(), Select_plan.class);
//                            startActivity(intent);
//                        } else {
//
//                            fragment = new subscription_plan();
//                            title.setText("Subscription Plan");
//                            if (fragment != null) {
//                                FragmentManager fragmentManager = getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.container_body, fragment);
//                                fragmentTransaction.commit();
//                            }
//                            mDrawerLayout.closeDrawer(rel);
//                        }
//                    }
//                });
//            }
//
//            if (groupPosition == 3) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (Constant.plan_price.equals("null")) {
//                            Intent intent = new Intent(getApplicationContext(), Select_plan.class);
//                            startActivity(intent);
//                        } else {
//                            fragment = new Track_member_device();
//                            title.setText("Track Members Device");
//                            if (fragment != null) {
//                                FragmentManager fragmentManager = getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.container_body, fragment);
//                                fragmentTransaction.commit();
//                            }
//                            mDrawerLayout.closeDrawer(rel);
//                        }
//                    }
//                });
//            }
//
//            if (groupPosition == 4) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (Constant.plan_price.equals("null")) {
//                            Intent intent = new Intent(getApplicationContext(), Select_plan.class);
//                            startActivity(intent);
//                        } else {
//
//
//                            fragment = new Sos();
//                            title.setText("SOS");
//                            if (fragment != null) {
//                                FragmentManager fragmentManager = getSupportFragmentManager();
//                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                fragmentTransaction.replace(R.id.container_body, fragment);
//                                fragmentTransaction.commit();
//                            }
//                            mDrawerLayout.closeDrawer(rel);
//                        }
//                    }
//                });
//            }
//
//            if (groupPosition == 5) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (Constant.plan_price.equals("null")) {
//                            Intent intent = new Intent(getApplicationContext(), Select_plan.class);
//                            startActivity(intent);
//                        } else {
//
//                            if (Constant.view_route.equals("1")) {
//
//                                fragment = new Route_history();
//                                title.setText("Route History");
//                                if (fragment != null) {
//                                    FragmentManager fragmentManager = getSupportFragmentManager();
//                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                    fragmentTransaction.replace(R.id.container_body, fragment);
//                                    fragmentTransaction.commit();
//                                }
//                                mDrawerLayout.closeDrawer(rel);
//                            } else {
//
//                                Toast.makeText(MainActivity2.this, "Your subscribed plan is not allowed to view the route history, please upgrade your plan", Toast.LENGTH_LONG).show();
//
//                                Intent intent = new Intent(getApplicationContext(), Select_plan.class);
//                                startActivity(intent);
//                            }
//                        }
//                    }
//                });
//            }
//
//            if (groupPosition == 6) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        fragment = new Member_group();
//                        title.setText("Groups");
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            fragmentTransaction.commit();
//                        }
//                        mDrawerLayout.closeDrawer(rel);
//                    }
//                });
//            }
//            if (groupPosition == 7) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        fragment = new FAQActivity();
//                        title.setText("FAQ");
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            fragmentTransaction.commit();
//                        }
//                        mDrawerLayout.closeDrawer(rel);
//                    }
//                });
//            }
//
//            if (groupPosition == 8) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        fragment = new Emergency_Contact();
//                        title.setText("Emergency Contact");
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            fragmentTransaction.commit();
//                        }
//                        mDrawerLayout.closeDrawer(rel);
//                    }
//                });
//            }
//
//            if (groupPosition == 9) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        fragment = new TermsConditions();
//                        title.setText("Terms & Conditions");
//                        if (fragment != null) {
//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, fragment);
//                            fragmentTransaction.commit();
//                        }
//                        mDrawerLayout.closeDrawer(rel);
//                    }
//                });
//            }
//
//            if (groupPosition == 10) {
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Alert();
//                    }
//                });
//            }
//
//            return convertView;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            return true;
//        }
//    }

}



