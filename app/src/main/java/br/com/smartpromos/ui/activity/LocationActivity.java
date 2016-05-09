package br.com.smartpromos.ui.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import br.com.smartpromos.R;
import br.com.smartpromos.ui.fragment.LocationFragment;

public class LocationActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        LocationFragment mFrag = new LocationFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapa, mFrag);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
