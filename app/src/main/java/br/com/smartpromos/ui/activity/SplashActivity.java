package br.com.smartpromos.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashActivity extends AppCompatActivity {

    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
