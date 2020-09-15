package com.slowr.app.activity;

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
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.HashMap;

import retrofit2.Call;

public class ChangPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_page_title;
    LinearLayout img_back;
    EditText edt_old_password;
    EditText edt_new_password;
    EditText edt_new_confirm_password;
    Button btn_send_password;
    TextInputLayout til_old_password;
    TextInputLayout til_new_password;
    TextInputLayout til_new_confirm_password;

    HashMap<String, String> params = new HashMap<String, String>();
    private Function _fun = new Function();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_password);
        doDeclaration();
    }

    private void doDeclaration() {
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        edt_old_password = findViewById(R.id.edt_old_password);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_new_confirm_password = findViewById(R.id.edt_new_confirm_password);
        btn_send_password = findViewById(R.id.btn_send_password);
        til_old_password = findViewById(R.id.til_old_password);
        til_new_password = findViewById(R.id.til_new_password);
        til_new_confirm_password = findViewById(R.id.til_new_confirm_password);

        txt_page_title.setText(getString(R.string.change_password));

        btn_send_password.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_password:
                if (_fun.isInternetAvailable(ChangPasswordActivity.this)) {
                    changePassword();
                } else {
                    _fun.ShowNoInternetPopup(ChangPasswordActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            changePassword();
                        }
                    });
                }

                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void changePassword() {

        String oldPassword = edt_old_password.getText().toString().trim();
        String password = edt_new_password.getText().toString().trim();
        String confirmPassword = edt_new_confirm_password.getText().toString().trim();

        if (oldPassword.isEmpty()) {
            til_old_password.setError(getString(R.string.old_password_required));
            til_old_password.requestFocus();
            return;
        } else {
            til_old_password.setErrorEnabled(false);
        }

        if (oldPassword.length() < 6) {
            til_old_password.setError(getString(R.string.password_empty_alert));
            til_old_password.requestFocus();
            return;
        } else {
            til_old_password.setErrorEnabled(false);
        }

        if (password.isEmpty()) {
            til_new_password.setError(getString(R.string.password_required));
            til_new_password.requestFocus();
            return;
        } else {
            til_new_password.setErrorEnabled(false);
        }

        if (password.length() < 6) {
            til_new_password.setError(getString(R.string.password_empty_alert));
            til_new_password.requestFocus();
            return;
        } else {
            til_new_password.setErrorEnabled(false);
        }
        if (confirmPassword.isEmpty()) {
            til_new_confirm_password.setError(getString(R.string.confirm_password_required));
            til_new_confirm_password.requestFocus();
            return;
        } else {
            til_new_confirm_password.setErrorEnabled(false);
        }

        if (!confirmPassword.equals(password)) {
            til_new_confirm_password.setError(getString(R.string.password_new_retype_same));
            til_new_confirm_password.requestFocus();
            return;
        } else {
            til_new_confirm_password.setErrorEnabled(false);
        }

        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("current_password", oldPassword);
        params.put("new_password", password);
        Log.i("params", params.toString());


        RetrofitClient.getClient().create(Api.class).changePassword(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(ChangPasswordActivity.this, changePasswordApi, true));
    }

    retrofit2.Callback<DefaultResponse> changePasswordApi = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    Function.CustomMessage(ChangPasswordActivity.this, dr.getMessage());
                    finish();
                } else {
                    Function.CustomMessage(ChangPasswordActivity.this, dr.getMessage());
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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
