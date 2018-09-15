//package com.example.android.vcare.pending;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.example.android.vcare.R;
//import com.example.android.vcare.model.PayPalConfig;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PayPalPayment;
//import com.paypal.android.sdk.payments.PayPalService;
//import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmation;
//
//import org.json.JSONException;
//
//import java.math.BigDecimal;
//
//public class Paypal_integration extends AppCompatActivity implements View.OnClickListener {
//    Toolbar toolbar;
//    //The views
//    private Button buttonPay;
//    private EditText editTextAmount;
//    String plan_price="",plan_id="";
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
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_paypal_integration);
//
//        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("PayPal Gateway");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        Intent intent1 = getIntent();
//        plan_price = intent1.getStringExtra("plan_price");
//        plan_id    = intent1.getStringExtra("plan_id");
//
//
//
//        buttonPay = (Button) findViewById(R.id.buttonPay);
//        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
//        editTextAmount.setText(plan_price);
//
//
//        buttonPay.setOnClickListener(this);
//
//        Intent intent = new Intent(this, PayPalService.class);
//
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//
//        startService(intent);
//    }
//
//    @Override
//    public void onClick(View v) {
//        getPayment();
//    }
//
//    @Override
//    public void onDestroy() {
//        stopService(new Intent(this, PayPalService.class));
//        super.onDestroy();
//    }
//
//    private void getPayment() {
//        //Getting the amount from editText
//        paymentAmount = editTextAmount.getText().toString();
//
//        //Creating a paypalpayment
//        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "V Care Plan ",
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
//                        Log.e("paymentDetails","=======>"+paymentDetails);
//
//
//                        //Starting a new activity for the payment details and also putting the payment details with intent
//                        startActivity(new Intent(this, ConfirmationActivity.class)
//                                .putExtra("plan_id",plan_id)
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
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
//
//
//}