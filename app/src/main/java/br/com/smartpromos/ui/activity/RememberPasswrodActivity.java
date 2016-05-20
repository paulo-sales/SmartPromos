package br.com.smartpromos.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import br.com.smartpromos.api.general.request.LocalizacaoRequest;
import br.com.smartpromos.api.general.response.MensagemResponse;
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.util.SmartSharedPreferences;
import br.com.smartpromos.util.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RememberPasswrodActivity extends AppCompatActivity {

    private TextView txtTitle;
    private ImageButton imgToolbar;

    private EditText edtEmail;
    private Button btnEnviar;

    private RelativeLayout containerView;
    private LinearLayout contentPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_passwrod);

        containerView = (RelativeLayout) findViewById(R.id.containerView);

        if(!Util.isNetworkAvailable()){

            Util.showNetworkInfo(containerView, getApplicationContext());
        }

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_recuperar_senha));

        imgToolbar = (ImageButton) findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_36dp));

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton();
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isNetworkAvailable()){

                    Util.showNetworkInfo(containerView, getApplicationContext());
                }else{
                    recuperarSenha();
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
        String email = edtEmail.getText().toString();

        if(email.equals("") || email == null){
            return false;
        }
        return true;
    }

    private void recuperarSenha(){

        if(validarDados()) {


            SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
            smartRepo.resetPass(edtEmail.getText().toString(), new Callback<MensagemResponse>() {
                @Override
                public void success(MensagemResponse mensagemResponse, Response response) {
                    if(mensagemResponse.getId() == 3) {

                        showDialog("Envio de senha", mensagemResponse.getMensagem());

                    }else{
                        showDialog("Erro no envio de senha", mensagemResponse.getMensagem());
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });


        } else{
            showDialog("Envio de senha", "Preencha o campo!");
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
