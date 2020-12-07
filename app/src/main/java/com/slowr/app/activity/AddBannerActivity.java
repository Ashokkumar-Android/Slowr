package com.slowr.app.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.slowr.app.R;
import com.slowr.app.adapter.CityMultiSelectAdapter;
import com.slowr.app.adapter.ViewColorAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.BannerDetailsModel;
import com.slowr.app.models.CityChipModel;
import com.slowr.app.models.CityItemModel;
import com.slowr.app.models.ColorCodeItemModel;
import com.slowr.app.models.ColorModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.EditBannerModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;
import com.slowr.materialchips.ChipsInput;
import com.slowr.materialchips.model.ChipInterface;
import com.slowr.matisse.Matisse;
import com.slowr.matisse.MimeType;
import com.slowr.matisse.engine.impl.GlideEngine;
import com.slowr.matisse.internal.entity.CaptureStrategy;
import com.slowr.matisse.ui.MatisseActivity;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddBannerActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    EditText edt_banner_title;
    EditText edt_description;
    TextView txt_title_count;
    TextView txt_from_date;
    TextView txt_to_date;
    TextView txt_description_count;
    TextView txt_city_content;
    TextView txt_summary_content;
    TextView txt_total_price;
    TextView btn_confirm;
    TextView txt_guid_line;
    LinearLayout layout_city;
    LinearLayout layout_banner_bg;
    ImageView btn_add_image;
    ImageView img_banner_view;
    Button btn_preview_banner;
    RecyclerView rc_color_list;
    ScrollView layout_banner_form;
    LinearLayout layout_preview;
    LinearLayout layout_list;
    EditText edt_list_search;
    RecyclerView rc_list;
    TextView txt_page_title;
    LinearLayout img_back;
    TextView txt_page_action;
    View view_color;
    Button btn_customise;
    Button btn_done;
    TextView txt_preview_title;
    TextView txt_prosperId;
    TextView txt_preview_description;
    ImageView img_banner_preview;
    TextView txt_banner_duration;
    TextView txt_city;
    ChipsInput chips_input;

    ViewColorAdapter viewColorAdapter;
    ArrayList<ColorCodeItemModel> colorList = new ArrayList<>();
    ArrayList<CityItemModel> cityList = new ArrayList<>();
    ArrayList<CityChipModel> cityChipList = new ArrayList<>();
    ArrayList<CityChipModel> selectChipList = new ArrayList<>();

    private int mYear, mMonth, mDay;
    String adStartDate = "";
    String adEndDate = "";
    LinearLayoutManager listManager;
    private Function _fun = new Function();
    String imgPath = "";
    Uri selectedImage;
    MultipartBody.Part bannerImage = null;
    String colorCodeOne = "";
    String colorCodeTwo = "";
    private PopupWindow spinnerPopup;

    boolean isList = false;
    boolean isPreview = false;
    boolean isRenew = false;

    CityMultiSelectAdapter cityListAdapter;

    HashMap<String, Object> params = new HashMap<String, Object>();
    JsonArray jsonArray = null;
    HashMap<String, String> cityIdArray = null;
    String cityId = "";
    String totalAmount = "";
    String bannerId = "";
    String Type = "";
    String totalDays = "";
    String cityName = "";

    String paymentId = "";
    boolean changeDeductedAmount = false;
    boolean changeChip = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banner);
        doDeclaration();
    }

    private void doDeclaration() {
        Type = getIntent().getStringExtra("Type");
        if (Type.equals("2")) {
            bannerId = getIntent().getStringExtra("BannerID");
        }
        edt_banner_title = findViewById(R.id.edt_banner_title);
        txt_title_count = findViewById(R.id.txt_title_count);
        txt_from_date = findViewById(R.id.txt_from_date);
        txt_to_date = findViewById(R.id.txt_to_date);
        edt_description = findViewById(R.id.edt_description);
        txt_description_count = findViewById(R.id.txt_description_count);
        txt_city_content = findViewById(R.id.txt_city_content);
        layout_city = findViewById(R.id.layout_city);
        btn_add_image = findViewById(R.id.btn_add_image);
        img_banner_view = findViewById(R.id.img_banner_view);
        btn_preview_banner = findViewById(R.id.btn_preview_banner);
        txt_summary_content = findViewById(R.id.txt_summary_content);
        txt_total_price = findViewById(R.id.txt_total_price);
        btn_confirm = findViewById(R.id.btn_confirm);
        txt_guid_line = findViewById(R.id.txt_guid_line);
        rc_color_list = findViewById(R.id.rc_color_list);
        layout_banner_bg = findViewById(R.id.layout_banner_bg);
        layout_banner_form = findViewById(R.id.layout_banner_form);
        layout_preview = findViewById(R.id.layout_preview);
        layout_list = findViewById(R.id.layout_list);
        edt_list_search = findViewById(R.id.edt_list_search);
        rc_list = findViewById(R.id.rc_list);
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        txt_page_action = findViewById(R.id.txt_page_action);
        view_color = findViewById(R.id.view_color);
        btn_customise = findViewById(R.id.btn_customise);
        btn_done = findViewById(R.id.btn_done);
        txt_preview_title = findViewById(R.id.txt_preview_title);
        txt_prosperId = findViewById(R.id.txt_prosperId);
        txt_preview_description = findViewById(R.id.txt_preview_description);
        img_banner_preview = findViewById(R.id.img_banner_preview);
        txt_banner_duration = findViewById(R.id.txt_banner_duration);
        txt_city = findViewById(R.id.txt_city);
        chips_input = findViewById(R.id.chips_input);

        listManager = new LinearLayoutManager(AddBannerActivity.this, RecyclerView.HORIZONTAL, false);
        rc_color_list.setLayoutManager(listManager);
        rc_color_list.setItemAnimator(new DefaultItemAnimator());
        viewColorAdapter = new ViewColorAdapter(getApplicationContext(), colorList);
        rc_color_list.setAdapter(viewColorAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_list.setLayoutManager(linearLayoutManager);
        rc_list.setItemAnimator(new DefaultItemAnimator());
        cityListAdapter = new CityMultiSelectAdapter(cityList, getApplicationContext());
        rc_list.setAdapter(cityListAdapter);


        txt_page_title.setText(getString(R.string.txt_banner));
        txt_page_action.setText(getString(R.string.txt_ok));
        txt_page_action.setVisibility(View.GONE);
        img_back.setOnClickListener(this);
        txt_from_date.setOnClickListener(this);
        txt_to_date.setOnClickListener(this);
        layout_city.setOnClickListener(this);
        txt_page_action.setOnClickListener(this);
        btn_add_image.setOnClickListener(this);
        img_banner_view.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
        btn_customise.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        btn_preview_banner.setOnClickListener(this);
        Checkout.preload(getApplicationContext());
        CountSet();

        CallBackFunction();
        if (Type.equals("2")) {
            getBannerDetails();
        } else {
            getColor();
        }
        chips_input.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                if (!changeChip) {
                    selectChipList.add(new CityChipModel(chip.getId(), chip.getCityName(), chip.getPrice()));
                    SummaryContent();
                }
            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
                for (int i = 0; i < selectChipList.size(); i++) {
                    if (selectChipList.get(i).getId().equals(chip.getId())) {
                        selectChipList.remove(i);
                        break;
                    }
                }
                SummaryContent();
            }

            @Override
            public void onTextChanged(CharSequence text) {

            }

            @Override
            public void onKeyboardDone() {
                Function.hideSoftKeyboard(AddBannerActivity.this, img_back);
                layout_list.setVisibility(View.GONE);
                layout_banner_form.setVisibility(View.VISIBLE);
                txt_page_title.setText(getString(R.string.txt_banner));
                txt_page_action.setVisibility(View.GONE);
                isList = false;
                SummaryContent();
            }
        });
    }

    private void getBannerDetails() {
        if (_fun.isInternetAvailable(AddBannerActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getBannerDetails(bannerId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(AddBannerActivity.this, bannerDetailsResponse, false));
        } else {
            _fun.ShowNoInternetPopup(AddBannerActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getBannerDetails(bannerId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(AddBannerActivity.this, bannerDetailsResponse, false));
                }
            });
        }
    }

    private void CallBackFunction() {
        viewColorAdapter.setCallBack(new ViewColorAdapter.CallBack() {
            @Override
            public void itemClick(int pos) {
                for (int i = 0; i < colorList.size(); i++) {
                    if (i == pos) {
                        colorList.get(i).setChange(true);
                    } else {
                        colorList.get(i).setChange(false);
                    }
                }
                viewColorAdapter.notifyDataSetChanged();
                colorCodeOne = colorList.get(pos).getColorOne();
                colorCodeTwo = colorList.get(pos).getColorTwo();

                Function.GradientBgSet(layout_banner_bg, colorCodeOne, colorCodeTwo);
                Function.GradientBgSet(view_color, colorCodeOne, colorCodeTwo);
            }
        });
    }

    private void getColor() {
        if (_fun.isInternetAvailable(AddBannerActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getColorCode(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(AddBannerActivity.this, colorResponse, true));
        } else {
            _fun.ShowNoInternetPopup(AddBannerActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getColorCode(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(AddBannerActivity.this, colorResponse, true));
                }
            });
        }
    }

    private void CountSet() {
        txt_description_count.setText(getString(R.string.txt_des_count_banner, "0"));
        txt_title_count.setText(getString(R.string.txt_title_count, "0"));
        edt_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int desValue = edt_description.getText().toString().length();
                txt_description_count.setText(getString(R.string.txt_des_count_banner, String.valueOf(desValue)));
                if (desValue == 100 && !changeDeductedAmount) {
                    Function.CustomMessage(AddBannerActivity.this, getString(R.string.txt_limit_reached));
                }
            }
        });
        edt_banner_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int desValue = edt_banner_title.getText().toString().length();
                txt_title_count.setText(getString(R.string.txt_title_count, String.valueOf(desValue)));
                if (desValue == 55 && !changeDeductedAmount) {
                    Function.CustomMessage(AddBannerActivity.this, getString(R.string.txt_limit_reached));
                }
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
                cityListAdapter.getFilter().filter(edt_list_search.getText().toString());
            }
        });
    }

    private void SummaryContent() {

        try {

            cityName = "";
            cityId = "";
//            jsonArray = new JsonArray();
            cityIdArray = new HashMap<String, String>();
            int price = 0;
            for (int i = 0; i < selectChipList.size(); i++) {

                if (cityName.equals("")) {
                    cityName = selectChipList.get(i).getCityName();
                    price = Integer.valueOf(selectChipList.get(i).getPrice());
                    if (!adStartDate.equals("") && !adEndDate.equals("")) {
                        price = (price * Integer.valueOf(totalDays));
                    }
                    cityId = selectChipList.get(i).getId();
                } else {
                    cityName = cityName + "," + selectChipList.get(i).getCityName();
                    if (!adStartDate.equals("") && !adEndDate.equals("")) {
                        price = price + (Integer.valueOf(selectChipList.get(i).getPrice()) * Integer.valueOf(totalDays));
                    } else {
                        price = price + Integer.valueOf(selectChipList.get(i).getPrice());
                    }

                    cityId = cityId + "," + selectChipList.get(i).getId();
                }
//                    jsonArray.add(cityList.get(i).getCityId());
                cityIdArray.put(String.valueOf(cityIdArray.size()), selectChipList.get(i).getId());

            }
            txt_city_content.setText(cityName);
            totalAmount = String.valueOf(price);
            txt_total_price.setText(totalAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String contentTwo = "<font color=#000000>Banner Ad posted\nCity\n</font> <font color=#F2672E>" + cityName + "</font> <font color=#000000>\nduration\n</font><font color=#F2672E>" + totalDays + " days</font>";
        contentTwo = contentTwo.replace("\n", "<br>");
        txt_summary_content.setText(Html.fromHtml(contentTwo));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                if (isList) {
                    layout_list.setVisibility(View.GONE);
                    layout_banner_form.setVisibility(View.VISIBLE);
                    txt_page_title.setText(getString(R.string.txt_banner));
                    txt_page_action.setVisibility(View.GONE);
                    isList = false;
                } else if (isPreview) {
                    layout_banner_form.setVisibility(View.VISIBLE);
                    layout_preview.setVisibility(View.GONE);
                    isPreview = false;
                } else {
                    if (checkData() && !Type.equals("2") && !isRenew) {
                        WarningPopup();
                    } else {
                        finish();
                    }
                }
                break;
            case R.id.txt_from_date:
                Function.hideSoftKeyboard(AddBannerActivity.this, v);
                getDate("1");
                break;
            case R.id.txt_to_date:
                Function.hideSoftKeyboard(AddBannerActivity.this, v);
                getDate("2");
                break;
            case R.id.layout_city:
                Function.hideSoftKeyboard(AddBannerActivity.this, v);
                layout_list.setVisibility(View.VISIBLE);
                layout_banner_form.setVisibility(View.GONE);
                txt_page_title.setText(getString(R.string.txt_select) + " " + getString(R.string.txt_city_select));
                txt_page_action.setVisibility(View.VISIBLE);

                Function.openSoftKeyboard(AddBannerActivity.this, v);
                isList = true;
                break;
            case R.id.txt_page_action:
                Function.hideSoftKeyboard(AddBannerActivity.this, v);
                layout_list.setVisibility(View.GONE);
                layout_banner_form.setVisibility(View.VISIBLE);
                txt_page_title.setText(getString(R.string.txt_banner));
                txt_page_action.setVisibility(View.GONE);
                isList = false;
                edt_list_search.setText("");
//                setCity();
                SummaryContent();
                break;
            case R.id.btn_add_image:
                Function.hideSoftKeyboard(AddBannerActivity.this, v);
                if (_fun.checkPermission(AddBannerActivity.this)) {
                    MatisseActivity.PAGE_FROM = 2;
                    Matisse.from(AddBannerActivity.this)
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
            case R.id.img_banner_view:
                Function.hideSoftKeyboard(AddBannerActivity.this, v);
                if (_fun.checkPermission(AddBannerActivity.this)) {
                    MatisseActivity.PAGE_FROM = 2;
                    Matisse.from(AddBannerActivity.this)
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
            case R.id.btn_confirm:
                doValidation("1");
                break;
            case R.id.btn_customise:
                layout_banner_form.setVisibility(View.GONE);
                layout_preview.setVisibility(View.VISIBLE);
                txt_preview_title.setText(edt_banner_title.getText().toString());
                txt_prosperId.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
                txt_preview_description.setText(edt_description.getText().toString());
                isPreview = true;
                break;
            case R.id.btn_preview_banner:
                btn_customise.performClick();
                break;
            case R.id.btn_done:
                layout_banner_form.setVisibility(View.VISIBLE);
                layout_preview.setVisibility(View.GONE);
                isPreview = false;
                break;
        }

    }

    private void doValidation(String type) {
        String bannerTitle = edt_banner_title.getText().toString().trim();
        String description = edt_description.getText().toString().trim();


        if (bannerTitle.equals("")) {
            Function.CustomMessage(AddBannerActivity.this, getString(R.string.txt_enter_banner_title));
            return;
        }
        if (!Type.equals("2")) {
            if (!doDateValidation()) {
                return;
            }
        }
        if (description.equals("")) {
            Function.CustomMessage(AddBannerActivity.this, getString(R.string.enter_banner_description));
            return;
        }
        if (cityId.equals("")) {
            Function.CustomMessage(AddBannerActivity.this, getString(R.string.txt_select_city));
            return;
        }
        if (!Type.equals("2") && !isRenew) {
            if (imgPath.equals("")) {
                Function.CustomMessage(AddBannerActivity.this, getString(R.string.txt_upload_banner_image));
                return;
            }
        }
//        String totalDays = getCountOfDays(adStartDate, adEndDate);
        if (Type.equals("1")) {
            startPayment();
        } else {
            addBannerCall();
        }

    }

    private void addBannerCall() {
        String bannerTitle = edt_banner_title.getText().toString().trim();
        String description = edt_description.getText().toString().trim();
        if (!imgPath.equals("")) {
            File file = new File(Function.compressImage(imgPath));
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            bannerImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }
        final RequestBody bTitle = RequestBody.create(okhttp3.MultipartBody.FORM, bannerTitle);
        final RequestBody fromDate = RequestBody.create(okhttp3.MultipartBody.FORM, adStartDate);
        final RequestBody toDate = RequestBody.create(okhttp3.MultipartBody.FORM, adEndDate);
        final RequestBody bDescription = RequestBody.create(okhttp3.MultipartBody.FORM, description);
        final RequestBody _cityId = RequestBody.create(okhttp3.MultipartBody.FORM, cityId);
        final RequestBody bAmount = RequestBody.create(okhttp3.MultipartBody.FORM, totalAmount);
        final RequestBody bDays = RequestBody.create(okhttp3.MultipartBody.FORM, totalDays);
        final RequestBody bColors = RequestBody.create(okhttp3.MultipartBody.FORM, colorCodeOne + "," + colorCodeTwo);
        final RequestBody _bannerId = RequestBody.create(okhttp3.MultipartBody.FORM, bannerId);
        final RequestBody transactionId = RequestBody.create(okhttp3.MultipartBody.FORM, paymentId);
        final RequestBody promotionType = RequestBody.create(okhttp3.MultipartBody.FORM, "4");
        Log.i("params", totalDays + " , " + _cityId);


        if (_fun.isInternetAvailable(AddBannerActivity.this)) {
            if (Type.equals("1")) {
                RetrofitClient.getClient().create(Api.class).AddBanner(bannerImage, _bannerId, bTitle, fromDate, toDate, bDescription, _cityId, bAmount, bDays, bColors, transactionId, promotionType, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(AddBannerActivity.this, addBannerResponse, true));
            } else {
                RetrofitClient.getClient().create(Api.class).UpdateBanner(bannerImage, bTitle, _bannerId, bDescription, bColors, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(AddBannerActivity.this, addBannerResponse, true));
            }
        } else {
            _fun.ShowNoInternetPopup(AddBannerActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    if (Type.equals("1")) {
                        RetrofitClient.getClient().create(Api.class).AddBanner(bannerImage, _bannerId, bTitle, fromDate, toDate, bDescription, _cityId, bAmount, bDays, bColors, transactionId, promotionType, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                .enqueue(new RetrofitCallBack(AddBannerActivity.this, addBannerResponse, true));
                    } else {
                        RetrofitClient.getClient().create(Api.class).UpdateBanner(bannerImage, bTitle, _bannerId, bDescription, bColors, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                .enqueue(new RetrofitCallBack(AddBannerActivity.this, addBannerResponse, true));
                    }
                }
            });
        }
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
//        co.setKeyID("rzp_test_A2vBDkxnd63ve4");
        int tAmount = Integer.valueOf(totalAmount) * 100;
        try {
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.app_name));
            options.put("description", "Ad promotion");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount",  String.valueOf(tAmount));

            JSONObject preFill = new JSONObject();
            JSONObject themeStyle = new JSONObject();
            preFill.put("email", Sessions.getSession(Constant.UserEmail, getApplicationContext()));
            preFill.put("contact", Sessions.getSession(Constant.UserPhone, getApplicationContext()));
            themeStyle.put("color", "#0F4C81");

            options.put("prefill", preFill);
            options.put("theme", themeStyle);

            co.open(activity, options);
        } catch (Exception e) {
            Function.CustomMessage(AddBannerActivity.this, "Try Again.");
            e.printStackTrace();
        }
    }

    private boolean doDateValidation() {
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
            Function.CustomMessage(AddBannerActivity.this, getString(R.string.select_from_date));
            return false;
        }
        if (adEndDate.equals("")) {
            Function.CustomMessage(AddBannerActivity.this, getString(R.string.select_end_date));
            return false;
        }
        try {
            Log.i("Date Time", formStartDate + " + " + formEndDate);
            fDate = sdf.parse(formStartDate);
            tDate = sdf1.parse(formEndDate);
            cDate = sdf3.parse(formCurrentDate);

            if (tDate.before(fDate)) {
                Function.CustomMessage(AddBannerActivity.this, getString(R.string.event_date_validation));
                return false;

            } else {
                return true;
            }
        } catch (ParseException ex) {
            valid = false;
            Log.v("Exception", ex.getLocalizedMessage());
        }


        return false;
    }

    public void getDate(final String type) {
        try {
            final Calendar c = Calendar.getInstance();
            if (type.equals("1") && !adStartDate.equals("")) {
                String[] sDate = adStartDate.split("-");
                c.set(Calendar.YEAR, Integer.valueOf(sDate[0]));
                c.set(Calendar.MONTH, Integer.valueOf(sDate[1]) - 1);
                c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(sDate[2]));
            } else if (type.equals("2") && !adEndDate.equals("")) {
                String[] sDate = adEndDate.split("-");
                c.set(Calendar.YEAR, Integer.valueOf(sDate[0]));
                c.set(Calendar.MONTH, Integer.valueOf(sDate[1]) - 1);
                c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(sDate[2]));
            } else {
                c.add(Calendar.DAY_OF_MONTH, 1);

            }
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(AddBannerActivity.this,
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
                            if (!adStartDate.equals("") && !adEndDate.equals("")) {
                                totalDays = getCountOfDays(adStartDate, adEndDate);
                                SummaryContent();
                            }
//                                calculatePrice();
                        }
                    }, mYear, mMonth, mDay);
