package com.example.android.vcare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.User_Detail;
import com.example.android.vcare.model.User_function;
import com.example.android.vcare.phonefield.PhoneField;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Add_device extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    /*private static final String API_KEY = "AIzaSyD6U4WJXqOHdJ2sGNbF5jzwRwuMqGqBZQo";*/

    ProgressDialog pDialog;
    User_function user_function = new User_function();
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    public static final int REQUEST_CODE = 102;
    Toolbar toolbar;
    EditText name, email, phone, speed, radious;
    Spinner time;
    CustomPhoneInputLayout customphone;
    int cunterycode;
    String cuntery_code = "", cuntery_name = "";
    //MultiAutoCompleteTextView zone;
    Button update;
    private TextInputLayout inputLayoutname, inputLayoutemail, inputLayoutmobile, inputLayoutspeed,
            inputLayoutzone, inputLayoutradious;
    ImageButton imagebutton_speed, imagebutton_zone, imagebutton_minuts, imagebutton_radius;
    String strname, stremail, strmobile, strzone, strspeed, strtime, strradious, parent_id, device_id = "";


    LatLng point;
    ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();
    ArrayList<Double> array_lat = new ArrayList<Double>();
    ArrayList<Double> array_long = new ArrayList<Double>();
    ArrayList<Double> latlong = new ArrayList<Double>();
    String data = "";


    com.libaml.android.view.chip.ChipLayout chip;
    ArrayAdapter<String> multi_slect_adapter;
    ArrayList<String> multi_zone = new ArrayList<String>();
    ArrayList<Integer> zone_id = new ArrayList<Integer>();
    ArrayList<Integer> zn_id = new ArrayList<Integer>();
    Double deslat, deslang, lat, lang;

    String nameIntent, emailIntent, country_name, phone_number;
    String[] time_list = {"15 min", "30 min", "45 min"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        nameIntent = intent.getStringExtra("name");
        emailIntent = intent.getStringExtra("email");
        country_name = intent.getStringExtra("country_name");
        phone_number = intent.getStringExtra("phone_number");

        customphone = (CustomPhoneInputLayout) findViewById(R.id.custom_phone);
        customphone.setDefaultCountry(country_name);
        PhoneField.mSpinner.setEnabled(false);


        inputLayoutname = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutemail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutmobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inputLayoutspeed = (TextInputLayout) findViewById(R.id.input_layout_speed);
        // inputLayouttime = (TextInputLayout)findViewById(R.id.input_layout_time);
        inputLayoutradious = (TextInputLayout) findViewById(R.id.input_layout_radious);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.mobile);
        speed = (EditText) findViewById(R.id.speed);
        radious = (EditText) findViewById(R.id.radious);
        time = (Spinner) findViewById(R.id.time);
        update = (Button) findViewById(R.id.update);

        name.setText(nameIntent);
        email.setText(emailIntent);
        phone.setText(phone_number);

        imagebutton_speed = (ImageButton) findViewById(R.id.imageButton_speed);
        imagebutton_zone = (ImageButton) findViewById(R.id.imageButton_zone);
        imagebutton_minuts = (ImageButton) findViewById(R.id.imageButton_minuts);
        imagebutton_radius = (ImageButton) findViewById(R.id.imageButton_radius);

        imagebutton_speed.setOnClickListener(this);
        imagebutton_zone.setOnClickListener(this);
        imagebutton_minuts.setOnClickListener(this);
        imagebutton_radius.setOnClickListener(this);

        name.addTextChangedListener(new MyTextWatcher(name));
        email.addTextChangedListener(new MyTextWatcher(email));
        phone.addTextChangedListener(new MyTextWatcher(phone));
        speed.addTextChangedListener(new MyTextWatcher(speed));
        // time.addTextChangedListener(new MyTextWatcher(time));
        radious.addTextChangedListener(new MyTextWatcher(radious));


        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, time_list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        time.setAdapter(aa);


        databaseHandler = new DatabaseHandler(Add_device.this);
        feeditem = new ArrayList<User_Detail>();
        Cursor cursor = databaseHandler.get_rider_detail();
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                User_Detail detail = new User_Detail();
                detail.setId(cursor.getString(5));
                detail.setAddress(cursor.getString(6));

                feeditem.add(detail);
                cursor.moveToNext();
            }
            cursor.close();
        }

        for (User_Detail userDetail : feeditem) {
            parent_id = userDetail.getId();

        }

        // Check Marshmellow Permission on Real time
        AndroidPermissions.check(this).permissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE)
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


                        ActivityCompat.requestPermissions(Add_device.this
                                , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE}
                                , REQUEST_CODE);
                    }
                })
                .check();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                device_id = tm.getDeviceId();
                Log.e("device_id", "" + device_id);

                Submit_detail();

            }
        });

        chip = (com.libaml.android.view.chip.ChipLayout) findViewById(R.id.zone);
        chip.clearFocus();

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.e("---------------", chip.getText().toString());
            }
        });
        chip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Log.e("---------------", "" + i);
            }
        });
        chip.addLayoutTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Log.d("---------------",editable.toString());
            }
        });
        chip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
              /*  InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(chip, InputMethodManager.SHOW_IMPLICIT);*/
                //  Log.e("----", String.valueOf(b));
            }
        });
        chip.setOnChipItemChangeListener(new com.libaml.android.view.chip.ChipLayout.ChipItemChangeListener() {
            @Override
            public void onChipAdded(int pos, String txt) {
                //  Log.e(txt, String.valueOf(pos));
              /*  multi_slect_adapter.remove(txt);
                chip.setAdapter(multi_slect_adapter);

                int index = multi_zone.indexOf(txt);


                int id = zone_id.get(index);

                zn_id.add(id);
*/
            }

            @Override
            public void onChipRemoved(int pos, String txt) {
                //  Log.d(txt, String.valueOf(pos));
              /*  multi_slect_adapter.add(txt);
                chip.setAdapter(multi_slect_adapter);
                dr_id.remove(pos);*/
            }
        });

        chip.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.search_listitem));
        chip.setOnItemClickListener(this);


      /*  zone.setThreshold(2);
        zone.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.search_listitem));
        zone.setOnItemClickListener(this);*/

    }

    private void Submit_detail() {
        if (!validatename()) {
            return;
        }

        if (!validateemail()) {
            return;
        }

        if (!validatmobile()) {
            return;
        }


        if (!validatspeed()) {
            return;
        }

       /* if (!validatezone()) {
            return;
        }*/
        if (!validatetime()) {
            return;
        }
        if (!validateradious()) {
            return;
        } else {


            Intent intent = new Intent(getApplicationContext(), Multiple_Zone.class);
            intent.putExtra("name", strname);
            intent.putExtra("email", stremail);
            intent.putExtra("mobile", strmobile);
            intent.putExtra("radius", strradious);
            intent.putExtra("speed", strspeed);
            intent.putExtra("time", strtime);
            intent.putExtra("countrycode", cuntery_code);
            intent.putExtra("cuntery_name", cuntery_name);
            intent.putExtra("deviceid", device_id);
            intent.putExtra("parentid", parent_id);
            startActivity(intent);


        }
    }


    private boolean validatename() {
        strname = name.getText().toString().trim();
        if (strname.length() == 0) {
            inputLayoutname.setError("Please enter your full name");
            requestFocus(name);
            return false;
        } else {
            inputLayoutname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateemail() {
        stremail = email.getText().toString().trim();

        if (stremail.isEmpty() || !isValidEmail(stremail)) {
            inputLayoutemail.setError("Enter valid email address");
            requestFocus(email);
            return false;
        } else {
            inputLayoutemail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatmobile() {
        strmobile = phone.getText().toString().trim();
        cunterycode = PhoneField.iso_code;
        cuntery_code = "+" + cunterycode;
        cuntery_name = PhoneField.code;
        if (strmobile.length() < 6 || strmobile.length() > 13) {
            inputLayoutmobile.setError("Please enter your mobile number without country code");
            requestFocus(phone);
            return false;
        } else {
            inputLayoutmobile.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatspeed() {
        strspeed = speed.getText().toString().trim();
        if (strspeed.length() == 0) {
            inputLayoutspeed.setError("Please enter max. speed");
            requestFocus(speed);
            return false;
        } else if (Integer.parseInt(strspeed) > 300) {
            Toast.makeText(this, "Speed limit can't be more than 300km/hr", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            inputLayoutspeed.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatezone() {
        data = "";
        //List<Integer> list = doctor_list.getSelectedIndices();

        int size = array_lat.size();

        if (size == 0) {

            Toast.makeText(getApplicationContext(), "Please Enter Track zone", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            for (int i = 0; i < size; i++) {

                Double id = array_lat.get(i);

                data += id + ",";
            }
            data = data.substring(0, data.length() - 1);

            Log.e("doctor_id", "doctor_id>> " + data);
            // inputLayouttime.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatetime() {
        strtime = time.getSelectedItem().toString().trim();
        StringTokenizer tokenizer = new StringTokenizer(strtime, " ");
        strtime = tokenizer.nextToken();
       /* if (strtime.length()==0) {
            inputLayouttime.setError("Please enter time of Trace device in minutes");
            requestFocus(time);
            return false;
        } else {
            inputLayouttime.setErrorEnabled(false);
        }
*/
        return true;
    }

    private boolean validateradious() {
        strradious = radious.getText().toString().trim();
        if (strradious.length() == 0) {
            inputLayoutradious.setError("Please enter radius");
            requestFocus(radious);
            return false;
        } else {
            inputLayoutradious.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            Add_device.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        AndroidPermissions.result(Add_device.this)
                .addPermissions(REQUEST_CODE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE)
                .putActions(REQUEST_CODE, new com.nobrain.android.permissions.Result.Action0() {
                    @Override
                    public void call() {
                        //Device id
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        device_id = tm.getDeviceId();
                        Log.e("device_id", "" + device_id);
                    }
                }, new com.nobrain.android.permissions.Result.Action1() {
                    @Override
                    public void call(String[] hasPermissions, String[] noPermissions) {
                    }
                })
                .result(requestCode, permissions, grantResults);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        hideKeyboard(this);

        String str = (String) adapterView.getItemAtPosition(position);
        Log.e("second point", "point>>" + str);
        String fareestimation_location = "";
        fareestimation_location = str;

        Geocoder coder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = coder.getFromLocationName(str, 10);
            if (addresses == null) {
            }
            coder = new Geocoder(this, Locale.getDefault());
            Address location = addresses.get(0);
            deslat = location.getLatitude();
            deslang = location.getLongitude();

            array_lat.clear();
            array_long.clear();

            array_lat.add(deslat);
            array_long.add(deslang);
            Log.e("array_lat", "array_lat++++" + array_lat);
            Log.e("array_long", "array_long++++" + array_long);

            addresses = coder.getFromLocation(deslat, deslang, 1);
            Address bestMatch = (addresses.isEmpty() ? null : addresses.get(0));

            if (addresses.size() > 0) {

            }

            String postalCode = addresses.get(0).getPostalCode();
            String locality = addresses.get(0).getLocality();
            Log.e("postalCode", "postalCode>>>" + bestMatch);


        } catch (IOException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < array_lat.size(); i++) {
            lat = array_lat.get(i);
            lang = array_long.get(i);

            point = new LatLng(lat, lang);
            markerPoints.add(point);


        }


        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(point);

           /*
             * For the start location, the color of marker is GREEN and
             * for the end location, the color of marker is RED.
             *//**//**/


        // Add new marker to the Google Map Android API V2
        // map.addMarker(options);

        if (markerPoints.size() >= 2) {
            LatLng origin = markerPoints.get(0);
            LatLng dest = markerPoints.get(1);
            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

        }
    }


    public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + getString(R.string.google_key));
            //  sb.append("&components=country:gr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            Log.e("output", "output>>>>" + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    @Override
    public void onClick(View v) {
        if (v == imagebutton_speed) {
            speed_dialouge();
        }

        if (v == imagebutton_zone) {
            zone_dialouge();
        }

        if (v == imagebutton_minuts) {
            minuts_dialouge();
        }

        if (v == imagebutton_radius) {
            radius_dialouge();
        }
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        // distance in miles
        String unitsystem = "units=imperial";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + unitsystem;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl1(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl1(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask1 parserTask = new ParserTask1();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask1 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String fare_distance = "";
            String fare_duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        fare_distance = (String) point.get("distance");
                        fare_distance = fare_distance.replace(" mi", "");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        fare_duration = (String) point.get("duration");
                        fare_duration = fare_duration.replace(" mins", "");

                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.GREEN);
            }

            //  String   second_location = zone.getText().toString();


            //tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
            // Toast.makeText(Confirmation.this, ""+"Distance:"+fare_distance + ", Duration:"+fare_duration, Toast.LENGTH_SHORT).show();
            // Drawing polyline in the Google Map for the i-th route
            // map.addPolyline(lineOptions);
            Log.e("distance", "dis>>>" + fare_distance + " time " + fare_duration);
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

                case R.id.name:
                    validatename();
                    break;

                case R.id.email:
                    validateemail();
                    break;
                case R.id.mobile:
                    validatmobile();
                    break;
                case R.id.speed:
                    validatspeed();
                    break;
                case R.id.zone:
                    validatezone();
                    break;
                case R.id.time:
                    validatetime();
                    break;
                case R.id.radious:
                    validateradious();
                    break;
            }

        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.share_app:
                shareTextUrl();
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }

    }

    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "I would like to invite you to download this V-Care Apps, a traceability system and risk management for your love one.  It also provides a closer communication link among your friends, family in the case of emergency. Download : https://www.dropbox.com/current_speed/fdgt6btgjp95ktt/V-Care%20demo%20apps.apk?dl=0");

        startActivity(Intent.createChooser(share, "Share via"));
    }


    // Alert dialouge
    private void speed_dialouge() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This is about speed Enter speed of your child");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

            }
        });
        alertDialogBuilder.setTitle("ABOUT SPEED");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    // Alert dialouge
    private void zone_dialouge() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This is about zone Enter Multiple zone of your child");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

            }
        });
        alertDialogBuilder.setTitle("ABOUT ZONE");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    // Alert dialouge
    private void minuts_dialouge() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This is about device response in minuts Enter minuts of device time intervel of your child");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

            }
        });
        alertDialogBuilder.setTitle("ABOUT DEVICE RESPONSE");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    // Alert dialouge
    private void radius_dialouge() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This is about Radius Enter radius in meter of your child");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

            }
        });
        alertDialogBuilder.setTitle("ABOUT RADIUS");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
