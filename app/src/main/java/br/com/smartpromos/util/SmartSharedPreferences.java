package br.com.smartpromos.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import br.com.smartpromos.api.general.response.ClienteResponse;

/**
 * Created by Paulo on 13/04/2016.
 */
public class SmartSharedPreferences {

    private static final String PREFERENCIAS = "SMATPROMOS_PREFS";

    private static final String TAG = "gcm";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String USER = "user";

    // Preferências para salvar o registration id
    private static android.content.SharedPreferences getPreferences(Context context) {
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    // Salva o usuario complesto
    public static boolean gravarUsuarioResponseCompleto(Context context,  ClienteResponse clienteResponse) {

        final android.content.SharedPreferences prefs = getPreferences(context);
        final android.content.SharedPreferences.Editor editor = prefs.edit();

        String persistence = new Gson().toJson(clienteResponse, ClienteResponse.class);

        return editor.putString(USER, persistence).commit();

    }

    // Retorna o usuario completo
    public static ClienteResponse getUsuarioCompleto(Context context) {
        final android.content.SharedPreferences prefs = getPreferences(context);
        String user = prefs.getString(USER, "");
        ClienteResponse clienteResponse = new Gson().fromJson(user, ClienteResponse.class);
        return clienteResponse;
    }

}
