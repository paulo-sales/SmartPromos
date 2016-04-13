package br.com.smartpromos.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import br.com.smartpromos.R;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.util.SmartSharedPreferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
