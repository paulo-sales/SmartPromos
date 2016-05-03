package br.com.smartpromos.ui.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.R;
import br.com.smartpromos.adapter.TypeLocaleAdapter;
import br.com.smartpromos.api.general.request.ClienteRequest;
import br.com.smartpromos.api.general.request.LocalizacaoRequest;
import br.com.smartpromos.api.general.request.MensagemRequest;
import br.com.smartpromos.task.GoogleGeocodingAPITask;
import br.com.smartpromos.ui.activity.RegisterActivity;
import br.com.smartpromos.util.UIDialogsFragments;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView txtTitle;
    private ImageButton imgToolbar;
    private Button btnConfirmarLocal;
    private Button btnAlterarLocal;
    private LinearLayout containerSetLocale;
    private EditText edtLoadLocale;
    private ImageButton btnSearch;
    private Spinner spinnerLocale;
    private String[] locale = new String[]{"Tipo da sua localização", "Residencial", "Trabalho", "Outros"};

    private static final int THRESHOLD = 0;
    private String latitude, longitude;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;

    private EditText edtCep;
    private EditText edtEndereco;
    private EditText edtNumero;
    private EditText edtBairro;
    private EditText edtCidade;
    private EditText edtEstado;

    private View view;

    private Button btnMyLocation;

    private UIDialogsFragments uiDialogs;

    private String clientStr = null;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_location, container, false);
        getActivity().setDefaultKeyMode(getActivity().DEFAULT_KEYS_SEARCH_LOCAL);

        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(getActivity());

        clientStr = getActivity().getIntent().getStringExtra("cliente");

        if(clientStr != null && !clientStr.equals("")){
            Log.v("DATA_RESPONSE", clientStr);
            ClienteRequest cliente = new Gson().fromJson(clientStr, ClienteRequest.class);
            uiDialogs.showDialog("Olá, "+cliente.getFirst_name()+"!", "Não encontramos o seu cadastro em nosso sistema. Você poderia nos passar mais algumas informações?");
        }

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        map = mMapFragment.getMap();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.map_container, mMapFragment).commit();
        mMapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        edtCep = (EditText) view.findViewById(R.id.edtCep);
        edtEndereco = (EditText) view.findViewById(R.id.edtEndereco);
        edtNumero = (EditText) view.findViewById(R.id.edtNumero);
        edtBairro = (EditText) view.findViewById(R.id.edtBairro);
        edtCidade = (EditText) view.findViewById(R.id.edtCidade);
        edtEstado = (EditText) view.findViewById(R.id.edtEstado);

        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_escolher_licalizacao));

        btnConfirmarLocal = (Button) view.findViewById(R.id.btnConfirmarLocal);
        btnAlterarLocal = (Button) view.findViewById(R.id.btnAlterarLocal);

        btnMyLocation = (Button) view.findViewById(R.id.btnMyLocation);

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

                    GoogleGeocodingAPITask googleGeocodingAPITask = new GoogleGeocodingAPITask(getContext(), map, view);
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

        btnConfirmarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistration();
            }
        });

        btnAlterarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                triggerAutoComplete(containerSetLocale);
            }
        });

        spinnerLocale = (Spinner) view.findViewById(R.id.spinnerLocale);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.type_locale, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        spinnerLocale.setLayoutParams(lp);
        spinnerLocale.setAdapter(adapter);
        spinnerLocale.setAdapter(new TypeLocaleAdapter(getActivity(), locale));

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        return view;
    }

    public String validaDados(){
        String cep = edtCep.getText().toString();
        String endereo = edtEndereco.getText().toString();
        String bairro = edtBairro.getText().toString();
        String cidade = edtCidade.getText().toString();
        String estado = edtEstado.getText().toString();
        String tipoEndereco = spinnerLocale.getSelectedItem().toString();

        if(cep.equals("") || cep == null )
            return "Por favor, preencha o seu cep!";

        if(endereo.equals("") || endereo == null )
            return "Por favor, preencha o seu endereço!";

        if(bairro.equals("") || bairro == null )
            return "Por favor, preencha o seu bairro!";

        if(cidade.equals("") || cidade == null )
            return "Por favor, preencha a sua cidade!";

        if(estado.equals("") || estado == null )
            return "Por favor, preencha o seu estado!";

        if(tipoEndereco.equals("") || tipoEndereco.equalsIgnoreCase("Tipo da sua localização") || tipoEndereco == null )
            return "Por favor, escolha o tipo da sua localização!";

        return null;
    }

    public void goToRegistration(){
        if(validaDados() == null){

            LocalizacaoRequest localizacaoRequest = new LocalizacaoRequest(
                    0,
                    edtBairro.getText().toString(),
                    edtCidade.getText().toString(),
                    edtEstado.getText().toString(),
                    Integer.parseInt(edtCep.getText().toString()),
                    "Brasil",
                    edtEndereco.getText().toString(),
                    edtNumero.getText().toString(),
                    1,
                    spinnerLocale.getSelectedItem().toString(),
                    0,
                    new MensagemRequest(0, "Inserir Localização")
            );
            String localeString = new Gson().toJson(localizacaoRequest, LocalizacaoRequest.class);

            Intent intent = new Intent(getActivity(), RegisterActivity.class);

            if(clientStr != null && !clientStr.equals("")){
                Log.v("DATA_SEND_CLIENT", clientStr);
                intent.putExtra("cliente", clientStr);
            }

            intent.putExtra("localizacao", localeString);
            getActivity().startActivity(intent);

        }else{
            uiDialogs.showDialog("Campos obrigatórios", validaDados());
        }
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

        if(btnText.equalsIgnoreCase("alterar")){
            btnAlterarLocal.setText("Cancelar");
        }else{
            btnAlterarLocal.setText("Alterar");
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

            map.clear();

            String[] p = {String.valueOf(l.getLatitude()), String.valueOf(l.getLongitude())};

            GoogleGeocodingAPITask googleGeocodingAPITask = new GoogleGeocodingAPITask(getContext(), map, view);
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
