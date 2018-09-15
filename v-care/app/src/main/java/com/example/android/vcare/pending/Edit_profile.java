//package com.example.android.vcare.pending;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.design.widget.TextInputLayout;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.Toast;
//
//import com.android.camera.CropImageIntentBuilder;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.example.android.vcare.AppController;
//import com.example.android.vcare.R;
//import com.example.android.vcare.model.C_Base64;
//import com.example.android.vcare.model.DatabaseHandler;
//import com.example.android.vcare.model.ExifUtils;
//import com.example.android.vcare.model.User_Detail;
//import com.example.android.vcare.model.UserHandler;
//import com.example.android.vcare.phonefield.PhoneField;
//import com.example.android.vcare.ui.login.LoginActivity;
//import com.nobrain.android.permissions.AndroidPermissions;
//import com.nobrain.android.permissions.Checker;
//import com.nobrain.android.permissions.Result;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//public class Edit_profile extends Fragment {
//    UserHandler2 user_handler = new UserHandler2();
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    ProgressDialog pDialog;
//    EditText name,email,phone;
//    Button update;
//    ImageView userimage;
//    LinearLayout add_picture;
//    private TextInputLayout inputLayoutname,inputLayoutemail,inputLayoutmobile;
//    String strname="",stremail="",strmobile="",parent_id="",mobile_token="",base_64="",cuntery_code="",cuntery_name="";
//    public static String status;
//
//    private Bitmap bitmap, bitmapp, bitmap1;
//    private int REQUEST_CODE_PICKER = 2000;
//    Fragment fragment = null;
//    public static final int REQUEST_CODE = 102;
//    private static final int REQUEST_CROP_PICTURE = 3;
//    Uri imageUri;
//    private static final int PICK_Camera_IMAGE = 2;
//    private static final int PICK_IMAGE = 1;
//    CustomPhoneInputLayout  customphone;
//    int cunterycode;
//    public Edit_profile() {
//        // Required empty public constructor
//    }
//
//
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
//        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
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
//        Log.e("parent_id", "editprofile>>"+ parent_id);
//        Log.e("mobiletoken", "editprofile>>"+ mobile_token);
//
//
//        inputLayoutname = (TextInputLayout)rootView.findViewById(R.id.input_layout_name);
//        inputLayoutemail = (TextInputLayout)rootView.findViewById(R.id.input_layout_email);
//        inputLayoutmobile = (TextInputLayout)rootView.findViewById(R.id.input_layout_mobile);
//
//        name  = (EditText)rootView.findViewById(R.id.name);
//        email = (EditText)rootView.findViewById(R.id.email);
//        phone = (EditText)rootView.findViewById(R.id.mobile);
//        update = (Button)rootView.findViewById(R.id.update);
//
//        customphone = (CustomPhoneInputLayout)rootView.findViewById(R.id.custom_phone);
//        customphone.setDefaultCountry("MY");
//
//        add_picture = (LinearLayout)rootView.findViewById(R.id.add_picture);
//        userimage   = (ImageView)rootView.findViewById(R.id.userimage);
//
//        name.addTextChangedListener(new MyTextWatcher(name));
//        email.addTextChangedListener(new MyTextWatcher(email));
//        phone.addTextChangedListener(new MyTextWatcher(phone));
//
//
//        add_picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                AndroidPermissions.check(getActivity()).permissions(android.Manifest.permission.CAMERA,
//                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .hasPermissions(new Checker.Action0() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has " + permissions[0];
//                                popup();
//                            }
//                        })
//                        .noPermissions(new Checker.Action1() {
//                            @Override
//                            public void call(String[] permissions) {
//                                String msg = "Permission has no " + permissions[0];
//
//                                ActivityCompat.requestPermissions(getActivity()
//                                        , new String[]{android.Manifest.permission.CAMERA,
//                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
//                                        , REQUEST_CODE);
//                            }
//                        })
//                        .check();
//
//            }
//        });
//
//
//
//
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Cursor cursor1 = databaseHandler.get_token_detail();
//                if (cursor1 != null){
//                    cursor1.moveToFirst();
//                    for (int j=0; j< cursor1.getCount(); j++){
//                        mobile_token = cursor1.getString(0);
//                        cursor1.moveToNext();
//                    }
//
//                    cursor1.close();
//                }
//                update_profile();
//            }
//        });
//
//        Parent_detail_api();
//
//        return rootView;
//    }
//
//    private void update_profile() {
//
//      /*  if (!validateimage()){
//            return;
//        }
//*/
//        if (!validatename()) {
//            return;
//        }
//
//        if (!validateemail()) {
//            return;
//        }
//
//        if (!validatmobile()) {
//            return;
//        }else {
//
//            Edit_profile_api();
//        }
//
//    }
//
//    private boolean validateimage() {
//        if (base_64.isEmpty()){
//            Toast.makeText(getActivity(), "Please select image", Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//
//        }
//
//        return true;
//    }
//
//
//    private boolean validatename() {
//        strname = name.getText().toString().trim();
//        if (strname.length()==0) {
//            inputLayoutname.setError("Please enter your Full name");
//            requestFocus(name);
//            return false;
//        } else {
//            inputLayoutname.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    private boolean validateemail() {
//         stremail = email.getText().toString().trim();
//
//        if (stremail.isEmpty() || !isValidEmail(stremail)) {
//            inputLayoutemail.setError("Enter valid email address");
//            requestFocus(email);
//            return false;
//        } else {
//            inputLayoutemail.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//
//    private boolean validatmobile() {
//        strmobile = phone.getText().toString().trim();
//
//        cunterycode =  PhoneField.iso_code;
//        cuntery_code = "+"+cunterycode;
//        cuntery_name = PhoneField.code;
//
//        if (strmobile.length()==0) {
//            inputLayoutmobile.setError("Please enter your mobile number");
//            requestFocus(phone);
//            return false;
//        } else {
//            inputLayoutmobile.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//    private static boolean isValidEmail(String email) {
//        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//           getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
//                case R.id.name:
//                    validatename();
//                    break;
//
//                case R.id.email:
//                    validateemail();
//                    break;
//                case R.id.mobile:
//                    validatmobile();
//                    break;
//            }
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
//        AndroidPermissions.result()
//                .addPermissions(REQUEST_CODE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .putActions(REQUEST_CODE, new Result.Action0() {
//                    @Override
//                    public void call() {
//                        popup();
//                    }
//                }, new Result.Action1() {
//                    @Override
//                    public void call(String[] hasPermissions, String[] noPermissions) {
//                    }
//                })
//                .result(requestCode, permissions, grantResults);
//    }
//
//    PopupWindow popup;
//    private void popup() {
//        // TODO Auto-generated method stub
//        try {
//            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View layout = inflater.inflate(R.layout.image_popup, null);
//            Button camera = (Button) layout.findViewById(R.id.camera);
//            Button gallery = (Button) layout.findViewById(R.id.gallery);
//            Button cancel = (Button) layout.findViewById(R.id.cancel);
//            cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popup.dismiss();
//
//                }
//            });
//            camera.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    capture_image();
//                    popup.dismiss();
//                }
//            });
//
//            gallery.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    Get_Image_Gallery();
//                    popup.dismiss();
//                }
//            });
//
//            popup = new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//            popup.update();
//            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//            popup.setFocusable(true);
//            popup.setOutsideTouchable(true);
//            popup.setBackgroundDrawable(new BitmapDrawable());
//            popup.setAnimationStyle(R.style.animationName);
//            popup.setTouchInterceptor(new View.OnTouchListener() {
//
//                @Override
//                public boolean onTouch(View arg0, MotionEvent event) {
//                    // TODO Auto-generated method stub
//                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                        popup.dismiss();
//                        return true;
//                    }
//                    return false;
//
//                }
//            });
//            popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//
//    }
//
//
//    private void capture_image() {
//        // TODO Auto-generated method stub
//        //define the file-name to save photo taken by Camera activity
//        String fileName = "new-photo-name.jpg";
//        //create parameters for Intent with filename
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, fileName);
//        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
//        //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
//        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        //create new Intent
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//        startActivityForResult(intent, PICK_Camera_IMAGE);
//    }
//
//
//    private void Get_Image_Gallery() {
//
//        try {
//            Intent gintent = new Intent();
//            gintent.setType("image/*");
//            gintent.setAction(Intent.ACTION_PICK);
//            startActivityForResult(
//                    Intent.createChooser(gintent, "Select Picture"),
//                    PICK_IMAGE);
//        } catch (Exception e) {
//            Toast.makeText(getActivity(),
//                    e.getMessage(),
//                    Toast.LENGTH_LONG).show();
//            Log.e(e.getClass().getName(), e.getMessage(), e);
//        }
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Uri selectedImageUri = null;
//        String filePath = null;
//        File croppedImageFile = new File(getActivity().getFilesDir(), "test.jpg");
//        switch (requestCode) {
//            case PICK_IMAGE:
//                if (resultCode == Activity.RESULT_OK) {
//                    selectedImageUri = data.getData();
//
//                    // setting the output image file and size to 200x200 pixels square.
//                    Uri croppedImage = Uri.fromFile(croppedImageFile);
//
//                    CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, croppedImage);
//                    cropImage.setOutlineColor(0xFF03A9F4);
//                    cropImage.setSourceImage(data.getData());
//
//                    startActivityForResult(cropImage.getIntent(getActivity()), REQUEST_CROP_PICTURE);
//
//                }
//                break;
//            case PICK_Camera_IMAGE:
//                if (resultCode == getActivity().RESULT_OK) {
//                    // setting the output image file and size to 200x200 pixels square.
//                    Uri croppedImage = Uri.fromFile(croppedImageFile);
//
//                    CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, croppedImage);
//                    cropImage.setOutlineColor(0xFF03A9F4);
//                    cropImage.setSourceImage(imageUri);
//
//                    startActivityForResult(cropImage.getIntent(getActivity()), REQUEST_CROP_PICTURE);
//                } else if (resultCode == getActivity().RESULT_CANCELED) {
//                    Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//
//            case REQUEST_CROP_PICTURE:
//
//
//                if (resultCode == getActivity().RESULT_OK){
//                    String path = croppedImageFile.getAbsolutePath();
//                    decodeFile(path);
//
//                }
//
//                break;
//
//
//            /*if ((requestCode == REQUEST_CROP_PICTURE) && (resultCode == getActivity().RESULT_OK)) {
//                // When we are done cropping, display it in the ImageView.
//                //userimage.setImageBitmap(BitmapFactory.decodeFile(getcroppedImageFile.getAbsolutePath()));
//
//                String path = croppedImageFile.getAbsolutePath();
//
//                decodeFile(path);
//
//
//
//            }*/
//        }
//    }
//
//    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
//        if (cursor != null) {
//            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
//    }
//
//    public void decodeFile(String filePath) {
//        //Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, o);
//
//        // The new size we want to scale to
//        final int REQUIRED_SIZE = 1024;
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
//                break;
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//
//        // Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        bitmap = BitmapFactory.decodeFile(filePath, o2);
//        bitmap1 = ExifUtils.rotateBitmap(filePath, bitmap);
//        // imgView.setImageBitmap(bitmap);
//        Log.e("IMAGE", "img " + bitmap);
//
//        if (bitmap1 == null) {
//            Toast.makeText(getActivity(),
//                    "Please select image", Toast.LENGTH_LONG).show();
//        } else {
//            userimage.setImageBitmap(bitmap1);
//
//
//            BitmapFactory.Options bfo;
//            Bitmap bitmapOrg;
//            ByteArrayOutputStream bao;
//            bfo = new BitmapFactory.Options();
//            bfo.inSampleSize = 2;
//            bao = new ByteArrayOutputStream();
//            bitmap1.compress(Bitmap.CompressFormat.JPEG, 90, bao);
//            byte[] ba = bao.toByteArray();
//            base_64 = C_Base64.encodeBytes(ba);
//            Log.e("base64", "base" + base_64);
//            String img_name = System.currentTimeMillis() + ".jpg";
//        }
//    }
//
//
//
//    private void Parent_detail_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"parentprofile",
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
//
//                                JSONObject object = objJson.getJSONObject("profile");
//
//                                String namee      = object.getString("name");
//                                String emaill     = object.getString("email");
//                                String mobilee    = object.getString("phone_number");
//                                String image      =  object.getString("profile_image");
//                                String country_name  =  object.getString("country_name");
//
//                                customphone.setDefaultCountry(country_name);
//                                name.setText(namee);
//                                email.setText(emaill);
//                                phone.setText(mobilee);
//                                Constant.image = image;
///*
//
//                                Bitmap BitmapImage = null;
//                                try {
//                                    URL url = new URL(image);
//                                     BitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                                } catch(IOException e) {
//                                    System.out.println(e);
//                                }
//*/
//
//                                if (image.isEmpty()){
//
//                                }else {
//                                    Picasso.with(getActivity())
//                                            .load(image)
//                                            .error(R.drawable.header_icon)
//                                            .placeholder(R.drawable.header_icon)
//                                            .into(userimage);
//                                }
//
//                               /* Bitmap resized = Bitmap.createScaledBitmap(BitmapImage, 500, 500, true);
//                                Bitmap conv_bm = getRoundedRectBitmap(resized, 500);
//                                userimage.setImageBitmap(conv_bm);*/
//
//
//                                String mobiletoken = objJson.getString("mobile_token");
//                                databaseHandler.resetTables("token");
//                                databaseHandler.add_token(mobiletoken);
//
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
//                params.put("mobile_token",mobile_token);
//                Log.e("Insertttt", params.toString());
//                return params;
//            }
//        };
//        // for response time increase
//        req.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        req.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(req);
//    }
//
//
//
//
//    private void Edit_profile_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(getActivity());
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"updateparentinfo",
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
//
//                            if (success == 1) {
//                                JSONArray array = objJson.getJSONArray("User");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//                                    String parent_id = jsobj.getString("parent_id");
//                                    String  namee = jsobj.getString("name");
//                                    String   emaill = jsobj.getString("email");
//                                    String phone_number = jsobj.getString("phone_number");
//                                    String otpstatus  =  jsobj.getString("otp_status");
//
//                                    status       = jsobj.getString("is_parent");
//
//                                    String mobiletoken = objJson.getString("mobile_token");
//
//
//                                    databaseHandler.resetTables("riderinfo");
//                                    databaseHandler.Add_Rider(namee, "", emaill, phone_number, otpstatus, parent_id, status, "", "", "");
//
//                                    databaseHandler.resetTables("token");
//                                    databaseHandler.add_token(mobiletoken);
//                                    String text = objJson.getString("text");
//
//                                    Toast.makeText(getActivity(),text, Toast.LENGTH_LONG).show();
//                                    Intent intent = new Intent(getActivity(),MainActivity.class);
//                                    startActivity(intent);
//                                    getActivity().onBackPressed();
//                                }
//
//                            } else if(success == 0) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
//                            }
//                            else if (success==2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
//                            } else if (success==3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
//                            } else if (success==4) {
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
//                params.put("mobile_token", mobile_token);
//                params.put("full_name",strname );
//                params.put("phone_number",strmobile );
//                params.put("email",stremail);
//                params.put("country_code",cuntery_code);
//                params.put("country_name",cuntery_name);
//                params.put("profile_image",base_64);
//
//                Log.e("Insertttt", params.toString());
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
//
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
//
//
//}
