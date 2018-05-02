package com.example.android.vcare.LocationPackage;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukeesh on 8/31/2017.
 */

public class GetAddress {
    public static String Address(Activity activity, Double lat, Double lon) {

        String strAdd = "";
        Geocoder geocoder = new Geocoder(activity);
        Log.d("Geocoder value", geocoder.toString());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                ArrayList<String> addressFragments = new ArrayList<String>();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {

                    addressFragments.add(returnedAddress.getAddressLine(i));
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                String postal = returnedAddress.getPostalCode();

                strAdd = addressFragments.toString().trim();
                Log.w("Current loction postal", "" + strAdd);
                if (postal != null) {
                    strAdd = strAdd.replace(postal, "");
                }

                strAdd = strAdd.replace("[", "");
                strAdd = strAdd.replace("]", "");


                Log.w("My Current loction", "" + strAdd);
            } else {
                Log.w("Current loction address", "No Address returned from Geo Coder");
                strAdd = getReverseLocation(lat, lon);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current loction address", "Exception in Geo Coder");

            strAdd = getReverseLocation(lat, lon);
        }


        return strAdd.trim();


    }

    /**
     * method to Get Address using lat long from Reverse Location API
     *
     * @param lataddress
     * @param lngaddress
     * @return
     */
    public static String getReverseLocation(Double lataddress, Double lngaddress) {
        String address1 = "";
        String address2 = "";
        String city = "";
        String state = "";
        String country = "";
        String county = "";
        String PIN = "";
        String full_address = "";
        String formatted_address = "";
        try {

            JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lataddress + ","
                    + lngaddress + "&sensor=true");
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                formatted_address = zero.getString("formatted_address");
                Log.e("formatted_address", "====>" + formatted_address);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (!TextUtils.isEmpty(long_name) || !TextUtils.isEmpty(long_name) || long_name.length() > 0) {
                        if (Type.equalsIgnoreCase("street_number")) {
                            address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            address1 = address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            city = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            county = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            state = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        }

                    }

                    full_address = address1 + "," + address2 + "," + city + "," + state + "," + country + "," + PIN;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatted_address;

    }

    /**
     * method to Request for Address from Reverse API using Http Client
     *
     * @param url
     * @return
     */
    public static JSONObject getJSONfromURL(String url) {

        // initialize
        InputStream is = null;
        String result = "";
        JSONObject jObject = null;

        // http post
        try {
            org.apache.http.client.HttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
            org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }

        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObject = new JSONObject(result);
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
        }

        return jObject;
    }
}