//            c.add(Calendar.DAY_OF_MONTH, 1);
            final Calendar c1 = Calendar.getInstance();
            c1.add(Calendar.DAY_OF_MONTH, 1);
            dpd.getDatePicker().setMinDate(c1.getTimeInMillis());
            dpd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

        return ("" + ((int) dayCount + 1));
    }

    retrofit2.Callback<EditBannerModel> bannerDetailsResponse = new retrofit2.Callback<EditBannerModel>() {
        @Override
        public void onResponse(Call<EditBannerModel> call, retrofit2.Response<EditBannerModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            EditBannerModel dr = response.body();
            try {
                if (dr.isStatus()) {
                    layout_banner_form.setVisibility(View.VISIBLE);
                    changeDeductedAmount = true;
                    BannerDetailsModel detailsModel = dr.getEditBannerDataModel().getBannerDetailsModel();
                    edt_banner_title.setText(detailsModel.getBannerTitle());
                    edt_description.setText(detailsModel.getDescription());
                    txt_from_date.setText(detailsModel.getFromDate());
                    adStartDate = detailsModel.getFromDate();
                    txt_to_date.setText(detailsModel.getToDate());
                    adEndDate = detailsModel.getToDate();
                    txt_city_content.setText(dr.getEditBannerDataModel().getCityNames());
                    cityName = dr.getEditBannerDataModel().getCityNames();
                    cityId = dr.getEditBannerDataModel().getCityIds();
                    txt_guid_line.setText(dr.getEditBannerDataModel().getGuideLines());
                    txt_total_price.setText(detailsModel.getTotalAmount());
                    totalDays = detailsModel.getTotalDays();

                    Glide.with(AddBannerActivity.this)
                            .load(dr.getEditBannerDataModel().getBannerImage())
                            .placeholder(R.drawable.ic_default_vertical)
                            .error(R.drawable.ic_default_vertical)
                            .into(img_banner_view);

                    Glide.with(AddBannerActivity.this)
                            .load(dr.getEditBannerDataModel().getBannerImage())
                            .placeholder(R.drawable.ic_default_vertical)
                            .error(R.drawable.ic_default_vertical)
                            .into(img_banner_preview);
                    colorList.clear();
                    String[] col = dr.getEditBannerDataModel().getBannerDetailsModel().getColorCode().split(",");
                    for (int i = 0; i < dr.getEditBannerDataModel().getColorCode().size(); i++) {
                        if (dr.getEditBannerDataModel().getColorCode().get(i).getColorOne().equals(col[0]) && dr.getEditBannerDataModel().getColorCode().get(i).getColorTwo().equals(col[1])) {
                            colorList.add(new ColorCodeItemModel(dr.getEditBannerDataModel().getColorCode().get(i).getColorOne(), dr.getEditBannerDataModel().getColorCode().get(i).getColorTwo(), true));
                            colorCodeOne = dr.getEditBannerDataModel().getColorCode().get(i).getColorOne();
                            colorCodeTwo = dr.getEditBannerDataModel().getColorCode().get(i).getColorTwo();

                        } else {
                            colorList.add(new ColorCodeItemModel(dr.getEditBannerDataModel().getColorCode().get(i).getColorOne(), dr.getEditBannerDataModel().getColorCode().get(i).getColorTwo(), true));
                        }
                    }
                    viewColorAdapter.notifyDataSetChanged();
                    Function.GradientBgSet(layout_banner_bg, colorCodeOne, colorCodeTwo);
                    Function.GradientBgSet(view_color, colorCodeOne, colorCodeTwo);
                    cityList.clear();
                    for (int i = 0; i < dr.getEditBannerDataModel().getCityList().size(); i++) {
                        boolean isAdd = false;
                        if (!cityId.contains(",")) {
                            if (dr.getEditBannerDataModel().getCityList().get(i).getCityId().equals(cityId)) {
                                cityList.add(new CityItemModel(dr.getEditBannerDataModel().getCityList().get(i).getCityId(), dr.getEditBannerDataModel().getCityList().get(i).getCityName(), dr.getEditBannerDataModel().getCityList().get(i).getCityPrice(), true));
                                selectChipList.add(new CityChipModel(dr.getEditBannerDataModel().getCityList().get(i).getCityId(), dr.getEditBannerDataModel().getCityList().get(i).getCityName(), dr.getEditBannerDataModel().getCityList().get(i).getCityPrice()));
                            } else {
                                cityList.add(new CityItemModel(dr.getEditBannerDataModel().getCityList().get(i).getCityId(), dr.getEditBannerDataModel().getCityList().get(i).getCityName(), dr.getEditBannerDataModel().getCityList().get(i).getCityPrice(), false));
                            }
                        } else {
                            String[] cIds = cityId.split(",");
                            for (int s = 0; s < cIds.length; s++) {
                                if (dr.getEditBannerDataModel().getCityList().get(i).getCityId().equals(cIds[s])) {
                                    cityList.add(new CityItemModel(dr.getEditBannerDataModel().getCityList().get(i).getCityId(), dr.getEditBannerDataModel().getCityList().get(i).getCityName(), dr.getEditBannerDataModel().getCityList().get(i).getCityPrice(), true));
                                    selectChipList.add(new CityChipModel(dr.getEditBannerDataModel().getCityList().get(i).getCityId(), dr.getEditBannerDataModel().getCityList().get(i).getCityName(), dr.getEditBannerDataModel().getCityList().get(i).getCityPrice()));
                                    isAdd = true;
                                    break;
                                }
                            }
                            if (!isAdd)
                                cityList.add(new CityItemModel(dr.getEditBannerDataModel().getCityList().get(i).getCityId(), dr.getEditBannerDataModel().getCityList().get(i).getCityName(), dr.getEditBannerDataModel().getCityList().get(i).getCityPrice(), false));
                        }
                        cityChipList.add(new CityChipModel(dr.getEditBannerDataModel().getCityList().get(i).getCityId(), dr.getEditBannerDataModel().getCityList().get(i).getCityName(), dr.getEditBannerDataModel().getCityList().get(i).getCityPrice()));
                    }
                    changeChip = true;
                    for (int z = 0; z < selectChipList.size(); z++) {
//                        chips_input.addChip(selectChipList.get(z).getId(),selectChipList.get(z).getCityName(),selectChipList.get(z).getPrice());
                        chips_input.addChip(selectChipList.get(z));
                    }
                    changeChip = false;
                    chips_input.setFilterableList(cityChipList);
//                    cityList.addAll(dr.getEditBannerDataModel().getCityList());
                    cityListAdapter.notifyDataSetChanged();
                    btn_confirm.setText(getString(R.string.txt_update_banner));
                    if (!detailsModel.getAdStatus().equals("3")) {
                        txt_to_date.setEnabled(false);
                        txt_from_date.setEnabled(false);
                        layout_city.setEnabled(false);
                        txt_to_date.setTextColor(getResources().getColor(R.color.hint_txt_color));
                        txt_from_date.setTextColor(getResources().getColor(R.color.hint_txt_color));
                        txt_banner_duration.setTextColor(getResources().getColor(R.color.hint_txt_color));
                        txt_city.setTextColor(getResources().getColor(R.color.hint_txt_color));
                        txt_city_content.setTextColor(getResources().getColor(R.color.hint_txt_color));
                    } else {
                        txt_to_date.setText("");
                        txt_from_date.setText("");
                        txt_from_date.setText("");
                        btn_confirm.setText(getString(R.string.txt_confirm));
                        adStartDate = "";
                        adEndDate = "";
                        totalDays = "0";
                        Type = "1";
                        isRenew = true;
                    }
                    SummaryContent();
                    changeDeductedAmount = false;
                } else {
                    Function.CustomMessage(AddBannerActivity.this, dr.getMessage());
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
    retrofit2.Callback<ColorModel> colorResponse = new retrofit2.Callback<ColorModel>() {
        @Override
        public void onResponse(Call<ColorModel> call, retrofit2.Response<ColorModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            ColorModel dr = response.body();
            try {
                if (dr.isStatus()) {
                    layout_banner_form.setVisibility(View.VISIBLE);
                    colorList.clear();
                    for (int i = 0; i < dr.getColorItemModel().getColorCode().size(); i++) {
                        if (i == 0) {
                            colorList.add(new ColorCodeItemModel(dr.getColorItemModel().getColorCode().get(i).getColorOne(), dr.getColorItemModel().getColorCode().get(i).getColorTwo(), true));
                        } else {
                            colorList.add(new ColorCodeItemModel(dr.getColorItemModel().getColorCode().get(i).getColorOne(), dr.getColorItemModel().getColorCode().get(i).getColorTwo(), false));
                        }
                    }
                    viewColorAdapter.notifyDataSetChanged();
                    colorCodeOne = colorList.get(0).getColorOne();
                    colorCodeTwo = colorList.get(0).getColorTwo();
                    Function.GradientBgSet(layout_banner_bg, colorCodeOne, colorCodeTwo);
                    Function.GradientBgSet(view_color, colorCodeOne, colorCodeTwo);
                    txt_guid_line.setText(dr.getColorItemModel().getGuideLines());
                    cityList.clear();
                    cityList.addAll(dr.getColorItemModel().getCityList());
                    for (int i = 0; i < cityList.size(); i++) {
                        cityChipList.add(new CityChipModel(cityList.get(i).getCityId(), cityList.get(i).getCityName(), cityList.get(i).getCityPrice()));
                    }
                    chips_input.setFilterableList(cityChipList);
                    cityListAdapter.notifyDataSetChanged();
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

    retrofit2.Callback<DefaultResponse> addBannerResponse = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            DefaultResponse dr = response.body();
            try {
                if (dr.isStatus()) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);

                    ShowPopupSuccess(dr.getMessage());
                } else {
                    Function.CustomMessage(AddBannerActivity.this, dr.getMessage());
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
        if (isList) {
            layout_list.setVisibility(View.GONE);
            layout_banner_form.setVisibility(View.VISIBLE);
            txt_page_title.setText(getString(R.string.txt_banner));
            txt_page_action.setVisibility(View.GONE);
            isList = false;
        } else if (isPreview) {
            layout_banner_form.setVisibility(View.VISIBLE);
            layout_preview.setVisibility(View.GONE);
            isPreview = false;
        } else {
            if (checkData() && !Type.equals("2") && !isRenew) {
                WarningPopup();
            } else {
                finish();
                super.onBackPressed();
            }
        }

    }

    public boolean checkData() {
        return !edt_banner_title.getText().toString().equals("") || !edt_description.getText().toString().equals("") || !adStartDate.equals("") || !adEndDate.equals("") || !cityId.equals("") || !(imgPath.equals(""));
    }

    private void WarningPopup() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                AddBannerActivity.this);
        alertDialog2.setMessage(getString(R.string.ad_discard));
        alertDialog2.setPositiveButton("DISCARD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog2.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50) {
            if (data != null) {
                List<String> slist = Matisse.obtainPathResult(data);
                selectedImage = data.getData();
                if (slist.size() > 0) {
                    for (int i = 0; i < slist.size(); i++) {
//                        imgPath = Function.getBase64String(slist.get(i));
                        imgPath = slist.get(i);
                        Glide.with(this)
                                .load(slist.get(i))
                                .placeholder(R.drawable.ic_default_vertical)
                                .error(R.drawable.ic_default_vertical)
                                .into(img_banner_view);

                        Glide.with(this)
                                .load(slist.get(i))
                                .placeholder(R.drawable.ic_default_vertical)
                                .error(R.drawable.ic_default_vertical)
                                .into(img_banner_preview);
                    }
                }


            }


        }
    }

    public void ShowPopupSuccess(String mesg) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_post_success, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(false);
        spinnerPopup.update();
        TextView txt_content_one = view.findViewById(R.id.txt_content_one);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        txt_content_one.setText(mesg);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
                finish();
            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        paymentId = razorpayPaymentID;
        addBannerCall();
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {

            Log.e("Payment:", code + " " + response);
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentError", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = false;
        for (int i = 0; i < Constant.Permissions.length; i++) {
            int result1 = ContextCompat.checkSelfPermission(AddBannerActivity.this, Constant.Permissions[i]);
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        if (result) {
            MatisseActivity.PAGE_FROM = 2;
            Matisse.from(AddBannerActivity.this)
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


}
