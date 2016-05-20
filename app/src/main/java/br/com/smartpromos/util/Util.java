package br.com.smartpromos.util;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;

import br.com.smartpromos.R;
import br.com.smartpromos.smartpromosapplication.SmartPromosApp;

/**
 * Created by Paulo on 18/05/2016.
 */
public class Util {

    public static boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) SmartPromosApp.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static void showNetworkInfo(View view, final Context context){

        final Snackbar snackbar = Snackbar
                .make(view, SmartPromosApp.context.getResources().getString(R.string.snack_alert_no_connection), Snackbar.LENGTH_LONG)
                .setActionTextColor(SmartPromosApp.context.getResources().getColor(R.color.colorPressedRed))
                .setAction("CONECTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        context.startActivity(intent);

                    }
                });

        snackbar.show();

    }


    public static void showLocationInfo(View view, final Context context){

        final Snackbar snackbar = Snackbar
                .make(view, SmartPromosApp.context.getResources().getString(R.string.snack_alert_no_location), Snackbar.LENGTH_LONG)
                .setActionTextColor(SmartPromosApp.context.getResources().getColor(R.color.colorPressedRed))
                .setAction("CONECTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        context.startActivity(intent);

                    }
                });

        snackbar.show();

    }

    public static boolean isLocationEnabled(){

        LocationManager locationManager = (LocationManager) SmartPromosApp.context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

}
