package br.com.smartpromos.ui.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.R;
import br.com.smartpromos.task.GoogleGeocodingAPITask;
import br.com.smartpromos.ui.activity.RegisterActivity;
import br.com.smartpromos.util.GoogleGeocodingAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements TextWatcher, AdapterView.OnItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView txtTitle;
    private ImageButton imgToolbar;
    private Button btnConfirmarLocal;
    private Button btnAlterarLocal;
    private LinearLayout containerSetLocale;
    private AutoCompleteTextView edtLoadLocale;

    private static final int THRESHOLD = 0;
    private String latitude, longitude;
    private List<Address> autoCompleteSuggestionAddresses;
    private ArrayAdapter<String> autoCompleteAdapter;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        getActivity().setDefaultKeyMode(getActivity().DEFAULT_KEYS_SEARCH_LOCAL);

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

        autoCompleteAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        autoCompleteAdapter.setNotifyOnChange(false);

        edtLoadLocale = (AutoCompleteTextView) view.findViewById(R.id.edtLoadLocale);

        edtLoadLocale.addTextChangedListener(this);
        //edtLoadLocale.setOnItemSelectedListener();
        edtLoadLocale.setThreshold(THRESHOLD);
        edtLoadLocale.setAdapter(autoCompleteAdapter);

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnConfirmarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        btnAlterarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAutoComplete(containerSetLocale);
                String btnText = btnAlterarLocal.getText().toString();

                if(btnText.equalsIgnoreCase("alterar")){
                    btnAlterarLocal.setText("Cancelar");
                }else{
                    btnAlterarLocal.setText("Alterar");
                }
            }
        });

        return view;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocationFragment f = (LocationFragment) getFragmentManager()
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


    }

    private void setMapLocation(Location l) {

        if(l != null){

            map.clear();

            String[] p = {String.valueOf(l.getLatitude()), String.valueOf(l.getLongitude())};

            GoogleGeocodingAPITask googleGeocodingAPITask = new GoogleGeocodingAPITask(getActivity(), map);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String value = s.toString();

        GetSuggestions getSuggestions = new GetSuggestions();
        String[] p = {value};
        getSuggestions.execute(p);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position < autoCompleteSuggestionAddresses.size()) {
            Address selected = autoCompleteSuggestionAddresses.get(position);
            latitude = Double.toString(selected.getLatitude());
            longitude = Double.toString(selected.getLongitude());

            map.clear();

            String[] p = {latitude, longitude};

            GoogleGeocodingAPITask googleGeocodingAPITask = new GoogleGeocodingAPITask(getActivity(), map);
            googleGeocodingAPITask.execute(p);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class GetSuggestions extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //autoCompleteAdapter.clear();
        }

        @Override
        protected String doInBackground(String... params) {

            String value = params[0];

            try {
                autoCompleteSuggestionAddresses = new Geocoder(getContext()).getFromLocationName(value, 0);

            } catch (IOException e) {

                e.printStackTrace();
            }


            return "Ok";
        }

        @Override
        protected void onPostExecute(String s) {

            latitude = longitude = null;


            if(autoCompleteSuggestionAddresses.size() > 0){
                autoCompleteAdapter.clear();
            }

            for (Address a : autoCompleteSuggestionAddresses) {
                Log.v("text autocomplete", a.toString());
                //String temp = ""+ a.getFeatureName()+" "+a.getLocality()+ " "+a.getCountryName();
                autoCompleteAdapter.add(a.toString());
            }

            autoCompleteAdapter.notifyDataSetChanged();

            super.onPostExecute(s);
        }
    }

}
