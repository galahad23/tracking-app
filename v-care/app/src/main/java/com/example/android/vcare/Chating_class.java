package com.example.android.vcare;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.vcare.model.C_Base64;
import com.example.android.vcare.model.ChatMessage;
import com.example.android.vcare.model.CommonMethods;
import com.example.android.vcare.model.DatabaseHandler;
import com.example.android.vcare.model.ExifUtils;
import com.example.android.vcare.model.FrameLayoutFixed;
import com.example.android.vcare.model.User_Detail;
import com.example.android.vcare.model.User_function;
import com.example.android.vcare.ui.ViewProxy;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nobrain.android.permissions.AndroidPermissions;
import com.nobrain.android.permissions.Checker;
import com.nobrain.android.permissions.Result;
import com.squareup.picasso.Picasso;
import com.yovenny.videocompress.MediaController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import id.zelory.compressor.Compressor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;


public class Chating_class extends AppCompatActivity implements View.OnClickListener {
    public static final int RequestPermissionCode = 1;
    public static final int REQUEST_CODE = 102;
    public static final String APP_DIR = "VideoCompressor";
    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed Videos/";
    public static final String TEMP_DIR = "/Temp/";
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".mp3";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp3";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    // private SwipeRefreshLayout swipeRefreshLayout;
    public static List<ChatMessage> chatlist = new ArrayList<>();
    //    public static ChatAdapter chatAdapter;
    public static ListViewAdapter listViewAdapter;
    final Handler handler = new Handler();
    Toolbar toolbar;
    LinearLayout attechment, camera_image;
    LinearLayout mRevealView;
    boolean hidden = true;
    ImageButton ib_gallery, ib_video, ib_audio, voicebtn, sendbtn;
    View line;
    String base_64 = "", strgroup_id = "", parent_id = "", strgroupname, filepath_voice;
    String strfilepath, audiofile, sostype = "", imagepath = "", audiopath = "", videopath = "";
    EditText textmessage;
    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val;
    String dwnload_file_path = "";
    String file_type = "", type = "";
    TextView toolbartext;
    User_function user_function = new User_function();
    DatabaseHandler databaseHandler;
    ProgressDialog pDialog;
    ListView msgListView;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    FrameLayoutFixed layoutFixed;
    LinearLayout message_ly;
    Uri imageUri;
    String message, outPath;
    String str_chatid = "";
    SparseBooleanArray selected;
    PopupWindow popup;
    private Timer timer = new Timer();
    private List<User_Detail> feeditem;
    private String time;
    private Random random;
    private TextView recordTimeText;
    private View recordPanel;
    private View slideText;
    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = {MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.OutputFormat.THREE_GPP};
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};
    private Bitmap bitmap, bitmap1;
    private ArrayList list_item = new ArrayList<>(); // for holding list item ids
    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            AppLog.logString("Error: " + what + ", " + extra);
        }
    };
    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            AppLog.logString("Warning: " + what + ", " + extra);
        }
    };

    public static int dp(float value) {
        return (int) Math.ceil(1 * value);
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public static void try2CreateCompressDir() {
        File f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + COMPRESSED_VIDEOS_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + TEMP_DIR);
        f.mkdirs();
    }
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatinfg_class);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Constant.group_id = "";
        Intent intent = getIntent();
        strgroupname = intent.getStringExtra("group");
        strgroup_id = intent.getStringExtra("group_id");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        message_ly = (LinearLayout) findViewById(R.id.text_message_layout);
        layoutFixed = (FrameLayoutFixed) findViewById(R.id.record_panel);
        toolbartext = (TextView) findViewById(R.id.toolbar_text);
        voicebtn = (ImageButton) findViewById(R.id.voice_btn);
        sendbtn = (ImageButton) findViewById(R.id.send_btn);
        textmessage = (EditText) findViewById(R.id.textmessage);
        camera_image = (LinearLayout) findViewById(R.id.image_camera);
        mRevealView = (LinearLayout) findViewById(R.id.reveal_items);
        ib_audio = (ImageButton) findViewById(R.id.audio);
        ib_gallery = (ImageButton) findViewById(R.id.gallery);
        ib_video = (ImageButton) findViewById(R.id.video);
        line = (View) findViewById(R.id.attachment_line);
        ib_audio.setOnClickListener(this);
        ib_gallery.setOnClickListener(this);
        ib_video.setOnClickListener(this);
        camera_image.setOnClickListener(this);
        voicebtn.setOnClickListener(this);
        sendbtn.setOnClickListener(this);
        mRevealView.setVisibility(View.INVISIBLE);
        attechment = (LinearLayout) findViewById(R.id.attchment);

        toolbartext.setText(strgroupname);


        databaseHandler = new DatabaseHandler(Chating_class.this);
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
        Log.e("parent_id", "parent_id>>" + parent_id);

        textmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    voicebtn.setVisibility(View.VISIBLE);
                    camera_image.setVisibility(View.VISIBLE);
                    sendbtn.setVisibility(View.GONE);
                } else {
                    voicebtn.setVisibility(View.GONE);
                    camera_image.setVisibility(View.GONE);
                    sendbtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        msgListView = (ListView) findViewById(R.id.msgListView);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(Chating_class.this, chatlist);
        msgListView.setAdapter(listViewAdapter);

        msgListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        msgListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = msgListView.getCheckedItemCount();
                listViewAdapter.toggleSelection(position);
                mode.setTitle(checkedCount + " Selected");
                if (checked) {
                    list_item.add(id);     // Add to list when checked ==  true
                } else {
                    list_item.remove(id);
                }
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        if (Constant.hasConnection(Chating_class.this)) {
                            // Calls getSelectedIds method from ListViewAdapter Class
                            selected = listViewAdapter
                                    .getSelectedIds();
                            // Captures all selected ids with a loop
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    str_chatid += listViewAdapter.getItem(selected.keyAt(i)).msgid + ",";
                                    Log.d("de_item", str_chatid);
                                }

                            }
                            if (str_chatid != null && str_chatid.length() > 0 && str_chatid.charAt(str_chatid.length() - 1) == ',') {
                                str_chatid = str_chatid.substring(0, str_chatid.length() - 1);
                            }

                            Log.d("newde_item", str_chatid);
                            if (Constant.hasConnection(Chating_class.this)) {
                                delete_msg();
                            } else {
                                Toast.makeText(Chating_class.this, "No Internet Connection found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Chating_class.this, "No Internet Connection found.", Toast.LENGTH_SHORT).show();
                        }
                        // Close CAB
                        mode.finish();
                        list_item.clear();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete_action_mode, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                listViewAdapter.removeSelection();

            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });

