package br.com.smartpromos.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Paulo on 11/04/2016.
 */
public class GoogleGeocodingAPI {

    public static JSONObject getLocationInfo(double lat, double lng){

        HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng="+ lat+","+lng +"&sensor=true");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            Reader reader = new InputStreamReader(stream, "UTF-8");

            int b;
            while ((b = reader.read()) != -1 ){
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    public static String getCurrentLocationByJSON(double lat, double lng){
        JSONObject jsonObj = getLocationInfo(lat, lng);
        Log.i("JSON String =>", jsonObj.toString());

        String currentLocation = "testing";
        String street_address = null;
        String postal_code = null;

        try {

            String status = jsonObj.getString("status").toString();
            Log.i("status", status);

            if(status.equalsIgnoreCase("OK")){

                JSONArray results = jsonObj.getJSONArray("results");
                int i = 0;
                Log.i("i", i+ "," + results.length() ); //TODO delete this

                do{

                    JSONObject r = results.getJSONObject(i);
                    JSONArray typesArray = r.getJSONArray("types");
                    String types = typesArray.getString(0);

                    if(types.equalsIgnoreCase("street_address")){

                        String[] fullAddred = r.getString("formatted_address").split(",");
                        String[] numberAndNeighborhood = fullAddred[1].split("-");

                        street_address = fullAddred[0]+", "+numberAndNeighborhood[0].trim();
                        Log.i("street_address", street_address);

                        postal_code = numberAndNeighborhood[1].trim()+" - "+fullAddred[3].trim();
                        Log.i("postal_code", postal_code);
                    }

                    if(street_address!=null && postal_code!=null){
                        currentLocation = street_address + "\n" + postal_code;
                        Log.i("Current Location =>", currentLocation); //Delete this
                        i = results.length();
                    }

                    i++;
                }while(i<results.length());

                Log.i("JSON Geo Locatoin =>", currentLocation);
                return currentLocation;

            }


        } catch (JSONException e) {
            Log.e("testing","Failed to load JSON");
            e.printStackTrace();
        }

        return null;
    }

}
