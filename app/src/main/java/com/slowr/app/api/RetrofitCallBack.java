package com.slowr.app.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.slowr.app.components.ViewDialog;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.NoConnectivityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.slowr.app.utils.Constant.Msg_Something;
import static com.slowr.app.utils.Constant.SERVER_ERROR;
import static com.slowr.app.utils.Constant.msg_Somethingwentwrong;


public class RetrofitCallBack<T> implements Callback<T> {


    private static final String TAG = "RetrofitCallback";
    public Context mContext;
    public final Callback<T> mCallback;
    //    ProgressDialog dialog;
    ViewDialog viewDialog;
    boolean isLoad = false;

    public RetrofitCallBack(Context context, Callback<T> callback, boolean _isLoad) {
        mContext = context;
        this.mCallback = callback;
        isLoad = _isLoad;
        viewDialog = new ViewDialog((Activity) context);
//        dialog = new ProgressDialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setMessage("Please wait....");
//        dialog.setCancelable(false);
//        dialog.show();
        if (isLoad)
            viewDialog.showDialog();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        mCallback.onResponse(call, response);
        if (response.code() == SERVER_ERROR) {
//            Toast.makeText(mContext, Msg_Something, Toast.LENGTH_SHORT).show();
            Function.CustomMessage((Activity)mContext,Msg_Something);
        }
        if (isLoad)
            viewDialog.hideDialog();
//        if (dialog.isShowing())
//            dialog.dismiss();

//        Log.d("Api Response", new Gson().toJson(response));
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        mCallback.onFailure(call, t);
        Log.d("Api Response error", t.getMessage());
        if (t instanceof NoConnectivityException) {
//            Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
            Function.CustomMessage((Activity)mContext,msg_Somethingwentwrong);
//            call.clone().enqueue(this);
        }
        if (isLoad)
            viewDialog.hideDialog();


    }
}
