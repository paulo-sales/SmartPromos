package br.com.smartpromos.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.MensagemResponse;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.services.scroll.EndlessScrollListener;
import br.com.smartpromos.services.scroll.EndlessScrollView;
import br.com.smartpromos.task.GoogleGeocodingStabTask;
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.ui.fragment.UseCoupon;
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CouponDetailsUsableActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView tituCoupon;
    private TextView txtDescription;
    private TextView txtFim;
    private ImageView containerImgCoupon;
    private Button btnConfirmar;
    private CupomResponse cupom;
    private ClienteResponse cliente;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details_usable);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolBar);

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        map = mMapFragment.getMap();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.mapa, mMapFragment).commit();
        mMapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(CouponDetailsUsableActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        cliente = SmartSharedPreferences.getUsuarioCompleto(getApplicationContext());
        String cupomid = this.getIntent().getStringExtra("cupomid");

        tituCoupon          = (TextView) findViewById(R.id.tituCoupon);
        txtDescription      = (TextView) findViewById(R.id.txtDescription);
        txtFim              = (TextView) findViewById(R.id.txtFim);
        containerImgCoupon  = (ImageView) findViewById(R.id.containerImgCoupon);
        btnConfirmar        = (Button) findViewById(R.id.btnConfirmar);

        getInfoCoupom(cupomid);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useCupom();
            }
        });

        toolbarTextAppearence();
    }

    private void toolbarTextAppearence() {

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

    }

    private void getInfoCoupom(String id){

        smartRepo.cuponsById(id, new Callback<CupomResponse>() {

            @Override
            public void success(CupomResponse cupomResponse, Response response) {

                if(cupomResponse != null){
                    cupom = cupomResponse;

                    //txtTitle.setText(cupomResponse.getSale().getEstablishment().getFantasy_name());

                    collapsingToolbarLayout.setTitle(cupomResponse.getSale().getEstablishment().getFantasy_name());

                    tituCoupon.setText(cupomResponse.getName());
                    txtDescription.setText(cupomResponse.getDescription());
                    //txtInicio.setText("Início "+cupomResponse.getSale().getStart_date());
                    txtFim.setText(cupomResponse.getSale().getOver_date());

                    Bitmap bitmap = ImageHandler.getImageBitmap(String.valueOf(cupomResponse.getId_coupon()), cupomResponse.getPath_img());
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                    containerImgCoupon.setBackground(drawable);

                }else{
                    showDialog("Erro", "Não conseguimos localizar este cupom!", "");
                }


            }

            @Override
            public void failure(RetrofitError error) {
                showDialog("Erro", "Ocorreu um erro ao tentar conectar com o servidor!", "");
            }
        });

    }

    private void descartarCupons(){

        smartRepo.descartarCuponsAceitos(cupom.getId_coupon(), cliente.getEmail(), 2, new Callback<MensagemResponse>() {
            @Override
            public void success(MensagemResponse mensagemResponse, Response response) {

                if(mensagemResponse.getId() == 1){
                    showDialog(mensagemResponse.getMensagem(), "", "rejected");
                }else{
                    showDialog(mensagemResponse.getMensagem(), "", "");
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    public void showDialog(String title, String descDialog, String extra) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("description", descDialog);

        if(extra != null || !extra.equals("")){
            bundle.putString("extra", extra);
        }

        DialogUI newFragment = new DialogUI();
        newFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    public void useCupom() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = new Bundle();

        String clienteStr = new Gson().toJson(cliente, ClienteResponse.class);
        String CupomStr = new Gson().toJson(cupom, CupomResponse.class);

        bundle.putString("cliente", clienteStr);
        bundle.putString("cupom", CupomStr);

        UseCoupon newFragment = new UseCoupon();
        newFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        if(cupom != null){

            String address = cupom.getSale().getEstablishment().getStreet();
            String number = (cupom.getSale().getEstablishment().getNumber_address() != null) ? "+"+cupom.getSale().getEstablishment().getNumber_address() : "";
            String bairro =  (!"".equals(cupom.getSale().getEstablishment().getNeighborwood()) || cupom.getSale().getEstablishment().getNeighborwood() != null) ? "+"+cupom.getSale().getEstablishment().getNeighborwood() : "";
            String cidade =  (!"".equals(cupom.getSale().getEstablishment().getCity()) || cupom.getSale().getEstablishment().getCity() != null) ? "+"+cupom.getSale().getEstablishment().getCity() : "";
            String estado =  (!"".equals(cupom.getSale().getEstablishment().getState()) || cupom.getSale().getEstablishment().getState() != null) ? "+"+cupom.getSale().getEstablishment().getState() : "";
            String cep =  (!"".equals(cupom.getSale().getEstablishment().getZip_code()) || cupom.getSale().getEstablishment().getZip_code() != null ) ? "+"+String.valueOf(cupom.getSale().getEstablishment().getZip_code()) : "";

            String addreddComplete = address+number+bairro+cidade+estado+cep;
            getMapLocation(addreddComplete.replace(" ", "+"));
        }

    }

    private void getMapLocation(String address){
        String[] p = {"getLocation", address};

        GoogleGeocodingStabTask googleGeocodingAPITask = new GoogleGeocodingStabTask(CouponDetailsUsableActivity.this, map);
        googleGeocodingAPITask.execute(p);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        onMapReady(map);
        onConnected(new Bundle());
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
