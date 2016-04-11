package br.com.smartpromos.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import br.com.smartpromos.R;
import br.com.smartpromos.util.GoogleGeocodingAPI;

/**
 * Created by Paulo on 11/04/2016.
 */
public class GoogleGeocodingAPITask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    Context context;
    GoogleMap map;
    double latitude;
    double longitude;

    public GoogleGeocodingAPITask(Context c, GoogleMap map){
        context = c;
        this.map = map;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(context, "Buscando sua localização", "Por favor, aguarde.", true, true);
    }

    @Override
    protected String doInBackground(String... params) {
        latitude = Double.parseDouble(params[0]);
        longitude = Double.parseDouble(params[1]);

        return GoogleGeocodingAPI.getCurrentLocationByJSON(latitude, longitude);
    }

    @Override
    protected void onPostExecute(String s) {

        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        map.animateCamera(update);

        MarkerOptions mo = new MarkerOptions();
        mo.position(latLng);
        mo.title(s).visible(true);
        mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_white_48dp));

        map.clear();
        map.addMarker(mo).showInfoWindow();

        progressDialog.dismiss();

        super.onPostExecute(s);
    }

}
