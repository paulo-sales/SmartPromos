package br.com.smartpromos.smartpromosapplication;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Paulo on 06/04/2016.
 */
public class SmartPromosApp extends MultiDexApplication {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

}