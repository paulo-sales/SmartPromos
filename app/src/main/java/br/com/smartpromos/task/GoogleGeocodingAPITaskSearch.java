package br.com.smartpromos.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.GmapsLocale;
import br.com.smartpromos.api.general.response.ListPlacesResponse;
import br.com.smartpromos.api.general.response.Result;
import br.com.smartpromos.util.GetLocationGmpas;
import br.com.smartpromos.util.GoogleGeocodingAPI;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by Paulo on 11/04/2016.
 */
public class GoogleGeocodingAPITaskSearch extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private View view;
    Context context;
    GoogleMap map;
    double latitude;
    double longitude;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_GMAPS_PLACES, 45);
    private ClienteResponse cliente;
    private List<Result> resultList;
    private String[] types = {"liquor_store","store","spa","restaurant","pet_store","painter","night_club","meal_delivery","veterinary_care","bakery","bar","beauty_salon","bicycle_store","book_store","cafe","car_dealer","car_rental","car_repair","car_wash","clothing_store","department_store","electronics_store","florist","food","furniture_store","grocery_or_supermarket","gym","hardware_store"};

    public GoogleGeocodingAPITaskSearch(Context c, GoogleMap map, View view, ClienteResponse clienteResponse){
        context = c;
        this.map = map;
        this.view = view;
        this.cliente = clienteResponse;

        resultList = new ArrayList<>();
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

            GmapsLocale gmapsLocale = new Gson().fromJson(s, GmapsLocale.class);

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


        for(int i =0; i< types.length; i++){
            getPlaces(types[i]);
        }

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Log.e("ClickMarker", marker.getTitle()+marker.getTitle());
                for (Result r: resultList) {
                    LatLng latlng = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());
                    if(marker.getPosition() == latlng){

                        String location = String.valueOf(r.getGeometry().getLocation().getLat())+","+ String.valueOf(r.getGeometry().getLocation().getLng());

                        Log.e("ClickMarker", r.getName());
                        Log.e("ClickMarkerPosittion", location);

                    }
                }


            }
        });

        super.onPostExecute(s);
    }


    private void getPlaces(String type){

        String location = latitude+","+longitude;
        String gmapsKey =context.getResources().getString(R.string.gmaps_id);


        smartRepo.getPlacesByCustomerLocation(location, (cliente.getSale_radius() * 1000), type, gmapsKey, true, new Callback<ListPlacesResponse>() {
            @Override
            public void success(ListPlacesResponse lista, Response response) {

                if (lista.getStatus().equalsIgnoreCase("OK")){

                    //String json = new Gson().toJson(lista, ListPlacesResponse.class);
                    //Log.e("LIST_PLACES",json);

                    LatLng latLngList;
                    MarkerOptions moList;
                    int i = resultList.size();

                    for (Result r : lista.getResults()) {

                        //Log.e("PLACE_"+r.getName(), String.valueOf(r.getGeometry().getLocation().getLat())+","+String.valueOf(r.getGeometry().getLocation().getLng()));

                        moList = new MarkerOptions();
                        latLngList = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());

                        moList.position(latLngList)
                                .title(r.getName())
                                .visible(true);

                        //Bitmap bitmap = ImageHandler.loadImagem(r.getIcon());
                        //mo.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        moList.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_white_48dp));

                        map.addMarker(moList);
                        i++;

                        resultList.add(r);
                    }

                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }

}
