package com.slowr.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gioco.image.cropper.CropImage;
import com.gioco.image.cropper.CropImageView;
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
import com.slowr.app.components.ViewDialog;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    LinearLayout img_back;
    TextView txt_page_title;
    TextView txt_parent_title;
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
    View productDivider;
    TextView txt_service_img_alert;

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
    boolean isPrefix = false;
    String titleContent = "";

    int tabNo = 1;
    int AdType = 0;
    String EditType = "";
    boolean isOTPView = false;
    boolean isEdit = false;
    String otp = "";
    int resentCount = 0;
    private PopupWindow spinnerPopup;
    boolean changeDeductedAmount = false;

    private Function _fun = new Function();
    PostImageListAdapter postImageListAdapter;
    ProductAutoCompleteAdapter productAutoCompleteAdapter;
    AreaAutoCompleteAdapter areaAutoCompleteAdapter;

    HashMap<String, Object> params = new HashMap<String, Object>();
    boolean isServiceValid = false;

    ViewDialog viewDialog;
    String imagePath = "";
    boolean isClickChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        doDeclaration();
    }

    private void doDeclaration() {
        viewDialog = new ViewDialog(this);
        AdType = getIntent().getIntExtra("AdType", 0);
        if (AdType == 1) {
            catId = getIntent().getStringExtra("CatId");
            adId = getIntent().getStringExtra("AdId");
            EditType = getIntent().getStringExtra("EditType");
        } else if (AdType == 2) {
            parentId = getIntent().getStringExtra("ParId");
        } else if (AdType == 0) {
            if (getIntent().hasExtra("ParId")) {
                parentId = getIntent().getStringExtra("ParId");
                Log.i("ParentID", parentId);
            } else {
                parentId = "";
            }
        }
        img_back = findViewById(R.id.img_back);
        txt_page_title = findViewById(R.id.txt_page_title);
        txt_page_title.setText(getString(R.string.txt_add_post));
        rc_category = findViewById(R.id.rc_category);
        rc_image_list = findViewById(R.id.rc_image_list);
        txt_parent_title = findViewById(R.id.txt_parent_title);
        layout_post_details = findViewById(R.id.layout_post_details);
        layout_list = findViewById(R.id.layout_list);
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
        txt_service_img_alert = findViewById(R.id.txt_service_img_alert);

        txt_page_action.setText(getString(R.string.txt_done));
        txt_page_action.setVisibility(View.GONE);

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
                        Function.CustomMessage(AddPostActivity.this, getString(R.string.enter_otp));
                        return false;
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
                    return true;
                }
                return false;
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
            txt_area_content.setHint(getString(R.string.enter_locality_your));
            tb_offer_need.setChecked(true);
            if (tabNo == 1) {
                edt_description.setHint(getString(R.string.des_products_req));
            } else {
                edt_description.setHint(getString(R.string.des_service_req));
            }
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
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, adDetails, true, false));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            RetrofitClient.getClient().create(Api.class).getAdDetails(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                    .enqueue(new RetrofitCallBack(AddPostActivity.this, adDetails, true, false));
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
                if (desValue == 200 && !changeDeductedAmount) {
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
                if (desValue == 50 && !changeDeductedAmount) {
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
                isAttributes = false;
                int desValue = txt_product_type_content.getText().toString().length();
                txt_product_type_count.setText(getString(R.string.txt_pro_count, String.valueOf(desValue)));
                if (desValue == 50 && !changeDeductedAmount) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.txt_limit_reached));
                }
                productTitle = txt_product_type_content.getText().toString().trim();
                AdTitleCreation();
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
                areaId = txt_area_content.getText().toString().trim();
                if (desValue == 50 && !changeDeductedAmount) {
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
                    MatisseActivity.PAGE_FROM = 2;
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
                            .maxSelectable(1)
                            .showSingleMediaType(true)
                            .countable(false)
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
                        if (attributeList.get(i).getIsPrefix() != null && !attributeList.get(i).getIsPrefix().equals("") && !attributeList.get(i).getIsPrefix().equals("null")) {
                            isPrefix = true;
                            if (!attributeTitle.equals(""))
                                attributeTitle = attributeList.get(i).getIsPrefix() + " " + attributeTitle;
                        } else {
                            isPrefix = false;
                        }

                    }
                }

                AdTitleCreation();
            }
        });
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);

                    if (filterType == 1) {
                        txt_product_type_content.setText(edt_search.getText().toString());
                        AdTitleCreation();

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

                        AdTitleCreation();
                    } else {
                        txt_area_content.setText(edt_search.getText().toString());
                        areaId = edt_search.getText().toString().trim();
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

                AdTitleCreation();

                ListVisible(false);
                isCity = false;
            }
        });
        rentalDurationAdapter.setCallback(new RentalDurationAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                txt_rental_duration_content.setText(rentalDurationList.get(pos));
                setRentalDuration(rentalDurationList.get(pos));
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
                    txt_area_content.setHint(getString(R.string.enter_locality_your));
                    txt_service_img_alert.setVisibility(View.GONE);
                    if (tabNo == 1) {
                        edt_description.setHint(getString(R.string.des_products_req));
                    } else {
                        edt_description.setHint(getString(R.string.des_service_req));
                    }
                } else {
                    AdType = 0;
//                    if (tabNo == 1) {
                    layout_image_upload.setVisibility(View.VISIBLE);
                    txt_service_img_alert.setVisibility(View.GONE);
//                    } else {
//                        txt_service_img_alert.setVisibility(View.VISIBLE);
//                    }
                    if (rentalDurationPos != 2) {
                        layout_input_price.setVisibility(View.VISIBLE);
                        view_rental_fee.setVisibility(View.VISIBLE);
                    }

                    isRequirement = false;
                    txt_area_content.setHint(getString(R.string.txt_select_locality));
                    if (tabNo == 1) {
                        edt_description.setHint(getString(R.string.des_products));
                    } else {
                        edt_description.setHint(getString(R.string.des_service));
                    }
                }


                if (!productTitle.equals("")) {
                    AdTitleCreation();
                }
            }
        });
    }

    private void setRentalDuration(String pos) {
        rentalDuration = pos;
        if (pos.equals("Per Hour")) {
            rentalDurationTitle = getString(R.string.txt_hourly);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.VISIBLE);
                view_rental_fee.setVisibility(View.VISIBLE);
            }
            isPriceVisible = true;
        } else if (pos.equals("Per Day")) {
            rentalDurationTitle = getString(R.string.txt_daily);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.VISIBLE);
                view_rental_fee.setVisibility(View.VISIBLE);
            }
            isPriceVisible = true;
        } else if (pos.equals("Per Week")) {
//            layout_input_price.setVisibility(View.INVISIBLE);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.VISIBLE);
                view_rental_fee.setVisibility(View.VISIBLE);
            }
            isPriceVisible = true;
