package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.slowr.app.helper.FacebookHelper;
import com.slowr.app.helper.GoogleSignInHelper;
import com.slowr.app.helper.TwitterHelper;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.LoginResponse;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.KeyHashGenerator;
import com.slowr.app.utils.Sessions;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements FacebookHelper.OnFbSignInListener, GoogleSignInHelper.OnGoogleSignInListener,
        TwitterHelper.OnTwitterSignInListener, View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    //--------------------------------Facebook login--------------------------------------//
    private FacebookHelper fbConnectHelper;
    private ImageView fbSignInButton;
    //----------------------------------Google +Sign in-----------------------------------//
    //Google plus sign-in button
    private GoogleSignInHelper googleSignInHelper;
    private ImageView gSignInButton;

    //-----------------------------------Twitter Sign In -----------------------------------//
    private TwitterHelper twitterHelper;
    private ImageView tSignInButton;


    private TextView btn_sign_up;
    private Button btn_sign_in;
    private boolean isFbLogin = false;
    private TextInputEditText edt_email;
    private TextInputEditText edt_password;
    private TextInputEditText edt_mobile_number;
    private TextInputLayout til_email;
    private TextInputLayout til_password;
    private TextInputLayout til_mobile_number;
    private TextInputLayout til_email_forgot;
    private TextInputLayout til_forgot_password;
    private TextInputLayout til_forgot_confirm_password;
    private TextView txt_forgot_password;
    private LinearLayout layout_login;
    private LinearLayout layout_forgot_password;
    private LinearLayout layout_change_password;
    private LinearLayout layout_otp;
    private EditText edt_email_forgot_password;
    private EditText edt_change_password;
    private EditText edt_change_confirm_password;
    private Button btn_send_mail;
    private Button btn_send_password;
    private Button btn_request_otp;
    private TextView txt_terms_condistion;
    private ImageView img_back;
    private LinearLayout layout_password;
    private TextView txt_login_password;
    private TextView txt_login_otp;
    private TextView txt_otp_content;
    private TextView txt_privacy_policy;


    TextView btn_sent_otp;
    Button btn_verify_otp;
    OtpView edt_otp;

    PopupWindow popupWindow;

    HashMap<String, String> params = new HashMap<String, String>();
    boolean isForgotPass = false;
    boolean isMobileLogin = false;
    boolean isOTPMail = false;
    boolean isPasswordLayout = false;
    boolean isOTPView = false;

    String otp = "";
    String fbToken = "";
    int resentCount = 0;
    private Function _fun = new Function();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterHelper = new TwitterHelper(this, this); // Twitter Initialization
        FacebookSdk.sdkInitialize(getApplicationContext()); // Facebook SDK Initialization
        setContentView(R.layout.activity_login);
        doDeclaration();
    }

    private void doDeclaration() {
        btn_sign_up = findViewById(R.id.btn_sign_up);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        txt_forgot_password = findViewById(R.id.txt_forgot_password);
        layout_login = findViewById(R.id.layout_login);
        layout_forgot_password = findViewById(R.id.layout_forgot_password);
        edt_email_forgot_password = findViewById(R.id.edt_email_forgot_password);
        btn_send_mail = findViewById(R.id.btn_send_mail);
        edt_change_password = findViewById(R.id.edt_change_password);
        edt_change_confirm_password = findViewById(R.id.edt_change_confirm_password);
        btn_send_password = findViewById(R.id.btn_send_password);
        layout_change_password = findViewById(R.id.layout_change_password);
        layout_otp = findViewById(R.id.layout_otp);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        btn_request_otp = findViewById(R.id.btn_request_otp);
        edt_mobile_number = findViewById(R.id.edt_mobile_number);
        til_mobile_number = findViewById(R.id.til_mobile_number);
        txt_terms_condistion = findViewById(R.id.txt_terms_condistion);
        layout_password = findViewById(R.id.layout_password);
        txt_login_password = findViewById(R.id.txt_login_password);
        txt_login_otp = findViewById(R.id.txt_login_otp);
        txt_otp_content = findViewById(R.id.txt_otp_content);
        til_forgot_password = findViewById(R.id.til_forgot_password);
        til_forgot_confirm_password = findViewById(R.id.til_forgot_confirm_password);

        btn_sent_otp = findViewById(R.id.txt_resend_otp);
        btn_verify_otp = findViewById(R.id.btn_verify_otp);
        edt_otp = findViewById(R.id.otp_view);
        img_back = findViewById(R.id.img_back);
        til_email_forgot = findViewById(R.id.til_email_forgot);
        txt_privacy_policy = findViewById(R.id.txt_privacy_policy);

        btn_sign_up.setOnClickListener(this);
        btn_sign_in.setOnClickListener(this);
        txt_forgot_password.setOnClickListener(this);
        btn_send_mail.setOnClickListener(this);
        btn_send_password.setOnClickListener(this);
        btn_request_otp.setOnClickListener(this);
        txt_terms_condistion.setOnClickListener(this);
        btn_sent_otp.setOnClickListener(this);
        btn_verify_otp.setOnClickListener(this);
        img_back.setOnClickListener(this);
        txt_login_password.setOnClickListener(this);
        txt_login_otp.setOnClickListener(this);
        txt_privacy_policy.setOnClickListener(this);
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
                    if (otp.isEmpty()) {
                        Function.CustomMessage(LoginActivity.this, "Kindly enter OTP");
                        return false;
                    }

                    if (isForgotPass) {
                        String val = edt_email_forgot_password.getText().toString().trim();
                        boolean digitsOnly = TextUtils.isDigitsOnly(val);
                        if (digitsOnly) {
                            verifyForgotPasswordPhoneOTP(otp);
                        } else {
                            verifyOTP(otp);
                        }
                    } else {
                        verifyLoginOTP(otp);
                    }
                    return true;
                }
                return false;
            }
        });
        //--------------------------------Facebook login--------------------------------------//
        KeyHashGenerator.generateKey(this);
        fbConnectHelper = new FacebookHelper(this, this);
        fbSignInButton = findViewById(R.id.fb_sign_in_button);
        fbSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_fun.isInternetAvailable(LoginActivity.this)) {
                    fbConnectHelper.connect();
                    isFbLogin = true;
                    isForgotPass = false;
                } else {
                    _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            fbConnectHelper.connect();
                            isFbLogin = true;
                            isForgotPass = false;
                        }
                    });
                }

            }
        });


        //----------------------------------Google +Sign in-----------------------------------//

        googleSignInHelper = new GoogleSignInHelper(this, this);
