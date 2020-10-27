package com.slowr.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.slowr.app.R;
import com.slowr.app.adapter.AreaAutoCompleteAdapter;
import com.slowr.app.adapter.AreaListAdapter;
import com.slowr.app.adapter.AttributeListAdapter;
import com.slowr.app.adapter.AttributeSelectAdapter;
import com.slowr.app.adapter.CategoryListAdapter;
import com.slowr.app.adapter.CityListAdapter;
import com.slowr.app.adapter.PostImageListAdapter;
import com.slowr.app.adapter.ProductAutoCompleteAdapter;
import com.slowr.app.adapter.RentalDurationAdapter;
import com.slowr.app.adapter.SubCategoryListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.components.otpview.OnOtpCompletionListener;
import com.slowr.app.components.otpview.OtpView;
import com.slowr.app.models.AreaItemModel;
import com.slowr.app.models.AreaModel;
import com.slowr.app.models.AttributeItemModel;
import com.slowr.app.models.AttributeModel;
import com.slowr.app.models.AttributeSelectModel;
import com.slowr.app.models.CategoryItemModel;
import com.slowr.app.models.CategoryModel;
import com.slowr.app.models.CityItemModel;
import com.slowr.app.models.CityModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.EditAdDetailsModel;
import com.slowr.app.models.EditAdModel;
import com.slowr.app.models.SubCategoryItemModel;
import com.slowr.app.models.UploadImageModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;
import com.slowr.matisse.Matisse;
import com.slowr.matisse.MimeType;
import com.slowr.matisse.engine.impl.GlideEngine;
import com.slowr.matisse.internal.entity.CaptureStrategy;
import com.slowr.matisse.ui.MatisseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    LinearLayout img_back;
    TextView txt_page_title;
    TextView txt_parent_title;
    TextView txt_category_content;
    TextView txt_sub_category_content;
    TextView txt_product_type_count;
    TextView txt_area_count;
    AppCompatAutoCompleteTextView txt_product_type_content;
    TextView txt_city_content;
    AppCompatAutoCompleteTextView txt_area_content;
    TextView txt_post_title_content;
    ImageView btn_add_image;
    RecyclerView rc_category;
    RecyclerView rc_image_list;
    RecyclerView rc_attributes;
    RecyclerView rc_service;
    NestedScrollView layout_post_details;
    //    Spinner sp_price_type;
    EditText edt_price;
    EditText edt_description;
    EditText edt_mobile_number;
    EditText edt_search;
    LinearLayout layout_list;
    FrameLayout layout_product_type;
    LinearLayout layout_sub_category;
    LinearLayout layout_child_category;
    LinearLayout layout_city;
    FrameLayout layout_area;
    Button btn_add_post;
    CheckBox cb_price;
    CheckBox cb_mobile_number;
    CheckBox cb_tc;
    Button btn_product;
    Button btn_service;
    LinearLayout layout_selection_button;
    LinearLayout layout_input_price;
    TextView txt_product_type_title;
    TextView txt_rental_duration_title;
    TextView txt_rental_fee_title;
    LinearLayout layout_image_upload;
    TextView txt_description_count;
    TextView txt_privacy_policy;
    TextView txt_page_action;
    ImageView img_drop_down;
    LinearLayout layout_otp;
    OtpView otp_view;
    TextView txt_resend_otp;
    TextView txt_image_content;
    TextView txt_rental_duration_content;
    Button btn_verify_otp;
    View view_rental_fee;
    LinearLayout layout_rental_duration;
    ToggleButton tb_offer_need;
    FrameLayout layout_search;
    TextView txt_product_count;
    ImageView img_search;
    LinearLayout layout_product_container;

    CategoryListAdapter categoryListAdapter;
    SubCategoryListAdapter subCategoryListAdapter;
    AttributeListAdapter attributeListAdapter;
    CityListAdapter cityListAdapter;
    AreaListAdapter areaListAdapter;
    AttributeSelectAdapter attributeSelectAdapter;
    RentalDurationAdapter rentalDurationAdapter;
    ArrayAdapter<String> spinnerAdapter;

    ArrayList<CategoryItemModel> categoryList = new ArrayList<>();
    ArrayList<CategoryItemModel> tempCategoryList = new ArrayList<>();
    ArrayList<CategoryItemModel> tempServiceList = new ArrayList<>();
    ArrayList<SubCategoryItemModel> subCategoryList = new ArrayList<>();
    ArrayList<AttributeItemModel> attributeList = new ArrayList<>();
    ArrayList<CityItemModel> cityList = new ArrayList<>();
    ArrayList<AreaItemModel> areaList = new ArrayList<>();
    ArrayList<AttributeSelectModel> attributeValueList = new ArrayList<>();

    public List<Uri> selectedUris = new ArrayList<>();
    public List<String> selectedPaths = new ArrayList<>();
    public List<String> rentalDurationList = new ArrayList<>();
    ArrayList<UploadImageModel> shareImageList = new ArrayList<>();
    public int isAddIcon = 0;
    public int attributeSelectPos = 0;
    public int filterType = 1;
    public boolean isCity = false;
    public boolean isSubCat = false;
    public boolean isPriceVisible = true;
    public boolean isPostView = false;
    public boolean isRequirement = false;
    String catId = "";
    String parentId = "";
    String chatId = "";
    String cityId = "";
    String areaId = "";
    String rentalDuration = "";
    String rentalDurationTitle = "";
    String adId = "";
    String adStatus = "0";
    String productTitle = "";
    String attributeTitle = "";
    String postMobileNum = "";
    int rentalDurationPos = 0;
    boolean isAttributes = false;

    int tabNo = 1;
    int AdType = 0;
    String EditType = "";
    boolean isOTPView = false;
    boolean isEdit = false;
    String otp = "";
    int resentCount = 0;
    private PopupWindow spinnerPopup;

    private Function _fun = new Function();
    PostImageListAdapter postImageListAdapter;
    ProductAutoCompleteAdapter productAutoCompleteAdapter;
    AreaAutoCompleteAdapter areaAutoCompleteAdapter;

    HashMap<String, Object> params = new HashMap<String, Object>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        doDeclaration();

    }

    private void doDeclaration() {
        AdType = getIntent().getIntExtra("AdType", 0);
        if (AdType == 1) {
            catId = getIntent().getStringExtra("CatId");
            adId = getIntent().getStringExtra("AdId");
            EditType = getIntent().getStringExtra("EditType");
        }
        img_back = findViewById(R.id.img_back);
        txt_page_title = findViewById(R.id.txt_page_title);
        txt_page_title.setText(getString(R.string.txt_add_post));
        rc_category = findViewById(R.id.rc_category);
        rc_image_list = findViewById(R.id.rc_image_list);
        txt_parent_title = findViewById(R.id.txt_parent_title);
        txt_category_content = findViewById(R.id.txt_category_content);
        txt_sub_category_content = findViewById(R.id.txt_sub_category_content);
        layout_post_details = findViewById(R.id.layout_post_details);
        layout_list = findViewById(R.id.layout_list);
        layout_sub_category = findViewById(R.id.layout_sub_category);
        layout_child_category = findViewById(R.id.layout_child_category);
        btn_add_image = findViewById(R.id.btn_add_image);
//        sp_price_type = findViewById(R.id.sp_price_type);
        edt_price = findViewById(R.id.edt_price);
        rc_attributes = findViewById(R.id.rc_attributes);
        layout_city = findViewById(R.id.layout_city);
        txt_city_content = findViewById(R.id.txt_city_content);
        layout_area = findViewById(R.id.layout_area);
        txt_area_content = findViewById(R.id.txt_area_content);
        btn_add_post = findViewById(R.id.btn_add_post);
        edt_description = findViewById(R.id.edt_description);
        cb_price = findViewById(R.id.cb_price);
        edt_mobile_number = findViewById(R.id.edt_mobile_number);
        txt_product_type_content = findViewById(R.id.txt_product_type_content);
        layout_product_type = findViewById(R.id.layout_product_type);
        txt_post_title_content = findViewById(R.id.txt_post_title_content);
        edt_search = findViewById(R.id.edt_search);
        cb_mobile_number = findViewById(R.id.cb_mobile_number);
        btn_product = findViewById(R.id.btn_product);
        btn_service = findViewById(R.id.btn_service);
        layout_selection_button = findViewById(R.id.layout_selection_button);
        layout_input_price = findViewById(R.id.layout_input_price);
        txt_product_type_title = findViewById(R.id.txt_product_type_title);
        txt_rental_duration_title = findViewById(R.id.txt_rental_duration_title);
        txt_rental_fee_title = findViewById(R.id.txt_rental_fee_title);
        layout_image_upload = findViewById(R.id.layout_image_upload);
        txt_description_count = findViewById(R.id.txt_description_count);
        txt_privacy_policy = findViewById(R.id.txt_privacy_policy);
        cb_tc = findViewById(R.id.cb_tc);
        img_drop_down = findViewById(R.id.img_drop_down);
        layout_otp = findViewById(R.id.layout_otp);
        otp_view = findViewById(R.id.otp_view);
        txt_resend_otp = findViewById(R.id.txt_resend_otp);
        btn_verify_otp = findViewById(R.id.btn_verify_otp);
        txt_image_content = findViewById(R.id.txt_image_content);
        view_rental_fee = findViewById(R.id.view_rental_fee);
        txt_page_action = findViewById(R.id.txt_page_action);
        txt_rental_duration_content = findViewById(R.id.txt_rental_duration_content);
        layout_rental_duration = findViewById(R.id.layout_rental_duration);
        rc_service = findViewById(R.id.rc_service);
        tb_offer_need = findViewById(R.id.tb_offer_need);
        layout_search = findViewById(R.id.layout_search);
        txt_product_count = findViewById(R.id.txt_product_count);
        img_search = findViewById(R.id.img_search);
        layout_product_container = findViewById(R.id.layout_product_container);
        txt_product_type_count = findViewById(R.id.txt_product_type_count);
        txt_area_count = findViewById(R.id.txt_area_count);

        txt_page_action.setText(getString(R.string.txt_done));
        txt_page_action.setVisibility(View.GONE);

        otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String _otp) {
                otp = _otp;
            }
        });
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(AddPostActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rc_image_list.setLayoutManager(horizontalLayoutManagaer);
        postImageListAdapter = new PostImageListAdapter(AddPostActivity.this, shareImageList);
        rc_image_list.setAdapter(postImageListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_category.setLayoutManager(linearLayoutManager);
        rc_category.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_service.setLayoutManager(linearLayoutManager6);
        rc_service.setItemAnimator(new DefaultItemAnimator());

        categoryListAdapter = new CategoryListAdapter(categoryList, getApplicationContext());

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_attributes.setLayoutManager(linearLayoutManager2);
        rc_attributes.setItemAnimator(new DefaultItemAnimator());
        attributeListAdapter = new AttributeListAdapter(attributeList, AddPostActivity.this);
        rc_attributes.setAdapter(attributeListAdapter);

        subCategoryListAdapter = new SubCategoryListAdapter(subCategoryList, getApplicationContext());
        attributeSelectAdapter = new AttributeSelectAdapter(attributeValueList, getApplicationContext());
        cityListAdapter = new CityListAdapter(cityList, getApplicationContext());
        areaListAdapter = new AreaListAdapter(areaList, AddPostActivity.this);
        areaCallBack(areaListAdapter);
        rentalDurationAdapter = new RentalDurationAdapter(rentalDurationList, getApplicationContext());


        spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.layout_spinner_item, rentalDurationList);
// Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(R.layout.layout_spinner_item);
// Apply the adapter to the spinner
//        sp_price_type.setAdapter(spinnerAdapter);
//        sp_price_type.setOnItemSelectedListener(this);

        img_back.setOnClickListener(this);
        btn_add_post.setOnClickListener(this);
        layout_sub_category.setOnClickListener(this);
        layout_child_category.setOnClickListener(this);
        btn_add_image.setOnClickListener(this);
        layout_city.setOnClickListener(this);
        layout_area.setOnClickListener(this);
        layout_product_type.setOnClickListener(this);
        btn_product.setOnClickListener(this);
        btn_service.setOnClickListener(this);
//        img_drop_down.setOnClickListener(this);
        txt_resend_otp.setOnClickListener(this);
        btn_verify_otp.setOnClickListener(this);
        txt_page_action.setOnClickListener(this);
        layout_rental_duration.setOnClickListener(this);
        layout_product_container.setOnClickListener(this);
        txt_area_content.setOnClickListener(this);

//        onClickFunction();
        if (AdType == 0) {
            getCategory();
        } else if (AdType == 1) {
            isEdit = true;
            GetAdDetails();
            btn_add_post.setText(getString(R.string.txt_update_post));
            layout_list.setVisibility(View.GONE);
            isPostView = false;
            layout_post_details.setVisibility(View.GONE);
            if (!EditType.equals("1")) {
                layout_image_upload.setVisibility(View.GONE);
                layout_input_price.setVisibility(View.GONE);
                view_rental_fee.setVisibility(View.GONE);
                AdType = 2;
            }
        } else if (AdType == 2) {
            layout_image_upload.setVisibility(View.GONE);
            layout_input_price.setVisibility(View.GONE);
            view_rental_fee.setVisibility(View.GONE);
            getCategory();
            isRequirement = true;
            tb_offer_need.setChecked(true);
        }

        CallBackFunction();

        edt_mobile_number.setText(Sessions.getSession(Constant.UserPhone, getApplicationContext()));
        doFilter();
        GetCity();
        TCPPTextView(txt_privacy_policy);
        onClickFunction();
        layout_post_details.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return false;
            }
        });

    }

    public void TCPPTextView(TextView textView) {

        SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append(getString(R.string.txt_ad_post_content) + " ");
        spanText.append(getString(R.string.txt_ad_post_tc));
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent i = new Intent(AddPostActivity.this, PolicyActivity.class);
                i.putExtra("PageFrom", "2");
                startActivity(i);

            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.txt_orange));    // you can use custom color
                textPaint.setUnderlineText(false);    // this remove the underline
            }
        }, spanText.length() - getString(R.string.txt_ad_post_tc).length(), spanText.length(), 0);
        spanText.append(" *");

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spanText, TextView.BufferType.SPANNABLE);

    }

    private void GetAdDetails() {


        if (_fun.isInternetAvailable(AddPostActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getAdDetails(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, adDetails, true));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            RetrofitClient.getClient().create(Api.class).getAdDetails(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                    .enqueue(new RetrofitCallBack(AddPostActivity.this, adDetails, true));
                        }
                    });
                }
            }, 200);

        }
    }

    private void doFilter() {
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (filterType) {
                    case 1:
                        subCategoryListAdapter.getFilter().filter(edt_search.getText().toString());
                        break;
                    case 3:
                        attributeSelectAdapter.getFilter().filter(edt_search.getText().toString());
                        break;
                    case 4:
                        cityListAdapter.getFilter().filter(edt_search.getText().toString());
                        break;
                    case 5:
                        areaListAdapter.getFilter().filter(edt_search.getText().toString());
                        break;
                }

            }
        });
    }

    private void CallBackFunction() {
        txt_description_count.setText(getString(R.string.txt_des_count, "0"));
        txt_product_count.setText(getString(R.string.txt_pro_count, "0"));
        txt_product_type_count.setText(getString(R.string.txt_pro_count, "0"));
        txt_area_count.setText(getString(R.string.txt_pro_count, "0"));
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
                txt_description_count.setText(getString(R.string.txt_des_count, String.valueOf(desValue)));
                if (desValue == 200) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.txt_limit_reached));
                }
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int desValue = edt_search.getText().toString().length();
                txt_product_count.setText(getString(R.string.txt_pro_count, String.valueOf(desValue)));
                if (desValue == 50) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.txt_limit_reached));
                }
            }
        });
        txt_product_type_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int desValue = txt_product_type_content.getText().toString().length();
                txt_product_type_count.setText(getString(R.string.txt_pro_count, String.valueOf(desValue)));
                if (desValue == 50) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.txt_limit_reached));
                }
            }
        });
        txt_area_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int desValue = txt_area_content.getText().toString().length();
                txt_area_count.setText(getString(R.string.txt_pro_count, String.valueOf(desValue)));
                areaId = txt_area_content.getText().toString();
                if (desValue == 50) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.txt_limit_reached));
                }
            }
        });
        postImageListAdapter.setCallBack(new PostImageListAdapter.CallBack() {
            @Override
            public void itemClick(int pos) {

//                selectedPaths.remove(pos);
//                selectedUris.remove(pos);
                shareImageList.remove(pos);
                postImageListAdapter.notifyDataSetChanged();
                if (shareImageList.size() != 0) {
                    btn_add_image.setVisibility(View.GONE);
//                        view_gallery.setVisibility(View.GONE);
                    rc_image_list.setVisibility(View.VISIBLE);
                } else {
                    btn_add_image.setVisibility(View.VISIBLE);
//                        view_gallery.setVisibility(View.VISIBLE);
                    rc_image_list.setVisibility(View.GONE);
                }
//                if (!isPageChange) {
//                    isPageChange = true;
//                    isAddIcon = 0;
//                    Intent intent = new Intent(getApplicationContext(), MatisseCropActivity.class);
//                    intent.putParcelableArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION, (ArrayList<? extends Parcelable>) selectedUris);
//                    intent.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH, (ArrayList<String>) selectedPaths);
////                    intent.putStringArrayListExtra(MatisseActivity.EXTRA_RESULT_CROP_TYPE, CropTypes);
//                    intent.putExtra("pos", pos);
//                    startActivityForResult(intent, 50);
//                }
               /* showPopupWindow(pos);
                vp_position = pos;*/
            }

            @Override
            public void addClick() {
                isAddIcon = 1;
                Log.e("CallBackFunction", "addClick");
                if (_fun.checkPermission(AddPostActivity.this)) {
                    MatisseActivity.PAGE_FROM = 1;
                    Matisse.from(AddPostActivity.this)
                            .choose(MimeType.of(MimeType.PNG, MimeType.JPEG), true)
                            .countable(true)
                            .capture(true)
                            .theme(R.style.Matisse_Dracula)
                            .captureStrategy(
                                    new CaptureStrategy(true, "com.slowr.app.provider", "Android/data/com.slowr.app/files/Pictures"))
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.gride_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .maxSelectable(5 - (shareImageList.size()))
                            .showSingleMediaType(true)
                            .forResult(50);
                }


            }
        });

        attributeListAdapter.setCallback(new AttributeListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                attributeSelectPos = pos;
                String[] strArray = attributeList.get(pos).getAttributeValues().split(",");
                attributeValueList.clear();
                for (int i = 0; i < strArray.length; i++) {
                    attributeValueList.add(new AttributeSelectModel("", strArray[i]));
                }

                rc_category.setAdapter(attributeSelectAdapter);
                filterType = 3;
                txt_page_action.setVisibility(View.VISIBLE);

                ListVisible(true);
                edt_search.setImeOptions(EditorInfo.IME_ACTION_DONE);
                txt_page_title.setText(getString(R.string.txt_type) + " " + attributeList.get(pos).getName());
                edt_search.setText(attributeList.get(pos).getInputValue());
                attributeSelectAdapter.getFilter().filter("");
                isCity = true;

            }

            @Override
            public void attributeEnterValue(String val, int pos) {
                attributeTitle = "";
                for (int i = 0; i < attributeList.size(); i++) {
                    if (attributeList.get(i).getIsTitle().equals("1")) {
                        if (attributeTitle.equals("")) {
                            attributeTitle = attributeList.get(i).getInputValue().trim();
                        } else {
                            attributeTitle = attributeTitle + " " + attributeList.get(i).getInputValue().trim();
                            attributeTitle = attributeTitle.trim();
                        }
                    }
                }

                if (tabNo == 1) {
                    if (AdType == 2) {
                        txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                    } else {
                        txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                    }
                } else {
                    if (AdType == 2) {
                        txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                    } else
                        txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                }
            }
        });
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);

                    if (filterType == 1) {
                        txt_product_type_content.setText(edt_search.getText().toString());
                        if (tabNo == 1) {
                            productTitle = edt_search.getText().toString();
                            if (AdType == 2) {
                                txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                            } else {
                                txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                            }
                        } else {
                            productTitle = edt_search.getText().toString();
                            if (AdType == 2) {
                                txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                            } else {
                                txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                            }
                        }

                        Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);

                        if (_fun.isInternetAvailable(AddPostActivity.this)) {
                            GetAttributes(edt_search.getText().toString());
                        } else {
                            _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {
                                    GetAttributes(edt_search.getText().toString());
                                }
                            });
                        }
                    } else if (filterType == 3) {
                        attributeList.get(attributeSelectPos).setInputValue(edt_search.getText().toString());
                        attributeListAdapter.notifyDataSetChanged();
                        attributeTitle = "";
                        for (int i = 0; i < attributeList.size(); i++) {
                            if (attributeList.get(i).getIsTitle().equals("1")) {
                                if (attributeTitle.equals("")) {
                                    attributeTitle = attributeList.get(i).getInputValue().trim();
                                } else {
                                    attributeTitle = attributeTitle + " " + attributeList.get(i).getInputValue().trim();
                                }
                            }
                        }

                        if (tabNo == 1) {
                            if (AdType == 2) {
                                txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                            } else {
                                txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                            }
                        } else {
                            if (AdType == 2) {
                                txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                            } else {
                                txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                            }
                        }
                    } else {
                        txt_area_content.setText(edt_search.getText().toString());
                        areaId = edt_search.getText().toString();
                    }
                    ListVisible(false);
                    isCity = false;
                    return true;
                }
                return false;
            }
        });
        attributeSelectAdapter.setCallback(new AttributeSelectAdapter.Callback() {
            @Override
            public void itemClick(AttributeSelectModel model) {
                Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);
                attributeList.get(attributeSelectPos).setInputValue(model.getAttributeValue());
                attributeListAdapter.notifyDataSetChanged();
                attributeTitle = "";
                for (int i = 0; i < attributeList.size(); i++) {
                    if (attributeList.get(i).getIsTitle().equals("1")) {
                        if (attributeTitle.equals("")) {
                            attributeTitle = attributeList.get(i).getInputValue().trim();
                        } else {

                            attributeTitle = attributeTitle + " " + attributeList.get(i).getInputValue().trim();
                            attributeTitle = attributeTitle.trim();
                        }
                    }
                }

                if (tabNo == 1) {
                    if (AdType == 2) {
                        txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                    } else {
                        txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                    }
                } else {
                    if (AdType == 2) {
                        txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                    } else {
                        txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                    }
                }

                ListVisible(false);
                isCity = false;
            }
        });
        rentalDurationAdapter.setCallback(new RentalDurationAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                txt_rental_duration_content.setText(rentalDurationList.get(pos));
                setRentalDuration(pos);
                ListVisible(false);
                isCity = false;
            }
        });

        tb_offer_need.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AdType = 2;
                    layout_image_upload.setVisibility(View.GONE);
                    layout_input_price.setVisibility(View.GONE);
                    view_rental_fee.setVisibility(View.GONE);
                    isRequirement = true;
                } else {
                    AdType = 0;
                    if (tabNo == 1) {
                        layout_image_upload.setVisibility(View.VISIBLE);
                    }
                    layout_input_price.setVisibility(View.VISIBLE);
                    view_rental_fee.setVisibility(View.VISIBLE);
                    isRequirement = false;
                }


                if (!productTitle.equals("")) {

                    if (tabNo == 1) {
                        if (AdType == 2) {
                            if (attributeTitle.equals("")) {
                                txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                            } else {
                                txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                            }

                        } else {
                            if (attributeTitle.equals("")) {
                                txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                            } else {
                                txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                            }
                        }
                    } else {
                        if (AdType == 2) {
                            if (attributeTitle.equals("")) {
                                txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                            } else {
                                txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                            }

                        } else {
                            if (attributeTitle.equals("")) {
                                txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                            } else {
                                txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                            }
                        }
                    }


                }
            }
        });
    }

    private void setRentalDuration(int pos) {
        rentalDuration = rentalDurationList.get(pos);
        if (pos == 0) {
            rentalDurationTitle = getString(R.string.txt_hourly);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.VISIBLE);
            }
            isPriceVisible = true;
        } else if (pos == 1) {
            rentalDurationTitle = getString(R.string.txt_daily);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.VISIBLE);
            }
            isPriceVisible = true;
        } else if (pos == 2) {
//            layout_input_price.setVisibility(View.INVISIBLE);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.INVISIBLE);
            }
            isPriceVisible = false;
            edt_price.setText("");
            rentalDurationTitle = rentalDurationList.get(pos);
        }
        if (!productTitle.equals("")) {

            if (tabNo == 1) {
                if (AdType == 2) {
                    txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                } else {
                    txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                }
            } else {
                if (AdType == 2) {
                    txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                } else {
                    txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                }
            }


        }
    }


    public void ListVisible(boolean isVisible) {
        if (isVisible) {
            isCity = true;
            layout_list.setVisibility(View.VISIBLE);
            layout_post_details.setVisibility(View.GONE);
            tb_offer_need.setVisibility(View.GONE);
            layout_search.setVisibility(View.VISIBLE);
            edt_search.setText("");
        } else {
            txt_page_action.setVisibility(View.GONE);
            isCity = false;
            layout_list.setVisibility(View.GONE);
            layout_post_details.setVisibility(View.VISIBLE);
            if (!isEdit)
                tb_offer_need.setVisibility(View.VISIBLE);
            layout_search.setVisibility(View.GONE);
            txt_page_title.setText(getString(R.string.txt_add_post));
            img_search.setVisibility(View.GONE);
            txt_product_count.setVisibility(View.VISIBLE);
        }
        edt_search.setImeOptions(EditorInfo.IME_ACTION_NEXT);

    }

    private void onClickFunction() {
        categoryListAdapter.setCallback(new CategoryListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                isPostView = true;
                txt_parent_title.setText(categoryList.get(pos).getName());
                parentId = categoryList.get(pos).getId();
                ClearFileds();
                layout_selection_button.setVisibility(View.GONE);
                subCategoryList.clear();
                subCategoryList.addAll(categoryList.get(pos).getSubCategoryList());
                rc_category.setVisibility(View.VISIBLE);
                rc_service.setVisibility(View.GONE);

                ListVisible(false);
                ClearFileds();
                isSubCat = false;

                productAutoCompleteAdapter = new ProductAutoCompleteAdapter(AddPostActivity.this, R.layout.layout_sub_category_item, subCategoryList);
                txt_product_type_content.setThreshold(3);
                txt_product_type_content.setAdapter(productAutoCompleteAdapter);
                txt_product_type_content.setSelected(true);
                SubCategoryCallBack();

            }
        });


    }

    private void getCategory() {

        if (_fun.isInternetAvailable(AddPostActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getCategory()
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, categoryApi, true));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            RetrofitClient.getClient().create(Api.class).getCategory()
                                    .enqueue(new RetrofitCallBack(AddPostActivity.this, categoryApi, true));
                        }
                    });
                }
            }, 200);

        }

    }


    retrofit2.Callback<CategoryModel> categoryApi = new retrofit2.Callback<CategoryModel>() {
        @Override
        public void onResponse(Call<CategoryModel> call, retrofit2.Response<CategoryModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                CategoryModel categoryModel = response.body();

                if (categoryModel.isStatus()) {
                    categoryList.clear();
                    tempCategoryList.addAll(categoryModel.getCategoryServiceModel().getProductList());
                    tempServiceList.addAll(categoryModel.getCategoryServiceModel().getServiceList());
                    categoryList.addAll(categoryModel.getCategoryServiceModel().getProductList());


//                    categoryExpandAdapter = new CategoryExpandAdapter(getApplicationContext(), categoryList);
                    rc_service.setAdapter(categoryListAdapter);

                    rentalDurationList.clear();
                    rentalDurationList.addAll(categoryModel.getCategoryServiceModel().getRentalDurationList());
                    rentalDuration = rentalDurationList.get(0);
                    setRentalDuration(0);
                    txt_rental_duration_content.setText(rentalDurationList.get(0));
//                    spinnerAdapter.notifyDataSetChanged();
                    rentalDurationAdapter.notifyDataSetChanged();
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


    private void SubCategoryCallBack() {

        subCategoryListAdapter.setCallback(new SubCategoryListAdapter.Callback() {
            @Override
            public void itemClick(final SubCategoryItemModel model) {
//                subCategoryChildList.clear();
//                subCategoryChildList.addAll(model.getSubCategoryList());
                txt_product_type_content.setText(model.getSubcategoryName().trim());
                txt_product_type_content.setSelection(txt_product_type_content.getText().toString().length());
                if (tabNo == 1) {
                    productTitle = model.getSubcategoryName().trim();
                    if (AdType == 2) {
                        txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                    } else {
                        txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                    }
                } else {
                    productTitle = model.getSubcategoryName().trim();
                    if (AdType == 2) {
                        txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                    } else {
                        txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                    }
                }

                Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);

                if (_fun.isInternetAvailable(AddPostActivity.this)) {
                    GetAttributes(model.getSubcategoryName());
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            GetAttributes(model.getSubcategoryName());
                        }
                    });
                }
//                if (subCategoryChildList.size() == 0) {
//                    txt_product_type_content.setText(model.getSubcategoryName().trim());
//                    if (tabNo == 1) {
//                        productTitle = model.getSubcategoryName().trim();
//                        if (AdType == 2) {
//                            txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
//                        } else {
//                            txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
//                        }
//                    } else {
//                        productTitle = model.getSubcategoryName().trim();
//                        if (AdType == 2) {
//                            txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
//                        } else {
//                            txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
//                        }
//                    }
//
//                    Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);
//
//                    if (_fun.isInternetAvailable(AddPostActivity.this)) {
//                        GetAttributes(model.getSubcategoryId());
//                    } else {
//                        _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
//                            @Override
//                            public void isInternet() {
//                                GetAttributes(model.getSubcategoryId());
//                            }
//                        });
//                    }
//                } else {
//                    isSubCat = true;
//                    rc_category.setAdapter(childCategoryListAdapter);
//                    filterType = 2;
//                    ChildCategoryCallBack();
//                    ClearFileds();
//                }

                ListVisible(false);

                txt_category_content.setText(model.getSubcategoryName());
//                ChildCategoryCallBack();
            }
        });
        productAutoCompleteAdapter.setCallback(new ProductAutoCompleteAdapter.Callback() {
            @Override
            public void itemClick(SubCategoryItemModel model) {
                txt_product_type_content.setText(model.getSubcategoryName());
                txt_product_type_content.dismissDropDown();
                txt_product_type_content.setSelection(txt_product_type_content.getText().toString().length());
                if (tabNo == 1) {
                    productTitle = model.getSubcategoryName().trim();
                    if (AdType == 2) {
                        txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                    } else {
                        txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                    }
                } else {
                    productTitle = model.getSubcategoryName().trim();
                    if (AdType == 2) {
                        txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                    } else {
                        txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                    }
                }

                Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);

                if (_fun.isInternetAvailable(AddPostActivity.this)) {
                    GetAttributes(model.getSubcategoryName());
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            GetAttributes(model.getSubcategoryName());
                        }
                    });
                }
            }
        });
        txt_product_type_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isAttributes = false;
            }
        });
        txt_product_type_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !isAttributes) {
                    productTitle = txt_product_type_content.getText().toString().trim();
                    if (tabNo == 1) {

                        if (AdType == 2) {
                            txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                        } else {
                            txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                        }
                    } else {
                        if (AdType == 2) {
                            txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                        } else {
                            txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                        }
                    }

                    Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);

                    if (_fun.isInternetAvailable(AddPostActivity.this)) {
                        GetAttributes(productTitle);
                    } else {
                        _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                GetAttributes(productTitle);
                            }
                        });
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_image:
                if (_fun.checkPermission(AddPostActivity.this)) {
                    MatisseActivity.PAGE_FROM = 1;
                    Matisse.from(AddPostActivity.this)
                            .choose(MimeType.of(MimeType.PNG, MimeType.JPEG), true)
//                            .choose(MimeType.of(MimeType.GIF), false)
//                            .choose(MimeType.ofAll())
                            .countable(true)
                            .capture(true)
                            .theme(R.style.Matisse_Dracula)
                            .captureStrategy(
                                    new CaptureStrategy(true, "com.slowr.app.provider", "Android/data/com.slowr.app/files/Pictures"))
                            .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.gride_size))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .imageEngine(new GlideEngine())
                            .maxSelectable(5)
                            .showSingleMediaType(true)
                            .forResult(50);


                }
                break;
            case R.id.img_back:
                Function.hideSoftKeyboard(AddPostActivity.this, v);
                if (isCity) {
                    isCity = false;
                    ListVisible(false);

                } else if (isOTPView) {
                    layout_otp.setVisibility(View.GONE);
                    layout_post_details.setVisibility(View.VISIBLE);
                    if (!isEdit)
                        tb_offer_need.setVisibility(View.VISIBLE);
                } else if (isSubCat) {
                    isSubCat = false;
                    layout_list.setVisibility(View.VISIBLE);
                    layout_post_details.setVisibility(View.GONE);
                    tb_offer_need.setVisibility(View.GONE);
                    layout_search.setVisibility(View.GONE);
                    rc_category.setAdapter(subCategoryListAdapter);
                } else if (isPostView) {
                    isPostView = false;
                    layout_list.setVisibility(View.VISIBLE);
                    layout_post_details.setVisibility(View.GONE);
                    tb_offer_need.setVisibility(View.GONE);
                    isCity = false;
                    layout_search.setVisibility(View.GONE);
                    layout_selection_button.setVisibility(View.VISIBLE);
                    rc_category.setVisibility(View.GONE);
                    rc_service.setVisibility(View.VISIBLE);
//                    if (tabNo == 1) {
//                        categoryList.clear();
//                        categoryList.addAll(tempCategoryList);
//                        rc_category.setAdapter(categoryListAdapter);
//                        rc_service.setVisibility(View.VISIBLE);
//                    } else {
//                        categoryList.clear();
//                        categoryList.addAll(tempServiceList);
//                        rc_category.setAdapter(categoryListAdapter);
//                    }
                } else {
                    finish();
                }
                break;
            case R.id.layout_sub_category:
                rc_category.setAdapter(subCategoryListAdapter);
                layout_list.setVisibility(View.VISIBLE);
                layout_post_details.setVisibility(View.GONE);
                break;
            case R.id.layout_child_category:
                layout_list.setVisibility(View.VISIBLE);
                layout_post_details.setVisibility(View.GONE);
                break;
            case R.id.layout_city:
                Function.hideSoftKeyboard(AddPostActivity.this, v);
                isCity = true;
                cityListAdapter.getFilter().filter("");
                rc_category.setAdapter(cityListAdapter);
                filterType = 4;
                cityCallBack(cityListAdapter);
                ListVisible(true);
                txt_page_title.setText(getString(R.string.txt_select) + " " + getString(R.string.txt_city_select));
                img_search.setVisibility(View.VISIBLE);
                txt_product_count.setVisibility(View.GONE);
                break;
            case R.id.txt_area_content:
                Function.hideSoftKeyboard(AddPostActivity.this, v);
                if (cityId.equals("")) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.select_city));
                }
