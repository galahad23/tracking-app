package com.example.android.vcare;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.vcare.adapter.ChatAdapter;
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
import com.yovenny.videocompress.MediaController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cz.msebera.android.httpclient.Header;
import id.zelory.compressor.Compressor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

public class Sos extends Fragment implements View.OnClickListener {
    PopupWindow popup;
    User_function user_function = new User_function();
    DatabaseHandler databaseHandler;
    private List<User_Detail> feeditem;
    ProgressDialog pDialog;
    LinearLayout attechment, camera_image, voice;
    LinearLayout mRevealView;
    boolean hidden = true;
    ImageButton ib_gallery, ib_video, ib_audio, voicebtn, sendbtn;
    View line;
    String base_64 = "", message = "", filepath_voice = "", parent_id = "", sostype = "";
    String SOS_TYPE_CHILD_PARENT = "";
    EditText textmessage;

    public static List<ChatMessage> chatlist = new ArrayList<>();
    public static ChatAdapter chatAdapter;
    ListView msgListView;
    private String user1 = "khushi", user2 = "khushi1";
    private Random random;

    private TextView recordTimeText;
    private View recordPanel;
    private View slideText;
    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Timer timer;
    FrameLayoutFixed layoutFixed;
    LinearLayout message_ly;
    String strfilepath, audiofile;

    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".mp3";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp3";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = {MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.OutputFormat.THREE_GPP};
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};


    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private Bitmap bitmap, bitmap1;
    Uri imageUri;
    public static final int REQUEST_CODE = 102;
    String ba1, img_name;
    private static final int PICK_CONTACT = 1000;

    public static final int RequestPermissionCode = 1;

    String outPath;
    public static final String APP_DIR = "VideoCompressor";

    public static final String COMPRESSED_VIDEOS_DIR = "/Compressed Videos/";

    public static final String TEMP_DIR = "/Temp/";

    LinearLayout layoutFirst, layoutSecond, layoutThird;
    TextView personNameFirst, personNumberFirst, personNameSecond,
            personNumberSecond, personNameThird, personNumberThird, callText;
    Button clickHere;
    String contactNumber, calltype = "";
    private Uri uri;
    private String pathToStoredVideo = "";

    public Sos() {
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
        View rootView = inflater.inflate(R.layout.fragment_sos, container, false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        message_ly = (LinearLayout) rootView.findViewById(R.id.text_message_layout);
        layoutFixed = (FrameLayoutFixed) rootView.findViewById(R.id.record_panel);
        voicebtn = (ImageButton) rootView.findViewById(R.id.voice_btn);
        sendbtn = (ImageButton) rootView.findViewById(R.id.send_btn);
        textmessage = (EditText) rootView.findViewById(R.id.textmessage);
        camera_image = (LinearLayout) rootView.findViewById(R.id.image_camera);
        voice = (LinearLayout) rootView.findViewById(R.id.voice);
        mRevealView = (LinearLayout) rootView.findViewById(R.id.reveal_items);
        ib_audio = (ImageButton) rootView.findViewById(R.id.audio);
        ib_gallery = (ImageButton) rootView.findViewById(R.id.gallery);
        ib_video = (ImageButton) rootView.findViewById(R.id.video);
        line = (View) rootView.findViewById(R.id.attachment_line);
        ib_audio.setOnClickListener(this);
        ib_gallery.setOnClickListener(this);
        ib_video.setOnClickListener(this);
        camera_image.setOnClickListener(this);
        sendbtn.setOnClickListener(this);
        mRevealView.setVisibility(View.INVISIBLE);
        attechment = (LinearLayout) rootView.findViewById(R.id.attchment);

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

        msgListView = (ListView) rootView.findViewById(R.id.msgListView);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<>();
        chatAdapter = new ChatAdapter(getActivity(), chatlist);
        msgListView.setAdapter(chatAdapter);


        attechment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cx = (mRevealView.getLeft() + mRevealView.getRight());
                int cy = (mRevealView.getTop());

                // to find  radius when icon is tapped for showing layout
                int startradius = 0;
                int endradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

                // performing circular reveal when icon will be tapped
               /* Animator animator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, startradius, endradius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(400);*/

                //reverse animation
                // to find radius when icon is tapped again for hiding layout
                //  starting radius will be the radius or the extent to which circular reveal animation is to be shown


                int reverse_startradius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());

                //endradius will be zero
                int reverse_endradius = 0;

                // performing circular reveal for reverse animation
                // Animator animate = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, reverse_startradius, reverse_endradius);
                if (hidden) {
                    // to show the layout when icon is tapped
                    mRevealView.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    // animator.start();
                    hidden = false;
                } else {
                    /*mRevealView.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);*/
                    mRevealView.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
                    hidden = true;

                }
            }
        });

        recordPanel = rootView.findViewById(R.id.record_panel);
        recordTimeText = (TextView) rootView.findViewById(R.id.recording_time_text);
        slideText = rootView.findViewById(R.id.slideText);
        TextView textView = (TextView) rootView.findViewById(R.id.slideToCancelTextView);
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

                        message_ly.setVisibility(View.VISIBLE);
                        layoutFixed.setVisibility(View.GONE);

                        // stop recording(true)
                        stoprecord();

                    } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        float x = motionEvent.getX();
                        if (x < -distCanMove) {

                            message_ly.setVisibility(View.VISIBLE);
                            layoutFixed.setVisibility(View.GONE);
                            // recording_cancel(True)
                            stoprecord_delete();
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


        EmergencyContactPopup();

        return rootView;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        RECORD_AUDIO,
                        READ_EXTERNAL_STORAGE,

                }, RequestPermissionCode);

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
            Toast.makeText(getActivity(), "Hold to record,release to send", Toast.LENGTH_SHORT).show();
        }

        /*if(null != recorder){

            try {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder.setOutputFile(getFilename1());
                recorder = null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }


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
            Toast.makeText(getActivity(), "Recording cancel", Toast.LENGTH_SHORT).show();
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
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dp(float value) {
        return (int) Math.ceil(1 * value);
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


            getActivity().runOnUiThread(new Runnable() {

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


    private String getFilename() {
        filepath_voice = Environment.getExternalStorageDirectory().getPath();
        File file_audio = new File(filepath_voice, AUDIO_RECORDER_FOLDER);

        if (!file_audio.exists()) {
            file_audio.mkdirs();
        }
        audiofile = file_audio.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat];
        return (file_audio.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);

    }

    private String getFilename1() {
        filepath_voice = Environment.getExternalStorageDirectory().getPath();
        File file_audio = new File(filepath_voice, AUDIO_RECORDER_FOLDER);
        Log.e("aaaa", "aaaa>>>>" + audiofile);
        RequestParams parameter = new RequestParams();
        File file = new File(audiofile);

        try {
            parameter.put("ImageName", file);
            parameter.put("parent_id", parent_id);
            parameter.put("sos_type_parent_child", SOS_TYPE_CHILD_PARENT);
            parameter.put("sos_type", "Audio");
            Log.e("file", "file>>>" + file);
            Log.e("parameter", "parameter> " + parameter.toString());
            SignupTask(parameter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("catchexception", "catchexception> " + e.toString());
        }


        return (file_audio.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);

    }


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio:
                sostype = "";
                sostype = "Audio";
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload, 1);
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
                AndroidPermissions.check(getActivity()).permissions(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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


                                ActivityCompat.requestPermissions(getActivity()
                                        , new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                        , REQUEST_CODE);

                            }
                        })
                        .check();


                // capture_image();
                break;
            case R.id.send_btn:
                sendtextmessage(v);
                break;
        }
    }

    private void Popup() {
        // TODO Auto-generated method stub
        try {
            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    if (videoCaptureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
        AndroidPermissions.check(getActivity()).permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

                        ActivityCompat.requestPermissions(getActivity()
                                , new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , REQUEST_CODE);
                    }
                })
                .check();
    }

    private void Realtime_Permission_for_gallery() {
        AndroidPermissions.check(getActivity()).permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
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

                        ActivityCompat.requestPermissions(getActivity()
                                , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                                , REQUEST_CODE);
                    }
                })
                .check();
    }

    private void Realtime_Permission_for_video() {
        AndroidPermissions.check(getActivity()).permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has " + permissions[0];
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 1);
                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];

                        ActivityCompat.requestPermissions(getActivity()
                                , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                                , REQUEST_CODE);
                    }
                })
                .check();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, final String[] permissions, int[] grantResults) {
        AndroidPermissions.result()
                .addPermissions(REQUEST_CODE, Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE)
                .putActions(REQUEST_CODE, new Result.Action0() {
                    @Override
                    public void call() {
                        Get_Image_Gallery();
                        capture_image();
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);

                        if (calltype.equals("phonecall")) {

                            call();
                        }
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

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getActivity(), RECORD_AUDIO);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);


        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    private void sendtextmessage(View v) {
        message = textmessage.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage("", "",
                    message, "", "", "", "", true,"");
            chatMessage.setMsgID();
            chatMessage.body = message;
            chatMessage.Date = CommonMethods.getCurrentDate();
            chatMessage.Time = CommonMethods.getCurrentTime();
            send_notification_to_child();
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
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
            startActivityForResult(Intent.createChooser(gintent, "Select Picture"), PICK_IMAGE);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == getActivity().RESULT_OK) {
                    //use imageUri here to access the image
                    selectedImageUri = imageUri;
                         /*Bitmap mPic = (Bitmap) data.getExtras().get("data");
                        selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*/
                } else if (resultCode == getActivity().RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
            uri = data.getData();
            pathToStoredVideo = getRealPathFromURIPath(uri, getActivity());
            Log.d("Recorded Video Path ", pathToStoredVideo);
            if (sostype.equals("CaptureVideo")) {

                try2CreateCompressDir();
                outPath = Environment.getExternalStorageDirectory()
                        + File.separator
                        + APP_DIR
                        + "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
                File file = new File(pathToStoredVideo);
                sostype = "Video";
                if (pathToStoredVideo != null) {
                    RequestParams parameter = new RequestParams();
                    try {

                        parameter.put("ImageName", file);
                        parameter.put("parent_id", parent_id);
                        parameter.put("sos_type_parent_child", SOS_TYPE_CHILD_PARENT);
                        parameter.put("sos_type", sostype);
                        Log.e("parameter", "parameter> " + parameter.toString());
                        SignupTask(parameter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    Toast.makeText(getActivity(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
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
        Log.e("strfilepath", "strfilepath>>>" + strfilepath);
        File file1 = new File(strfilepath);
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

                    parameter.put("ImageName", file);
                    parameter.put("parent_id", parent_id);
                    parameter.put("sos_type_parent_child", SOS_TYPE_CHILD_PARENT);
                    parameter.put("sos_type", sostype);
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

                    parameter.put("ImageName", file1);
                    parameter.put("parent_id", parent_id);
                    parameter.put("sos_type_parent_child", SOS_TYPE_CHILD_PARENT);
                    parameter.put("sos_type", sostype);
                    Log.e("parameter", "parameter> " + parameter.toString());
                    SignupTask(parameter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {


            Compressor.getDefault(getActivity())
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

                                    parameter.put("ImageName", file);
                                    parameter.put("parent_id", parent_id);
                                    parameter.put("sos_type_parent_child", SOS_TYPE_CHILD_PARENT);
                                    parameter.put("sos_type", sostype);
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
        // imgView.setImageBitmap(bitmap);
        Log.e("IMAGE", "img " + bitmap);

        if (bitmap1 == null) {
            //Toast.makeText(getActivity(),"Please select image", Toast.LENGTH_LONG).show();
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
            // Log.e("base64", "base" + base_64);
            String img_name = System.currentTimeMillis() + ".jpg";

        }
    }

    public static void try2CreateCompressDir() {
        File f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + COMPRESSED_VIDEOS_DIR);
        f.mkdirs();
        f = new File(Environment.getExternalStorageDirectory(), File.separator + APP_DIR + TEMP_DIR);
        f.mkdirs();
    }

    class VideoCompressor extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   progressBar.setVisibility(View.VISIBLE);
            pDialog = new ProgressDialog(getActivity());
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

                //  zip(strArray,strfilepath);

                if (strfilepath != null) {
                    RequestParams parameter = new RequestParams();
                    try {

                        parameter.put("ImageName", file);
                        parameter.put("parent_id", parent_id);
                        parameter.put("sos_type_parent_child", SOS_TYPE_CHILD_PARENT);
                        parameter.put("sos_type", sostype);
                        Log.e("parameter", "parameter> " + parameter.toString());
                        SignupTask(parameter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {

                Toast.makeText(getActivity(), "Error occurred please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void send_notification_to_child() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL + "send_notification_to_child",
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
                                textmessage.setText("");
                            } else if (success == 0) {
                                user_function.logoutUser(getActivity());
                                alert();
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
                Map<String, String> params = new HashMap
                        <String, String>();
                params.put("parent_id", parent_id);
                params.put("sos_type_parent_child", SOS_TYPE_CHILD_PARENT);
                params.put("message", message);
                Log.e("Insertttt", params.toString());
                return params;
            }
        };

        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);

    }

    private void alert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Your Session is Expired. Please Login Again");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
                getActivity().finishAffinity();

            }
        });
        alertDialogBuilder.setTitle("Alert");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    public void SignupTask(RequestParams parameter) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50 * 1000);
        client.post(Config.YOUR_API_URL + "SOS_Audio_send_notification", parameter, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                pDialog.dismiss();
                Log.e("insert", "statusCode==========" + statusCode);
                String result = new String(responseBody);
                Log.e("response", "response==========" + result);

                if (statusCode == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String text = jsonObject.getString("text");
                        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.dismiss();
                Log.i("", "statusCode==========" + statusCode + "   " + error.getMessage() + "  " + error.toString() + "   " + error.getCause());
                //  progress.dismiss();
                // progressDialog.dismiss();
                if (statusCode == 404) {
                    Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getActivity(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {


                    Toast.makeText(getActivity(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void SOS_POPU_FOR_CHILD_OR_PARENT() {
        // TODO Auto-generated method stub
        try {
            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.sos_child_parent_popup, null);

            Button submit;
            final RadioButton soschild, sosparent;
            final RadioGroup radioGroup;

            ImageView cancel = (ImageView) layout.findViewById(R.id.cancel);
            submit = (Button) layout.findViewById(R.id.select_plan);
            soschild = (RadioButton) layout.findViewById(R.id.paypal);
            sosparent = (RadioButton) layout.findViewById(R.id.smoovpay);
            radioGroup = (RadioGroup) layout.findViewById(R.id.radiogroup);


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finishAffinity();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getContext(), "Please Select One To You Want Send A SOS ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (soschild.isChecked()) {
                            SOS_TYPE_CHILD_PARENT = "child";
                            popup.dismiss();
                        } else {
                            SOS_TYPE_CHILD_PARENT = "parent";
                            popup.dismiss();
                        }
                    }
                }
            });
            popup = new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            popup.setFocusable(true);
            popup.update();
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            popup.setAnimationStyle(R.style.animationName);
            popup.setOutsideTouchable(true);
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

    public void zip(String[] _files, String zipFileName) {

        int BUFFER = 2048;

        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void EmergencyContactPopup() {
        // TODO Auto-generated method stub
        try {
            LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.emergencycontactpopup, null);
            ImageView cancel = (ImageView) layout.findViewById(R.id.cancel);

            layoutFirst = (LinearLayout) layout.findViewById(R.id.layoutFirst);
            layoutSecond = (LinearLayout) layout.findViewById(R.id.layoutSecond);
            layoutThird = (LinearLayout) layout.findViewById(R.id.layoutThird);

            personNameFirst = (TextView) layout.findViewById(R.id.personNameFirst);
            personNumberFirst = (TextView) layout.findViewById(R.id.personNumberFirst);
            personNameSecond = (TextView) layout.findViewById(R.id.personNameSecond);
            personNumberSecond = (TextView) layout.findViewById(R.id.personNumberSecond);
            personNameThird = (TextView) layout.findViewById(R.id.personNameThird);
            personNumberThird = (TextView) layout.findViewById(R.id.personNumberThird);

            callText = (TextView) layout.findViewById(R.id.callText);

            personNumberFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactNumber = personNumberFirst.getText().toString();
                    calltype = "phonecall";
                    CheckPermission();
                }
            });

            personNumberSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactNumber = personNumberSecond.getText().toString();
                    calltype = "phonecall";
                    CheckPermission();
                }
            });

            personNumberThird.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactNumber = personNumberThird.getText().toString();
                    calltype = "phonecall";
                    CheckPermission();
                }
            });

            clickHere = (Button) layout.findViewById(R.id.clickHere);
            clickHere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();

                    // SOS_POPU_FOR_CHILD_OR_PARENT();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finishAffinity();
                }
            });

            Button submit;
            final RadioButton soschild, sosparent;
            final RadioGroup radioGroup;

            submit = (Button) layout.findViewById(R.id.select_plan);
            soschild = (RadioButton) layout.findViewById(R.id.paypal);
            sosparent = (RadioButton) layout.findViewById(R.id.smoovpay);
            radioGroup = (RadioGroup) layout.findViewById(R.id.radiogroup);


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getContext(), "Please Select One To You Want Send A SOS ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (soschild.isChecked()) {
                            SOS_TYPE_CHILD_PARENT = "child";
                            popup.dismiss();
                        } else {
                            SOS_TYPE_CHILD_PARENT = "parent";
                            popup.dismiss();
                        }
                    }
                }
            });


            Get_Emergency_Number();


            popup = new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            popup.setFocusable(true);
            popup.update();
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            popup.setAnimationStyle(R.style.animationName);
            popup.setOutsideTouchable(true);
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

    private void CheckPermission() {
        // Check Marshmellow Permission on Real time
        AndroidPermissions.check(getActivity()).permissions(Manifest.permission.CALL_PHONE)
                .hasPermissions(new Checker.Action0() {
                    @Override
                    public void call(String[] permissions) {
                        // get phone contact number
                        Call();
                    }
                })
                .noPermissions(new Checker.Action1() {
                    @Override
                    public void call(String[] permissions) {
                        String msg = "Permission has no " + permissions[0];

                        ActivityCompat.requestPermissions(getActivity()
                                , new String[]{Manifest.permission.CALL_PHONE}
                                , REQUEST_CODE);
                    }
                })
                .check();

    }


    private void Call() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + contactNumber));
        startActivity(callIntent);
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

                                if (array.length() == 0) {
                                    callText.setText("Emergency details empty, either to text message or add emergency contacts.");

                                } else {

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsobj = array.getJSONObject(i);

                                        String person_name = jsobj.getString("person_name");
                                        String emergency_number = jsobj.getString("emergency_number");

                                        if (i == 0) {
                                            layoutFirst.setVisibility(View.VISIBLE);
                                            personNumberFirst.setText(emergency_number);
                                            personNameFirst.setText(person_name);

                                            if (emergency_number.isEmpty()) {
                                                layoutFirst.setVisibility(View.GONE);
                                            }

                                        } else if (i == 1) {
                                            layoutSecond.setVisibility(View.VISIBLE);
                                            personNumberSecond.setText(emergency_number);
                                            personNameSecond.setText(person_name);
                                            if (emergency_number.isEmpty()) {
                                                layoutSecond.setVisibility(View.GONE);
                                            }
                                        } else if (i == 2) {
                                            layoutThird.setVisibility(View.VISIBLE);
                                            personNumberThird.setText(emergency_number);
                                            personNameThird.setText(person_name);
                                            if (emergency_number.isEmpty()) {
                                                layoutThird.setVisibility(View.GONE);
                                            }
                                        }


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

}
