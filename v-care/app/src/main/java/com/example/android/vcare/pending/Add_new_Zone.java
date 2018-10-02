//package com.example.android.vcare.pending;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.app.ProgressDialog;
//import android.app.TimePickerDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Color;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.ImageButton;
//import android.widget.ListView;
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
//import com.example.android.vcare.model.DatabaseHandler;
//import com.example.android.vcare.model.UserData;
//import com.example.android.vcare.model.User_Detail;
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
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//public class Add_new_Zone extends AppCompatActivity {
//    ProgressDialog pDialog;
//    Button submit, addmore;
//    Toolbar toolbar;
//    private List<User_Detail> feeditem = new ArrayList<>();
//    private Multiple_zone_adapter listadapter;
//    private List<UserData> feeditem1;
//    private ListView listView;
//    DatabaseHandler databaseHandler;
//    String start_date = "", end_date = "", zone = "", s_datetime = "", e_datetime = "", zones = "", lattt = "", langgg = "";
//    String strname = "", stremail = "", strmobile = "", strradious = "", strspeed = "", strtime = "", cuntery_code = "", device_id = "",
//            parent_id = "", child_id = "",
//            check_address = "";
//    int i = 1;
//    String item_id = "";
//    Multiple_zone_adapter.ViewHolder holder;
//    Calendar myCalendar;
//    int hour, minute, sec;
//    int setDate = 0;
//    private static final String LOG_TAG = "ExampleApp";
//    String  str_date = "", selectTime = "";
//    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
//    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
//    private static final String OUT_JSON = "/json";
//
//    //------------ make your specific key ------------
//    /*private static final String API_KEY = "AIzaSyD6U4WJXqOHdJ2sGNbF5jzwRwuMqGqBZQo";*/
//
//    LatLng point;
//    ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();
//    ArrayList<Double> array_lat = new ArrayList<Double>();
//    ArrayList<Double> array_long = new ArrayList<Double>();
//    ArrayList<Double> latlong = new ArrayList<Double>();
//    String data = "";
//    Double deslat, deslang, lat, lang;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_new__zone);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        databaseHandler = new DatabaseHandler(this);
//
//        addmore = (Button) findViewById(R.id.add_more);
//        submit = (Button) findViewById(R.id.submit);
//
//
//        listView = (ListView) findViewById(R.id.listview);
//        feeditem = new ArrayList<User_Detail>();
//        listadapter = new Multiple_zone_adapter(Add_new_Zone.this, feeditem);
//        listView.setAdapter(listadapter);
//
//        Intent intent = getIntent();
//        Constant.parent_child_id = intent.getStringExtra("parent_child_id");
//
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (s_datetime.isEmpty()) {
//                    Toast.makeText(Add_new_Zone.this, "Please Enter Zone details", Toast.LENGTH_SHORT).show();
//                } else if (check_address.isEmpty()) {
//                    Toast.makeText(Add_new_Zone.this, "Please choose zone in drop down", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    feeditem1 = new ArrayList<UserData>();
//                    Cursor cursor = databaseHandler.get_zone_detail();
//                    if (cursor != null) {
//                        cursor.moveToFirst();
//                        for (int i = 0; i < cursor.getCount(); i++) {
//                            UserData detail = new UserData();
//                            // detail.setId(cursor.getString(0));
//                            detail.setName(cursor.getString(1));
//                            detail.setEmail(cursor.getString(2));
//                            detail.setMessage(cursor.getString(3));
//                            detail.setMobile(cursor.getString(4));
//                            detail.setIMEI(cursor.getString(5));
//                            feeditem1.add(detail);
//
//
//                            cursor.moveToNext();
//                        }
//                        cursor.close();
//
//
//                        for (UserData userDetail : feeditem1) {
//                            start_date += userDetail.getName() + ",";
//                            end_date += userDetail.getEmail() + ",";
//                            zone += userDetail.getMessage() + "^";
//                            lattt += userDetail.getMobile() + ",";
//                            langgg += userDetail.getIMEI() + ",";
//
//
//                        }
//
//                        start_date = start_date.substring(0, start_date.length() - 1);
//                        end_date = end_date.substring(0, end_date.length() - 1);
//                        zone = zone.substring(0, zone.length() - 1);
//                        lattt = lattt.substring(0, lattt.length() - 1);
//                        langgg = langgg.substring(0, langgg.length() - 1);
//
//
//                        Add_child_new_zone_api();
//
//                        Log.e("start_date", "start_date>>" + start_date);
//                        Log.e("end_date", "end_date>>" + end_date);
//                        Log.e("zone", "zone>>" + zone);
//                        Log.e("lattt", "lattt>>" + lattt);
//                        Log.e("langgg", "langgg>>" + langgg);
//
//
//                    }
//                }
//            }
//        });
//
//        addmore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (s_datetime.isEmpty()) {
//                    Toast.makeText(Add_new_Zone.this, "Please enter start date", Toast.LENGTH_SHORT).show();
//                } else if (e_datetime.isEmpty()) {
//                    Toast.makeText(Add_new_Zone.this, "Please enter end date", Toast.LENGTH_SHORT).show();
//                } else if (zones.isEmpty()) {
//                    Toast.makeText(Add_new_Zone.this, "Please enter zone", Toast.LENGTH_SHORT).show();
//                } else if (check_address.isEmpty()) {
//                    Toast.makeText(Add_new_Zone.this, "Please choose zone in drop down", Toast.LENGTH_SHORT).show();
//                } else {
//                    check_address = "";
//
//                    i++;
//                    feeditem.clear();
//                    databaseHandler.add_zone(String.valueOf(i), "", "", "", "", "", "", "");
//
//                    Cursor cursor = databaseHandler.get_zone_detail();
//                    if (cursor != null) {
//                        cursor.moveToFirst();
//                        for (int i = 0; i < cursor.getCount(); i++) {
//                            User_Detail detail = new User_Detail();
//                            detail.setId(cursor.getString(0));
//                            detail.setName(cursor.getString(1));
//                            detail.setEmail(cursor.getString(2));
//                            detail.setImage(cursor.getString(3));
//                            feeditem.add(detail);
//
//                            cursor.moveToNext();
//                        }
//                        cursor.close();
//                        listadapter.notifyDataSetChanged();
//                    }
//                }
//
//            }
//
//        });
//
//        Set_first_item();
//    }
//
//    private void Set_first_item() {
//        databaseHandler.resetTable_zone();
//        databaseHandler.add_zone(String.valueOf(i), "", "", "", "", "", "", "");
//
//        Cursor cursor = databaseHandler.get_zone_detail();
//        if (cursor != null) {
//            cursor.moveToFirst();
//            for (int i = 0; i < cursor.getCount(); i++) {
//                User_Detail detail = new User_Detail();
//                detail.setId(cursor.getString(0));
//                detail.setName(cursor.getString(1));
//                detail.setEmail(cursor.getString(2));
//                detail.setImage(cursor.getString(3));
//
//                feeditem.add(detail);
//
//                cursor.moveToNext();
//            }
//            cursor.close();
//            listadapter.notifyDataSetChanged();
//        }
//
//
//    }
//
//
//    public class Multiple_zone_adapter extends BaseAdapter implements AdapterView.OnItemClickListener {
//
//        private Activity activity;
//        private LayoutInflater inflater;
//        public List<User_Detail> galleryItems;
//
//
//        public Multiple_zone_adapter(Activity activity, List<User_Detail> galleryItems) {
//            this.activity = activity;
//            this.galleryItems = galleryItems;
//
//            // databaseHandler=new DatabaseHandler(activity);
//
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return galleryItems.size();
//        }
//
//        @Override
//        public Object getItem(int location) {
//            // TODO Auto-generated method stub
//            return galleryItems.get(location);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return galleryItems.indexOf(getItem(position));
//        }
//
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//            // hideKeyboard(this);
//
//            String str = (String) adapterView.getItemAtPosition(position);
//            Log.e("second point", "point>>" + str);
//            String fareestimation_location = "";
//            fareestimation_location = str;
//
//            Geocoder coder = new Geocoder(Add_new_Zone.this);
//            List<Address> addresses;
//            try {
//                addresses = coder.getFromLocationName(str, 10);
//                if (addresses.isEmpty()) {
//                } else {
//
//                    Address location = addresses.get(0);
//
//                    deslat = location.getLatitude();
//                    deslang = location.getLongitude();
//
//                    array_lat.clear();
//                    array_long.clear();
//
//                    array_lat.add(deslat);
//                    array_long.add(deslang);
//
//                    check_address = str;
//
//                    if (databaseHandler.CheckIsDataAlreadyInDBorNot_Claim(item_id)) {
//                        Log.e("item_id", "item_id" + item_id);
//                        databaseHandler.update_Claim_List(item_id, s_datetime, e_datetime, zones, String.valueOf(deslat), String.valueOf(deslang), "", "");
//                    } else {
//                        Log.e("else_item_id", "else_item_id" + item_id);
//                        databaseHandler.add_zone(item_id, s_datetime, e_datetime, zones, String.valueOf(deslat), String.valueOf(deslang), "", "");
//                    }
//
//                    Log.e("array_lat", "array_lat++++" + array_lat);
//                    Log.e("array_long", "array_long++++" + array_long);
//
//                    addresses = coder.getFromLocation(deslat, deslang, 1);
//                    Address bestMatch = (addresses.isEmpty() ? null : addresses.get(0));
//
//                    if (addresses.size() > 0) {
//
//                    }
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            // Add new marker to the Google Map Android API V2
//            // map.addMarker(options);
//
//
//        }
//
//        public ArrayList<String> autocomplete(String input) {
//            ArrayList<String> resultList = null;
//
//            HttpURLConnection conn = null;
//            StringBuilder jsonResults = new StringBuilder();
//            try {
//                StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
//                sb.append("?key=" + getString(R.string.google_key));
//                //  sb.append("&components=country:gr");
//                sb.append("&input=" + URLEncoder.encode(input, "utf8"));
//
//                URL url = new URL(sb.toString());
//
//                System.out.println("URL: " + url);
//                Log.e("output", "output>>>>" + url);
//                conn = (HttpURLConnection) url.openConnection();
//                InputStreamReader in = new InputStreamReader(conn.getInputStream());
//
//                // Load the results into a StringBuilder
//                int read;
//                char[] buff = new char[1024];
//                while ((read = in.read(buff)) != -1) {
//                    jsonResults.append(buff, 0, read);
//                }
//            } catch (MalformedURLException e) {
//                Log.e(LOG_TAG, "Error processing Places API URL", e);
//                return resultList;
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error connecting to Places API", e);
//                return resultList;
//            } finally {
//                if (conn != null) {
//                    conn.disconnect();
//                }
//            }
//
//            try {
//
//                // Create a JSON object hierarchy from the results
//                JSONObject jsonObj = new JSONObject(jsonResults.toString());
//                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
//
//                // Extract the Place descriptions from the results
//                resultList = new ArrayList<String>(predsJsonArray.length());
//                for (int i = 0; i < predsJsonArray.length(); i++) {
//                    System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
//                    System.out.println("============================================================");
//                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
//                }
//            } catch (JSONException e) {
//                Log.e(LOG_TAG, "Cannot process JSON results", e);
//            }
//
//            return resultList;
//        }
//
//        class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
//            private ArrayList<String> resultList;
//
//            public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
//                super(context, textViewResourceId);
//            }
//
//            @Override
//            public int getCount() {
//                return resultList.size();
//            }
//
//            @Override
//            public String getItem(int index) {
//                return resultList.get(index);
//            }
//
//            @Override
//            public Filter getFilter() {
//                Filter filter = new Filter() {
//                    @Override
//                    protected FilterResults performFiltering(CharSequence constraint) {
//                        FilterResults filterResults = new FilterResults();
//                        if (constraint != null) {
//                            // Retrieve the autocomplete results.
//                            resultList = autocomplete(constraint.toString());
//
//                            // Assign the data to the FilterResults
//                            filterResults.values = resultList;
//                            filterResults.count = resultList.size();
//                        }
//                        return filterResults;
//                    }
//
//                    @Override
//                    protected void publishResults(CharSequence constraint, FilterResults results) {
//                        if (results != null && results.count > 0) {
//                            notifyDataSetChanged();
//                        } else {
//                            notifyDataSetInvalidated();
//                        }
//                    }
//                };
//                return filter;
//            }
//        }
//
//
//        private String getDirectionsUrl(LatLng origin, LatLng dest) {
//
//            // Origin of route
//            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//            // Destination of route
//            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//            // Sensor enabled
//            String sensor = "sensor=false";
//            // distance in miles
//            String unitsystem = "units=imperial";
//
//            // Building the parameters to the web service
//            String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + unitsystem;
//
//            // Output format
//            String output = "json";
//
//            // Building the url to the web service
//            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//
//            return url;
//        }
//
//        /**
//         * A method to download json data from url
//         */
//        private String downloadUrl1(String strUrl) throws IOException {
//            String data = "";
//            InputStream iStream = null;
//            HttpURLConnection urlConnection = null;
//            try {
//                URL url = new URL(strUrl);
//
//                // Creating an http connection to communicate with url
//                urlConnection = (HttpURLConnection) url.openConnection();
//
//                // Connecting to url
//                urlConnection.connect();
//
//                // Reading data from url
//                iStream = urlConnection.getInputStream();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//                StringBuffer sb = new StringBuffer();
//
//                String line = "";
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                data = sb.toString();
//
//                br.close();
//
//            } catch (Exception e) {
//                Log.d("Exception", e.toString());
//            } finally {
//                iStream.close();
//                urlConnection.disconnect();
//            }
//            return data;
//        }
//
//        // Fetches data from url passed
//        private class DownloadTask extends AsyncTask<String, Void, String> {
//
//            // Downloading data in non-ui thread
//            @Override
//            protected String doInBackground(String... url) {
//
//                // For storing data from web service
//                String data = "";
//
//                try {
//                    // Fetching the data from web service
//                    data = downloadUrl1(url[0]);
//                } catch (Exception e) {
//                    Log.d("Background Task", e.toString());
//                }
//                return data;
//            }
//
//            // Executes in UI thread, after the execution of
//            // doInBackground()
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//                Multiple_zone_adapter.ParserTask1 parserTask = new Multiple_zone_adapter.ParserTask1();
//
//                // Invokes the thread for parsing the JSON data
//                parserTask.execute(result);
//            }
//        }
//
//        /**
//         * A class to parse the Google Places in JSON format
//         */
//        private class ParserTask1 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//            // Parsing the data in non-ui thread
//            @Override
//            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//                JSONObject jObject;
//                List<List<HashMap<String, String>>> routes = null;
//
//                try {
//                    jObject = new JSONObject(jsonData[0]);
//                    DirectionsJSONParser parser = new DirectionsJSONParser();
//
//                    // Starts parsing data
//                    routes = parser.parse(jObject);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return routes;
//            }
//
//            // Executes in UI thread, after the parsing process
//            @Override
//            protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//                ArrayList<LatLng> points = null;
//                PolylineOptions lineOptions = null;
//                MarkerOptions markerOptions = new MarkerOptions();
//                String fare_distance = "";
//                String fare_duration = "";
//
//                if (result.size() < 1) {
//                    Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Traversing through all the routes
//                for (int i = 0; i < result.size(); i++) {
//                    points = new ArrayList<LatLng>();
//                    lineOptions = new PolylineOptions();
//
//                    // Fetching i-th route
//                    List<HashMap<String, String>> path = result.get(i);
//
//                    // Fetching all the points in i-th route
//                    for (int j = 0; j < path.size(); j++) {
//                        HashMap<String, String> point = path.get(j);
//
//                        if (j == 0) {    // Get distance from the list
//                            fare_distance = (String) point.get("distance");
//                            fare_distance = fare_distance.replace(" mi", "");
//                            continue;
//                        } else if (j == 1) { // Get duration from the list
//                            fare_duration = (String) point.get("duration");
//                            fare_duration = fare_duration.replace(" mins", "");
//
//                            continue;
//                        }
//
//                        double lat = Double.parseDouble(point.get("lat"));
//                        double lng = Double.parseDouble(point.get("lng"));
//                        LatLng position = new LatLng(lat, lng);
//
//                        points.add(position);
//                    }
//
//                    // Adding all the points in the route to LineOptions
//                    lineOptions.addAll(points);
//                    lineOptions.width(5);
//                    lineOptions.color(Color.GREEN);
//                }
//
//            }
//        }
//
//
//        class ViewHolder {
//            TextView indexing;
//            EditText start, end;
//            AutoCompleteTextView zonee;
//            ImageButton delete;
//        }
//
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//             holder = new Multiple_zone_adapter.ViewHolder();
//
//            inflater = (LayoutInflater) activity
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//
//            convertView = inflater.inflate(R.layout.multiple_zone_adapter, null);
//
//            holder.indexing = (TextView) convertView.findViewById(R.id.indexing);
//            holder.start = (EditText) convertView.findViewById(R.id.startdate);
//            holder.end = (EditText) convertView.findViewById(R.id.enddate);
//            holder.zonee = (AutoCompleteTextView) convertView.findViewById(R.id.zone);
//            holder.ic_cross = (ImageButton) convertView.findViewById(R.id.ic_cross);
//
//
//            final User_Detail item = galleryItems.get(position);
//
//
//            holder.indexing.setText(item.getId());
//            holder.start.setText(item.getName());
//            holder.end.setText(item.getEmail());
//            holder.zonee.setText(item.getImage());
//
//            Calendar c = Calendar.getInstance();
//
//            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//            String formattedDate = df.format(c.getTime());
//
//            if (item.getName().isEmpty()) {
//                holder.start.setText(formattedDate);
//            }
//
//
//            holder.start.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                    s_datetime = holder.start.getText().toString();
//                    e_datetime = holder.end.getText().toString();
//                    zones = holder.zonee.getText().toString();
//
//                    if (databaseHandler.CheckIsDataAlreadyInDBorNot_Claim(item.getId())) {
//                        databaseHandler.update_Claim_List(item.getId(), s_datetime, e_datetime, zones, "", "", "", "");
//                    } else {
//                        databaseHandler.add_zone(item.getId(), s_datetime, e_datetime, zones, "", "", "", "");
//                    }
//                }
//            });
//
//            holder.end.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                    s_datetime = holder.start.getText().toString();
//                    e_datetime = holder.end.getText().toString();
//                    zones = holder.zonee.getText().toString();
//
//                    if (databaseHandler.CheckIsDataAlreadyInDBorNot_Claim(item.getId())) {
//                        databaseHandler.update_Claim_List(item.getId(), s_datetime, e_datetime, zones, "", "", "", "");
//                    } else {
//                        databaseHandler.add_zone(item.getId(), s_datetime, e_datetime, zones, "", "", "", "");
//                    }
//
//                }
//            });
//            holder.zonee.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                    item_id = item.getId();
//                    s_datetime = holder.start.getText().toString();
//                    e_datetime = holder.end.getText().toString();
//                    zones = holder.zonee.getText().toString();
//
//                   /* if (databaseHandler.CheckIsDataAlreadyInDBorNot_Claim(item.getId())) {
//                        databaseHandler.update_Claim_List(item.getId(), s_datetime, e_datetime, zones, "", "", "", "");
//                    } else {
//                        databaseHandler.add_zone(item.getId(), s_datetime, e_datetime, zones, "", "", "", "");
//                    }*/
//
//                }
//            });
//
//            holder.start.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
///*                    SlideDateTimeListener listener = new SlideDateTimeListener() {
//
//                        @Override
//                        public void onDateTimeSet(Date date) {
//
//                            // Overriding onDateTimeCancel() is optional.
//                            Calendar c = Calendar.getInstance();
//                            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//                            String formattedDate = df.format(date);
//                            Date currentdate = new Date();
//
//                            Calendar cal = Calendar.getInstance();
//                            cal.roll(Calendar.DATE, -1);
//
//                            if (date.before(cal.getTime())) {
//                                Toast.makeText(getApplicationContext(), "You cannot select previous date", Toast.LENGTH_SHORT).show();
//
//                            } else {
//
//                                holder.start.setText(formattedDate);
//                            }
//
//                        }
//
//                        @Override
//                        public void onDateTimeCancel() {
//
//                        }
//                    };
//
//
//                    new SlideDateTimePicker.Builder(Add_new_Zone.this.getSupportFragmentManager())
//                            .setListener(listener)
//                            .setInitialDate(new Date())
//                            .setIs24HourTime(true)
//                            .setIndicatorColor(getResources().getColor(R.color.colorPrimary))
//                            .build()
//                            .show();*/
//
//                    setDate = 1;
//                    myCalendar = Calendar.getInstance();
//                    DatePickerDialog datepicker = new DatePickerDialog(Add_new_Zone.this, date, myCalendar
//                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                            myCalendar.get(Calendar.DAY_OF_MONTH));
//                    datepicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                    datepicker.show();
//                }
//            });
//
//            holder.end.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
///*                    SlideDateTimeListener listener = new SlideDateTimeListener() {
//
//                        @Override
//                        public void onDateTimeSet(Date date) {
//
//                            // Overriding onDateTimeCancel() is optional.
//                            Calendar c = Calendar.getInstance();
//
//                            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//                            String formattedDate = df.format(date);
//                            Date currentdate = new Date();
//
//                            if (date.before(currentdate)) {
//                                Toast.makeText(getApplicationContext(), "You cannot select previous date", Toast.LENGTH_SHORT).show();
//
//                            } else {
//
//                                holder.end.setText(formattedDate);
//                            }
//
//                        }
//
//                        @Override
//                        public void onDateTimeCancel() {
//
//                        }
//                    };
//
//                    new SlideDateTimePicker.Builder(Add_new_Zone.this.getSupportFragmentManager())
//                            .setListener(listener)
//                            .setInitialDate(new Date())
//                            .setIs24HourTime(true)
//                            .setIndicatorColor(getResources().getColor(R.color.colorPrimary))
//                            .build()
//                            .show();*/
//                    setDate = 2;
//                    myCalendar = Calendar.getInstance();
//                    DatePickerDialog datepicker = new DatePickerDialog(Add_new_Zone.this, date, myCalendar
//                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                            myCalendar.get(Calendar.DAY_OF_MONTH));
//                    datepicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                    datepicker.show();
//
//                }
//            });
//
//            holder.zonee.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//
//                        HideKeyboard(activity);
//
//                    }
//
//                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
//
//                        HideKeyboard(activity);
//
//                    }
//
//                    return true;
//                }
//            });
//
//
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("item_id", "item_id>>" + item_id);
//                    if (item.getId().isEmpty() || item.getId().equals("1")) {
//                        Toast.makeText(activity, "You Cannot delete Default Zone", Toast.LENGTH_SHORT).show();
//                    } else {
//                        check_address = "fbcksj";
//                        Delet_cart_item(item.getId());
//                    }
//                }
//            });
//
//
//            holder.zonee.setThreshold(2);
//            holder.zonee.setAdapter(new Multiple_zone_adapter.GooglePlacesAutocompleteAdapter(Add_new_Zone.this, R.layout.search_listitem));
//            holder.zonee.setOnItemClickListener(this);
//
//
//            return convertView;
//        }
//
//    }
//
//    public static void HideKeyboard(Activity activity) {
//        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
//        }
//    }
//
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
//            mTimePicker = new TimePickerDialog(Add_new_Zone.this, new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                    if (selectedHour <= hour && !DateFormat.is24HourFormat(Add_new_Zone.this) && selectedMinute <= minute && str_date.equals(current_date)) {
//                        selectTime = "";
//                        Toast.makeText(Add_new_Zone.this, "You cannot select previous date", Toast.LENGTH_SHORT).show();
//                    } else if (selectedMinute < 9) {
//                        selectTime = selectedHour + ":0" + selectedMinute + ":" + sec;
//                    } else if (sec < 9) {
//                        selectTime = selectedHour + ":" + selectedMinute + ":0" + sec;
//                    } else {
//                        selectTime = selectedHour + ":" + selectedMinute + ":" + sec;
//                        if (setDate == 1) {
//                            holder.start.setText(str_date + " " + selectTime);
//                        } else {
//                            holder.end.setText(str_date + " " + selectTime);
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
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
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
//    private void Delet_cart_item(final String getproductid) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Are You Sure, You Want delete Zone");
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//
//                databaseHandler.deletecart(getproductid);
//                feeditem.clear();
//                Cursor cursor = databaseHandler.get_zone_detail();
//                if (cursor != null) {
//                    cursor.moveToFirst();
//                    for (int i = 0; i < cursor.getCount(); i++) {
//                        User_Detail detail = new User_Detail();
//                        detail.setId(cursor.getString(0));
//                        detail.setName(cursor.getString(1));
//                        detail.setEmail(cursor.getString(2));
//                        detail.setImage(cursor.getString(3));
//
//                        feeditem.add(detail);
//
//                        cursor.moveToNext();
//                    }
//                    cursor.close();
//                    listadapter.notifyDataSetChanged();
//                }
//
//            }
//
//
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
//    private void Add_child_new_zone_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Add_new_Zone.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Add_more_zone",
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
//                                Toast.makeText(Add_new_Zone.this, msg, Toast.LENGTH_LONG).show();
//                                finish();
//
//                            } else if (success == 0) {
//
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Add_new_Zone.this, msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Add_new_Zone.this, msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 4) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Add_new_Zone.this, msg, Toast.LENGTH_LONG).show();
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
//                params.put("start_date_time", start_date);
//                params.put("end_date_time", end_date);
//                params.put("latitude", lattt);
//                params.put("longitude", langgg);
//                params.put("address", zone);
//                params.put("parent_child_id", Constant.parent_child_id);
//
//
//                Log.e("Insertttt", "" + params.toString());
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
//}