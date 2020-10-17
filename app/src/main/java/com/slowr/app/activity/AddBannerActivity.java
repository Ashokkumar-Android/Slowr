package com.slowr.app.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.adapter.CityMultiSelectAdapter;
import com.slowr.app.adapter.ViewColorAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.CityItemModel;
import com.slowr.app.models.ColorModel;
import com.slowr.app.models.UploadImageModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;
import com.slowr.matisse.Matisse;
import com.slowr.matisse.MimeType;
import com.slowr.matisse.engine.impl.GlideEngine;
import com.slowr.matisse.internal.entity.CaptureStrategy;
import com.slowr.matisse.ui.MatisseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class AddBannerActivity extends AppCompatActivity implements View.OnClickListener {

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

    ViewColorAdapter viewColorAdapter;
    ArrayList<UploadImageModel> colorList = new ArrayList<>();
    ArrayList<CityItemModel> cityList = new ArrayList<>();

    private int mYear, mMonth, mDay;
    String adStartDate = "";
    String adEndDate = "";
    LinearLayoutManager listManager;
    private Function _fun = new Function();
    String imgPath = "";
    Uri selectedImage;

    boolean isList = false;

    CityMultiSelectAdapter cityListAdapter;

    HashMap<String, Object> params = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banner);
        doDeclaration();
    }

    private void doDeclaration() {
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
        img_back.setOnClickListener(this);
        txt_from_date.setOnClickListener(this);
        txt_to_date.setOnClickListener(this);
        layout_city.setOnClickListener(this);
        txt_page_action.setOnClickListener(this);
        btn_add_image.setOnClickListener(this);
        img_banner_view.setOnClickListener(this);
        CountSet();
        getColor();
        CallBackFunction();
    }

    private void CallBackFunction() {
        viewColorAdapter.setCallBack(new ViewColorAdapter.CallBack() {
            @Override
            public void itemClick(int pos) {
                for (int i = 0; i < colorList.size(); i++) {
                    if (i == pos) {
                        colorList.get(i).setChanged(true);
                    } else {
                        colorList.get(i).setChanged(false);
                    }
                }
                viewColorAdapter.notifyDataSetChanged();
                layout_banner_bg.setBackgroundColor(Color.parseColor(colorList.get(pos).getImgURL()));
            }
        });
    }

    private void getColor() {

        RetrofitClient.getClient().create(Api.class).getColorCode(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(AddBannerActivity.this, colorResponse, false));
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
                if (desValue == 100) {
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
                if (desValue == 60) {
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
                } else {
                    finish();
                }
                break;
            case R.id.txt_from_date:
                getDate("1");
                break;
            case R.id.txt_to_date:
                getDate("2");
                break;
            case R.id.layout_city:
                layout_list.setVisibility(View.VISIBLE);
                layout_banner_form.setVisibility(View.GONE);
                txt_page_title.setText(getString(R.string.txt_select) + " " + getString(R.string.txt_city_select));
                txt_page_action.setVisibility(View.VISIBLE);
                isList = true;
                break;
            case R.id.txt_page_action:
                layout_list.setVisibility(View.GONE);
                layout_banner_form.setVisibility(View.VISIBLE);
                txt_page_title.setText(getString(R.string.txt_banner));
                txt_page_action.setVisibility(View.GONE);
                isList = false;
                setCity();
                break;
            case R.id.btn_add_image:
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
        }

    }

    private void setCity() {
        try {


            String cityName = "";
            int price = 0;
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).isSelect()) {
                    if (cityName.equals("")) {
                        cityName = cityList.get(i).getCityName();
                        price = Integer.valueOf(cityList.get(i).getCityPrice());
                    } else {
                        cityName = cityName + "," + cityList.get(i).getCityName();
                        price = price + Integer.valueOf(cityList.get(i).getCityPrice());
                    }
                }
            }
            txt_city_content.setText(cityName);
            txt_total_price.setText(String.valueOf(price));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDate(final String type) {
        try {
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, 1);
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

                            }
//                                calculatePrice();
                        }
                    }, mYear, mMonth, mDay);
//            c.add(Calendar.DAY_OF_MONTH, 1);
            dpd.getDatePicker().setMinDate(c.getTimeInMillis());
            dpd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    retrofit2.Callback<ColorModel> colorResponse = new retrofit2.Callback<ColorModel>() {
        @Override
        public void onResponse(Call<ColorModel> call, retrofit2.Response<ColorModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            ColorModel dr = response.body();
            try {
                if (dr.isStatus()) {
                    colorList.clear();
                    for (int i = 0; i < dr.getColorItemModel().getColorCode().size(); i++) {
                        if (i == 0) {
                            colorList.add(new UploadImageModel(dr.getColorItemModel().getColorCode().get(i), true, "", ""));
                        } else {
                            colorList.add(new UploadImageModel(dr.getColorItemModel().getColorCode().get(i), false, "", ""));
                        }
                    }
                    viewColorAdapter.notifyDataSetChanged();
                    layout_banner_bg.setBackgroundColor(Color.parseColor(colorList.get(0).getImgURL()));
                    cityList.clear();
                    cityList.addAll(dr.getColorItemModel().getCityList());
                    cityListAdapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {
        if (isList) {
            layout_list.setVisibility(View.GONE);
            layout_banner_form.setVisibility(View.VISIBLE);
            txt_page_title.setText(getString(R.string.txt_banner));
            txt_page_action.setVisibility(View.GONE);
            isList = false;
        } else {
            finish();
            super.onBackPressed();
        }

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
                                .placeholder(R.drawable.ic_default_profile)
                                .error(R.drawable.ic_default_profile)
                                .into(img_banner_view);
                    }
                }


            }


        }
    }
}
