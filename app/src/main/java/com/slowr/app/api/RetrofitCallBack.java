package com.slowr.app.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.slowr.app.R;
import com.slowr.app.activity.HomeActivity;
import com.slowr.app.components.ViewDialog;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.NoConnectivityException;
import com.slowr.app.utils.Sessions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.slowr.app.utils.Constant.Msg_Something;
import static com.slowr.app.utils.Constant.SERVER_ERROR;
import static com.slowr.app.utils.Constant.Unauthorized_Error;
import static com.slowr.app.utils.Constant.msg_Somethingwentwrong;


public class RetrofitCallBack<T> implements Callback<T> {


    private static final String TAG = "RetrofitCallback";
    public Context mContext;
    public final Callback<T> mCallback;
    //    ProgressDialog dialog;
    ViewDialog viewDialog;
    boolean isLoad = false;

    public RetrofitCallBack(Context context, Callback<T> callback, boolean _isLoad, boolean _isCancel) {
        mContext = context;
        this.mCallback = callback;
        isLoad = _isLoad;
        viewDialog = new ViewDialog((Activity) context);
//        dialog = new ProgressDialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setMessage("Please wait....");
//        dialog.setCancelable(false);
//        dialog.show();

        if (isLoad){
            viewDialog.showDialog();
            viewDialog.cancelableDialog(_isCancel);
        }

    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        mCallback.onResponse(call, response);
        if (response.code() == SERVER_ERROR) {
//            Toast.makeText(mContext, Msg_Something, Toast.LENGTH_SHORT).show();
            Function.CustomMessage((Activity) mContext, Msg_Something);
        } else if (response.code() == Unauthorized_Error) {
            String cityId = Sessions.getSession(Constant.CityId, mContext);
            String cityName = Sessions.getSession(Constant.CityName, mContext);

            Sessions.clearSession(mContext);
            Sessions.saveSession(Constant.CityId, cityId, mContext);
            Sessions.saveSession(Constant.CityName, cityName, mContext);
            Sessions.saveSessionBool(Constant.IsFirst, true, mContext);
            Intent h = new Intent(mContext, HomeActivity.class);
            h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(h);
        }
        if (isLoad)
            viewDialog.hideDialog();
//        if (dialog.isShowing())
//            dialog.dismiss();

//        Log.d("Api Response", new Gson().toJson(response));
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

        Log.d("Api Response error", t.getMessage());
        if (t instanceof NoConnectivityException) {
//            Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
//            Function.CustomMessage((Activity) mContext, mContext.getString(R.string.txt_poor_connection));
            call.clone().enqueue(this);
            Log.d("NoConnection", t.getMessage());
        }else {
            mCallback.onFailure(call, t);
        }
        if (isLoad)
            viewDialog.hideDialog();


    }
}
