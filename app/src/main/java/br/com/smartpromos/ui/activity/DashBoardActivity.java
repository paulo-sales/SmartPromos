package br.com.smartpromos.ui.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import br.com.smartpromos.R;
import br.com.smartpromos.ui.fragment.CanceledCouponsFragment;
import br.com.smartpromos.ui.fragment.DialogUI;
import br.com.smartpromos.ui.fragment.NewSalesFragment;
import br.com.smartpromos.ui.fragment.SalesDiscardedFragment;
import br.com.smartpromos.ui.fragment.SalesExpiredFragment;
import br.com.smartpromos.ui.fragment.SalesReceivedsFragment;
import br.com.smartpromos.ui.fragment.SalesRequestFragment;
import br.com.smartpromos.ui.fragment.SalesUsedFragment;
import br.com.smartpromos.ui.fragment.SearchByLocationFragment;
import br.com.smartpromos.util.SmartSharedPreferences;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private TextView HeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        HeaderTitle = (TextView) findViewById(R.id.HeaderTitle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        SalesReceivedsFragment mfrag = new SalesReceivedsFragment();

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mfrag);
        fragmentTransaction.commit();


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

    public void changeToSales(View view){

        SalesReceivedsFragment mfragSales = new SalesReceivedsFragment();

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mfragSales);
        fragmentTransaction.commit();

        HeaderTitle.setText(getResources().getString(R.string.txt_sales));


    }

    public void changetToSearch(View view){

        SearchByLocationFragment searchFrag = new SearchByLocationFragment();

        android.support.v4.app.FragmentTransaction fragSearch = getSupportFragmentManager().beginTransaction();
        fragSearch.replace(R.id.fragment_container, searchFrag);
        fragSearch.commit();

        HeaderTitle.setText(getResources().getString(R.string.txt_search_sales));
    }

    public void changeToCoupons(View view){
        NewSalesFragment mCouponfrag = new NewSalesFragment();

        android.support.v4.app.FragmentTransaction fragCouponTransaction = getSupportFragmentManager().beginTransaction();
        fragCouponTransaction.replace(R.id.fragment_container, mCouponfrag);
        fragCouponTransaction.commit();

        HeaderTitle.setText(getResources().getString(R.string.txt_sales));

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

        item.setChecked(false);

        switch (id){
            case R.id.nav_profile:

                item.setChecked(true);
                startActivity(new Intent(DashBoardActivity.this, ProfileActivity.class));

                break;
            case R.id.nav_locale:
                item.setChecked(true);
                startActivity(new Intent(DashBoardActivity.this, LocaleCustomerActivity.class));

                break;

            case R.id.nav_sales_received:
                item.setChecked(true);
                SalesReceivedsFragment mSalesReceived = new SalesReceivedsFragment();

                android.support.v4.app.FragmentTransaction fragSalesReceived = getSupportFragmentManager().beginTransaction();
                fragSalesReceived.replace(R.id.fragment_container, mSalesReceived);
                fragSalesReceived.commit();

                HeaderTitle.setText(getResources().getString(R.string.txt_sales));

                break;

            case R.id.nav_sale_search:
                item.setChecked(true);

                HeaderTitle.setText(getResources().getString(R.string.txt_search_sales));

                SearchByLocationFragment searchFrag = new SearchByLocationFragment();

                android.support.v4.app.FragmentTransaction fragSearch = getSupportFragmentManager().beginTransaction();
                fragSearch.replace(R.id.fragment_container, searchFrag);
                fragSearch.commit();

                break;

            case R.id.nav_sales_requested:
                item.setChecked(true);
                SalesRequestFragment requesFrag = new SalesRequestFragment();

                android.support.v4.app.FragmentTransaction frafReqTrans = getSupportFragmentManager().beginTransaction();
                frafReqTrans.replace(R.id.fragment_container, requesFrag);
                frafReqTrans.commit();

                HeaderTitle.setText(getResources().getString(R.string.txt_sales));

                break;
            /*
            case R.id.nav_sales_discarded:
                item.setChecked(true);
                SalesDiscardedFragment fragDiscar = new SalesDiscardedFragment();

                android.support.v4.app.FragmentTransaction trasDiscarded = getSupportFragmentManager().beginTransaction();
                trasDiscarded.replace(R.id.fragment_container, fragDiscar);
                trasDiscarded.commit();

                HeaderTitle.setText(getResources().getString(R.string.txt_sales));

                break;

            case R.id.nav_my_coupons:
                item.setChecked(true);
                NewSalesFragment mCouponfrag = new NewSalesFragment();

                android.support.v4.app.FragmentTransaction fragCouponTransaction = getSupportFragmentManager().beginTransaction();
                fragCouponTransaction.replace(R.id.fragment_container, mCouponfrag);
                fragCouponTransaction.commit();

                HeaderTitle.setText(getResources().getString(R.string.txt_coupons));

                break;
            */
            case R.id.nav_my_coupons_used:
                item.setChecked(true);
                SalesUsedFragment usedFrag = new SalesUsedFragment();

                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, usedFrag);
                fragmentTransaction.commit();

                HeaderTitle.setText(getResources().getString(R.string.txt_coupons));

                break;

            case R.id.nav_my_coupons_expired:
                item.setChecked(true);
                SalesExpiredFragment salesExpiredFragment = new SalesExpiredFragment();

                android.support.v4.app.FragmentTransaction salesExTrasFragment = getSupportFragmentManager().beginTransaction();
                salesExTrasFragment.replace(R.id.fragment_container, salesExpiredFragment);
                salesExTrasFragment.commit();

                HeaderTitle.setText(getResources().getString(R.string.txt_coupons));

                break;
            /*
            case R.id.nav_coupons_canceled:
                item.setChecked(true);
                HeaderTitle.setText(getResources().getString(R.string.txt_canceled_coupons));

                CanceledCouponsFragment mfrag = new CanceledCouponsFragment();

                android.support.v4.app.FragmentTransaction fragCanTrans = getSupportFragmentManager().beginTransaction();
                fragCanTrans.replace(R.id.fragment_container, mfrag);
                fragCanTrans.commit();

                break;
            */
            /*case R.id.nav_sync:

                HeaderTitle.setText(getResources().getString(R.string.txt_sync));

                SyncFragment fragSync = new SyncFragment();

                android.support.v4.app.FragmentTransaction fragmentTransactionSync = getSupportFragmentManager().beginTransaction();
                fragmentTransactionSync.replace(R.id.fragment_container, fragSync);
                fragmentTransactionSync.commit();

                break;*/

//            case R.id.nav_tutorial:
//
//                HeaderTitle.setText(getResources().getString(R.string.txt_tutorial));
//
//                break;

            case R.id.nav_config:
                item.setChecked(true);
                startActivity(new Intent(DashBoardActivity.this, SettingsActivity.class));

                break;

            case R.id.nav_logout:

                logOut();

                SmartSharedPreferences.logoutCliente(getApplicationContext());
                startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
                finish();

                break;

            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {

            item.setChecked(true);
            SalesReceivedsFragment mSalesReceived = new SalesReceivedsFragment();

            android.support.v4.app.FragmentTransaction fragSalesReceived = getSupportFragmentManager().beginTransaction();
            fragSalesReceived.replace(R.id.fragment_container, mSalesReceived);
            fragSalesReceived.commit();

            HeaderTitle.setText(getResources().getString(R.string.txt_sales));

            return true;
        }

        if (id == R.id.action_search) {

            item.setChecked(true);

            HeaderTitle.setText(getResources().getString(R.string.txt_search_sales));

            SearchByLocationFragment searchFrag = new SearchByLocationFragment();

            android.support.v4.app.FragmentTransaction fragSearch = getSupportFragmentManager().beginTransaction();
            fragSearch.replace(R.id.fragment_container, searchFrag);
            fragSearch.commit();

        }

        return super.onOptionsItemSelected(item);
    }

    private void logOut(){

        if(FacebookSdk.isInitialized()){
            LoginManager.getInstance().logOut();
        }

    }


}
