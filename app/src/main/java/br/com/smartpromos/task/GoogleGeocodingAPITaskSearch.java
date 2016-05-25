package br.com.smartpromos.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.places.PlaceRequest;
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
import br.com.smartpromos.api.general.response.MensagemResponse;
import br.com.smartpromos.api.general.response.PlaceResponse;
import br.com.smartpromos.api.general.response.Result;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.util.GetLocationGmpas;
import br.com.smartpromos.util.GoogleGeocodingAPI;
import br.com.smartpromos.util.UIDialogsFragments;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paulo on 11/04/2016.
 */
public class GoogleGeocodingAPITaskSearch extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private View view;
    Context context;
    GoogleMap map;
    private MarkerOptions mo;
    double latitude;
    double longitude;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_GMAPS_PLACES, 45);
    private static SmartRepo smartRepoServer = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
    private ClienteResponse cliente;
    private List<Result> resultList;
    private String[] types = {"liquor_store","store","spa","restaurant","pet_store","painter","night_club","meal_delivery","veterinary_care","bakery","bar","beauty_salon","bicycle_store","book_store","cafe","car_dealer","car_rental","car_repair","car_wash","clothing_store","department_store","electronics_store","florist","food","furniture_store","grocery_or_supermarket","gym","hardware_store"};
    private UIDialogsFragments uiDialogs;

    public GoogleGeocodingAPITaskSearch(Context c, GoogleMap map, View view, ClienteResponse clienteResponse, UIDialogsFragments uiDialogs){
        context = c;
        this.map = map;
        this.view = view;
        this.cliente = clienteResponse;

        resultList = new ArrayList<>();
        mo = new MarkerOptions();
        this.uiDialogs = uiDialogs;
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

        String title = "";

        if(s != null){

            GmapsLocale gmapsLocale = new Gson().fromJson(s, GmapsLocale.class);

            title = gmapsLocale.getEndereco()+", "+gmapsLocale.getNumero()+" - "+gmapsLocale.getBairro();
        }else{
            Toast.makeText(context, "Erro ao carregar localização", Toast.LENGTH_SHORT).show();
        }

        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        map.animateCamera(update);

        mo.position(latLng);
        mo.title("Minha localização")
                .snippet(title)
                .visible(true);
        mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_white_48dp));

        map.clear();
        map.addMarker(mo).showInfoWindow();

        progressDialog.dismiss();

        //for(int i =0; i< types.length; i++){
        for(int i =0; i< 1; i++){
            getPlaces(types[i]);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                for (Result r: resultList) {
                    LatLng latlng = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());

                    if(marker.getPosition().equals(latlng)){

                        String place = new Gson().toJson(r, Result.class);
                        uiDialogs.showDialogMarker(place);

                        Log.e("ClickMarker", place);
                    }
                }

                return false;
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

                    for (Result r : lista.getResults()) {

                        PlaceResponse place = new PlaceResponse();

                        place.setAdr_address(r.getVicinity());
                        place.setFormatted_address(r.getVicinity());
                        place.setFormatted_phone_number("");
                        place.setInternational_phone_number("");
                        place.setIcon(r.getTypes().get(0));
                        place.setName(r.getName());
                        place.setPlace_id(r.getPlaceId());

                        savePLace(place, r.getTypes().get(0));
                        Log.e("ICON_MARKER", r.getTypes().get(0));

                        mo = new MarkerOptions();
                        LatLng latLngList = new LatLng(r.getGeometry().getLocation().getLat(), r.getGeometry().getLocation().getLng());

                        mo.position(latLngList)
                                .title(r.getName())
                                .snippet(r.getVicinity())
                                .visible(true);

                        //Bitmap bitmap = ImageHandler.loadImagem(r.getIcon());
                        //mo.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                        mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_white_48dp));

                        map.addMarker(mo);

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

    private void savePLace(final PlaceResponse place, final String type){

        String placeStr = new Gson().toJson(place, PlaceResponse.class);

        Log.v("PLACE_JSON", placeStr);

        smartRepoServer.insertNearbyPlaces(placeStr, new Callback<MensagemResponse>() {
            @Override
            public void success(MensagemResponse mensagemResponse, Response response) {

                ImageHandler.generateFeedfileIcon(place.getIcon(), type);
                Log.v("PLACE_INSERIDO", mensagemResponse.getMensagem());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

}
