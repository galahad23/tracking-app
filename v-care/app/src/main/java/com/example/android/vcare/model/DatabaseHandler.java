package com.example.android.vcare.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.android.vcare.BuildConfig;


/**
 * Created by Mtoag on 7/8/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static String LOGCAT;
    public SQLiteDatabase sqliteDB;

    public DatabaseHandler(Context applicationcontext) {
        super(applicationcontext, "V_Care.db", null, 2);
        Log.e(LOGCAT, "Create database");
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS riderinfo(Id INTEGER PRIMARY KEY,firstname TEXT,secondname TEXT, email TEXT, mobile TEXT,address TEXT,driver_id TEXT,pincode TEXT,city TEXT,state TEXT, countary TEXT)");
        database.execSQL("CREATE TABLE IF NOT EXISTS token(Id INTEGER PRIMARY KEY,tokenid TEXT)");
        database.execSQL("CREATE TABLE IF NOT EXISTS Contact(Id INTEGER PRIMARY KEY,tokenid TEXT)");
        database.execSQL("CREATE TABLE IF NOT EXISTS Multiple_zone(Id INTEGER PRIMARY KEY,item_id TEXT,startdate TEXT,enddate TEXT, zone TEXT, " +
                "starttime TEXT,endtime TEXT,name TEXT,email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS riderinfo");
        database.execSQL("DROP TABLE IF EXISTS token");
        database.execSQL("DROP TABLE IF EXISTS Contact");
        database.execSQL("DROP TABLE IF EXISTS Multiple_zone");
        onCreate(database);
    }

    public void Add_Rider(String firstname, String secondname, String email, String mobile, String address,
                          String driver_id, String pincode, String city, String state, String countary) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstname", firstname);
        values.put("secondname", secondname);
        values.put("email", email);
        values.put("mobile", mobile);
        values.put("address", address);
        values.put("driver_id", driver_id);
        values.put("pincode", pincode);
        values.put("city", city);
        values.put("state", state);
        values.put("countary", countary);
        Log.e("List Itemmmmm", values.toString());
        db.insert("riderinfo", null, values);
        db.close();
    }





    public void add_token(String tokenid){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tokenid", tokenid);

        Log.e("TOKENNN", values.toString());
        db.insert("token", null, values);
        db.close();
    }

    public void add_contact(String contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("contact", contact);

        Log.e("CONTACT", values.toString());
        db.insert("contact", null, values);
        db.close();
    }

    public void add_zone(String item_id,  String startdate,String enddate,String zone,String starttime,String endtime,String name,String email){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_id", item_id);
        values.put("startdate", startdate);
        values.put("enddate", enddate);
        values.put("zone", zone);
        values.put("starttime", starttime);
        values.put("endtime", endtime);
        values.put("name", name);
        values.put("email", email);

        Log.e("Multiple_zone", values.toString());
        db.insert("Multiple_zone", null, values);
        db.close();
    }


    public Cursor get_rider_detail(){
        Cursor cursor = getWritableDatabase().query("riderinfo", new String[]{"firstname", "secondname", "email", "mobile", "address", "driver_id", "pincode", "city", "state", "countary"}, BuildConfig.FLAVOR, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }


    public Cursor get_token_detail(){
        Cursor cursor = getWritableDatabase().query("token", new String[]{"tokenid"}, BuildConfig.FLAVOR,null,null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor get_contact_detail(){
        Cursor cursor = getWritableDatabase().query("Contact", new String[]{"contact"}, BuildConfig.FLAVOR,null,null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

    public void resetTables(String tablename) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(tablename, null, null);

    }

    public Cursor get_zone_detail(){
        Cursor cursor = getWritableDatabase().query("Multiple_zone", new String[]{"item_id","startdate", "enddate", "zone", "starttime", "endtime", "name", "email"}, BuildConfig.FLAVOR, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }


    public int getRowCount() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT  * FROM riderinfo", null);
        int rowCount = cursor.getCount();
        database.close();
        cursor.close();
        return rowCount;
    }

    public void resetTable_zone() {

        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("Multiple_zone", null, null);

    }

    public void update_Claim_List(String item_id,String startdate,String enddate,String zone,String starttime,String endtime,String name,String email){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_id", item_id);
        values.put("startdate", startdate);
        values.put("enddate", enddate);
        values.put("zone", zone);
        values.put("starttime", starttime);
        values.put("endtime", endtime);
        values.put("name", name);
        values.put("email", email);
        Log.e("Listupdate>>>", values.toString());
        db.update("Multiple_zone", values, "item_id" + "=" + item_id, null);
    }

    public void deletecart(String id) {
        SQLiteDatabase database = this.getWritableDatabase();


        database.execSQL("delete from Multiple_zone where item_id='"+id+"'");

    }

    public boolean CheckIsDataAlreadyInDBorNot_Claim(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from Multiple_zone where " + "item_id"
                + "=" + "'" + id + "'";
        Cursor cursor = db.rawQuery(Query, null); // add the String your
        // searching by here
        Log.e("proid", "i"+id);
        boolean hasObject = false;
        if (cursor.getCount() > 0) {
            hasObject = true;
            cursor.close(); // Don't forget to close your cursor
        }

        cursor.close();
        return hasObject;
    }

    public void deleteallcartitem() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("Multiple_zone", null, null);
    }
    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + "Multiple_zone";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public void open() {
        this.sqliteDB = getWritableDatabase();
        this.sqliteDB = getReadableDatabase();
    }

    public void close() {
        if (this.sqliteDB != null) {
            this.sqliteDB.close();
        }
    }

}
