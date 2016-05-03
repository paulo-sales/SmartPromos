package br.com.smartpromos.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.MensagemResponse;
import br.com.smartpromos.api.general.response.Result;
import br.com.smartpromos.ui.activity.DashBoardActivity;
import br.com.smartpromos.util.SmartSharedPreferences;
import br.com.smartpromos.util.UIDialogsFragments;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paulo Vitor on 12/03/2016.
 */
public class DialogMarker extends DialogFragment {

    private Button btnEnviar;
    private TextView txtTitlePlace, txtSub;
    private RelativeLayout containerHelp;
    private UIDialogsFragments uiDialogs;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
    private Result place;

    public DialogMarker(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_marker_details, container, false);
        // Inflate the layout to use as dialog or embedded fragment
        containerHelp   = (RelativeLayout) view.findViewById(R.id.containerHelp);

        btnEnviar       = (Button) view.findViewById(R.id.btnEnviar);
        txtTitlePlace   = (TextView) view.findViewById(R.id.txtTitlePlace);
        txtSub          = (TextView) view.findViewById(R.id.txtSub);

        uiDialogs       = new UIDialogsFragments();
        uiDialogs.uiGetActivity(getActivity());

        place           = new Gson().fromJson(getArguments().getString("place"), Result.class);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSolicitacao(place);
            }
        });

        txtTitlePlace.setText(place.getName());
        txtSub.setText(place.getVicinity());

        containerHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void sendSolicitacao(Result place){

        ClienteResponse cliente = SmartSharedPreferences.getUsuarioCompleto(getContext());

        smartRepo.sendRequestSale(cliente.getDoc_id(), place.getPlaceId(), new Callback<MensagemResponse>() {
            @Override
            public void success(MensagemResponse mensagem, Response response) {

                dismiss();
                uiDialogs.showDialog(mensagem.getMensagem(),"");

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
