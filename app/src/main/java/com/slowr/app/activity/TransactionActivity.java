package com.slowr.app.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.slowr.app.R;
import com.slowr.app.adapter.InvoiceAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.InvoiceItemModel;
import com.slowr.app.models.InvoiceModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    TextView txt_page_title;
    LinearLayout img_back;
    RecyclerView rc_favorite;
    LinearLayout layout_no_result;
    InvoiceAdapter invoiceAdapter;
    ArrayList<InvoiceItemModel> invoiceList = new ArrayList<>();
    LinearLayoutManager listManager;
    SwipeRefreshLayout layout_swipe_refresh;
    Button btn_continue;
    HashMap<String, String> params = new HashMap<String, String>();
    private Function _fun = new Function();
    private PopupWindow spinnerPopup;

    String invoiceId = "";
    String PageFrom = "";
    String NotificationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        doDeclaration();
    }

    private void doDeclaration() {
        if (getIntent().hasExtra("PageFrom")) {
            PageFrom = getIntent().getStringExtra("PageFrom");
            NotificationId = getIntent().getStringExtra("NotificationId");
            NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            ReadNotification(NotificationId);
        }
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        rc_favorite = findViewById(R.id.rc_favorite);
        layout_no_result = findViewById(R.id.layout_no_result);
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);
        btn_continue = findViewById(R.id.btn_continue);
        listManager = new LinearLayoutManager(TransactionActivity.this, RecyclerView.VERTICAL, false);
        rc_favorite.setLayoutManager(listManager);
        rc_favorite.setItemAnimator(new DefaultItemAnimator());
        invoiceAdapter = new InvoiceAdapter(invoiceList, getApplicationContext());
        rc_favorite.setAdapter(invoiceAdapter);
        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        layout_swipe_refresh.setOnRefreshListener(this);
        txt_page_title.setText(getString(R.string.transaction_history));
        img_back.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        if (_fun.isInternetAvailable(TransactionActivity.this)) {
            getInvoice(true);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(TransactionActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getInvoice(true);
                        }
                    });
                }
            }, 200);

        }

        CallBackFunction();
    }

    private void ReadNotification(String noteId) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("notification_id", noteId);
        Log.i("Params", params.toString());

        if (_fun.isInternetAvailable(TransactionActivity.this)) {
            RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(TransactionActivity.this, noteReadResponse, false,false));
        } else {
            _fun.ShowNoInternetPopup(TransactionActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(TransactionActivity.this, noteReadResponse, false,false));
                }
            });
        }
    }

    private void CallBackFunction() {
        invoiceAdapter.setCallback(new InvoiceAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                invoiceId = invoiceList.get(pos).getInvoiceId();
                ShowPopupProsper();
            }
        });
    }

    private void getInvoice(boolean isLoad) {
        RetrofitClient.getClient().create(Api.class).getInvoice(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(TransactionActivity.this, adListResponse, isLoad,false));

    }

    retrofit2.Callback<InvoiceModel> adListResponse = new retrofit2.Callback<InvoiceModel>() {
        @Override
        public void onResponse(Call<InvoiceModel> call, retrofit2.Response<InvoiceModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            InvoiceModel dr = response.body();
            try {
                if (layout_swipe_refresh.isRefreshing()) {
                    layout_swipe_refresh.setRefreshing(false);
                }
                if (dr.isStatus()) {

//                    Log.i("ImagePathUrl", dr.getUrlPath());
//                    Sessions.saveSession(Constant.ImagePath, dr.getUrlPath(), FavoriteActivity.this);

                    invoiceList.clear();
                    invoiceList.addAll(dr.getInvoiceItemModels());
                    invoiceAdapter.notifyDataSetChanged();
                    if (invoiceList.size() == 0) {
                        layout_no_result.setVisibility(View.VISIBLE);
                        rc_favorite.setVisibility(View.GONE);
                    } else {
                        layout_no_result.setVisibility(View.GONE);
                        rc_favorite.setVisibility(View.VISIBLE);
                    }
                } else {
                    invoiceList.clear();
                    invoiceAdapter.notifyDataSetChanged();
                    if (invoiceList.size() == 0) {
                        layout_no_result.setVisibility(View.VISIBLE);
                        rc_favorite.setVisibility(View.GONE);
                    } else {
                        layout_no_result.setVisibility(View.GONE);
                        rc_favorite.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                layout_no_result.setVisibility(View.VISIBLE);
                rc_favorite.setVisibility(View.GONE);
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            layout_no_result.setVisibility(View.VISIBLE);
            rc_favorite.setVisibility(View.GONE);
            call.cancel();
            if (layout_swipe_refresh.isRefreshing()) {
                layout_swipe_refresh.setRefreshing(false);
            }
        }
    };
    retrofit2.Callback<DefaultResponse> noteReadResponse = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            DefaultResponse dr = response.body();
            try {
                if (dr.isStatus()) {
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                if (PageFrom.equals("2")) {
                    Intent h = new Intent(TransactionActivity.this, HomeActivity.class);
                    h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(h);
                }
                finish();
                break;
            case R.id.btn_continue:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (PageFrom.equals("2")) {
            Intent h = new Intent(TransactionActivity.this, HomeActivity.class);
            h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(h);
        }
        finish();
        super.onBackPressed();
    }

    public void ShowPopupProsper() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_sent_invoice, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        final EditText edt_email = view.findViewById(R.id.edt_email);
        final TextInputLayout til_email = view.findViewById(R.id.til_email);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        edt_email.setText(Sessions.getSession(Constant.UserEmail, getApplicationContext()));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edt_email.getText().toString().trim();
                if (email.isEmpty()) {
                    til_email.setError("Email is required");
                    til_email.requestFocus();
                    return;
                } else {
                    til_email.setErrorEnabled(false);
                }
                if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    til_email.setError(getString(R.string.enter_email));
                    til_email.requestFocus();
                    return;
                } else {
                    til_email.setErrorEnabled(false);
                }
                spinnerPopup.dismiss();

                if (_fun.isInternetAvailable(TransactionActivity.this)) {
                    sendInvoiceMail(email);
                } else {
                    _fun.ShowNoInternetPopup(TransactionActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            sendInvoiceMail(email);
                        }
                    });

                }

            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void sendInvoiceMail(String mail) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("invoice_id", invoiceId);
        params.put("email", mail);
        Log.i("params", params.toString());


        RetrofitClient.getClient().create(Api.class).sendInvoice(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))

                .enqueue(new RetrofitCallBack(TransactionActivity.this, changePasswordApi, true,false));
    }

    retrofit2.Callback<DefaultResponse> changePasswordApi = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    Function.CustomMessage(TransactionActivity.this, dr.getMessage());
//                    finish();
                } else {
                    Function.CustomMessage(TransactionActivity.this, dr.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };

    @Override
    public void onRefresh() {
        Function.CoinTone(TransactionActivity.this);
        if (_fun.isInternetAvailable(TransactionActivity.this)) {
            getInvoice(false);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(TransactionActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getInvoice(false);
                        }
                    });
                }
            }, 200);

        }
    }
}
