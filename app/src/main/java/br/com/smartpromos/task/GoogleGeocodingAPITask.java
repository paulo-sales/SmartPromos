package br.com.smartpromos.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONObject;

import br.com.smartpromos.R;
import br.com.smartpromos.api.general.response.GmapsLocale;
import br.com.smartpromos.util.GetLocationGmpas;
import br.com.smartpromos.util.GoogleGeocodingAPI;

/**
 * Created by Paulo on 11/04/2016.
 */
public class GoogleGeocodingAPITask extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private View view;
    Context context;
    GoogleMap map;
    double latitude;
    double longitude;

    public GoogleGeocodingAPITask(Context c, GoogleMap map, View view){
        context = c;
        this.map = map;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(context, "Buscando sua localização", "Por favor, aguarde.", true, true);
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

        String title = "Minha localização";

        if(s != null){

            EditText edtCep = (EditText) view.findViewById(R.id.edtCep);
            EditText edtNumero = (EditText) view.findViewById(R.id.edtNumero);
            EditText edtEndereco = (EditText) view.findViewById(R.id.edtEndereco);
            EditText edtBairro = (EditText) view.findViewById(R.id.edtBairro);
            EditText edtCidade = (EditText) view.findViewById(R.id.edtCidade);
            EditText edtEstado = (EditText) view.findViewById(R.id.edtEstado);

            GmapsLocale gmapsLocale = new Gson().fromJson(s, GmapsLocale.class);

            edtCep.setText( ((gmapsLocale.getCep() == 0) ? "" : String.valueOf(gmapsLocale.getCep())) );
            edtEndereco.setText(gmapsLocale.getEndereco());
            edtNumero.setText(gmapsLocale.getNumero());
            edtBairro.setText(gmapsLocale.getBairro());
            edtCidade.setText(gmapsLocale.getCidade());
            edtEstado.setText(gmapsLocale.getEstado());

            title = gmapsLocale.getEndereco()+", "+gmapsLocale.getNumero()+" - "+gmapsLocale.getBairro();
        }else{
            Toast.makeText(context, "Erro ao carregar localização", Toast.LENGTH_SHORT).show();
        }

        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        map.animateCamera(update);

        MarkerOptions mo = new MarkerOptions();
        mo.position(latLng);
        mo.title(title).visible(true);
        mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_white_48dp));

        map.clear();
        map.addMarker(mo).showInfoWindow();

        progressDialog.dismiss();

        super.onPostExecute(s);
    }

}
