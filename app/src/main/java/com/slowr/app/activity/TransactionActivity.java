package com.slowr.app.activity;

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

public class TransactionActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt_page_title;
    LinearLayout img_back;
    RecyclerView rc_favorite;
    LinearLayout layout_no_result;
    InvoiceAdapter invoiceAdapter;
    ArrayList<InvoiceItemModel> invoiceList = new ArrayList<>();
    LinearLayoutManager listManager;
    HashMap<String, String> params = new HashMap<String, String>();
    private Function _fun = new Function();
    private PopupWindow spinnerPopup;

    String invoiceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        doDeclaration();
    }

    private void doDeclaration() {
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        rc_favorite = findViewById(R.id.rc_favorite);
        layout_no_result = findViewById(R.id.layout_no_result);

        listManager = new LinearLayoutManager(TransactionActivity.this, RecyclerView.VERTICAL, false);
        rc_favorite.setLayoutManager(listManager);
        rc_favorite.setItemAnimator(new DefaultItemAnimator());
        invoiceAdapter = new InvoiceAdapter(invoiceList, getApplicationContext());
        rc_favorite.setAdapter(invoiceAdapter);

        txt_page_title.setText(getString(R.string.transaction_history));
        img_back.setOnClickListener(this);
        if (_fun.isInternetAvailable(TransactionActivity.this)) {
            getInvoice();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(TransactionActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getInvoice();
                        }
                    });
                }
            }, 200);

        }

        CallBackFunction();
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

    private void getInvoice() {
        RetrofitClient.getClient().create(Api.class).getInvoice(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(TransactionActivity.this, adListResponse, true));

    }

    retrofit2.Callback<InvoiceModel> adListResponse = new retrofit2.Callback<InvoiceModel>() {
        @Override
        public void onResponse(Call<InvoiceModel> call, retrofit2.Response<InvoiceModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            InvoiceModel dr = response.body();
            try {
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
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
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

                .enqueue(new RetrofitCallBack(TransactionActivity.this, changePasswordApi, true));
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
}
