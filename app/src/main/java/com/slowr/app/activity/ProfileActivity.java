package com.slowr.app.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputLayout;
import com.slowr.app.R;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.components.otpview.OnOtpCompletionListener;
import com.slowr.app.components.otpview.OtpView;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.ProfileModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;
import com.slowr.matisse.Matisse;
import com.slowr.matisse.MimeType;
import com.slowr.matisse.engine.impl.GlideEngine;
import com.slowr.matisse.internal.entity.CaptureStrategy;
import com.slowr.matisse.ui.MatisseActivity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_page_title;
    TextView txt_prosperId;
    TextView txt_user_name;
    TextView txt_user_mobile;
    TextView txt_email;
    TextView txt_change_password;
    EditText edt_name;
    EditText edt_phone_number;
    EditText edt_email;
    LinearLayout img_back;
    Button btn_edit;
    Button btn_update_profile;
    Button btn_fancy_prosper_id;
    Button btn_profile_verification;
    ImageView img_profile_pic;
    TextInputLayout til_email;
    TextInputLayout til_mobile_number;
    TextInputLayout til_edt_name;
    LinearLayout layout_edit_profile;
    LinearLayout layout_profile_details;
    LinearLayout layout_profile_details_edit;
    LinearLayout layout_otp;
    TextView txt_user_mobile_verify;
    TextView txt_user_email_verify;
    TextView txt_verified;
    private TextView txt_otp_content;

    private Function _fun = new Function();
    String imgPath = "";
    Uri selectedImage;
    HashMap<String, Object> params = new HashMap<String, Object>();

    String currentMobileNumber = "";

    boolean isOTPView = false;
    boolean isEditView = false;
    String otp = "";
    OtpView otp_view;
    TextView txt_resend_otp;
    Button btn_verify_otp;
    int resentCount = 0;
    int PROSPER_ID_CODE = 1299;

    boolean isImageChanged = false;
    boolean isEmail = false;
    String PageFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        doDeclaration();
    }

    private void doDeclaration() {
        if (getIntent().hasExtra("PageFrom")) {
            PageFrom = getIntent().getStringExtra("PageFrom");
        }
        txt_page_title = findViewById(R.id.txt_page_title);
        txt_prosperId = findViewById(R.id.txt_prosperId);
        img_back = findViewById(R.id.img_back);
        btn_edit = findViewById(R.id.btn_edit);
        img_profile_pic = findViewById(R.id.img_profile_pic);
        txt_user_name = findViewById(R.id.txt_user_name);
        txt_user_mobile = findViewById(R.id.txt_user_mobile);
        txt_email = findViewById(R.id.txt_email);
        txt_change_password = findViewById(R.id.txt_change_password);
        edt_name = findViewById(R.id.edt_name);
        edt_phone_number = findViewById(R.id.edt_phone_number);
        edt_email = findViewById(R.id.edt_email);
        til_email = findViewById(R.id.til_email);
        til_mobile_number = findViewById(R.id.til_mobile_number);
        til_edt_name = findViewById(R.id.til_edt_name);
        btn_update_profile = findViewById(R.id.btn_update_profile);
        layout_edit_profile = findViewById(R.id.layout_edit_profile);
        layout_profile_details = findViewById(R.id.layout_profile_details);
        layout_profile_details_edit = findViewById(R.id.layout_profile_details_edit);
        otp_view = findViewById(R.id.otp_view);
        txt_resend_otp = findViewById(R.id.txt_resend_otp);
        btn_verify_otp = findViewById(R.id.btn_verify_otp);
        layout_otp = findViewById(R.id.layout_otp);
        btn_fancy_prosper_id = findViewById(R.id.btn_fancy_prosper_id);
        txt_user_mobile_verify = findViewById(R.id.txt_user_mobile_verify);
        txt_user_email_verify = findViewById(R.id.txt_user_email_verify);
        btn_profile_verification = findViewById(R.id.btn_profile_verification);
        txt_verified = findViewById(R.id.txt_verified);
        txt_otp_content = findViewById(R.id.txt_otp_content);

        txt_page_title.setText(getString(R.string.txt_profile));

        btn_edit.setOnClickListener(this);
        img_back.setOnClickListener(this);
        img_profile_pic.setOnClickListener(this);
        txt_change_password.setOnClickListener(this);
        btn_update_profile.setOnClickListener(this);
        txt_resend_otp.setOnClickListener(this);
        btn_verify_otp.setOnClickListener(this);
        btn_fancy_prosper_id.setOnClickListener(this);
        btn_profile_verification.setOnClickListener(this);
        txt_verified.setOnClickListener(this);
        txt_user_email_verify.setOnClickListener(this);
        img_profile_pic.setEnabled(false);

        if (_fun.isInternetAvailable(ProfileActivity.this)) {
            getUserDetails();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getUserDetails();
                        }
                    });
                }
            }, 200);

        }
        otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String _otp) {
                otp = _otp;
            }
        });
        edt_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doValidation();
                    return true;
                }
                return false;
            }
        });
    }

    private void getUserDetails() {
        RetrofitClient.getClient().create(Api.class).getProfileDetails(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(ProfileActivity.this, profileDetails, true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                if (PageFrom.equals("2")) {
                    Intent h = new Intent(ProfileActivity.this, HomeActivity.class);
                    h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(h);
                    finish();
                } else if (isOTPView) {
                    isOTPView = false;
                    layout_otp.setVisibility(View.GONE);
                    layout_profile_details_edit.setVisibility(View.VISIBLE);
                } else if (isEditView) {
                    isEditView = false;
                    layout_edit_profile.setVisibility(View.GONE);
                    layout_profile_details.setVisibility(View.VISIBLE);
                    btn_edit.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
                break;
            case R.id.btn_edit:
                btn_edit.setVisibility(View.GONE);
                layout_edit_profile.setVisibility(View.VISIBLE);
                layout_profile_details.setVisibility(View.GONE);
                img_profile_pic.setEnabled(true);
                isEditView = true;
                break;
            case R.id.img_profile_pic:
                if (_fun.checkPermission(ProfileActivity.this)) {
                    MatisseActivity.PAGE_FROM = 2;
                    Matisse.from(ProfileActivity.this)
                            .choose(MimeType.of(MimeType.PNG, MimeType.JPEG), true)
//                            .choose(MimeType.of(MimeType.GIF), false)
//                            .choose(MimeType.ofAll())

                            .countable(true)
                            .capture(true)
                            .theme(R.style.Matisse_Dracula)
                            .captureStrategy(
                                    new CaptureStrategy(true, "com.slowr.app.provider", "Android/data/com.slowr.app/files/Pictures"))
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.gride_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .maxSelectable(1)
                            .showSingleMediaType(true)
                            .forResult(50);


                }
                break;
            case R.id.txt_change_password:
                Intent i = new Intent(ProfileActivity.this, ChangPasswordActivity.class);
                startActivity(i);
                break;
            case R.id.btn_update_profile:
                doValidation();


                break;
            case R.id.btn_verify_otp:
                if (otp.length() == 0) {
                    Function.CustomMessage(ProfileActivity.this, "Enter OTP");
                } else {
                    if (_fun.isInternetAvailable(ProfileActivity.this)) {
                        if (isEmail) {
                            verifyEmailOTP(otp);
                        } else {
                            verifyOTP(otp);
                        }
                    } else {
                        _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                if (isEmail) {
                                    verifyEmailOTP(otp);
                                } else {
                                    verifyOTP(otp);
                                }
                            }
                        });
                    }

                }
                break;
            case R.id.txt_resend_otp:
                if (resentCount != 2) {
                    resentCount++;

                    if (_fun.isInternetAvailable(ProfileActivity.this)) {
                        reSendOTP();
                    } else {
                        _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                reSendOTP();
                            }
                        });
                    }
                    otp_view.setText("");
                } else {
                    Intent p = new Intent(ProfileActivity.this, ReportUsActivity.class);
                    startActivity(p);
                }
                break;
            case R.id.btn_fancy_prosper_id:
                Intent p = new Intent(ProfileActivity.this, UpgradeActivity.class);
                p.putExtra("PageFrom", "1");
                startActivityForResult(p, PROSPER_ID_CODE);
                break;
            case R.id.btn_profile_verification:
                Intent g = new Intent(ProfileActivity.this, VerificationActivity.class);
                startActivityForResult(g, PROSPER_ID_CODE);
                break;
            case R.id.txt_verified:
                btn_profile_verification.performClick();
                break;
            case R.id.txt_user_email_verify:
                sendEmailOTP();
                break;
        }
    }

    private void doValidation() {
        resentCount = 0;

        String email = edt_email.getText().toString().trim();
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

        if (_fun.isInternetAvailable(ProfileActivity.this)) {
            if (currentMobileNumber.equals(edt_phone_number.getText().toString())) {
                if (isImageChanged) {
                    saveProfileImage();
                } else {
                    saveProfileDetails();
                }
            } else {
                sendOTP();
            }
        } else {
            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    if (currentMobileNumber.equals(edt_phone_number.getText().toString())) {
                        if (isImageChanged) {
                            saveProfileImage();
                        } else {
                            saveProfileDetails();
                        }
                    } else {
                        sendOTP();
                    }
                }
            });
        }


    }

    private void saveProfileDetails() {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("name", edt_name.getText().toString());
        params.put("email", edt_email.getText().toString());
        params.put("phone", edt_phone_number.getText().toString());
        params.put("is_mobile_verified", "1");
        params.put("is_email_verified", "0");
        Log.i("Params", params.toString());
        RetrofitClient.getClient().create(Api.class).uploadProfileDetails(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(ProfileActivity.this, uploadProfileDetails, true));
    }

    private void saveProfileImage() {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("avator", imgPath);
        Log.i("Params", params.toString());
        RetrofitClient.getClient().create(Api.class).uploadProfileImage(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(ProfileActivity.this, uploadProfileImage, true));
    }

    private void verifyOTP(String otp) {
        String phone = edt_phone_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("otp", otp);

        Log.i("params", params.toString());

        RetrofitClient.getClient().create(Api.class).verifyOTP(params)
                .enqueue(new RetrofitCallBack(ProfileActivity.this, verifyOTP, true));

    }

    private void verifyEmailOTP(String otp) {
        String phone = edt_phone_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("otp", otp);

        Log.i("params", params.toString());

        RetrofitClient.getClient().create(Api.class).OTPVerificationEmail(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(ProfileActivity.this, verifyOTP, true));

    }

    private void sendEmailOTP() {

        if (_fun.isInternetAvailable(ProfileActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getEmailVerificationOTP(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ProfileActivity.this, sendOTPEmail, true));
        } else {
            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getEmailVerificationOTP(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ProfileActivity.this, sendOTPEmail, true));
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
        params.put("email", "email");
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(ProfileActivity.this)) {
            RetrofitClient.getClient().create(Api.class).emailPhoneRegistration(params)
                    .enqueue(new RetrofitCallBack(ProfileActivity.this, emailPhoneValidate, true));
        } else {
            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).emailPhoneRegistration(params)
                            .enqueue(new RetrofitCallBack(ProfileActivity.this, emailPhoneValidate, true));
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

        RetrofitClient.getClient().create(Api.class).reSendOTP(params)
                .enqueue(new RetrofitCallBack(ProfileActivity.this, reSendOTP, true));


    }

    retrofit2.Callback<ProfileModel> profileDetails = new retrofit2.Callback<ProfileModel>() {
        @Override
        public void onResponse(Call<ProfileModel> call, retrofit2.Response<ProfileModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                ProfileModel dr = response.body();
                if (dr.isStatus()) {
                    layout_profile_details_edit.setVisibility(View.VISIBLE);
                    btn_edit.setVisibility(View.VISIBLE);
                    txt_prosperId.setText(dr.getUserDetailsModel().getProsperId());
                    txt_user_name.setText(dr.getUserDetailsModel().getUserName());
                    edt_name.setText(dr.getUserDetailsModel().getUserName());
                    txt_user_mobile.setText(dr.getUserDetailsModel().getUserMobile());
                    edt_phone_number.setText(dr.getUserDetailsModel().getUserMobile());
                    if (dr.getUserDetailsModel().getUserMobile() != null) {
                        currentMobileNumber = dr.getUserDetailsModel().getUserMobile();
                    }
                    txt_email.setText(dr.getUserDetailsModel().getUserEmail());
                    edt_email.setText(dr.getUserDetailsModel().getUserEmail());
                    if (dr.getUserDetailsModel().getUserMobile() != null && !dr.getUserDetailsModel().getUserMobile().equals(""))
                        if (dr.getUserDetailsModel().getIsMobileVerified().equals("1")) {
                            txt_user_mobile_verify.setText("Verified");
                            txt_user_mobile_verify.setTextColor(getResources().getColor(R.color.bg_green));
                        } else {
                            txt_user_mobile_verify.setText("Not Verified");
                            txt_user_mobile_verify.setTextColor(getResources().getColor(R.color.chat_sticker_circle_bg));
                        }

//                    if (dr.getUserDetailsModel().getUserEmail() != null && !dr.getUserDetailsModel().getUserEmail().equals(""))
//                        if (dr.getUserDetailsModel().getIsEmailVerified().equals("1")) {
//                            txt_user_email_verify.setText("Verified");
//                            txt_user_email_verify.setTextColor(getResources().getColor(R.color.bg_green));
//                            txt_user_email_verify.setEnabled(false);
//                        } else {
//                            txt_user_email_verify.setText("Not Verified");
//                            txt_user_email_verify.setTextColor(getResources().getColor(R.color.chat_sticker_circle_bg));
//                        }

                    Sessions.saveSession(Constant.UserPhone, currentMobileNumber, getApplicationContext());
                    Sessions.saveSession(Constant.UserName, dr.getUserDetailsModel().getUserName(), getApplicationContext());
                    Sessions.saveSession(Constant.ProsperId, dr.getUserDetailsModel().getProsperId(), getApplicationContext());
                    Sessions.saveSession(Constant.UserProfile, dr.getUserDetailsModel().getUserPhoto(), getApplicationContext());
                    Glide.with(ProfileActivity.this)
                            .load(dr.getUserDetailsModel().getUserPhoto())
                            .circleCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.ic_default_profile)
                            .error(R.drawable.ic_default_profile)
                            .into(img_profile_pic);
                    Sessions.saveSession(Constant.UserVerified, dr.getUserDetailsModel().getIsProfileVerified(), getApplicationContext());
                    if (!dr.getUserDetailsModel().getIsProfileVerified().equals("0")) {
                        btn_profile_verification.setEnabled(false);
                        btn_profile_verification.setTextColor(getResources().getColor(R.color.bg_green));
                        btn_profile_verification.setBackgroundResource(R.drawable.bg_green_border_profile);
                        btn_profile_verification.setText(getString(R.string.txt_verified_profile));
//                        txt_verified.setVisibility(View.VISIBLE);
                        txt_verified.setText(getString(R.string.txt_verified));
                        txt_verified.setTextColor(getResources().getColor(R.color.bg_green));
                        txt_verified.setEnabled(false);
                    } else {
                        txt_verified.setText(getString(R.string.txt_unverified));
                        txt_verified.setTextColor(getResources().getColor(R.color.txt_orange));
                        txt_verified.setEnabled(true);
                    }
                } else {
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50) {
            if (data != null) {
                List<String> slist = Matisse.obtainPathResult(data);
                selectedImage = data.getData();
                if (slist.size() > 0) {
                    for (int i = 0; i < slist.size(); i++) {
                        imgPath = Function.getBase64String(slist.get(i));
                        isImageChanged = true;
                        Glide.with(this)
                                .load(slist.get(i))
                                .circleCrop()
                                .placeholder(R.drawable.ic_default_profile)
                                .error(R.drawable.ic_default_profile)
                                .into(img_profile_pic);
                    }
                }


            }


        } else if (requestCode == PROSPER_ID_CODE) {
            if (_fun.isInternetAvailable(ProfileActivity.this)) {
                getUserDetails();
            } else {
                _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                    @Override
                    public void isInternet() {
                        getUserDetails();
                    }
                });
            }
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
        }
    }

    retrofit2.Callback<DefaultResponse> uploadProfileImage = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
