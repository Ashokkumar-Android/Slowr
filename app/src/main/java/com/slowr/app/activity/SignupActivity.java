package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.slowr.app.R;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.components.otpview.OnOtpCompletionListener;
import com.slowr.app.components.otpview.OtpView;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.LoginResponse;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.HashMap;

import retrofit2.Call;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText edt_name;
    TextInputEditText edt_phone_number;
    TextInputEditText edt_email;
    TextInputEditText edt_password;
    TextInputEditText edt_confirm_password;
    TextInputLayout til_edt_name;
    TextInputLayout til_mobile_number;
    TextInputLayout til_email;
    TextInputLayout til_password;
    TextInputLayout til_confirm_password;
    LinearLayout layout_register;
    LinearLayout layout_otp;
    Button btn_sign_up;
    TextView txt_privacy_policy;
    TextView txt_login;
    ImageView img_back;
    TextView btn_sent_otp;
    Button btn_verify_otp;
    OtpView edt_otp;

    int resentCount = 0;
    boolean isOTPView = false;

    PopupWindow popupWindow;

    HashMap<String, String> params = new HashMap<String, String>();
    private Function _fun = new Function();
    String otp = "";
    String fieldType = "";
    String fieldValue = "";
    String fbToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        doDeclaration();
    }

    private void doDeclaration() {
        fieldType = getIntent().getStringExtra("field");
        fieldValue = getIntent().getStringExtra("value");
        edt_name = findViewById(R.id.edt_name);
        edt_phone_number = findViewById(R.id.edt_phone_number);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        til_edt_name = findViewById(R.id.til_edt_name);
        til_mobile_number = findViewById(R.id.til_mobile_number);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        til_confirm_password = findViewById(R.id.til_confirm_password);
        txt_privacy_policy = findViewById(R.id.txt_privacy_policy);
        txt_login = findViewById(R.id.txt_login);
        img_back = findViewById(R.id.img_back);
        btn_sent_otp = findViewById(R.id.txt_resend_otp);
        btn_verify_otp = findViewById(R.id.btn_verify_otp);
        edt_otp = findViewById(R.id.otp_view);
        layout_register = findViewById(R.id.layout_register);
        layout_otp = findViewById(R.id.layout_otp);

        btn_sign_up.setOnClickListener(this);
        txt_login.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_sent_otp.setOnClickListener(this);
        btn_verify_otp.setOnClickListener(this);
        edt_otp.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String _otp) {
                otp = _otp;
            }
        });
        TCPPTextView(txt_privacy_policy);
        CheckError();
        if (fieldType.equals("mobile")) {
            edt_phone_number.setText(fieldValue);
        } else {
            edt_email.setText(fieldValue);
        }
        GetDeviceId();
    }

    private void GetDeviceId() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SignupActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                fbToken = newToken;
                Log.e("newToken", newToken);

            }
        });
    }

    private void CheckError() {
        edt_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = edt_phone_number.getText().toString().length();
                if (count == 10) {
                    if (til_mobile_number.isErrorEnabled()) {
                        til_mobile_number.setErrorEnabled(false);
                    }
                }
            }
        });
        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int valCount = edt_name.getText().toString().length();
                if (valCount != 0) {
                    til_edt_name.setErrorEnabled(false);
                }
            }
        });
