package br.com.smartpromos.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.MensagemResponse;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.ui.fragment.UseCoupon;
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CouponDetailsUsableActivity extends AppCompatActivity {

    private TextView txtTitle;
    private ImageButton imgToolbar;

    private TextView tituCoupon;
    private TextView txtDescription;
    private TextView txtInicio;
    private TextView txtFim;
    private RelativeLayout containerImgCoupon;
    private Button btnDescartar;
    private Button btnConfirmar;
    private CupomResponse cupom;
    private ClienteResponse cliente;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details_usable);

        cliente = SmartSharedPreferences.getUsuarioCompleto(getApplicationContext());
        String cupomid = this.getIntent().getStringExtra("cupomid");

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getResources().getString(R.string.txt_cadastro));

        imgToolbar = (ImageButton) findViewById(R.id.imgToolbar);
        imgToolbar.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_white_36dp));

        imgToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tituCoupon          = (TextView) findViewById(R.id.tituCoupon);
        txtDescription      = (TextView) findViewById(R.id.txtDescription);
        txtInicio           = (TextView) findViewById(R.id.txtInicio);
        txtFim              = (TextView) findViewById(R.id.txtFim);
        containerImgCoupon  = (RelativeLayout) findViewById(R.id.containerImgCoupon);
        btnDescartar        = (Button) findViewById(R.id.btnDescartar);
        btnConfirmar        = (Button) findViewById(R.id.btnConfirmar);

        getInfoCoupom(cupomid);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useCupom();
            }
        });

        btnDescartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descartarCupons();
            }
        });
    }


    private void getInfoCoupom(String id){

        smartRepo.cuponsById(id, new Callback<CupomResponse>() {

            @Override
            public void success(CupomResponse cupomResponse, Response response) {

                if(cupomResponse != null){
                    cupom = cupomResponse;

                    txtTitle.setText(cupomResponse.getSale().getEstablishment().getFantasy_name());
                    tituCoupon.setText(cupomResponse.getName());
                    txtDescription.setText(cupomResponse.getDescription());
                    txtInicio.setText(cupomResponse.getSale().getStart_date());
                    txtFim.setText(cupomResponse.getSale().getOver_date());

                    //Bitmap bitmap = ImageHandler.loadImagem(cupomResponse.getPath_img());
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

        smartRepo.descartarCuponsAceitos(cupom.getId_coupon(), cliente.getDoc_id(), 2, new Callback<MensagemResponse>() {
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

}
