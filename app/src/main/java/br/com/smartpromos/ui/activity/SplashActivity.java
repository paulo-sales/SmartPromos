package br.com.smartpromos.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.adapter.ListCouponsToUseAdapter;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.ListaCuponsResponse;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.smartpromosapplication.SmartPromosApp;
import br.com.smartpromos.util.SmartSharedPreferences;
import br.com.smartpromos.util.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashActivity extends AppCompatActivity {

    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
    private LinearLayout containerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        containerView = (LinearLayout) findViewById(R.id.containerView);

        if(Util.isNetworkAvailable()){

            ClienteResponse cliente = SmartSharedPreferences.getUsuarioCompleto(getApplicationContext());

            if(cliente != null){
                preLoadImages(cliente);
            }


            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {

                    ClienteResponse clienteResponse = SmartSharedPreferences.getUsuarioCompleto(getApplicationContext());

                    if(clienteResponse == null){

                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();


                    }else{

                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }, 1000);

        }else{

            Util.showNetworkInfo(containerView, getApplicationContext());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!Util.isNetworkAvailable()){
            Util.showNetworkInfo(containerView, getApplicationContext());
        }else{

            ClienteResponse cliente = SmartSharedPreferences.getUsuarioCompleto(getApplicationContext());

            if(cliente != null){
                preLoadImages(cliente);
            }


            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {

                    ClienteResponse clienteResponse = SmartSharedPreferences.getUsuarioCompleto(getApplicationContext());

                    if(clienteResponse == null){

                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();


                    }else{

                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }, 1000);

        }

    }

    private void preLoadImages(ClienteResponse cliente){

        smartRepo.cuponsByEmailToLoadImage(cliente.getEmail(), new Callback<ListaCuponsResponse>() {
            @Override
            public void success(ListaCuponsResponse listaCuponsResponse, Response response) {

                if(listaCuponsResponse != null && listaCuponsResponse.getCupons().size() > 0){

                    for (CupomResponse r : listaCuponsResponse.getCupons()) {

                        ImageHandler.generateFeedfileImage(r.getPath_img(), String.valueOf(r.getId_coupon()));

                    }

                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }
}
