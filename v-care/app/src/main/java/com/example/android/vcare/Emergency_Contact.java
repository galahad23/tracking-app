package com.example.android.vcare;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.User_Detail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Emergency_Contact extends Fragment implements View.OnClickListener {

    EditText emergencyNumberFirst, emergencyNumberSecond, emergencyNumberThird,
            emergencyPersonFirst, emergencyPersonSecond, emergencyPersonThird;
    Button submitEmergencyNumber;
    String firstNumber = "", secondNumber = "", thirdNumber = "", parent_id = "",
            firstPerson = "", secondPerson = "", thirdPerson = "", emergency_number = "", person_name = "";
    ProgressDialog pDialog;
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;

    public Emergency_Contact() {
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
        View rootview = inflater.inflate(R.layout.fragment_emergency__contact, container, false);

        emergencyNumberFirst = (EditText) rootview.findViewById(R.id.emergencyNumberFirst);
        emergencyNumberSecond = (EditText) rootview.findViewById(R.id.emergencyNumberSecond);
        emergencyNumberThird = (EditText) rootview.findViewById(R.id.emergencyNumberThird);

        emergencyPersonFirst = (EditText) rootview.findViewById(R.id.emergencyPersonFirst);
        emergencyPersonSecond = (EditText) rootview.findViewById(R.id.emergencyPersonSecond);
        emergencyPersonThird = (EditText) rootview.findViewById(R.id.emergencyPersonThird);

        submitEmergencyNumber = (Button) rootview.findViewById(R.id.submitEmergencyNumber);
        submitEmergencyNumber.setOnClickListener(this);


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

        Get_Emergency_Number();


        return rootview;
    }


    @Override
    public void onClick(View v) {

        if (v == submitEmergencyNumber) {

            firstNumber = emergencyNumberFirst.getText().toString();
            secondNumber = emergencyNumberSecond.getText().toString();
            thirdNumber = emergencyNumberThird.getText().toString();

            firstPerson = emergencyPersonFirst.getText().toString();
            secondPerson = emergencyPersonSecond.getText().toString();
            thirdPerson = emergencyPersonThird.getText().toString();


            if (firstPerson.trim().length() == 0 && secondPerson.trim().length() == 0 && thirdPerson.trim().length() == 0) {

                Toast.makeText(getActivity(), "Please Enter Contact Person Name.", Toast.LENGTH_SHORT).show();

            } else if (firstNumber.trim().length() == 0 && secondNumber.trim().length() == 0 && thirdNumber.trim().length() == 0) {

                Toast.makeText(getActivity(), "Please Enter At Least One Number.", Toast.LENGTH_SHORT).show();

            } else {
                person_name = firstPerson + "," + secondPerson + "," + thirdPerson;
                emergency_number = firstNumber + "," + secondNumber + "," + thirdNumber;

                Add_Emergency_Number();
            }

        }

    }


    private void Get_Emergency_Number() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Get_Emergency_Number",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {

                                JSONArray array = objJson.getJSONArray("ParentInfo");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsobj = array.getJSONObject(i);

                                    String person_name = jsobj.getString("person_name");
                                    String emergency_number = jsobj.getString("emergency_number");

                                    if (i == 0) {
                                        emergencyNumberFirst.setText(emergency_number);
                                        emergencyPersonFirst.setText(person_name);
                                    } else if (i == 1) {
                                        emergencyNumberSecond.setText(emergency_number);
                                        emergencyPersonSecond.setText(person_name);
                                    } else if (i == 2) {
                                        emergencyNumberThird.setText(emergency_number);
                                        emergencyPersonThird.setText(person_name);
                                    }


                                }


                            } else if (success == 0) {

                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                            } else if (success == 1) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            } else if (success == 2) {
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
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);
    }


    private void Add_Emergency_Number() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Update_Emergency_Number",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {

                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();


                            } else if (success == 0) {

                                String msg = objJson.getString("text");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

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
                params.put("person_name", person_name);
                params.put("emergency_number", emergency_number);

                Log.e("Insertttt", params.toString());
                return params;
            }
        };
        // for response time increase
        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);
    }

}
