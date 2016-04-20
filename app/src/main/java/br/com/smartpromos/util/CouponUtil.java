package br.com.smartpromos.util;


import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.ListaCuponsResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paulo on 20/04/2016.
 */
public class CouponUtil {


    private static ListaCuponsResponse listaCupons;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    public static ListaCuponsResponse getCupons(int status, ClienteResponse cliente){

        smartRepo.cuponsByEmailAndStatus(cliente.getEmail(), status, new Callback<ListaCuponsResponse>() {
            @Override
            public void success(ListaCuponsResponse listaCuponsResponse, Response response) {

                listaCupons = listaCuponsResponse;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        return listaCupons;

    }

}
