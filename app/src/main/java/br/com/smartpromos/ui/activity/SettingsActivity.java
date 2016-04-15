package br.com.smartpromos.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appyvet.rangebar.RangeBar;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.request.LocalizacaoRequest;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.LocalizacaoResponse;
import br.com.smartpromos.api.general.response.MensagemResponse;
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingsActivity extends AppCompatActivity {

    private TextView txtTitle;
    private ImageButton imgToolbar;

    private Button btnEnviar;

    private ToggleButton tgBtnConectado;
    private ToggleButton tgBtnRecebeOfertaTodas;
    private ToggleButton tgBtnRecebeOfertaSolicitadas;
    private ToggleButton tgBtnRecebeOfertaComprei;

    private TextView txtKm;
    private RangeBar rangebar;

    private ClienteResponse clienteResponse;
    private int conectado;
    private int distancia;
    private int privacidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        clienteResponse = SmartSharedPreferences.getUsuarioCompleto(getApplicationContext());

        conectado = clienteResponse.getStay_logged_in();
        distancia = clienteResponse.getSale_radius();
        privacidade = clienteResponse.getGet_offers();

        Log.i("SETTINGS_SAVE", conectado+" - "+distancia+" - "+privacidade);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_settings));

        imgToolbar = (ImageButton) findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_48dp));

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnEnviar = (Button) findViewById(R.id.btnEnviar);

        tgBtnConectado = (ToggleButton) findViewById(R.id.tgBtnConectado);
        tgBtnRecebeOfertaTodas = (ToggleButton) findViewById(R.id.tgBtnRecebeOfertaTodas);
        tgBtnRecebeOfertaSolicitadas = (ToggleButton) findViewById(R.id.tgBtnRecebeOfertaSolicitadas);
        tgBtnRecebeOfertaComprei = (ToggleButton) findViewById(R.id.tgBtnRecebeOfertaComprei);

        txtKm = (TextView) findViewById(R.id.txtKm);
        rangebar = (RangeBar) findViewById(R.id.rangebar);

        if (privacidade == 0) {
            tgBtnRecebeOfertaTodas.setChecked(true);
            tgBtnRecebeOfertaSolicitadas.setChecked(false);
            tgBtnRecebeOfertaComprei.setChecked(false);
        }

        if (privacidade == 1){
            tgBtnRecebeOfertaSolicitadas.setChecked(true);
            tgBtnRecebeOfertaTodas.setChecked(false);
            tgBtnRecebeOfertaComprei.setChecked(false);
        }

        if( privacidade == 2) {
            tgBtnRecebeOfertaComprei.setChecked(true);
            tgBtnRecebeOfertaTodas.setChecked(false);
            tgBtnRecebeOfertaSolicitadas.setChecked(false);
        }

        tgBtnConectado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    conectado = 1;
                }else{
                    conectado = 0;
                }
            }
        });

        tgBtnRecebeOfertaTodas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tgBtnRecebeOfertaSolicitadas.setChecked(false);
                    tgBtnRecebeOfertaComprei.setChecked(false);
                    privacidade = 0;
                }
            }
        });

        tgBtnRecebeOfertaSolicitadas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tgBtnRecebeOfertaTodas.setChecked(false);
                    tgBtnRecebeOfertaComprei.setChecked(false);
                    privacidade = 1;
                }
            }
        });

        tgBtnRecebeOfertaComprei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tgBtnRecebeOfertaTodas.setChecked(false);
                    tgBtnRecebeOfertaSolicitadas.setChecked(false);
                    privacidade = 2;
                }
            }
        });

        rangebar.setSeekPinByIndex(distancia-5);
        txtKm.setText(distancia+" km");

        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                distancia = rightPinIndex+5;
                txtKm.setText(rightPinValue+" km");
                Log.i("LEFT_VALUE", leftPinValue);
                Log.i("RIGHT_VALUE", String.valueOf(rightPinIndex));

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

    }

    public String validateSettings(){

        if( !tgBtnRecebeOfertaTodas.isChecked() && !tgBtnRecebeOfertaSolicitadas.isChecked() && !tgBtnRecebeOfertaComprei.isChecked() )
            return "Por favor, escolha o tipo de privacidade para receber as ofertas!";

        return null;
    }

    private void saveSettings(){

        if(validateSettings() == null){

            clienteResponse.setSale_radius(distancia);
            clienteResponse.setStay_logged_in(conectado);
            clienteResponse.setGet_offers(privacidade);

            String cliente = new Gson().toJson(clienteResponse, ClienteResponse.class);
            Log.i("RESULT_SETTINGS", cliente);

            SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

            smartRepo.updateCustomerSettings(cliente, new Callback<MensagemResponse>() {
                @Override
                public void success(MensagemResponse mensagemResponse, Response response) {

                    if(mensagemResponse.getId() == 1){

                        showDialog(mensagemResponse.getMensagem(), "");

                        if(clienteResponse.getStay_logged_in() == 1){
                            SmartSharedPreferences.gravarUsuarioResponseCompleto(getApplicationContext(), clienteResponse);
                        }else{
                            SmartSharedPreferences.logoutCliente(getApplicationContext());
                        }

                    }else{

                        showDialog("Erro ao atualizar", mensagemResponse.getMensagem());
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }else{
            showDialog("Opções obrigatórias", validateSettings());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

}
