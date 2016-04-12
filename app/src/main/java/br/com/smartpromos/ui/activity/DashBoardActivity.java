package br.com.smartpromos.ui.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.smartpromos.R;
import br.com.smartpromos.ui.fragment.SalesFragment;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private TextView HeaderTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        HeaderTitle = (TextView) findViewById(R.id.HeaderTitle);

        SalesFragment mfrag = new SalesFragment();

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mfrag);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        HeaderTitle.setText(getResources().getString(R.string.txt_sales));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.findViewById(R.id.nav_view);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_profile:

                HeaderTitle.setText(getResources().getString(R.string.txt_profile));

                break;
            case R.id.nav_locale:

                HeaderTitle.setText(getResources().getString(R.string.txt_locale));

                break;

            case R.id.nav_sales_received:

                HeaderTitle.setText(getResources().getString(R.string.txt_sales));

                break;

            case R.id.nav_sale_search:

                HeaderTitle.setText(getResources().getString(R.string.txt_search_sales));

                break;

            case R.id.nav_my_coupons:

                HeaderTitle.setText(getResources().getString(R.string.txt_coupons));

                break;

            case R.id.nav_coupons_canceled:

                HeaderTitle.setText(getResources().getString(R.string.txt_canceled_coupons));

                break;

            case R.id.nav_sync:

                HeaderTitle.setText(getResources().getString(R.string.txt_sync));

                break;

            case R.id.nav_config:

                HeaderTitle.setText(getResources().getString(R.string.txt_settings));

                break;

            case R.id.nav_logout:

                break;

            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
