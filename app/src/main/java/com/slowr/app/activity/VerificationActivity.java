package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.slowr.app.R;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.GSTModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.HashMap;

import retrofit2.Call;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_page_title;
    LinearLayout img_back;

    Button btn_individual;
    Button btn_corporate;
    LinearLayout layout_input_individual;
    LinearLayout layout_input_corporate;
    EditText edt_company_name;
    EditText edt_gst_no;
    EditText edt_aadhaar_name;
    EditText edt_aadhaar_no;
    Button btn_verify;
    Button btn_cancel;
    Button btn_home_page;
    TextInputLayout til_company_name;
    TextInputLayout til_gst_no;
    LinearLayout layout_root;
    LinearLayout layout_success;

    int tabNo = 1;

    HashMap<String, String> params = new HashMap<String, String>();
    private Function _fun = new Function();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        doDeclaration();
    }

    private void doDeclaration() {
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        btn_individual = findViewById(R.id.btn_individual);
        btn_corporate = findViewById(R.id.btn_corporate);
        layout_input_individual = findViewById(R.id.layout_input_individual);
        layout_input_corporate = findViewById(R.id.layout_input_corporate);
        btn_verify = findViewById(R.id.btn_verify);
        btn_cancel = findViewById(R.id.btn_cancel);
        edt_company_name = findViewById(R.id.edt_company_name);
        edt_gst_no = findViewById(R.id.edt_gst_no);
        til_gst_no = findViewById(R.id.til_gst_no);
        til_company_name = findViewById(R.id.til_company_name);
        edt_aadhaar_name = findViewById(R.id.edt_aadhaar_name);
        edt_aadhaar_no = findViewById(R.id.edt_aadhaar_no);
        layout_root = findViewById(R.id.layout_root);
        layout_success = findViewById(R.id.layout_success);
        btn_home_page = findViewById(R.id.btn_home_page);

        txt_page_title.setText(getString(R.string.profile_verification));

        img_back.setOnClickListener(this);
        btn_individual.setOnClickListener(this);
        btn_corporate.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_home_page.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_individual:
                if (tabNo != 1) {
                    tabNo = 1;
                    btn_individual.setBackground(getResources().getDrawable(R.drawable.bg_left_orenge));
                    btn_corporate.setBackground(getResources().getDrawable(R.drawable.bg_orenge_border_right));
                    btn_individual.setTextColor(getResources().getColor(R.color.color_white));
                    btn_corporate.setTextColor(getResources().getColor(R.color.txt_orange));
                    layout_input_individual.setVisibility(View.VISIBLE);
                    layout_input_corporate.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_corporate:
                if (tabNo != 2) {
                    tabNo = 2;
                    btn_individual.setBackground(getResources().getDrawable(R.drawable.bg_orenge_border_left));
                    btn_corporate.setBackground(getResources().getDrawable(R.drawable.bg_right_orenge));
                    btn_individual.setTextColor(getResources().getColor(R.color.txt_orange));
                    btn_corporate.setTextColor(getResources().getColor(R.color.color_white));
                    layout_input_individual.setVisibility(View.GONE);
                    layout_input_corporate.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_verify:
                doValidation();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_home_page:
                finish();
                break;
        }
    }

    private void doValidation() {
        String companyName = edt_company_name.getText().toString();
        final String gstNo = edt_gst_no.getText().toString();
        if (tabNo == 2) {
            if (companyName.length() == 0) {
                til_company_name.setError(getString(R.string.enter_company_name));
                til_company_name.requestFocus();
                return;
            } else {
                til_company_name.setErrorEnabled(false);
            }
            if (gstNo.length() == 0) {
                til_gst_no.setError(getString(R.string.enter_gst_no));
                til_gst_no.requestFocus();
                return;
            } else {
                til_gst_no.setErrorEnabled(false);
            }
            if (_fun.isInternetAvailable(VerificationActivity.this)) {
                verifyGSTNO(gstNo);
            } else {
                _fun.ShowNoInternetPopup(VerificationActivity.this, new Function.NoInternetCallBack() {
                    @Override
                    public void isInternet() {
                        verifyGSTNO(gstNo);
                    }
                });
            }

        }
    }

    private void verifyGSTNO(String gstNo) {
        RetrofitClient.getClientGST().create(Api.class).verifyGST(gstNo, Constant.Authorization, Constant.Content_Type, Constant.ClientId)
                .enqueue(new RetrofitCallBack(VerificationActivity.this, gstResponse, true));
    }

    retrofit2.Callback<GSTModel> gstResponse = new retrofit2.Callback<GSTModel>() {
        @Override
        public void onResponse(Call<GSTModel> call, retrofit2.Response<GSTModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());

            try {
                GSTModel dr = response.body();
                if (dr.isError()) {
                    Function.CustomMessage(VerificationActivity.this,"GST Verification failed");
                } else {

                    if (_fun.isInternetAvailable(VerificationActivity.this)) {
                        saveVerification();
                    } else {
                        _fun.ShowNoInternetPopup(VerificationActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                saveVerification();
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };


    private void saveVerification() {
        if (!params.isEmpty()) {
            params.clear();
        }

        params.put("is_verified", String.valueOf(tabNo));
        if (tabNo == 1) {
            params.put("aadhar", edt_aadhaar_no.getText().toString());
        } else {
            params.put("gst", edt_gst_no.getText().toString());
        }
        Log.i("params", params.toString());

        RetrofitClient.getClient().create(Api.class).GSTVerificationSave(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(VerificationActivity.this, sumbitVerification, true));

    }

    retrofit2.Callback<DefaultResponse> sumbitVerification = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
//                    finish();
                    layout_root.setVisibility(View.GONE);
                    layout_success.setVisibility(View.VISIBLE);
                } else {
                    Function.CustomMessage(VerificationActivity.this,dr.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };
}
