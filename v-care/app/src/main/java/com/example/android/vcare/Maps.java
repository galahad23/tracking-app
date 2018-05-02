package com.example.android.vcare;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.MyItem;
import com.example.android.vcare.model.User_Detail;
import com.example.android.vcare.model.User_function;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Maps extends Fragment implements OnMapReadyCallback {

    String parent_id;
    User_function user_function = new User_function();
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    Marker marker;
    MapView mapView;
    ProgressDialog pDialog;
    GPSTracker gps;
    LatLng point;
    ArrayList<LatLng> markerPoints;
    ArrayList<String> Title;
    int p = 2, j, arylenth;
    Double latitude = 0.00, longitude = 0.00;

    private GoogleMap map;

    public Maps() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        databaseHandler = new DatabaseHandler(getActivity());
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

        markerPoints = new ArrayList<LatLng>();
        Title = new ArrayList<String>();


        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.getUiSettings().setMapToolbarEnabled(false);
        Track_member_list_detail_Api();
    }


    private void Track_member_list_detail_Api() {

        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "childlistmap",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (pDialog != null && pDialog.isShowing())
                            pDialog.dismiss();

                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {
                                List<MyItem> items = new ArrayList<MyItem>();
                                JSONArray array = objJson.getJSONArray("child");
                                for (int i = 0; i < array.length(); i++) {
                                    String title = null;
                                    String snippet = null;
                                    JSONObject object = array.getJSONObject(i);
                                    double lat = object.optDouble("latitude", 0.00);
                                    double lng = object.optDouble("longitude", 0.00);
                                    String userimage = object.getString("profile_image");

                                    if (!object.isNull("name")) {
                                        title = object.getString("name");
                                    }
                                    if (!object.isNull("address")) {
                                        snippet = object.getString("address");
                                    }
                                    point = new LatLng(lat, lng);

                                    drawMarker(new LatLng(lat, lng), i, title, snippet, userimage);
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));

                                }

                            } else if (success == 2) {

                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
                        if (pDialog != null && pDialog.isShowing())
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
        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    private void drawMarker(LatLng point, int position, String title, String snippet, String userimage) {
        View view = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        ImageView image = (ImageView) view.findViewById(R.id.num_txt);


        if (!TextUtils.isEmpty(userimage)) {
            Picasso.with(getApplicationContext())
                    .load(userimage)
                    .placeholder(R.drawable.header_icon)
                    .error(R.drawable.header_icon)
                    .into(image);
            try {
                URL url = new URL(userimage);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                Bitmap bmImg;
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);

                bmImg = Bitmap.createScaledBitmap(bmImg, 120, 140, false);
                Bitmap imagenAndroid = BitmapFactory.decodeResource(getResources(), R.drawable.custom_markernew);
                Bitmap mergedImages = createSingleImageFromMultipleImages(Bitmap.createScaledBitmap(imagenAndroid,140, 170,false), toRoundBitmap(bmImg));
                marker = map.addMarker(new MarkerOptions()
                        .position(point)
                        .snippet(snippet)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(mergedImages)));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Picasso.with(getApplicationContext())
                    .load(R.drawable.header_icon)
                    .placeholder(R.drawable.header_icon)
                    .error(R.drawable.header_icon)
                    .into(image);
            Bitmap bmImg = BitmapFactory.decodeResource(getResources(), R.drawable.header_icon);
            bmImg = Bitmap.createScaledBitmap(bmImg, 120, 140, false);
            Bitmap imagenAndroid = BitmapFactory.decodeResource(getResources(), R.drawable.custom_markernew);
            Bitmap mergedImages = createSingleImageFromMultipleImages(Bitmap.createScaledBitmap(imagenAndroid,140, 170,false), toRoundBitmap(bmImg));
            marker = map.addMarker(new MarkerOptions()
                    .position(point)
                    .snippet(snippet)
                    .title(title)
                    .icon(BitmapDescriptorFactory.fromBitmap(mergedImages)));

        }

        marker.setTag(position);

    }

    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {
        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 10f, 5f, null);
        return result;
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2.0f;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2.0f;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


}
