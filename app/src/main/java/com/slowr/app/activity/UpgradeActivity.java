package com.slowr.app.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.slowr.app.R;
import com.slowr.app.adapter.ProsperIdAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.PromotePriceModel;
import com.slowr.app.models.ProsperIdItemModel;
import com.slowr.app.models.ProsperIdModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;

public class UpgradeActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    private static final String TAG = UpgradeActivity.class.getSimpleName();
    TextView txt_page_title;
    LinearLayout img_back;
    TextView txt_from_date;
    TextView txt_to_date;
    Button btn_select_top;
    Button btn_select_premium;
    TextView txt_top_content;
    TextView txt_top_price;
    TextView txt_premium_price;
    TextView txt_premium_content;
    LinearLayout layout_success;
    LinearLayout layout_success_prosper;
    LinearLayout layout_error;
    ScrollView layout_upgrade;
    LinearLayout layout_search_list;
    EditText edt_search_suggestion;
    RecyclerView rc_prosperList;
    Button btn_home_page;
    Button btn_profile;
    Button btn_go_back;
    TextView txt_prosper_id;
    TextView txt_ad_title;
    ImageView img_fancy_content;

    LinearLayoutManager listManager;

    private int mYear, mMonth, mDay;
    String adStartDate = "";
    String adEndDate = "";
    int topPrice = 0;
    int premiumPrice = 0;

    String promotionType = "";
    String adId = "";
    String catId = "";
    String paymentId = "";
    String selectedId = "";
    String pageFrom = "";
    String adTitle = "";
    private Function _fun = new Function();
    HashMap<String, String> params = new HashMap<String, String>();
    ArrayList<ProsperIdItemModel> prosperIdList = new ArrayList<>();
    ProsperIdAdapter prosperIdAdapter;

    private PopupWindow spinnerPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        doDeclaration();
    }

    private void doDeclaration() {
        pageFrom = getIntent().getStringExtra("PageFrom");
        if (pageFrom.equals("2")) {
            catId = getIntent().getStringExtra("CatId");
            adId = getIntent().getStringExtra("AdId");
            adTitle = getIntent().getStringExtra("AdTitle");
        }
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        txt_from_date = findViewById(R.id.txt_from_date);
        txt_to_date = findViewById(R.id.txt_to_date);
        btn_select_top = findViewById(R.id.btn_select_top);
        btn_select_premium = findViewById(R.id.btn_select_premium);
        txt_top_content = findViewById(R.id.txt_top_content);
        txt_top_price = findViewById(R.id.txt_top_price);
        txt_premium_price = findViewById(R.id.txt_premium_price);
        txt_premium_content = findViewById(R.id.txt_premium_content);
        layout_success = findViewById(R.id.layout_success);
        layout_upgrade = findViewById(R.id.layout_upgrade);
        layout_search_list = findViewById(R.id.layout_search_list);
        edt_search_suggestion = findViewById(R.id.edt_search_suggestion);
        rc_prosperList = findViewById(R.id.rc_prosperList);
        btn_home_page = findViewById(R.id.btn_home_page);
        layout_success_prosper = findViewById(R.id.layout_success_prosper);
        txt_prosper_id = findViewById(R.id.txt_prosper_id);
        btn_profile = findViewById(R.id.btn_profile);
        img_fancy_content = findViewById(R.id.img_fancy_content);
        btn_go_back = findViewById(R.id.btn_go_back);
        layout_error = findViewById(R.id.layout_error);
        txt_ad_title = findViewById(R.id.txt_ad_title);


        listManager = new LinearLayoutManager(UpgradeActivity.this, RecyclerView.VERTICAL, false);
        rc_prosperList.setLayoutManager(listManager);
        rc_prosperList.setItemAnimator(new DefaultItemAnimator());
        prosperIdAdapter = new ProsperIdAdapter(prosperIdList, getApplicationContext());
        rc_prosperList.setAdapter(prosperIdAdapter);

        img_back.setOnClickListener(this);
        txt_from_date.setOnClickListener(this);
        txt_to_date.setOnClickListener(this);
        btn_select_top.setOnClickListener(this);
        btn_select_premium.setOnClickListener(this);
        btn_home_page.setOnClickListener(this);
        btn_profile.setOnClickListener(this);
        btn_go_back.setOnClickListener(this);
        if (pageFrom.equals("2")) {
            layout_upgrade.setVisibility(View.VISIBLE);
            layout_search_list.setVisibility(View.GONE);
            txt_page_title.setText(getString(R.string.txt_boost_ad));
            if (_fun.isInternetAvailable(UpgradeActivity.this)) {
                getPrice();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                getPrice();
                            }
                        });
                    }
                }, 200);

            }
        } else {
            layout_search_list.setVisibility(View.VISIBLE);
            layout_upgrade.setVisibility(View.GONE);
            txt_page_title.setText(getString(R.string.fancy_prosper_id));
        }
        Checkout.preload(getApplicationContext());

        SearchFunction();
        CallBackFunction();
    }

    private void CallBackFunction() {
        prosperIdAdapter.setCallback(new ProsperIdAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                selectedId = prosperIdList.get(pos).getProsperId();
                String amount = prosperIdList.get(pos).getProsPrice();
                promotionType = "1";
                ShowPopupProsper(selectedId, amount);


            }
        });

    }

    private void SearchFunction() {
        edt_search_suggestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edt_search_suggestion.getRight() - edt_search_suggestion.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (edt_search_suggestion.getText().length() == 0) {
                            Function.CustomMessage(UpgradeActivity.this, "Enter Prosper Id");
                        } else if (edt_search_suggestion.getText().length() < 4) {
                            Function.CustomMessage(UpgradeActivity.this, "Enter 4 digits");
                        } else {
                            getProsperIdList();
                        }


                        return true;
                    }
                }
                return false;
            }
        });
        edt_search_suggestion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edt_search_suggestion.getText().length() == 0) {
                        Function.CustomMessage(UpgradeActivity.this, "Enter Prosper Id");
                    } else if (edt_search_suggestion.getText().length() < 4) {
                        Function.CustomMessage(UpgradeActivity.this, "Enter 4 digits");
                    } else {
                        getProsperIdList();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void getProsperIdList() {
        if (!params.isEmpty()) {
            params.clear();
        }

        params.put("prosper_id", edt_search_suggestion.getText().toString());
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(UpgradeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).searchProsperId(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UpgradeActivity.this, prosperIdListAPI, true));
        } else {
            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).searchProsperId(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(UpgradeActivity.this, prosperIdListAPI, true));
                }
            });

        }

    }

    private void getPrice() {
        RetrofitClient.getClient().create(Api.class).getPromotionPrice(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(UpgradeActivity.this, priceResponse, true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;
            case R.id.txt_to_date:
                getDate("2");
                break;
            case R.id.txt_from_date:
                getDate("1");
                break;
            case R.id.btn_select_top:
                promotionType = "2";
                doDateValidation();
                break;
            case R.id.btn_select_premium:
                promotionType = "3";
                doDateValidation();
                break;
            case R.id.btn_home_page:
                Intent d = new Intent(UpgradeActivity.this, DashBoardActivity.class);
                startActivity(d);
                finish();
                break;
            case R.id.btn_profile:
                finish();
                break;
            case R.id.btn_go_back:
                if (promotionType.equals("2")) {
                    layout_upgrade.setVisibility(View.VISIBLE);
                    layout_error.setVisibility(View.GONE);
                } else if (promotionType.equals("3")) {
                    layout_upgrade.setVisibility(View.VISIBLE);
                    layout_error.setVisibility(View.GONE);
                } else {
                    layout_search_list.setVisibility(View.VISIBLE);
                    layout_error.setVisibility(View.GONE);
                }
                break;
        }
    }


    public void getDate(final String type) {
        try {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(UpgradeActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String dat = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            c.set(Calendar.MONTH, monthOfYear);
                            c.set(Calendar.YEAR, year);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            String da = sdf.format(c.getTime());

                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                            String da1 = sdf1.format(c.getTime());

                            if (type.equals("1")) {
                                txt_from_date.setText(da);
                                adStartDate = da1;
                            } else {
                                txt_to_date.setText(da);
                                adEndDate = da1;

                            }
                            if (!adStartDate.equals("") && !adEndDate.equals(""))
                                calculatePrice();
                        }
                    }, mYear, mMonth, mDay);
            dpd.getDatePicker().setMinDate(c.getTimeInMillis());
            dpd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculatePrice() {
        int day = Integer.valueOf(getCountOfDays(adStartDate, adEndDate)) + 1;
        int totalTopPrice = day * topPrice;
        int totalPremium = day * premiumPrice;
        txt_top_price.setText(String.valueOf(totalTopPrice));
        txt_premium_price.setText(String.valueOf(totalPremium));
    }

    private void doDateValidation() {
        boolean valid = false;
        String sDate = adStartDate;
        String eDate = adEndDate;

        Date fDate = null;
        Date tDate = null;
        Date cDate = null;
        String formStartDate = sDate;
        String formEndDate = eDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();
        String formCurrentDate = sdf2.format(calendar.getTime());
        if (adStartDate.equals("")) {
            Function.CustomMessage(UpgradeActivity.this, getString(R.string.select_from_date));
            return;
        }
        if (adEndDate.equals("")) {
            Function.CustomMessage(UpgradeActivity.this, getString(R.string.select_end_date));
            return;
        }
        try {
            Log.i("Date Time", formStartDate + " + " + formEndDate);
            fDate = sdf.parse(formStartDate);
            tDate = sdf1.parse(formEndDate);
            cDate = sdf3.parse(formCurrentDate);

            if (tDate.before(fDate)) {
                Function.CustomMessage(UpgradeActivity.this, getString(R.string.event_date_validation));
                return;

            }
        } catch (ParseException ex) {
            valid = false;
            Log.v("Exception", ex.getLocalizedMessage());
        }

        validatePromotion();


    }

    retrofit2.Callback<PromotePriceModel> priceResponse = new retrofit2.Callback<PromotePriceModel>() {
        @Override
        public void onResponse(Call<PromotePriceModel> call, retrofit2.Response<PromotePriceModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            PromotePriceModel dr = response.body();
            try {
                if (dr.isStatus()) {
                    txt_top_content.setText(dr.getPriceItemModel().getTopOfPageContent());
                    txt_top_price.setText(dr.getPriceItemModel().getTopOfPagePrice());
                    txt_premium_content.setText(dr.getPriceItemModel().getPremiumPageContent());
                    txt_premium_price.setText(dr.getPriceItemModel().getPremiumPagePrice());
                    topPrice = Integer.valueOf(dr.getPriceItemModel().getTopOfPagePrice());
                    premiumPrice = Integer.valueOf(dr.getPriceItemModel().getPremiumPagePrice());
                } else {
                    Function.CustomMessage(UpgradeActivity.this, dr.getMessage());
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

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
//        co.setKeyID("rzp_test_A2vBDkxnd63ve4");

        try {
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.app_name));
            options.put("description", "Ad promotion");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "contact@slowr.in");
            preFill.put("contact", "9876543210");
            JSONObject ReadOnly = new JSONObject();
            JSONObject themeStyle = new JSONObject();
            ReadOnly.put("email", "true");
            ReadOnly.put("contact", "true");
            themeStyle.put("color", "#0F4C81");
            options.put("prefill", preFill);
            options.put("readonly", ReadOnly);
            options.put("theme", themeStyle);

            co.open(activity, options);
        } catch (Exception e) {

            Function.CustomMessage(UpgradeActivity.this, "Try Again.");
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            paymentId = razorpayPaymentID;
            savePromotion();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            if (promotionType.equals("2")) {
                layout_upgrade.setVisibility(View.GONE);
                layout_error.setVisibility(View.VISIBLE);
            } else if (promotionType.equals("3")) {
                layout_upgrade.setVisibility(View.GONE);
                layout_error.setVisibility(View.VISIBLE);
            } else {
                layout_search_list.setVisibility(View.GONE);
                layout_error.setVisibility(View.VISIBLE);
            }
//            savePromotion();
            Log.e("Payment:", code + " " + response);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    public void validatePromotion() {
        if (!params.isEmpty()) {
            params.clear();
        }


        params.put("from_date", adStartDate);
        params.put("to_date", adEndDate);
        params.put("ads_id", adId);
        params.put("category_id", catId);

        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(UpgradeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).checkPromotionValid(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UpgradeActivity.this, validatePromotionApi, true));
        } else {
            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).checkPromotionValid(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(UpgradeActivity.this, validatePromotionApi, true));
                }
            });
        }
    }

    public void savePromotion() {
        if (!params.isEmpty()) {
            params.clear();
        }
        String totalAmount = "";
        if (promotionType.equals("2")) {
            totalAmount = txt_top_price.getText().toString().trim();
        } else {
            totalAmount = txt_premium_price.getText().toString().trim();
        }
        params.put("type", promotionType);
        params.put("transaction_id", paymentId);
        if (!promotionType.equals("1")) {

            params.put("start_date", adStartDate);
            params.put("end_date", adEndDate);
            params.put("ads_id", adId);
            params.put("category_id", catId);
            params.put("total_amount", totalAmount);
        } else {
            params.put("prosper_id", selectedId);
        }
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(UpgradeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).savePromotion(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UpgradeActivity.this, savePromotionApi, true));
        } else {
            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).savePromotion(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(UpgradeActivity.this, savePromotionApi, true));
                }
            });
        }
    }

    retrofit2.Callback<DefaultResponse> savePromotionApi = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    if (promotionType.equals("2")) {
                        txt_ad_title.setText(adTitle + "-" + getString(R.string.promote_content_one));
                        layout_upgrade.setVisibility(View.GONE);
                        layout_success.setVisibility(View.VISIBLE);
                    } else if (promotionType.equals("3")) {
                        txt_ad_title.setText(adTitle + "-" + getString(R.string.promote_content_one));
                        layout_upgrade.setVisibility(View.GONE);
                        layout_success.setVisibility(View.VISIBLE);
                    } else {
                        Sessions.saveSession(Constant.ProsperId, selectedId, getApplicationContext());
                        txt_prosper_id.setText(selectedId);
                        layout_search_list.setVisibility(View.GONE);
                        layout_success_prosper.setVisibility(View.VISIBLE);
                    }


                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
