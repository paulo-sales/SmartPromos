package br.com.smartpromos.ui.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.ListPlacesRespopnse;
import br.com.smartpromos.api.general.response.LocalizacaoResponse;
import br.com.smartpromos.api.general.response.LocationResponse;
import br.com.smartpromos.api.general.response.PlaceListResponse;
import br.com.smartpromos.task.GoogleGeocodingAPITaskSearch;
import br.com.smartpromos.util.SmartSharedPreferences;
import br.com.smartpromos.util.UIDialogsFragments;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByLocationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView txtTitle;
    private ImageButton imgToolbar;
    private Button btnConfirmarLocal;
    private Button btnAlterarLocal;
    private LinearLayout containerSetLocale;
    private EditText edtLoadLocale;
    private ImageButton btnSearch;

    private static final int THRESHOLD = 0;
    private String latitude, longitude;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;


    private View view;

    private UIDialogsFragments uiDialogs;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_GMAPS_PLACES, 45);
    private ClienteResponse cliente;

    public SearchByLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_by_location, container, false);
        getActivity().setDefaultKeyMode(getActivity().DEFAULT_KEYS_SEARCH_LOCAL);

        getActivity().findViewById(R.id.containerToolbarBottom).setVisibility(View.GONE);
        getActivity().findViewById(R.id.containerButtonsTop).setVisibility(View.GONE);

        cliente = SmartSharedPreferences.getUsuarioCompleto(getContext());

        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(getActivity());

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        map = mMapFragment.getMap();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map_container, mMapFragment).commit();
        mMapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();


        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_escolher_licalizacao));

        btnConfirmarLocal = (Button) view.findViewById(R.id.btnConfirmarLocal);
        btnAlterarLocal = (Button) view.findViewById(R.id.btnAlterarLocal);

        imgToolbar = (ImageButton) view.findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_48dp));

        containerSetLocale = (LinearLayout) view.findViewById(R.id.containerSetLocale);

        btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        edtLoadLocale = (EditText) view.findViewById(R.id.edtLoadLocale);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edtLoadLocale.getText().toString();
                if(!"".equalsIgnoreCase(address) || address != null){

                    String[] p = {"getLocation", address.replace(" ","+")};

                    GoogleGeocodingAPITaskSearch googleGeocodingAPITask = new GoogleGeocodingAPITaskSearch(getContext(), map, view);
                    googleGeocodingAPITask.execute(p);

                }else{
                    uiDialogs.showDialog("Nova Localização", "Preencha o seu endereço para continuar.");
                }
            }
        });

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnAlterarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                triggerAutoComplete(containerSetLocale);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().findViewById(R.id.containerToolbarBottom).setVisibility(View.GONE);
        getActivity().findViewById(R.id.containerButtonsTop).setVisibility(View.GONE);

    }

    public void showAutoComplete(LinearLayout linearLayout){

        int visibility = linearLayout.getVisibility();

        if(visibility == View.INVISIBLE){

            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.requestLayout();
            linearLayout.setAlpha(0.0f);

            linearLayout.animate()
                    .translationY(linearLayout.getHeight())
                    .alpha(1.0f);

        }else if(visibility == View.VISIBLE){

            linearLayout.setVisibility(View.INVISIBLE);
            linearLayout.requestLayout();

            linearLayout.animate()
                    .translationY(0)
                    .alpha(0.0f);

        }



    }

    public void triggerAutoComplete(LinearLayout linearLayout){

        showAutoComplete(linearLayout);
        String btnText = btnAlterarLocal.getText().toString();

        if(btnText.equalsIgnoreCase(getContext().getResources().getString(R.string.btn_alt_local_search))){
            btnAlterarLocal.setText("Cancelar");
        }else{
            btnAlterarLocal.setText(getContext().getResources().getString(R.string.btn_alt_local_search));
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SearchByLocationFragment f = (SearchByLocationFragment) getFragmentManager()
                .findFragmentById(R.id.map_container);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        setMapLocation(l);
        getPlaces(l);

    }

    private void setMapLocation(Location l) {

        if(l != null){

            map.clear();

            String[] p = {String.valueOf(l.getLatitude()), String.valueOf(l.getLongitude())};

            GoogleGeocodingAPITaskSearch googleGeocodingAPITask = new GoogleGeocodingAPITaskSearch(getContext(), map, view);
            googleGeocodingAPITask.execute(p);

        }


    }

    private void getPlaces(Location l){

        if(l != null){
            String location = String.valueOf(l.getLatitude())+","+String.valueOf(l.getLongitude());
            String gmapsKey = getContext().getResources().getString(R.string.gmaps_id);
            smartRepo.getPlacesByCustomerLocation(location, cliente.getSale_radius(), gmapsKey, new Callback<ListPlacesRespopnse>() {
                @Override
                public void success(ListPlacesRespopnse lista, Response response) {

                    if (lista.getStatus().equalsIgnoreCase("OK")){

                        String gson = new Gson().toJson(lista, ListPlacesRespopnse.class);
                        Log.e("LIST_PLACES", gson);

                    }

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

    }

}
