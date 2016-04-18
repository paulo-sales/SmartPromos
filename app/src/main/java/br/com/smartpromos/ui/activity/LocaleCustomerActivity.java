package br.com.smartpromos.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.CycleInterpolator;
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

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.adapter.TypeLocaleAdapter;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.request.ClienteRequest;
import br.com.smartpromos.api.general.request.LocalizacaoRequest;
import br.com.smartpromos.api.general.request.MensagemRequest;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.LocalizacaoResponse;
import br.com.smartpromos.task.GoogleGeocodingAPITask;
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LocaleCustomerActivity extends AppCompatActivity implements TextWatcher, AdapterView.OnItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView txtTitle;
    private ImageButton imgToolbar;
    private String[] locale = new String[]{"Tipo da sua localização", "Residencial", "Trabalho", "Outros"};
    private Spinner spinnerLocale;

    private EditText edtCep;
    private EditText edtEndereco;
    private EditText edtNumero;
    private EditText edtBairro;
    private EditText edtCidade;
    private EditText edtEstado;
    private Button btnRegister;
    private Button btnAlterarLocal;
    private LinearLayout containerSetLocale;
    private AutoCompleteTextView edtLoadLocale;

    private static final int THRESHOLD = 0;
    private String latitude, longitude;
    private List<Address> autoCompleteSuggestionAddresses;
    private ArrayAdapter<String> autoCompleteAdapter;
    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;

    private View view;

    String cep;
    String endereco;
    String numero;
    String bairro;
    String cidade;
    String estado;
    String tipoEndereco;
    LocalizacaoResponse localizacaoResponse;
    ClienteResponse clienteResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale_customer);

        view = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        map = mMapFragment.getMap();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.map_container, mMapFragment).commit();
        mMapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(LocaleCustomerActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        localizacaoResponse = SmartSharedPreferences.getLocalizacao(getApplicationContext());
        clienteResponse = SmartSharedPreferences.getUsuarioCompleto(getApplicationContext());


        edtCep = (EditText) findViewById(R.id.edtCep);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtBairro = (EditText) findViewById(R.id.edtBairro);
        edtCidade = (EditText) findViewById(R.id.edtCidade);
        edtEstado = (EditText) findViewById(R.id.edtEstado);
        btnRegister = (Button) findViewById(R.id.btnConfirmarLocal);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_locale));

        imgToolbar = (ImageButton) findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_48dp));

        spinnerLocale = (Spinner) findViewById(R.id.spinnerLocale);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_locale, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        spinnerLocale.setLayoutParams(lp);
        spinnerLocale.setAdapter(adapter);
        spinnerLocale.setAdapter(new TypeLocaleAdapter(this, locale));

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        containerSetLocale = (LinearLayout) view.findViewById(R.id.containerSetLocale);

        autoCompleteAdapter = new ArrayAdapter<String>(LocaleCustomerActivity.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        autoCompleteAdapter.setNotifyOnChange(false);

        edtLoadLocale = (AutoCompleteTextView) view.findViewById(R.id.edtLoadLocale);

        edtLoadLocale.addTextChangedListener(this);
        edtLoadLocale.setOnItemSelectedListener(this);
        edtLoadLocale.setThreshold(THRESHOLD);
        edtLoadLocale.setAdapter(autoCompleteAdapter);

        btnAlterarLocal = (Button) view.findViewById(R.id.btnAlterarLocal);
        edtCep.setText(String.valueOf(localizacaoResponse.getZip_code()));
        edtEndereco.setText(localizacaoResponse.getAddress());
        edtNumero.setText(localizacaoResponse.getNumber_address());
        edtBairro.setText(localizacaoResponse.getNeighborwood());
        edtCidade.setText(localizacaoResponse.getCity());
        edtEstado.setText(localizacaoResponse.getCountry());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarLocalizacao();
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean validarDados(){

        cep = edtCep.getText().toString();
        endereco = edtEndereco.getText().toString();
        numero = edtNumero.getText().toString();
        bairro = edtBairro.getText().toString();
        cidade = edtCidade.getText().toString();
        estado = edtEstado.getText().toString();
        tipoEndereco = spinnerLocale.getSelectedItem().toString();


        if(cep.equals("") || cep == null){
            return false;
        }

        if(endereco.equals("") || endereco == null){
            return false;
        }

        if(numero.equals("") || numero == null){
            return false;
        }

        if(bairro.equals("") || bairro == null){
            return false;
        }

        if(cidade.equals("") || cidade == null){
            return false;
        }

        if(estado.equals("") || estado == null){
            return false;
        }

        if(tipoEndereco.equals("") || tipoEndereco.equalsIgnoreCase("Tipo da sua localização") || tipoEndereco == null){
            return false;
        }

        return true;
    }

    private void atualizarLocalizacao(){

        if(validarDados()){

                LocalizacaoRequest localizacao = new LocalizacaoRequest(localizacaoResponse.getId_locale(), bairro, cidade, estado, Integer.parseInt(cep), localizacaoResponse.getCountry(),  endereco, numero, localizacaoResponse.getType(), tipoEndereco, localizacaoResponse.getCustomer(), new MensagemRequest(0,"Atualizar endereço"));

                SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
                String localizacaoJs = new Gson().toJson(localizacao,LocalizacaoRequest.class);
                smartRepo.updateLocale(localizacaoJs, new Callback<LocalizacaoResponse>() {
                    @Override
                    public void success(LocalizacaoResponse localizacaoResponse, Response response) {

                        if(clienteResponse.getStay_logged_in() == 1){
                            SmartSharedPreferences.gravarLocalizacao(getApplicationContext(),localizacaoResponse);
                        }else{
                            SmartSharedPreferences.logoutCliente(getApplicationContext());
                        }

                        showDialog("Atualização de localização", localizacaoResponse.getMensagem().getMensagem());

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }

                });

                Log.i("LocalizacaoRequest",localizacaoJs);


        }else{
            showDialog("Atualização de localização", "Preencha todos os campos!");
        }

    }

    public void showDialog(String title, String descDialog) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("description", descDialog);

        DialogUI newFragment = new DialogUI();
        newFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            GoogleGeocodingAPITask googleGeocodingAPITask = new GoogleGeocodingAPITask(LocaleCustomerActivity.this, map, view);
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

            GoogleGeocodingAPITask googleGeocodingAPITask = new GoogleGeocodingAPITask(LocaleCustomerActivity.this, map, view);
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
                autoCompleteSuggestionAddresses = new Geocoder(getApplicationContext()).getFromLocationName(value, 0);

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
