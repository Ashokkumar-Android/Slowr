package com.slowr.app.utils;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ashok on 10/28/2017.
 */

public class ConnectivityInterceptor implements Interceptor {

    private Context mContext;

    public ConnectivityInterceptor() {
    }

    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtility.isOnline()) {
            throw new NoConnectivityException();
        }
        Request.Builder builder = chain.request().newBuilder();//.addHeader("Cache-Control", "no-cache");
        return chain.proceed(builder.build());
    }
}
