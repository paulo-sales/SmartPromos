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

import org.json.JSONObject;

import br.com.smartpromos.R;
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
        latitude = Double.parseDouble(params[0]);
        longitude = Double.parseDouble(params[1]);

        return GoogleGeocodingAPI.getCurrentLocationByJSON(latitude, longitude);
    }

    @Override
    protected void onPostExecute(String s) {

        String title = "Minha localização";

        if(s != null){

            EditText edtCep = (EditText) view.findViewById(R.id.edtCep);
            EditText edtEndereco = (EditText) view.findViewById(R.id.edtEndereco);
            EditText edtBairro = (EditText) view.findViewById(R.id.edtBairro);
            EditText edtCidade = (EditText) view.findViewById(R.id.edtCidade);
            EditText edtEstado = (EditText) view.findViewById(R.id.edtEstado);

            String[] fullAddred = s.split(",");
            String[] numberAndNeighborhood = fullAddred[1].split("-");
            String[] cityAndState = fullAddred[2].split("-");

            String street_address = fullAddred[0]+", "+numberAndNeighborhood[0].trim();

            Log.i("street_address", street_address);

            String postal_code = numberAndNeighborhood[1].trim()+" - "+fullAddred[3].trim();
            Log.i("postal_code", postal_code);

            edtCep.setText(fullAddred[3].trim().replace("-",""));
            edtEndereco.setText(fullAddred[0]);
            edtBairro.setText(numberAndNeighborhood[1].trim());
            edtCidade.setText(cityAndState[0].trim());
            edtEstado.setText(cityAndState[1].trim());
            title = street_address+" "+postal_code;
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
