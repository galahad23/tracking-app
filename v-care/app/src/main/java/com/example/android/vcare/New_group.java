package com.example.android.vcare;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.android.vcare.model.C_Base64;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.ExifUtils;
import com.example.android.vcare.model.User_Detail;
import com.example.android.vcare.model.User_function;
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;
import com.nobrain.android.permissions.Result;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class New_group extends AppCompatActivity {
    User_function user_function = new User_function();
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    ProgressDialog pDialog;
    Toolbar toolbar;
    Button add_group;
    RelativeLayout imaglayout;
    EditText groupname;
    ImageView userimage;
    String strgroupname,edit_groupname,image;
    String parent_id,mobile_token,base_64="";
    String filePath;
    private Bitmap bitmap, bitmapp, bitmap1;
    private int REQUEST_CODE_PICKER = 2000;
    Fragment fragment = null;
    public static final int REQUEST_CODE = 102;
    Uri imageUri;
    private static final int PICK_Camera_IMAGE = 2;
    private static final int PICK_IMAGE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseHandler = new DatabaseHandler(New_group.this);
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

        Cursor cursor1 = databaseHandler.get_token_detail();
        if (cursor1 != null){
            cursor1.moveToFirst();
            for (int j=0; j< cursor1.getCount(); j++){
                mobile_token = cursor1.getString(0);
                cursor1.moveToNext();
            }
            cursor1.close();
        }

        Log.e("parent_id", "parent_id>>" + parent_id);
        Log.e("mobiletoken", "mobiletokenn>>" + mobile_token);

        add_group = (Button)findViewById(R.id.add_group);
        imaglayout = (RelativeLayout)findViewById(R.id.imagly);
        groupname =  (EditText)findViewById(R.id.groupname);
        userimage =  (ImageView)findViewById(R.id.userimage);



        imaglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AndroidPermissions.check(getApplicationContext()).permissions(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .hasPermissions(new Checker.Action0() {
                            @Override
                            public void call(String[] permissions) {
                                String msg = "Permission has " + permissions[0];
                                popup();
                            }
                        })
                        .noPermissions(new Checker.Action1() {
                            @Override
                            public void call(String[] permissions) {
                                String msg = "Permission has no " + permissions[0];

                                ActivityCompat.requestPermissions(New_group.this
                                        , new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                        , REQUEST_CODE);
                            }
                        })
                        .check();

            }
        });

        add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strgroupname = groupname.getText().toString().trim();

                if (base_64.isEmpty()){
                    //Toast.makeText(New_group.this, "Please select image", Toast.LENGTH_SHORT).show();
                    alert();
                }else if (strgroupname.length()==0){
                    groupname.setError("Please enter group name");
                    groupname.requestFocus();
                }else {
                    Intent intent = new Intent(getApplicationContext(),Select_member.class);
                    intent.putExtra("parent_id",parent_id);
                    intent.putExtra("bsae_64",base_64);
                    intent.putExtra("groupname",strgroupname);
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        AndroidPermissions.result()
                .addPermissions(REQUEST_CODE, android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .putActions(REQUEST_CODE, new Result.Action0() {
                    @Override
                    public void call() {
                        popup();
                    }
                }, new Result.Action1() {
                    @Override
                    public void call(String[] hasPermissions, String[] noPermissions) {
                    }
                })
                .result(requestCode, permissions, grantResults);
    }

    PopupWindow popup;
    private void popup() {
        // TODO Auto-generated method stub
        try {
            LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.image_popup, null);
            Button camera = (Button) layout.findViewById(R.id.camera);
            Button gallery = (Button) layout.findViewById(R.id.gallery);
            Button cancel = (Button) layout.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();

                }
            });
            camera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    capture_image();
                    popup.dismiss();
                }
            });

            gallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Get_Image_Gallery();
                    popup.dismiss();
                }
            });

            popup = new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            popup.update();
            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            popup.setFocusable(true);
            popup.setOutsideTouchable(true);
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setAnimationStyle(R.style.animationName);
            popup.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popup.dismiss();
                        return true;
                    }
                    return false;

                }
            });
            popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


    private void capture_image() {
        // TODO Auto-generated method stub
        //define the file-name to save photo taken by Camera activity
        String fileName = "new-photo-name.jpg";
        //create parameters for Intent with filename
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
        imageUri = New_group.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, PICK_Camera_IMAGE);
    }


    private void Get_Image_Gallery() {

        try {
            Intent gintent = new Intent();
            gintent.setType("image/*");
            gintent.setAction(Intent.ACTION_PICK);
            startActivityForResult(
                    Intent.createChooser(gintent, "Select Picture"),
                    PICK_IMAGE);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == RESULT_OK) {
                    //use imageUri here to access the image
                    selectedImageUri = imageUri;
		 		    	/*Bitmap mPic = (Bitmap) data.getExtras().get("data");
						selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*/
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if (selectedImageUri != null) {
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(getApplicationContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;
        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        bitmap1 = ExifUtils.rotateBitmap(filePath, bitmap);
        // imgView.setImageBitmap(bitmap);
        Log.e("IMAGE", "img " + bitmap);

        if (bitmap1 == null) {
            Toast.makeText(getApplicationContext(),
                    "Please select image", Toast.LENGTH_LONG).show();
        } else {
            userimage.setImageBitmap(bitmap1);
            BitmapFactory.Options bfo;
            Bitmap bitmapOrg;
            ByteArrayOutputStream bao;
            bfo = new BitmapFactory.Options();
            bfo.inSampleSize = 2;
            bao = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            base_64 = C_Base64.encodeBytes(ba);
            Log.e("base64", "base" + base_64);
           String img_name = System.currentTimeMillis() + ".jpg";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.share_app:
                shareTextUrl();
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }
        // Method to share either text or URL.
        private void shareTextUrl() {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
            share.putExtra(Intent.EXTRA_TEXT, "I would like to invite you to download this V-Care Apps, a traceability system and risk management for your love one.  It also provides a closer communication link among your friends, family in the case of emergency. Download : https://www.dropbox.com/current_speed/fdgt6btgjp95ktt/V-Care%20demo%20apps.apk?dl=0");
            startActivity(Intent.createChooser(share, "Share via"));
        }

    private void alert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please take photo or upload image\n" +
                "for your group before entering group name and click on Next button");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Warning !");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

       /* alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }




}
