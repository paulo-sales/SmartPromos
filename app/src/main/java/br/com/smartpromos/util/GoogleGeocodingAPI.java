package br.com.smartpromos.util;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

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
import br.com.smartpromos.api.general.response.GmapsLocale;
import br.com.smartpromos.smartpromosapplication.SmartPromosApp;

/**
 * Created by Paulo on 11/04/2016.
 */
public class GoogleGeocodingAPI {

    public static JSONObject getLocationInfo(double lat, double lng){

        HttpGet httpGet = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?key="+SmartPromosApp.context.getResources().getString(R.string.gmaps_id)+"&latlng="+ lat+","+lng +"&sensor=true");
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

        GmapsLocale gmapsLocale = new GmapsLocale();

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

                        JSONArray addressComponents = r.getJSONArray("address_components");

                        JSONObject numero = addressComponents.getJSONObject(0);
                        JSONObject endereco = addressComponents.getJSONObject(1);
                        JSONObject bairro = addressComponents.getJSONObject(2);
                        JSONObject cidade = addressComponents.getJSONObject(3);
                        JSONObject estado = addressComponents.getJSONObject(5);
                        JSONObject pais = addressComponents.getJSONObject(6);
                        JSONObject cep = addressComponents.getJSONObject(7);

                        JSONObject geometry = r.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");

                        gmapsLocale.setEndereco(endereco.getString("long_name"));
                        gmapsLocale.setNumero(numero.getString("long_name"));
                        gmapsLocale.setBairro(bairro.getString("long_name"));
                        gmapsLocale.setCidade(cidade.getString("long_name"));
                        gmapsLocale.setEstado(estado.getString("long_name"));
                        gmapsLocale.setPais(pais.getString("long_name"));

                        int c = 0;
                        try{
                            c = Integer.parseInt(cep.getString("long_name").replace("-",""));
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }

                        gmapsLocale.setCep(c);
                        gmapsLocale.setLat(Double.parseDouble(location.getString("lat")));
                        gmapsLocale.setLng(Double.parseDouble(location.getString("lng")));

                        street_address = new Gson().toJson(gmapsLocale, GmapsLocale.class);

                    }else{

                        JSONObject geometry = r.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");

                        gmapsLocale.setEndereco(r.getString("formatted_address"));

                        gmapsLocale.setLat(Double.parseDouble(location.getString("lat")));
                        gmapsLocale.setLng(Double.parseDouble(location.getString("lng")));

                        street_address = new Gson().toJson(gmapsLocale, GmapsLocale.class);

                    }

                    if(street_address!=null){
                        currentLocation = street_address;
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
