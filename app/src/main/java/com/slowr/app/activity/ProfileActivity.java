package com.slowr.app.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.gioco.image.cropper.CropImage;
import com.gioco.image.cropper.CropImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.slowr.app.R;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.components.HeartBeatView;
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

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    HeartBeatView btn_fancy_prosper_id;
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
    ImageView img_remove;
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
    Button btn_demo_page;
    int resentCount = 0;
    int PROSPER_ID_CODE = 1299;

    boolean isImageChanged = false;
    boolean isEmail = false;
    String PageFrom = "";
    MultipartBody.Part chatImage = null;
    String NotificationId = "";
    String adCount = "";
    boolean isPageChange = false;

    private PopupWindow spinnerPopup, demoPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        img_remove = findViewById(R.id.img_remove);
        btn_demo_page = findViewById(R.id.btn_demo_page);

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
        img_remove.setOnClickListener(this);
        btn_demo_page.setOnClickListener(this);
        btn_fancy_prosper_id.start();
        btn_fancy_prosper_id.setDurationBasedOnBPM(30);
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
        otp_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (otp.length() == 0) {
                        Function.CustomMessage(ProfileActivity.this, "Enter OTP");
                        return false;
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
                    return true;
                }
                return false;
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
        if (Sessions.getSessionBool(Constant.LoginType, ProfileActivity.this)) {
            txt_change_password.setVisibility(View.INVISIBLE);
        }
    }

    private void ReadNotification(String noteId) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("notification_id", noteId);
        Log.i("Params", params.toString());

        if (_fun.isInternetAvailable(ProfileActivity.this)) {
            RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ProfileActivity.this, noteReadResponse, false, false));
        } else {
            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ProfileActivity.this, noteReadResponse, false, false));
                }
            });
        }
    }

    private void getUserDetails() {
        RetrofitClient.getClient().create(Api.class).getProfileDetails(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(ProfileActivity.this, profileDetails, true, false));
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
                    txt_page_title.setText(getString(R.string.txt_profile));
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
                isEditView = true;
                txt_page_title.setText(getString(R.string.txt_profile_edit));
                if (txt_user_name.getText().toString().equals(getString(R.string.click_edit_name))) {
                    edt_name.setText("");
                } else {
                    edt_name.setText(txt_user_name.getText().toString());
                }
                if (txt_email.getText().toString().equals(getString(R.string.click_edit_email))) {
                    edt_email.setText("");
                } else {
                    edt_email.setText(txt_email.getText().toString());
                }
                if (txt_user_mobile.getText().toString().equals(getString(R.string.click_edit_phone))) {
                    edt_phone_number.setText("");
                } else {
                    edt_phone_number.setText(txt_user_mobile.getText().toString());
                }
                break;
            case R.id.img_profile_pic:
                if (isEditView) {
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
                } else {
                    if (Sessions.getSession(Constant.UserProfile, getApplicationContext()) != null && !Sessions.getSession(Constant.UserProfile, getApplicationContext()).equals(""))
                        if (!isPageChange) {
                            isPageChange = true;
                            Intent a = new Intent(ProfileActivity.this, ImageViewActivity.class);
                            a.putExtra("ImgURL", Sessions.getSession(Constant.UserProfile, getApplicationContext()));
                            a.putExtra("ImgPos", 0);
                            startActivity(a);
                        }
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
//                        reSendOTP();
                        sendOTP();
                    } else {
                        _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                sendOTP();
//                                reSendOTP();
                            }
                        });
                    }
                    otp_view.setText("");
                } else {
                    Intent p = new Intent(ProfileActivity.this, ReportUsActivity.class);
                    p.putExtra("PageFrom", "1");
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
            case R.id.img_remove:
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        ProfileActivity.this);

                alertDialog2.setTitle("Profile");

                alertDialog2.setMessage(getString(R.string.profile_remove_message));

                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                RemoveProfilePic();
                            }
                        });

                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog2.show();

                break;
            case R.id.btn_demo_page:

//                PackageManager packageManager = getPackageManager();
//                Intent t = new Intent(Intent.ACTION_VIEW);
//
//                try {
//                    String url = "https://api.whatsapp.com/send?phone="+ "+9101235489848" +"&text=" + URLEncoder.encode("hi", "UTF-8");
//                    t.setPackage("com.whatsapp");
//                    t.setData(Uri.parse(url));
//                    if (t.resolveActivity(packageManager) != null) {
//                        startActivity(t);
//                    }
//                } catch (Exception e){
//                    e.printStackTrace();
//                }

                if (adCount != null && !adCount.equals("") && !adCount.equals("0")) {
                    Intent j = new Intent(ProfileActivity.this, UserProfileActivity.class);
                    j.putExtra("prosperId", Sessions.getSession(Constant.ProsperId, getApplicationContext()));
                    j.putExtra("PageFrom", "1");
                    startActivityForResult(j, PROSPER_ID_CODE);
                } else {
                    ShowPopupProfileDemo();
                }
                break;
        }
    }

    private void RemoveProfilePic() {

        if (_fun.isInternetAvailable(ProfileActivity.this)) {
            RetrofitClient.getClient().create(Api.class).removeProfile(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ProfileActivity.this, removeProfileResponse, true, false));
        } else {
            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).removeProfile(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ProfileActivity.this, removeProfileResponse, true, false));
                }
            });
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
                .enqueue(new RetrofitCallBack(ProfileActivity.this, uploadProfileDetails, true, false));
    }

    private void saveProfileImage() {
//        if (!params.isEmpty()) {
//            params.clear();
//        }
//        params.put("avator", imgPath);
//        Log.i("Params", params.toString());
//        RetrofitClient.getClient().create(Api.class).uploadProfileImage(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
//                .enqueue(new RetrofitCallBack(ProfileActivity.this, uploadProfileImage, true));

        //creating a file
        File file = new File(Function.compressImage(imgPath));
        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        chatImage = MultipartBody.Part.createFormData("avator", file.getName(), requestFile);

        if (_fun.isInternetAvailable(ProfileActivity.this)) {
            RetrofitClient.getClient().create(Api.class).uploadImage(chatImage, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ProfileActivity.this, uploadProfileImage, true, false));
        } else {
            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).uploadImage(chatImage, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ProfileActivity.this, uploadProfileImage, true, false));
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

        RetrofitClient.getClient().create(Api.class).verifyOTP(params)
                .enqueue(new RetrofitCallBack(ProfileActivity.this, verifyOTP, true, false));

    }

    private void verifyEmailOTP(String otp) {
        String phone = edt_phone_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("otp", otp);

        Log.i("params", params.toString());

        RetrofitClient.getClient().create(Api.class).OTPVerificationEmail(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(ProfileActivity.this, verifyOTP, true, false));

    }

    private void sendEmailOTP() {

        if (_fun.isInternetAvailable(ProfileActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getEmailVerificationOTP(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ProfileActivity.this, sendOTPEmail, true, false));
        } else {
            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getEmailVerificationOTP(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ProfileActivity.this, sendOTPEmail, true, false));
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
                    .enqueue(new RetrofitCallBack(ProfileActivity.this, emailPhoneValidate, true, false));
        } else {
            _fun.ShowNoInternetPopup(ProfileActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).emailPhoneRegistration(params)
                            .enqueue(new RetrofitCallBack(ProfileActivity.this, emailPhoneValidate, true, false));
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
                .enqueue(new RetrofitCallBack(ProfileActivity.this, reSendOTP, true, false));


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
                    adCount = dr.getUserDetailsModel().getAdCount();
                    if (adCount != null && !adCount.equals("") && !adCount.equals("0")) {
                        btn_demo_page.setText(getString(R.string.txt_view_client));
                    } else {
                        btn_demo_page.setText(getString(R.string.txt_demo_profile));
                    }
                    if (dr.getUserDetailsModel().getUserName() != null && !dr.getUserDetailsModel().getUserName().equals("")) {
                        txt_user_name.setText(dr.getUserDetailsModel().getUserName());
                    } else {
                        txt_user_name.setText(getString(R.string.click_edit_name));
                    }

                    if (dr.getUserDetailsModel().getUserMobile() != null && !dr.getUserDetailsModel().getUserMobile().equals("")) {
                        txt_user_mobile.setText(dr.getUserDetailsModel().getUserMobile());
                        currentMobileNumber = dr.getUserDetailsModel().getUserMobile();
                    } else {
                        txt_user_mobile.setText(getString(R.string.click_edit_phone));
                    }
                    if (dr.getUserDetailsModel().getUserEmail() != null && !dr.getUserDetailsModel().getUserEmail().equals("")) {
                        txt_email.setText(dr.getUserDetailsModel().getUserEmail());
                    } else {
                        txt_email.setText(getString(R.string.click_edit_email));
                    }


                    if (dr.getUserDetailsModel().getUserMobile() != null && !dr.getUserDetailsModel().getUserMobile().equals(""))
                        if (dr.getUserDetailsModel().getIsMobileVerified().equals("1")) {
                            txt_user_mobile_verify.setText("Verified");
                            txt_user_mobile_verify.setVisibility(View.GONE);
                            txt_user_mobile_verify.setTextColor(getResources().getColor(R.color.bg_green));
                        } else {
                            txt_user_mobile_verify.setText("Not Verified");
                            txt_user_mobile_verify.setVisibility(View.GONE);
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
                            .placeholder(R.drawable.ic_default_profile)
                            .error(R.drawable.ic_default_profile)
                            .into(img_profile_pic);
                    Sessions.saveSession(Constant.UserVerified, dr.getUserDetailsModel().getIsProfileVerified(), getApplicationContext());
                    if (!dr.getUserDetailsModel().getIsProfileVerified().equals("0")) {
                        btn_profile_verification.setEnabled(false);
                        btn_profile_verification.setTextColor(getResources().getColor(R.color.bg_green));
                        btn_profile_verification.setBackgroundResource(R.drawable.bg_green_border_profile);
                        btn_profile_verification.setText(getString(R.string.txt_verified_profile));
                        txt_verified.setVisibility(View.GONE);
                        txt_verified.setText(getString(R.string.txt_verified));
                        txt_verified.setTextColor(getResources().getColor(R.color.bg_green));
                        txt_verified.setEnabled(false);
                    } else {
                        txt_verified.setVisibility(View.GONE);
                        txt_verified.setText(getString(R.string.txt_unverified));
                        txt_verified.setTextColor(getResources().getColor(R.color.txt_orange));
                        txt_verified.setEnabled(true);
                    }

                    if (dr.getUserDetailsModel().getUserPhoto() != null && !dr.getUserDetailsModel().getUserPhoto().equals("")) {
                        img_remove.setVisibility(View.VISIBLE);
                    } else {
                        img_remove.setVisibility(View.GONE);
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
//        if (requestCode == 50) {
//            if (data != null) {
//                List<String> slist = Matisse.obtainPathResult(data);
//                selectedImage = data.getData();
//                if (slist.size() > 0) {
//                    for (int i = 0; i < slist.size(); i++) {
////                        imgPath = Function.getBase64String(slist.get(i));
//                        imgPath = slist.get(i);
//                        isImageChanged = true;
//                        Glide.with(this)
//                                .load(slist.get(i))
//                                .circleCrop()
//                                .placeholder(R.drawable.ic_default_profile)
//                                .error(R.drawable.ic_default_profile)
//                                .into(img_profile_pic);
//                    }
//                }
//
//
//            }
//
//
//        }


        if (requestCode == 50) {
            if (data != null) {
                List<String> slist = Matisse.obtainPathResult(data);
                List<Uri> uris = Matisse.obtainResult(data);
                if (slist.size() > 0) {
                    for (int i = 0; i < slist.size(); i++) {
                        imgPath = slist.get(i);
                        CropImage.activity(uris.get(i))
                                .setFixAspectRatio(true)
                                .setAspectRatio(4, 4)
                                .setCropShape(CropImageView.CropShape.OVAL)
                                .start(ProfileActivity.this);
                        Log.i("Path", slist.get(i));
                    }
                }

            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (result.getType() == 2) {
                Uri resultUri = result.getUri();
                imgPath = resultUri.getPath();
            }

            isImageChanged = true;
            Glide.with(this)
                    .load(imgPath)
                    .circleCrop()
                    .placeholder(R.drawable.ic_default_profile)
                    .error(R.drawable.ic_default_profile)
                    .into(img_profile_pic);

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
                    layout_edit_profile.setVisibility(View.GONE);
                    layout_profile_details.setVisibility(View.VISIBLE);
                    btn_edit.setVisibility(View.VISIBLE);
                    isEditView = false;
                    txt_page_title.setText(getString(R.string.txt_profile));
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

    retrofit2.Callback<DefaultResponse> removeProfileResponse = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            DefaultResponse dr = response.body();
            try {
                if (dr.isStatus()) {
                    Sessions.saveSession(Constant.UserProfile, "", getApplicationContext());
                    Glide.with(ProfileActivity.this)
                            .load(R.drawable.ic_default_profile)
                            .circleCrop()
                            .placeholder(R.drawable.ic_default_profile)
                            .error(R.drawable.ic_default_profile)
                            .into(img_profile_pic);
                    img_remove.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
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

    private void callOTP() {
        String phone = edt_phone_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("status", "2");
        params.put("message_type", "4");
        Log.i("params", params.toString());
        RetrofitClient.getClient().create(Api.class).sendOTP(params)
                .enqueue(new RetrofitCallBack(ProfileActivity.this, sendOTP, true, false));

    }

    @Override
    public void onBackPressed() {
        if (spinnerPopup != null && spinnerPopup.isShowing()) {
            spinnerPopup.dismiss();
        } else if (demoPopup != null && demoPopup.isShowing()) {
            demoPopup.dismiss();
        } else if (PageFrom.equals("2")) {
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
            txt_page_title.setText(getString(R.string.txt_profile));
            layout_edit_profile.setVisibility(View.GONE);
            layout_profile_details.setVisibility(View.VISIBLE);
            btn_edit.setVisibility(View.VISIBLE);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = false;
        for (int i = 0; i < Constant.Permissions.length; i++) {
            int result1 = ContextCompat.checkSelfPermission(ProfileActivity.this, Constant.Permissions[i]);
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        if (result) {
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
    }

    public void ShowPopupAppVersion() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_post_success, null);

        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();

        TextView popup_content = view.findViewById(R.id.txt_content_one);
        TextView txt_skip = view.findViewById(R.id.txt_skip);
        Button txt_done = view.findViewById(R.id.btn_ok);
        popup_content.setText(getString(R.string.view_as_client_content));

        txt_done.setText(getString(R.string.txt_add_post));

        txt_skip.setVisibility(View.VISIBLE);
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });

        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
                if (demoPopup.isShowing()) {
                    demoPopup.dismiss();
                }
                Intent p = new Intent(ProfileActivity.this, AddPostActivity.class);
                p.putExtra("AdType", 0);
                startActivityForResult(p, PROSPER_ID_CODE);
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void ShowPopupProfileDemo() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_profile_demo, null);
        demoPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        demoPopup.setOutsideTouchable(true);
        demoPopup.setFocusable(true);
        demoPopup.update();
        TextView txt_prosperId_demo = view.findViewById(R.id.txt_prosperId_demo);
        TextView txt_name_demo = view.findViewById(R.id.txt_name_demo);
        TextView txt_phone_dmo = view.findViewById(R.id.txt_phone_dmo);
        TextView txt_email_dmo = view.findViewById(R.id.txt_email_dmo);
        TextView txt_page_title = view.findViewById(R.id.txt_page_title);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        LinearLayout img_back = view.findViewById(R.id.img_back);
        txt_page_title.setText("Demo Prosper Page");
        txt_prosperId_demo.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
        if (Sessions.getSession(Constant.UserName, getApplicationContext()).equals("")) {
            txt_name_demo.setText(getString(R.string.txt_demo_name));
        } else {
            txt_name_demo.setText(Sessions.getSession(Constant.UserName, getApplicationContext()));
        }
        if (Sessions.getSession(Constant.UserPhone, getApplicationContext()).equals("")) {
            txt_phone_dmo.setText(getString(R.string.txt_demo_phone));
        } else {
            txt_phone_dmo.setText(Sessions.getSession(Constant.UserPhone, getApplicationContext()));
        }

        if (Sessions.getSession(Constant.UserEmail, getApplicationContext()).equals("")) {
            txt_email_dmo.setText(getString(R.string.txt_demo_email));
        } else {
            txt_email_dmo.setText(Sessions.getSession(Constant.UserEmail, getApplicationContext()));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowPopupAppVersion();
            }
        }, 500);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoPopup.dismiss();
                spinnerPopup.dismiss();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoPopup.dismiss();
                spinnerPopup.dismiss();
            }
        });

        demoPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onResume() {
        isPageChange = false;
        super.onResume();
    }
}