//        googleSignInHelper.onStart();
        googleSignInHelper.connect();
        gSignInButton = findViewById(R.id.main_g_sign_in_button);
        gSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_fun.isInternetAvailable(LoginActivity.this)) {
                    googleSignInHelper.signIn();
                    isFbLogin = false;
                    isForgotPass = false;
                } else {
                    _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            googleSignInHelper.signIn();
                            isFbLogin = false;
                            isForgotPass = false;
                        }
                    });
                }


            }
        });

        //----------------------------------Twitter Sign in button ------------------------------//
        tSignInButton = findViewById(R.id.main_twitter_sign_in_button);
        tSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_fun.isInternetAvailable(LoginActivity.this)) {
                    twitterHelper.connect();
                    isFbLogin = false;
                    isForgotPass = false;
                } else {
                    _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            twitterHelper.connect();
                            isFbLogin = false;
                            isForgotPass = false;
                        }
                    });
                }


            }
        });


        MobileEmailCheckFunction();
        GetDeviceId();
    }

    private void GetDeviceId() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                fbToken = newToken;
                Log.e("newToken", newToken);

            }
        });
    }

    private void MobileEmailCheckFunction() {
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = edt_email.getText().toString().trim();
                boolean digitsOnly = TextUtils.isDigitsOnly(email);
                if (digitsOnly && email.length() == 10) {
                    til_email.setErrorEnabled(false);
                    params.put("mobile_email", email);
                    Log.i("params", params.toString());

                    RetrofitClient.getClient().create(Api.class).emailPhoneValidate(params)
                            .enqueue(new RetrofitCallBack(LoginActivity.this, emailPhoneValidate, true));

                    isMobileLogin = true;
                } else {
                    if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        til_email.setErrorEnabled(false);
                        return;
                    }
                    isMobileLogin = false;
                    btn_sign_in.setText(getString(R.string.txt_continue));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("onStart", "Called");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleSignInHelper.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);
        twitterHelper.onActivityResult(requestCode, resultCode, data);
        if (isFbLogin) {
            isFbLogin = false;
        }
    }

    @Override
    public void OnFbSignInComplete(GraphResponse graphResponse, String error) {
        if (error == null) {
            try {
                JSONObject jsonObject = graphResponse.getJSONObject();

//                String profileImg = "http://graph.facebook.com/" + id + "/picture?type=large";
                String email = "";
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                if (jsonObject.has("email")) {
                    email = jsonObject.getString("email");
                }
                callLoginApi("2", email, "", name, "", "", "facebook", id);


            } catch (JSONException e) {
                Log.i(TAG, e.getMessage());
            }
        } else {
        }
    }

    @Override
    public void OnGSignInSuccess(GoogleSignInAccount googleSignInAccount) {
        if (googleSignInAccount != null) {

            String id = googleSignInAccount.getId();
            String name = googleSignInAccount.getGivenName() + " " + googleSignInAccount.getFamilyName();
            String email = googleSignInAccount.getEmail();
            callLoginApi("2", email, "", name, "", "", "google", id);
        }
    }

    @Override
    public void OnGSignInError(String error) {
        Log.e(TAG, error);
    }

    @Override
    public void OnTwitterSignInComplete(TwitterHelper.UserDetails userDetails, String error) {
        if (userDetails != null) {
            String name = userDetails.getUserName();
            String id = String.valueOf(userDetails.getUserId());
            String email = "";
            String phone = "";
            if (userDetails.getUserEmail() != null) {
                email = userDetails.getUserEmail();
            }
            callLoginApi("2", email, "", name, "", "", "twitter", id);
        }
    }

    @Override
    public void OnTweetPostComplete(Result<Tweet> result, String error) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signup);
