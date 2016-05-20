package br.com.smartpromos.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import br.com.smartpromos.util.UIDialogsFragments;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paulo Vitor on 12/03/2016.
 */
public class UseCoupon extends DialogFragment {

    private TextView txtTitleDialog, descDialog;
    private RelativeLayout containerErrors;
    private Button btnConfirmar;
    private Button btnCancelar;
    private EditText edtCodigo;
    private String code;
    private UIDialogsFragments uiDialogs;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
    private static ClienteResponse cliente;
    private static CupomResponse cupom;
    public UseCoupon(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_use_coupon, container, false);
        // Inflate the layout to use as dialog or embedded fragment
        containerErrors = (RelativeLayout) view.findViewById(R.id.containerErrors);

        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(getActivity());

        btnConfirmar = (Button) view.findViewById(R.id.btnConfirmar);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        txtTitleDialog = (TextView) view.findViewById(R.id.txtTitleDialog);
        descDialog = (TextView) view.findViewById(R.id.descDialog);
        edtCodigo = (EditText) view.findViewById(R.id.edtCodigo);

        cliente = new Gson().fromJson(getArguments().getString("cliente"), ClienteResponse.class);
        cupom   = new Gson().fromJson(getArguments().getString("cupom"), CupomResponse.class);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useFinish();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        containerErrors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private boolean validCode(){
        code = edtCodigo.getText().toString();

        if(code == null || code.equals(""))
            return false;

        return true;
    }

    private void useFinish(){

        if(validCode()){
            useCoupom();
        }else{
            uiDialogs.showDialog("Código inválido", "Por favor digite o código de confirmação!");
        }

    }

    private void useCoupom(){

        smartRepo.utilizarCupon(code, cupom.getId_coupon(), cliente.getEmail(), new Callback<MensagemResponse>() {
            @Override
            public void success(MensagemResponse mensagemResponse, Response response) {
                if(mensagemResponse.getId() == 4){
                    uiDialogs.showDialogUseCupom(mensagemResponse.getMensagem(), "", "used");
                }else{
                    uiDialogs.showDialog(mensagemResponse.getMensagem(), "");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                uiDialogs.showDialog("Oops!", "Ocorreu um ao tentar conectar com o servidor.");
            }
        });

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
