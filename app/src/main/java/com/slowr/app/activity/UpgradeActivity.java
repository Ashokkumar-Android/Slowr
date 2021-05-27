package com.slowr.app.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.slowr.app.R;
import com.slowr.app.adapter.GSTListAdapter;
import com.slowr.app.adapter.ProsperIdAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.GSTListItemModel;
import com.slowr.app.models.GSTLitsModel;
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

import okhttp3.ResponseBody;
import retrofit2.Call;

public class UpgradeActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultWithDataListener {

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
    ImageView img_search;

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
    String orderId = "";
    String paymentSignature = "";
    String selectedId = "";
    String pageFrom = "";
    String adTitle = "";
    private Function _fun = new Function();
    HashMap<String, String> params = new HashMap<String, String>();
    ArrayList<ProsperIdItemModel> prosperIdList = new ArrayList<>();
    ProsperIdAdapter prosperIdAdapter;

    private PopupWindow spinnerPopup,spinnerPopupCity;

    String prosperAmount = "0";

    EditText txt_company_name;
    EditText txt_company_address;
    EditText edt_gst_no_bill;
    LinearLayout layout_address;
    GSTListAdapter gstListAdapter;
    ArrayList<GSTListItemModel> gstNoList = new ArrayList<>();

    String gstId = "0";
    String gstNo = "";
    String gstName = "";
    String gstAddress = "";
    GridLayoutManager gridManager;
    boolean isAddressEnable = false;

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
        edt_search_suggestion = findViewById(R.id.edt_search_prosper);
        rc_prosperList = findViewById(R.id.rc_prosperList);
        btn_home_page = findViewById(R.id.btn_home_page);
        layout_success_prosper = findViewById(R.id.layout_success_prosper);
        txt_prosper_id = findViewById(R.id.txt_prosper_id);
        btn_profile = findViewById(R.id.btn_profile);
        img_fancy_content = findViewById(R.id.img_fancy_content);
        btn_go_back = findViewById(R.id.btn_go_back);
        layout_error = findViewById(R.id.layout_error);
        txt_ad_title = findViewById(R.id.txt_ad_title);
        img_search = findViewById(R.id.img_search);


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
        img_search.setOnClickListener(this);
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
        getPaymentHistory();
//        setFillter();
    }

    private void getPaymentHistory() {
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
//        edt_search_suggestion.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_RIGHT = 2;
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (edt_search_suggestion.getRight() - edt_search_suggestion.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        if (edt_search_suggestion.getText().length() == 0) {
//                            Function.CustomMessage(UpgradeActivity.this, "Enter Prosper Id");
//                        } else if (edt_search_suggestion.getText().length() < 4) {
//                            Function.CustomMessage(UpgradeActivity.this, "Enter 4 digits");
//                        } else if (!CheckValitation()) {
//                            Function.CustomMessage(UpgradeActivity.this, "Enter valid inputs");
//                        } else {
//                            getProsperIdList();
//                        }
//
//
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
        edt_search_suggestion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edt_search_suggestion.getText().length() == 0) {
                        Function.CustomMessage(UpgradeActivity.this, "Enter Prosper Id");
                    } else if (edt_search_suggestion.getText().length() < 4) {
                        Function.CustomMessage(UpgradeActivity.this, "Enter minimum 4 digits");
                    } else if (!CheckValitation()) {
                        Function.CustomMessage(UpgradeActivity.this, "Enter valid inputs");
                    } else {
                        getProsperIdList();
                    }
                    return true;
                }
                return false;
            }
        });