//                    img_profile_pic.setEnabled(false);
//                    layout_edit_profile.setVisibility(View.GONE);
//                    layout_profile_details.setVisibility(View.VISIBLE);

                    if (_fun.isInternetAvailable(ProfileActivity.this)) {
                        saveProfileDetails();
                    } else {
                        _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                saveProfileDetails();
                            }
                        });
                    }
                } else {
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
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
    retrofit2.Callback<DefaultResponse> uploadProfileDetails = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
                    img_profile_pic.setEnabled(false);
                    layout_edit_profile.setVisibility(View.GONE);
                    layout_profile_details.setVisibility(View.VISIBLE);
                    btn_edit.setVisibility(View.VISIBLE);
                    isEditView = false;
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    if (_fun.isInternetAvailable(ProfileActivity.this)) {
                        getUserDetails();
                    } else {
                        _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                getUserDetails();
                            }
                        });
                    }
                } else {
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
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
                    layout_profile_details_edit.setVisibility(View.GONE);
                    layout_otp.setVisibility(View.VISIBLE);
                    isOTPView = true;
                    txt_otp_content.setText(getString(R.string.txt_otp_content));
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
                } else {
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
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
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
                } else {
                    if (_fun.isInternetAvailable(ProfileActivity.this)) {
                        sendOTP();
                    } else {
                        _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                sendOTP();
                            }
                        });
                    }

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
                    layout_otp.setVisibility(View.GONE);
                    layout_profile_details_edit.setVisibility(View.VISIBLE);
                    isOTPView = false;
                    if (isEmail) {
                        isEmail = false;
                        otp_view.setText("");
                        if (_fun.isInternetAvailable(ProfileActivity.this)) {
                            getUserDetails();
                        } else {
                            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {
                                    getUserDetails();
                                }
                            });
                        }
                    } else {

                        if (_fun.isInternetAvailable(ProfileActivity.this)) {
                            if (isImageChanged) {
                                saveProfileImage();
                            } else {
                                saveProfileDetails();
                            }
                        } else {
                            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {
                                    if (isImageChanged) {
                                        saveProfileImage();
                                    } else {
                                        saveProfileDetails();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
        }
    };

    retrofit2.Callback<DefaultResponse> emailPhoneValidate = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    callOTP();
                } else {
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
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

    retrofit2.Callback<DefaultResponse> sendOTPEmail = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();

                if (dr.isStatus()) {
                    txt_otp_content.setText(getString(R.string.txt_otp_content_email));
                    layout_profile_details_edit.setVisibility(View.GONE);
                    layout_otp.setVisibility(View.VISIBLE);
                    isOTPView = true;
                    isEmail = true;
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
                } else {
                    Function.CustomMessage(ProfileActivity.this, dr.getMessage());
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

    private void callOTP() {
        String phone = edt_phone_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("status", "2");
        Log.i("params", params.toString());
        RetrofitClient.getClient().create(Api.class).sendOTP(params)
                .enqueue(new RetrofitCallBack(ProfileActivity.this, sendOTP, true));

    }

    @Override
    public void onBackPressed() {
        if (PageFrom.equals("2")) {
            Intent h = new Intent(ProfileActivity.this, HomeActivity.class);
            h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(h);
            finish();
        } else if (isOTPView) {
            isOTPView = false;
            layout_otp.setVisibility(View.GONE);
            layout_profile_details_edit.setVisibility(View.VISIBLE);
        } else if (isEditView) {
            isEditView = false;
            layout_edit_profile.setVisibility(View.GONE);
            layout_profile_details.setVisibility(View.VISIBLE);
            btn_edit.setVisibility(View.VISIBLE);
        } else {
            finish();
            super.onBackPressed();
        }
    }
}
