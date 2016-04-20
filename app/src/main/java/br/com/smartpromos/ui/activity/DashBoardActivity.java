package br.com.smartpromos.ui.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.smartpromos.R;
import br.com.smartpromos.ui.fragment.CanceledCouponsFragment;
import br.com.smartpromos.ui.fragment.SalesDiscardedFragment;
import br.com.smartpromos.ui.fragment.SalesFragment;
import br.com.smartpromos.ui.fragment.SalesRequestFragment;
import br.com.smartpromos.ui.fragment.SyncFragment;
import br.com.smartpromos.util.SmartSharedPreferences;

public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private TextView HeaderTitle;

    private Button btnActionOne;
    private Button btnActionTwo;
    private Button btnActionTree;

    private RelativeLayout containerSalesBtn;
    private RelativeLayout containerSearchBtn;
    private RelativeLayout containerCouponsBtn;

    private Button icoBtnSales;
    private TextView txtBtnSales;

    private Button icoBtnSearch;
    private TextView txtBtnSearch;

    private Button icoBtnCoupons;
    private TextView txtBtnCoupons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        HeaderTitle = (TextView) findViewById(R.id.HeaderTitle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        btnActionOne = (Button) findViewById(R.id.btnActionOne);
        btnActionTwo = (Button) findViewById(R.id.btnActionTwo);
        btnActionTree = (Button) findViewById(R.id.btnActionTree);

        containerSalesBtn = (RelativeLayout) findViewById(R.id.containerSalesBtn);
        containerSearchBtn = (RelativeLayout) findViewById(R.id.containerSearchBtn);
        containerCouponsBtn = (RelativeLayout) findViewById(R.id.containerCouponsBtn);

        icoBtnSales = (Button) findViewById(R.id.icoBtnSales);
        txtBtnSales = (TextView) findViewById(R.id.txtBtnSales);

        icoBtnSearch = (Button) findViewById(R.id.icoBtnSearch);
        txtBtnSearch = (TextView) findViewById(R.id.txtBtnSearch);

        icoBtnCoupons = (Button) findViewById(R.id.icoBtnCoupons);
        txtBtnCoupons = (TextView) findViewById(R.id.txtBtnCoupons);

        SalesFragment mfrag = new SalesFragment();

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

        btnActionOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(btnActionOne);
            }
        });

        btnActionTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(btnActionTwo);
            }
        });

        btnActionTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(btnActionTree);
            }
        });

    }

    public void changeToSales(View view){

        SalesFragment mfragSales = new SalesFragment();

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mfragSales);
        fragmentTransaction.commit();

        icoBtnSales.setEnabled(false);
        txtBtnSales.setTextColor(getResources().getColor(R.color.colorGreenBtn));

        icoBtnSearch.setEnabled(true);
        txtBtnSearch.setTextColor(getResources().getColor(R.color.colorBlack));

        icoBtnCoupons.setEnabled(true);
        txtBtnCoupons.setTextColor(getResources().getColor(R.color.colorBlack));

        containerSalesBtn.setBackground(getResources().getDrawable(R.drawable.border_top_green));
        containerSearchBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));
        containerCouponsBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));

        HeaderTitle.setText(getResources().getString(R.string.txt_sales));

        btnActionOne.setEnabled(false);
        btnActionOne.setTextColor(getResources().getColor(R.color.colorWhite));
        btnActionOne.setText("Recebidas");

        btnActionTwo.setEnabled(true);
        btnActionTwo.setTextColor(getResources().getColor(R.color.colorBlack));
        btnActionTwo.setText("Solicitadas");

        btnActionTree.setEnabled(true);
        btnActionTree.setTextColor(getResources().getColor(R.color.colorBlack));
        btnActionTree.setText("Descartadas");

    }

    public void changetToSearch(View view){
        SalesRequestFragment mfragSearch = new SalesRequestFragment();

        android.support.v4.app.FragmentTransaction fragSearchTransaction = getSupportFragmentManager().beginTransaction();
        fragSearchTransaction.replace(R.id.fragment_container, mfragSearch);
        fragSearchTransaction.commit();

        icoBtnSales.setEnabled(true);
        txtBtnSales.setTextColor(getResources().getColor(R.color.colorBlack));

        icoBtnSearch.setEnabled(false);
        txtBtnSearch.setTextColor(getResources().getColor(R.color.colorGreenBtn));

        icoBtnCoupons.setEnabled(true);
        txtBtnCoupons.setTextColor(getResources().getColor(R.color.colorBlack));

        containerSalesBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));
        containerSearchBtn.setBackground(getResources().getDrawable(R.drawable.border_top_green));
        containerCouponsBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));

        HeaderTitle.setText(getResources().getString(R.string.txt_search_sales));
    }

    public void changeToCoupons(View view){
        SalesDiscardedFragment mCouponfrag = new SalesDiscardedFragment();

        android.support.v4.app.FragmentTransaction fragCouponTransaction = getSupportFragmentManager().beginTransaction();
        fragCouponTransaction.replace(R.id.fragment_container, mCouponfrag);
        fragCouponTransaction.commit();

        icoBtnSales.setEnabled(true);
        txtBtnSales.setTextColor(getResources().getColor(R.color.colorBlack));

        icoBtnSearch.setEnabled(true);
        txtBtnSearch.setTextColor(getResources().getColor(R.color.colorBlack));

        icoBtnCoupons.setEnabled(false);
        txtBtnCoupons.setTextColor(getResources().getColor(R.color.colorGreenBtn));

        containerSalesBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));
        containerSearchBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));
        containerCouponsBtn.setBackground(getResources().getDrawable(R.drawable.border_top_green));

        HeaderTitle.setText(getResources().getString(R.string.txt_sales));

        btnActionOne.setEnabled(false);
        btnActionOne.setTextColor(getResources().getColor(R.color.colorWhite));
        btnActionOne.setText("Novos");

        btnActionTwo.setEnabled(true);
        btnActionTwo.setTextColor(getResources().getColor(R.color.colorBlack));
        btnActionTwo.setText("Usados");

        btnActionTree.setEnabled(true);
        btnActionTree.setTextColor(getResources().getColor(R.color.colorBlack));
        btnActionTree.setText("Expirados");
    }

    public void changeActivity(Button button){

        int btnId = button.getId();

        switch (btnId){

            case R.id.btnActionOne:

                if(button.getText().toString().equalsIgnoreCase("recebidas")){

                    SalesFragment mfrag = new SalesFragment();

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, mfrag);
                    fragmentTransaction.commit();

                    HeaderTitle.setText(getResources().getString(R.string.txt_sales));
                }else{

                }

                btnActionOne.setEnabled(false);
                btnActionOne.setTextColor(getResources().getColor(R.color.colorWhite));
                btnActionTwo.setEnabled(true);
                btnActionTwo.setTextColor(getResources().getColor(R.color.colorBlack));
                btnActionTree.setEnabled(true);
                btnActionTree.setTextColor(getResources().getColor(R.color.colorBlack));

                break;

            case R.id.btnActionTwo:

                if(button.getText().toString().equalsIgnoreCase("solicitadas")){

                    SalesRequestFragment mfrag = new SalesRequestFragment();

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, mfrag);
                    fragmentTransaction.commit();

                    HeaderTitle.setText(getResources().getString(R.string.txt_sales));

                }else{

                }

                btnActionOne.setEnabled(true);
                btnActionOne.setTextColor(getResources().getColor(R.color.colorBlack));
                btnActionTwo.setEnabled(false);
                btnActionTwo.setTextColor(getResources().getColor(R.color.colorWhite));
                btnActionTree.setEnabled(true);
                btnActionTree.setTextColor(getResources().getColor(R.color.colorBlack));

                break;

            case R.id.btnActionTree:

                if(button.getText().toString().equalsIgnoreCase("descartadas")){

                    SalesDiscardedFragment mfrag = new SalesDiscardedFragment();

                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, mfrag);
                    fragmentTransaction.commit();

                    HeaderTitle.setText(getResources().getString(R.string.txt_sales));

                }else{

                }

                btnActionOne.setEnabled(true);
                btnActionOne.setTextColor(getResources().getColor(R.color.colorBlack));
                btnActionTwo.setEnabled(true);
                btnActionTwo.setTextColor(getResources().getColor(R.color.colorBlack));
                btnActionTree.setEnabled(false);
                btnActionTree.setTextColor(getResources().getColor(R.color.colorWhite));

                break;

            default:
                break;
        }

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

                startActivity(new Intent(DashBoardActivity.this, ProfileActivity.class));

                break;
            case R.id.nav_locale:

                startActivity(new Intent(DashBoardActivity.this, LocaleCustomerActivity.class));

                break;

            case R.id.nav_sales_received:

                HeaderTitle.setText(getResources().getString(R.string.txt_sales));

                icoBtnSales.setEnabled(false);
                txtBtnSales.setTextColor(getResources().getColor(R.color.colorGreenBtn));

                icoBtnSearch.setEnabled(true);
                txtBtnSearch.setTextColor(getResources().getColor(R.color.colorBlack));

                icoBtnCoupons.setEnabled(true);
                txtBtnCoupons.setTextColor(getResources().getColor(R.color.colorBlack));

                containerSalesBtn.setBackground(getResources().getDrawable(R.drawable.border_top_green));
                containerSearchBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));
                containerCouponsBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));

                btnActionOne.setEnabled(false);
                btnActionOne.setTextColor(getResources().getColor(R.color.colorWhite));
                btnActionOne.setText("Recebidas");

                btnActionTwo.setEnabled(true);
                btnActionTwo.setTextColor(getResources().getColor(R.color.colorBlack));
                btnActionTwo.setText("Solicitadas");

                btnActionTree.setEnabled(true);
                btnActionTree.setTextColor(getResources().getColor(R.color.colorBlack));
                btnActionTree.setText("Descartadas");

                break;

            case R.id.nav_sale_search:


                HeaderTitle.setText(getResources().getString(R.string.txt_search_sales));

                break;

            case R.id.nav_my_coupons:

                icoBtnSales.setEnabled(true);
                txtBtnSales.setTextColor(getResources().getColor(R.color.colorBlack));

                icoBtnSearch.setEnabled(true);
                txtBtnSearch.setTextColor(getResources().getColor(R.color.colorBlack));

                icoBtnCoupons.setEnabled(false);
                txtBtnCoupons.setTextColor(getResources().getColor(R.color.colorGreenBtn));

                containerSalesBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));
                containerSearchBtn.setBackground(getResources().getDrawable(R.drawable.border_top_white));
                containerCouponsBtn.setBackground(getResources().getDrawable(R.drawable.border_top_green));

                btnActionOne.setEnabled(false);
                btnActionOne.setTextColor(getResources().getColor(R.color.colorWhite));
                btnActionOne.setText("Novos");

                btnActionTwo.setEnabled(true);
                btnActionTwo.setTextColor(getResources().getColor(R.color.colorBlack));
                btnActionTwo.setText("Usados");

                btnActionTree.setEnabled(true);
                btnActionTree.setTextColor(getResources().getColor(R.color.colorBlack));
                btnActionTree.setText("Expirados");

                HeaderTitle.setText(getResources().getString(R.string.txt_coupons));

                break;

            case R.id.nav_coupons_canceled:

                HeaderTitle.setText(getResources().getString(R.string.txt_canceled_coupons));

                CanceledCouponsFragment mfrag = new CanceledCouponsFragment();

                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, mfrag);
                fragmentTransaction.commit();

                break;

            /*case R.id.nav_sync:

                HeaderTitle.setText(getResources().getString(R.string.txt_sync));

                SyncFragment fragSync = new SyncFragment();

                android.support.v4.app.FragmentTransaction fragmentTransactionSync = getSupportFragmentManager().beginTransaction();
                fragmentTransactionSync.replace(R.id.fragment_container, fragSync);
                fragmentTransactionSync.commit();

                break;*/

            case R.id.nav_tutorial:

                HeaderTitle.setText(getResources().getString(R.string.txt_tutorial));

                break;

            case R.id.nav_config:

                startActivity(new Intent(DashBoardActivity.this, SettingsActivity.class));

                break;

            case R.id.nav_logout:
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
}
