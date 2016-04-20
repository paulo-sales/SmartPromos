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

import br.com.smartpromos.R;
import br.com.smartpromos.smartpromosapplication.SmartPromosApp;

/**
 * Created by Paulo on 11/04/2016.
 */
public class GetLocationGmpas {

    public static JSONObject getLocationInfo(String addresss){

        Log.i("LINK_SEARCH", "https://maps.googleapis.com/maps/api/geocode/json?key="+SmartPromosApp.context.getResources().getString(R.string.gmaps_id)+"&address="+addresss+"&sensor=true");

        HttpGet httpGet = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?key="+SmartPromosApp.context.getResources().getString(R.string.gmaps_id)+"&address="+addresss+"&sensor=true");
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

    public static String[] getCurrentLocationByJSON(String address){
        JSONObject jsonObj = getLocationInfo(address);
        Log.i("JSON String =>", jsonObj.toString());

        String currentLocation = "testing";
        String latitude = null;
        String longitude = null;

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

                    JSONObject geometry = r.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    latitude = location.getString("lat");
                    longitude = location.getString("lng");


                    if(latitude!=null && longitude!=null){
                        currentLocation = "lat "+latitude+ " Lng "+longitude;
                        Log.i("Current Location =>", currentLocation); //Delete this
                        i = results.length();
                    }

                    i++;
                }while(i<results.length());

                Log.i("JSON Geo Locatoin =>", currentLocation);
                return new String[] {latitude, longitude};

            }


        } catch (JSONException e) {
            Log.e("testing","Failed to load JSON");
            e.printStackTrace();
        }

        return null;
    }

}
