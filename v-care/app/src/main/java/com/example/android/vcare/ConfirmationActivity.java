package com.example.android.vcare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.PayPalConfig;
import com.example.android.vcare.model.User_Detail;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {
    Toolbar toolbar;
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    ProgressDialog pDialog;
    String amount,payment_id="",status,parent_id;
    TextView textViewId,textViewStatus,textViewAmount;
    String plan_price="",plan_id="";


    //Payment Amount
    private String paymentAmount;
    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;
    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent intent1 = getIntent();
        plan_price = intent1.getStringExtra("plan_price");
        plan_id    = intent1.getStringExtra("plan_id");

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pay Pal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHandler = new DatabaseHandler(this);

        databaseHandler = new DatabaseHandler(ConfirmationActivity.this);
        feeditem = new ArrayList<User_Detail>();
        Cursor cursor = databaseHandler.get_rider_detail();
        if (cursor != null){
            cursor.moveToFirst();
            for (int i =0 ; i< cursor.getCount(); i++){
                User_Detail detail = new User_Detail();
                detail.setId(cursor.getString(5));
                feeditem.add(detail);
                cursor.moveToNext();
            }
            cursor.close();
        }

        for (User_Detail userDetail : feeditem){
            parent_id  = userDetail.getId();
        }

        Log.e("parent_id", "parent_id>>" + parent_id);




        //Getting Intent
        Intent intent = getIntent();
         plan_id = intent.getStringExtra("plan_id");
         amount     = intent.getStringExtra("PaymentAmount");

         textViewId = (TextView) findViewById(R.id.paymentId);
         textViewStatus= (TextView) findViewById(R.id.paymentStatus);
         textViewAmount = (TextView) findViewById(R.id.paymentAmount);


        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
            Log.e("jsonDetails","PAYPAL_RESPONSE====>"+jsonDetails);






        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {

        payment_id = jsonDetails.getString("id");
        status     = jsonDetails.getString("state");
        Log.e("jsonDetails","payment_id====>"+payment_id);
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus= (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount + " USD");


        paypal_API();

    }


    private void paypal_API() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(ConfirmationActivity.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"paypal",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {

                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                            } else if(success == 0) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


                            }
                            else if (success==2) {

                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();

                            } else if (success==3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error.Response", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("parent_id", parent_id);
                params.put("plan_id", plan_id);
                params.put("transaction_id",payment_id);
                params.put("plan_status","1");
                params.put("payment_type","0");

                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finishAffinity();

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finishAffinity();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
