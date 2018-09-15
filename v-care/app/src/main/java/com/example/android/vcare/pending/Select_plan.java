//package com.example.android.vcare.pending;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.android.vcare.AppController;
//import com.example.android.vcare.R;
//import com.example.android.vcare.model.PayPalConfig;
//import com.example.android.vcare.model.UserData;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PayPalPayment;
//import com.paypal.android.sdk.payments.PayPalService;
//import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmation;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Select_plan extends AppCompatActivity {
//    ProgressDialog pDialog;
//    Toolbar toolbar;
//    Button select_paln;
//    RadioButton paypal, smoovpay;
//    RadioGroup radioGroup;
//    LinearLayout paln_layout;
//    Spinner plan_spinner;
//    String plan_name = "", timeperoide = "", allow_devices = "", description = "", strplan = "";
//    private List<UserData> feeditem = new ArrayList<>();
//    ArrayList<String> PLAN_NAME = new ArrayList<String>();
//    ArrayList<Double> PLAN_PRICE = new ArrayList<Double>();
//    ArrayList<Integer> PLAN_ID = new ArrayList<Integer>();
//    int plan_id;
//    String plan_id_intent;
//    Double planprice;
//    TextView desc;
//
//
//    //Payment Amount
//    private String paymentAmount;
//    //Paypal intent request code to track onActivityResult method
//    public static final int PAYPAL_REQUEST_CODE = 123;
//    //Paypal Configuration Object
//    private static PayPalConfiguration config = new PayPalConfiguration()
//            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
//            // or live (ENVIRONMENT_PRODUCTION)
//            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
//            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_plan);
//
//        PLAN_ID.add(0);
//        PLAN_PRICE.add(0.00);
//        PLAN_NAME.add("Select Plan");
//
//        Intent intent = getIntent();
//
//        plan_id_intent = intent.getStringExtra("plan_id");
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Subscription Plan");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        select_paln = (Button) findViewById(R.id.select_plan);
//        paypal = (RadioButton) findViewById(R.id.paypal);
//        smoovpay = (RadioButton) findViewById(R.id.smoovpay);
//        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
//
//        desc = (TextView) findViewById(R.id.description);
//
//
//        plan_spinner = (Spinner) findViewById(R.id.select_plan_spinner);
//        paln_layout = (LinearLayout) findViewById(R.id.planlist_layout);
//
//
//        select_paln.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                strplan = plan_spinner.getSelectedItem().toString();
//
//                if (strplan.equalsIgnoreCase("Select Plan")) {
//                    Toast.makeText(Select_plan.this, "Please Choose plan first", Toast.LENGTH_SHORT).show();
//                } else if (radioGroup.getCheckedRadioButtonId() == -1) {
//                    Toast.makeText(Select_plan.this, "Please choose payment method", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    if (paypal.isChecked()) {
//
//                        getPayment();
//                       /* Intent intent = new Intent(getApplicationContext(),Paypal_integration.class);
//                        intent.putExtra("plan_price",String.valueOf(planprice));
//                        intent.putExtra("plan_id",String.valueOf(plan_id));
//                        startActivity(intent);
//                        finish();*/
//
//                    } else {
//                        Intent intent = new Intent(getApplicationContext(), Smoovpay.class);
//                        intent.putExtra("plan_id", String.valueOf(plan_id));
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            }
//        });
//
//        // plan list display
//        Get_plan_list();
//
//        paln_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                plan_spinner.performClick();
//            }
//        });
//
//        plan_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                plan_id = PLAN_ID.get(position);
//                Double price = PLAN_PRICE.get(position);
//
//                planprice = price;
//                Log.e("planprice", "planprice>>" + planprice);
//
//                if (plan_id != 0) {
//                    int pos = position - 1;
//
//                    String view_route = feeditem.get(pos).getIMEI();
//
//                    if (view_route.equals("0")) {
//
//                        view_route = "No";
//                    } else {
//                        view_route = "yes";
//                    }
//
//                    desc.setText("Description: " +
//                            feeditem.get(pos).getMessage() + "\n View Route History: " + view_route);
//
//                } else {
//
//                    desc.setText("Please select plan");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//    }
//
//    private void Get_plan_list() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Select_plan.this);
//        pDialog.setMessage("Please wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "planlist",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            int success = obj.getInt("success");
//                            if (success == 1) {
//                                JSONArray array = obj.getJSONArray("planlist");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//
//                                    int p_id = jsobj.getInt("plan_id");
//                                    plan_name = jsobj.getString("plan_name");
//                                    planprice = jsobj.getDouble("plan_price");
//                                    timeperoide = jsobj.getString("view_route");
//                                    //   allow_devices = jsobj.getString("allowed_device");
//                                    description = jsobj.getString("description");
//
//                                    UserData detail = new UserData();
//
//                                    detail.setID(p_id);
//                                    detail.setName(plan_name);
//                                    detail.setEmail(allow_devices);
//                                    detail.setMessage(description);
//                                    detail.setIMEI(timeperoide);
//                                    detail.setMobile(String.valueOf(planprice));
//
//                                    feeditem.add(detail);
//
//                                    PLAN_ID.add(p_id);
//                                    PLAN_PRICE.add(planprice);
//                                    PLAN_NAME.add(plan_name + "( USD " + planprice + ")");
//
//                                    ArrayAdapter<String> data2 = new ArrayAdapter<String>(Select_plan.this,
//                                            R.layout.spinner_item, PLAN_NAME);
//
//                                    plan_spinner.setAdapter(data2);
//
//                                    // for auto set spinner Value
//
//                                    Log.e("plan_id_intent", "plan_id_intent>>" + plan_id_intent);
//
//
//                                    if (plan_id_intent == null) {
//
//                                    } else {
//                                        int position = Integer.parseInt(plan_id_intent);
//                                        int index = PLAN_ID.indexOf(position);
//                                        plan_spinner.setSelection(index);
//                                    }
//
//
//                                }
//
//                            } else if (success == 0) {
//                                String msg = obj.getString("text");
//                                Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_LONG).show();
//                            } else if (success == 2) {
//
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
//                return params;
//            }
//        };
//
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//
//    private void getPayment() {
//        //Getting the amount from editText
//        paymentAmount = String.valueOf(planprice);
//
//
//        //Creating a paypalpayment
//        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", strplan,
//                PayPalPayment.PAYMENT_INTENT_SALE);
//
//        //Creating Paypal Payment activity intent
//        Intent intent = new Intent(this, PaymentActivity.class);
//
//        //putting the paypal configuration to the intent
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//        //Puting paypal payment to the intent
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
//
//        //Starting the intent activity for result
//        //the request code will be used on the method onActivityResult
//        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //If the result is from paypal
//        if (requestCode == PAYPAL_REQUEST_CODE) {
//
//            //If the result is OK i.e. user has not canceled the payment
//            if (resultCode == Activity.RESULT_OK) {
//                //Getting the payment confirmation
//                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//
//                //if confirmation is not null
//                if (confirm != null) {
//                    try {
//                        //Getting the payment details
//                        String paymentDetails = confirm.toJSONObject().toString(4);
//                        Log.i("paymentExample", paymentDetails);
//
//                        Log.e("paymentDetails", "=======>" + paymentDetails);
//
//
//                        //Starting a new activity for the payment details and also putting the payment details with intent
//                        startActivity(new Intent(this, ConfirmationActivity.class)
//                                .putExtra("plan_id", String.valueOf(plan_id))
//                                .putExtra("PaymentDetails", paymentDetails)
//                                .putExtra("PaymentAmount", paymentAmount));
//
//                    } catch (JSONException e) {
//                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Log.i("paymentExample", "The user canceled.");
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
//            }
//        }
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
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finishAffinity();
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
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
//        finish();
//        super.onBackPressed();
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
//}
