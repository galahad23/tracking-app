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
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
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
//import com.example.android.vcare.model.C_Base64;
//import com.example.android.vcare.model.DatabaseHandler;
//import com.example.android.vcare.model.ExifUtils;
//import com.example.android.vcare.model.Group_detail_list;
//import com.example.android.vcare.model.User_Detail;
//import com.example.android.vcare.model.UserHandler;
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
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Group_detail extends AppCompatActivity {
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    UserHandler2 user_handler = new UserHandler2();
//    ProgressDialog pDialog;
//    Toolbar toolbar;
//    LinearLayout addmember, deletegroup, editgroup, editgroupname_ly, deletegroup_ly, map_ly;
//    LinearLayout editgrouplayout, mainlayout;
//    RelativeLayout imagelayout;
//    ImageView groupimage, edit_image;
//    EditText groupname_edit;
//    TextView group_name, admin;
//    View view_line;
//    Button update;
//    String group_id, groupname, parent_id, member_id, new_groupname, parent_id1, admin_id, onwer = "0";
//    String image;
//    private List<Group_detail_list> helplist = new ArrayList<>();
//    private Group_info_adapter mAdapter;
//    private RecyclerView recyclerView;
//    private Bitmap bitmap, bitmap1;
//    private int REQUEST_CODE_PICKER = 2000;
//    String base_64 = "";
//    String filePath;
//    public static final int REQUEST_CODE = 102;
//    Uri imageUri;
//    private static final int PICK_Camera_IMAGE = 2;
//    private static final int PICK_IMAGE = 1;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_group_detail);
//
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//
//        databaseHandler = new DatabaseHandler(Group_detail.this);
//        feeditem = new ArrayList<User_Detail>();
//        Cursor cursor = databaseHandler.get_rider_detail();
//        if (cursor != null) {
//            cursor.moveToFirst();
//            for (int i = 0; i < cursor.getCount(); i++) {
//                User_Detail detail = new User_Detail();
//                detail.setId(cursor.getString(5));
//                detail.setAddress(cursor.getString(6));
//                feeditem.add(detail);
//                cursor.moveToNext();
//            }
//            cursor.close();
//        }
//
//        for (User_Detail userDetail : feeditem) {
//            parent_id1 = userDetail.getId();
//        }
//
//        final Intent intent = getIntent();
//        group_id = intent.getStringExtra("group_id");
//        parent_id = intent.getStringExtra("parent_id");
//
//        view_line = (View) findViewById(R.id.view);
//        editgroupname_ly = (LinearLayout) findViewById(R.id.edit_groupname);
//        map_ly = (LinearLayout) findViewById(R.id.map_ly);
//        deletegroup_ly = (LinearLayout) findViewById(R.id.buttonly);
//        addmember = (LinearLayout) findViewById(R.id.add_member);
//        deletegroup = (LinearLayout) findViewById(R.id.delete_group);
//        editgroup = (LinearLayout) findViewById(R.id.edit_groupname);
//        groupimage = (ImageView) findViewById(R.id.user_image);
//        group_name = (TextView) findViewById(R.id.groupname);
//        mainlayout = (LinearLayout) findViewById(R.id.hader_layout);
//        editgrouplayout = (LinearLayout) findViewById(R.id.edit_layout);
//        imagelayout = (RelativeLayout) findViewById(R.id.imagly);
//        edit_image = (ImageView) findViewById(R.id.user_image_edit);
//        groupname_edit = (EditText) findViewById(R.id.groupname_edit);
//        admin = (TextView) findViewById(R.id.admin);
//        update = (Button) findViewById(R.id.update_group);
//
//        map_ly.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Constant.plan_price.equals("null")) {
//                    Intent intent = new Intent(getApplicationContext(), Select_plan.class);
//                    startActivity(intent);
//                } else {
//
//                    Intent intent = new Intent(getApplicationContext(), Group_member_map.class);
//                    intent.putExtra("group_id", group_id);
//                    startActivity(intent);
//                }
//            }
//        });
//
//        addmember.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Select_member_group.class);
//                intent.putExtra("group_id", group_id);
//                startActivity(intent);
//            }
//        });
//
//        editgroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainlayout.setVisibility(View.GONE);
//                editgrouplayout.setVisibility(View.VISIBLE);
//
//                groupname_edit.setText(groupname);
//                if (image.isEmpty()) {
//
//                } else {
//                    Picasso.with(Group_detail.this)
//                            .load(image)
//                            .error(R.drawable.blankprofile)
//                            .placeholder(R.drawable.blankprofile)
//                            .into(edit_image);
//                }
//
//
//            }
//        });
//
//        imagelayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                AndroidPermissions.check(getApplicationContext()).permissions(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
//                                ActivityCompat.requestPermissions(Group_detail.this
//                                        , new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
//                                        , REQUEST_CODE);
//                            }
//                        })
//                        .check();
//
//            }
//        });
//
//
//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                new_groupname = groupname_edit.getText().toString();
//
//                if (new_groupname.length() == 0) {
//
//                    groupname_edit.setError("Please enter group name");
//                    groupname_edit.requestFocus();
//                } else {
//                    Group_edit();
//                }
//            }
//        });
//
//
//        deletegroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Delete_alert();
//
//            }
//        });
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
//        mAdapter = new Group_info_adapter(helplist);
//        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Group_detail.this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//
//
//    }
//
//    @Override
//    protected void onResume() {
//        helplist.clear();
//        mAdapter.notifyDataSetChanged();
//
//        Group_info_api();
//        super.onResume();
//    }
//
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
//
//    private void popup() {
//        // TODO Auto-generated method stub
//        try {
//            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
//
//
//        //create parameters for Intent with filename
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, fileName);
//        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
//        //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
//        imageUri = Group_detail.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
//            Toast.makeText(getApplicationContext(),
//                    e.getMessage(),
//                    Toast.LENGTH_LONG).show();
//            Log.e(e.getClass().getName(), e.getMessage(), e);
//        }
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Uri selectedImageUri = null;
//        String filePath = null;
//        switch (requestCode) {
//            case PICK_IMAGE:
//                if (resultCode == Activity.RESULT_OK) {
//                    selectedImageUri = data.getData();
//                }
//                break;
//            case PICK_Camera_IMAGE:
//                if (resultCode == RESULT_OK) {
//                    //use imageUri here to access the image
//                    selectedImageUri = imageUri;
//                         /*Bitmap mPic = (Bitmap) data.getExtras().get("data");
//                        selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*/
//                } else if (resultCode == RESULT_CANCELED) {
//                    Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//
//        if (selectedImageUri != null) {
//            try {
//                // OI FILE Manager
//                String filemanagerstring = selectedImageUri.getPath();
//
//                // MEDIA GALLERY
//                String selectedImagePath = getPath(selectedImageUri);
//
//                if (selectedImagePath != null) {
//                    filePath = selectedImagePath;
//                } else if (filemanagerstring != null) {
//                    filePath = filemanagerstring;
//                } else {
//                    Toast.makeText(getApplicationContext(), "Unknown path",
//                            Toast.LENGTH_LONG).show();
//                    Log.e("Bitmap", "Unknown path");
//                }
//
//                if (filePath != null) {
//                    decodeFile(filePath);
//                } else {
//                    bitmap = null;
//                }
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Internal error",
//                        Toast.LENGTH_LONG).show();
//                Log.e(e.getClass().getName(), e.getMessage(), e);
//            }
//        }
//    }
//
//    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
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
//            Toast.makeText(getApplicationContext(),
//                    "Please select image", Toast.LENGTH_LONG).show();
//        } else {
//            edit_image.setImageBitmap(bitmap1);
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
//    private void Group_info_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Group_detail.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "groupInfo",
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
//                                JSONObject object = objJson.getJSONObject("groupinfo");
//                                groupname = object.getString("group_name");
//                                image = object.getString("image_url");
//                                admin_id = object.getString("parent_id");
//
//                                if (admin_id.equals(parent_id1)) {
//                                    onwer = "1";
//                                }
//
//
//                                JSONArray array = objJson.getJSONArray("group_member_list");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//                                    Group_detail_list item = new Group_detail_list();
//                                    item.setName(jsobj.getString("name"));
//                                    item.setId(jsobj.getString("member_id"));
//                                    item.setMobile(jsobj.getString("request_type"));
//                                    item.setAddress(jsobj.getString("member_type"));
//
//
//                                    helplist.add(item);
//                                }
//                                mAdapter.notifyDataSetChanged();
//
//
//                                //  admin.setText(admin_name);
//
//                                group_name.setText(groupname);
//                                if (image.isEmpty()) {
//
//                                } else {
//                                    Picasso.with(Group_detail.this)
//                                            .load(image)
//                                            .error(R.drawable.blankprofile)
//                                            .placeholder(R.drawable.blankprofile)
//                                            .into(groupimage);
//                                }
//
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(Group_detail.this);
//                                alert();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
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
//                params.put("group_id", group_id);
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
//    private void Group_edit() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Group_detail.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "editgroup",
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
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
//
//                                JSONObject object = objJson.getJSONObject("group_info");
//                                groupname = object.getString("group_name");
//                                image = object.getString("group_image");
//
//
//                                group_name.setText(groupname);
//                                if (image.isEmpty()) {
//
//                                } else {
//                                    Picasso.with(Group_detail.this)
//                                            .load(image)
//                                            .error(R.drawable.blankprofile)
//                                            .placeholder(R.drawable.blankprofile)
//                                            .into(groupimage);
//                                }
//                                /*Picasso.with(holder.mRootView.getContext())
//                                        .load(new File(trackingNumbers.get(position)
//                                                .getImageUrl()))
//                                        .error(R.drawable.no_image_place_holder)
//                                        .noFade().placeholder(R.drawable.no_image_place_holder)
//                                        .into(circularImageView);*/
//
//
//                                mainlayout.setVisibility(View.VISIBLE);
//                                editgrouplayout.setVisibility(View.GONE);
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(Group_detail.this);
//                                alert();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
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
//                params.put("group_id", group_id);
//                params.put("group_name", new_groupname);
//                params.put("group_image", base_64);
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
//    // Alert dialouge
//
//    private void alert() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Group_detail.this);
//        alertDialogBuilder.setMessage("Your Session is Expired. Please LoginActivity Again");
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                Intent i = new Intent(Group_detail.this, LoginActivity.class);
//                startActivity(i);
//                Group_detail.this.finishAffinity();
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
//    private void Group_delete_api() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Group_detail.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "deletegroup",
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
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
//
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
//                                finishAffinity();
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(Group_detail.this);
//                                alert();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
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
//                params.put("group_id", group_id);
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
//    public class Group_info_adapter extends RecyclerView.Adapter<Group_info_adapter.MyViewHolder> {
//
//        private List<Group_detail_list> help_details;
//        Context context;
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            TextView member;
//            TextView request_type, admin;
//            LinearLayout view_layout, delete, adminlayout, view1;
//
//            public MyViewHolder(View view) {
//                super(view);
//
//                member = (TextView) view.findViewById(R.id.checkbox);
//                ic_cross = (LinearLayout) view.findViewById(R.id.ic_cross);
//                view_layout = (LinearLayout) view.findViewById(R.id.view);
//                request_type = (TextView) view.findViewById(R.id.request_type);
//                admin = (TextView) view.findViewById(R.id.admin);
//                adminlayout = (LinearLayout) view.findViewById(R.id.admin_layout);
//                view1 = (LinearLayout) view.findViewById(R.id.view1);
//            }
//        }
//
//        public Group_info_adapter(List<Group_detail_list> helplist) {
//            this.help_details = helplist;
//        }
//
//        @Override
//        public Group_info_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.group_info_adapter, parent, false);
//            context = parent.getContext();
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final Group_info_adapter.MyViewHolder holder, final int position) {
//
//            final Group_detail_list list = help_details.get(position);
//            holder.member.setText(list.getName());
//            String status = list.getMobile();
//            String membertype = list.getaddress();
//
//
//            if (admin_id.equals(list.getId()) && membertype.equals("1")) {
//                if (membertype.equals("1")) {
//                    holder.admin.setVisibility(View.VISIBLE);
//                    holder.adminlayout.setVisibility(View.GONE);
//                    holder.view1.setVisibility(View.GONE);
//                }
//                if (status.equals("0")) {
//                    holder.request_type.setVisibility(View.VISIBLE);
//                    holder.view_layout.setVisibility(View.GONE);
//                    holder.view1.setVisibility(View.GONE);
//                }
//            } else {
//                if (onwer.equals("1")) {
//                    holder.adminlayout.setVisibility(View.VISIBLE);
//                    holder.admin.setVisibility(View.GONE);
//                    holder.view1.setVisibility(View.GONE);
//
//                } else {
//                    if (parent_id1.equals(list.getId())) {
//                        holder.adminlayout.setVisibility(View.VISIBLE);
//                        holder.delete.setVisibility(View.VISIBLE);
//                        holder.view1.setVisibility(View.GONE);
//
//                        holder.admin.setVisibility(View.GONE);
//                        holder.view_layout.setVisibility(View.VISIBLE);
//                        editgroupname_ly.setVisibility(View.GONE);
//                        deletegroup_ly.setVisibility(View.GONE);
//                        view_line.setVisibility(View.GONE);
//                    } else {
//                        holder.admin.setVisibility(View.GONE);
//                        holder.delete.setVisibility(View.GONE);
//
//
//                        holder.view_layout.setVisibility(View.GONE);
//                        editgroupname_ly.setVisibility(View.GONE);
//                        deletegroup_ly.setVisibility(View.GONE);
//                        view_line.setVisibility(View.GONE);
//                        holder.adminlayout.setVisibility(View.GONE);
//                        holder.view1.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//
//            holder.view_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    member_id = list.getId();
//                    View_member_detail();
//
//                }
//            });
//
//            holder.view1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    member_id = list.getId();
//                    View_member_detail();
//
//                }
//            });
//
//            holder.delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    member_id = list.getId();
//                    Delete_member();
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return help_details.size();
//        }
//    }
//
//
//    private void View_member_detail() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Group_detail.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "ViewGroupMember",
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
//                                JSONObject array = objJson.getJSONObject("member_detail");
//
//                                String groupname = array.getString("name");
//                                String latitude = array.getString("latitude");
//                                String longitude = array.getString("longitude");
//                                String profile_image = array.getString("profile_image");
//
//                                Intent intent = new Intent(getApplicationContext(), Member_view.class);
//                                intent.putExtra("groupname", groupname);
//                                intent.putExtra("latitude", latitude);
//                                intent.putExtra("longitude", longitude);
//                                intent.putExtra("profile_image", profile_image);
//                                startActivity(intent);
//
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(Group_detail.this);
//                                alert();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
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
//                params.put("member_id", member_id);
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
//    private void delete_member() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(Group_detail.this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "DeleteGroupMember",
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
//                                String msg = objJson.getString("Delete_member");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
//                               /* helplist.clear();
//                                mAdapter.notifyDataSetChanged();
//                                Group_info_api();*/
//                                finish();
//
//                            } else if (success == 0) {
//                                user_handler.logoutUser(Group_detail.this);
//                                alert();
//                            } else if (success == 2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
//
//                            } else if (success == 3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(Group_detail.this, msg, Toast.LENGTH_LONG).show();
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
//                params.put("member_id", member_id);
//                params.put("group_id", group_id);
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
//    public void Delete_alert() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Group_detail.this);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Alert");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Are you sure, You want to delete group?");
//
//        // On pressing the Settings button.
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Group_delete_api();
//            }
//        });
//
//        // On pressing the cancel button
//        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
//
//
//    public void Delete_member() {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Group_detail.this);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("Alert");
//
//        // Setting Dialog Message
//        alertDialog.setMessage("Are you sure, You want to remove member?");
//
//        // On pressing the Settings button.
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                delete_member();
//            }
//        });
//
//        // On pressing the cancel button
//        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        // Showing Alert Message
//        alertDialog.show();
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
//                if (mainlayout.getVisibility() == View.VISIBLE) {
//                    finish();
//                } else if (editgrouplayout.getVisibility() == View.VISIBLE) {
//                    mainlayout.setVisibility(View.VISIBLE);
//                    editgrouplayout.setVisibility(View.GONE);
//                }
//                return true;
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
//
//}
