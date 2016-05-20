package br.com.smartpromos.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.request.ClienteRequest;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.LocalizacaoResponse;
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.util.SmartSharedPreferences;
import br.com.smartpromos.util.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextView txtTitle;
    private ImageButton imgToolbar;

    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtPass;
    private Button btnRegister;

    private RelativeLayout containerView;
    private LinearLayout contentPanel;

    String nome;
    String email;
    String senha;

    private SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        containerView = (RelativeLayout) findViewById(R.id.containerView);

        if(!Util.isNetworkAvailable()){

            Util.showNetworkInfo(containerView, getApplicationContext());
        }

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_cadastro));

        imgToolbar = (ImageButton) findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_36dp));

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton();
            }
        });

        Intent i = getIntent();

        if( i != null) {
            String locale = i.getStringExtra("localizacao");
            String clienteStr = i.getStringExtra("cliente");

            if(clienteStr != null && !clienteStr.equals("")){
                Log.v("DATA_RESPONSE", clienteStr);
                ClienteRequest cliente = new Gson().fromJson(clienteStr, ClienteRequest.class);

                edtNome.setText(cliente.getFirst_name());
                edtEmail.setText(cliente.getEmail());

            }

        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Util.isNetworkAvailable()){

                    Util.showNetworkInfo(containerView, getApplicationContext());
                }else{

                    cadastrarCliente();
                }
            }
        });

        contentPanel = (LinearLayout) findViewById(R.id.containerFields);

        Animation showContainer  = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animate_bottom_to_up);
        contentPanel.startAnimation(showContainer);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!Util.isNetworkAvailable()){

            Util.showNetworkInfo(containerView, getApplicationContext());
        }
    }

    private void backButton() {

        Animation hideContainer  = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animate_slide_up_out);

        hideContainer.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contentPanel.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
        contentPanel.startAnimation(hideContainer);

    }

    private boolean validarDados(){
        nome = edtNome.getText().toString();
        email = edtEmail.getText().toString();
        senha = edtPass.getText().toString();

        if(nome.equals("") || nome == null){
            return false;
        }

        if(email.equals("") || email == null){
            return false;
        }
        if(senha.equals("") || senha == null){
            return false;
        }

        return true;
    }

    private void cadastrarCliente(){

        if(validarDados()){

            long c = 0;

            ClienteRequest cliente = new ClienteRequest(c, nome, "", 0, 0, 0, 0, 5, 0, 1, email, senha, "");

            String clienteJs = new Gson().toJson(cliente,ClienteRequest.class);

            Log.i("ClienteRequest",clienteJs);

            smartRepo.criarCadastro(clienteJs, new Callback<ClienteResponse>(){

                @Override
                public void success(ClienteResponse clienteResponse, Response response) {

                    if (clienteResponse.getMensagem().getId() == 3) {

                        showDialog(clienteResponse.getMensagem().getMensagem(), "");
                        SmartSharedPreferences.gravarUsuarioResponseCompleto(getApplicationContext(),clienteResponse);
                        //getLocale(clienteResponse.getEmail());

                        startActivity(new Intent(RegisterActivity.this, DashBoardActivity.class));
                        finish();

                    } else {

                        showDialog("Erro ao cadastrar", clienteResponse.getMensagem().getMensagem());
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }else{

            showDialog("Formulário de cadastro", "Preencha todos os campos!");
        }

    }

    public void getLocale(String email){

        smartRepo.getLocalizacao(email, new Callback<LocalizacaoResponse>() {
            @Override
            public void success(LocalizacaoResponse localizacaoResponse, Response response) {

                if (localizacaoResponse.getMensagem().getId() == 1) {

                    SmartSharedPreferences.gravarLocalizacao(getApplicationContext(), localizacaoResponse);

                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

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
