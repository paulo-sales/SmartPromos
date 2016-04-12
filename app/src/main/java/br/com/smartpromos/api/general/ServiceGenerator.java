package br.com.smartpromos.api.general;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

public class ServiceGenerator {

    private ServiceGenerator() {

    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, int timeoutInSeconds) {

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(timeoutInSeconds, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(timeoutInSeconds, TimeUnit.SECONDS);

        RequestInterceptor requestInterceptor = new RequestInterceptor()
        {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Token", "SmartPromos");
                request.addHeader("User-Agent", "Android");
            }
        };


        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("RETROFIT_LOG"))
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(okHttpClient));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);

    }

}