//                else {
//
//                    txt_page_action.setVisibility(View.VISIBLE);
//
//                    filterType = 5;
//
//                    rc_category.setAdapter(areaListAdapter);
//                    ListVisible(true);
//                    edt_search.setImeOptions(EditorInfo.IME_ACTION_DONE);
//                    txt_page_title.setText(getString(R.string.txt_type) + " " + getString(R.string.txt_locality_select));
//                    edt_search.setText(areaId);
//                    areaListAdapter.getFilter().filter("");
//                    isCity = true;
//                }
                break;
            case R.id.layout_product_type:
//                isCity = true;
//                Function.hideSoftKeyboard(AddPostActivity.this, v);
////                if (subCategoryChildList.size() == 0) {
//                filterType = 1;
//                subCategoryListAdapter.getFilter().filter("");
//                rc_category.setAdapter(subCategoryListAdapter);
//                subCategoryListAdapter.notifyDataSetChanged();
////                } else {
////                    filterType = 2;
////                    childCategoryListAdapter.getFilter().filter("");
////                    rc_category.setAdapter(childCategoryListAdapter);
////                    childCategoryListAdapter.notifyDataSetChanged();
////                }
//                txt_page_action.setVisibility(View.VISIBLE);
//                ListVisible(true);
//                edt_search.setImeOptions(EditorInfo.IME_ACTION_DONE);
//                if (tabNo == 1) {
//                    txt_page_title.setText(getString(R.string.txt_type) + " " + getString(R.string.txt_product_type_select));
//                } else {
//                    txt_page_title.setText(getString(R.string.txt_type) + " " + getString(R.string.txt_profession_select));
//                }
//                edt_search.setText(txt_product_type_content.getText().toString());
//                subCategoryListAdapter.getFilter().filter("");
//                isCity = true;
                break;
            case R.id.btn_add_post:
                resentCount = 0;
                AddPostValidation();

                break;
            case R.id.btn_product:
                Function.hideSoftKeyboard(AddPostActivity.this, v);
                if (tabNo != 1) {
                    tabNo = 1;
                    btn_product.setBackground(getResources().getDrawable(R.drawable.bg_left_orenge));
                    btn_service.setBackground(getResources().getDrawable(R.drawable.bg_orenge_border_right));
                    btn_product.setTextColor(getResources().getColor(R.color.color_white));
                    btn_service.setTextColor(getResources().getColor(R.color.txt_orange));
                    categoryList.clear();
                    categoryList.addAll(tempCategoryList);
                    categoryListAdapter.notifyDataSetChanged();
                    txt_product_type_title.setText(getString(R.string.txt_product_type));
                    txt_rental_duration_title.setText(getString(R.string.txt_rental_duration));
                    txt_rental_fee_title.setText(getString(R.string.txt_rental_fee));
                    txt_image_content.setText(getString(R.string.txt_upload_image_product));
                    edt_price.setHint(getString(R.string.txt_input_price_product));
                    txt_product_type_content.setHint(getString(R.string.select_product_type));
                    if (AdType != 2) {
                        layout_image_upload.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.btn_service:
                Function.hideSoftKeyboard(AddPostActivity.this, v);
                if (tabNo != 2) {
                    tabNo = 2;
                    btn_product.setBackground(getResources().getDrawable(R.drawable.bg_orenge_border_left));
                    btn_service.setBackground(getResources().getDrawable(R.drawable.bg_right_orenge));
                    btn_product.setTextColor(getResources().getColor(R.color.txt_orange));
                    btn_service.setTextColor(getResources().getColor(R.color.color_white));
                    categoryList.clear();
                    categoryList.addAll(tempServiceList);
                    categoryListAdapter.notifyDataSetChanged();
                    txt_product_type_title.setText(getString(R.string.txt_profession));
                    txt_rental_duration_title.setText(getString(R.string.txt_hiring_pattern));
                    txt_rental_fee_title.setText(getString(R.string.txt_hiring_fee));
                    txt_image_content.setText(getString(R.string.txt_upload_image_service));
                    edt_price.setHint(getString(R.string.txt_input_price_service));
                    txt_product_type_content.setHint(getString(R.string.select_profession));
                    layout_image_upload.setVisibility(View.GONE);
                }
                break;
            case R.id.img_drop_down:
//                sp_price_type.performClick();
                break;
            case R.id.btn_verify_otp:
                if (otp.length() == 0) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.enter_otp));
                } else {
                    if (_fun.isInternetAvailable(AddPostActivity.this)) {
                        verifyOTP(otp);
                    } else {
                        _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                verifyOTP(otp);
                            }
                        });
                    }

                }
                break;
            case R.id.txt_resend_otp:
                if (resentCount != 2) {
                    resentCount++;
                    if (_fun.isInternetAvailable(AddPostActivity.this)) {
                        reSendOTP();
                    } else {
                        _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                reSendOTP();
                            }
                        });
                    }
                    otp_view.setText("");
                } else {
                    Intent i = new Intent(AddPostActivity.this, ReportUsActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.txt_page_action:
                Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);
                if (filterType == 1) {
                    txt_product_type_content.setText(edt_search.getText().toString());
                    if (tabNo == 1) {
                        productTitle = edt_search.getText().toString();
                        if (AdType == 2) {
                            txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                        } else {
                            txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                        }
                    } else {
                        productTitle = edt_search.getText().toString();
                        if (AdType == 2) {
                            txt_post_title_content.setText(getString(R.string.txt_need) + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                        } else {
                            txt_post_title_content.setText(productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                        }
                    }

                    Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);

                    if (_fun.isInternetAvailable(AddPostActivity.this)) {
                        GetAttributes(edt_search.getText().toString());
                    } else {
                        _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                GetAttributes(edt_search.getText().toString());
                            }
                        });
                    }
                } else if (filterType == 3) {
                    attributeList.get(attributeSelectPos).setInputValue(edt_search.getText().toString());
                    attributeListAdapter.notifyDataSetChanged();
                    attributeTitle = "";
                    for (int i = 0; i < attributeList.size(); i++) {
                        if (attributeList.get(i).getIsTitle().equals("1")) {
                            if (attributeTitle.equals("")) {
                                attributeTitle = attributeList.get(i).getInputValue().trim();
                            } else {
                                attributeTitle = attributeTitle + " " + attributeList.get(i).getInputValue().trim();
                            }
                        }
                    }

                    if (tabNo == 1) {
                        if (AdType == 2) {
                            txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                        } else {
                            txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                        }
                    } else {
                        if (AdType == 2) {
                            txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                        } else {
                            txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                        }
                    }
                } else {
                    txt_area_content.setText(edt_search.getText().toString());
                    areaId = edt_search.getText().toString();
                }

                ListVisible(false);
                isCity = false;
                break;
            case R.id.layout_rental_duration:
                Function.hideSoftKeyboard(AddPostActivity.this, v);
                isCity = true;
                rc_category.setAdapter(rentalDurationAdapter);
                ListVisible(true);
                layout_search.setVisibility(View.GONE);

                if (tabNo == 1) {
                    txt_page_title.setText(getString(R.string.txt_rental_duration_select));
                } else {
                    txt_page_title.setText(getString(R.string.txt_hiring_duration_select));
                }
                break;
            case R.id.layout_product_container:
                if (!layout_product_type.isEnabled()) {
                    Function.CustomMessage(this, getString(R.string.you_cannot_edit));
                }
                break;
        }
    }

    private void verifyOTP(String otp) {
        String phone = edt_mobile_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("otp", otp);
        params.put("mob_add", "1");
        params.put("user_id", Sessions.getSession(Constant.UserId, getApplicationContext()));

        Log.i("params", params.toString());

        RetrofitClient.getClient().create(Api.class).verifyOTP(params)
                .enqueue(new RetrofitCallBack(AddPostActivity.this, verifyOTP, true));

    }

    private void sendOTP() {
        String phone = edt_mobile_number.getText().toString().trim();

        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("email", "email");
        Log.i("params", params.toString());


        if (_fun.isInternetAvailable(AddPostActivity.this)) {
            RetrofitClient.getClient().create(Api.class).emailPhoneRegistration(params)
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, emailPhoneValidate, true));
        } else {
            _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).emailPhoneRegistration(params)
                            .enqueue(new RetrofitCallBack(AddPostActivity.this, emailPhoneValidate, true));
                }
            });
        }

    }

    private void AddPostValidation() {
        if (catId.equals("")) {
            if (tabNo == 1) {
                Function.CustomMessage(AddPostActivity.this, getString(R.string.select_product_type));
            } else {
                Function.CustomMessage(AddPostActivity.this, getString(R.string.select_profession));
            }
            return;
        }
        for (int i = 0; i < attributeList.size(); i++) {

            if (attributeList.get(i).getMandatory().equals("1") && attributeList.get(i).getInputValue().equals("")) {
                if (attributeList.get(i).getType().equals("select")) {
                    Function.CustomMessage(AddPostActivity.this, "Select " + attributeList.get(i).getName());
                } else {
                    Function.CustomMessage(AddPostActivity.this, "Enter " + attributeList.get(i).getName());
                }
                return;
            }
        }

        if (AdType != 2) {
            if (isPriceVisible) {
                if (edt_price.getText().toString().length() == 0) {
                    if (tabNo == 1) {
                        Function.CustomMessage(AddPostActivity.this, getString(R.string.enter_rental_fee));
                    } else {
                        Function.CustomMessage(AddPostActivity.this, getString(R.string.enter_hiring_fee));
                    }
                    return;
                }
            }
        }

        if (cityId.equals("")) {
            Function.CustomMessage(AddPostActivity.this, getString(R.string.txt_select_city));
            return;
        }


        if (areaId.equals("")) {
            Function.CustomMessage(AddPostActivity.this, getString(R.string.txt_select_locality));
            return;
        }

        if (AdType != 2) {
            if (cb_mobile_number.isChecked()) {
                if (edt_mobile_number.getText().toString().length() == 0) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.enter_mobile_number));
                    return;
                }
                if (!Patterns.PHONE.matcher(edt_mobile_number.getText().toString()).matches()) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.enter_valid_mobile_number));
                    return;
                }

                if (edt_mobile_number.getText().toString().length() < 10) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.enter_valid_mobile_number));
                    return;
                }
            }
        }
        if (!cb_tc.isChecked()) {
            Function.CustomMessage(AddPostActivity.this, getString(R.string.accept_tc));
            return;
        }
        if (!Sessions.getSession(Constant.UserPhone, getApplicationContext()).equals("")) {
            if (Sessions.getSession(Constant.UserPhone, getApplicationContext()).equals(edt_mobile_number.getText().toString()) || postMobileNum != null && postMobileNum.equals(edt_mobile_number.getText().toString())) {
                if (_fun.isInternetAvailable(AddPostActivity.this)) {
                    savePostDetails();
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            savePostDetails();
                        }
                    });
                }
            } else {
                if (_fun.isInternetAvailable(AddPostActivity.this)) {
                    sendOTP();
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            sendOTP();
                        }
                    });
                }
            }
        } else {
            if (postMobileNum.equals(edt_mobile_number.getText().toString())) {

                if (_fun.isInternetAvailable(AddPostActivity.this)) {
                    savePostDetails();
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            savePostDetails();
                        }
                    });
                }
            } else {

                if (_fun.isInternetAvailable(AddPostActivity.this)) {
                    sendOTP();
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            sendOTP();
                        }
                    });
                }
            }
        }
    }

    private void reSendOTP() {


        String phone = edt_mobile_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);

        Log.i("params", params.toString());

        RetrofitClient.getClient().create(Api.class).reSendOTP(params)
                .enqueue(new RetrofitCallBack(AddPostActivity.this, reSendOTP, true));


    }

    private void savePostDetails() {
        if (!params.isEmpty()) {
            params.clear();
        }
        String isNegos = "";
        if (cb_price.isChecked()) {
            isNegos = "1";
        } else {
            isNegos = "0";
        }

        String isMobile = "";
        if (cb_mobile_number.isChecked()) {
            isMobile = "1";
        } else {
            isMobile = "0";
        }
        HashMap<String, String> paramsId = new HashMap<String, String>();
        HashMap<String, String> paramsValue = new HashMap<String, String>();
        for (int i = 0; i < attributeList.size(); i++) {

            paramsId.put(String.valueOf(i), attributeList.get(i).getAttributeId());
            paramsValue.put(String.valueOf(i), attributeList.get(i).getInputValue());
        }
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < shareImageList.size(); i++) {
            if (shareImageList.get(i).getImgURL().equals("")) {
                jsonArray.add(Function.getBase64String(shareImageList.get(i).getUri()));
            } else {
                jsonArray.add(shareImageList.get(i).getImgURL());
            }


        }
        params.put("category_id", catId);
        if (AdType != 2) {
            params.put("rental_fee", edt_price.getText().toString());
        }
        params.put("rental_duration", rentalDuration);
        params.put("title", txt_post_title_content.getText().toString());
        params.put("description", edt_description.getText().toString());
        if (AdType != 2) {
            params.put("photos", jsonArray);
        }
        params.put("city_id", cityId);
        params.put("locality_id", areaId);
        params.put("status", adStatus);
        params.put("is_rent_negotiable", isNegos);
        params.put("attributeId", paramsId);
        params.put("attributeValue", paramsValue);
        params.put("mobile", edt_mobile_number.getText().toString());
        params.put("is_mobile_visible", isMobile);
        params.put("parent_id", parentId);


        if (AdType == 0) {
            RetrofitClient.getClient().create(Api.class).savePost(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, savePost, true));
        } else if (AdType == 1) {
            params.put("ads_id", adId);

            RetrofitClient.getClient().create(Api.class).updatePost(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, savePost, true));
        } else if (AdType == 2) {
            if (EditType.equals("2")) {
                params.put("ads_id", adId);
                RetrofitClient.getClient().create(Api.class).updateRequirementPost(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(AddPostActivity.this, savePost, true));
            } else {
                RetrofitClient.getClient().create(Api.class).saveRequirementPost(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(AddPostActivity.this, savePost, true));
            }
        }

        Log.i("Params", params.toString());
        Log.i("Token", Sessions.getSession(Constant.UserToken, getApplicationContext()));
    }

    private void areaCallBack(AreaListAdapter areaListAdapter) {
        areaListAdapter.setCallback(new AreaListAdapter.Callback() {
            @Override
            public void itemClick(AreaItemModel model) {
//                Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);
                txt_area_content.setText(model.getAreaName());
                areaId = model.getAreaName();
                ListVisible(false);
            }
        });
    }

    private void cityCallBack(CityListAdapter cityListAdapter) {
        cityListAdapter.setCallback(new CityListAdapter.Callback() {
            @Override
            public void itemClick(final CityItemModel model) {
                Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);
                txt_city_content.setText(model.getCityName());
                cityId = model.getCityId();
                txt_area_content.setText("");
                txt_area_content.setFocusableInTouchMode(true);
                areaId = "";
                ListVisible(false);
                if (_fun.isInternetAvailable(AddPostActivity.this)) {
                    GetArea(model.getCityId());
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            GetArea(model.getCityId());
                        }
                    });
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50) {
            if (data != null) {
                List<String> slist = Matisse.obtainPathResult(data);
                if (slist.size() > 0) {
                    for (int i = 0; i < slist.size(); i++) {
                        shareImageList.add(new UploadImageModel("", true, slist.get(i), "1"));
                        Log.i("Path", slist.get(i));
                    }
                }
                Log.e("Matisse", "PostSelected: " + selectedPaths + "\n" + selectedUris.size());
                postImageListAdapter.notifyDataSetChanged();
                if (shareImageList.size() != 0) {
                    btn_add_image.setVisibility(View.GONE);
//                        view_gallery.setVisibility(View.GONE);
                    rc_image_list.setVisibility(View.VISIBLE);
                } else {
                    btn_add_image.setVisibility(View.VISIBLE);
//                        view_gallery.setVisibility(View.VISIBLE);
                    rc_image_list.setVisibility(View.GONE);
                }

            }
        }
    }


    public void GetAttributes(String attVal) {
        isAttributes = true;
        if (attributeList.size() != 0) {
            attributeList.clear();
            attributeListAdapter.notifyDataSetChanged();
        }
        catId = attVal;
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("attributeValue", attVal);
        params.put("parent_id", parentId);

        Log.i("Params", params.toString());

        RetrofitClient.getClient().create(Api.class).getAttributes(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(AddPostActivity.this, attributeValue, true));
    }

    public void GetCity() {

        if (_fun.isInternetAvailable(AddPostActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getCity()
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, cityValue, false));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            RetrofitClient.getClient().create(Api.class).getCity()
                                    .enqueue(new RetrofitCallBack(AddPostActivity.this, cityValue, false));
                        }
                    });
                }
            }, 200);

        }
    }

    public void GetArea(String cityId) {
        RetrofitClient.getClient().create(Api.class).getArea(cityId)
                .enqueue(new RetrofitCallBack(AddPostActivity.this, areaValue, true));
    }

    retrofit2.Callback<AttributeModel> attributeValue = new retrofit2.Callback<AttributeModel>() {
        @Override
        public void onResponse(Call<AttributeModel> call, retrofit2.Response<AttributeModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                AttributeModel dr = response.body();
                if (dr.isStatus()) {
                    if (attributeList.size() != 0) {
                        attributeList.clear();
                    }
//                    attributeList.addAll(dr.getAttributeList());
                    for (int i = 0; i < dr.getAttributeList().size(); i++) {
                        attributeList.add(new AttributeItemModel(dr.getAttributeList().get(i).getName(), dr.getAttributeList().get(i).getAttributeValues(), dr.getAttributeList().get(i).getType(), dr.getAttributeList().get(i).getAttributeId(), dr.getAttributeList().get(i).getMandatory(), dr.getAttributeList().get(i).getIsTitle(), ""));
                    }
//                    attributeListAdapter = new AttributeListAdapter(attributeList, getApplicationContext());
                    attributeListAdapter.notifyDataSetChanged();
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

    retrofit2.Callback<CityModel> cityValue = new retrofit2.Callback<CityModel>() {
        @Override
        public void onResponse(Call<CityModel> call, retrofit2.Response<CityModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                CityModel dr = response.body();
                if (dr.isStatus()) {
                    if (cityList.size() != 0) {
                        cityList.clear();
                    }
                    cityList.addAll(dr.getCityList());

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
    retrofit2.Callback<AreaModel> areaValue = new retrofit2.Callback<AreaModel>() {
        @Override
        public void onResponse(Call<AreaModel> call, retrofit2.Response<AreaModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                AreaModel dr = response.body();
                if (dr.isStatus()) {
                    if (areaList.size() != 0) {
                        areaList.clear();
                    }
                    areaList.addAll(dr.getAreaList());
                    areaAutoCompleteAdapter = new AreaAutoCompleteAdapter(AddPostActivity.this, R.layout.layout_sub_category_item, areaList);
                    txt_area_content.setAdapter(areaAutoCompleteAdapter);
                    txt_area_content.setThreshold(2);
                    areaAutoCompleteAdapter.setCallback(new AreaAutoCompleteAdapter.Callback() {
                        @Override
                        public void itemClick(AreaItemModel model) {
                            Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);
                            txt_area_content.setText(model.getAreaName());
                            txt_area_content.dismissDropDown();
                            txt_area_content.setSelection(txt_area_content.getText().toString().length());
                            areaId = model.getAreaName();
                        }
                    });
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
    retrofit2.Callback<DefaultResponse> savePost = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            DefaultResponse dr = response.body();
            try {
                if (dr.isStatus()) {
//                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
//                    if (AdType == 1) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
//                    } else if (AdType == 2 && EditType.equals("2")) {
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                    }
                    ShowPopupSuccess(dr.getMessage());


                } else {
                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
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

    retrofit2.Callback<EditAdModel> adDetails = new retrofit2.Callback<EditAdModel>() {
        @Override
        public void onResponse(Call<EditAdModel> call, retrofit2.Response<EditAdModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                EditAdModel dr = response.body();
                if (dr.isStatus()) {
                    if (dr.getEditDataModel().getAdDetailsModel() != null) {
                        EditAdDetailsModel editAdDetailsModel = dr.getEditDataModel().getAdDetailsModel();
                        layout_list.setVisibility(View.GONE);
                        isPostView = false;
                        layout_post_details.setVisibility(View.VISIBLE);
                        rc_category.setVisibility(View.VISIBLE);
                        rc_service.setVisibility(View.GONE);
                        layout_product_type.setEnabled(false);
                        txt_product_type_content.setTextColor(getResources().getColor(R.color.hint_txt_color));
                        txt_product_type_content.setFocusableInTouchMode(false);
                        txt_product_type_title.setTextColor(getResources().getColor(R.color.hint_txt_color));
                        layout_selection_button.setVisibility(View.GONE);

                        if (dr.getEditDataModel().getCatGroup() == 1) {
                            tabNo = 1;
                            txt_product_type_title.setText(getString(R.string.txt_product_type));
                            txt_rental_duration_title.setText(getString(R.string.txt_rental_duration));
                            txt_rental_fee_title.setText(getString(R.string.txt_rental_fee));
                            txt_image_content.setText(getString(R.string.txt_upload_image_product));
                            edt_price.setHint(getString(R.string.txt_input_price_product));
                            txt_product_type_content.setHint(getString(R.string.select_product_type));
                            layout_image_upload.setVisibility(View.VISIBLE);
                            if (!EditType.equals("1")) {
                                layout_image_upload.setVisibility(View.GONE);
                            }
                        } else {
                            tabNo = 2;
                            txt_product_type_title.setText(getString(R.string.txt_profession));
                            txt_rental_duration_title.setText(getString(R.string.txt_hiring_pattern));
                            txt_rental_fee_title.setText(getString(R.string.txt_hiring_fee));
                            txt_image_content.setText(getString(R.string.txt_upload_image_service));
                            edt_price.setHint(getString(R.string.txt_input_price_service));
                            txt_product_type_content.setHint(getString(R.string.select_profession));
                            layout_image_upload.setVisibility(View.GONE);
                        }
                        txt_parent_title.setText(dr.getEditDataModel().getParentTitle());

                        adStatus = editAdDetailsModel.getAdStatus();
                        txt_product_type_content.setText(dr.getEditDataModel().getProductType().get(0).getChildCategoryName().trim());
                        productTitle = dr.getEditDataModel().getProductType().get(0).getChildCategoryName().trim();
                        if (attributeList.size() != 0) {
                            attributeList.clear();
                        }
//                        attributeList.addAll(dr.getEditDataModel().getAttributeModel().getAttributeEditList());
                        for (int i = 0; i < dr.getEditDataModel().getAttributeModel().getAttributeEditList().size(); i++) {
                            attributeList.add(new AttributeItemModel(dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getName(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getAttributeValues(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getType(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getAttributeId(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getMandatory(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getIsTitle(), ""));
                        }
                        if (attributeList.size() != 0 && dr.getEditDataModel().getAttributeModel().getSelectValue() != null) {
                            attributeTitle = "";
                            for (Map.Entry<String, String> entry : dr.getEditDataModel().getAttributeModel().getSelectValue().entrySet()) {
                                String key = entry.getKey();
                                String value = entry.getValue();
                                for (int i = 0; i < attributeList.size(); i++) {
                                    if (key.equals(attributeList.get(i).getAttributeId())) {
                                        attributeList.get(i).setInputValue(value);
                                        break;
                                    }
                                }
                                Log.i("Key", key);
                                Log.i("Value", value);

                            }
                        }
                        for (int i = 0; i < attributeList.size(); i++) {
                            if (attributeList.get(i).getIsTitle().equals("1")) {
                                if (attributeTitle.equals("")) {
                                    attributeTitle = attributeList.get(i).getInputValue().trim();
                                } else {
                                    attributeTitle = attributeTitle + " " + attributeList.get(i).getInputValue().trim();
                                    attributeTitle = attributeTitle.trim();
                                }
                            }
                        }
                        attributeListAdapter.SetEditable(true);
                        attributeListAdapter.notifyDataSetChanged();

                        rentalDurationList.clear();
                        rentalDurationList.addAll(dr.getEditDataModel().getRentalDurationList());
//                        spinnerAdapter.notifyDataSetChanged();
                        rentalDurationAdapter.notifyDataSetChanged();


                        for (int i = 0; i < rentalDurationList.size(); i++) {
                            if (editAdDetailsModel.getRentalDuration().equals(rentalDurationList.get(i))) {
                                rentalDuration = rentalDurationList.get(i);
                                txt_rental_duration_content.setText(rentalDurationList.get(i));
                                setRentalDuration(i);
                                rentalDurationAdapter.setPosValues(i);
                                break;
                            }
                        }
                        if (cityList.size() != 0) {
                            cityList.clear();
                        }
                        cityList.addAll(dr.getEditDataModel().getCityList());
                        if (areaList.size() != 0) {
                            areaList.clear();
                        }
                        areaList.addAll(dr.getEditDataModel().getAreaList());
                        if (editAdDetailsModel.getRentalFee() != null) {
                            String price = "";
                            if (editAdDetailsModel.getRentalFee().contains(".")) {
                                String tempPrice[] = editAdDetailsModel.getRentalFee().split("\\.");
                                price = tempPrice[0];
                            } else {
                                price = editAdDetailsModel.getRentalFee();
                            }

                            edt_price.setText(price);
                        }
                        cityId = editAdDetailsModel.getCityId();
                        areaId = editAdDetailsModel.getAreaId();
                        for (int c = 0; c < cityList.size(); c++) {
                            if (cityId.equals(cityList.get(c).getCityId())) {
                                txt_city_content.setText(cityList.get(c).getCityName());
                                break;
                            }
                        }

                        for (int a = 0; a < areaList.size(); a++) {
                            if (areaId.equals(areaList.get(a).getAreaId())) {
                                txt_area_content.setText(areaList.get(a).getAreaName());
                                areaId = areaList.get(a).getAreaName();
                                break;
                            }
                        }
                        txt_area_content.setFocusableInTouchMode(true);
                        edt_mobile_number.setText(editAdDetailsModel.getMobile());
                        postMobileNum = editAdDetailsModel.getMobile();
                        if (editAdDetailsModel.getIsNegotiable().equals("1")) {
                            cb_price.setChecked(true);
                        } else {
                            cb_price.setChecked(false);
                        }
                        if (editAdDetailsModel.getIsMobileVisible().equals("1")) {
                            cb_mobile_number.setChecked(true);
                        } else {
                            cb_mobile_number.setChecked(false);
                        }
                        edt_description.setText(editAdDetailsModel.getDescription());
                        txt_post_title_content.setText(editAdDetailsModel.getAdTitle().trim());
//                        if (!productTitle.equals("")) {
//
//                            if (tabNo == 1) {
//                                if (AdType == 2) {
//                                    txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
//                                } else {
//                                    txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
//                                }
//                            } else {
//                                if (AdType == 2) {
//                                    txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
//                                } else {
//                                    txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
//                                }
//                            }
//
//
//                        }
                        if (dr.getEditDataModel().getAdImage() != null) {
                            shareImageList.clear();
                            for (int p = 0; p < dr.getEditDataModel().getAdImage().size(); p++) {
                                shareImageList.add(new UploadImageModel(dr.getEditDataModel().getAdImage().get(p), false, "", ""));

                            }
//                        if (dr.getEditDataModel().getCatGroup() == 1) {
                            if (shareImageList.size() != 0) {
                                btn_add_image.setVisibility(View.GONE);
//                        view_gallery.setVisibility(View.GONE);
                                rc_image_list.setVisibility(View.VISIBLE);
                            } else {
                                btn_add_image.setVisibility(View.VISIBLE);
//                        view_gallery.setVisibility(View.VISIBLE);
                                rc_image_list.setVisibility(View.GONE);
                            }
                        }
//                        }
                    }
                } else {
                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
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
                    layout_post_details.setVisibility(View.GONE);
                    tb_offer_need.setVisibility(View.GONE);
                    layout_otp.setVisibility(View.VISIBLE);
                    isOTPView = true;
                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
                } else {
                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
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
                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
                } else {
                    if (_fun.isInternetAvailable(AddPostActivity.this)) {
                        sendOTP();
                    } else {
                        _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
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
                    layout_post_details.setVisibility(View.VISIBLE);
                    if (!isEdit)
                        tb_offer_need.setVisibility(View.VISIBLE);
                    isOTPView = false;
                    if (Sessions.getSession(Constant.UserPhone, getApplicationContext()).equals("")) {
                        Sessions.saveSession(Constant.UserPhone, edt_mobile_number.getText().toString(), getApplicationContext());
                    }
                    if (_fun.isInternetAvailable(AddPostActivity.this)) {
                        savePostDetails();
                    } else {
                        _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                savePostDetails();
                            }
                        });
                    }
                } else {
                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
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
                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
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

        String phone = edt_mobile_number.getText().toString().trim();
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("mobile", phone);
        params.put("status", "2");
        Log.i("params", params.toString());
        RetrofitClient.getClient().create(Api.class).sendOTP(params)
                .enqueue(new RetrofitCallBack(AddPostActivity.this, sendOTP, true));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        rentalDuration = rentalDurationList.get(position);
        if (position == 0) {
            rentalDurationTitle = getString(R.string.txt_hourly);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.VISIBLE);
            }
            isPriceVisible = true;
        } else if (position == 1) {
            rentalDurationTitle = getString(R.string.txt_daily);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.VISIBLE);
            }
            isPriceVisible = true;
        } else if (position == 2) {
//            layout_input_price.setVisibility(View.INVISIBLE);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.INVISIBLE);
            }
            isPriceVisible = false;
            edt_price.setText("");
            rentalDurationTitle = rentalDurationList.get(position);
        }
        if (!productTitle.equals("")) {

            if (tabNo == 1) {
                if (AdType == 2) {
                    txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                } else {
                    txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                }
            } else {
                if (AdType == 2) {
                    txt_post_title_content.setText(getString(R.string.txt_need) + " " + attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                } else {
                    txt_post_title_content.setText(attributeTitle + " " + productTitle + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                }
            }


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        if (spinnerPopup != null && spinnerPopup.isShowing()) {
            if (isRequirement) {
                Intent h = new Intent(AddPostActivity.this, HomeActivity.class);
                h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(h);
            }
            finish();
        } else if (isCity) {
            isCity = false;
            ListVisible(false);

        } else if (isOTPView) {
            layout_otp.setVisibility(View.GONE);
            layout_post_details.setVisibility(View.VISIBLE);
            if (!isEdit)
                tb_offer_need.setVisibility(View.VISIBLE);
        } else if (isSubCat) {
            isSubCat = false;
            layout_list.setVisibility(View.VISIBLE);
            layout_post_details.setVisibility(View.GONE);
            tb_offer_need.setVisibility(View.GONE);
            layout_search.setVisibility(View.GONE);
            rc_category.setAdapter(subCategoryListAdapter);
        } else if (isPostView) {
            isPostView = false;
            layout_list.setVisibility(View.VISIBLE);
            layout_post_details.setVisibility(View.GONE);
            tb_offer_need.setVisibility(View.GONE);
            isCity = false;
            layout_search.setVisibility(View.GONE);
            layout_selection_button.setVisibility(View.VISIBLE);
            rc_category.setVisibility(View.GONE);
            rc_service.setVisibility(View.VISIBLE);
//            if (tabNo == 1) {
//                categoryList.clear();
//                categoryList.addAll(tempCategoryList);
//                rc_category.setAdapter(categoryListAdapter);
//            } else {
//                categoryList.clear();
//                categoryList.addAll(tempServiceList);
//                rc_category.setAdapter(categoryListAdapter);
//            }
        } else {
            finish();
            super.onBackPressed();
        }

    }

    public void ClearFileds() {
        edt_price.setText("");
        edt_description.setText("");
        cityId = "";
        txt_city_content.setText("");
        txt_area_content.setText("");
        txt_area_content.setFocusableInTouchMode(false);
        txt_post_title_content.setText("");
        txt_product_type_content.setText("");
        shareImageList.clear();
        postImageListAdapter.notifyDataSetChanged();
        attributeList.clear();
        attributeListAdapter.notifyDataSetChanged();
        if (shareImageList.size() != 0) {
            btn_add_image.setVisibility(View.GONE);
//                        view_gallery.setVisibility(View.GONE);
            rc_image_list.setVisibility(View.VISIBLE);
        } else {
            btn_add_image.setVisibility(View.VISIBLE);
//                        view_gallery.setVisibility(View.VISIBLE);
            rc_image_list.setVisibility(View.GONE);
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
                if (isRequirement) {
                    Intent h = new Intent(AddPostActivity.this, HomeActivity.class);
                    h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(h);
                }
                finish();
            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