//                    finish();
                } else {
                    Function.CustomMessage(UpgradeActivity.this, dr.getMessage());
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

    retrofit2.Callback<DefaultResponse> validatePromotionApi = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    if (_fun.isInternetAvailable(UpgradeActivity.this)) {
                        startPayment();
                    } else {
                        _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                startPayment();
                            }
                        });
                    }
                } else {
//                    Function.CustomMessage(UpgradeActivity.this, dr.getMessage());
                    ShowPopupSuccess(dr.getMessage());
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

    retrofit2.Callback<ProsperIdModel> prosperIdListAPI = new retrofit2.Callback<ProsperIdModel>() {
        @Override
        public void onResponse(Call<ProsperIdModel> call, retrofit2.Response<ProsperIdModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                ProsperIdModel dr = response.body();
                if (dr.isStatus()) {
                    img_fancy_content.setVisibility(View.GONE);
                    prosperIdList.clear();
                    prosperIdList.addAll(dr.getProsperIdItemModel());
                    prosperIdAdapter.notifyDataSetChanged();
                    if (dr.getMessage() != null && !dr.getMessage().equals("")) {
                        Function.CustomMessage(UpgradeActivity.this, dr.getMessage());
                    }
                } else {
                    Function.CustomMessage(UpgradeActivity.this, dr.getMessage());
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

    public String getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ("" + (int) dayCount);
    }

    public void ShowPopupProsper(String proId, String amount) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_select_prosper_id, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        TextView txt_prosperId_popup = view.findViewById(R.id.txt_prosper_id);
        TextView txt_prosper_content = view.findViewById(R.id.txt_prosper_content);
        Button btn_ok = view.findViewById(R.id.btn_check_out);
        txt_prosperId_popup.setText(getString(R.string.txt_rupee_simpal) + " " + amount);
        txt_prosper_content.setText(getString(R.string.prosper_id) + " " + proId + "\n" + getString(R.string.txt_has_been_selected));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
                if (_fun.isInternetAvailable(UpgradeActivity.this)) {
                    startPayment();
                } else {
                    _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            startPayment();
                        }
                    });
                }
            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void ShowPopupSuccess(String mesg) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_post_success, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        TextView txt_content_one = view.findViewById(R.id.txt_content_one);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        txt_content_one.setText(mesg);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerPopup.dismiss();

            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
