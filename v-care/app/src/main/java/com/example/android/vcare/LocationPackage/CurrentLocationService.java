package com.example.android.vcare.LocationPackage;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableString;
import android.util.Log;

import com.example.android.vcare.pending.Constant;
import com.example.android.vcare.R;

import java.math.BigDecimal;
import java.util.Locale;

public class CurrentLocationService extends Service implements LocationListener {
    public static Double strlat = 0.0;
    public static Double strlong = 0.0;
    public static boolean isMeetBoth = false;
    SharedPreferences shref_whereyaat;
    SharedPreferences shref_whereyaatTimer;
    SharedPreferences mPref;
    Location location;
    private LocationManager locationManager;
    double old_lat = 0.0, old_long = 0.0;
    public static SpannableString current_speed_service = SpannableString.valueOf("0km/h");

    @Override
    public void onCreate() {
        super.onCreate();


        mPref = this.getSharedPreferences(
                getResources().getString(R.string.pref_name),
                Activity.MODE_PRIVATE);


        shref_whereyaat = this.getSharedPreferences("shref_whereyaat",
                Context.MODE_PRIVATE);
        shref_whereyaatTimer = this.getSharedPreferences(
                "shref_whereyaatTimer", Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("CurrentLocationService", "Service is started");

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                boolean networkIsEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                boolean gpsIsEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                Log.e("Location ", "" + networkIsEnabled);
                Log.e("Location gpsIsEnabled", "" + gpsIsEnabled);

                if (networkIsEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 0, 0, this);

                    if (location != null) {
                        Log.v("lat lastlocation ",
                                "" + location.getLatitude());
                        getAndSetLocation(location);
                    } else {
                        if (gpsIsEnabled) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER, 0, 0, this);

                            if (location != null) {
                                Log.v("lat lastlocation gps",
                                        "" + location.getLatitude());
                                getAndSetLocation(location);
                            }
                        }
                    }
                } else if (gpsIsEnabled) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 0, 0, this);

                    if (location != null) {
                        Log.v("lat lastlocation gps",
                                "" + location.getLatitude());
                        getAndSetLocation(location);
                    }

                }
            } else {
                Log.v("Location Service", "Location Manager is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("onDestroy", "CurrentLocationService called");

        locationManager.removeUpdates(this);
        locationManager = null;

        // stopService(new Intent(CurrentLocationService.this,
        // AutoTerminateConnectionService.class));
        shref_whereyaatTimer.edit().clear().commit();

        SharedPreferences.Editor editor1 = shref_whereyaat.edit();
        editor1.putString("isOnMap", "false");
        editor1.commit();

        stopSelf();
    }

    public String getBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);

        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            return provider;
        } else {
            return LocationManager.NETWORK_PROVIDER;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onLocationChanged(Location point) {
        Log.v("CurrentLocationService",
                "onLocationChanged getAndSetLocation is called");

        getAndSetLocation(point);


        if (old_lat == 0.0 && old_long == 0.0) {
            old_lat = location.getLatitude();
            old_long = location.getLongitude();
        }
        double speedGeyt = getDistance(point.getLatitude(), point.getLongitude(), old_lat, old_long);
        Log.d("speedGeyt", speedGeyt + "");
        speedGeyt = speedGeyt * 3.6;
        BigDecimal bd = new BigDecimal(speedGeyt);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        speedGeyt = bd.doubleValue();
        current_speed_service = SpannableString.valueOf(String.format(Locale.ENGLISH, "%.0f", speedGeyt) + "km/h");
        Log.d("Speed", String.valueOf(current_speed_service));
        old_lat = location.getLatitude();
        old_long = location.getLongitude();
        location = point;

    }

    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6372.8; // for haversine use R = 6372.8 km instead of 6371 km
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private void getAndSetLocation(Location point) {
        try {
            if (point != null) {
                double user_lat = point.getLatitude();
                double user_long = point.getLongitude();
                Log.e("CurrentLocationService ", "user_lat " + user_lat
                        + " user_long " + user_long);

                if (user_lat != 0 && user_long != 0) {
                    if (!shref_whereyaat.getString("user_id", "").equals("")
                            && !shref_whereyaat.getString("user_lat", "")
                            .equals("")
                            && !shref_whereyaat.getString("user_long", "")
                            .equals("")) {
                        Log.v("user_id and user_lat", "is if");
                        if (Double.valueOf(shref_whereyaat.getString(
                                "user_lat", "")) != user_lat
                                || Double.valueOf(shref_whereyaat.getString(
                                "user_long", "")) != user_long) {
                            Log.v("user_id and user_lat", "is if compare");
                            SharedPreferences.Editor editor = shref_whereyaat
                                    .edit();
                            editor.putString("user_lat",
                                    String.valueOf(user_lat));
                            editor.putString("user_long",
                                    String.valueOf(user_long));
                            editor.apply();

                            String[] arr = new String[]{
                                    shref_whereyaat.getString("user_id", ""),
                                    Double.toString(user_lat),
                                    Double.toString(user_long)};
                            setVendorLocation(user_lat, user_long);
                        }

                    } else {
                        Log.v("user_id and user_lat", "is else");
                        SharedPreferences.Editor editor = shref_whereyaat
                                .edit();
                        editor.putString("user_lat", String.valueOf(user_lat));
                        editor.putString("user_long", String.valueOf(user_long));
                        editor.apply();

                        String[] arr = new String[]{
                                shref_whereyaat.getString("user_id", ""),
                                Double.toString(user_lat),
                                Double.toString(user_long)};
                        setVendorLocation(user_lat, user_long);

                    }
                }

            } else {
                Log.e("CurrentLocationService",
                        "onLocationChanged point is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onProviderDisabled(String arg0) {

    }

    @Override
    public void onProviderEnabled(String arg0) {

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

    }

    private void setVendorLocation(final double user_lat, final double user_lng) {

        Constant.LATITUDE = "" + user_lat;
        Constant.LONGITUDE = "" + user_lng;

        strlat = user_lat;
        strlong = user_lng;

        mPref.edit().putString(Constant.LATITUDE, "" + user_lat).commit();
        mPref.edit().putString(Constant.LONGITUDE, "" + user_lng).commit();

        Log.e("user_lat_user_lng", "user_lat_user_lng" + user_lat + "\n" + user_lng);

    }

}