//        edt_search_suggestion.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.i("Count", s + "," + String.valueOf(start) + "," + String.valueOf(before) + "," + String.valueOf(count));
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    int length = edt_search_suggestion.getText().toString().length();
//                    int numlength = 0;
//                    int txtlength = 0;
//                    String inTxt = edt_search_suggestion.getText().toString().trim();
////                if (length != 0) {
////                    for (int i = 0; i < length; i++) {
////                        boolean digitsOnly = TextUtils.isDigitsOnly(edt_search_suggestion.getText().toString().substring(i));
////                        if (digitsOnly) {
////                            numlength++;
////                        } else {
////                            txtlength++;
////                        }
////                    }
////                    if (numlength < 4 && txtlength < 2) {
////                        setFillter("[a-zA-Z0-9]+");
////                        edt_search_suggestion.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
////                    } else if (numlength == 4 && txtlength < 2) {
////                        setFillter("[a-zA-Z]+");
////                    } else if (numlength < 4 && txtlength == 2) {
////                        setFillter("[0-9]+");
////                    } else {
////                        setFillter("[a-zA-Z0-9]+");
////                    }
////                } else {
////                    setFillter("[a-zA-Z0-9]+");
////                }
//
//
//                    if (length != 0) {
//                        for (int i = 0; i < length; i++) {
//
//                            String v = edt_search_suggestion.getText().toString().substring(i);
//                            Log.i("LastText", v);
//                            boolean digitsOnly = TextUtils.isDigitsOnly(v);
//                            if (digitsOnly) {
//                                numlength++;
//                                if (numlength > 4) {
//                                    inTxt = inTxt.replace(inTxt.substring(i), "");
//
//                                    edt_search_suggestion.setText(inTxt);
//                                    edt_search_suggestion.setSelection(inTxt.length());
//                                }
//                            } else {
//                                txtlength++;
//                                if (txtlength > 2) {
//                                    inTxt = inTxt.replace(inTxt.substring(i), "");
//                                    edt_search_suggestion.setText(inTxt);
//                                    edt_search_suggestion.setSelection(inTxt.length());
//                                }
//                            }
//                        }
//
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private boolean CheckValitation() {
        int length = edt_search_suggestion.getText().toString().length();
        int numlength = 0;
        int txtlength = 0;
        for (int i = 0; i < length; i++) {

            String v = edt_search_suggestion.getText().toString().substring(i);
            Log.i("LastText", v);
            boolean digitsOnly = TextUtils.isDigitsOnly(v);
            if (digitsOnly) {
                numlength++;
                if (numlength > 4) {
                    return false;
                }
            } else {
                txtlength++;
                if (txtlength > 2) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setFillter() {
        edt_search_suggestion.setInputType(InputType.TYPE_CLASS_TEXT);
        edt_search_suggestion.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) {
                            return src;
                        }
                        Log.i("Entert Text", String.valueOf(src));
//                        if (src.toString().matches(fillterMatch)) {
//                            return src;
//                        }

                        int length = src.length();
                        int numlength = 0;
                        int txtlength = 0;
                        if (length != 0) {
                            for (int i = 0; i < length; i++) {
                                boolean digitsOnly = TextUtils.isDigitsOnly(String.valueOf(src).substring(i));
                                if (digitsOnly) {
                                    numlength++;
                                    if (numlength > 4) {
                                        src = String.valueOf(src).replace(String.valueOf(src).substring(i), "");
                                        return src;
                                    }
                                } else {
                                    txtlength++;
                                    if (txtlength > 2) {
                                        src = String.valueOf(src).replace(String.valueOf(src).substring(i), "");
                                        return src;
                                    }
                                }
                            }
//                            if (numlength <= 4 && txtlength <= 2) {
//                                return src;
//                            } else if (numlength == 4 && txtlength < 2) {
////                                setFillter("[a-zA-Z]+");
//                                boolean digitsOnly = TextUtils.isDigitsOnly(String.valueOf(src).substring(length - 1));
//                                if (digitsOnly) {
//                                    String t = String.valueOf(src).replace(String.valueOf(src).substring(length - 1), "");
//                                    return t;
//                                } else {
//                                    return src;
//                                }
//                            } else if (numlength < 4 && txtlength == 2) {
//                                boolean digitsOnly = TextUtils.isDigitsOnly(String.valueOf(src).substring(length - 1));
//                                if (digitsOnly) {
//                                    return src;
//                                } else {
//                                    String t = String.valueOf(src).replace(String.valueOf(src).substring(length - 1), "");
//                                    return t;
//                                }
//                            } else {
//                                String t = String.valueOf(src).replace(String.valueOf(src).substring(length - 1), "");
//                                return t;
//                            }

                            return src;
                        }


                        return "";
                    }
                }
        });
    }

    private void getProsperIdList() {
        if (!params.isEmpty()) {
            params.clear();
        }

        params.put("prosper_id", edt_search_suggestion.getText().toString());
        Log.i("params", params.toString());
//        RetrofitClient.getClient().create(Api.class).getProsperId(edt_search_suggestion.getText().toString())
//                .enqueue(new RetrofitCallBack(UpgradeActivity.this, prosperIdListAPI, true));

        if (_fun.isInternetAvailable(UpgradeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).searchProsperId(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UpgradeActivity.this, prosperIdListAPI, true, false));
        } else {
            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).searchProsperId(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(UpgradeActivity.this, prosperIdListAPI, true, false));
                }
            });

        }

    }

    private void getPrice() {
        RetrofitClient.getClient().create(Api.class).getPromotionPrice(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(UpgradeActivity.this, priceResponse, true, false));
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
            case R.id.img_search:
                if (edt_search_suggestion.getText().length() == 0) {
                    Function.CustomMessage(UpgradeActivity.this, "Enter Prosper Id");
                } else if (edt_search_suggestion.getText().length() < 4) {
                    Function.CustomMessage(UpgradeActivity.this, "Enter minimum 4 digits");
                } else if (!CheckValitation()) {
                    Function.CustomMessage(UpgradeActivity.this, "Enter valid inputs");
                } else {
                    getProsperIdList();
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

    public void startPayment(String amount, String orderId) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
//        co.setKeyID("rzp_test_A2vBDkxnd63ve4");
        int totalAmount = Integer.valueOf(amount) * 100;
        Log.i("Amount", String.valueOf(totalAmount));
        try {
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.app_name));
            options.put("description", "Fancy Prosper ID Purchase");
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(totalAmount));
            options.put("order_id", orderId);

            JSONObject preFill = new JSONObject();
            preFill.put("email", Sessions.getSession(Constant.UserEmail, getApplicationContext()));
            preFill.put("contact", Sessions.getSession(Constant.UserPhone, getApplicationContext()));
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

