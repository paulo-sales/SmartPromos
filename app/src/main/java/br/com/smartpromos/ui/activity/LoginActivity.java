package br.com.smartpromos.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.smartpromos.R;
import br.com.smartpromos.ui.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment mfrag = new LoginFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fraglogin, mfrag);
        fragmentTransaction.commit();
    }

}
