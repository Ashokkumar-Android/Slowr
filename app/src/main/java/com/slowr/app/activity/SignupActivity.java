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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.slowr.app.R;
import com.slowr.app.adapter.RegCityListAdapter;
import com.slowr.app.adapter.StateListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.components.otpview.OnOtpCompletionListener;
import com.slowr.app.components.otpview.OtpView;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.LoginResponse;
import com.slowr.app.models.RegCityItemModel;
import com.slowr.app.models.RegCityModel;
import com.slowr.app.models.StateItemModel;
import com.slowr.app.models.StateModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
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
    LinearLayout layout_state;
    LinearLayout layout_city;
    Button btn_sign_up;
    TextView txt_privacy_policy;
    TextView txt_login;
    ImageView img_back;
    TextView btn_sent_otp;
    Button btn_verify_otp;
    OtpView edt_otp;
    ScrollView layout_root_register;
    LinearLayout layout_list;
    EditText edt_list_search;
    RecyclerView rc_list;
    TextView txt_state_content;
    TextView txt_city_content;
    TextView txt_page_title;
    LinearLayout latout_img_back;
    RadioGroup rg_gender;
    RadioButton selectedRadioButton;

    int resentCount = 0;
    boolean isOTPView = false;
    boolean isCityView = false;

    PopupWindow popupWindow;

    HashMap<String, String> params = new HashMap<String, String>();
    private Function _fun = new Function();
    StateListAdapter stateListAdapter;
    RegCityListAdapter cityListAdapter;
    ArrayList<StateItemModel> stateList = new ArrayList<>();
    ArrayList<RegCityItemModel> cityList = new ArrayList<>();
    String otp = "";
    String fieldType = "";
    String fieldValue = "";
    String fbToken = "";
    String stateId = "";
    String stateName = "";
    String cityId = "";
    String cityName = "";
    int tabNo = 0;

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
        img_back = findViewById(R.id.reg_img_back);
        btn_sent_otp = findViewById(R.id.txt_resend_otp);
        btn_verify_otp = findViewById(R.id.btn_verify_otp);
        edt_otp = findViewById(R.id.otp_view);
        layout_register = findViewById(R.id.layout_register);
        layout_otp = findViewById(R.id.layout_otp);
        layout_root_register = findViewById(R.id.layout_root_register);
        layout_list = findViewById(R.id.layout_list);
        edt_list_search = findViewById(R.id.edt_list_search);
        rc_list = findViewById(R.id.rc_list);
        layout_state = findViewById(R.id.layout_state);
        txt_state_content = findViewById(R.id.txt_state_content);
        layout_city = findViewById(R.id.layout_city);
        txt_city_content = findViewById(R.id.txt_city_content);
        txt_page_title = findViewById(R.id.txt_page_title);
        latout_img_back = findViewById(R.id.img_back);
        rg_gender = findViewById(R.id.rg_gender);

        btn_sign_up.setOnClickListener(this);
        txt_login.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_sent_otp.setOnClickListener(this);
        btn_verify_otp.setOnClickListener(this);
        layout_state.setOnClickListener(this);
        layout_city.setOnClickListener(this);
        latout_img_back.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_list.setLayoutManager(linearLayoutManager);
        rc_list.setItemAnimator(new DefaultItemAnimator());
        stateListAdapter = new StateListAdapter(stateList, SignupActivity.this);
        cityListAdapter = new RegCityListAdapter(cityList, SignupActivity.this);
        edt_otp.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String _otp) {
                otp = _otp;
            }
        });
        edt_otp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (otp.length() == 0) {
                        Function.CustomMessage(SignupActivity.this, "Enter OTP");
                        return false;
                    } else {
                        verifyOTP(otp);
                    }
                    return true;
                }
                return false;
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
        getStateList();
        callBackFunction();
    }

    private void callBackFunction() {
        stateListAdapter.setCallback(new StateListAdapter.Callback() {
            @Override
            public void itemClick(StateItemModel model) {
                stateId = model.getStateId();
                stateName = model.getStateName();
                txt_state_content.setText(model.getStateName());
                layout_list.setVisibility(View.GONE);
                layout_root_register.setVisibility(View.VISIBLE);
                isCityView = false;
                edt_list_search.setText("");
//                stateListAdapter.getFilter().filter("");
                cityId = "";
                cityName = "";
                txt_city_content.setText("");
                cityList.clear();
                getCityList(stateId);
            }
        });
        cityListAdapter.setCallback(new RegCityListAdapter.Callback() {
            @Override
            public void itemClick(RegCityItemModel model) {
                cityId = model.getStateId();
                cityName = model.getStateName();
                txt_city_content.setText(model.getStateName());
                layout_list.setVisibility(View.GONE);
                layout_root_register.setVisibility(View.VISIBLE);
                isCityView = false;
                edt_list_search.setText("");
                Function.hideSoftKeyboard(SignupActivity.this, btn_sign_up);
//                cityListAdapter.getFilter().filter("");
            }
        });
        edt_list_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tabNo == 1) {
                    stateListAdapter.getFilter().filter(edt_list_search.getText().toString());
                } else if (tabNo == 2) {
                    cityListAdapter.getFilter().filter(edt_list_search.getText().toString());
                }

            }
        });
    }

    private void getStateList() {
        RetrofitClient.getClient().create(Api.class).getState()
                .enqueue(new RetrofitCallBack(SignupActivity.this, stateValue, false, false));
    }

    private void getCityList(String stateId) {
        RetrofitClient.getClient().create(Api.class).getCityState(stateId)
                .enqueue(new RetrofitCallBack(SignupActivity.this, cityValue, true, false));
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
            case R.id.reg_img_back:
                if (isOTPView) {
                    isOTPView = false;
                    layout_register.setVisibility(View.VISIBLE);
                    layout_otp.setVisibility(View.GONE);
                } else if (isCityView) {
                    layout_list.setVisibility(View.GONE);
                    layout_root_register.setVisibility(View.VISIBLE);
                    isCityView = false;
                } else {
//                    Intent i = new Intent(SignupActivity.this, LoginActivity.class);
//                    startActivity(i);
                    finish();
                }
                break;
            case R.id.txt_resend_otp:
                if (resentCount != 2) {
                    resentCount++;
//                    reSendOTP();
                    sendOTP();
                    edt_otp.setText("");
                } else {
                    Intent i = new Intent(SignupActivity.this, ReportUsActivity.class);
                    i.putExtra("PageFrom", "1");
                    startActivity(i);
                }
                break;
            case R.id.btn_verify_otp:
                if (otp.length() == 0) {
                    Function.CustomMessage(SignupActivity.this, "Enter OTP");
                } else {
                    verifyOTP(otp);
                }
                break;
            case R.id.layout_state:
                layout_root_register.setVisibility(View.GONE);
                layout_list.setVisibility(View.VISIBLE);
                rc_list.setAdapter(stateListAdapter);
                stateListAdapter.notifyDataSetChanged();
                isCityView = true;
                tabNo = 1;
                txt_page_title.setText(getString(R.string.txt_select_state));
                break;
            case R.id.layout_city:
                if (stateId.equals("")) {
                    Function.CustomMessage(SignupActivity.this, getString(R.string.select_state));
                    Function.hideSoftKeyboard(SignupActivity.this, v);
                } else {
                    if (cityList.size() != 0) {
                        layout_root_register.setVisibility(View.GONE);
                        layout_list.setVisibility(View.VISIBLE);
                        rc_list.setAdapter(cityListAdapter);
                        cityListAdapter.notifyDataSetChanged();
                        isCityView = true;
                        tabNo = 2;
                        txt_page_title.setText(getString(R.string.txt_select_city));
                    }
                }
                break;
            case R.id.img_back:
                layout_list.setVisibility(View.GONE);
                layout_root_register.setVisibility(View.VISIBLE);
                isCityView = false;
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
        if (name.length() < 3) {
            til_edt_name.setError(getString(R.string.enter_name_minimum));
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
        if (stateId.equals("")) {
            Function.CustomMessage(SignupActivity.this, getString(R.string.txt_select_state));
            return;
        }
        if (cityId.equals("")) {
            Function.CustomMessage(SignupActivity.this, getString(R.string.txt_select_city));
            return;
        }
        if (password.isEmpty()) {
            til_password.setError(getString(R.string.enter_password));
            til_password.requestFocus();
            return;
        } else {
            til_password.setErrorEnabled(false);
        }

        if (password.length() < 4) {
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
                    .enqueue(new RetrofitCallBack(SignupActivity.this, emailPhoneValidate, true, false));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).emailPhoneRegistration(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, emailPhoneValidate, true, false));
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
        params.put("message_type", "1");
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(SignupActivity.this)) {
            RetrofitClient.getClient().create(Api.class).sendOTP(params)
                    .enqueue(new RetrofitCallBack(SignupActivity.this, sendOTP, true, false));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).sendOTP(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, sendOTP, true, false));
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
                    .enqueue(new RetrofitCallBack(SignupActivity.this, reSendOTP, true, false));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).reSendOTP(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, reSendOTP, true, false));
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
                    .enqueue(new RetrofitCallBack(SignupActivity.this, verifyOTP, true, false));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).verifyOTP(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, verifyOTP, true, false));
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
        String gender = "";
        int selectedRadioButtonId = rg_gender.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedRbText = selectedRadioButton.getText().toString();
            if (selectedRbText.equals(getString(R.string.txt_male))) {
                gender = "1";
            } else if (selectedRbText.equals(getString(R.string.txt_female))) {
                gender = "2";
            } else {
                gender = "3";
            }
        } else {
            gender = "";
        }
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("password", password);
        params.put("password_confirmation", password);
        params.put("fcm_token", fbToken);
        params.put("platform", "2");
        params.put("state_id", stateId);
        params.put("city_id", cityId);
        params.put("gender", gender);

        Log.i("params", params.toString());

        if (_fun.isInternetAvailable(SignupActivity.this)) {
            RetrofitClient.getClient().create(Api.class).register(params)
                    .enqueue(new RetrofitCallBack(SignupActivity.this, register, true, false));
        } else {
            _fun.ShowNoInternetPopup(SignupActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).register(params)
                            .enqueue(new RetrofitCallBack(SignupActivity.this, register, true, false));
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
                    Function.CustomMessage(SignupActivity.this, dr.getMessage());
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
                    Function.CustomMessage(SignupActivity.this, dr.getMessage());
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
                    Function.CustomMessage(SignupActivity.this, dr.getMessage());
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
                    Function.CustomMessage(SignupActivity.this, dr.getMessage());
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
                    Sessions.saveSessionBool(Constant.LoginType, false, getApplicationContext());
                    Sessions.saveSession(Constant.UserToken, "Bearer " + dr.getToken(), getApplicationContext());
                    Sessions.saveSession(Constant.UserId, dr.getUserId(), getApplicationContext());
                    Sessions.saveSession(Constant.UserName, dr.getName(), getApplicationContext());
                    Sessions.saveSession(Constant.UserEmail, dr.getEmail(), getApplicationContext());
                    Sessions.saveSession(Constant.UserPhone, dr.getPhone(), getApplicationContext());
                    Sessions.saveSession(Constant.ProsperId, dr.getProsperId(), getApplicationContext());
                    Sessions.saveSession(Constant.RegCityId, cityId, getApplicationContext());
                    Sessions.saveSession(Constant.RegStateId, stateId, getApplicationContext());
                    Sessions.saveSession(Constant.RegCityName, cityName, getApplicationContext());
                    Sessions.saveSession(Constant.RegStateName, stateName, getApplicationContext());
                    Intent signup = new Intent(SignupActivity.this, HomeActivity.class);
                    signup.putExtra("IsRegister", "True");
                    signup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(signup);
                    finish();
                } else {
                    Function.CustomMessage(SignupActivity.this, dr.getMessage());
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
    retrofit2.Callback<StateModel> stateValue = new retrofit2.Callback<StateModel>() {
        @Override
        public void onResponse(Call<StateModel> call, retrofit2.Response<StateModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                StateModel dr = response.body();
                if (dr.isStatus()) {
                    if (stateList.size() != 0) {
                        stateList.clear();
                    }

                    stateList.addAll(dr.getStateList());

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
    retrofit2.Callback<RegCityModel> cityValue = new retrofit2.Callback<RegCityModel>() {
        @Override
        public void onResponse(Call<RegCityModel> call, retrofit2.Response<RegCityModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                RegCityModel dr = response.body();
                if (dr.isStatus()) {
                    if (cityList.size() != 0) {
                        cityList.clear();
                    }

                    cityList.addAll(dr.getStateList());

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
        } else if (isCityView) {
            layout_list.setVisibility(View.GONE);
            layout_root_register.setVisibility(View.VISIBLE);
            isCityView = false;
        } else {
            finish();
            super.onBackPressed();
        }

    }
}
