package com.slowr.app.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slowr.app.utils.ConnectivityInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //*\..........Devlapement...........\*
    private static final String BASE_URL = "https://dev.slowr.in/slowrapi/public/api/v1/";
    //*\..........Test...........\*
    private static final String BASE_URL_TEST = "https://test.slowr.in/slowrapi/public/api/v1/";

    //*\..........Production...........\*
    private static final String BASE_URL_PRODUCTION = "https://slowr.com/slowrapi/public/api/v1/";

    //*\..........Local...........\*
    private static final String BASE_URL_LOCAL = "http://192.168.0.105/slowr/slowrapi/public/api/v1/";

    //*\.........GST..........\*
    private static final String BASE_URL_GST = "https://commonapi.mastersindia.co/commonapis/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofitGST = null;

    static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new LogJsonInterceptor())
            .cache(null)
            .addInterceptor(new ConnectivityInterceptor())
            .readTimeout(180, TimeUnit.SECONDS)
            .connectTimeout(180, TimeUnit.SECONDS)
            .build();
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_TEST)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getClientGST() {
        if (retrofitGST == null) {
            retrofitGST = new Retrofit.Builder()
                    .baseUrl(BASE_URL_GST)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitGST;
    }
}
 