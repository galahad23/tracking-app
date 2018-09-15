//package com.example.android.vcare.pending;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.android.vcare.R;
//import com.example.android.vcare.ui.login.LoginActivity;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.MapsInitializer;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Member_view extends AppCompatActivity implements OnMapReadyCallback {
//    Toolbar toolbar;
//    private GoogleMap map;
//    MapView mapView;
//    GPSTracker gps;
//    String groupname, userimage, lattide, longitude, sourcelocation;
//    Double lat = 0.00, longg = 0.00;
//    int ZoomLevel;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_member_view);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        gps = new GPSTracker(Member_view.this);
//
//      /*  gps = new GPSTracker(getApplicationContext());
//        // check if GPS enabled*/
//
//        if (gps.canGetLocation()) {
//
//
//        } else {
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }
//
//
//        TextView membername = (TextView) findViewById(R.id.membername);
//
//        MapsInitializer.initialize(getApplicationContext());
//
//        mapView = (MapView) findViewById(R.id.map);
//        mapView.onCreate(savedInstanceState);
//        mapView.onResume();
//        mapView.getMapAsync(this);
//
//
//        Intent intent = getIntent();
//        groupname = intent.getStringExtra("groupname");
//        lattide = intent.getStringExtra("latitude");
//        longitude = intent.getStringExtra("longitude");
//        userimage = intent.getStringExtra("profile_image");
//
//        if (lattide.isEmpty()) {
//            lat = 0.0;
//            longg = 0.0;
//            ZoomLevel = 5;
//        } else {
//
//
//            try {
//                lat = Double.valueOf(lattide);
//                longg = Double.valueOf(longitude);
//
//                ZoomLevel = 15;
//            } catch (NumberFormatException e) {
//                //not a double
//
//                lat = 0.0;
//                longg = 0.0;
//                ZoomLevel = 5;
//                Toast.makeText(this, "Latitude Longitude not found.", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//
//        sourcelocation = addressFromlatlng(lat, longg);
//        if (TextUtils.isEmpty(sourcelocation)) {
//            sourcelocation = getReverseLocation(lat, longg);
//        }
//
//        membername.setText(groupname);
//    }
//
//    public static String getReverseLocation(Double lataddress, Double lngaddress) {
//        String address1 = "";
//        String address2 = "";
//        String city = "";
//        String state = "";
//        String country = "";
//        String county = "";
//        String PIN = "";
//        String full_address = "";
//        String formatted_address = "";
//        try {
//
//            JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lataddress + ","
//                    + lngaddress + "&sensor=true");
//            String Status = jsonObj.getString("status");
//            if (Status.equalsIgnoreCase("OK")) {
//                JSONArray Results = jsonObj.getJSONArray("results");
//                JSONObject zero = Results.getJSONObject(0);
//                formatted_address = zero.getString("formatted_address");
//                Log.e("formatted_address", "====>" + formatted_address);
//                JSONArray address_components = zero.getJSONArray("address_components");
//
//                for (int i = 0; i < address_components.length(); i++) {
//                    JSONObject zero2 = address_components.getJSONObject(i);
//                    String long_name = zero2.getString("long_name");
//                    JSONArray mtypes = zero2.getJSONArray("types");
//                    String Type = mtypes.getString(0);
//
//                    if (!TextUtils.isEmpty(long_name) || !TextUtils.isEmpty(long_name) || long_name.length() > 0) {
//                        if (Type.equalsIgnoreCase("street_number")) {
//                            address1 = long_name + " ";
//                        } else if (Type.equalsIgnoreCase("route")) {
//                            address1 = address1 + long_name;
//                        } else if (Type.equalsIgnoreCase("sublocality")) {
//                            address2 = long_name;
//                        } else if (Type.equalsIgnoreCase("locality")) {
//                            // Address2 = Address2 + long_name + ", ";
//                            city = long_name;
//                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
//                            county = long_name;
//                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
//                            state = long_name;
//                        } else if (Type.equalsIgnoreCase("country")) {
//                            country = long_name;
//                        } else if (Type.equalsIgnoreCase("postal_code")) {
//                            PIN = long_name;
//                        }
//
//                    }
//
//                    full_address = address1 + "," + address2 + "," + city + "," + state + "," + country + "," + PIN;
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return formatted_address;
//
//    }
//
//    /**
//     * method to Request for Address from Reverse API using Http Client
//     *
//     * @param url
//     * @return
//     */
//    public static JSONObject getJSONfromURL(String url) {
//
//        // initialize
//        InputStream is = null;
//        String result = "";
//        JSONObject jObject = null;
//
//        // http post
//        try {
//            org.apache.http.client.HttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
//            org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(url);
//            org.apache.http.HttpResponse response = httpclient.execute(httppost);
//            org.apache.http.HttpEntity entity = response.getEntity();
//            is = entity.getContent();
//
//        } catch (Exception e) {
//            Log.e("log_tag", "Error in http connection " + e.toString());
//        }
//
//        // convert response to string
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            is.close();
//            result = sb.toString();
//        } catch (Exception e) {
//            Log.e("log_tag", "Error converting result " + e.toString());
//        }
//
//        // try parse the string to a JSON object
//        try {
//            jObject = new JSONObject(result);
//        } catch (JSONException e) {
//            Log.e("log_tag", "Error parsing data " + e.toString());
//        }
//
//        return jObject;
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//
//        map.getUiSettings().setMapToolbarEnabled(false);
//
//        // check if GPS enabled
//        if (gps.canGetLocation()) {
//
//            double latitude = lat;
//            double longitude = longg;
//
//
//            LatLng ll = new LatLng(latitude, longitude);
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, ZoomLevel));
//
//
//            View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
//            ImageView image = (ImageView) marker.findViewById(R.id.num_txt);
//
//
//            if (userimage.isEmpty()) {
//
//            } else {
//
//                Picasso.with(getApplicationContext())
//                        .load(userimage)
//                        .placeholder(R.drawable.header_icon)
//                        .error(R.drawable.header_icon)
//                        .into(image);
//            }
//            Log.e("userimage", "userimage>>>" + userimage);
//
//
//            map.addMarker(new MarkerOptions()
//                    .position(ll)
//                    .title(sourcelocation)
//                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
//        }
//    }
//
//
//    public String addressFromlatlng(double lataddress, double lngaddress) {
//
//        String strAdd = "";
//        Geocoder geocoder = new Geocoder(Member_view.this);
//        Log.d("Geocoder value", geocoder.toString());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(lataddress, lngaddress, 1);
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder("");
//                ArrayList<String> addressFragments = new ArrayList<String>();
//                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
//                    addressFragments.add(returnedAddress.getAddressLine(i));
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
//                }
//                strAdd = returnedAddress.getAddressLine(0);
//                Log.w("Current loction postal", "" + strAdd);
//            } else {
//                Log.w("Current loction address", "No Address returned!");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.w("Current loction address", "Canont get Address!");
//        }
//        return strAdd.trim();
//    }
//
//
//    // Alert dialouge
//
//    private void alert() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Member_view.this);
//        alertDialogBuilder.setMessage("Your Session is Expired. Please LoginActivity Again");
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                Intent i = new Intent(Member_view.this, LoginActivity.class);
//                startActivity(i);
//                Member_view.this.finishAffinity();
//
//            }
//        });
//        alertDialogBuilder.setTitle("Alert");
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
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
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
//
//    // Convert a view to bitmap
//    public static Bitmap createDrawableFromView(Context context, View view) {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
//        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
//        view.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//
//        return bitmap;
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
//        share.putExtra(Intent.EXTRA_TEXT, "I would like to invite you to download this V-Care Apps, a traceability system and risk management for your love one.  It also provides a closer communication link among your friends, family in the case of emergency. Download : https://www.dropbox.com/current_speed/fdgt6btgjp95ktt/V-Care%20demo%20apps.apk?dl=0");
//        startActivity(Intent.createChooser(share, "Share via"));
//    }
//}