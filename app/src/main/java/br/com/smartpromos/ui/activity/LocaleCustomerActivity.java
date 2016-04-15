package br.com.smartpromos.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

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
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LocaleCustomerActivity extends AppCompatActivity {

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

    String cep;
    String endereco;
    String numero;
    String bairro;
    String cidade;
    String estado;
    String tipoEndereco;
    LocalizacaoResponse localizacaoResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale_customer);

        localizacaoResponse = SmartSharedPreferences.getLocalizacao(getApplicationContext());

        edtCep = (EditText) findViewById(R.id.edtCep);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtBairro = (EditText) findViewById(R.id.edtBairro);
        edtCidade = (EditText) findViewById(R.id.edtCidade);
        edtEstado = (EditText) findViewById(R.id.edtEstado);
        btnRegister = (Button) findViewById(R.id.btnRegister);

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

                            SmartSharedPreferences.gravarLocalizacao(getApplicationContext(),localizacaoResponse);
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

}
