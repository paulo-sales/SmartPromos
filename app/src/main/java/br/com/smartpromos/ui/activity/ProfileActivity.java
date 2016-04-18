package br.com.smartpromos.ui.activity;

import android.content.Intent;
import android.graphics.Color;
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
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {

    private TextView txtTitle;
    private ImageButton imgToolbar;
    private Spinner spinnerGender;
    private String[] gender = new String[]{"Gênero", "Masculino", "Feminino"};

    private Button edtDataNasc;

    private EditText edtNome;
    private EditText edtSobreNome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtConfPass;
    private Button btnRegister;

    private

    String nome;
    String sobrenome;
    String telefone;
    String email;
    String dataNasc;
    String senha;
    String confSenha;
    String genero;
    int idgenero;
    ClienteResponse clienteResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        clienteResponse = SmartSharedPreferences.getUsuarioCompleto(getApplicationContext());

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtSobreNome = (EditText) findViewById(R.id.edtSobreNome);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPass = (EditText) findViewById(R.id.edtPass);
        edtConfPass = (EditText) findViewById(R.id.edtConfPass);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        edtDataNasc = (Button) findViewById(R.id.edtDataNasc);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_profile));

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

        spinnerGender.getItemAtPosition(((clienteResponse.getGender() == 1) ? 0 : 1));

        spinnerGender.setAdapter(new GenderAdapter(this, gender));


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_locale, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        edtDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ProfileActivity.this,
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

        edtNome.setText(clienteResponse.getFirst_name());
        edtSobreNome.setText(clienteResponse.getLast_name());
        edtTelefone.setText(clienteResponse.getPhone());
        edtEmail.setText(clienteResponse.getEmail());
        edtDataNasc.setText(clienteResponse.getBirthday()+"/"+clienteResponse.getBirthday_month()+"/"+ clienteResponse.getBirthday_yaer());


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarCliente();
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
        telefone = edtTelefone.getText().toString();
        email = edtEmail.getText().toString();
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

        if(telefone.equals("") || telefone == null){
            return false;
        }

        if(email.equals("") || email == null){
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

    private void atualizarCliente(){

        if(validarDados()){
            if(checarSenha()){
                String[] data = dataNasc.split("/");
                int dia = Integer.parseInt(data[0]);
                int mes = Integer.parseInt(data[1]);
                int ano = Integer.parseInt(data[2]);
                ClienteRequest cliente = new ClienteRequest(clienteResponse.getDoc_id(), nome,  sobrenome,  idgenero,  dia,  mes, ano,clienteResponse.getSale_radius(), clienteResponse.getGet_offers(),clienteResponse.getStay_logged_in(), email, senha, telefone);

                SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
                String clienteJs = new Gson().toJson(cliente,ClienteRequest.class);
                smartRepo.updateCustomer(clienteJs, new Callback<ClienteResponse>() {
                    @Override
                    public void success(ClienteResponse clienteResponse, Response response) {
                        if (clienteResponse.getMensagem().getId() == 3) {

                            if(clienteResponse.getStay_logged_in() == 1){
                                SmartSharedPreferences.gravarUsuarioResponseCompleto(getApplicationContext(),clienteResponse);
                            }else{
                                SmartSharedPreferences.logoutCliente(getApplicationContext());
                            }
                            showDialog(clienteResponse.getMensagem().getMensagem(), "");



                        } else if (clienteResponse.getMensagem().getId() == 2) {

                            showDialog("Erro ao atualizar", clienteResponse.getMensagem().getMensagem());

                        } else if (clienteResponse.getMensagem().getId() == 1){

                            showDialog("Erro ao atualizar", clienteResponse.getMensagem().getMensagem());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

                Log.i("ClienteRequest",clienteJs);
            }else{
                showDialog("Atualização de cadastro", "Campos de senha devem ser iguais!");
            }
        }else{
            showDialog("Atualização de cadastro", "Preencha todos os campos!");
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
