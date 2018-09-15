//package com.example.android.vcare.pending;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.Rect;
//import android.graphics.RectF;
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
//import android.widget.Toast;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.android.vcare.AppController;
//import com.example.android.vcare.R;
//import com.example.android.vcare.model.MyItem;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.MapsInitializer;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Group_member_map extends AppCompatActivity implements OnMapReadyCallback {
//
//    ProgressDialog pDialog;
//    Toolbar toolbar;
//    private GoogleMap map;
//    MapView mapView;
//    String group_id, sourcelocation = "";
//    Double latitude = 0.00, longitude = 0.00;
//    LatLng point;
//    ArrayList<LatLng> markerPoints;
//    ArrayList<String> Title;
//    Marker marker;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_group_member_map);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        pDialog = new ProgressDialog(Group_member_map.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        final Intent intent = getIntent();
//        group_id = intent.getStringExtra("group_id");
//
//        markerPoints = new ArrayList<LatLng>();
//        Title = new ArrayList<String>();
//
//        MapsInitializer.initialize(getApplicationContext());
//
//        mapView = (MapView) findViewById(R.id.map);
//        mapView.onCreate(savedInstanceState);
//        mapView.onResume();
//        mapView.getMapAsync(this);
//
//
//    }
//
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map = googleMap;
//
//        map.getUiSettings().setMapToolbarEnabled(false);
//
//        All_member_on_map();
//
//
//    }
//
//
//    public String addressFromlatlng(double lataddress, double lngaddress) {
//
//        String strAdd = "";
//        Geocoder geocoder = new Geocoder(Group_member_map.this);
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
//    private void All_member_on_map() {
//        // TODO Auto-generated method stub
//
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "groupInfo",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if (pDialog != null && pDialog.isShowing())
//                            pDialog.dismiss();
//
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//                                List<MyItem> items = new ArrayList<MyItem>();
//                                JSONArray array = objJson.getJSONArray("group_member_list");
//                                for (int i = 0; i < array.length(); i++) {
//                                    String title = null;
//                                    String snippet = null;
//                                    JSONObject object = array.getJSONObject(i);
//                                    double lat = object.getDouble("latitudee");
//                                    double lng = object.getDouble("longitudea");
//                                    String userimage = object.getString("profile_image");
//                                    double newLat = lat + (Math.random() - .02) / 10000;// * (Math.random() * (max - min) + min);
//                                    double newLng = lng + (Math.random() - .02) / 10000;// * (Math.random() * (max - min) + min);
//                                    sourcelocation = addressFromlatlng(lat, lng);
//                                    if (TextUtils.isEmpty(sourcelocation)) {
//                                        sourcelocation = getReverseLocation(lat, lng);
//                                    }
//                                    snippet = sourcelocation;
//
//                                    if (!object.isNull("name")) {
//                                        title = object.getString("name");
//                                    }
//
//                                    point = new LatLng(lat, lng);
//
//                                    drawMarker(point, i, title, snippet, userimage);
//                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
//
//                                }
//
//
//                            } else if (success == 2) {
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                            }
//
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.e("Error.Response", e.toString());
//
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (pDialog != null && pDialog.isShowing())
//                            pDialog.dismiss();
//
//                        // error
//                        Log.e("Error.Response", error.toString());
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("group_id", group_id);
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
//    private void drawMarker(LatLng point, int position, String title, String snippet, String userimage) {
//        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
//        ImageView image = (ImageView) view.findViewById(R.id.num_txt);
//
//
//        if (!TextUtils.isEmpty(userimage)) {
//            Picasso.with(getApplicationContext())
//                    .load(userimage)
//                    .placeholder(R.drawable.header_icon)
//                    .error(R.drawable.header_icon)
//                    .into(image);
//            try {
//                URL url = new URL(userimage);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                Bitmap bmImg;
//                conn.setDoInput(true);
//                conn.connect();
//
//                InputStream is = conn.getInputStream();
//                bmImg = BitmapFactory.decodeStream(is);
//
//                bmImg = Bitmap.createScaledBitmap(bmImg, 120, 140, false);
//                Bitmap imagenAndroid = BitmapFactory.decodeResource(getResources(), R.drawable.custom_markernew);
//                Bitmap mergedImages = createSingleImageFromMultipleImages(Bitmap.createScaledBitmap(imagenAndroid, 140, 170, false), toRoundBitmap(bmImg));
//                marker = map.addMarker(new MarkerOptions()
//                        .position(point)
//                        .snippet(snippet)
//                        .title(title)
//                        .icon(BitmapDescriptorFactory.fromBitmap(mergedImages)));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Picasso.with(getApplicationContext())
//                    .load(R.drawable.header_icon)
//                    .placeholder(R.drawable.header_icon)
//                    .error(R.drawable.header_icon)
//                    .into(image);
//            Bitmap bmImg = BitmapFactory.decodeResource(getResources(), R.drawable.header_icon);
//            bmImg = Bitmap.createScaledBitmap(bmImg, 120, 140, false);
//            Bitmap imagenAndroid = BitmapFactory.decodeResource(getResources(), R.drawable.custom_markernew);
//            Bitmap mergedImages = createSingleImageFromMultipleImages(Bitmap.createScaledBitmap(imagenAndroid, 140, 170, false), toRoundBitmap(bmImg));
//            marker = map.addMarker(new MarkerOptions()
//                    .position(point)
//                    .snippet(snippet)
//                    .title(title)
//                    .icon(BitmapDescriptorFactory.fromBitmap(mergedImages)));
//
//        }
//
//        marker.setTag(position);
//
//    }
//
//    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {
//        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
//        Canvas canvas = new Canvas(result);
//        canvas.drawBitmap(firstImage, 0f, 0f, null);
//        canvas.drawBitmap(secondImage, 10f, 5f, null);
//        return result;
//    }
//
//    public static Bitmap toRoundBitmap(Bitmap bitmap) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        float roundPx;
//        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
//        if (width <= height) {
//            roundPx = width / 2.0f;
//            top = 0;
//            bottom = width;
//            left = 0;
//            right = width;
//            height = width;
//            dst_left = 0;
//            dst_top = 0;
//            dst_right = width;
//            dst_bottom = width;
//        } else {
//            roundPx = height / 2.0f;
//            float clip = (width - height) / 2;
//            left = clip;
//            right = width - clip;
//            top = 0;
//            bottom = height;
//            width = height;
//            dst_left = 0;
//            dst_top = 0;
//            dst_right = height;
//            dst_bottom = height;
//        }
//
//        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect src = new Rect((int) left, (int) top, (int) right,
//                (int) bottom);
//        final Rect dst = new Rect((int) dst_left, (int) dst_top,
//                (int) dst_right, (int) dst_bottom);
//        final RectF rectF = new RectF(dst);
//
//        paint.setAntiAlias(true);
//
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, src, dst, paint);
//        return output;
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
//
//
//}