/*
        msgListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                msgListView.getChildAt(msgListView.getCheckedItemCount()).setBackgroundColor(Color.WHITE);
                chatAdapter.removeSelection();

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.delete_action_mode, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_mode:
                        SparseBooleanArray selected = chatAdapter.getSelectedIds();
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                select_del_msg = String.valueOf(chatAdapter.getItem(selected.keyAt(i)));
                                Toast.makeText(Chating_class.this, select_del_msg, Toast.LENGTH_SHORT).show();
                            }

                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                                  boolean checked) {
                int checkedCount = msgListView.getCheckedItemCount();
                select_del_msg = chatlist.get(position).msgid;

                msgListView.getChildAt(checkedCount).setBackgroundColor(Color.LTGRAY);
                mode.setTitle(checkedCount + " selected");
                Log.d("select_de", select_del_msg);
                chatAdapter.toggleSelection(position);
            }
        });
*/

        attechment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hidden) {

                    // to show the layout when icon is tapped
                    mRevealView.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    hidden = false;
                } else {
                    mRevealView.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
                    hidden = true;

                }

            }
        });

        recordPanel = findViewById(R.id.record_panel);
        recordTimeText = (TextView) findViewById(R.id.recording_time_text);
        slideText = findViewById(R.id.slideText);
        TextView textView = (TextView) findViewById(R.id.slideToCancelTextView);
        textView.setText("Slide To Cancel");

        voicebtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (checkPermission()) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                        message_ly.setVisibility(View.GONE);
                        layoutFixed.setVisibility(View.VISIBLE);
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                                .getLayoutParams();
                        params.leftMargin = dp(30);
                        slideText.setLayoutParams(params);
                        ViewProxy.setAlpha(slideText, 1);
                        startedDraggingX = -1;


                        // startRecording();
                        startrecord();


                        voicebtn.getParent()
                                .requestDisallowInterceptTouchEvent(true);
                        recordPanel.setVisibility(View.VISIBLE);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                            || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                        startedDraggingX = -1;

                        // stop recording(true)
                        stoprecord();


                        message_ly.setVisibility(View.VISIBLE);
                        layoutFixed.setVisibility(View.GONE);

                        // stopRecording(true);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        float x = motionEvent.getX();
                        if (x < -distCanMove) {

                            // stopRecording(false);
                            stoprecord_delete();

                            message_ly.setVisibility(View.VISIBLE);
                            layoutFixed.setVisibility(View.GONE);
                        }
                        x = x + ViewProxy.getX(voicebtn);

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                                .getLayoutParams();
                        if (startedDraggingX != -1) {
                            float dist = (x - startedDraggingX);
                            params.leftMargin = dp(30) + (int) dist;
                            slideText.setLayoutParams(params);
                            float alpha = 1.0f + dist / distCanMove;
                            if (alpha > 1) {
                                alpha = 1;
                            } else if (alpha < 0) {
                                alpha = 0;
                            }
                            ViewProxy.setAlpha(slideText, alpha);
                        }
                        if (x <= ViewProxy.getX(slideText) + slideText.getWidth()
                                + dp(30)) {
                            if (startedDraggingX == -1) {
                                startedDraggingX = x;
                                distCanMove = (recordPanel.getMeasuredWidth()
                                        - slideText.getMeasuredWidth() - dp(48)) / 2.0f;
                                if (distCanMove <= 0) {
                                    distCanMove = dp(80);
                                } else if (distCanMove > dp(80)) {
                                    distCanMove = dp(80);
                                }
                            }
                        }
                        if (params.leftMargin > dp(30)) {
                            params.leftMargin = dp(30);
                            slideText.setLayoutParams(params);
                            ViewProxy.setAlpha(slideText, 1);
                            startedDraggingX = -1;
                        }
                    }
                    view.onTouchEvent(motionEvent);

                    return true;
                } else {

                    requestPermission();
                }
                return false;
            }
        });


        callAsynchronousTask();
    }

    private void callAsynchronousTask() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {


                        All_message();
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 15000);  //Intervel for 15 sec

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]
                {
                        RECORD_AUDIO,
                        READ_EXTERNAL_STORAGE,

                }, RequestPermissionCode);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
        if (str_chatid != null)
            str_chatid = "";

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
    }

    private void startrecord() {
        // TODO Auto-generated method stub

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(getFilename());
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        startTime = SystemClock.uptimeMillis();
        timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1000);
        vibrate();
    }

    private void stoprecord() {
        // TODO Auto-generated method stub


        try {
            recorder.stop();
            recorder.setOutputFile(getFilename1());
        } catch (RuntimeException stopException) {
            //handle cleanup here
            Toast.makeText(getApplicationContext(), "Hold to record,release to send", Toast.LENGTH_SHORT).show();
        }


      /*  if(null != recorder){
            recorder.setOutputFile(getFilename1());
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }*/


        if (timer != null) {
            timer.cancel();
        }
        if (recordTimeText.getText().toString().equals("00:00")) {
            return;
        }
        recordTimeText.setText("00:00");
        vibrate();
    }

    private void stoprecord_delete() {
        // TODO Auto-generated method stub
        if (null != recorder) {
            Toast.makeText(Chating_class.this, "Recording cancel", Toast.LENGTH_SHORT).show();
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }

        if (timer != null) {
            timer.cancel();
        }
        if (recordTimeText.getText().toString().equals("00:00")) {
            return;
        }
        recordTimeText.setText("00:00");
        vibrate();
    }

    private void vibrate() {
        // TODO Auto-generated method stub
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFilename() {
        filepath_voice = Environment.getExternalStorageDirectory().getPath();
        File file_audio = new File(filepath_voice, AUDIO_RECORDER_FOLDER);

        if (!file_audio.exists()) {
            file_audio.mkdirs();
        }
        audiofile = file_audio.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat];
        Log.e("audiofile", "audiofile>>>>" + audiofile);
        return (file_audio.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);

    }

    private String getFilename1() {
        filepath_voice = Environment.getExternalStorageDirectory().getPath();
        File file_audio = new File(filepath_voice, AUDIO_RECORDER_FOLDER);

        time = getCurrentDate() + " " + CommonMethods.getCurrentTime();
        String datee = CommonMethods.getCurrentDate();

        Log.e("aaaa", "aaaa>>>>" + audiofile);

        if (audiofile == null) {

            Toast.makeText(this, "Hold to record,release to send", Toast.LENGTH_SHORT).show();

        } else {

            RequestParams parameter = new RequestParams();
            File file = new File(audiofile);

            try {
                parameter.put("media_file", file);
                parameter.put("parent_id", parent_id);
                parameter.put("group_id", strgroup_id);
                parameter.put("file_type", "Audio");
                parameter.put("chat_time", time);
                parameter.put("is_mine", "1");
                Log.e("file", "file>>>" + file);
                Log.e("parameter", "parameter> " + parameter.toString());
                SignupTask(parameter);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("catchexception", "catchexception> " + e.toString());
            }
        }

        return (file_audio.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio:
                sostype = "";
                sostype = "Audio";
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
               /* Intent intent_upload = new Intent();
                intent_upload.setType("audio*//*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload, 1);*/
                mRevealView.setVisibility(View.INVISIBLE);
                line.setVisibility(View.INVISIBLE);
                hidden = true;
                break;

            case R.id.video:
                sostype = "";
                sostype = "Video";
                Realtime_Permission_for_video();
                mRevealView.setVisibility(View.INVISIBLE);
                line.setVisibility(View.INVISIBLE);
                hidden = true;
                break;
            case R.id.gallery:
                sostype = "";
                sostype = "Image";
                Realtime_Permission_for_gallery();
                mRevealView.setVisibility(View.INVISIBLE);
                line.setVisibility(View.INVISIBLE);

                hidden = true;
                break;
            case R.id.image_camera:
                AndroidPermissions.check(Chating_class.this).permissions(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .hasPermissions(new Checker.Action0() {
                            @Override
                            public void call(String[] permissions) {
                                String msg = "Permission has " + permissions[0];
                                // Toast.makeText(Splash.this, "hasPermissions", Toast.LENGTH_SHORT).show();
                                Popup();
                            }
                        })
                        .noPermissions(new Checker.Action1() {
                            @Override
                            public void call(String[] permissions) {
                                String msg = "Permission has no " + permissions[0];


                                ActivityCompat.requestPermissions(Chating_class.this
                                        , new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                        , REQUEST_CODE);

                            }
                        })
                        .check();

                break;
            case R.id.send_btn:
                sendtextmessage(v);
                //  Toast.makeText(Chating_class.this, "Send button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.voice_btn:
                // Toast.makeText(Chating_class.this, "Voice button", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void Popup() {
        // TODO Auto-generated method stub
        try {
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.image_popup, null);
            Button camera = (Button) layout.findViewById(R.id.camera);
            Button gallery = (Button) layout.findViewById(R.id.gallery);
            Button cancel = (Button) layout.findViewById(R.id.cancel);
            camera.setText("Capture Picture");
            gallery.setText("Capture Video");
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
                    popup.dismiss();
                    sostype = "";
                    sostype = "Image";
                    Realtime_Permission_for_camera();
                }
            });

            gallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popup.dismiss();
                    sostype = "CaptureVideo";
                    Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                    }

                }
            });

            popup = new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            popup.update();
            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            popup.setFocusable(true);
            popup.setOutsideTouchable(true);
            popup.setBackgroundDrawable(new BitmapDrawable());

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

    private void Realtime_Permission_for_camera() {
        AndroidPermissions.check(getApplicationContext()).permissions(android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has " + permissions[0];
                        //Get_Image_Gallery();
                        capture_image();
                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];

                        ActivityCompat.requestPermissions(Chating_class.this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , REQUEST_CODE);
                    }
                })
                .check();
    }

    private void Realtime_Permission_for_gallery() {
        AndroidPermissions.check(getApplicationContext()).permissions(READ_EXTERNAL_STORAGE)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has " + permissions[0];
                        Get_Image_Gallery();
                        // capture_image();
                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];

                        ActivityCompat.requestPermissions(Chating_class.this
                                , new String[]{READ_EXTERNAL_STORAGE}
                                , REQUEST_CODE);
                    }
                })
                .check();
    }

    private void Realtime_Permission_for_video() {
        AndroidPermissions.check(getApplicationContext()).permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has " + permissions[0];
                       /* Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("video*//*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,1);*/

                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 1);

                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];

                        ActivityCompat.requestPermissions(Chating_class.this
                                , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                                , REQUEST_CODE);
                    }
                })
                .check();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        AndroidPermissions.result()
                .addPermissions(REQUEST_CODE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .putActions(REQUEST_CODE, new Result.Action0() {
                    @Override
                    public void call() {
                        Get_Image_Gallery();
                        capture_image();

                        showProgress(dwnload_file_path);
                        new Thread(new Runnable() {
                            public void run() {
                                downloadFile();
                            }
                        }).start();

                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 1);

                    }
                }, new Result.Action1() {
                    @Override
                    public void call(String[] hasPermissions, String[] noPermissions) {
                    }
                })
                .result(requestCode, permissions, grantResults);

        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && ReadContactsPermission) {

                    } else {

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    private void sendtextmessage(View v) {
        message = textmessage.getEditableText().toString();
        time = getCurrentDate() + " " + CommonMethods.getCurrentTime();
        String datee = CommonMethods.getCurrentDate();

        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage("", time,
                    message, "", "", "", "", true, "");
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = CommonMethods.getCurrentDate();
            chatMessage.Time = CommonMethods.getCurrentTime();
            textmessage.setText("");
            send_message();
            listViewAdapter.add(chatMessage);
            listViewAdapter.notifyDataSetChanged();

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
        imageUri = Chating_class.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
                    //  Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    // Toast.makeText(getApplicationContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
            Uri uri = data.getData();
            String pathToStoredVideo = getRealPathFromURIPath(uri, this);
            Log.d("Recorded Video Path ", pathToStoredVideo);
            if (sostype.equals("CaptureVideo")) {
                time = getCurrentDate() + " " + CommonMethods.getCurrentTime();
                try2CreateCompressDir();
                outPath = Environment.getExternalStorageDirectory()
                        + File.separator
                        + APP_DIR
                        + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
                File file = new File(pathToStoredVideo);

                RequestParams parameter = new RequestParams();
                try {
                    parameter.put("media_file", file);
                    parameter.put("parent_id", parent_id);
                    parameter.put("group_id", strgroup_id);
                    parameter.put("file_type", "Video");
                    parameter.put("chat_time", time);
                    parameter.put("is_mine", "1");
                    Log.e("parameter", "parameter> " + parameter.toString());
                    SignupTask(parameter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
                    Toast.makeText(getApplicationContext(), "Unknown path", Toast.LENGTH_LONG).show();
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

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
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
        strfilepath = filePath;
        File file1 = new File(strfilepath);
        time = getCurrentDate() + " " + CommonMethods.getCurrentTime();
        String datee = CommonMethods.getCurrentDate();

        Log.e("strfilepath", "strfilepath>>>" + strfilepath);
        if (sostype.equals("Video")) {

            try2CreateCompressDir();
            outPath = Environment.getExternalStorageDirectory()
                    + File.separator
                    + APP_DIR
                  /*  + COMPRESSED_VIDEOS_DIR*/
                    + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
           /* new VideoCompressor().execute(filePath, outPath);*/
            File file = new File(strfilepath);

            if (strfilepath != null) {
                RequestParams parameter = new RequestParams();
                try {

                    parameter.put("media_file", file);
                    parameter.put("parent_id", parent_id);
                    parameter.put("group_id", strgroup_id);
                    parameter.put("file_type", sostype);
                    parameter.put("chat_time", time);
                    parameter.put("is_mine", "1");
                    Log.e("parameter", "parameter> " + parameter.toString());
                    SignupTask(parameter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (sostype.equals("Audio")) {
            if (strfilepath != null) {
                RequestParams parameter = new RequestParams();
                try {

                    parameter.put("media_file", file1);
                    parameter.put("parent_id", parent_id);
                    parameter.put("group_id", strgroup_id);
                    parameter.put("file_type", sostype);
                    parameter.put("chat_time", time);
                    parameter.put("is_mine", "1");
                    Log.e("parameter", "parameter> " + parameter.toString());
                    SignupTask(parameter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Compressor.getDefault(Chating_class.this)
                    .compressToFileAsObservable(file1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {


                            double time1 = file.length();
                            Log.e("", "time1=====" + time1);

                            if (strfilepath != null) {
                                RequestParams parameter = new RequestParams();
                                try {

                                    parameter.put("media_file", file);
                                    parameter.put("parent_id", parent_id);
                                    parameter.put("group_id", strgroup_id);
                                    parameter.put("file_type", sostype);
                                    parameter.put("chat_time", time);
                                    parameter.put("is_mine", "1");
                                    Log.e("parameter", "parameter> " + parameter.toString());
                                    SignupTask(parameter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }

                            //setCompressedImage();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            //   showError(throwable.getMessage());
                            Log.e("throwable", "===>>>" + throwable.getMessage());
                        }
                    });
        }

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

        if (bitmap1 == null) {
            //  Toast.makeText(getApplicationContext(),"Please select image", Toast.LENGTH_LONG).show();
        } else {
            //  edit_image.setImageBitmap(bitmap1);
            BitmapFactory.Options bfo;
            Bitmap bitmapOrg;
            ByteArrayOutputStream bao;
            bfo = new BitmapFactory.Options();
            bfo.inSampleSize = 2;
            bao = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            base_64 = C_Base64.encodeBytes(ba);
        }
    }

    private void All_message() {
        // TODO Auto-generated method stub

        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "chat_all_message",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {
                                chatlist.clear();
                                // for list scroll auto
                                msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);

                                JSONArray array = objJson.getJSONArray("text");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsobj = array.getJSONObject(i);

                                    String chat_message = jsobj.getString("message");
                                    int ismine = jsobj.getInt("is_mine");
                                    String file_name = jsobj.getString("file_name");
                                    String username = jsobj.getString("name");
                                    String chat_id = jsobj.getString("chat_id");
                                    String media = jsobj.getString("Image_url");
                                    String type = jsobj.getString("file_type");
                                    String user_image = jsobj.getString("profile_image");
                                    String date = jsobj.getString("chat_time");
                                  /*  StringTokenizer time1 = new StringTokenizer(date, " ");
                                    String datee = time1.nextToken();
                                    final String time = time1.nextToken();*/


                                    boolean is_mine;
                                    if (ismine == 1) {
                                        is_mine = true;
                                    } else {
                                        is_mine = false;
                                    }
                                    final ChatMessage chatMessage = new ChatMessage(username, date,
                                            chat_message, type, media, user_image, file_name, is_mine, chat_id);

                                    chatlist.add(chatMessage);
                                }
                                listViewAdapter.notifyDataSetChanged();
                            } else if (success == 0) {
                                user_function.logoutUser(Chating_class.this);
                                alert();
                            } else if (success == 2) {
                                String msg = objJson.getString("text");
                                Toast.makeText(Chating_class.this, msg, Toast.LENGTH_LONG).show();


                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(Chating_class.this, msg, Toast.LENGTH_LONG).show();
                            } else if (success == 4) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chating_class.this);
                                alertDialogBuilder.setMessage("No Record Found");
                                alertDialogBuilder.setCancelable(false);

                                alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                alertDialog = alertDialogBuilder.create();
                                if (alertDialog != null && !alertDialog.isShowing())
                                    alertDialog.show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Log.e("Error.Response", e.toString());
                            e.printStackTrace();
                        }
                        // swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // error
                        Log.e("Error.Response", error.toString());
                        // swipeRefreshLayout.setRefreshing(false);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parent_id", parent_id);
                params.put("group_id", strgroup_id);
                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    private void delete_msg() {
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "delete_chat_message",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // response
                        Log.e("delete_chat_message Res", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            String text = objJson.getString("text");
                            if (success == 1) {
                                for (int i = (selected.size() - 1); i >= 0; i--) {
                                    if (selected.valueAt(i)) {
                                        ChatMessage selecteditem = listViewAdapter.getItem(selected.keyAt(i));
                                        str_chatid += listViewAdapter.getItem(selected.keyAt(i)).msgid + ",";
                                        Log.d("de_item", str_chatid);
                                        listViewAdapter.remove(selecteditem);
                                    }
                                }
                                str_chatid = "";
                                Toast.makeText(Chating_class.this, text, Toast.LENGTH_SHORT).show();
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
                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("chat_id", str_chatid);
                Log.e("Del Request", params.toString());
                return params;
            }
        };

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    private void send_message() {
        // TODO Auto-generated method stub
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "chat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("Responsesearch", response);
                        try {
                            JSONObject objJson = new JSONObject(response);
                            int success = objJson.getInt("success");
                            if (success == 1) {
                                All_message();
                                String msg = objJson.getString("text");
                                //  Toast.makeText(Chating_class.this, msg, Toast.LENGTH_LONG).show();
                                textmessage.setText("");
                            } else if (success == 0) {
                                user_function.logoutUser(Chating_class.this);
                                alert();
                            } else if (success == 2) {
                                String msg = objJson.getString("text");
                                Toast.makeText(Chating_class.this, msg, Toast.LENGTH_LONG).show();


                            } else if (success == 3) {
                                String msg = objJson.getString("text");
                                Toast.makeText(Chating_class.this, msg, Toast.LENGTH_LONG).show();
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

                        // error
                        Log.e("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parent_id", parent_id);
                params.put("group_id", strgroup_id);
                params.put("message", message);
                params.put("chat_time", time);
                params.put("is_mine", "1");
                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    public void SignupTask(RequestParams parameter) {
        pDialog = new ProgressDialog(Chating_class.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(20 * 1000);
        client.post(Config.YOUR_API_URL + "chat_media", parameter, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                pDialog.dismiss();
                All_message();
                Log.e("insert", "statusCode==========" + statusCode);
                String result = new String(responseBody);
                Log.e("response", "response==========" + result);

                if (statusCode == 200) {
                    Toast.makeText(Chating_class.this, "Message Sent Successfully", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.dismiss();
                Log.i("", "statusCode==========" + statusCode + "   " + error.getMessage() + "  " + error.toString() + "   " + error.getCause());
                //  progress.dismiss();
                // progressDialog.dismiss();
                if (statusCode == 404) {
                    Toast.makeText(Chating_class.this, "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(Chating_class.this, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Chating_class.this, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void alert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chating_class.this);
        alertDialogBuilder.setMessage("Your Session is Expired. Please Login Again");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent i = new Intent(Chating_class.this, Login.class);
                startActivity(i);
                finishAffinity();

            }
        });
        alertDialogBuilder.setTitle("Alert");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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
                timer.cancel();
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
        share.putExtra(Intent.EXTRA_TEXT, "I would like to invite you to download this apps for our own circle better communication  https://play.google.com/store/apps?hl=en");

        startActivity(Intent.createChooser(share, "Share via"));
    }

    void downloadFile() {

        try {
            Log.e("downloadFile", "downloadFile>>" + dwnload_file_path);
            Log.e("file_type", "file_type>>" + file_type);
            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Accept-Encoding", "identity");

            //connect
            urlConnection.connect();

            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file

            // File file = new File(SDCardRoot,"downloaded_file.mp4");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + file_type;


            File file = new File(path);

            if (file.exists()) {

                if (type.equals("Audio")) {
                    Uri video = Uri.parse(path);
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    File file1 = new File(path);
                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    Uri video = Uri.parse(path);
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    File file1 = new File(path);
                    intent.setDataAndType(Uri.fromFile(file), "video/*");
                    startActivity(intent);
                    dialog.dismiss();
                }
            } else {

                // showProgress(dwnload_file_path);

                FileOutputStream fileOutput = new FileOutputStream(file);

                //Stream used for reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file which we are downloading
                totalSize = urlConnection.getContentLength();

                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setMax(totalSize);
                    }
                });

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {

                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    // update the progressbar //
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pb.setProgress(downloadedSize);
                            float per = ((float) downloadedSize / totalSize) * 100;
                            cur_val.setText("Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + (int) per + "%)");

                        }
                    });
                }
                //close the output stream when complete //
                fileOutput.close();
                dialog.dismiss();
                downloadedSize = 0;
                totalSize = 0;
                pb.setMax(0);
                pb.setProgress(0);


                /*if (type.equals("Audio")){
                    Uri video = Uri.parse(path);
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    File file1 = new File(path);
                    intent.setDataAndType(Uri.fromFile(file), "audio*//*");
                    startActivity(intent);
                    dialog.dismiss();
                }else if (type.equals("Audio")){
                    Uri video = Uri.parse(path);
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    File file1 = new File(path);
                    intent.setDataAndType(Uri.fromFile(file), "video*//*");
                    startActivity(intent);
                    dialog.dismiss();
                }
*/

                runOnUiThread(new Runnable() {
                    public void run() {
                        // pb.dismiss();// if you want close it..
                    }
                });


            }
        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        } catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
            dialog.dismiss();
            downloadedSize = 0;
            totalSize = 0;
            pb.setMax(0);
            pb.setProgress(0);
        }
    }

    void showError(final String err) {
        runOnUiThread(new Runnable() {
            public void run() {
                dialog.dismiss();
                Toast.makeText(Chating_class.this, err, Toast.LENGTH_LONG).show();
                Log.e("err", "err>>>" + err);
            }
        });
    }


//   Chat adapter

/*
    public class ChatAdapter extends BaseAdapter {

        public List<ChatMessage> chatMessageList = new ArrayList<>();
        private LayoutInflater inflater = null;
        Context context;
        String filepath_voice, audiofile;
        private static final String AUDIO_RECORDER_FOLDER = "DownloadAudio";
        private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".mp3";
        private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp3";
        private MediaRecorder recorder = null;
        private int currentFormat = 0;
        private int output_formats[] = {MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.OutputFormat.THREE_GPP};
        private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};

        // ArrayList&lt;ChatMessage&gt; chatMessageList;


        public ChatAdapter(Activity activity, List<ChatMessage> list) {
            chatMessageList = list;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return chatMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ChatMessage message = (ChatMessage) chatMessageList.get(position);

            View vi = convertView;
            if (convertView == null) {
                vi = inflater.inflate(R.layout.chating_adapter, null);
            }


            LinearLayout layout = (LinearLayout) vi.findViewById(R.id.bubble_layout);
            final LinearLayout parent_layout = (LinearLayout) vi.findViewById(R.id.bubble_layout_parent);
            ImageView userimage_chat = (ImageView) vi.findViewById(R.id.userimage_chat);
            ImageView userimage_chat_right = (ImageView) vi.findViewById(R.id.userimage_chat_right);


            final ImageView imageView = (ImageView) vi.findViewById(R.id.chatimage);
            TextView msg = (TextView) vi.findViewById(R.id.message_text);
            TextView user = (TextView) vi.findViewById(R.id.username);
            TextView time = (TextView) vi.findViewById(R.id.time);
            ImageButton audio = (ImageButton) vi.findViewById(R.id.audio);
            ImageButton video = (ImageButton) vi.findViewById(R.id.video);

            msg.setText(message.body);
            user.setText(message.sender);
            time.setText(message.receiver);

            type = message.image;

            // use for audio or video or image
            final String audiopath = message.audio;
            // use for userimage
            final String userimage = message.Video;

            layout.setClickable(true);
            layout.setFocusable(true);
            parent_layout.setClickable(true);
            parent_layout.setFocusable(true);
            if (userimage.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.header_icon)
                        .error(R.drawable.header_icon)
                        .placeholder(R.drawable.header_icon)
                        .into(userimage_chat);

                Picasso.with(getApplicationContext())
                        .load(R.drawable.header_icon)
                        .error(R.drawable.header_icon)
                        .placeholder(R.drawable.header_icon)
                        .into(userimage_chat_right);

            } else {
                Picasso.with(getApplicationContext())
                        .load(userimage)
                        .error(R.drawable.loadingicon)
                        .placeholder(R.drawable.loadingicon)
                        .into(userimage_chat);

                Picasso.with(getApplicationContext())
                        .load(userimage)
                        .error(R.drawable.loadingicon)
                        .placeholder(R.drawable.loadingicon)
                        .into(userimage_chat_right);

            }

            // if message is mine then align to right
            if (message.isMine) {
                userimage_chat_right.setVisibility(View.VISIBLE);
                userimage_chat.setVisibility(View.GONE);
                user.setVisibility(View.GONE);
                layout.setBackgroundResource(R.drawable.new_right);
                parent_layout.setGravity(Gravity.RIGHT);
                msg.setTextColor(Color.WHITE);

                if (audiopath.isEmpty()) {
                    video.setVisibility(View.GONE);
                    audio.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    // msg.setText(message.body);
                } else if (type.equals("Image")) {
                    imageView.setVisibility(View.VISIBLE);
                    audio.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    //  msg.setVisibility(View.GONE);
                    Picasso.with(context)
                            .load(audiopath)
                            .error(R.drawable.loadingicon)
                            .placeholder(R.drawable.loadingicon)
                            .into(imageView);


                } else if (type.equals("Audio")) {
                    imageView.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    // msg.setVisibility(View.GONE);
                    audio.setVisibility(View.VISIBLE);
                    // audio.setText("Audio file");

                } else if (type.equals("Video")) {
                    imageView.setVisibility(View.GONE);
                    audio.setVisibility(View.GONE);
                    // msg.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);
                    // audio.setText("Video file");

                } else {
                    //msg.setVisibility(View.VISIBLE);
                    // msg.setText(message.body);
                }

            }
            // If not mine then align to left
            else {
                userimage_chat_right.setVisibility(View.GONE);
                userimage_chat.setVisibility(View.VISIBLE);
                user.setVisibility(View.VISIBLE);
                layout.setBackgroundResource(R.drawable.new_bubble1);
                parent_layout.setGravity(Gravity.LEFT);
                msg.setTextColor(Color.parseColor("#e8e8e9"));

                if (audiopath.isEmpty()) {
                    audio.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    // msg.setText(message.body);
                } else if (type.equals("Image")) {
                    audio.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    // msg.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(audiopath)
                            .error(R.drawable.loadingicon)
                            .placeholder(R.drawable.loadingicon)
                            .into(imageView);

                } else if (type.equals("Audio")) {
                    //msg.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    audio.setVisibility(View.VISIBLE);
                    // audio.setText("Audio file");

                } else if (type.equals("Video")) {
                    // msg.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    audio.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);
                    // audio.setText("Video file");

                } else {
                    //  msg.setVisibility(View.VISIBLE);
                    //  msg.setText(message.body);
                }


            }


            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    type = message.image;
                    file_type = message.filetypes;
                    dwnload_file_path = audiopath;

                    // Check Marshmellow Permission on Real time
                    AndroidPermissions.check(Chating_class.this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .hasPermissions(new Checker.Action0() {
                                @Override
                                public void call(String[] permissions) {
                                    String msg = "Permission has " + permissions[0];


                                    showProgress(dwnload_file_path);
                                    new Thread(new Runnable() {
                                        public void run() {
                                            downloadFile();
                                        }
                                    }).start();

                                }
                            })
                            .noPermissions(new Checker.Action1() {
                                @Override
                                public void call(String[] permissions) {
                                    String msg = "Permission has no " + permissions[0];


                                    ActivityCompat.requestPermissions(Chating_class.this
                                            , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                            , REQUEST_CODE);
                                }
                            })
                            .check();


                }
            });


            video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    type = message.image;
                    file_type = message.filetypes;
                    dwnload_file_path = audiopath;
                    // Check Marshmellow Permission on Real time
                    AndroidPermissions.check(Chating_class.this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .hasPermissions(new Checker.Action0() {
                                @Override
                                public void call(String[] permissions) {
                                    String msg = "Permission has " + permissions[0];


                                    showProgress(dwnload_file_path);
                                    new Thread(new Runnable() {
                                        public void run() {
                                            downloadFile();
                                        }
                                    }).start();

                                }
                            })
                            .noPermissions(new Checker.Action1() {
                                @Override
                                public void call(String[] permissions) {
                                    String msg = "Permission has no " + permissions[0];


                                    ActivityCompat.requestPermissions(Chating_class.this
                                            , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                            , REQUEST_CODE);
                                }
                            })
                            .check();
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Zoom_image.class);
                    intent.putExtra("Image_url", audiopath);
                    v.getContext().startActivity(intent);
                }
            });

         */
/*  if (audiopath.isEmpty()){
            audio.setVisibility(View.GONE);
            msg.setText(message.body);
        }else {
            audio.setVisibility(View.VISIBLE);
            audio.setText(audiopath);
            msg.setText(message.body);
        }*//*



            return vi;
        }

        public void add(ChatMessage object) {
            chatMessageList.add(object);
        }
    }
*/

    void showProgress(String file_path) {
        dialog = new Dialog(Chating_class.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setCancelable(false);
        dialog.setTitle("Download Progress");

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file from ... " + file_path);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();

        pb = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            final String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(updatedTime)),
                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(updatedTime)));
            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                    .toMinutes(updatedTime));
            System.out.println(lastsec + " hms " + hms);
            Log.e("lastsec", "lastsec>>>>>" + lastsec);
            Log.e("hms", "hms>>>>>" + hms);


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (recordTimeText != null)
                            recordTimeText.setText(hms);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
        }
    }

    private class VideoCompressor extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Chating_class.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("", "Start video compression");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return MediaController.getInstance().convertVideo(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            // progressBar.setVisibility(View.GONE);
            pDialog.dismiss();
            if (compressed) {
                Log.d("", "Compression successfully!");

                File file = new File(outPath);

                if (strfilepath != null) {
                    RequestParams parameter = new RequestParams();
                    try {

                        parameter.put("media_file", file);
                        parameter.put("parent_id", parent_id);
                        parameter.put("group_id", strgroup_id);
                        parameter.put("file_type", sostype);
                        parameter.put("chat_time", time);
                        parameter.put("is_mine", "1");
                        Log.e("parameter", "parameter> " + parameter.toString());
                        SignupTask(parameter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {

                Toast.makeText(Chating_class.this, "Error occurred please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ListViewAdapter extends ArrayAdapter<ChatMessage> {
        Context context;
        LayoutInflater inflater;
        List<ChatMessage> chatMessageList;
        View vi;
        private SparseBooleanArray mSelectedItemsIds;

        public ListViewAdapter(Context context,
                               List<ChatMessage> worldpopulationlist) {
            super(context, 0, worldpopulationlist);
            mSelectedItemsIds = new SparseBooleanArray();
            this.context = context;
            this.chatMessageList = worldpopulationlist;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ChatMessage message = (ChatMessage) chatMessageList.get(position);
            vi = convertView;
            if (convertView == null) {
                vi = inflater.inflate(R.layout.chating_adapter, null);
            }
            LinearLayout layout = (LinearLayout) vi.findViewById(R.id.bubble_layout);
            final LinearLayout parent_layout = (LinearLayout) vi.findViewById(R.id.bubble_layout_parent);
            ImageView userimage_chat = (ImageView) vi.findViewById(R.id.userimage_chat);
            ImageView userimage_chat_right = (ImageView) vi.findViewById(R.id.userimage_chat_right);


            final ImageView imageView = (ImageView) vi.findViewById(R.id.chatimage);
            TextView msg = (TextView) vi.findViewById(R.id.message_text);
            TextView user = (TextView) vi.findViewById(R.id.username);
            TextView time = (TextView) vi.findViewById(R.id.time);
            ImageView audio = (ImageView) vi.findViewById(R.id.audio);
            ImageView video = (ImageView) vi.findViewById(R.id.video);

            msg.setText(message.body);
            user.setText(message.sender);
            time.setText(message.receiver);

            type = message.image;

            // use for audio or video or image
            final String audiopath = message.audio;
            // use for userimage
            final String userimage = message.Video;


            if (userimage.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.header_icon)
                        .error(R.drawable.header_icon)
                        .placeholder(R.drawable.header_icon)
                        .into(userimage_chat);

                Picasso.with(getApplicationContext())
                        .load(R.drawable.header_icon)
                        .error(R.drawable.header_icon)
                        .placeholder(R.drawable.header_icon)
                        .into(userimage_chat_right);

            } else {
                Picasso.with(getApplicationContext())
                        .load(userimage)
                        .error(R.drawable.loadingicon)
                        .placeholder(R.drawable.loadingicon)
                        .into(userimage_chat);

                Picasso.with(getApplicationContext())
                        .load(userimage)
                        .error(R.drawable.loadingicon)
                        .placeholder(R.drawable.loadingicon)
                        .into(userimage_chat_right);

            }

            // if message is mine then align to right
            if (message.isMine) {
                userimage_chat_right.setVisibility(View.VISIBLE);
                userimage_chat.setVisibility(View.GONE);
                user.setVisibility(View.GONE);
                layout.setBackgroundResource(R.drawable.new_right);
                parent_layout.setGravity(Gravity.RIGHT);
                msg.setTextColor(Color.WHITE);

                if (audiopath.isEmpty()) {
                    video.setVisibility(View.GONE);
                    audio.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    // msg.setText(message.body);
                } else if (type.equals("Image")) {
                    imageView.setVisibility(View.VISIBLE);
                    audio.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    //  msg.setVisibility(View.GONE);
                    Picasso.with(context)
                            .load(audiopath)
                            .error(R.drawable.loadingicon)
                            .placeholder(R.drawable.loadingicon)
                            .into(imageView);


                } else if (type.equals("Audio")) {
                    imageView.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    // msg.setVisibility(View.GONE);
                    audio.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(audiopath)
                            .error(R.drawable.loadingicon)
                            .placeholder(R.drawable.loadingicon)
                            .into(imageView);

                    // audio.setText("Audio file");

                } else if (type.equals("Video")) {
                    imageView.setVisibility(View.GONE);
                    audio.setVisibility(View.GONE);
                    // msg.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);
                    // audio.setText("Video file");

                } else {
                    //msg.setVisibility(View.VISIBLE);
                    // msg.setText(message.body);
                }

            }
            // If not mine then align to left
            else {
                userimage_chat_right.setVisibility(View.GONE);
                userimage_chat.setVisibility(View.VISIBLE);
                user.setVisibility(View.VISIBLE);
                layout.setBackgroundResource(R.drawable.new_bubble1);
                parent_layout.setGravity(Gravity.LEFT);
                msg.setTextColor(Color.parseColor("#e8e8e9"));

                if (audiopath.isEmpty()) {
                    audio.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    // msg.setText(message.body);
                } else if (type.equals("Image")) {
                    audio.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    // msg.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(audiopath)
                            .error(R.drawable.loadingicon)
                            .placeholder(R.drawable.loadingicon)
                            .into(imageView);

                } else if (type.equals("Audio")) {
                    //msg.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                    audio.setVisibility(View.VISIBLE);
                    // audio.setText("Audio file");

                } else if (type.equals("Video")) {
                    // msg.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    audio.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);
                    // audio.setText("Video file");

                } else {
                    //  msg.setVisibility(View.VISIBLE);
                    //  msg.setText(message.body);
                }


            }

            audio.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    msgListView.setItemChecked(position, true);
                    listViewAdapter.toggleSelection(position);
                    selectView(position, true);
                    return false;
                }
            });
            video.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    msgListView.setItemChecked(position, true);
                    listViewAdapter.toggleSelection(position);
                    selectView(position, true);
                    return false;
                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    msgListView.setItemChecked(position, true);
                    listViewAdapter.toggleSelection(position);
                    selectView(position, true);
                    return false;
                }
            });

            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SparseBooleanArray sparseBooleanArray = msgListView.getCheckedItemPositions();
                    if (mSelectedItemsIds.size() > 0) {
                        if (sparseBooleanArray.get(position)) {
                            msgListView.setItemChecked(position, false);
                            listViewAdapter.toggleSelection(position);
                            selectView(position, false);

                        } else {
                            msgListView.setItemChecked(position, true);
                            listViewAdapter.toggleSelection(position);
                            selectView(position, true);
                        }
                    } else {
                        msgListView.setItemChecked(position, false);
                        selectView(position, false);
                        type = message.image;
                        file_type = message.filetypes;
                        dwnload_file_path = audiopath;

                        // Check Marshmellow Permission on Real time
                        AndroidPermissions.check(Chating_class.this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .hasPermissions(new Checker.Action0() {
                                    @Override
                                    public void call(String[] permissions) {
                                        String msg = "Permission has " + permissions[0];


                                        showProgress(dwnload_file_path);
                                        new Thread(new Runnable() {
                                            public void run() {
                                                downloadFile();
                                            }
                                        }).start();

                                    }
                                })
                                .noPermissions(new Checker.Action1() {
                                    @Override
                                    public void call(String[] permissions) {
                                        String msg = "Permission has no " + permissions[0];


                                        ActivityCompat.requestPermissions(Chating_class.this
                                                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                                , REQUEST_CODE);
                                    }
                                })
                                .check();


                    }
                }
            });


            video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SparseBooleanArray sparseBooleanArray = msgListView.getCheckedItemPositions();
                    if (mSelectedItemsIds.size() > 0) {
                        if (sparseBooleanArray.get(position)) {
                            msgListView.setItemChecked(position, false);
                            listViewAdapter.toggleSelection(position);
                            selectView(position, false);

                        } else {
                            msgListView.setItemChecked(position, true);
                            listViewAdapter.toggleSelection(position);
                            selectView(position, true);
                        }
                    } else {
                        msgListView.setItemChecked(position, false);
                        selectView(position, false);
                        type = message.image;
                        file_type = message.filetypes;
                        dwnload_file_path = audiopath;
                        // Check Marshmellow Permission on Real time
                        AndroidPermissions.check(Chating_class.this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .hasPermissions(new Checker.Action0() {
                                    @Override
                                    public void call(String[] permissions) {
                                        String msg = "Permission has " + permissions[0];


                                        showProgress(dwnload_file_path);
                                        new Thread(new Runnable() {
                                            public void run() {
                                                downloadFile();
                                            }
                                        }).start();

                                    }
                                })
                                .noPermissions(new Checker.Action1() {
                                    @Override
                                    public void call(String[] permissions) {
                                        String msg = "Permission has no " + permissions[0];


                                        ActivityCompat.requestPermissions(Chating_class.this
                                                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                                , REQUEST_CODE);
                                    }
                                })
                                .check();
                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SparseBooleanArray sparseBooleanArray = msgListView.getCheckedItemPositions();
                    if (mSelectedItemsIds.size() > 0) {
                        if (sparseBooleanArray.get(position)) {
                            msgListView.setItemChecked(position, false);
                            listViewAdapter.toggleSelection(position);
                            selectView(position, false);

                        } else {
                            msgListView.setItemChecked(position, true);
                            listViewAdapter.toggleSelection(position);
                            selectView(position, true);
                        }
                    } else {
                        msgListView.setItemChecked(position, false);
                        selectView(position, false);
                        Intent intent = new Intent(v.getContext(), Zoom_image.class);
                        intent.putExtra("Image_url", audiopath);
                        v.getContext().startActivity(intent);
                    }
                }
            });
            return vi;
        }

        @Override
        public void remove(ChatMessage object) {
            chatMessageList.remove(object);
            notifyDataSetChanged();
        }


        public void toggleSelection(int position) {

            selectView(position, !mSelectedItemsIds.get(position));

        }


        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }

        public void selectView(int position, boolean value) {
            if (value) {
                mSelectedItemsIds.put(position, true);
            } else {
                mSelectedItemsIds.delete(position);

            }
            notifyDataSetChanged();
        }

        public int getSelectedCount() {
            return mSelectedItemsIds.size();
        }

        public SparseBooleanArray getSelectedIds() {
            return mSelectedItemsIds;
        }
    }

}
