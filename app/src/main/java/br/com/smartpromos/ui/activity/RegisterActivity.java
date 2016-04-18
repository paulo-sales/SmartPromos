package br.com.smartpromos.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.adapter.GenderAdapter;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.request.ClienteRequest;
import br.com.smartpromos.api.general.request.LocalizacaoRequest;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.LocalizacaoResponse;
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {

    private TextView txtTitle;
    private ImageButton imgToolbar;
    private Spinner spinnerGender;
    private String[] gender = new String[]{"Gênero", "Masculino", "Feminino"};

    private Button edtDataNasc;

    private LocalizacaoRequest localizacaoRequest;

    private EditText edtNome;
    private EditText edtSobreNome;
    private EditText edtEmail;
    private EditText edtCpf;
    private EditText edtPass;
    private EditText edtConfPass;
    private Button btnRegister;

    String nome;
    String sobrenome;
    String email;
    String cpf;
    String dataNasc;
    String senha;
    String confSenha;
    String genero;
    int idgenero;
    ClienteRequest clienteRequest;

    private SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edtNome = (EditText) findViewById(R.id.edtNome);
        edtSobreNome = (EditText) findViewById(R.id.edtSobreNome);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtCpf = (EditText) findViewById(R.id.edtCpf);
        edtPass = (EditText) findViewById(R.id.edtPass);
        edtConfPass = (EditText) findViewById(R.id.edtConfPass);
        btnRegister = (Button) findViewById(R.id.btnRegister);


        Intent i = getIntent();

        if( i != null) {
            String locale = i.getStringExtra("localizacao");
            localizacaoRequest = new Gson().fromJson(locale, LocalizacaoRequest.class);
            Log.i("Locale_json", locale);
            Log.i("Check_locale", localizacaoRequest.getAddress());
        }

        edtDataNasc = (Button) findViewById(R.id.edtDataNasc);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_cadastro));

        imgToolbar = (ImageButton) findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_48dp));

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        spinnerGender.setLayoutParams(lp1);
        spinnerGender.setAdapter(adapter1);
        spinnerGender.setAdapter(new GenderAdapter(this, gender));

        edtDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        RegisterActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(true);
                dpd.dismissOnPause(true);
                dpd.setAccentColor(Color.parseColor("#303F9F"));
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }

        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarCliente();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        edtDataNasc.setText(date);
    }

    private boolean validarDados(){
        nome = edtNome.getText().toString();
        sobrenome = edtSobreNome.getText().toString();
        email = edtEmail.getText().toString();
        cpf = edtCpf.getText().toString();
        dataNasc = edtDataNasc.getText().toString();
        senha = edtPass.getText().toString();
        confSenha = edtConfPass.getText().toString();
        genero = spinnerGender.getSelectedItem().toString();
        idgenero = ( !genero.equalsIgnoreCase("Gênero") && genero.equalsIgnoreCase("Masculino") ) ? 1 : 2;

        if(nome.equals("") || nome == null){
            return false;
        }

        if(sobrenome.equals("") || sobrenome == null){
            return false;
        }


        if(email.equals("") || email == null){
            return false;
        }

        if(cpf.equals("") || cpf == null){
            return false;
        }

        if(dataNasc.equals("") || dataNasc == null){
            return false;
        }

        if(senha.equals("") || senha == null){
            return false;
        }

        if(confSenha.equals("") || confSenha == null){
            return false;
        }

        if(genero.equals("") || genero.equalsIgnoreCase("Gênero") || genero == null){
            return false;
        }
        return true;
    }

    private boolean checarSenha(){

        String senha = edtPass.getText().toString();
        String confSenha = edtConfPass.getText().toString();

        if(senha.equals(confSenha)){
            return true;
        }
        return false;
    }

    private void cadastrarCliente(){

        if(validarDados()){

            if(checarSenha()){

                String[] data = dataNasc.split("/");
                int dia = Integer.parseInt(data[0]);
                int mes = Integer.parseInt(data[1]);
                int ano = Integer.parseInt(data[2]);

                long c = 0;

                try{
                    c = Long.parseLong(cpf);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    Log.v("ERROR_CPF", cpf);
                    Log.v("ERROR_CPF", e.getMessage());
                }

                ClienteRequest cliente = new ClienteRequest(c, nome, sobrenome, idgenero, dia, mes, ano, 5, 0, 1, email, senha, "");

                String clienteJs = new Gson().toJson(cliente,ClienteRequest.class);
                String localizacaoJs = new Gson().toJson(localizacaoRequest,LocalizacaoRequest.class);

                Log.i("ClienteRequest",clienteJs);
                Log.i("EnderecoRequest",localizacaoJs);

                smartRepo.criarCadastro(clienteJs,localizacaoJs, new Callback<ClienteResponse>(){


                    @Override
                    public void success(ClienteResponse clienteResponse, Response response) {
                        if (clienteResponse.getMensagem().getId() == 3) {

                            showDialog(clienteResponse.getMensagem().getMensagem(), "");
                            SmartSharedPreferences.gravarUsuarioResponseCompleto(getApplicationContext(),clienteResponse);
                            getLocale(clienteResponse.getDoc_id());

                            startActivity(new Intent(RegisterActivity.this,DashBoardActivity.class));
                        } else {

                            showDialog("Erro ao cadastrar", clienteResponse.getMensagem().getMensagem());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

            }else{
                showDialog("Formulário de cadastro", "Campos de senha devem ser iguais!");
            }
        }else{

            showDialog("Formulário de cadastro", "Preencha todos os campos!");
        }

    }

    public void getLocale(long doc_id){

        smartRepo.getLocalizacao(doc_id, new Callback<LocalizacaoResponse>() {
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