//                finish();
                break;
            case R.id.btn_sign_in:
                resentCount = 0;
                if (btn_sign_in.getText().toString().equals(getString(R.string.request_otp))) {
                    sendOTP(true);
                } else if (btn_sign_in.getText().toString().equals(getString(R.string.txt_sigin))) {

                    callLoginWithPassword();
                } else {
                    callLogin();
                }
                isForgotPass = false;
                break;
            case R.id.txt_forgot_password:
                resentCount = 0;
                layout_login.setVisibility(View.GONE);
                layout_forgot_password.setVisibility(View.VISIBLE);
                isForgotPass = true;
                isOTPView = true;
                break;
            case R.id.btn_send_mail:
                if (_fun.isInternetAvailable(LoginActivity.this)) {
                    sendForgotPasswordMail("1");
                } else {
                    _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            sendForgotPasswordMail("1");
                        }
                    });
                }
                break;
            case R.id.btn_send_password:
                String password = edt_change_password.getText().toString().trim();
                String confirmPassword = edt_change_confirm_password.getText().toString().trim();
                String email = edt_email_forgot_password.getText().toString().trim();
                if (password.length() < 4) {
                    til_forgot_password.setError(getString(R.string.password_empty_alert));
                    til_forgot_password.requestFocus();
                    return;
                } else {
                    til_forgot_password.setErrorEnabled(false);
                }
                if (confirmPassword.isEmpty()) {
                    til_forgot_confirm_password.setError(getString(R.string.enter_retype_password));
                    til_forgot_confirm_password.requestFocus();
                    return;
                } else {
                    til_forgot_confirm_password.setErrorEnabled(false);
                }

                if (!confirmPassword.equals(password)) {
                    til_forgot_confirm_password.setError(getString(R.string.password_retype_password_alert));
                    til_forgot_confirm_password.requestFocus();
                    return;
                } else {
                    til_forgot_confirm_password.setErrorEnabled(false);
                }

                if (!params.isEmpty()) {
                    params.clear();
                }
                boolean digitsOnly2 = TextUtils.isDigitsOnly(email);
                if (digitsOnly2) {
                    params.put("mobile", email);
                } else {
                    params.put("email", email);
                }
                params.put("password", password);
                params.put("password_confirmation", password);

                RetrofitClient.getClient().create(Api.class).updatePassword(params)
                        .enqueue(new RetrofitCallBack(LoginActivity.this, updatePassword, true));
                break;

            case R.id.btn_request_otp:
                String phoneNum = edt_email.getText().toString().trim();
                if (phoneNum.isEmpty()) {
                    til_mobile_number.setError(getString(R.string.enter_phone_number));
                    til_mobile_number.requestFocus();
                    return;
                } else {
                    til_mobile_number.setErrorEnabled(false);
                }
                if (!Patterns.PHONE.matcher(phoneNum).matches()) {
                    til_mobile_number.setError(getString(R.string.enter_valid_phone_number));
                    til_mobile_number.requestFocus();
                    return;
                } else {
                    til_mobile_number.setErrorEnabled(false);
                }

                callLoginApi("1", "", "", "", phoneNum, "", "", "");


                break;
            case R.id.txt_terms_condistion:
                Intent t = new Intent(LoginActivity.this, PolicyActivity.class);
                t.putExtra("PageFrom", "2");
                startActivity(t);
                break;
            case R.id.btn_verify_otp:
                if (otp.isEmpty()) {
                    Function.CustomMessage(LoginActivity.this, "Kindly enter OTP");
                    return;
                }

                if (isForgotPass) {
                    String val = edt_email_forgot_password.getText().toString().trim();
                    boolean digitsOnly = TextUtils.isDigitsOnly(val);
                    if (digitsOnly) {
                        verifyForgotPasswordPhoneOTP(otp);
                    } else {
                        verifyOTP(otp);
                    }
                } else {
                    verifyLoginOTP(otp);
                }
                break;
            case R.id.txt_resend_otp:
                if (resentCount != 2) {
                    resentCount++;
                    if (isForgotPass) {

                        if (_fun.isInternetAvailable(LoginActivity.this)) {
                            sendForgotPasswordMail("2");
                        } else {
                            _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {
                                    sendForgotPasswordMail("2");
                                }
                            });
                        }
                    } else {
//                        reSendOTP("1");
                        sendOTP(true);
                    }
                    edt_otp.setText("");
                } else {
                    Intent i = new Intent(LoginActivity.this, ReportUsActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.img_back:
                if (isOTPView) {
                    isOTPView = false;
                    layout_login.setVisibility(View.VISIBLE);
                    layout_otp.setVisibility(View.GONE);
                    layout_change_password.setVisibility(View.GONE);
                    layout_forgot_password.setVisibility(View.GONE);
                    edt_email_forgot_password.setText("");
                    edt_otp.setText("");
                } else {
//                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//                    startActivity(i);
                    finish();
                }
                break;
            case R.id.txt_login_otp:
                txt_login_password.setVisibility(View.VISIBLE);
                layout_password.setVisibility(View.GONE);
                isPasswordLayout = false;
                String val = edt_email.getText().toString().trim();
                boolean digitsOnly = TextUtils.isDigitsOnly(val);
                if (!digitsOnly) {
                    edt_email.setText("");

                }
                til_email.setHint(getString(R.string.enter_phone_number));
                btn_sign_in.setText(getString(R.string.request_otp));
                break;
            case R.id.txt_login_password:
                txt_login_password.setVisibility(View.GONE);
                layout_password.setVisibility(View.VISIBLE);
                isPasswordLayout = true;
                btn_sign_in.setText(getString(R.string.txt_sigin));
                til_email.setHint(getString(R.string.enter_your_phone_no_email));
                break;
            case R.id.txt_privacy_policy:
                Intent i = new Intent(LoginActivity.this, PolicyActivity.class);
                i.putExtra("PageFrom", "1");
                startActivity(i);
                break;
        }
    }

    private void callLoginWithPassword() {
        String password = edt_password.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        boolean digitsOnly = TextUtils.isDigitsOnly(email);
        if (email.isEmpty()) {
            til_email.setError(getString(R.string.enter_your_input));
            til_email.requestFocus();
            return;
        } else {
            if (digitsOnly) {

                if (!Patterns.PHONE.matcher(email).matches()) {
                    til_email.setError(getString(R.string.enter_valid_phone_number));
                    til_email.requestFocus();
                    return;
                } else if (email.length() < 10) {
                    til_email.setError(getString(R.string.enter_valid_phone_number));
                    til_email.requestFocus();
                    return;
                } else {
                    til_email.setErrorEnabled(false);
                }
            } else {
                if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    til_email.setError(getString(R.string.enter_email));
                    til_email.requestFocus();
                    return;
                } else {
                    til_email.setErrorEnabled(false);
                }
            }
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
        if (!params.isEmpty()) {
            params.clear();
        }
        isMobileLogin = false;
        params.put("mobile_email", email);
        params.put("password", password);
        params.put("fcm_token", fbToken);
        params.put("device_platform", "1");
        if (_fun.isInternetAvailable(LoginActivity.this)) {
            RetrofitClient.getClient().create(Api.class).toLogin(params)
                    .enqueue(new RetrofitCallBack(LoginActivity.this, login, true));
        } else {
            _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).toLogin(params)
                            .enqueue(new RetrofitCallBack(LoginActivity.this, login, true));
                }
            });
        }

    }

    private void sendOTP(final boolean isLoad) {
        isOTPMail = false;
        String phone = edt_email.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("status", "1");
        params.put("message_type", "2");
        Log.i("Params", params.toString());


        if (_fun.isInternetAvailable(LoginActivity.this)) {
            RetrofitClient.getClient().create(Api.class).sendOTP(params)
                    .enqueue(new RetrofitCallBack(LoginActivity.this, sendOTP, isLoad));
        } else {
            _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).sendOTP(params)
                            .enqueue(new RetrofitCallBack(LoginActivity.this, sendOTP, isLoad));
                }
            });
        }
    }

    private void sendForgotPasswordMail(String val) {

        String email = edt_email_forgot_password.getText().toString().trim();
        boolean digitsOnly = TextUtils.isDigitsOnly(email);
        if (email.isEmpty()) {
            til_email_forgot.setError(getString(R.string.enter_your_input));
            til_email_forgot.requestFocus();
            return;
        } else {
            if (digitsOnly) {
                if (!Patterns.PHONE.matcher(email).matches()) {
                    til_email_forgot.setError(getString(R.string.enter_valid_phone_number));
                    til_email_forgot.requestFocus();
                    return;
                } else if (email.length() < 10) {
                    til_email_forgot.setError(getString(R.string.enter_valid_phone_number));
                    til_email_forgot.requestFocus();
                    return;
                } else {
                    til_email_forgot.setErrorEnabled(false);
                }
            } else {
                if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    til_email_forgot.setError(getString(R.string.enter_email));
                    til_email_forgot.requestFocus();
                    return;
                } else {
                    til_email_forgot.setErrorEnabled(false);
                }
            }
        }

        if (digitsOnly) {
//            if (val.equals("1")) {
                if (!params.isEmpty()) {
                    params.clear();
                }
                params.put("mobile", email);
                params.put("status", "2");
                params.put("message_type", "3");
                Log.i("Params", params.toString());
                isOTPMail = false;
                RetrofitClient.getClient().create(Api.class).sendOTP(params)
                        .enqueue(new RetrofitCallBack(LoginActivity.this, sendOTP, true));
//            } else {
//                reSendOTP("2");
//            }
        } else {
            isOTPMail = true;
            if (!params.isEmpty()) {
                params.clear();
            }
            params.put("email", email);
            RetrofitClient.getClient().create(Api.class).forgotPasswordSendMail(params)
                    .enqueue(new RetrofitCallBack(LoginActivity.this, forgotPassword, true));
        }


    }

    private void callLogin() {

        String email = edt_email.getText().toString().trim();

        boolean digitsOnly = TextUtils.isDigitsOnly(email);
        if (email.isEmpty()) {
            til_email.setError(getString(R.string.enter_your_input));
            til_email.requestFocus();
            return;
        } else {
            if (digitsOnly) {

                if (!Patterns.PHONE.matcher(email).matches()) {
                    til_email.setError(getString(R.string.enter_valid_phone_number));
                    til_email.requestFocus();
                    return;
                } else if (email.length() < 10) {
                    til_email.setError(getString(R.string.enter_valid_phone_number));
                    til_email.requestFocus();
                    return;
                } else {
                    til_email.setErrorEnabled(false);
                }
            } else {
                if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    til_email.setError(getString(R.string.enter_email));
                    til_email.requestFocus();
                    return;
                } else {
                    til_email.setErrorEnabled(false);
                }
            }
        }
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile_email", email);
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(LoginActivity.this)) {
            RetrofitClient.getClient().create(Api.class).emailPhoneValidate(params)
                    .enqueue(new RetrofitCallBack(LoginActivity.this, emailPhoneValidate, true));
        } else {
            _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).emailPhoneValidate(params)
                            .enqueue(new RetrofitCallBack(LoginActivity.this, emailPhoneValidate, true));
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
                    if (isMobileLogin) {
                        if (dr.getAction().equals("1")) {
                            if (isPasswordLayout) {
                                btn_sign_in.setText(getString(R.string.txt_sigin));
                            } else {
                                btn_sign_in.setText(getString(R.string.request_otp));
                                txt_login_password.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
                            signup.putExtra("field", dr.getFileld());
                            signup.putExtra("value", edt_email.getText().toString());
                            startActivity(signup);
                        }
                    } else {
                        if (dr.getAction().equals("1")) {
                            if (dr.getFileld().equals("mobile")) {
                                if (isPasswordLayout) {
                                    btn_sign_in.setText(getString(R.string.txt_sigin));
                                } else {
                                    btn_sign_in.setText(getString(R.string.request_otp));
                                    txt_login_password.setVisibility(View.VISIBLE);
                                }
                            } else {
                                layout_password.setVisibility(View.VISIBLE);
                                isPasswordLayout = true;
                                txt_login_password.setVisibility(View.GONE);
                                btn_sign_in.setText(getString(R.string.txt_sigin));
                                txt_login_otp.setVisibility(View.VISIBLE);
                                til_email.setHint(getString(R.string.enter_your_phone_no_email));
                            }
                        } else {
                            Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
                            signup.putExtra("field", dr.getFileld());
                            signup.putExtra("value", edt_email.getText().toString());
                            startActivity(signup);
                        }
                    }
                } else {
                    til_email.setError(dr.getMessage());
                    til_email.requestFocus();
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

    private void callLoginApi(String loginWith, String email, String password, String name, String mobile, String phone, String provider, String providerId) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("login_with", loginWith);
        params.put("email", email);
        params.put("password", password);
        params.put("name", name);
        params.put("mobile", mobile);
        params.put("phone", phone);
        params.put("provider", provider);
        params.put("providerId", providerId);
        params.put("fcm_token", fbToken);
        params.put("platform", "2");
        params.put("device_platform", "1");

        Log.i("Param", params.toString());
        if (loginWith.equals("1")) {
            isMobileLogin = true;
            Sessions.saveSessionBool(Constant.LoginType, false, getApplicationContext());
        } else {
            isMobileLogin = false;
            Sessions.saveSessionBool(Constant.LoginType, true, getApplicationContext());
        }


        if (_fun.isInternetAvailable(LoginActivity.this)) {
            RetrofitClient.getClient().create(Api.class).toLogin(params)
                    .enqueue(new RetrofitCallBack(LoginActivity.this, login, true));
        } else {
            _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).toLogin(params)
                            .enqueue(new RetrofitCallBack(LoginActivity.this, login, true));
                }
            });
        }

    }

    retrofit2.Callback<LoginResponse> login = new retrofit2.Callback<LoginResponse>() {
        @Override
        public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                LoginResponse dr = response.body();
                if (dr.isStatus()) {

                    if (isMobileLogin) {
                        sendOTP(true);
                    } else {
                        Sessions.saveSessionBool(Constant.LoginFlag, true, getApplicationContext());
                        Sessions.saveSession(Constant.UserToken, "Bearer " + dr.getToken(), getApplicationContext());
                        Sessions.saveSession(Constant.UserId, dr.getUserId(), getApplicationContext());
                        Sessions.saveSession(Constant.UserName, dr.getName(), getApplicationContext());
                        Sessions.saveSession(Constant.UserEmail, dr.getEmail(), getApplicationContext());
                        Sessions.saveSession(Constant.UserPhone, dr.getPhone(), getApplicationContext());
                        Sessions.saveSession(Constant.ProsperId, dr.getProsperId(), getApplicationContext());
                        Sessions.saveSession(Constant.UserProfile, dr.getProUrl(), getApplicationContext());
                        Sessions.saveSession(Constant.UserVerified, dr.getIsVerified(), getApplicationContext());
                        Intent signup = new Intent(LoginActivity.this, HomeActivity.class);
                        signup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        if (dr.getIsRegister().equals("1")) {
                            signup.putExtra("PageFrom", "1");
                        }
                        startActivity(signup);
                        finish();

                    }


                } else {
                    Function.CustomMessage(LoginActivity.this, dr.getMessage());
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

    retrofit2.Callback<DefaultResponse> forgotPassword = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    Function.CustomMessage(LoginActivity.this, dr.getMessage());
//                    showPopupWindow(btn_send_mail, "1");
                    layout_forgot_password.setVisibility(View.GONE);
                    layout_otp.setVisibility(View.VISIBLE);
                    edt_otp.setText("");
                    if (isOTPMail) {
                        txt_otp_content.setText(getString(R.string.txt_otp_content_email));
                    } else {
                        txt_otp_content.setText(getString(R.string.txt_otp_content));
                    }
                } else {
                    Function.CustomMessage(LoginActivity.this, dr.getMessage());
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


    private void reSendOTP(String val) {
        String phone = "";
        if (val.equals("1")) {
            phone = edt_email.getText().toString().trim();
        } else {
            phone = edt_email_forgot_password.getText().toString().trim();
        }


        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);

        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(LoginActivity.this)) {
            RetrofitClient.getClient().create(Api.class).reSendOTP(params)
                    .enqueue(new RetrofitCallBack(LoginActivity.this, reSendOTP, true));
        } else {
            _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).reSendOTP(params)
                            .enqueue(new RetrofitCallBack(LoginActivity.this, reSendOTP, true));
                }
            });
        }
    }

    private void verifyOTP(String otp) {
        String phone = edt_email_forgot_password.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("emailuser", phone);
        params.put("v_otp", otp);


        if (_fun.isInternetAvailable(LoginActivity.this)) {
            RetrofitClient.getClient().create(Api.class).forgotPasswordVerifyOTP(params)
                    .enqueue(new RetrofitCallBack(LoginActivity.this, verifyOTP, true));
        } else {
            _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).forgotPasswordVerifyOTP(params)
                            .enqueue(new RetrofitCallBack(LoginActivity.this, verifyOTP, true));
                }
            });
        }
    }

    private void verifyForgotPasswordPhoneOTP(String otp) {
        String phone = edt_email_forgot_password.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("otp", otp);
        params.put("login_with", "2");

        Log.i("Params", params.toString());


        if (_fun.isInternetAvailable(LoginActivity.this)) {
            RetrofitClient.getClient().create(Api.class).verifyOTPForgotPassword(params)
                    .enqueue(new RetrofitCallBack(LoginActivity.this, verifyOTP, true));
        } else {
            _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).verifyOTPForgotPassword(params)
                            .enqueue(new RetrofitCallBack(LoginActivity.this, verifyOTP, true));
                }
            });
        }
    }

    private void verifyLoginOTP(String otp) {
        String phone = edt_email.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("otp", otp);
        params.put("login_with", "1");
        params.put("fcm_token", fbToken);

        Log.i("Params", params.toString());

        Sessions.saveSessionBool(Constant.LoginType, false, getApplicationContext());
        if (_fun.isInternetAvailable(LoginActivity.this)) {
            RetrofitClient.getClient().create(Api.class).verifyOTPLogin(params)
                    .enqueue(new RetrofitCallBack(LoginActivity.this, mobileLogin, true));
        } else {
            _fun.ShowNoInternetPopup(LoginActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).verifyOTPLogin(params)
                            .enqueue(new RetrofitCallBack(LoginActivity.this, mobileLogin, true));
                }
            });
        }
    }

    retrofit2.Callback<LoginResponse> mobileLogin = new retrofit2.Callback<LoginResponse>() {
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
                    Sessions.saveSession(Constant.UserProfile, dr.getProUrl(), getApplicationContext());
                    Sessions.saveSession(Constant.UserVerified, dr.getIsVerified(), getApplicationContext());
                    Intent signup = new Intent(LoginActivity.this, HomeActivity.class);
                    signup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    if (dr.getIsRegister().equals("1")) {
                        signup.putExtra("PageFrom", "1");
                    }
                    startActivity(signup);
                    finish();
                } else {
                    Function.CustomMessage(LoginActivity.this, dr.getMessage());
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
                    if (isForgotPass) {
                        layout_otp.setVisibility(View.GONE);
                        layout_change_password.setVisibility(View.VISIBLE);
                    } else {
                        layout_otp.setVisibility(View.GONE);
                        layout_login.setVisibility(View.VISIBLE);
                        isOTPView = false;
                        String phoneNum = edt_mobile_number.getText().toString().trim();

                    }
                } else {
                    Function.CustomMessage(LoginActivity.this, dr.getMessage());
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

    retrofit2.Callback<DefaultResponse> updatePassword = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
//                    popupWindow.dismiss();
                    layout_change_password.setVisibility(View.GONE);
                    layout_login.setVisibility(View.VISIBLE);
                    edt_password.setText("");
                    isOTPView = false;
                } else {
                    Function.CustomMessage(LoginActivity.this, dr.getMessage());
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

    retrofit2.Callback<DefaultResponse> sendOTP = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();

                if (dr.isStatus()) {
//                    showPopupWindow(btn_sign_up, "2");
                    if (isForgotPass) {
                        layout_forgot_password.setVisibility(View.GONE);
                    } else {
                        layout_login.setVisibility(View.GONE);
                        isOTPView = true;
                    }
                    layout_otp.setVisibility(View.VISIBLE);
                    edt_otp.setText("");
                    if (isOTPMail) {
                        txt_otp_content.setText(getString(R.string.txt_otp_content_email));
                    } else {
                        txt_otp_content.setText(getString(R.string.txt_otp_content));
                    }
                    Function.CustomMessage(LoginActivity.this, dr.getMessage());
                } else {
                    Function.CustomMessage(LoginActivity.this, dr.getMessage());
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
    retrofit2.Callback<DefaultResponse> reSendOTP = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    Function.CustomMessage(LoginActivity.this, dr.getMessage());
                } else {
                    sendOTP(true);
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
        if (isOTPView) {
            isOTPView = false;
            layout_login.setVisibility(View.VISIBLE);
            layout_otp.setVisibility(View.GONE);
            layout_change_password.setVisibility(View.GONE);
            layout_forgot_password.setVisibility(View.GONE);
            edt_email_forgot_password.setText("");
            edt_otp.setText("");
        } else {
//            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//            startActivity(i);
            finish();
            super.onBackPressed();
        }

    }
}
