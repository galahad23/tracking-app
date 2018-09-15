//package com.example.android.vcare.pending;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.ProgressDialog;
//import android.app.TimePickerDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.android.vcare.AppController;
//import com.example.android.vcare.R;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PolylineOptions;
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
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//public class Edit_single_zone extends AppCompatActivity implements AdapterView.OnItemClickListener {
//
//
//    private static final String LOG_TAG = "ExampleApp";
//
//    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
//    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
//    private static final String OUT_JSON = "/json";
//
//    //------------ make your specific key ------------
//    /*private static final String API_KEY = "AIzaSyD6U4WJXqOHdJ2sGNbF5jzwRwuMqGqBZQo";*/
//
//    LatLng point;
//    ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();
//    Double deslat, deslang, lat, lang;
//
//    String data = "", list_id = "";
//    String strname = "", stremail = "", strmobile = "", strradious = "", strspeed = "", strtime = "", cuntery_code = "", device_id = "", parent_id = "", child_id = "";
//
//    ProgressDialog pDialog;
//    Toolbar toolbar;
//    Button update;
//    EditText ss_date, ee_date;
//    AutoCompleteTextView zonee;
//    Calendar myCalendar;
//    int hour, minute, sec;
//    int setDate = 0;
//    String startdate, enddate, zone, latitude, longitude;
//    String  str_date = "", selectTime = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_single_zone);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        final Intent intent = getIntent();
//        startdate = intent.getStringExtra("startdate");
//        enddate = intent.getStringExtra("enddate");
//        zone = intent.getStringExtra("zone");
//        latitude = intent.getStringExtra("latitude");
//
//        longitude = intent.getStringExtra("longitude");
//
//        if (latitude.equals("null") && longitude.equals("null")) {
//
//        } else {
//            deslat = Double.valueOf(latitude);
//            deslang = Double.valueOf(longitude);
//        }
//        parent_id = intent.getStringExtra("parentid");
//        child_id = intent.getStringExtra("child_id");
//        list_id = intent.getStringExtra("list_id");
//
//        update = (Button) findViewById(R.id.update);
//
//        ss_date = (EditText) findViewById(R.id.startdate);
//        ee_date = (EditText) findViewById(R.id.enddate);
//
//        zonee = (AutoCompleteTextView) findViewById(R.id.zone);
//
//        ss_date.setText(startdate);
//        ee_date.setText(enddate);
//        zonee.setText(zone);
//
//
//        ss_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
///*                SlideDateTimeListener listener = new SlideDateTimeListener() {
//
//                    @Override
//                    public void onDateTimeSet(Date date) {
//
//                        // Overriding onDateTimeCancel() is optional.
//                        Calendar c = Calendar.getInstance();
//
//                        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//                        String formattedDate = df.format(date);
//
//
//                        // Date currentdate = new Date();
//
//                        Calendar cal = Calendar.getInstance();
//                        cal.roll(Calendar.DATE, -1);
//
//                        if (date.before(cal.getTime())) {
//                            Toast.makeText(getApplicationContext(), "You cannot select previous date", Toast.LENGTH_SHORT).show();
//
//                        } else {
//
//                            ss_date.setText(formattedDate);
//                        }
//
//                    }
//
//                    @Override
//                    public void onDateTimeCancel() {
//
//                    }
//                };
//
//
//                new SlideDateTimePicker.Builder(Edit_single_zone.this.getSupportFragmentManager())
//                        .setListener(listener)
//                        .setInitialDate(new Date())
//                        .setIs24HourTime(true)
//                        .setIndicatorColor(getResources().getColor(R.color.colorPrimary))
//                        .setMinDate(new Date())
//                        .build()
//                        .show();*/
//                setDate = 1;
//                myCalendar = Calendar.getInstance();
//                DatePickerDialog datepicker = new DatePickerDialog(Edit_single_zone.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                datepicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                datepicker.show();
//            }
//        });
//
//        zonee.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//                    HideKeyboard(Edit_single_zone.this);
//
//                }
//
//                if (actionId == EditorInfo.IME_ACTION_NEXT) {
//
//                    HideKeyboard(Edit_single_zone.this);
//
//                }
//
//                return true;
//            }
//        });
//
//
//
//
//
//        ee_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setDate = 2;
///*                SlideDateTimeListener listener = new SlideDateTimeListener() {
//
//                    @Override
//                    public void onDateTimeSet(Date date) {
//
//                        // Overriding onDateTimeCancel() is optional.
//                        Calendar c = Calendar.getInstance();
//
//                        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//                        String formattedDate = df.format(date);
//
//                        Date currentdate = yesterday();
//
//                        if (date.before(currentdate)) {
//                            Toast.makeText(getApplicationContext(), "You cannot select previous date", Toast.LENGTH_SHORT).show();
//
//                        } else {
//
//                            ee_date.setText(formattedDate);
//                        }
//
//                    }
//
//                    @Override
//                    public void onDateTimeCancel() {
//
//                    }
//                };
//
//
//                new SlideDateTimePicker.Builder(Edit_single_zone.this.getSupportFragmentManager())
//                        .setListener(listener)
//                        .setInitialDate(new Date())
//                        .setIs24HourTime(true)
//                        .setIndicatorColor(getResources().getColor(R.color.colorPrimary))
//                        .setMinDate(new Date())
//                        .build()
//                        .show();*/
//                myCalendar = Calendar.getInstance();
//                DatePickerDialog datepicker = new DatePickerDialog(Edit_single_zone.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));
//                datepicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                datepicker.show();
//
//            }
//        });
//
//        zonee.setThreshold(2);
//        zonee.setAdapter(new GooglePlacesAutocompleteAdapter(Edit_single_zone.this, R.layout.search_listitem));
//        zonee.setOnItemClickListener(this);
//
//        ss_date.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                startdate = ss_date.getText().toString();
//            }
//        });
//
//        ee_date.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                startdate = ee_date.getText().toString();
//            }
//        });
//
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                startdate = ss_date.getText().toString().trim();
//                enddate = ee_date.getText().toString().trim();
//                zone = zonee.getText().toString().trim();
//
//                if (startdate.length() == 0) {
//                    ss_date.requestFocus();
//                    ss_date.setError("Please select start date");
//                } else if (enddate.length() == 0) {
//                    ee_date.requestFocus();
//                    ee_date.setError("Please select end date");
//                } else if (zone.length() == 0) {
//                    Toast.makeText(Edit_single_zone.this, "Please select zone", Toast.LENGTH_SHORT).show();
//                } else {
//                    Update_child_Zone_detail();
//                }
//
//            }
//        });
//
//    }
//    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear,
//                              int dayOfMonth) {
//            // TODO Auto-generated method stub
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH, monthOfYear);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            updateLabel();
//        }
//
//        private void updateLabel() {
//            String myFormat = "MM-dd-yyyy"; //In which you need put here
//            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//            str_date = sdf.format(myCalendar.getTime());
//            opentimediloag();
//        }
//
//        private void opentimediloag() {
//
//            Calendar mcurrentTime = Calendar.getInstance();
//            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//            minute = mcurrentTime.get(Calendar.MINUTE);
//            sec = mcurrentTime.get(Calendar.SECOND);
//            TimePickerDialog mTimePicker;
//
//            System.out.println("Current time => " + mcurrentTime.getTime());
//
//            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
//            final String current_date = df.format(mcurrentTime.getTime());
//            mTimePicker = new TimePickerDialog(Edit_single_zone.this, new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                    if (selectedHour <= hour && !DateFormat.is24HourFormat(Edit_single_zone.this) && selectedMinute <= minute && str_date.equals(current_date)) {
//                        selectTime = "";
//                        Toast.makeText(Edit_single_zone.this, "You cannot select previous date", Toast.LENGTH_SHORT).show();
//                    } else if (selectedMinute < 9) {
//                        selectTime = selectedHour + ":0" + selectedMinute + ":" + sec;
//                    } else if (sec < 9) {
//                        selectTime = selectedHour + ":" + selectedMinute + ":0" + sec;
//                    } else {
//                        selectTime = selectedHour + ":" + selectedMinute + ":" + sec;
//                        if (setDate == 1) {
//                            ss_date.setText(str_date + " " + selectTime);
//                        } else {
//                            ee_date.setText(str_date + " " + selectTime);
//                        }
//                    }
//
//                }
//            }, hour, minute, true);//Yes 24 hour time
//            mTimePicker.show();
//
//        }
//    };
//
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//        hideKeyboard(this);
//
//        String str = (String) adapterView.getItemAtPosition(position);
//        Log.e("second point", "point>>" + str);
//        String fareestimation_location = "";
//        fareestimation_location = str;
//
//        Geocoder coder = new Geocoder(this);
//        List<Address> addresses;
//        try {
//            addresses = coder.getFromLocationName(str, 10);
//            if (addresses.isEmpty()) {
//            } else {
//                coder = new Geocoder(this, Locale.getDefault());
//                Address location = addresses.get(0);
//
//                deslat = location.getLatitude();
//                deslang = location.getLongitude();
//
//
//                addresses = coder.getFromLocation(deslat, deslang, 1);
//                Address bestMatch = (addresses.isEmpty() ? null : addresses.get(0));
//
//                Log.e("deslat", "deslat>>" + deslat);
//                Log.e("deslang", "deslang>>" + deslang);
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//    public static void HideKeyboard(Activity activity) {
//        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
//        }
//    }
//
//    public  ArrayList<String> autocomplete(String input) {
//        ArrayList<String> resultList = null;
//
//        HttpURLConnection conn = null;
//        StringBuilder jsonResults = new StringBuilder();
//        try {
//            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
//            sb.append("?key=" +  getString(R.string.google_key));
//            //  sb.append("&components=country:gr");
//            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
//
//            URL url = new URL(sb.toString());
//
//            System.out.println("URL: " + url);
//            Log.e("output", "output>>>>" + url);
//            conn = (HttpURLConnection) url.openConnection();
//            InputStreamReader in = new InputStreamReader(conn.getInputStream());
//
//            // Load the results into a StringBuilder
//            int read;
//            char[] buff = new char[1024];
//            while ((read = in.read(buff)) != -1) {
//                jsonResults.append(buff, 0, read);
//            }
//        } catch (MalformedURLException e) {
//            Log.e(LOG_TAG, "Error processing Places API URL", e);
//            return resultList;
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error connecting to Places API", e);
//            return resultList;
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//
//        try {
//
//            // Create a JSON object hierarchy from the results
//            JSONObject jsonObj = new JSONObject(jsonResults.toString());
//            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
//
//            // Extract the Place descriptions from the results
//            resultList = new ArrayList<String>(predsJsonArray.length());
//            for (int i = 0; i < predsJsonArray.length(); i++) {
//                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
//                System.out.println("============================================================");
//                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
//            }
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, "Cannot process JSON results", e);
//        }
//
//        return resultList;
//
//    }
//
//    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
//        private ArrayList<String> resultList;
//
//        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
//            super(context, textViewResourceId);
//        }
//
//        @Override
//        public int getCount() {
//            return resultList.size();
//        }
//
//        @Override
//        public String getItem(int index) {
//            return resultList.get(index);
//        }
//
//        @Override
//        public Filter getFilter() {
//            Filter filter = new Filter() {
//                @Override
//                protected FilterResults performFiltering(CharSequence constraint) {
//                    FilterResults filterResults = new FilterResults();
//                    if (constraint != null) {
//                        // Retrieve the autocomplete results.
//                        resultList = autocomplete(constraint.toString());
//
//                        // Assign the data to the FilterResults
//                        filterResults.values = resultList;
//                        filterResults.count = resultList.size();
//                    }
//                    return filterResults;
//                }
//
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    if (results != null && results.count > 0) {
//                        notifyDataSetChanged();
//                    } else {
//                        notifyDataSetInvalidated();
//                    }
//                }
//            };
//            return filter;
//        }
//    }
//
//
//    private String getDirectionsUrl(LatLng origin, LatLng dest) {
//
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//        // Sensor enabled
//        String sensor = "sensor=false";
//        // distance in miles
//        String unitsystem = "units=imperial";
//
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + unitsystem;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//
//        return url;
//    }
//
//    /**
//     * A method to download json data from url
//     */
//    private String downloadUrl1(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
//
//            // Creating an http connection to communicate with url
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//            // Connecting to url
//            urlConnection.connect();
//
//            // Reading data from url
//            iStream = urlConnection.getInputStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//            StringBuffer sb = new StringBuffer();
//
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            data = sb.toString();
//
//            br.close();
//
//        } catch (Exception e) {
//            Log.d("Exception", e.toString());
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }
//
//    // Fetches data from url passed
//    private class DownloadTask extends AsyncTask<String, Void, String> {
//
//        // Downloading data in non-ui thread
//        @Override
//        protected String doInBackground(String... url) {
//
//            // For storing data from web service
//            String data = "";
//
//            try {
//                // Fetching the data from web service
//                data = downloadUrl1(url[0]);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//            return data;
//        }
//
//        // Executes in UI thread, after the execution of
//        // doInBackground()
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            ParserTask1 parserTask = new ParserTask1();
//
//            // Invokes the thread for parsing the JSON data
//            parserTask.execute(result);
//        }
//    }
//
//    /**
//     * A class to parse the Google Places in JSON format
//     */
//    private class ParserTask1 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//        // Parsing the data in non-ui thread
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//            JSONObject jObject;
//            List<List<HashMap<String, String>>> routes = null;
//
//            try {
//                jObject = new JSONObject(jsonData[0]);
//                DirectionsJSONParser parser = new DirectionsJSONParser();
//
//                // Starts parsing data
//                routes = parser.parse(jObject);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return routes;
//        }
//
//        // Executes in UI thread, after the parsing process
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//            ArrayList<LatLng> points = null;
//            PolylineOptions lineOptions = null;
//            MarkerOptions markerOptions = new MarkerOptions();
//            String fare_distance = "";
//            String fare_duration = "";
//
//            if (result.size() < 1) {
//                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Traversing through all the routes
//            for (int i = 0; i < result.size(); i++) {
//                points = new ArrayList<LatLng>();
//                lineOptions = new PolylineOptions();
//
//                // Fetching i-th route
//                List<HashMap<String, String>> path = result.get(i);
//
//                // Fetching all the points in i-th route
//                for (int j = 0; j < path.size(); j++) {
//                    HashMap<String, String> point = path.get(j);
//
//                    if (j == 0) {    // Get distance from the list
//                        fare_distance = (String) point.get("distance");
//                        fare_distance = fare_distance.replace(" mi", "");
//                        continue;
//                    } else if (j == 1) { // Get duration from the list
//                        fare_duration = (String) point.get("duration");
//                        fare_duration = fare_duration.replace(" mins", "");
//
//                        continue;
//                    }
//
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lng = Double.parseDouble(point.get("lng"));
//                    LatLng position = new LatLng(lat, lng);
//
//                    points.add(position);
//                }
//
//                // Adding all the points in the route to LineOptions
//                lineOptions.addAll(points);
//                lineOptions.width(5);
//                lineOptions.color(Color.GREEN);
//            }
//
//            Log.e("distance", "dis>>>" + fare_distance + " time " + fare_duration);
//        }
//    }
//
//
//    private void Update_child_Zone_detail() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Edit_single_zone.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Update_child_Zone_detail",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", "" + response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Edit_single_zone.this, msg, Toast.LENGTH_LONG).show();
//                                finish();
//
//                            } else if (success == 0) {
//
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Edit_single_zone.this, msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Edit_single_zone.this, msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 4) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Edit_single_zone.this, msg, Toast.LENGTH_LONG).show();
//                            }
//
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.e("Error.Response", "" + e.toString());
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pDialog.dismiss();
//                        // error
//                        Log.e("Error.Response", "" + error.toString());
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("start_date_time", startdate);
//                params.put("end_date_time", enddate);
//                params.put("latitude", String.valueOf(deslat));
//                params.put("longitude", String.valueOf(deslang));
//                params.put("address", zone);
//                params.put("device_latlong_id", list_id);
//
//
//                Log.e("Insertttt", "" + params.toString());
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
//
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
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
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
//            default:
//
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
//
//        startActivity(Intent.createChooser(share, "Share via"));
//    }
//
//    private Date yesterday() {
//        final Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//        return cal.getTime();
//    }
//}
//