//    @Override
//    public void onPaymentSuccess(String razorpayPaymentID) {
//        try {
//            paymentId = razorpayPaymentID;
//            savePromotion();
//        } catch (Exception e) {
//            Log.e(TAG, "Exception in onPaymentSuccess", e);
//        }
//    }
//
//    @Override
//    public void onPaymentError(int code, String response) {
//        try {
//            if (promotionType.equals("2")) {
//                layout_upgrade.setVisibility(View.GONE);
//                layout_error.setVisibility(View.VISIBLE);
//            } else if (promotionType.equals("3")) {
//                layout_upgrade.setVisibility(View.GONE);
//                layout_error.setVisibility(View.VISIBLE);
//            } else {
//                layout_search_list.setVisibility(View.GONE);
//                layout_error.setVisibility(View.VISIBLE);
//            }
////            savePromotion();
//            Log.e("Payment:", code + " " + response);
//        } catch (Exception e) {
//            Log.e(TAG, "Exception in onPaymentError", e);
//        }
//    }

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
                    .enqueue(new RetrofitCallBack(UpgradeActivity.this, validatePromotionApi, true, false));
        } else {
            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).checkPromotionValid(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(UpgradeActivity.this, validatePromotionApi, true, false));
                }
            });
        }
    }

    public void savePromotion() {
        if (!params.isEmpty()) {
            params.clear();
        }


//        razorpay_payment_id,razorpay_order_id,razorpay_signature
        params.put("razorpay_payment_id", paymentId);
        params.put("razorpay_order_id", orderId);
        params.put("razorpay_signature", paymentSignature);

        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(UpgradeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).savePromotion(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UpgradeActivity.this, savePromotionApi, true, false));
        } else {
            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).savePromotion(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(UpgradeActivity.this, savePromotionApi, true, false));
                }
            });
        }
    }