//            edt_price.setText("");
            rentalDurationTitle = getString(R.string.txt_weekly);
        } else if (pos.equals("Per Month")) {
            rentalDurationTitle = getString(R.string.txt_monthly);
            if (AdType != 2) {
                layout_input_price.setVisibility(View.VISIBLE);
                view_rental_fee.setVisibility(View.VISIBLE);
            }
            isPriceVisible = true;
        }
        AdTitleCreation();

    }

    public void AdTitleCreation() {
        if (!productTitle.equals("")) {
            String prefix = "";
            if (isPrefix) {
                prefix = productTitle + " " + attributeTitle;
            } else {
                prefix = attributeTitle + " " + productTitle;
            }
            if (tabNo == 1) {
                if (AdType == 2) {
                    txt_post_title_content.setText(getString(R.string.txt_need) + " " + prefix.trim() + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                } else {
                    txt_post_title_content.setText(prefix.trim() + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_rental));
                }
            } else {
                if (AdType == 2) {
                    txt_post_title_content.setText(getString(R.string.txt_need) + " " + prefix.trim() + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
                } else {
                    txt_post_title_content.setText(prefix.trim() + " " + getString(R.string.txt_for) + " " + rentalDurationTitle + " " + getString(R.string.txt_hiring));
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
                catId = "";
                txt_product_type_title.setText(categoryList.get(pos).getCategoryTitle() + " *");
                txt_product_type_content.setHint("Enter " + categoryList.get(pos).getCategoryTitle());
                titleContent = categoryList.get(pos).getCategoryTitle();
                txt_parent_title.setText(categoryList.get(pos).getName());
                parentId = categoryList.get(pos).getId();
                layout_selection_button.setVisibility(View.GONE);
                subCategoryList.clear();
                subCategoryList.addAll(categoryList.get(pos).getSubCategoryList());
                rc_category.setVisibility(View.VISIBLE);
                rc_service.setVisibility(View.GONE);

                ListVisible(false);
                RentalDurationList(categoryList.get(pos).getRentalDuration());
                isSubCat = false;

                productAutoCompleteAdapter = new ProductAutoCompleteAdapter(AddPostActivity.this, R.layout.layout_sub_category_item, subCategoryList);
                txt_product_type_content.setThreshold(1);
                txt_product_type_content.setAdapter(productAutoCompleteAdapter);
                txt_product_type_content.setSelected(true);
                SubCategoryCallBack();

            }
        });


    }

    private void getCategory() {

        if (_fun.isInternetAvailable(AddPostActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getCategory()
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, categoryApi, true, false));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            RetrofitClient.getClient().create(Api.class).getCategory()
                                    .enqueue(new RetrofitCallBack(AddPostActivity.this, categoryApi, true, false));
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
                    boolean isDone = false;


//                    categoryExpandAdapter = new CategoryExpandAdapter(getApplicationContext(), categoryList);
                    rc_service.setAdapter(categoryListAdapter);


//                    spinnerAdapter.notifyDataSetChanged();

                    if ((isRequirement && !parentId.equals("")) || (AdType == 0 && !parentId.equals("")) || (AdType == 1 && !parentId.equals(""))) {
                        for (int i = 0; i < tempCategoryList.size(); i++) {
                            if (tempCategoryList.get(i).getId().equals(parentId)) {
                                isDone = true;
                                Log.i("Unction", "Called");

                                txt_product_type_title.setText(tempCategoryList.get(i).getCategoryTitle() + " *");
                                txt_product_type_content.setHint("Enter " + tempCategoryList.get(i).getCategoryTitle());
                                if (isRequirement) {
                                    edt_description.setHint(getString(R.string.des_products_req));
                                } else {
                                    edt_description.setHint(getString(R.string.des_products));
                                }
                                txt_parent_title.setText(tempCategoryList.get(i).getName());
                                titleContent = tempCategoryList.get(i).getCategoryTitle();
                                parentId = tempCategoryList.get(i).getId();
                                subCategoryList.clear();
                                subCategoryList.addAll(tempCategoryList.get(i).getSubCategoryList());
                                if (AdType != 1) {
                                    RentalDurationList(tempCategoryList.get(i).getRentalDuration());
                                    isPostView = true;
                                    ClearFileds();
                                    layout_selection_button.setVisibility(View.GONE);
                                    rc_category.setVisibility(View.VISIBLE);
                                    rc_service.setVisibility(View.GONE);
                                    ListVisible(false);
                                    isSubCat = false;

                                }
                                productAutoCompleteAdapter = new ProductAutoCompleteAdapter(AddPostActivity.this, R.layout.layout_sub_category_item, subCategoryList);
                                txt_product_type_content.setThreshold(1);
                                txt_product_type_content.setAdapter(productAutoCompleteAdapter);
                                txt_product_type_content.setSelected(true);
                                SubCategoryCallBack();
                            }
                        }

                        if (!isDone) {
                            for (int i = 0; i < tempServiceList.size(); i++) {
                                if (tempServiceList.get(i).getId().equals(parentId)) {
                                    tabNo = 2;
                                    postImageListAdapter.ChangeSize(4);
                                    postImageListAdapter.notifyDataSetChanged();
                                    txt_product_type_title.setText(tempServiceList.get(i).getCategoryTitle() + " *");
                                    txt_product_type_content.setHint("Enter " + tempServiceList.get(i).getCategoryTitle());
                                    titleContent = tempServiceList.get(i).getCategoryTitle();
                                    txt_rental_duration_title.setText(getString(R.string.txt_hiring_pattern));
                                    txt_rental_fee_title.setText(getString(R.string.txt_hiring_fee));
                                    txt_image_content.setText(getString(R.string.txt_upload_image_service));
                                    txt_parent_title.setText(tempServiceList.get(i).getName());
                                    edt_price.setHint(getString(R.string.txt_input_price_service));
                                    if (isRequirement) {
                                        edt_description.setHint(getString(R.string.des_service_req));
                                    } else {
                                        edt_description.setHint(getString(R.string.des_service));
                                    }
                                    subCategoryList.clear();
                                    subCategoryList.addAll(tempServiceList.get(i).getSubCategoryList());

                                    if (!isRequirement)
                                        layout_image_upload.setVisibility(View.VISIBLE);

                                    if (AdType != 1) {
                                        RentalDurationList(tempServiceList.get(i).getRentalDuration());
                                        isPostView = true;
                                        ClearFileds();
                                        layout_selection_button.setVisibility(View.GONE);

                                        rc_category.setVisibility(View.VISIBLE);
                                        rc_service.setVisibility(View.GONE);

                                        ListVisible(false);
                                        isSubCat = false;

                                    }

                                    productAutoCompleteAdapter = new ProductAutoCompleteAdapter(AddPostActivity.this, R.layout.layout_sub_category_item, subCategoryList);
                                    txt_product_type_content.setThreshold(1);
                                    txt_product_type_content.setAdapter(productAutoCompleteAdapter);
                                    txt_product_type_content.setSelected(true);
                                    SubCategoryCallBack();
                                }
                            }
                        }
                    }

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

    public void RentalDurationList(String _list) {
        rentalDurationList.clear();
        String[] strArray = _list.split(",");
        attributeValueList.clear();
        for (int i = 0; i < strArray.length; i++) {
            rentalDurationList.add(strArray[i]);
        }
        rentalDuration = rentalDurationList.get(0);
        setRentalDuration(rentalDurationList.get(0));
        txt_rental_duration_content.setText(rentalDurationList.get(0));
        rentalDurationAdapter.notifyDataSetChanged();
    }

    private void SubCategoryCallBack() {

        subCategoryListAdapter.setCallback(new SubCategoryListAdapter.Callback() {
            @Override
            public void itemClick(final SubCategoryItemModel model) {
//                subCategoryChildList.clear();
//                subCategoryChildList.addAll(model.getSubCategoryList());
                txt_product_type_content.setText(model.getSubcategoryName().trim());
                txt_product_type_content.setSelection(txt_product_type_content.getText().toString().trim().length());
                productTitle = model.getSubcategoryName().trim();
                AdTitleCreation();

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

                ListVisible(false);

//                ChildCategoryCallBack();
            }
        });
        productAutoCompleteAdapter.setCallback(new ProductAutoCompleteAdapter.Callback() {
            @Override
            public void itemClick(SubCategoryItemModel model) {
                txt_product_type_content.setText(model.getSubcategoryName().trim());
                txt_product_type_content.dismissDropDown();
                txt_product_type_content.setSelection(txt_product_type_content.getText().toString().length());
                productTitle = model.getSubcategoryName().trim();
                AdTitleCreation();
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

        txt_product_type_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !isAttributes) {
                    productTitle = txt_product_type_content.getText().toString().trim();
                    if (productTitle.length() != 0) {
                        AdTitleCreation();

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
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_image:
                if (_fun.checkPermission(AddPostActivity.this)) {
                    MatisseActivity.PAGE_FROM = 2;
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
                            .maxSelectable(1)
                            .showSingleMediaType(true)
                            .countable(false)
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
                    if (checkData()) {
                        WarningPopup();
                    } else {
                        isPostView = false;
                        layout_list.setVisibility(View.VISIBLE);
                        layout_post_details.setVisibility(View.GONE);
                        tb_offer_need.setVisibility(View.GONE);
                        isCity = false;
                        layout_search.setVisibility(View.GONE);
                        layout_selection_button.setVisibility(View.VISIBLE);
                        rc_category.setVisibility(View.GONE);
                        rc_service.setVisibility(View.VISIBLE);
                        if (isRequirement && !parentId.equals("")) {
//                            AdType = 0;
                            btn_product.performClick();
                        }
                    }
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
//
                if (cityId.equals("")) {
                    Function.CustomMessage(AddPostActivity.this, getString(R.string.select_city));
                    Function.hideSoftKeyboard(AddPostActivity.this, v);
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
                if (!isClickChange)
                    AddPostValidation();

                break;
            case R.id.btn_product:
                Function.hideSoftKeyboard(AddPostActivity.this, v);
                if (tabNo != 1) {
                    tabNo = 1;
                    postImageListAdapter.ChangeSize(4);
                    postImageListAdapter.notifyDataSetChanged();
                    btn_product.setBackground(getResources().getDrawable(R.drawable.bg_left_orenge));
                    btn_service.setBackground(getResources().getDrawable(R.drawable.bg_orenge_border_right));
                    btn_product.setTextColor(getResources().getColor(R.color.color_white));
                    btn_service.setTextColor(getResources().getColor(R.color.txt_orange));
                    categoryList.clear();
                    categoryList.addAll(tempCategoryList);
                    categoryListAdapter.notifyDataSetChanged();
//                    txt_product_type_title.setText(getString(R.string.txt_product_type));
                    txt_rental_duration_title.setText(getString(R.string.txt_rental_duration));
                    txt_rental_fee_title.setText(getString(R.string.txt_rental_fee));
                    txt_image_content.setText(getString(R.string.txt_upload_image_product));
                    edt_price.setHint(getString(R.string.txt_input_price_product));
                    txt_product_type_content.setHint(getString(R.string.select_product_type));
                    if (isRequirement) {
                        edt_description.setHint(getString(R.string.des_products_req));
                    } else {
                        edt_description.setHint(getString(R.string.des_products));
                    }
                    if (AdType != 2) {
                        layout_image_upload.setVisibility(View.VISIBLE);
                    }
                    setCityDefault();
//                    txt_service_img_alert.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_service:
                Function.hideSoftKeyboard(AddPostActivity.this, v);
                if (tabNo != 2) {
                    tabNo = 2;
                    postImageListAdapter.ChangeSize(4);
                    postImageListAdapter.notifyDataSetChanged();
                    btn_product.setBackground(getResources().getDrawable(R.drawable.bg_orenge_border_left));
                    btn_service.setBackground(getResources().getDrawable(R.drawable.bg_right_orenge));
                    btn_product.setTextColor(getResources().getColor(R.color.txt_orange));
                    btn_service.setTextColor(getResources().getColor(R.color.color_white));
                    categoryList.clear();
                    categoryList.addAll(tempServiceList);
                    categoryListAdapter.notifyDataSetChanged();
//                    txt_product_type_title.setText(getString(R.string.txt_profession));
                    txt_rental_duration_title.setText(getString(R.string.txt_hiring_pattern));
                    txt_rental_fee_title.setText(getString(R.string.txt_hiring_fee));
                    txt_image_content.setText(getString(R.string.txt_upload_image_service));
                    edt_price.setHint(getString(R.string.txt_input_price_service));
                    txt_product_type_content.setHint(getString(R.string.select_profession));
                    if (isRequirement) {
                        edt_description.setHint(getString(R.string.des_service_req));
                    } else {
                        edt_description.setHint(getString(R.string.des_service));
                    }
                    setCityDefault();
                    if (AdType != 2) {
                        layout_image_upload.setVisibility(View.VISIBLE);
                    }
                    if (!isRequirement)
                        txt_service_img_alert.setVisibility(View.GONE);
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
//                        reSendOTP();
                        callOTP();
                    } else {

                        _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
//                                reSendOTP();
                                callOTP();
                            }
                        });
                    }
                    otp_view.setText("");
                } else {
                    Intent i = new Intent(AddPostActivity.this, ReportUsActivity.class);
                    i.putExtra("PageFrom", "1");
                    startActivity(i);
                }
                break;
            case R.id.txt_page_action:
                Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);
                if (filterType == 1) {
                    txt_product_type_content.setText(edt_search.getText().toString());
                    productTitle = edt_search.getText().toString();
                    AdTitleCreation();
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

                    AdTitleCreation();
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
                .enqueue(new RetrofitCallBack(AddPostActivity.this, verifyOTP, true, false));

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
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, emailPhoneValidate, true, false));
        } else {
            _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).emailPhoneRegistration(params)
                            .enqueue(new RetrofitCallBack(AddPostActivity.this, emailPhoneValidate, true, false));
                }
            });
        }

    }

    private void AddPostValidation() {
        if (catId.equals("")) {
            Function.CustomMessage(AddPostActivity.this, "Enter " + titleContent);

            return;
        }


        for (int i = 0; i < attributeList.size(); i++) {

            if (attributeList.get(i).getMandatory().equals("1") && attributeList.get(i).getInputValue().equals("") && !isRequirement) {
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
                if (edt_price.getText().toString().length() == 0 || edt_price.getText().toString().equals("0")) {
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
        } else if (areaId.length() < 3) {
            Function.CustomMessage(AddPostActivity.this, getString(R.string.enter_locality_minimum));
            return;
        }

//        if (AdType != 2) {
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
//        }
        if (AdType != 2 && shareImageList.size() == 0) {
            if (tabNo == 1) {
                Function.CustomMessage(AddPostActivity.this, getString(R.string.image_one_must));
            } else {
                Function.CustomMessage(AddPostActivity.this, getString(R.string.image_one_must));
            }
            return;
        }

        if (!cb_tc.isChecked()) {
            Function.CustomMessage(AddPostActivity.this, getString(R.string.accept_tc));
            return;
        }
        if (!Sessions.getSession(Constant.UserPhone, getApplicationContext()).equals("")) {
            if (Sessions.getSession(Constant.UserPhone, getApplicationContext()).equals(edt_mobile_number.getText().toString()) || postMobileNum != null && postMobileNum.equals(edt_mobile_number.getText().toString())) {
                if (_fun.isInternetAvailable(AddPostActivity.this)) {
                    if (AdType == 0) {
                        savePostDetails();
                    } else {
                        new AsyncTaskSavePost().execute("");
                    }
//
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            if (AdType == 0) {
                                savePostDetails();
                            } else {
                                new AsyncTaskSavePost().execute("");
                            }
//                            new AsyncTaskSavePost().execute("");
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
                    if (AdType == 0) {
                        savePostDetails();
                    } else {
                        new AsyncTaskSavePost().execute("");
                    }
//                    new AsyncTaskSavePost().execute("");
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            if (AdType == 0) {
                                savePostDetails();
                            } else {
                                new AsyncTaskSavePost().execute("");
                            }
//                            new AsyncTaskSavePost().execute("");
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
                .enqueue(new RetrofitCallBack(AddPostActivity.this, reSendOTP, true, false));
    }

    private void savePostDetails() {

        viewDialog.showDialog();
        isClickChange = true;
        try {
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


            List<RequestBody> attId = new ArrayList<>();
            List<RequestBody> attVal = new ArrayList<>();
            for (int i = 0; i < attributeList.size(); i++) {
                attId.add(RequestBody.create(okhttp3.MultipartBody.FORM, attributeList.get(i).getAttributeId()));
                attVal.add(RequestBody.create(okhttp3.MultipartBody.FORM, attributeList.get(i).getInputValue()));

            }
            final RequestBody _parentId = RequestBody.create(okhttp3.MultipartBody.FORM, parentId);
            final RequestBody _catId = RequestBody.create(okhttp3.MultipartBody.FORM, catId);
            final RequestBody _rentalFee = RequestBody.create(okhttp3.MultipartBody.FORM, edt_price.getText().toString());
            final RequestBody _rentalDuration = RequestBody.create(okhttp3.MultipartBody.FORM, rentalDuration);
            final RequestBody _adTitle = RequestBody.create(okhttp3.MultipartBody.FORM, txt_post_title_content.getText().toString());
            final RequestBody _adDescription = RequestBody.create(okhttp3.MultipartBody.FORM, edt_description.getText().toString());
            final RequestBody _adCityId = RequestBody.create(okhttp3.MultipartBody.FORM, cityId);
            final RequestBody _adAreaId = RequestBody.create(okhttp3.MultipartBody.FORM, areaId);
            final RequestBody _adStatus = RequestBody.create(okhttp3.MultipartBody.FORM, adStatus);
            final RequestBody _adNegos = RequestBody.create(okhttp3.MultipartBody.FORM, isNegos);
            final RequestBody _adMobileNo = RequestBody.create(okhttp3.MultipartBody.FORM, edt_mobile_number.getText().toString());
            final RequestBody _adMobileVisible = RequestBody.create(okhttp3.MultipartBody.FORM, isMobile);

            List<MultipartBody.Part> parts = new ArrayList<>();
            for (int i = 0; i < shareImageList.size(); i++) {
                if (shareImageList.get(i).getImgURL().equals("")) {
                    File file = new File(Function.compressImage(shareImageList.get(i).getUri()));
                    final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    parts.add(MultipartBody.Part.createFormData("photos[]", file.getName(), requestFile));
                }

            }
            RequestBody.create(okhttp3.MultipartBody.FORM, isMobile);

            RetrofitClient.getClient().create(Api.class).savePostForm(parts, _catId, _rentalFee, _rentalDuration, _adTitle, _adDescription, _adCityId,
                    _adAreaId, _adStatus, _adNegos, attId, attVal, _adMobileNo, _adMobileVisible, _parentId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, savePost, false, false));
            Log.i("Form called", "True");


            Log.i("Token", Sessions.getSession(Constant.UserToken, getApplicationContext()));
        } catch (Exception e) {
            viewDialog.hideDialog();
            isClickChange = false;
        }
    }

    private class AsyncTaskSavePost extends AsyncTask<String, String, HashMap<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewDialog.showDialog();
            isClickChange = true;
        }

        @Override
        protected HashMap<String, Object> doInBackground(String... strings) {
            try {
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

                String attId = "";
                String attVal = "";
                HashMap<Object, Object> paramsId = new HashMap<Object, Object>();
                HashMap<Object, Object> paramsValue = new HashMap<Object, Object>();
                for (int i = 0; i < attributeList.size(); i++) {
                    paramsId.put(String.valueOf(i), attributeList.get(i).getAttributeId());
                    paramsValue.put(String.valueOf(i), attributeList.get(i).getInputValue());
                    if (i == 0) {
                        attId = attributeList.get(i).getAttributeId();
                        attVal = attributeList.get(i).getInputValue();
                    } else {
                        attId = attId + attributeList.get(i).getAttributeId();
                        attVal = attVal + attributeList.get(i).getInputValue();
                    }
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
                params.put("is_base", "1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return params;
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> params) {
            super.onPostExecute(params);
            if (AdType == 0) {
                RetrofitClient.getClient().create(Api.class).savePost(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(AddPostActivity.this, savePost, false, false));

            } else if (AdType == 1) {
                params.put("ads_id", adId);


                RetrofitClient.getClient().create(Api.class).updatePost(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(AddPostActivity.this, savePost, false, false));

            } else if (AdType == 2) {
                if (EditType.equals("2")) {
                    params.put("ads_id", adId);
                    RetrofitClient.getClient().create(Api.class).updateRequirementPost(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(AddPostActivity.this, savePost, false, false));
                } else {
                    RetrofitClient.getClient().create(Api.class).saveRequirementPost(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(AddPostActivity.this, savePost, false, false));

                }
            }

            Log.i("Params", params.toString());
            Log.i("Token", Sessions.getSession(Constant.UserToken, getApplicationContext()));
        }
    }


    private void areaCallBack(AreaListAdapter areaListAdapter) {
        areaListAdapter.setCallback(new AreaListAdapter.Callback() {
            @Override
            public void itemClick(AreaItemModel model) {
//                Function.hideSoftKeyboard(AddPostActivity.this, btn_add_post);
                txt_area_content.setText(model.getAreaName());
                areaId = model.getAreaName().trim();
                ListVisible(false);
            }
        });
    }

    private void cityCallBack(CityListAdapter cityListAdapter) {
        cityListAdapter.setCallback(new CityListAdapter.Callback() {
            @Override
            public void itemClick(final CityItemModel model) {
                Function.hideSoftKeyboard(AddPostActivity.this, txt_city_content);
                txt_city_content.setText(model.getCityName());
                cityId = model.getCityId();
                txt_area_content.setText("");
                txt_area_content.setFocusableInTouchMode(true);
                areaId = "";
                ListVisible(false);
                if (_fun.isInternetAvailable(AddPostActivity.this)) {
                    GetArea(model.getCityId(), true);
                } else {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            GetArea(model.getCityId(), true);
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
                List<Uri> uris = Matisse.obtainResult(data);
                if (slist.size() > 0) {
                    for (int i = 0; i < slist.size(); i++) {
                        imagePath = slist.get(i);
                        if (tabNo == 1) {
                            CropImage.activity(uris.get(i))
                                    .setFixAspectRatio(false)
                                    .start(AddPostActivity.this);
                        } else {
                            CropImage.activity(uris.get(i))
                                    .setFixAspectRatio(true)
                                    .setAspectRatio(4, 4)
                                    .setCropShape(CropImageView.CropShape.OVAL)
                                    .start(AddPostActivity.this);
                        }
                        Log.i("Path", slist.get(i));
                    }
                }
                Log.e("Matisse", "PostSelected: " + selectedPaths + "\n" + selectedUris.size());

            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (result.getType() == 2) {
                Uri resultUri = result.getUri();
                shareImageList.add(new UploadImageModel("", true, resultUri.getPath(), "1"));
                Log.e("Crop", "PostSelected: " + resultUri.getPath() + "\n" + selectedUris.size());
            } else {
                shareImageList.add(new UploadImageModel("", true, imagePath, "1"));
            }
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
                .enqueue(new RetrofitCallBack(AddPostActivity.this, attributeValue, true, false));
    }

    public void GetCity() {

        if (_fun.isInternetAvailable(AddPostActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getCity()
                    .enqueue(new RetrofitCallBack(AddPostActivity.this, cityValue, false, false));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            RetrofitClient.getClient().create(Api.class).getCity()
                                    .enqueue(new RetrofitCallBack(AddPostActivity.this, cityValue, false, false));
                        }
                    });
                }
            }, 200);

        }
    }

    public void GetArea(String cityId, boolean isLoad) {
        RetrofitClient.getClient().create(Api.class).getArea(cityId)
                .enqueue(new RetrofitCallBack(AddPostActivity.this, areaValue, isLoad, false));
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
                        attributeList.add(new AttributeItemModel(dr.getAttributeList().get(i).getName(), dr.getAttributeList().get(i).getAttributeValues(), dr.getAttributeList().get(i).getType(), dr.getAttributeList().get(i).getAttributeId(), dr.getAttributeList().get(i).getMandatory(), dr.getAttributeList().get(i).getIsTitle(), dr.getAttributeList().get(i).getIsPrefix(), "", ""));
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
                    if (AdType != 1 && !EditType.equals("2")) {

                        setCityDefault();
                    }

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

    private void setCityDefault() {
        if (!Sessions.getSession(Constant.CityId, getApplicationContext()).equals("")) {
            txt_city_content.setText(Sessions.getSession(Constant.CityName, getApplicationContext()));
            cityId = Sessions.getSession(Constant.CityId, getApplicationContext());
            txt_area_content.setText("");
            txt_area_content.setFocusableInTouchMode(true);
            areaId = "";

            if (_fun.isInternetAvailable(AddPostActivity.this)) {
                GetArea(cityId, false);
            } else {
                _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                    @Override
                    public void isInternet() {
                        GetArea(cityId, false);
                    }
                });
            }
        }
    }

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
                    txt_area_content.setThreshold(1);
                    areaAutoCompleteAdapter.setCallback(new AreaAutoCompleteAdapter.Callback() {
                        @Override
                        public void itemClick(AreaItemModel model) {
                            Function.hideSoftKeyboard(AddPostActivity.this, txt_city_content);
                            txt_area_content.setText(model.getAreaName().trim());
                            txt_area_content.dismissDropDown();
                            txt_area_content.setSelection(txt_area_content.getText().toString().length());
                            areaId = model.getAreaName().trim();
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
            viewDialog.hideDialog();
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
                    Constant.ParentId = "";

                } else {
                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
                }

                isClickChange = false;
            } catch (Exception e) {
                e.printStackTrace();
                viewDialog.hideDialog();
                isClickChange = false;
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            call.cancel();
            viewDialog.hideDialog();
            isClickChange = false;
        }
    };
    retrofit2.Callback<DefaultResponse> checkServicePost = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            DefaultResponse dr = response.body();
            try {
                if (dr.isStatus()) {
                    Function.CustomMessage(AddPostActivity.this, dr.getMessage());
                } else {
                    isServiceValid = true;
//                    savePostDetails();
                    new AsyncTaskSavePost().execute("");
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

                        changeDeductedAmount = true;
                        EditAdDetailsModel editAdDetailsModel = dr.getEditDataModel().getAdDetailsModel();
                        adStatus = editAdDetailsModel.getAdStatus();
                        layout_list.setVisibility(View.GONE);
                        isPostView = false;
                        layout_post_details.setVisibility(View.VISIBLE);
                        rc_category.setVisibility(View.VISIBLE);
                        rc_service.setVisibility(View.GONE);
                        if (!adStatus.equals("0")) {
                            layout_product_type.setEnabled(false);
                            txt_product_type_content.setTextColor(getResources().getColor(R.color.hint_txt_color));
                            txt_product_type_content.setFocusableInTouchMode(false);
                            txt_product_type_title.setTextColor(getResources().getColor(R.color.hint_txt_color));

                        }
                        layout_selection_button.setVisibility(View.GONE);
                        txt_product_type_title.setText(dr.getEditDataModel().getCategoryTitle() + " *");
                        txt_product_type_content.setHint("Enter " + dr.getEditDataModel().getCategoryTitle());

                        if (dr.getEditDataModel().getCatGroup().equals("1")) {
                            tabNo = 1;
                            postImageListAdapter.ChangeSize(4);
                            postImageListAdapter.notifyDataSetChanged();
//                            txt_product_type_title.setText(getString(R.string.txt_product_type));
                            txt_rental_duration_title.setText(getString(R.string.txt_rental_duration));
                            txt_rental_fee_title.setText(getString(R.string.txt_rental_fee));
                            txt_image_content.setText(getString(R.string.txt_upload_image_product));
                            edt_price.setHint(getString(R.string.txt_input_price_product));
                            if (isRequirement) {
                                edt_description.setHint(getString(R.string.des_products_req));
                            } else {
                                edt_description.setHint(getString(R.string.des_products));
                            }
//                            txt_product_type_content.setHint(getString(R.string.select_product_type));
                            layout_image_upload.setVisibility(View.VISIBLE);
                            if (!EditType.equals("1")) {
                                layout_image_upload.setVisibility(View.GONE);
                            }
                        } else {
                            tabNo = 2;
                            postImageListAdapter.ChangeSize(4);
                            postImageListAdapter.notifyDataSetChanged();
//                            txt_product_type_title.setText(getString(R.string.txt_profession));
                            txt_rental_duration_title.setText(getString(R.string.txt_hiring_pattern));
                            txt_rental_fee_title.setText(getString(R.string.txt_hiring_fee));
                            txt_image_content.setText(getString(R.string.txt_upload_image_service));
                            edt_price.setHint(getString(R.string.txt_input_price_service));
                            if (isRequirement) {
                                edt_description.setHint(getString(R.string.des_service_req));
                            } else {
                                edt_description.setHint(getString(R.string.des_service));
                            }

//                            txt_product_type_content.setHint(getString(R.string.select_profession));
                            if (!EditType.equals("1")) {
                                layout_image_upload.setVisibility(View.GONE);
                            }
//                            if (AdType != 2)
//                                txt_service_img_alert.setVisibility(View.VISIBLE);
                        }
                        txt_parent_title.setText(dr.getEditDataModel().getParentTitle());


                        txt_product_type_content.setText(dr.getEditDataModel().getProductType().get(0).getChildCategoryName().trim());
                        catId = dr.getEditDataModel().getProductType().get(0).getChildCategoryName().trim();
                        productTitle = dr.getEditDataModel().getProductType().get(0).getChildCategoryName().trim();
                        if (attributeList.size() != 0) {
                            attributeList.clear();
                        }
//                        attributeList.addAll(dr.getEditDataModel().getAttributeModel().getAttributeEditList());
                        for (int i = 0; i < dr.getEditDataModel().getAttributeModel().getAttributeEditList().size(); i++) {
                            attributeList.add(new AttributeItemModel(dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getName(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getAttributeValues(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getType(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getAttributeId(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getMandatory(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getIsTitle(), dr.getEditDataModel().getAttributeModel().getAttributeEditList().get(i).getIsPrefix(), "", ""));
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
                                if (attributeList.get(i).getIsPrefix() != null && !attributeList.get(i).getIsPrefix().equals("") && !attributeList.get(i).getIsPrefix().equals("null")) {
                                    isPrefix = true;
                                    if (!attributeTitle.equals(""))
                                        attributeTitle = attributeList.get(i).getIsPrefix() + " " + attributeTitle;
                                } else {
                                    isPrefix = false;
                                }
                            }

                        }
                        if (!adStatus.equals("0")) {
                            attributeListAdapter.SetEditable(true);
                        }
                        attributeListAdapter.notifyDataSetChanged();
                        RentalDurationList(dr.getEditDataModel().getRentalDuration());
                        rentalDurationAdapter.notifyDataSetChanged();

                        for (int i = 0; i < rentalDurationList.size(); i++) {
                            if (editAdDetailsModel.getRentalDuration().equals(rentalDurationList.get(i))) {
                                rentalDuration = rentalDurationList.get(i);
                                txt_rental_duration_content.setText(rentalDurationList.get(i));
                                setRentalDuration(rentalDurationList.get(i));
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
                                String[] tempPrice = editAdDetailsModel.getRentalFee().split("\\.");
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
                                areaId = areaList.get(a).getAreaName().trim();
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
                        parentId = dr.getEditDataModel().getProductType().get(0).getParentId();
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
                        changeDeductedAmount = false;
                        if (adStatus.equals("0")) {
                            subCategoryList.clear();
                            subCategoryList.addAll(dr.getEditDataModel().getProductTypeSuggestion());
                            parentId = dr.getEditDataModel().getProductType().get(0).getParentId();
                            productAutoCompleteAdapter = new ProductAutoCompleteAdapter(AddPostActivity.this, R.layout.layout_sub_category_item, subCategoryList);
                            txt_product_type_content.setThreshold(1);
                            txt_product_type_content.setAdapter(productAutoCompleteAdapter);
                            txt_product_type_content.setSelected(true);
                            SubCategoryCallBack();
                        }
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
//                        savePostDetails();
                        new AsyncTaskSavePost().execute("");
                    } else {
                        _fun.ShowNoInternetPopup(AddPostActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
//                                savePostDetails();
                                new AsyncTaskSavePost().execute("");
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
        params.put("message_type", "4");
        Log.i("params", params.toString());
        RetrofitClient.getClient().create(Api.class).sendOTP(params)
                .enqueue(new RetrofitCallBack(AddPostActivity.this, sendOTP, true, false));

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
        AdTitleCreation();
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
            if (checkData()) {
                WarningPopup();
            } else {
                isPostView = false;
                layout_list.setVisibility(View.VISIBLE);
                layout_post_details.setVisibility(View.GONE);
                tb_offer_need.setVisibility(View.GONE);
                isCity = false;
                layout_search.setVisibility(View.GONE);
                layout_selection_button.setVisibility(View.VISIBLE);
                rc_category.setVisibility(View.GONE);
                rc_service.setVisibility(View.VISIBLE);
                if (isRequirement && !parentId.equals("")) {
//                    AdType = 0;
                    btn_product.performClick();
//                    layout_input_price.setVisibility(View.GONE);
//                    view_rental_fee.setVisibility(View.GONE);
                }
            }

        } else {
            finish();
            super.onBackPressed();
        }

    }

    private void WarningPopup() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                AddPostActivity.this);

        alertDialog2.setMessage(getString(R.string.ad_discard));

        alertDialog2.setPositiveButton("DISCARD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        isPostView = false;
                        layout_list.setVisibility(View.VISIBLE);
                        layout_post_details.setVisibility(View.GONE);
                        tb_offer_need.setVisibility(View.GONE);
                        isCity = false;
                        layout_search.setVisibility(View.GONE);
                        layout_selection_button.setVisibility(View.VISIBLE);
                        rc_category.setVisibility(View.GONE);
                        rc_service.setVisibility(View.VISIBLE);
                        ClearFileds();

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

    public boolean checkData() {
        return !txt_product_type_content.getText().toString().equals("") || !edt_price.getText().toString().equals("") || !cityId.equals("") || !edt_description.getText().toString().equals("") || shareImageList.size() != 0;
    }

    public void ClearFileds() {
        productTitle = "";
        attributeTitle = "";

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
        cb_tc.setChecked(false);
        rentalDuration = rentalDurationList.get(0);
        setRentalDuration(rentalDurationList.get(0));
        txt_rental_duration_content.setText(rentalDurationList.get(0));
        setCityDefault();
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
                if (EditType.equals("")) {
                    Intent h = new Intent(AddPostActivity.this, HomeActivity.class);
                    h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(h);
                }
                finish();
            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = false;
        for (int i = 0; i < Constant.Permissions.length; i++) {
            int result1 = ContextCompat.checkSelfPermission(AddPostActivity.this, Constant.Permissions[i]);
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        if (result) {
            MatisseActivity.PAGE_FROM = 2;
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
                    .maxSelectable(1)
                    .countable(false)
                    .showSingleMediaType(true)
                    .forResult(50);
        }
    }
}
