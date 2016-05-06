package br.com.smartpromos.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import br.com.smartpromos.BuildConfig;
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
import br.com.smartpromos.util.UIDialogsFragments;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paulo on 06/05/2016.
 */
public class LoginTask extends AsyncTask<String, Void, String> {

    private UIDialogsFragments uiDialog;
    private Context context;
    private ClienteRequest cliente;

    public LoginTask(UIDialogsFragments uiDialog, Context context, ClienteRequest cliente){
        this.uiDialog   = uiDialog;
        this.context    = context;
        this.cliente    = cliente;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {

        SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

        checkUser(smartRepo, cliente);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    private void checkUser(final SmartRepo smartRepo, final ClienteRequest cliente){

        smartRepo.checkClienteByFacebook(cliente.getEmail(), new Callback<ClienteResponse>() {
            @Override
            public void success(ClienteResponse clienteResponse, Response response) {

                Log.e("USER_VERIFIED", ""+new Gson().toJson(cliente, ClienteRequest.class));

                Log.e("RETORNO_ID", ""+clienteResponse.getMensagem().getId());

                if (clienteResponse.getMensagem().getId() == 1) {

                    if(clienteResponse.getStay_logged_in() == 1){
                        SmartSharedPreferences.gravarUsuarioResponseCompleto(context,clienteResponse);
                    }else{
                        SmartSharedPreferences.logoutCliente(context);
                    }

                    getLocale(smartRepo, clienteResponse);

                }else{

                    Intent intent = new Intent(context, LocationActivity.class);
                    intent.putExtra("cliente", new Gson().toJson(cliente, ClienteRequest.class));

                    context.startActivity(intent);

                    uiDialog.loadingDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("USER_VEERIFIED_ERR", ""+new Gson().toJson(cliente, ClienteRequest.class));

                Log.e("RETORNO_ERR", ""+error.getCause());

            }
        });

    }

    public void getLocale(final SmartRepo smartRepo, final ClienteResponse clienteLocale){

        smartRepo.getLocalizacao(clienteLocale.getDoc_id(), new Callback<LocalizacaoResponse>() {
            @Override
            public void success(LocalizacaoResponse localizacaoResponse, Response response) {

                if (localizacaoResponse.getMensagem().getId() == 1) {

                    SmartSharedPreferences.gravarLocalizacao(context, localizacaoResponse);

                }

                preLoadImages(smartRepo, clienteLocale);

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void preLoadImages(SmartRepo smartRepo, ClienteResponse cliente){

        smartRepo.cuponsByEmailToLoadImage(cliente.getEmail(), new Callback<ListaCuponsResponse>() {
            @Override
            public void success(ListaCuponsResponse listaCuponsResponse, Response response) {

                if(listaCuponsResponse != null && listaCuponsResponse.getCupons().size() > 0){

                    for (CupomResponse r : listaCuponsResponse.getCupons()) {

                        ImageHandler.generateFeedfileImage(r.getPath_img(), String.valueOf(r.getId_coupon()));
                    }

                }

                uiDialog.loadingDialog.dismiss();

                context.getApplicationContext().startActivity(new Intent(context, DashBoardActivity.class));

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

}
