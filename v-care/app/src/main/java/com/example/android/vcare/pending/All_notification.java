//package com.example.android.vcare.pending;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
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
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
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
//import com.example.android.vcare.model.DatabaseHandler;
//import com.example.android.vcare.model.Group_detail_list;
//import com.example.android.vcare.model.User_Detail;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.StringTokenizer;
//
//public class All_notification extends AppCompatActivity {
//    Toolbar toolbar;
//    DatabaseHandler databaseHandler;
//    private List<User_Detail> feeditem;
//    ProgressDialog pDialog;
//    private List<Group_detail_list> helplist = new ArrayList<>();
//    private Notification_list_adapter mAdapter;
//    private RecyclerView recyclerView;
//    String parent_id="",Url;
//    LinearLayout notification;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all_notification);
//
//        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        databaseHandler = new DatabaseHandler(getApplicationContext());
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
//        Log.e("parent_id", "parent_id>>"+ parent_id);
//
//        notification = (LinearLayout)findViewById(R.id.add_group_layout);
//
//
//        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
//        mAdapter = new Notification_list_adapter(helplist);
//        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//
//
//        All_notificatio();
//
//    }
//
//    private void All_notificatio() {
//        // TODO Auto-generated method stub
//        pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Please Wait...");
//        pDialog.setCancelable(false);
//        pDialog.show();
//        StringRequest req = new StringRequest(Request.Method.POST, Config.YOUR_API_URL+"Child_Notification",
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
//                                JSONArray array = objJson.getJSONArray("Notification_list");
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject jsobj = array.getJSONObject(i);
//                                    Group_detail_list item = new Group_detail_list();
//                                    item.setName(jsobj.getString("notification_type"));
//                                    item.setImage(jsobj.getString("message"));
//                                    item.setId(jsobj.getString("created_date"));
//
//                                    helplist.add(item);
//                                }
//                                mAdapter.notifyDataSetChanged();
//
//
//                            }else if(success == 0) {
//                            }
//                            else if (success==2) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                                notification.setVisibility(View.VISIBLE);
//
//                            } else if (success==3) {
//                                String msg = objJson.getString("text");
//                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
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
//                Log.e("Insertttt", params.toString());
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
//                finish();
//                return true;
//
//            case R.id.share_app:
//                shareTextUrl();
//                return true;
//            default:
//
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
//    // Method to share either text or URL.
//    private void shareTextUrl() {
//        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//        share.setType("text/plain");
//        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//        // Add data to the intent, the receiving app will decide
//        // what to do with it.
//        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
//        share.putExtra(Intent.EXTRA_TEXT, R.string.share_contant);
//        startActivity(Intent.createChooser(share, "Share via"));
//    }
//
//
//    public class Notification_list_adapter extends RecyclerView.Adapter<Notification_list_adapter.MyViewHolder> {
//
//        private List<Group_detail_list> help_details;
//        Context context;
//
//
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            public TextView title,date,time,message;
//
//            public MyViewHolder(View view) {
//                super(view);
//
//                title = (TextView)view.findViewById(R.id.mesaage_title);
//                date = (TextView)view.findViewById(R.id.datee);
//                time = (TextView)view.findViewById(R.id.timee);
//                message = (TextView)view.findViewById(R.id.message);
//            }
//        }
//
//        public Notification_list_adapter(List<Group_detail_list> helplist) {
//            this.help_details = helplist;
//        }
//
//        @Override
//        public Notification_list_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.notification_layout_adapter, parent, false);
//            context = parent.getContext();
//            return new Notification_list_adapter.MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(final Notification_list_adapter.MyViewHolder holder, final int position) {
//
//            final Group_detail_list list = help_details.get(position);
//
//            String datetime = list.getId();
//            StringTokenizer date = new StringTokenizer(datetime, " ");
//            String datee = date.nextToken();
//            final String time = date.nextToken();
//            final String title1 = list.getImage();
//
//            holder.title.setText(list.getName());
//            holder.message.setText(list.getImage());
//            holder.date.setText(datee);
//            holder.time.setText(time);
//
//            holder.message.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    final String title1 = holder.title.getText().toString();
//
//                    if (title1.equals("Image")){
//
//                        Url = list.getImage();
//
//                        Intent intent = new Intent(context, NotificationView.class);
//                        intent.putExtra("url",Url);
//                        intent.putExtra("title",list.getName());
//                        context.startActivity(intent);
//
//                    }else if (title1.equals("Audio")){
//                        Url = list.getImage();
//                        Intent intent = new Intent(context, NotificationView.class);
//                        intent.putExtra("url",Url);
//                        intent.putExtra("title",list.getName());
//                        context.startActivity(intent);
//                    }
//                    else if (title1.equals("Video")){
//                        Url = list.getImage();
//                        Intent intent = new Intent(context, NotificationView.class);
//                        intent.putExtra("url",Url);
//                        intent.putExtra("title",list.getName());
//                        context.startActivity(intent);
//                    }
//
//
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
//   PopupWindow popup;
//    private void Web_View() {
//        // TODO Auto-generated method stub
//        try {
//            LayoutInflater inflater = (LayoutInflater)getBaseContext()
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View layout = inflater.inflate(R.layout.notification_web_view, null);
//
//          WebView  webView = (WebView) findViewById(R.id.webView);
//            WebSettings webSetting = webView.getSettings();
//
//
//
//            webSetting.setJavaScriptEnabled(true);
//            webSetting.setPluginState(WebSettings.PluginState.ON);
//            webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
//            webSetting.setSupportMultipleWindows(true);
//            webSetting.setSupportZoom(true);
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.loadUrl(Url);
//            webView.setWebViewClient(new myWebClient());
//
//
//
//
//
//            popup=new PopupWindow(layout, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//            popup.setFocusable(true);
//            popup.update();
//            popup.setBackgroundDrawable(new BitmapDrawable());
//            popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//            popup.setAnimationStyle(R.style.animationName);
//            popup.setOutsideTouchable(true);
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
//                }
//            });
//            popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//
//    }
//
//   public class myWebClient extends WebViewClient {
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            // TODO Auto-generated method stub
//            super.onPageStarted(view, url, favicon);
//
//            Log.e("url", "url???" + url);
//        }
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            // TODO Auto-generated method stub
//
//            view.loadUrl(url);
//            if (!pDialog.isShowing()) {
//                pDialog.show();
//            }
//            return true;
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            if (pDialog.isShowing()) {
//                pDialog.dismiss();
//            }
//        }
//    }
//
//}
