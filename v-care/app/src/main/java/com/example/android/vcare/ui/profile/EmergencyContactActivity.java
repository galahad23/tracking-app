package com.example.android.vcare.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.example.android.vcare.MyApplication;
import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ActivityEmergencyContactBinding;
import com.example.android.vcare.event.AccountEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.job.GetEmergencyNumberJob;
import com.example.android.vcare.job.UpdateEmergencyNumberJob;
import com.example.android.vcare.model.User;
import com.example.android.vcare.ui.BaseActivity;
import com.example.android.vcare.util.EventBusUtil;
import com.example.android.vcare.util.Util;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class EmergencyContactActivity extends BaseActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, EmergencyContactActivity.class);
        context.startActivity(starter);
    }

    private ActivityEmergencyContactBinding binding;
    private final int hashCode = hashCode();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emergency_contact);
        setDisplayHomeAsUpEnabled();
        setBackNavigation();
        setToolbarTitle(R.string.emergency_contacts);

        getEmergencyContacts();
        binding.submit.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == binding.submit) {
                if (isValid()) {
                    showLoadingDialog();
                    MyApplication.addJobInBackground(new UpdateEmergencyNumberJob(allPersonName, allEmergencyContact, hashCode));
                }
            }
        }
    };

    private String allPersonName;
    private String allEmergencyContact;

    private boolean isValid() {
        String firstName = binding.firstPersonName.getText().toString();
        String firstNumber = binding.firstNumber.getText().toString();
        String secondName = binding.secondPersonName.getText().toString();
        String secondNumber = binding.secondNumber.getText().toString();
        String thirdName = binding.thirdPersonName.getText().toString();
        String thirdNumber = binding.thirdNumber.getText().toString();

        if ((TextUtils.isEmpty(firstName) && TextUtils.isEmpty(firstNumber))
                && (TextUtils.isEmpty(secondName) && TextUtils.isEmpty(secondNumber))
                && (TextUtils.isEmpty(thirdName) && TextUtils.isEmpty(thirdNumber))) {
            Util.showOkOnlyDisableCancelAlertDialog(this, getString(R.string.error), getString(R.string.at_least_one_emergency_contact));
            return false;
        } else {
            allPersonName = firstName + "," + secondName + "," + thirdName;
            allEmergencyContact = firstNumber + "," + secondNumber + "," + thirdNumber;
            return true;
        }
    }

    private void getEmergencyContacts() {
        showLoadingDialog();
        MyApplication.addJobInBackground(new GetEmergencyNumberJob(hashCode));
    }

    private void populateEmergencyContacts(List<User> emergencyContacts) {
        for (int i = 0; i < emergencyContacts.size(); i++) {
            User user = emergencyContacts.get(i);
            switch (i) {
                case 0:
                    binding.firstPersonName.setText(user.getPersonName());
                    binding.firstNumber.setText(user.getEmergencyNumber());
                    break;
                case 1:
                    binding.secondPersonName.setText(user.getPersonName());
                    binding.secondNumber.setText(user.getEmergencyNumber());
                    break;
                case 2:
                    binding.thirdPersonName.setText(user.getPersonName());
                    binding.thirdNumber.setText(user.getEmergencyNumber());
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnUpdateEmergencyContact event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            Util.showOkOnlyDisableCancelAlertDialog(this,
                    getString(R.string.information),
                    event.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnGetEmergencyContact event) {
        if (hashCode == hashCode()) {
            populateEmergencyContacts(event.getEmergencyContactList());
            dismissLoadingDialog();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(ExceptionEvent event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            Util.showOkOnlyDisableCancelAlertDialog(this, null, event.getErrorMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBusUtil.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusUtil.unregister(this);
    }

    //    EditText emergencyNumberFirst, emergencyNumberSecond, emergencyNumberThird,
//            emergencyPersonFirst, emergencyPersonSecond, emergencyPersonThird;
//    Button submitEmergencyNumber;
//    String firstNumber = "", secondNumber = "", thirdNumber = "", parent_id = "",
//            firstPerson = "", secondPerson = "", thirdPerson = "", emergency_number = "", person_name = "";
//    ProgressDialog pDialog;
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//
//    public EmergencyContactActivity() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootview = inflater.inflate(R.layout.activity_emergency_contact, container, false);
//
//        emergencyNumberFirst = (EditText) rootview.findViewById(R.id.emergencyNumberFirst);
//        emergencyNumberSecond = (EditText) rootview.findViewById(R.id.emergencyNumberSecond);
//        emergencyNumberThird = (EditText) rootview.findViewById(R.id.emergencyNumberThird);
//
//        emergencyPersonFirst = (EditText) rootview.findViewById(R.id.emergencyPersonFirst);
//        emergencyPersonSecond = (EditText) rootview.findViewById(R.id.emergencyPersonSecond);
//        emergencyPersonThird = (EditText) rootview.findViewById(R.id.emergencyPersonThird);
//
//        submitEmergencyNumber = (Button) rootview.findViewById(R.id.submitEmergencyNumber);
//        submitEmergencyNumber.setOnClickListener(this);
//
//
//        databaseHandler = new DatabaseHandler(getActivity());
//        feeditem = new ArrayList<User_Detail>();
//        Cursor cursor = databaseHandler.get_rider_detail();
//        if (cursor != null) {
//            cursor.moveToFirst();
//            for (int i = 0; i < cursor.getCount(); i++) {
//                User_Detail detail = new User_Detail();
//                detail.setId(cursor.getString(5));
//                feeditem.add(detail);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//
//        for (User_Detail userDetail : feeditem) {
//            parent_id = userDetail.getId();
//        }
//
//        Get_Emergency_Number();
//
//
//        return rootview;
//    }
//
//
//    @Override
//    public void onClick(View v) {
//
//        if (v == submitEmergencyNumber) {
//
//            firstNumber = emergencyNumberFirst.getText().toString();
//            secondNumber = emergencyNumberSecond.getText().toString();
//            thirdNumber = emergencyNumberThird.getText().toString();
//
//            firstPerson = emergencyPersonFirst.getText().toString();
//            secondPerson = emergencyPersonSecond.getText().toString();
//            thirdPerson = emergencyPersonThird.getText().toString();
//
//
//            if (firstPerson.trim().length() == 0 && secondPerson.trim().length() == 0 && thirdPerson.trim().length() == 0) {
//
//                Toast.makeText(getActivity(), "Please Enter Contact Person Name.", Toast.LENGTH_SHORT).show();
//
//            } else if (firstNumber.trim().length() == 0 && secondNumber.trim().length() == 0 && thirdNumber.trim().length() == 0) {
//
//                Toast.makeText(getActivity(), "Please Enter At Least One Number.", Toast.LENGTH_SHORT).show();
//
//            } else {
//                person_name = firstPerson + "," + secondPerson + "," + thirdPerson;
//                emergency_number = firstNumber + "," + secondNumber + "," + thirdNumber;
//
//                Add_Emergency_Number();
//            }
//
//        }
//
//    }
//
//
//    private void Get_Emergency_Number() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Get_Emergency_Number",
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
//
//                                JSONArray array = objJson.getJSONArray("User");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//
//                                    String person_name = jsobj.getString("person_name");
//                                    String emergency_number = jsobj.getString("emergency_number");
//
//                                    if (i == 0) {
//                                        emergencyNumberFirst.setText(emergency_number);
//                                        emergencyPersonFirst.setText(person_name);
//                                    } else if (i == 1) {
//                                        emergencyNumberSecond.setText(emergency_number);
//                                        emergencyPersonSecond.setText(person_name);
//                                    } else if (i == 2) {
//                                        emergencyNumberThird.setText(emergency_number);
//                                        emergencyPersonThird.setText(person_name);
//                                    }
//
//
//                                }
//
//
//                            } else if (success == 0) {
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 1) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//    }
//
//
//    private void Add_Emergency_Number() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "Update_Emergency_Number",
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
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//
//                            } else if (success == 0) {
//
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
//                params.put("person_name", person_name);
//                params.put("emergency_number", emergency_number);
//
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//    }
}
