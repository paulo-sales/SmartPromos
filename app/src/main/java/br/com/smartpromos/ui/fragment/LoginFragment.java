package br.com.smartpromos.ui.fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;

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
import br.com.smartpromos.util.UIDialogsFragments;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private CallbackManager mCallBackMananger;

    private TextView txtHelp;
    private EditText edtLogin, edtPass;
    private Button btnLogin;

    private UIDialogsFragments uiDialogs;

    private ClienteRequest cliente;

    private SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @TargetApi(Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            //Log.v("LoginActivity", response.toString());

                            try {

                                if(object != null){

                                    uiDialogs.showLoading();

                                    Log.v("DATA_RESPONS", object.toString());

                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String gender = object.getString("gender");

                                    ClienteRequest cliente = new ClienteRequest();

                                    String[] fullName = name.split(" ");

                                    cliente.setFirst_name(fullName[0]);
                                    cliente.setLast_name(fullName[1]);

                                    if( object.has("birthday") &&  !object.getString("birthday").equals("")){

                                        String birth = object.getString("birthday");

                                        String[] bDay = birth.split("/");

                                        cliente.setBirthday(Integer.parseInt(bDay[1]));
                                        cliente.setBirthday_month(Integer.parseInt(bDay[0]));
                                        cliente.setBirthday_yaer(Integer.parseInt(bDay[2]));

                                    }

                                    int genero = ((gender.equalsIgnoreCase("male")) ? 1 : 2);

                                    cliente.setGender(genero);
                                    cliente.setPhone("");
                                    cliente.setEmail(email);

                                    cliente.setGet_offers(0);
                                    cliente.setStay_logged_in(1);
                                    cliente.setSale_radius(5);

                                    Log.v("DATA_CLIENT", new Gson().toJson(cliente, ClienteRequest.class));

                                    checkUser(cliente);

                                }

                            }catch (Exception e){
                                Log.e("ERRO_LOGIN_FB", e.getMessage());
                            }

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
            System.out.println(parameters);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

            Log.e("ERRO_FACEBOOK", e.getMessage()+" "+e.getCause());
        }
    };

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
        mCallBackMananger = CallbackManager.Factory.create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(getActivity());

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.loginFacebook);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallBackMananger, mCallback);

        txtHelp = (TextView) view.findViewById(R.id.txtHelp);

        edtLogin = (EditText) view.findViewById(R.id.edtLogin);
        edtPass = (EditText) view.findViewById(R.id.edtPass);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uiDialogs.showLoading();

                login();
            }
        });

        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp();
            }
        });

        cliente = null;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallBackMananger.onActivityResult(requestCode, resultCode, data);
    }

    private void checkUser(final ClienteRequest cliente){

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

                    Intent intent = new Intent(getContext(), LocationActivity.class);
                    intent.putExtra("cliente", new Gson().toJson(cliente, ClienteRequest.class));

                    getContext().startActivity(intent);

                    uiDialogs.loadingDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("USER_VEERIFIED_ERR", ""+new Gson().toJson(cliente, ClienteRequest.class));

                Log.e("RETORNO_ERR", ""+error.getCause());

            }
        });

    }

    private void login(){
        String login = edtLogin.getText().toString();
        String password = edtPass.getText().toString();

        if(!login.equals("")){

            if(!password.equals("")){


                smartRepo.loginCliente(login,password, new Callback<ClienteResponse>() {
                    @Override
                    public void success(ClienteResponse clienteResponse, Response response) {
                        if (clienteResponse.getMensagem().getId() == 3) {

                            if(clienteResponse.getStay_logged_in() == 1){
                                SmartSharedPreferences.gravarUsuarioResponseCompleto(getContext(),clienteResponse);
                            }else{
                                SmartSharedPreferences.logoutCliente(getContext());
                            }

                            getLocale(clienteResponse);

                        } else if (clienteResponse.getMensagem().getId() == 0) {
                            uiDialogs.loadingDialog.dismiss();
                            uiDialogs.showDialog("Erro ao acessar", clienteResponse.getMensagem().getMensagem());
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        uiDialogs.loadingDialog.dismiss();
                        uiDialogs.showDialog("Erro do servidor", "Nosso servidor esta off-line no momento.");
                    }
                });

            }else{
                uiDialogs.loadingDialog.dismiss();
                uiDialogs.showDialog("Erro ao acessar", "Preencha a sua senha!");
            }

        }else{
            uiDialogs.loadingDialog.dismiss();
            uiDialogs.showDialog("Erro ao acessar", "Preencha o seu e-mail!");
        }

    }

    public void getLocale(final ClienteResponse clienteLocale){

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

        smartRepo.cuponsByEmailToLoadImage(cliente.getEmail(), new Callback<ListaCuponsResponse>() {
            @Override
            public void success(ListaCuponsResponse listaCuponsResponse, Response response) {

                if(listaCuponsResponse != null && listaCuponsResponse.getCupons().size() > 0){

                    for (CupomResponse r : listaCuponsResponse.getCupons()) {

                        ImageHandler.generateFeedfileImage(r.getPath_img(), String.valueOf(r.getId_coupon()));
                    }

                }
                uiDialogs.loadingDialog.dismiss();

                getActivity().startActivity(new Intent(getActivity(), DashBoardActivity.class));
                getActivity().finish();

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

    public void showHelp(){
        uiDialogs.showHelpDialog();
    }


}
