package br.com.smartpromos.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import br.com.smartpromos.R;
import br.com.smartpromos.api.general.response.GmapsLocale;
import br.com.smartpromos.util.GetLocationGmpas;
import br.com.smartpromos.util.GoogleGeocodingAPI;

/**
 * Created by Paulo on 11/04/2016.
 */
public class GoogleGeocodingStabTask extends AsyncTask<String, Void, String> {

    Context context;
    GoogleMap map;
    private MarkerOptions mo;
    double latitude;
    double longitude;

    public GoogleGeocodingStabTask(Context c, GoogleMap map){
        context = c;
        this.map = map;
        mo = new MarkerOptions();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        double lat = 0;
        double lng = 0;

        if(params[0] == "getLocation"){

            String result[] = GetLocationGmpas.getCurrentLocationByJSON(params[1]);

            lat = Double.parseDouble(result[0]);
            lng = Double.parseDouble(result[1]);
        }else{

            lat = Double.parseDouble(params[0]);
            lng = Double.parseDouble(params[1]);
        }

        latitude = lat;
        longitude = lng;

        return GoogleGeocodingAPI.getCurrentLocationByJSON(latitude, longitude);

    }

    @Override
    protected void onPostExecute(String s) {

        String title = "";

        if(s != null){

            GmapsLocale gmapsLocale = new Gson().fromJson(s, GmapsLocale.class);

            title = gmapsLocale.getEndereco()+", "+gmapsLocale.getNumero()+" - "+gmapsLocale.getBairro();

            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            map.animateCamera(update);

            mo.position(latLng);
            mo.title(title)
                    .snippet(title)
                    .visible(true);
            mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_white_48dp));

            map.clear();
            map.addMarker(mo).showInfoWindow();

        }else{
            Toast.makeText(context, "Erro ao carregar localização", Toast.LENGTH_SHORT).show();
        }

        super.onPostExecute(s);
    }

}
