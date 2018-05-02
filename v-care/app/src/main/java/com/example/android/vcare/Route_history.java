package com.example.android.vcare;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.vcare.adapter.Route_history_adapter;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.Group_detail_list;
import com.example.android.vcare.model.User_Detail;
import com.example.android.vcare.model.User_function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Route_history extends Fragment {
    User_function user_function = new User_function();
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    LinearLayout date_ly, main_ly, view_ly;
    EditText datee;
    ProgressDialog pDialog;
    Toolbar toolbar;

    LinearLayout child_list_ly;
    Spinner child_list_spinner;
    String child_name = "", parent_id = "", search_date = "";

    ArrayList<String> CHILD_NAME = new ArrayList<String>();
    ArrayList<Integer> CHILD_ID = new ArrayList<Integer>();
    int child_id;

    private List<Group_detail_list> helplist = new ArrayList<>();
    private Route_history_adapter mAdapter;
    private RecyclerView recyclerView;
    Calendar myCalendar;
    int hour, minute, sec;
    String data = "", str_date = "", selectTime = "";

    public Route_history() {
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
        View rootView = inflater.inflate(R.layout.fragment_route_history, container, false);

        CHILD_ID.add(0);
        CHILD_NAME.add("Please select Member");

        databaseHandler = new DatabaseHandler(getActivity());
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


        datee = (EditText) rootView.findViewById(R.id.date_time);
        date_ly = (LinearLayout) rootView.findViewById(R.id.date_ly);
        main_ly = (LinearLayout) rootView.findViewById(R.id.main_ly);
        view_ly = (LinearLayout) rootView.findViewById(R.id.view);

        child_list_spinner = (Spinner) rootView.findViewById(R.id.select_child_spinner);
        child_list_ly = (LinearLayout) rootView.findViewById(R.id.child_ly);

        child_list_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child_list_spinner.performClick();
            }
        });

        child_list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                child_id = CHILD_ID.get(position);
                Log.e("child_id", "child_id>>" + child_id);
                if (!child_list_spinner.getSelectedItem().equals("Please select Member")) {
                    if (!TextUtils.isEmpty(search_date)) {
                        Complete_route_history();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        datee.setText( sdf.format( new Date() ));*/

        date_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                SlideDateTimeListener listener = new SlideDateTimeListener() {

                    @Override
                    public void onDateTimeSet(Date date) {

                        // Overriding onDateTimeCancel() is optional.
                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = df.format(date);
                        Date currentdate = new Date();

                        datee.setText(formattedDate);
                        search_date = datee.getText().toString().trim();
                        if (child_id == 0) {
                            Toast.makeText(getActivity(), "Please Select Member", Toast.LENGTH_SHORT).show();
                        } else if (search_date.isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
                        } else {
                            Complete_route_history();
                        }

                    }

                    @Override
                    public void onDateTimeCancel() {
                    }
                };


                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        //.setIs24HourTime(true)
                        .setIndicatorColor(getResources().getColor(R.color.colorPrimary))
                        .build()
                        .show();*/
                myCalendar = Calendar.getInstance();
                DatePickerDialog datepicker = new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datepicker.show();

            }
        });

        view_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (child_id == 0) {
                    Toast.makeText(getActivity(), "Please Select Member", Toast.LENGTH_SHORT).show();
                } else if (search_date.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
                } else {
                    Route_on_map();
                }


            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleview);
        mAdapter = new Route_history_adapter(helplist);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        GET_CHILD_LIST();


        return rootView;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

        private void updateLabel() {
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            str_date = sdf.format(myCalendar.getTime());
            datee.setText(str_date);
            search_date = datee.getText().toString().trim();
            if (child_id == 0) {
                Toast.makeText(getActivity(), "Please Select Member", Toast.LENGTH_SHORT).show();
            } else if (search_date.isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
            } else {
                Complete_route_history();
            }
//            opentimediloag();
        }

        private void opentimediloag() {

            Calendar mcurrentTime = Calendar.getInstance();
            hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            minute = mcurrentTime.get(Calendar.MINUTE);
            sec = mcurrentTime.get(Calendar.SECOND);
            TimePickerDialog mTimePicker;

            System.out.println("Current time => " + mcurrentTime.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            final String current_date = df.format(mcurrentTime.getTime());
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    if (selectedHour <= hour && !DateFormat.is24HourFormat(getActivity()) && selectedMinute <= minute && str_date.equals(current_date)) {
                        selectTime = "";
                        Toast.makeText(getActivity(), "You cannot select previous date", Toast.LENGTH_SHORT).show();
                    } else if (selectedMinute < 9) {
                        selectTime = selectedHour + ":0" + selectedMinute + ":" + sec;
                    } else if (sec < 9) {
                        selectTime = selectedHour + ":" + selectedMinute + ":0" + sec;
                    } else {
                        selectTime = selectedHour + ":" + selectedMinute + ":" + sec;
                        datee.setText(str_date + " " + selectTime);
                        search_date = datee.getText().toString().trim();
                        if (child_id == 0) {
                            Toast.makeText(getActivity(), "Please Select Member", Toast.LENGTH_SHORT).show();
                        } else if (search_date.isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
                        } else {
                            Complete_route_history();
                        }
                    }


                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.show();

            datee.setText(str_date + " " + selectTime);

        }
    };


    private void GET_CHILD_LIST() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Parent_Child_List",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            int success = obj.getInt("success");
                            if (success == 1) {
                                JSONArray array = obj.getJSONArray("child_list");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsobj = array.getJSONObject(i);

                                    int p_id = jsobj.getInt("child_id");
                                    child_name = jsobj.getString("name");


                                    CHILD_ID.add(p_id);
                                    CHILD_NAME.add(child_name);

                                    ArrayAdapter<String> data2 = new ArrayAdapter<String>(getActivity(),
                                            R.layout.spinner_item, CHILD_NAME);

                                    child_list_spinner.setAdapter(data2);


                                }

                            } else if (success == 0) {
                                String msg = obj.getString("text");
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_LONG).show();
                            } else if (success == 2) {
                                String msg = obj.getString("text");
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_LONG).show();
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
                Log.e("Insertttt", "" + params.toString());
                return params;
            }
        };

        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }


    private void Complete_route_history() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Parent_Child_Route_History",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            int success = obj.getInt("success");
                            if (success == 1) {

                                helplist.clear();
                                main_ly.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);

                                JSONArray array = obj.getJSONArray("child_history");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsobj = array.getJSONObject(i);
                                    Group_detail_list item = new Group_detail_list();
                                    item.setName(jsobj.getString("address"));
                                    item.setImage(jsobj.getString("created_date"));
                                    item.setId(jsobj.getString("child_id"));

                                    helplist.add(item);
                                }
                                mAdapter.notifyDataSetChanged();

                            } else if (success == 0) {
                                String msg = obj.getString("text");
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_LONG).show();
                            } else if (success == 2) {
                                main_ly.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                mAdapter.notifyDataSetChanged();

                                String msg = obj.getString("text");
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_LONG).show();
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
                        pDialog.dismiss();

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("child_id", String.valueOf(child_id));
                params.put("search_date", search_date);
                params.put("parent_id", parent_id);
                Log.e("Insertttt", "" + params.toString());
                return params;
            }
        };

        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    private void Route_on_map() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "DROW_CHILD_ROUTE_MAP",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            int success = obj.getInt("success");
                            if (success == 1) {

                                String url = obj.getString("Route_url");

                                Intent intent = new Intent(getActivity(), Route_map.class);
                                intent.putExtra("web_url", url);
                                startActivity(intent);


                            } else if (success == 0) {
                                String msg = obj.getString("text");
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_LONG).show();
                            } else if (success == 2) {
                                main_ly.setVisibility(View.GONE);
                                String msg = obj.getString("text");
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_LONG).show();
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
                        pDialog.dismiss();

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("child_id", String.valueOf(child_id));
                params.put("search_date", search_date);
                Log.e("Insertttt", "" + params.toString());
                return params;
            }
        };

        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

}