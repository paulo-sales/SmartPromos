package br.com.smartpromos.ui.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.ListPlacesResponse;
import br.com.smartpromos.api.general.response.ListaCuponsResponse;
import br.com.smartpromos.api.general.response.Result;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.task.GoogleGeocodingAPITaskSearch;
import br.com.smartpromos.util.SmartSharedPreferences;
import br.com.smartpromos.util.UIDialogsFragments;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByLocationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button btnConfirmarLocal;
    private Button btnAlterarLocal;
    private LinearLayout containerSetLocale;
    private EditText edtLoadLocale;
    private ImageButton btnSearch;

    private static final int THRESHOLD = 0;
    private String latitude, longitude;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;

    private FloatingActionButton btnMyLocation;

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

        btnConfirmarLocal = (Button) view.findViewById(R.id.btnConfirmarLocal);
        btnAlterarLocal = (Button) view.findViewById(R.id.btnAlterarLocal);

        btnMyLocation = (FloatingActionButton) view.findViewById(R.id.btnMyLocation);

        containerSetLocale = (LinearLayout) view.findViewById(R.id.containerSetLocale);

        btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        edtLoadLocale = (EditText) view.findViewById(R.id.edtLoadLocale);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edtLoadLocale.getText().toString();
                if(!"".equalsIgnoreCase(address) || address != null){

                    String[] p = {"getLocation", address.replace(" ","+")};

                    GoogleGeocodingAPITaskSearch googleGeocodingAPITask = new GoogleGeocodingAPITaskSearch(getContext(), map, view, cliente, uiDialogs);
                    googleGeocodingAPITask.execute(p);

                }else{
                    uiDialogs.showDialog("Nova Localização", "Preencha o seu endereço para continuar.");
                }
            }
        });

        btnAlterarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                triggerAutoComplete(containerSetLocale);
            }
        });

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void triggerAutoComplete(final LinearLayout linearLayout){


        String btnText = btnAlterarLocal.getText().toString();

        if(btnText.equalsIgnoreCase(getContext().getResources().getString(R.string.btn_alt_local_search))){

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.animate_up_to_bottom);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    linearLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            linearLayout.startAnimation(animation);

            btnAlterarLocal.setText("Cancelar");
        }else{
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.animate_slide_up_out);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    linearLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            linearLayout.startAnimation(animation);

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

    private void getLocation(){

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

    }

    private void setMapLocation(Location l) {

        if(l != null){

            String[] p = {String.valueOf(l.getLatitude()), String.valueOf(l.getLongitude())};
            GoogleGeocodingAPITaskSearch googleGeocodingAPITask = new GoogleGeocodingAPITaskSearch(getContext(), map, view, cliente, uiDialogs);
            googleGeocodingAPITask.execute(p);

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