//        edt_email.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                int count = edt_email.getText().toString().length();
//                if (count != 0) {
//                    til_password.setHint(getString(R.string.enter_your_password_star));
//                    til_confirm_password.setHint(getString(R.string.enter_your_confirm_password_star));
//                } else {
//                    til_password.setHint(getString(R.string.enter_your_password));
//                    til_confirm_password.setHint(getString(R.string.enter_your_confirm_password));
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                doUserSignUp();
                break;
            case R.id.txt_login:
                finish();
                break;
            case R.id.img_back:
                if (isOTPView) {
                    isOTPView = false;
                    layout_register.setVisibility(View.VISIBLE);
                    layout_otp.setVisibility(View.GONE);
                } else {
//                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
//                    startActivity(i);
                    finish();
                }
                break;
            case R.id.txt_resend_otp:
                if (resentCount != 2) {
                    resentCount++;
                    reSendOTP();
                    edt_otp.setText("");
                } else {
                    Intent i = new Intent(SignupActivity.this, ReportUsActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.btn_verify_otp:
                if (otp.length() == 0) {
                    Function.CustomMessage(SignupActivity.this,"Enter OTP");
                } else {
                    verifyOTP(otp);
                }
                break;
        }
    }

    private void doUserSignUp() {
        resentCount = 0;

        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String confirmPassword = edt_confirm_password.getText().toString().trim();
        String name = edt_name.getText().toString().trim();
        String phone = edt_phone_number.getText().toString().trim();

//        if (email.isEmpty()) {
//            edt_email.setError("Email is required");
//            edt_email.requestFocus();
//            return;
//        }
        if (name.isEmpty()) {
            til_edt_name.setError(getString(R.string.enter_name));
            til_edt_name.requestFocus();
            return;
        } else {
            til_edt_name.setErrorEnabled(false);
        }
        if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            til_email.setError(getString(R.string.enter_email));
            til_email.requestFocus();
            return;
        } else {
            til_email.setErrorEnabled(false);
        }

        if (phone.isEmpty()) {
            til_mobile_number.setError(getString(R.string.enter_phone_number));
            til_mobile_number.requestFocus();
            return;
        } else {
            til_mobile_number.setErrorEnabled(false);
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            til_mobile_number.setError(getString(R.string.enter_valid_phone_number));
            til_mobile_number.requestFocus();
            return;
        } else {
            til_mobile_number.setErrorEnabled(false);
        }
        if (phone.length() < 10) {
            til_mobile_number.setError(getString(R.string.enter_valid_phone_number));
            til_mobile_number.requestFocus();
            return;
        } else {
            til_mobile_number.setErrorEnabled(false);
        }

        if (password.isEmpty()) {
            til_password.setError(getString(R.string.enter_password));
            til_password.requestFocus();
            return;
        } else {
            til_password.setErrorEnabled(false);
        }

        if (password.length() < 6) {
            til_password.setError(getString(R.string.password_empty_alert));
            til_password.requestFocus();
            return;
        } else {
            til_password.setErrorEnabled(false);
        }
        if (confirmPassword.isEmpty()) {
            til_confirm_password.setError(getString(R.string.enter_retype_password));
            til_confirm_password.requestFocus();
            return;
        } else {
            til_confirm_password.setErrorEnabled(false);
        }

        if (!confirmPassword.equals(password)) {
            til_confirm_password.setError(getString(R.string.password_retype_password_alert));
            til_confirm_password.requestFocus();
            return;
        } else {
            til_confirm_password.setErrorEnabled(false);
        }

        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("email", email);
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(SignupActivity.this)) {
            RetrofitClient.getClient().create(Api.class).emailPhoneRegistration(params)
                    .enqueue(new RetrofitCallBack(SignupActivity.this, emailPhoneValidate, true));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).emailPhoneRegistration(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, emailPhoneValidate, true));
                }
            });
        }

    }

    private void sendOTP() {
        String phone = edt_phone_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("status", "2");
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(SignupActivity.this)) {
            RetrofitClient.getClient().create(Api.class).sendOTP(params)
                    .enqueue(new RetrofitCallBack(SignupActivity.this, sendOTP, true));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).sendOTP(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, sendOTP, true));
                }
            });
        }
    }

    private void reSendOTP() {


        String phone = edt_phone_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);

        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(SignupActivity.this)) {
            RetrofitClient.getClient().create(Api.class).reSendOTP(params)
                    .enqueue(new RetrofitCallBack(SignupActivity.this, reSendOTP, true));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).reSendOTP(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, reSendOTP, true));
                }
            });
        }

    }

    private void verifyOTP(String otp) {
        String phone = edt_phone_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("otp", otp);

        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(SignupActivity.this)) {
            RetrofitClient.getClient().create(Api.class).verifyOTP(params)
                    .enqueue(new RetrofitCallBack(SignupActivity.this, verifyOTP, true));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).verifyOTP(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, verifyOTP, true));
                }
            });
        }
    }

    private void Register() {
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String confirmPassword = edt_confirm_password.getText().toString().trim();
        String name = edt_name.getText().toString().trim();
        String phone = edt_phone_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("password", password);
        params.put("password_confirmation", password);
        params.put("fcm_token", fbToken);

        Log.i("params", params.toString());

        if (_fun.isInternetAvailable(SignupActivity.this)) {
            RetrofitClient.getClient().create(Api.class).register(params)
                    .enqueue(new RetrofitCallBack(SignupActivity.this, register, true));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).register(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, register, true));
                }
            });
        }
    }

    retrofit2.Callback<DefaultResponse> emailPhoneValidate = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    sendOTP();
                } else {
                    Function.CustomMessage(SignupActivity.this,dr.getMessage());
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
    retrofit2.Callback<DefaultResponse> sendOTP = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();

                if (dr.isStatus()) {
//                    showPopupWindow(btn_sign_up);
                    layout_register.setVisibility(View.GONE);
                    layout_otp.setVisibility(View.VISIBLE);
                    isOTPView = true;
                } else {
                    Function.CustomMessage(SignupActivity.this,dr.getMessage());
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
    retrofit2.Callback<DefaultResponse> reSendOTP = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    Function.CustomMessage(SignupActivity.this,dr.getMessage());
                } else {
                    sendOTP();
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
    retrofit2.Callback<DefaultResponse> verifyOTP = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
//                    layout_otp.setVisibility(View.GONE);
//                    layout_register.setVisibility(View.VISIBLE);
//                    isOTPView = false;
                    Register();
                } else {
                    Function.CustomMessage(SignupActivity.this,dr.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
//            call.cancel();
        }
    };
    retrofit2.Callback<LoginResponse> register = new retrofit2.Callback<LoginResponse>() {
        @Override
        public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                LoginResponse dr = response.body();
                if (dr.isStatus()) {
                    Sessions.saveSessionBool(Constant.LoginFlag, true, getApplicationContext());
                    Sessions.saveSession(Constant.UserToken, "Bearer " + dr.getToken(), getApplicationContext());
                    Sessions.saveSession(Constant.UserId, dr.getUserId(), getApplicationContext());
                    Sessions.saveSession(Constant.UserName, dr.getName(), getApplicationContext());
                    Sessions.saveSession(Constant.UserEmail, dr.getEmail(), getApplicationContext());
                    Sessions.saveSession(Constant.UserPhone, dr.getPhone(), getApplicationContext());
                    Sessions.saveSession(Constant.ProsperId, dr.getProsperId(), getApplicationContext());
                    Intent signup = new Intent(SignupActivity.this, HomeActivity.class);
                    signup.putExtra("IsRegister", "True");
                    signup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(signup);
                    finish();
                } else {
                    Function.CustomMessage(SignupActivity.this,dr.getMessage());
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

    public void TCPPTextView(TextView textView) {

        SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append(getString(R.string.txt_tc_register) + " ");
        spanText.append(getString(R.string.privacy_policy));
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent i = new Intent(SignupActivity.this, PolicyActivity.class);
                i.putExtra("PageFrom", "1");
                startActivity(i);

            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.txt_orange));    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - getString(R.string.privacy_policy).length(), spanText.length(), 0);

        spanText.append(" " + getString(R.string.txt_and) + " ");
        spanText.append(getString(R.string.txt_tc));
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent i = new Intent(SignupActivity.this, PolicyActivity.class);
                i.putExtra("PageFrom", "2");
                startActivity(i);

            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.txt_orange));    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - getString(R.string.txt_tc).length(), spanText.length(), 0);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spanText, TextView.BufferType.SPANNABLE);

    }

    @Override
    public void onBackPressed() {
        if (isOTPView) {
            isOTPView = false;
            layout_register.setVisibility(View.VISIBLE);
            layout_otp.setVisibility(View.GONE);
        } else {
            finish();
            super.onBackPressed();
        }

    }
}
