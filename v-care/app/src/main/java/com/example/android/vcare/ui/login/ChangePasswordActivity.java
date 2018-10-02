package com.example.android.vcare.ui.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.example.android.vcare.MyApplication;
import com.example.android.vcare.R;
import com.example.android.vcare.databinding.ActivityChangePasswordBinding;
import com.example.android.vcare.event.AccountEvent;
import com.example.android.vcare.event.ExceptionEvent;
import com.example.android.vcare.job.ChangePasswordJob;
import com.example.android.vcare.ui.BaseActivity;
import com.example.android.vcare.util.EventBusUtil;
import com.example.android.vcare.util.Util;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChangePasswordActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, ChangePasswordActivity.class);
        context.startActivity(starter);
    }


    private ActivityChangePasswordBinding binding;
    private final int hashCode = hashCode();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        setToolbarTitle(R.string.change_password);
        setDisplayHomeAsUpEnabled();
        setBackNavigation();

        binding.submit.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == binding.submit) {
                if (isValid()) {
                    attemptChangePassword();
                }
            }
        }
    };

    private void attemptChangePassword() {
        showLoadingDialog();
        String currentPassword = binding.currentPassword.getText().toString();
        String newPassword = binding.newPassword.getText().toString();
        MyApplication.addJobInBackground(new ChangePasswordJob(currentPassword, newPassword, hashCode));
    }

    private boolean isValid() {
        boolean isValid = true;
        String currentPassword = binding.currentPassword.getText().toString();
        String newPassword = binding.newPassword.getText().toString();
        String confirmPassword = binding.confirmPassword.getText().toString();

        if (TextUtils.isEmpty(currentPassword)) {
            isValid = false;
            binding.currentPassword.setError(getString(R.string.field_cannot_empty));
        }
        if (TextUtils.isEmpty(newPassword)) {
            isValid = false;
            binding.newPassword.setError(getString(R.string.field_cannot_empty));
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            isValid = false;
            binding.confirmPassword.setError(getString(R.string.field_cannot_empty));
        }
        if (isValid) {
            if (!newPassword.equalsIgnoreCase(confirmPassword)) {
                binding.confirmPassword.setError(getString(R.string.confirm_password_does_not_match));
                isValid = false;
            }
        }
        return isValid;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(AccountEvent.OnChangePassword event) {
        if (hashCode == event.getHashCode()) {
            Util.showOkOnlyDisableCancelAlertDialog(this,
                    getString(R.string.information),
                    event.getMessage(),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandle(ExceptionEvent event) {
        if (hashCode == event.getHashCode()) {
            dismissLoadingDialog();
            Util.showOkOnlyDisableCancelAlertDialog(this, null, event.getErrorMessage());
        }
    }

    //    UserHandler2 user_handler = new UserHandler2();
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    ProgressDialog pDialog;
//    EditText old_password,password,confirmpass;
//    Button change;
//    private TextInputLayout inputLayoutoldpassword , inputLayoutpassword,inputLayouteconfirmpass;
//    String strold="",strnew="",strconfirm="",parent_id="",mobile_token="";
//
//    public ChangePasswordActivity() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.activity_change_password, container, false);
//
//
//        databaseHandler = new DatabaseHandler(getActivity());
//        feeditem = new ArrayList<User_Detail>();
//        Cursor cursor = databaseHandler.get_rider_detail();
//        if (cursor != null){
//            cursor.moveToFirst();
//            for (int i =0 ; i< cursor.getCount(); i++){
//                User_Detail detail = new User_Detail();
//                detail.setId(cursor.getString(5));
//                feeditem.add(detail);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//
//        for (User_Detail userDetail : feeditem){
//            parent_id  = userDetail.getId();
//        }
//
//        Cursor cursor1 = databaseHandler.get_token_detail();
//        if (cursor1 != null){
//            cursor1.moveToFirst();
//            for (int j=0; j< cursor1.getCount(); j++){
//                mobile_token = cursor1.getString(0);
//                cursor1.moveToNext();
//            }
//
//            cursor1.close();
//        }
//
//        Log.e("parent_id", "parent_id>>"+ parent_id);
//        Log.e("mobiletoken", "mobiletokenn>>"+ mobile_token);
//
//        inputLayoutoldpassword = (TextInputLayout)rootView.findViewById(R.id.input_layout_oldpassword);
//        inputLayoutpassword = (TextInputLayout) rootView.findViewById(R.id.input_layout_password);
//        inputLayouteconfirmpass = (TextInputLayout) rootView.findViewById(R.id.input_layout_confirm);
//
//        old_password  = (EditText)rootView.findViewById(R.id.old_password);
//        password      = (EditText)rootView.findViewById(R.id.password);
//        confirmpass   = (EditText)rootView.findViewById(R.id.confirm);
//
//        old_password.addTextChangedListener(new MyTextWatcher(old_password));
//        password.addTextChangedListener(new MyTextWatcher(password));
//        confirmpass.addTextChangedListener(new MyTextWatcher(confirmpass));
//
//        change = (Button)rootView.findViewById(R.id.change);
//        change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                change_password();
//            }
//        });
//        return rootView;
//    }
//
//    private void change_password() {
//
//        if (!validateoldpassword()) {
//            return;
//        }
//
//
//        if (!validatepassword()) {
//            return;
//        }
//
//        if (!validateconfirmpas()) {
//            return;
//        }
//        else {
//            Change_password_api();
//            //Toast.makeText(getActivity(), "Successfully change password", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private boolean validateoldpassword() {
//        strold = old_password.getText().toString();
//        if (strold.trim().length()==0) {
//            inputLayoutoldpassword.setError("Please enter your Old password");
//            requestFocus(old_password);
//            return false;
//        } else {
//            inputLayoutoldpassword.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//
//    private boolean validatepassword() {
//        strnew = password.getText().toString();
//        if (strnew.trim().length()==0) {
//            inputLayoutpassword.setError("Enter new password.");
//            requestFocus(password);
//            return false;
//        } else {
//            inputLayoutpassword.setErrorEnabled(false);
//        }
//        return true;
//    }
//
//    private boolean validateconfirmpas() {
//        strconfirm = confirmpass.getText().toString();
//        if (!strconfirm.equals(strnew)) {
//            inputLayouteconfirmpass.setError("password do not match.");
//            requestFocus(confirmpass);
//            return false;
//        } else {
//            inputLayouteconfirmpass.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//
//    private class MyTextWatcher implements TextWatcher {
//
//        private View view;
//
//        private MyTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//
//                case R.id.old_password:
//                    validateoldpassword();
//                    break;
//                case R.id.password:
//                    validatepassword();
//                    break;
//                case R.id.confirm:
//                    validateconfirmpas();
//                    break;
//            }
//        }
//    }
//
//    private void Change_password_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"changepass",
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.dismiss();
//                        // response
//                        Log.e("Responsesearch", response);
//                        try {
//                            JSONObject objJson = new JSONObject(response);
//                            int success = objJson.getInt("success");
//                            if (success == 1) {
//                                String msg = objJson.getString("text");
//                                String mobiletoken = objJson.getString("mobile_token");
//                                Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
//                                databaseHandler.resetTables("token");
//                                databaseHandler.add_token(mobiletoken);
//
//                                Intent intent = new Intent(getActivity(),MainActivity.class);
//                                startActivity(intent);
//                                getActivity().onBackPressed();
//
//                            } else if(success == 0) {
//                                user_handler.logoutUser(getActivity());
//                                alert();
//                            }
//                            else if (success==2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
//                            } else if (success==3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
//                            }
//
//                        } catch (JSONException e) {
//                            // TODO Auto-generated catch block
//                            Log.e("Error.Response", e.toString());
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener()
//                {
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
//            protected Map<String, String> getParams()
//            {
//                Map<String, String>  params = new HashMap<String, String>();
//                params.put("parent_id", parent_id);
//                params.put("old_password", strold);
//                params.put("new_password",strnew );
//                params.put("confirm_password",strconfirm);
//                params.put("mobile_token",mobile_token);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//
//    }
//
//    // Alert dialouge
//
//    private void alert() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        alertDialogBuilder.setMessage("Your Session is Expired. Please LoginActivity Again");
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                Intent i = new Intent(getActivity(), LoginActivity.class);
//                startActivity(i);
//                getActivity().finishAffinity();
//
//            }
//        });
//        alertDialogBuilder.setTitle("Alert");
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//    }


}