//    public void savePromotion() {
//        if (!params.isEmpty()) {
//            params.clear();
//        }
//        String totalAmount = "";
//        if (promotionType.equals("2")) {
//            totalAmount = txt_top_price.getText().toString().trim();
//        } else if (promotionType.equals("3")) {
//            totalAmount = txt_premium_price.getText().toString().trim();
//        } else {
//            totalAmount = prosperAmount;
//        }
//        params.put("type", promotionType);
//        params.put("transaction_id", paymentId);
//        params.put("total_amount", totalAmount);
//        params.put("order_id", orderId);
//        if (!promotionType.equals("1")) {
//
//            params.put("start_date", adStartDate);
//            params.put("end_date", adEndDate);
//            params.put("ads_id", adId);
//            params.put("category_id", catId);
//
//        } else {
//            params.put("prosper_id", selectedId);
//        }
//        Log.i("params", params.toString());
//
//
//        if (_fun.isInternetAvailable(UpgradeActivity.this)) {
//            RetrofitClient.getClient().create(Api.class).savePromotion(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
//                    .enqueue(new RetrofitCallBack(UpgradeActivity.this, savePromotionApi, true));
//        } else {
//            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
//                @Override
//                public void isInternet() {
//                    RetrofitClient.getClient().create(Api.class).savePromotion(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
//                            .enqueue(new RetrofitCallBack(UpgradeActivity.this, savePromotionApi, true));
//                }
//            });
//        }
//    }


    public void getOrderId() {
        if (!params.isEmpty()) {
            params.clear();
        }
        String totalAmount = "";
        if (promotionType.equals("2")) {
            totalAmount = txt_top_price.getText().toString().trim();
        } else if (promotionType.equals("3")) {
            totalAmount = txt_premium_price.getText().toString().trim();
        } else {
            totalAmount = prosperAmount;
        }
        params.put("type", promotionType);
        params.put("transaction_id", paymentId);
        params.put("total_amount", totalAmount);
        params.put("name", gstName);
        params.put("gst_no", gstNo);
        params.put("address", gstAddress);
        params.put("gst_id", gstId);
        if (!promotionType.equals("1")) {

            params.put("start_date", adStartDate);
            params.put("end_date", adEndDate);
            params.put("ads_id", adId);
            params.put("category_id", catId);

        } else {
            params.put("prosper_id", selectedId);
        }
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(UpgradeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getOrderId(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UpgradeActivity.this, getOrderIdApi, true, false));
        } else {
            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getOrderId(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(UpgradeActivity.this, getOrderIdApi, true, false));
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


    retrofit2.Callback<DefaultResponse> getOrderIdApi = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    String totalAmount = "";
                    if (promotionType.equals("2")) {
                        totalAmount = txt_top_price.getText().toString().trim();
                    } else if (promotionType.equals("3")) {
                        totalAmount = txt_premium_price.getText().toString().trim();
                    } else {
                        totalAmount = prosperAmount;
                    }
                    orderId = dr.getOrderId();
                    startPayment(totalAmount, dr.getOrderId());
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
                        startPayment("", "");
                    } else {
                        _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                startPayment("", "");
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
        String text = "<font color=#000000>" + getString(R.string.prosper_id) + "</font> <font color=#F2672E>" + " " + proId + " " + "</font>" + "<font color=#000000>" + getString(R.string.txt_has_been_selected);
        txt_prosper_content.setText(Html.fromHtml(text));
//        txt_prosper_content.setText(getString(R.string.prosper_id) + " " + proId + " " + getString(R.string.txt_has_been_selected));
        prosperAmount = amount;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ShowPopupStateCity();
                if (Sessions.getSession(Constant.RegStateId, getApplicationContext())!=null&&Sessions.getSession(Constant.RegStateId, getApplicationContext()).equals("")) {
                    ShowPopupStateCity();
                } else {
                    spinnerPopup.dismiss();
                    isAddressEnable = false;
                    ShowPopupGST();
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

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        try {
            paymentId = paymentData.getPaymentId();
            paymentSignature = paymentData.getSignature();
            orderId = paymentData.getOrderId();

            savePromotion();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData) {
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


    public void ShowPopupGST() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_gst_bill, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(false);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        LinearLayout layout_input_gst = view.findViewById(R.id.layout_input_gst);
        LinearLayout layout_message = view.findViewById(R.id.layout_message);
        layout_address = view.findViewById(R.id.layout_address);
        txt_company_name = view.findViewById(R.id.txt_company_name);
        txt_company_address = view.findViewById(R.id.txt_company_address);
        edt_gst_no_bill = view.findViewById(R.id.edt_gst_no_bill);
        Button btn_yes = view.findViewById(R.id.btn_yes);
        Button btn_no = view.findViewById(R.id.btn_no);
        Button btn_input_back = view.findViewById(R.id.btn_input_back);
        Button btn_input_continue = view.findViewById(R.id.btn_input_continue);
        RecyclerView rc_gst_no = view.findViewById(R.id.rc_gst_no);
        gstListAdapter = new GSTListAdapter(gstNoList, UpgradeActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpgradeActivity.this, RecyclerView.VERTICAL, false);
        gridManager = new GridLayoutManager(UpgradeActivity.this, 2);
        rc_gst_no.setItemAnimator(new DefaultItemAnimator());
        rc_gst_no.setLayoutManager(linearLayoutManager);
        rc_gst_no.setAdapter(gstListAdapter);
        gstListAdapter.setCallback(new GSTListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                edt_gst_no_bill.setText(gstNoList.get(pos).getGstNo());
                gstId = gstNoList.get(pos).getGstId();
                gstNo = gstNoList.get(pos).getGstNo();
                gstAddress = gstNoList.get(pos).getCompanyAddress();
                gstName = gstNoList.get(pos).getCompanyName();
                layout_address.setVisibility(View.VISIBLE);
                txt_company_name.setText(gstName);
                txt_company_address.setText(gstAddress);
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
                getOrderId();
            }
        });
        btn_input_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_input_gst.setVisibility(View.GONE);
                layout_address.setVisibility(View.GONE);
                layout_message.setVisibility(View.VISIBLE);
                txt_company_name.setText("");
                txt_company_address.setText("");
                edt_gst_no_bill.setText("");
            }
        });
        btn_input_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Function.hideSoftKeyboard(UpgradeActivity.this, v);
                if (isAddressEnable && !edt_gst_no_bill.getText().toString().equals("") && gstNo.equals(edt_gst_no_bill.getText().toString())) {
                    gstName = txt_company_name.getText().toString();
                    gstAddress = txt_company_address.getText().toString();
                    if (gstName.length() == 0) {
//                        Toast.makeText(getApplicationContext(), getString(R.string.enter_company_name), Toast.LENGTH_SHORT).show();
                        Function.CustomMessage(UpgradeActivity.this, getString(R.string.enter_company_name));
                        return;
                    }
                    if (!Function.GSTNameValidation(gstName)) {
//                        Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_company_name), Toast.LENGTH_SHORT).show();
                        Function.CustomMessage(UpgradeActivity.this, getString(R.string.enter_valid_company_name));
                        return;
                    }
                    if (gstAddress.length() == 0) {
//                        Toast.makeText(getApplicationContext(), getString(R.string.enter_company_address), Toast.LENGTH_SHORT).show();
                        Function.CustomMessage(UpgradeActivity.this, getString(R.string.enter_company_address));
                        return;
                    }
                    getOrderId();
                    spinnerPopup.dismiss();
                } else {
                    boolean isSelect = false;
                    for (int i = 0; i < gstNoList.size(); i++) {
                        if (gstNoList.get(i).getGstNo().toLowerCase().equals(edt_gst_no_bill.getText().toString().toLowerCase())) {
                            gstId = gstNoList.get(i).getGstId();
                            gstNo = gstNoList.get(i).getGstNo();
                            gstAddress = gstNoList.get(i).getCompanyAddress();
                            gstName = gstNoList.get(i).getCompanyName();
                            layout_address.setVisibility(View.VISIBLE);
                            txt_company_name.setText(gstName);
                            txt_company_address.setText(gstAddress);
                            isSelect = true;
                            break;
                        }
                    }
                    if (!isSelect)
                        doValidationGST(edt_gst_no_bill.getText().toString());
                }

            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_input_gst.setVisibility(View.VISIBLE);
                layout_message.setVisibility(View.GONE);
                getGSTNOList();
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void doValidationGST(String _gstNo) {

        if (_gstNo.length() == 0) {
//            Toast.makeText(getApplicationContext(), getString(R.string.enter_gst_no), Toast.LENGTH_SHORT).show();
            Function.CustomMessage(UpgradeActivity.this, getString(R.string.enter_gst_no));
            return;
        }
        if (!Function.GSTNoValidation(_gstNo)) {
//            Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_gst_no), Toast.LENGTH_SHORT).show();
            Function.CustomMessage(UpgradeActivity.this, getString(R.string.enter_valid_gst_no));
            return;
        }
        gstNo = _gstNo;
        if (_fun.isInternetAvailable(UpgradeActivity.this)) {
            verifyGSTNO(_gstNo);
        } else {
            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    verifyGSTNO(_gstNo);
                }
            });


        }
    }

    private void getGSTNOList() {
        RetrofitClient.getClient().create(Api.class).gstNoList(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(UpgradeActivity.this, gstNoListResponse, true, false));
    }

    private void verifyGSTNO(String gstNo) {
        RetrofitClient.getClientGST().create(Api.class).verifyGST(gstNo, Constant.Authorization, Constant.Content_Type, Constant.ClientId)
                .enqueue(new RetrofitCallBack(UpgradeActivity.this, gstResponse, true, false));
    }

    retrofit2.Callback<String> gstResponse = new retrofit2.Callback<String>() {
        @Override
        public void onResponse(Call<String> call, retrofit2.Response<String> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());

            try {

                JSONObject json = new JSONObject(response.body());
                Object value = json.get("error");
                if (value instanceof Boolean) {
                    if (!json.getBoolean("error")) {

                        JSONObject json_data = json.getJSONObject("data");
                        if (json_data.getString("tradeNam").equals("")) {
                            txt_company_name.setText(json_data.getString("lgnm"));
                        } else {
                            txt_company_name.setText(json_data.getString("tradeNam"));
                        }
                        JSONObject json_data_pre = json_data.getJSONObject("pradr");
                        JSONObject json_data_address = json_data_pre.getJSONObject("addr");
                        String address = json_data_address.getString("bno") + "," +
                                json_data_address.getString("flno") + "," +
                                json_data_address.getString("bnm") + "," +
                                json_data_address.getString("st") + "," +
                                json_data_address.getString("dst") + "," +
                                json_data_address.getString("stcd") + " " +
                                json_data_address.getString("pncd");
                        txt_company_address.setText(address);
                        layout_address.setVisibility(View.VISIBLE);
                        isAddressEnable = true;
                        gstId = "0";
                    } else {
                        Function.CustomMessage(UpgradeActivity.this, "Enter a valid GST No");
//                        Toast.makeText(getApplicationContext(), "Enter a valid GST No", Toast.LENGTH_SHORT).show();
                        txt_company_name.setText("");
                        txt_company_address.setText("");
                        layout_address.setVisibility(View.VISIBLE);
                        isAddressEnable = true;
//                        gstNo = "";

                    }
                } else {


                    txt_company_name.setText("");
                    txt_company_address.setText("");
                    layout_address.setVisibility(View.VISIBLE);
                    isAddressEnable = true;
//                    gstNo = "";
                    if (value.equals("invalid_grant")) {
                        updateToken();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                txt_company_name.setText("");
                txt_company_address.setText("");
                layout_address.setVisibility(View.VISIBLE);
                isAddressEnable = true;
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            call.cancel();
            txt_company_name.setText("");
            txt_company_address.setText("");
            layout_address.setVisibility(View.VISIBLE);
            isAddressEnable = true;
        }
    };

    retrofit2.Callback<GSTLitsModel> gstNoListResponse = new retrofit2.Callback<GSTLitsModel>() {
        @Override
        public void onResponse(Call<GSTLitsModel> call, retrofit2.Response<GSTLitsModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());

            try {
                GSTLitsModel dr = response.body();
                if (dr.isStatus()) {
                    gstNoList.clear();
                    for (int i = 0; i < dr.getGstList().size(); i++) {
                        if (!dr.getGstList().get(i).getGstNo().equals("")) {
                            gstNoList.add(dr.getGstList().get(i));
                        }
                    }

                    gstListAdapter.notifyDataSetChanged();
                } else {

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

    retrofit2.Callback<ResponseBody> gstTokenResponse = new retrofit2.Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());

            try {
                if (response.isSuccessful()) {
                    doValidationGST(edt_gst_no_bill.getText().toString());
                } else {

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

    public void updateToken() {
        if (!params.isEmpty()) {
            params.clear();
        }

        params.put("username", "mayukh@torbit.in");
        params.put("password", "Torbit@123");
        params.put("client_id", "uaBInTAtsKXNexyIyj");
        params.put("client_secret", "ygyoPiaL0fUvdkJbQ4yYw9eT");
        params.put("grant_type", "password");

        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(UpgradeActivity.this)) {
            RetrofitClient.getTokenGST().create(Api.class).getToken(params)
                    .enqueue(new RetrofitCallBack(UpgradeActivity.this, gstTokenResponse, true, false));
        } else {
            _fun.ShowNoInternetPopup(UpgradeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getTokenGST().create(Api.class).getToken(params)
                            .enqueue(new RetrofitCallBack(UpgradeActivity.this, gstTokenResponse, true, false));
                }
            });
        }
    }

    public void ShowPopupStateCity() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_state_city, null);

        spinnerPopupCity = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopupCity.setOutsideTouchable(false);
        spinnerPopupCity.setFocusable(false);
        spinnerPopupCity.update();

        TextView txt_skip = view.findViewById(R.id.txt_skip);
        Button txt_done = view.findViewById(R.id.btn_ok);

        txt_skip.setVisibility(View.GONE);
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopupCity.dismiss();
            }
        });

        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopupCity.dismiss();
                Intent profile = new Intent(UpgradeActivity.this, ProfileActivity.class);
                profile.putExtra("PageFrom", "3");
                startActivity(profile);
            }
        });
        spinnerPopupCity.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
