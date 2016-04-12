package br.com.smartpromos.ui.fragment;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import br.com.smartpromos.R;
import br.com.smartpromos.ui.activity.DashBoardActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private ProgressDialog progressDialog;
    private CallbackManager mCallBackMananger;

    private TextView txtHelp;
    private EditText edtLogin, edtPass;
    private Button btnLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallBackMananger = CallbackManager.Factory.create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.loginFacebook);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallBackMananger, mCallback);

        txtHelp = (TextView) view.findViewById(R.id.txtHelp);

        edtLogin = (EditText) view.findViewById(R.id.edtLogin);
        edtPass = (EditText) view.findViewById(R.id.edtPass);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp();
            }
        });

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

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            /*
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            System.out.println("Meu usuario" + profile.getName());
            displayWelcomeMessage(profile);
            */

            // App code

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
                                    String birth = object.getString("birthday");
                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String pass = object.getString("id");
                                    String picture = "https://graph.facebook.com/" + object.getString("id")+ "/picture?type=large";


                                }

                            }catch (Exception e){

                            }

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
            System.out.println(parameters);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private void login(){
        String login = edtLogin.getText().toString();
        String password = edtPass.getText().toString();

        if(!login.equals("")){

            if(!password.equals("")){

                getActivity().startActivity(new Intent(getActivity(), DashBoardActivity.class));

            }else{
                showDialog("Erro ao acessar", "Preencha a sua senha!");
            }

        }else{
            showDialog("Erro ao acessar", "Preencha o seu e-mail!");
        }

    }

    public void showHelp(){
        showHelpDialog();
    }

    public void showHelpDialog() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogHelpLogin newFragment = new DialogHelpLogin();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();
    }

    public void showDialog(String title, String descDialog) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

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
