package br.com.smartpromos.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.gson.Gson;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.request.ClienteRequest;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.ListaCuponsResponse;
import br.com.smartpromos.api.general.response.LocalizacaoResponse;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.ui.activity.DashBoardActivity;
import br.com.smartpromos.ui.activity.LocationActivity;
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paulo Vitor on 12/03/2016.
 */
public class DialogLoading extends DialogFragment {

    public DialogLoading(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.loading_dialog, container, false);
        // Inflate the layout to use as dialog or embedded fragment

        Bundle arguments = getArguments();

        if(arguments != null){

            String clientStr = getArguments().getString("cliente");

            if(clientStr != null && !clientStr.equals("")){
                Log.v("DATA_RESPONSE_FACEBOOK", clientStr);
                ClienteRequest cliente = new Gson().fromJson(clientStr, ClienteRequest.class);

                checkUser(cliente);
            }

        }

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void checkUser(final ClienteRequest cliente){

        SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

        smartRepo.checkClienteByFacebook(cliente.getEmail(), new Callback<ClienteResponse>() {
            @Override
            public void success(ClienteResponse clienteResponse, Response response) {

                Log.e("USER_VERIFIED", ""+new Gson().toJson(cliente, ClienteRequest.class));

                Log.e("RETORNO_ID", ""+clienteResponse.getMensagem().getId());

                if (clienteResponse.getMensagem().getId() == 1) {

                    if(clienteResponse.getStay_logged_in() == 1){
                        SmartSharedPreferences.gravarUsuarioResponseCompleto(getContext(),clienteResponse);
                    }else{
                        SmartSharedPreferences.logoutCliente(getContext());
                    }

                    getLocale(clienteResponse);

                }else{

                    Intent intent = new Intent(getActivity(), LocationActivity.class);
                    intent.putExtra("cliente", new Gson().toJson(cliente, ClienteRequest.class));

                    getActivity().startActivity(intent);
                    dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("USER_VEERIFIED_ERR", ""+new Gson().toJson(cliente, ClienteRequest.class));

                Log.e("RETORNO_ERR", ""+error.getCause());

            }
        });

    }

    public void getLocale(final ClienteResponse clienteLocale){

        SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

        smartRepo.getLocalizacao(clienteLocale.getDoc_id(), new Callback<LocalizacaoResponse>() {
            @Override
            public void success(LocalizacaoResponse localizacaoResponse, Response response) {

                if (localizacaoResponse.getMensagem().getId() == 1) {

                    SmartSharedPreferences.gravarLocalizacao(getContext(), localizacaoResponse);

                }

                preLoadImages(clienteLocale);

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void preLoadImages(ClienteResponse cliente){

        SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

        smartRepo.cuponsByEmailToLoadImage(cliente.getEmail(), new Callback<ListaCuponsResponse>() {
            @Override
            public void success(ListaCuponsResponse listaCuponsResponse, Response response) {

                if(listaCuponsResponse != null && listaCuponsResponse.getCupons().size() > 0){

                    for (CupomResponse r : listaCuponsResponse.getCupons()) {

                        ImageHandler.generateFeedfileImage(r.getPath_img(), String.valueOf(r.getId_coupon()));
                    }

                }

                dismiss();

                getActivity().startActivity(new Intent(getActivity(), DashBoardActivity.class));
                getActivity().finish();

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }




}
