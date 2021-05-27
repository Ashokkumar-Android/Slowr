package com.slowr.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.slowr.app.R;
import com.slowr.app.adapter.FilterOptionAdapter;
import com.slowr.app.adapter.FilterSelectAdapter;
import com.slowr.app.adapter.HomeAdGridAdapter;
import com.slowr.app.adapter.HomeAdListAdapter;
import com.slowr.app.adapter.HomeBannerAdapter;
import com.slowr.app.adapter.HomeCustomListAdapter;
import com.slowr.app.adapter.ProductCategoryListAdapter;
import com.slowr.app.adapter.SearchSuggistionAdapter;
import com.slowr.app.adapter.ServiceCategoryListAdapter;
import com.slowr.app.adapter.SortByAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.components.carouselview.CarouselView;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.models.AppVersionModel;
import com.slowr.app.models.BannerItemModel;
import com.slowr.app.models.CategoryItemModel;
import com.slowr.app.models.CityItemModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.Fillter.FilterModel;
import com.slowr.app.models.FilterResult.FilterResult;
import com.slowr.app.models.FiltersModel;
import com.slowr.app.models.HomeAdsModel;
import com.slowr.app.models.HomeBannerModel;
import com.slowr.app.models.HomeDetailsModel;
import com.slowr.app.models.HomeFlyersModel;
import com.slowr.app.models.SortByModel;
import com.slowr.app.models.SuggistionItem;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

public class HomeActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseActivity.CallBackCity {
    ViewPager vp_banner;
    NestedScrollView layout_home;
    LinearLayout layout_search_list;
    ImageView img_list;
    ImageView img_grid;
    ImageView img_back;
    LinearLayout layout_sort_by;
    LinearLayout layout_filter;
    LinearLayout layout_requirement_ad;
    LinearLayout layout_list_filter;
    Button btn_need_req;
    SwipeRefreshLayout layout_swipe_refresh;
    CardView layout_filter_root;
    CardView layout_no_ad_city;
    Button btn_offer;
    Button btn_need;
    Button btn_offer_req;
    ImageView img_category_no_ad;
    TextView txt_category_no_ad;
    TextView txt_category_name;

    RecyclerView rc_product_list;
    RecyclerView rc_service_list;
    RecyclerView rc_ad_list;
    RecyclerView rc_home_ad_list;
    CarouselView rc_banner;
    ProgressBar pb_circule;

    ProductCategoryListAdapter productCategoryListAdapter;
    ServiceCategoryListAdapter serviceCategoryListAdapter;
    HomeAdListAdapter homeAdListAdapter;
    HomeAdGridAdapter homeAdGridAdapter;
    HomeCustomListAdapter homeCustomListAdapter;

    ArrayList<CategoryItemModel> categoryList = new ArrayList<>();
    ArrayList<CategoryItemModel> serviceList = new ArrayList<>();
    ArrayList<AdItemModel> adList = new ArrayList<>();
    ArrayList<AdItemModel> popularAdList = new ArrayList<>();
    ArrayList<HomeAdsModel> homeAdList = new ArrayList<>();
    ArrayList<SortByModel> sortByList = new ArrayList<>();
    ArrayList<FiltersModel> filterList = new ArrayList<>();
    ArrayList<SortByModel> filterSelectList = new ArrayList<>();
    ArrayList<SuggistionItem> searchSuggestionList = new ArrayList<>();
    ArrayList<BannerItemModel> bannerList = new ArrayList<>();

    private static int currentPage = 0;
    private static int NUM_PAGES = 3;
    boolean isCategory = false;
    boolean isGrid = false;
    int favPosition = 0;
    int currentPageNo = 1;
    int lastPageNo = 1;
    String sortById = "";
    String catId = "";
    String productType = "";
    boolean clickRefresh = false;

    HashMap<String, Object> params = new HashMap<String, Object>();


    View rootView = null;

    GridLayoutManager gridManager;
    LinearLayoutManager listManager;
    boolean doubleBackToExitPressedOnce = false;
    private PopupWindow spinnerPopup, filterPopup, demoPopup;
    SortByAdapter sortByAdapter;
    FilterOptionAdapter filterOptionAdapter;
    FilterSelectAdapter filterSelectAdapter;
    SearchSuggistionAdapter searchSuggistionAdapter;
    HomeBannerAdapter homeBannerAdapter;
    boolean isFilterClear = false;
    int EDIT_AD_VIEW = 1266;
    int LOGIN_VIEW = 1299;
    boolean isRegister = false;
    private Function _fun = new Function();

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    boolean isLoading = false;
    boolean isRefresh = false;

    boolean isBannerStarted = false;
    boolean isViewBack = false;
    boolean isFilterSubCat = false;


    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private static final int REQUEST_CODE = 101;
    String cityName = "";
    BaseActivity homeActivity;
    CallBackCity callBackCity;
    String filterSubCatId = "";
    int filterSelectCount = 0;
    String maxValue = "";
    String minValue = "";
    int filterProSer = 3;

    long startTime;

    boolean requestFlrClick = false;
    String requestFlyType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_home, contentFrameLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        Constant.ParentId = "";
        doDeclaration();
    }

    private void doDeclaration() {
        if (getIntent().hasExtra("IsRegister")) {
            isRegister = true;
            Log.i("IsRegister", "Called");
        }

        if (getIntent().hasExtra("ProsperId")) {
            String proID = getIntent().getStringExtra("ProsperId");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ShowPopupNoProsperPage(proID);
                }
            }, 200);
        }
        homeActivity = new BaseActivity();
        homeActivity.callBackCity = callBackCity;
        rc_product_list = findViewById(R.id.rc_product_list);
        rc_service_list = findViewById(R.id.rc_service_list);
        rc_ad_list = findViewById(R.id.rc_ad_list);
        rc_home_ad_list = findViewById(R.id.rc_home_ad_list);
        vp_banner = findViewById(R.id.vp_banner);
        layout_home = findViewById(R.id.layout_home);
        layout_search_list = findViewById(R.id.layout_search_list);
        img_list = findViewById(R.id.img_list);
        img_grid = findViewById(R.id.img_grid);
        img_back = findViewById(R.id.img_back);
        layout_sort_by = findViewById(R.id.layout_sort_by);
        layout_filter = findViewById(R.id.layout_filter);
        btn_need_req = findViewById(R.id.btn_need_req);
        layout_requirement_ad = findViewById(R.id.layout_requirement_ad);
        layout_list_filter = findViewById(R.id.layout_list_filter);
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);
        rc_banner = findViewById(R.id.rc_banner);
        layout_filter_root = findViewById(R.id.layout_filter_root);
        layout_no_ad_city = findViewById(R.id.layout_no_ad_city);
        btn_offer = findViewById(R.id.btn_offer);
        btn_need = findViewById(R.id.btn_need);
        btn_offer_req = findViewById(R.id.btn_offer_req);
        pb_circule = findViewById(R.id.pb_circule);
        img_category_no_ad = findViewById(R.id.img_category_no_ad);
        txt_category_no_ad = findViewById(R.id.txt_category_no_ad);
        txt_category_name = findViewById(R.id.txt_category_name);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false);
        rc_product_list.setLayoutManager(linearLayoutManager1);
        rc_product_list.setItemAnimator(new DefaultItemAnimator());
        productCategoryListAdapter = new ProductCategoryListAdapter(categoryList, HomeActivity.this);
        rc_product_list.setAdapter(productCategoryListAdapter);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false);
        rc_service_list.setLayoutManager(linearLayoutManager2);
        rc_service_list.setItemAnimator(new DefaultItemAnimator());
        serviceCategoryListAdapter = new ServiceCategoryListAdapter(serviceList, HomeActivity.this);
        rc_service_list.setAdapter(serviceCategoryListAdapter);

        gridManager = new GridLayoutManager(HomeActivity.this, 2);
        listManager = new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, false);
        rc_ad_list.setLayoutManager(listManager);
        rc_ad_list.setItemAnimator(new DefaultItemAnimator());
        homeAdListAdapter = new HomeAdListAdapter(adList, HomeActivity.this, true);
        homeAdGridAdapter = new HomeAdGridAdapter(adList, HomeActivity.this);
        rc_ad_list.setAdapter(homeAdListAdapter);


        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, false);
        rc_home_ad_list.setLayoutManager(linearLayoutManager3);
        rc_home_ad_list.setItemAnimator(new DefaultItemAnimator());
        homeCustomListAdapter = new HomeCustomListAdapter(homeAdList, getApplicationContext());

        rc_home_ad_list.setAdapter(homeCustomListAdapter);
        sortByAdapter = new SortByAdapter(sortByList, getApplicationContext());
        filterOptionAdapter = new FilterOptionAdapter(filterList, getApplicationContext());
        filterSelectAdapter = new FilterSelectAdapter(filterSelectList, getApplicationContext());
        searchSuggistionAdapter = new SearchSuggistionAdapter(searchSuggestionList, getApplicationContext());


        homeBannerAdapter = new HomeBannerAdapter(bannerList, HomeActivity.this);
        rc_banner.setAdapter(homeBannerAdapter);
        homeBannerAdapter.hideText(true);


        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        img_back.setOnClickListener(this);
        img_list.setOnClickListener(this);
        img_grid.setOnClickListener(this);
        layout_sort_by.setOnClickListener(this);
        layout_filter.setOnClickListener(this);
        btn_need.setOnClickListener(this);
        btn_offer.setOnClickListener(this);
        btn_need_req.setOnClickListener(this);
        btn_offer_req.setOnClickListener(this);
        layout_swipe_refresh.setOnRefreshListener(this);
        CallBackFunction();
//        getCategory();
//        setBanner();
        if (_fun.isInternetAvailable(HomeActivity.this)) {
            getHomeBannerDetails(true);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getHomeBannerDetails(true);
                        }
                    });
                }
            }, 200);

        }


        rc_ad_list.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return false;
            }
        });
        Pageination();
        CheckAppUpdate();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);

        saveUserDetails();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ShowPopupProsper();
//            }
//        }, 200);
    }

    private void saveUserDetails() {
        GetLocationDetails();
    }

    private void GetLocationDetails() {
        try {


            Log.i("User Location", "Step1");
            if (_fun.checkPermissionLocation(HomeActivity.this)) {

                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.i("User Location", "Step2");
                        if (location != null) {
                            currentLocation = location;
                            Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                cityName = addresses.get(0).getLocality();
                                String stateName = addresses.get(0).getAdminArea();
                                String countryName = addresses.get(0).getCountryName();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        } else {
                            Log.i("User Location", "Step4");
                        }
                        if (!params.isEmpty()) {
                            params.clear();
                        }

                        String deviceDet = "{" + getHardwareAndSoftwareInfo() + "}";
                        params.put("city", cityName);
                        if (currentLocation != null) {
                            params.put("longitude", String.valueOf(currentLocation.getLongitude()));
                            params.put("latitude", String.valueOf(currentLocation.getLatitude()));
                        } else {
                            params.put("longitude", "");
                            params.put("latitude", "");
                        }
                        params.put("device_details", deviceDet);
                        if (isRegister) {
                            params.put("action", "1");
                        }
                        Log.i("Params", params.toString());
                        RetrofitClient.getClient().create(Api.class).deviceDetails(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                .enqueue(new RetrofitCallBack(HomeActivity.this, deviceDetailsResponse, false, false));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!params.isEmpty()) {
                params.clear();
            }

            String deviceDet = "{" + getHardwareAndSoftwareInfo() + "}";
            params.put("city", "");

            params.put("longitude", "");
            params.put("latitude", "");

            params.put("device_details", deviceDet);
            if (isRegister) {
                params.put("action", "1");
            }
            Log.i("Params", params.toString());
            RetrofitClient.getClient().create(Api.class).deviceDetails(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, deviceDetailsResponse, false, false));
        }
    }

    private String getHardwareAndSoftwareInfo() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        return getString(R.string.serial) + " " + Build.SERIAL + "\n" +
                getString(R.string.model) + " " + Build.MODEL + "\n" +
                getString(R.string.id) + " " + Build.ID + "\n" +
                getString(R.string.manufacturer) + " " + Build.MANUFACTURER + "\n" +
                getString(R.string.brand) + " " + Build.BRAND + "\n" +
                getString(R.string.type) + " " + Build.TYPE + "\n" +
                getString(R.string.user) + " " + Build.USER + "\n" +
                getString(R.string.base) + " " + Build.VERSION_CODES.BASE + "\n" +
                getString(R.string.incremental) + " " + Build.VERSION.INCREMENTAL + "\n" +
                getString(R.string.sdk) + " " + Build.VERSION.SDK + "\n" +
                getString(R.string.board) + " " + Build.BOARD + "\n" +
                getString(R.string.host) + " " + Build.HOST + "\n" +
                getString(R.string.fingerprint) + " " + Build.FINGERPRINT + "\n" +
                getString(R.string.versioncode) + " " + Build.VERSION.RELEASE;
    }

    private void CheckAppUpdate() {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("platform", "1");
        params.put("version", String.valueOf(Function.getAppVersionCode(HomeActivity.this)));
        Log.i("params", params.toString());
        if (_fun.isInternetAvailable(HomeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).appVersionCheck(params)
                    .enqueue(new RetrofitCallBack(HomeActivity.this, checkApp, false, false));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            RetrofitClient.getClient().create(Api.class).appVersionCheck(params)
                                    .enqueue(new RetrofitCallBack(HomeActivity.this, checkApp, false, false));
                        }
                    });
                }
            }, 200);

        }
    }


    private void getHomeBannerDetails(boolean isLoad) {
        layout_no_ad_city.setVisibility(View.GONE);
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("cityId", Sessions.getSession(Constant.CityId, HomeActivity.this));
        Log.i("Params", params.toString());

        if (_fun.isInternetAvailable(HomeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getHomeBanners(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, homeBannerResponse, isLoad, false));
        } else {
            _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getHomeBanners(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(HomeActivity.this, homeBannerResponse, isLoad, false));
                }
            });
        }
    }

    private void getHomeAdsList(boolean isLoad) {
        pb_circule.setVisibility(View.VISIBLE);
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("cityId", Sessions.getSession(Constant.CityId, HomeActivity.this));
        Log.i("Params", params.toString());

        if (_fun.isInternetAvailable(HomeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getHomeFlyers(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, homeFlyersResponse, isLoad, false));
        } else {
            _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getHomeFlyers(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(HomeActivity.this, homeFlyersResponse, isLoad, false));
                }
            });
        }

    }

    private void getHomeDetails(boolean isLoad) {


        if (_fun.isInternetAvailable(HomeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getHomeCategory(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, homeDetailsResponse, isLoad, false));
        } else {
            _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getHomeCategory(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(HomeActivity.this, homeDetailsResponse, isLoad, false));
                }
            });
        }
    }

    private void CallBackFunction() {
        homeBannerAdapter.setCallback(new HomeBannerAdapter.Callback() {
            @Override
            public void itemClick(BannerItemModel model, boolean isClick) {
                String userProsperId = model.getProsperId();

                if (isClick) {
                    isBannerStarted = false;
                    rc_banner.stop();
                    startTime = System.currentTimeMillis();

                } else {
                    isBannerStarted = true;
                    rc_banner.start(6, TimeUnit.SECONDS);
                    long difference = System.currentTimeMillis() - startTime;
                    if (difference < 1000) {
                        if (userProsperId != null) {
                            Intent i = new Intent(HomeActivity.this, UserProfileActivity.class);
                            i.putExtra("prosperId", userProsperId);
                            i.putExtra("PageFrom", "1");
                            i.putExtra("PageID", "5");
                            startActivity(i);
                        } else {
                            ShowPopupDefauldBanner();
                        }
                    }
                }

            }
        });
        homeAdGridAdapter.setCallback(new HomeAdGridAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                String adId = adList.get(pos).getAdSlug();
                String userId = adList.get(pos).getUserId();
                clickRefresh = true;
                changeFragment(adId, userId);
            }

            @Override
            public void onFavoriteClick(int pos) {

                if (!adList.get(pos).isProgress()) {
                    if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                        favPosition = pos;
                        adList.get(pos).setProgress(true);

                        if (_fun.isInternetAvailable(HomeActivity.this)) {
                            callAddFavorite();
                        } else {
                            _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {
                                    callAddFavorite();
                                }
                            });


                        }
                    } else {
                        Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivityForResult(l, LOGIN_VIEW);
                    }


                }
            }

            @Override
            public void onShareClick(int pos) {
                String adId = adList.get(pos).getAdSlug();
                String adTitle = adList.get(pos).getAdTitle();
                String catGroup = adList.get(pos).getCatGroup();
                String url = adList.get(pos).getPhotoType();

                Function.ShareLink(HomeActivity.this, adId, adTitle, catGroup, url);
            }

            @Override
            public void onLoginClick(int pos) {
                Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                startActivityForResult(l, LOGIN_VIEW);
            }
        });

        homeAdListAdapter.setCallback(new HomeAdListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                String adId = adList.get(pos).getAdSlug();
                String userId = adList.get(pos).getUserId();
                clickRefresh = true;
                changeFragment(adId, userId);

            }

            @Override
            public void onFavoriteClick(int pos) {

//                if (!adList.get(pos).isProgress()) {
//                    favPosition = pos;
//                    adList.get(pos).setProgress(true);
//                    callAddFavorite();
//
//                }
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    favPosition = pos;
                    adList.get(pos).setProgress(true);
                    if (_fun.isInternetAvailable(HomeActivity.this)) {
                        callAddFavorite();
                    } else {
                        _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                callAddFavorite();
                            }
                        });

                    }
                } else {
                    Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(l, LOGIN_VIEW);
                }

            }

            @Override
            public void onShareClick(int pos) {
                String adId = adList.get(pos).getAdSlug();
                String adTitle = adList.get(pos).getAdTitle();
                String catGroup = adList.get(pos).getCatGroup();
                String url = adList.get(pos).getPhotoType();
                Function.ShareLink(HomeActivity.this, adId, adTitle, catGroup, url);
            }

            @Override
            public void onLoginClick(int pos) {
                Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                startActivityForResult(l, LOGIN_VIEW);
            }
        });

        homeCustomListAdapter.setCallback(new HomeCustomListAdapter.Callback() {
            @Override
            public void itemClick(AdItemModel model) {
                String adId = model.getAdSlug();
                String userId = model.getUserId();
                clickRefresh = false;
                changeFragment(adId, userId);
            }

            @Override
            public void viewMoreClick(int pos) {
                isCategory = true;
                isFilterClear = false;
                sortByList.clear();
                filterList.clear();

                productType = homeAdList.get(pos).getProductType();
                filterProSer = Integer.valueOf(homeAdList.get(pos).getProductType());
                currentPageNo = 1;
                catId = "";
                sortById = "";
                minValue = "";
                maxValue = "";
                filterSubCatId = "";
                isFilterSubCat = false;
                filterSelectCount = 0;
                txt_category_name.setText(homeAdList.get(pos).getListTitle());
                getFilterData(false);
                getAdList(true);

            }

            @Override
            public void requestFlyersClick(String type) {
                requestFlyType = type;
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    p.putExtra("ParId", "");
                    p.putExtra("RequstType", type);
                    startActivity(p);
                    callInsigths(type);
                } else {
                    Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(l, LOGIN_VIEW);
                    requestFlrClick = true;
                }
            }
        });

        productCategoryListAdapter.setCallback(new ProductCategoryListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                isCategory = true;
                isFilterClear = false;
                sortByList.clear();
                filterList.clear();
                isFilterSubCat = false;
                productType = "";
                filterSubCatId = "";
                sortById = "";
                minValue = "";
                maxValue = "";
                filterSelectCount = 0;
                filterProSer = 1;
                if (sortByAdapter != null) {
                    sortByAdapter.clearValues();
                }
                catId = categoryList.get(pos).getId();
                txt_category_name.setText(categoryList.get(pos).getName());
                Constant.ParentId = catId;
                currentPageNo = 1;
                getFilterData(false);
                getAdList(true);
            }
        });
        serviceCategoryListAdapter.setCallback(new ServiceCategoryListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                isCategory = true;
                isFilterClear = false;
                sortByList.clear();
                filterList.clear();
                isFilterSubCat = false;
                catId = serviceList.get(pos).getId();
                Constant.ParentId = catId;
                currentPageNo = 1;
                productType = "";
                filterSubCatId = "";
                sortById = "";
                minValue = "";
                maxValue = "";
                filterSelectCount = 0;
                filterProSer = 2;
                txt_category_name.setText(serviceList.get(pos).getName());
                if (sortByAdapter != null) {
                    sortByAdapter.clearValues();
                }
                getFilterData(false);
                getAdList(true);

            }
        });

    }

    private void callInsigths(String type) {

        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("request_type", type);
        params.put("platform", "2");
        Log.i("Params", params.toString());

        RetrofitClient.getClient().create(Api.class).storeRequest(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(HomeActivity.this, requestFlyerResponse, false, false));

    }

    private void getFilterData(boolean isLoad) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("parent_id", catId);
        params.put("subcategory_id", filterSubCatId);
        params.put("category_type", productType);
        params.put("city_id", Sessions.getSession(Constant.CityId, getApplicationContext()));
        Log.i("Params", params.toString());
        RetrofitClient.getClient().create(Api.class).getFilter(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(HomeActivity.this, filterResponse, isLoad, false));
    }

    private void callAddFavorite() {
        String adId = adList.get(favPosition).getAdSlug();
        String isFav = adList.get(favPosition).getIsFavorite();
        if (isFav.equals("0")) {
            if (!params.isEmpty()) {
                params.clear();
            }
            params.put("ads_id", adId);
            Log.i("Params", params.toString());
            RetrofitClient.getClient().create(Api.class).addFavorite(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, addFavorite, false, false));
        } else {
            RetrofitClient.getClient().create(Api.class).deleteFavorite(adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, addFavorite, false, false));
        }
    }

    void changeFragment(String adId, String userId) {
        if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            Intent p = new Intent(HomeActivity.this, MyPostViewActivity.class);
            p.putExtra("AdId", adId);
            startActivityForResult(p, EDIT_AD_VIEW);
        } else {
            Intent p = new Intent(HomeActivity.this, PostViewActivity.class);
            p.putExtra("AdId", adId);
            startActivityForResult(p, EDIT_AD_VIEW);
        }
    }


    private void Pageination() {

        if (rc_ad_list.getLayoutManager() instanceof LinearLayoutManager) {
            if (!isGrid) {
                listManager = (LinearLayoutManager) rc_ad_list
                        .getLayoutManager();
            } else {
                gridManager = (GridLayoutManager) rc_ad_list
                        .getLayoutManager();
            }
        }
        rc_ad_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (!isGrid) {
                    totalItemCount = listManager.getItemCount();
                    lastVisibleItem = listManager
                            .findLastVisibleItemPosition();
                } else {
                    totalItemCount = gridManager.getItemCount();
                    lastVisibleItem = gridManager
                            .findLastVisibleItemPosition();
                }
                if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    if (currentPageNo != lastPageNo) {
                        if (!isLoading) {
                            currentPageNo = currentPageNo + 1;

                            getAdList(true);

                        }
                    } else {
                        Log.i("List End", "Success");
                    }
                    Log.i("List Bottom", "Success");
                }
            }
        });

    }

    private void getAdList(boolean isLoad) {
        isLoading = true;
        if (!params.isEmpty()) {
            params.clear();
        }
        HashMap<String, Object> paramsFilter = new HashMap<String, Object>();
        JsonArray rentalDuration = new JsonArray();
        JsonArray adType = new JsonArray();
        JsonArray fee = new JsonArray();
        JsonArray locality = new JsonArray();
        JsonArray attribute = new JsonArray();


        for (int i = 0; i < filterList.size(); i++) {

            if (filterList.get(i).getFilterId().equals("1")) {
                for (int j = 0; j < filterList.get(i).getFilterValue().size(); j++) {
                    if (filterList.get(i).getFilterValue().get(j).isSelect()) {
                        rentalDuration.add(filterList.get(i).getFilterValue().get(j).getSortValue());
                    }
                }
            }

            if (filterList.get(i).getFilterId().equals("2")) {
                for (int j = 0; j < filterList.get(i).getFilterValue().size(); j++) {
                    if (filterList.get(i).getFilterValue().get(j).isSelect()) {
                        adType.add(filterList.get(i).getFilterValue().get(j).getSortId());
                    }
                }
            }

            if (filterList.get(i).getFilterId().equals("3")) {
                for (int j = 0; j < filterList.get(i).getFilterValue().size(); j++) {
                    if (filterList.get(i).getFilterValue().get(j).isSelect()) {
                        fee.add(filterList.get(i).getFilterValue().get(j).getSortId());
                    }
                }
            }
            if (filterList.get(i).getFilterId().equals("4")) {
                for (int j = 0; j < filterList.get(i).getFilterValue().size(); j++) {
                    if (filterList.get(i).getFilterValue().get(j).isSelect()) {
                        locality.add(filterList.get(i).getFilterValue().get(j).getSortId());
                    }
                }
            }
            if (filterList.get(i).getFilterId().equals("6")) {
                for (int j = 0; j < filterList.get(i).getFilterValue().size(); j++) {
                    if (filterList.get(i).getFilterValue().get(j).isSelect()) {
                        attribute.add(filterList.get(i).getFilterValue().get(j).getSortId());
                    }
                }
            }
        }

        paramsFilter.put("category_id", catId);
        paramsFilter.put("subcategory_id", filterSubCatId);
        paramsFilter.put("rental_duration", rentalDuration);
        paramsFilter.put("ad_type", adType);
        paramsFilter.put("fee", fee);
        paramsFilter.put("attribute_value", attribute);
        paramsFilter.put("sort_by", sortById);
        paramsFilter.put("locality_id", locality);
        paramsFilter.put("fee_min", minValue);
        paramsFilter.put("fee_max", maxValue);
        paramsFilter.put("category_type", productType);
        paramsFilter.put("city_id", Sessions.getSession(Constant.CityId, getApplicationContext()));
        params.put("filter", paramsFilter);
        params.put("searchTerm", "");

        params.put("page", String.valueOf(currentPageNo));


        Log.i("Params", params.toString());

        if (_fun.isInternetAvailable(HomeActivity.this)) {

            RetrofitClient.getClient().create(Api.class).getHomeAdsNew(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, adListResponse, isLoad, false));
        } else {
            _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getHomeAdsNew(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(HomeActivity.this, adListResponse, isLoad, false));
                }
            });
        }
    }

    retrofit2.Callback<HomeBannerModel> homeBannerResponse = new retrofit2.Callback<HomeBannerModel>() {
        @Override
        public void onResponse(Call<HomeBannerModel> call, retrofit2.Response<HomeBannerModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            HomeBannerModel dr = response.body();
            try {
//                if (layout_swipe_refresh.isRefreshing()) {
//                    layout_swipe_refresh.setRefreshing(false);
//                }
                if (response.isSuccessful()) {
                    layout_home.setVisibility(View.GONE);
                    bannerList.clear();
                    bannerList.addAll(dr.getBannerList());

                    homeBannerAdapter.notifyDataSetChanged();

                    if (!isBannerStarted) {
                        isBannerStarted = true;
                        rc_banner.start(6, TimeUnit.SECONDS);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            getHomeDetails(false);
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            call.cancel();
//            if (layout_swipe_refresh.isRefreshing()) {
//                layout_swipe_refresh.setRefreshing(false);
//            }
            getHomeDetails(false);
        }
    };

    retrofit2.Callback<HomeDetailsModel> homeDetailsResponse = new retrofit2.Callback<HomeDetailsModel>() {
        @Override
        public void onResponse(Call<HomeDetailsModel> call, retrofit2.Response<HomeDetailsModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            HomeDetailsModel dr = response.body();
            try {
                if (layout_swipe_refresh.isRefreshing()) {
                    layout_swipe_refresh.setRefreshing(false);
                }
                if (response.isSuccessful()) {

                    layout_home.setVisibility(View.VISIBLE);
                    serviceList.clear();
                    serviceList.addAll(dr.getHomeDetailsModel().getServiceList());
                    categoryList.clear();
                    categoryList.addAll(dr.getHomeDetailsModel().getProductList());
                    cityList.clear();
                    CityItemModel cityItemModel = new CityItemModel("", "All India", "", false);
                    cityList.add(cityItemModel);
                    cityList.addAll(dr.getHomeDetailsModel().getCityList());

                    productCategoryListAdapter.notifyDataSetChanged();
                    serviceCategoryListAdapter.notifyDataSetChanged();
                    if (isRegister) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ShowPopupProsper();
                            }
                        }, 200);
                    }
                    getHomeAdsList(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                getHomeAdsList(false);
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            call.cancel();
            if (layout_swipe_refresh.isRefreshing()) {
                layout_swipe_refresh.setRefreshing(false);
            }
            getHomeAdsList(false);
        }
    };

    retrofit2.Callback<HomeFlyersModel> homeFlyersResponse = new retrofit2.Callback<HomeFlyersModel>() {
        @Override
        public void onResponse(Call<HomeFlyersModel> call, retrofit2.Response<HomeFlyersModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            HomeFlyersModel dr = response.body();
            pb_circule.setVisibility(View.GONE);
            try {
                if (layout_swipe_refresh.isRefreshing()) {
                    layout_swipe_refresh.setRefreshing(false);
                }
                if (response.isSuccessful()) {
                    if (!isCategory)
                        layout_home.setVisibility(View.VISIBLE);

                    homeAdList.clear();
                    if (dr.getHomeAdsList() != null)
                        homeAdList.addAll(dr.getHomeAdsList());

                    if (homeAdList.size() == 0) {
//                        layout_no_ad_city.setVisibility(View.VISIBLE);
//                        rc_home_ad_list.setVisibility(View.GONE);
                        BaseActivity.instance.txt_location.setText("All India");
                        cityId = "0";
                        Sessions.saveSession(Constant.CityId, "", getApplicationContext());
                        getHomeBannerDetails(false);
                        ShowPopupNoADCity();
                    } else {
                        layout_no_ad_city.setVisibility(View.GONE);
                        rc_home_ad_list.setVisibility(View.VISIBLE);
                    }
                    homeCustomListAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (layout_swipe_refresh.isRefreshing()) {
                    layout_swipe_refresh.setRefreshing(false);
                }
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            call.cancel();
            pb_circule.setVisibility(View.GONE);
            if (layout_swipe_refresh.isRefreshing()) {
                layout_swipe_refresh.setRefreshing(false);
            }
        }
    };
    retrofit2.Callback<FilterResult> adListResponse = new retrofit2.Callback<FilterResult>() {
        @Override
        public void onResponse(Call<FilterResult> call, retrofit2.Response<FilterResult> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            FilterResult dr = response.body();
            try {
                if (layout_swipe_refresh.isRefreshing()) {
                    layout_swipe_refresh.setRefreshing(false);
                }
                if (dr.getStatus()) {
                    currentPageNo = dr.getCurrentPage();
                    lastPageNo = dr.getLastPage();
                    Log.i("CurrentPage", currentPageNo + "," + lastPageNo);
                    isLoading = false;
                    if (currentPageNo == 1) {
                        adList.clear();
                        if (dr.getValue() != null) {
                            sortByList.clear();
                            sortByList.addAll(dr.getValue());
                            if (sortByList.size() != 0) {
                                layout_sort_by.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    if (isGrid) {
                        homeAdGridAdapter.notifyDataSetChanged();
                    } else {
                        homeAdListAdapter.notifyDataSetChanged();
                    }

                    for (int i = 0; i < dr.getData().size(); i++) {
                        AdItemModel adItemModel = new AdItemModel();
                        adItemModel.setAdId(String.valueOf(dr.getData().get(i).getId()));
                        adItemModel.setCatId(String.valueOf(dr.getData().get(i).getCategoryId()));
                        adItemModel.setAdTitle(dr.getData().get(i).getTitle());
                        adItemModel.setAdDescription(dr.getData().get(i).getDescription());
                        adItemModel.setAdDuration(dr.getData().get(i).getRentalDuration());
                        adItemModel.setAdFee(dr.getData().get(i).getRentalFee());
                        adItemModel.setAdNegotiable(String.valueOf(dr.getData().get(i).getIsRentNegotiable()));
                        if (dr.getData().get(i).getCustomLocality() != null && !dr.getData().get(i).getCustomLocality().equals("")) {
                            adItemModel.setAreaName(dr.getData().get(i).getCustomLocality());
                        } else {
                            if (dr.getData().get(i).getLocality() != null && dr.getData().get(i).getLocality().getArea() != null)
                                adItemModel.setAreaName(dr.getData().get(i).getLocality().getArea());
                        }
                        adItemModel.setCityName(dr.getData().get(i).getCity().getCity());
                        adItemModel.setStateName("");
                        if (dr.getData().get(i).getPhotos() != null && !dr.getData().get(i).getPhotos().equals("")) {
                            if (dr.getData().get(i).getPhotos().contains(",")) {
                                String[] tempPrice = dr.getData().get(i).getPhotos().split(",");
                                adItemModel.setPhotoType(dr.getImagePath() + tempPrice[0]);
                            } else {
                                adItemModel.setPhotoType(dr.getImagePath() + dr.getData().get(i).getPhotos());
                            }
                        }
                        adItemModel.setLikeCount(String.valueOf(dr.getData().get(i).getTotalLike()));
                        if (dr.getData().get(i).getIsFave() != null) {
                            adItemModel.setIsFavorite("1");
                        } else {
                            adItemModel.setIsFavorite("0");
                        }
                        if (dr.getData().get(i).getAdsLike() != null) {
                            adItemModel.setIsLike("1");
                        } else {
                            adItemModel.setIsLike("0");
                        }
                        adItemModel.setUserId(String.valueOf(dr.getData().get(i).getUserId()));
                        adItemModel.setAdType(String.valueOf(dr.getData().get(i).getType()));
                        adItemModel.setAdStatus(String.valueOf(dr.getData().get(i).getStatus()));
                        adItemModel.setAdPromotion("");
                        adItemModel.setCatGroup(String.valueOf(dr.getData().get(i).getCategoryType()));
                        adItemModel.setServiceAdCount(dr.getData().get(i).getServiceAdCount());
                        adItemModel.setProsperId(dr.getData().get(i).getUser().getProsperId());
                        adItemModel.setAdParentId(String.valueOf(dr.getData().get(i).getParentId()));
                        adItemModel.setAdSlug(dr.getData().get(i).getSlug());
                        adItemModel.setProgress(false);
                        adItemModel.setImagePath(dr.getImagePath());

                        adList.add(adItemModel);
                    }
//                    adList.addAll(dr.getAdListModel().getAdList());
//                    if (!isFilterClear) {
//                        sortByList.clear();
//                        sortByList.addAll(dr.getSortByModel());
//
//                        filterList.clear();
//                        filterList.addAll(dr.getFilterModel());
//                    }
                    if (isGrid) {
                        homeAdGridAdapter.notifyDataSetChanged();
                    } else {
                        homeAdListAdapter.notifyDataSetChanged();
                    }
                    if (currentPageNo == 1 && adList.size() != 0 && !isViewBack) {

                        rc_ad_list.smoothScrollToPosition(0);

                    }
                    if (isViewBack) {
                        isViewBack = false;
                    }
                    layout_search_list.setVisibility(View.VISIBLE);
                    layout_home.setVisibility(View.GONE);
                    if (adList.size() == 0) {
                        rc_ad_list.setVisibility(View.GONE);
                        layout_requirement_ad.setVisibility(View.VISIBLE);
                        layout_filter_root.setVisibility(View.VISIBLE);
                    } else {
                        rc_ad_list.setVisibility(View.VISIBLE);
                        layout_requirement_ad.setVisibility(View.GONE);
                        layout_filter_root.setVisibility(View.VISIBLE);
                    }

                } else {
                    Function.CustomMessage(HomeActivity.this, dr.getMessage());
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
            if (layout_swipe_refresh.isRefreshing()) {
                layout_swipe_refresh.setRefreshing(false);
            }
        }
    };
    retrofit2.Callback<DefaultResponse> addFavorite = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    if (adList.get(favPosition).getIsFavorite().equals("0")) {
                        adList.get(favPosition).setIsFavorite("1");
                    } else {
                        adList.get(favPosition).setIsFavorite("0");
                    }

                } else {
                    Function.CustomMessage(HomeActivity.this, dr.getMessage());
                }
                adList.get(favPosition).setProgress(false);
//                if (isGrid) {
//                    homeAdGridAdapter.notifyItemChanged(favPosition);
//                } else {
//                    homeAdListAdapter.notifyItemChanged(favPosition);
//                }
                Function.CustomMessage(HomeActivity.this, dr.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                adList.get(favPosition).setProgress(false);
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {

            Log.d("TAG", t.getMessage());
            call.cancel();
            adList.get(favPosition).setProgress(false);
        }
    };

    retrofit2.Callback<AppVersionModel> checkApp = new retrofit2.Callback<AppVersionModel>() {
        @Override
        public void onResponse(Call<AppVersionModel> call, retrofit2.Response<AppVersionModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                AppVersionModel dr = response.body();
                if (dr.isStatus()) {
                    if (dr.getAppVersionDataModel().isForced()) {
                        ShowPopupAppVersion(dr.getAppVersionDataModel().getUpdateMessage(), 1);
                    } else if (dr.getAppVersionDataModel().isUpdate()) {
                        ShowPopupAppVersion(dr.getAppVersionDataModel().getUpdateMessage(), 2);
                    }

                } else {
//                    Function.CustomMessage(HomeActivity.this, dr.getMessage());
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

    retrofit2.Callback<DefaultResponse> deviceDetailsResponse = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
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

    retrofit2.Callback<DefaultResponse> requestFlyerResponse = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
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

    retrofit2.Callback<FilterModel> filterResponse = new retrofit2.Callback<FilterModel>() {
        @Override
        public void onResponse(Call<FilterModel> call, retrofit2.Response<FilterModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                FilterModel dr = response.body();
//                if (dr.isStatus()) {


//                } else {
//                    Function.CustomMessage(HomeActivity.this, dr.getMessage());
//                }
                if (!isFilterSubCat) {

                    filterList.clear();
                    minValue = "";
                    maxValue = "";
                    filterSelectCount = 0;
                    if (dr.getData().getRentalDuration() != null) {
                        ArrayList<SortByModel> _rentalDuration = new ArrayList<>();

                        for (int r = 0; r < dr.getData().getRentalDuration().getValue().size(); r++) {
                            _rentalDuration.add(new SortByModel(String.valueOf(r), dr.getData().getRentalDuration().getValue().get(r), false));
                        }
                        filterList.add(new FiltersModel("1", dr.getData().getRentalDuration().getTitle(), _rentalDuration, "Any", false, true));
                    }

                    if (dr.getData().getAdType() != null) {
                        ArrayList<SortByModel> _adType = new ArrayList<>();
                        for (int r = 0; r < dr.getData().getAdType().getValue().size(); r++) {
                            _adType.add(new SortByModel(String.valueOf(dr.getData().getAdType().getValue().get(r).getId()), dr.getData().getAdType().getValue().get(r).getValue(), false));
                        }
                        filterList.add(new FiltersModel("2", dr.getData().getAdType().getTitle(), _adType, "Any", false, false));
                    }
                    if (dr.getData().getFee() != null) {
                        ArrayList<SortByModel> _feeType = new ArrayList<>();
                        for (int r = 0; r < dr.getData().getFee().getValue().size(); r++) {
                            _feeType.add(new SortByModel(String.valueOf(dr.getData().getFee().getValue().get(r).getId()), dr.getData().getFee().getValue().get(r).getValue(), false));
                        }
                        filterList.add(new FiltersModel("3", dr.getData().getFee().getTitle(), _feeType, "Any", false, false));

                    }
                    if (dr.getData().getLocalityList() != null) {
                        ArrayList<SortByModel> _locality = new ArrayList<>();
                        for (int r = 0; r < dr.getData().getLocalityList().getValue().size(); r++) {
                            _locality.add(new SortByModel(String.valueOf(dr.getData().getLocalityList().getValue().get(r).getId()), dr.getData().getLocalityList().getValue().get(r).getArea(), false));
                        }
                        filterList.add(new FiltersModel("4", dr.getData().getLocalityList().getTitle(), _locality, "Any", true, false));
                    }
                    if (dr.getData().getSubcategoryFilterOption() != null) {
                        ArrayList<SortByModel> _category = new ArrayList<>();
                        for (int r = 0; r < dr.getData().getSubcategoryFilterOption().getSubCategory().size(); r++) {
                            _category.add(new SortByModel(String.valueOf(dr.getData().getSubcategoryFilterOption().getSubCategory().get(r).getId()), dr.getData().getSubcategoryFilterOption().getSubCategory().get(r).getName(), false));
                        }
                        filterList.add(new FiltersModel("5", dr.getData().getSubcategoryFilterOption().getAdTitleLabel(), _category, "Any", true, false));
                    }

                    if (dr.getData().getAttributeFilterOption() != null) {
                        for (int o = 0; o < dr.getData().getAttributeFilterOption().size(); o++) {

                            ArrayList<SortByModel> _attribute = new ArrayList<>();
                            for (int r = 0; r < dr.getData().getAttributeFilterOption().get(o).getAttributeValue().size(); r++) {
                                _attribute.add(new SortByModel(String.valueOf(dr.getData().getAttributeFilterOption().get(o).getAttributeValue().get(r).getId()), dr.getData().getAttributeFilterOption().get(o).getAttributeValue().get(r).getValue(), false));
                            }
                            filterList.add(new FiltersModel("6", dr.getData().getAttributeFilterOption().get(o).getName(), _attribute, "Any", true, false));


                        }
                        filterOptionAdapter.notifyDataSetChanged();

                    }

                } else {
                    for (int i = filterList.size() - 1; i >= 0; i--) {
                        if (Integer.valueOf(filterList.get(i).getFilterId()) > 5) {
                            filterList.remove(i);
                        }
                    }

                    if (dr.getData().getAttributeFilterOption() != null) {
                        for (int o = 0; o < dr.getData().getAttributeFilterOption().size(); o++) {

                            ArrayList<SortByModel> _attribute = new ArrayList<>();
                            for (int r = 0; r < dr.getData().getAttributeFilterOption().get(o).getAttributeValue().size(); r++) {
                                _attribute.add(new SortByModel(String.valueOf(dr.getData().getAttributeFilterOption().get(o).getAttributeValue().get(r).getId()), dr.getData().getAttributeFilterOption().get(o).getAttributeValue().get(r).getValue(), false));
                            }
                            filterList.add(new FiltersModel("6", dr.getData().getAttributeFilterOption().get(o).getName(), _attribute, "Any", true, false));


                        }
                        filterOptionAdapter.notifyDataSetChanged();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                layout_search_list.setVisibility(View.GONE);
                layout_home.setVisibility(View.VISIBLE);
                isCategory = false;
                edt_search.setText("");
                Constant.ParentId = "";
                if (isRefresh) {
                    layout_no_ad_city.setVisibility(View.GONE);
                    isRefresh = false;
                    if (_fun.isInternetAvailable(HomeActivity.this)) {
                        getHomeBannerDetails(false);
                    } else {

                        _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                getHomeBannerDetails(false);
                            }
                        });


                    }
                }
                break;
            case R.id.img_list:
                if (isGrid) {
                    rc_ad_list.setLayoutManager(listManager);
                    rc_ad_list.setAdapter(homeAdListAdapter);
                    img_list.setColorFilter(ContextCompat.getColor(this, R.color.txt_orange));
                    img_grid.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
                    isGrid = false;
                }
                break;
            case R.id.img_grid:
                if (!isGrid) {
                    rc_ad_list.setLayoutManager(gridManager);
                    rc_ad_list.setAdapter(homeAdGridAdapter);
                    img_list.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
                    img_grid.setColorFilter(ContextCompat.getColor(this, R.color.txt_orange));
                    isGrid = true;
                }
                break;
            case R.id.layout_sort_by:
                ShowPopupSortBy(1);
                break;
            case R.id.layout_filter:
                ShowPopupSortBy(2);
                break;
            case R.id.btn_need_req:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 2);
                    p.putExtra("ParId", catId);
                    startActivity(p);
                } else {
                    Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(l, LOGIN_VIEW);
                }
                break;
            case R.id.btn_offer:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    p.putExtra("ParId", "");
                    startActivity(p);
                } else {
                    Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(l, LOGIN_VIEW);
                }
                break;
            case R.id.btn_offer_req:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    p.putExtra("ParId", catId);
                    startActivity(p);
                } else {
                    Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(l, LOGIN_VIEW);
                }
                break;
            case R.id.btn_need:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 2);
                    p.putExtra("ParId", "");
                    startActivity(p);
                } else {
                    Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(l, LOGIN_VIEW);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {

        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isLocationVisible) {
//            layout_list = findViewById(R.id.layout_list);
            layout_list.setVisibility(View.GONE);
            isLocationVisible = false;
        } else {
            if (isCategory) {
                layout_search_list.setVisibility(View.GONE);
                layout_home.setVisibility(View.VISIBLE);
                isCategory = false;
                edt_search.setText("");
                Constant.ParentId = "";
                if (isRefresh) {
                    layout_no_ad_city.setVisibility(View.GONE);
                    isRefresh = false;
                    if (_fun.isInternetAvailable(HomeActivity.this)) {
                        getHomeBannerDetails(false);
                    } else {

                        _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                getHomeBannerDetails(false);
                            }
                        });


                    }
                }
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Function.CustomMessage(HomeActivity.this, getString(R.string.click_again));
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    public void ShowPopupSortBy(final int type) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_fillter_popup, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        LinearLayout img_popup_back = view.findViewById(R.id.img_back);
        TextView txt_popup_title = view.findViewById(R.id.txt_page_title);
        TextView txt_filter_option_title = view.findViewById(R.id.txt_filter_option_title);
        final TextView txt_page_action = view.findViewById(R.id.txt_page_action);
        final RecyclerView rc_filter_option = view.findViewById(R.id.rc_filter_option);
        final TextView txt_clear = view.findViewById(R.id.txt_clear);
        TextView txt_rent_hire_title = view.findViewById(R.id.txt_rent_hire_title);
        EditText edt_search_suggestion = view.findViewById(R.id.edt_search_suggestion);
        EditText edt_min = view.findViewById(R.id.edt_min);
        EditText edt_max = view.findViewById(R.id.edt_max);
        Button btn_reset = view.findViewById(R.id.btn_reset);
        LinearLayout layout_rent_hire = view.findViewById(R.id.layout_rent_hire);
        LinearLayout layout_min_max = view.findViewById(R.id.layout_min_max);

        RecyclerView rc_filter = view.findViewById(R.id.rc_filter);
        txt_page_action.setText(getString(R.string.txt_apply));
        txt_page_action.setEnabled(false);
        txt_page_action.setTextColor(getResources().getColor(R.color.disabled_color));
        txt_clear.setEnabled(false);
        txt_clear.setTextColor(getResources().getColor(R.color.disabled_color));
        txt_clear.setText(getString(R.string.txt_clear));
        LinearLayoutManager listManager = new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, false);
        rc_filter_option.setLayoutManager(listManager);
        rc_filter_option.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager listManager2 = new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, false);
        rc_filter.setLayoutManager(listManager2);
        rc_filter.setItemAnimator(new DefaultItemAnimator());
        rc_filter.setAdapter(filterSelectAdapter);
        edt_max.setText(maxValue);
        edt_min.setText(minValue);

        if (filterSelectCount == 0 && minValue.equals("") && maxValue.equals("")) {
            txt_page_action.setEnabled(false);
            txt_page_action.setTextColor(getResources().getColor(R.color.disabled_color));
            txt_clear.setEnabled(false);
            txt_clear.setTextColor(getResources().getColor(R.color.disabled_color));
        } else {
            txt_page_action.setEnabled(true);
            txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
            txt_clear.setEnabled(true);
            txt_clear.setTextColor(getResources().getColor(R.color.color_white));
        }
//        SortByAdapter sortByAdapter = new SortByAdapter(sortByList, getApplicationContext());
        if (type == 1) {
            rc_filter_option.setAdapter(sortByAdapter);
            txt_popup_title.setText(getString(R.string.txt_sort_by));
            for (int s = 0; s < sortByList.size(); s++) {
                if (sortByList.get(s).isSelect()) {
                    txt_page_action.setEnabled(true);
                    txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
                    txt_clear.setEnabled(true);
                    txt_clear.setTextColor(getResources().getColor(R.color.color_white));
                    break;
                }
            }
        } else if (type == 2) {
            rc_filter_option.setAdapter(filterOptionAdapter);
            txt_popup_title.setText(getString(R.string.txt_filter));
            for (int k = 0; k < filterList.size(); k++) {
                if (!filterList.get(k).getSelectedValue().equals("Any")) {
                    txt_page_action.setEnabled(true);
                    txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
                    txt_clear.setEnabled(true);
                    txt_clear.setTextColor(getResources().getColor(R.color.color_white));
                    break;
                }
            }
        }
        for (int i = 0; i < filterList.size(); i++) {
            if (i == 0) {
                filterList.get(i).setSelect(true);
            } else {
                filterList.get(i).setSelect(false);
            }
        }
        filterOptionAdapter.notifyDataSetChanged();
        if (filterProSer == 1) {
            txt_rent_hire_title.setText(getString(R.string.txt_rent_fee_range));
            txt_filter_option_title.setText("Enter " + getString(R.string.txt_rent_fee_range));
        } else if (filterProSer == 2) {
            txt_rent_hire_title.setText(getString(R.string.txt_hire_fee_range));
            txt_filter_option_title.setText("Enter " + getString(R.string.txt_hire_fee_range));
        } else {
            txt_rent_hire_title.setText(getString(R.string.txt_rent_hire));
            txt_filter_option_title.setText("Enter " + getString(R.string.txt_rent_hire));
        }
        filterSelectList.clear();
        filterSelectList.addAll(filterList.get(0).getFilterValue());
        filterSelectAdapter.notifyDataSetChanged();
        layout_rent_hire.setBackgroundColor(getResources().getColor(R.color.gst_bg));
        txt_rent_hire_title.setTextColor(getResources().getColor(R.color.color_black_five));
        layout_min_max.setVisibility(View.GONE);
        rc_filter.setVisibility(View.VISIBLE);
        txt_filter_option_title.setText(getString(R.string.txt_choose) + " " + filterList.get(0).getFilterTitle());
        SetFilterOptions(0, txt_page_action, txt_clear, filterList.get(0).isSearch(), edt_search_suggestion);
        img_popup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerPopup.isShowing()) {
                    spinnerPopup.dismiss();
                }
            }
        });
        sortByAdapter.setCallback(new SortByAdapter.Callback() {
            @Override
            public void itemClick(SortByModel model) {
                sortById = model.getSortId();
                txt_page_action.setEnabled(true);
                txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
                txt_clear.setEnabled(true);
                txt_clear.setTextColor(getResources().getColor(R.color.color_white));
//                if (spinnerPopup.isShowing()) {
//                    spinnerPopup.dismiss();
//                }
            }
        });

        filterOptionAdapter.setCallback(new FilterOptionAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                filterSelectList.clear();
                filterSelectList.addAll(filterList.get(pos).getFilterValue());
                for (int i = 0; i < filterList.size(); i++) {
                    if (i == pos) {
                        filterList.get(i).setSelect(true);
                    } else {
                        filterList.get(i).setSelect(false);
                    }
                }
                layout_rent_hire.setBackgroundColor(getResources().getColor(R.color.gst_bg));
                txt_rent_hire_title.setTextColor(getResources().getColor(R.color.color_black_five));
                filterOptionAdapter.notifyDataSetChanged();
                txt_filter_option_title.setText(getString(R.string.txt_choose) + " " + filterList.get(pos).getFilterTitle());
                layout_min_max.setVisibility(View.GONE);
                rc_filter.setVisibility(View.VISIBLE);
                Function.hideSoftKeyboard(HomeActivity.this, layout_min_max);
                filterSelectAdapter.getFilter().filter("");
                edt_search_suggestion.setText("");
//                ShowPopupFilter(pos, txt_page_action, txt_clear, filterList.get(pos).isSearch());
                SetFilterOptions(pos, txt_page_action, txt_clear, filterList.get(pos).isSearch(), edt_search_suggestion);
            }
        });
        txt_page_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!maxValue.equals("") && !minValue.equals("") && Integer.valueOf(maxValue) < Integer.valueOf(minValue)) {
                    Function.CustomMessage(HomeActivity.this, getString(R.string.min_max_valitaion_msg));
                } else {
                    if (spinnerPopup.isShowing()) {
                        spinnerPopup.dismiss();
                    }
                    filterSelectAdapter.getFilter().filter("");
                    edt_search_suggestion.setText("");
                    isFilterClear = true;
                    currentPageNo = 1;
                    getAdList(true);
                }

//                if (type == 1) {
//                    getAdList(catId, "", true);
//                } else if (type == 2) {
//                }

            }
        });
        txt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
//                    rc_filter.setAdapter(sortByAdapter);
                    sortByAdapter.clearValues();
                    sortById = "";
                } else if (type == 2) {
                    for (int i = 0; i < filterList.size(); i++) {
                        filterList.get(i).setSelectedValue("Any");
                        for (int j = 0; j < filterList.get(i).getFilterValue().size(); j++) {
                            if (filterList.get(i).getFilterValue().get(j).isSelect()) {
                                filterList.get(i).getFilterValue().get(j).setSelect(false);
                            }
                        }
                    }
                    for (int i = filterList.size() - 1; i >= 0; i--) {
                        if (Integer.valueOf(filterList.get(i).getFilterId()) > 5) {
                            filterList.remove(i);
                        }
                    }
                    filterOptionAdapter.notifyDataSetChanged();
                    filterSubCatId = "";
                }
                txt_page_action.setEnabled(false);
                txt_page_action.setTextColor(getResources().getColor(R.color.disabled_color));
                txt_clear.setEnabled(false);
                txt_clear.setTextColor(getResources().getColor(R.color.disabled_color));
                minValue = "";
                maxValue = "";
                btn_reset.performClick();
                filterSelectAdapter.notifyDataSetChanged();
                isFilterClear = true;
                currentPageNo = 1;
                filterSelectCount = 0;
                getAdList(false);

            }

        });
        layout_rent_hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < filterList.size(); i++) {
                    filterList.get(i).setSelect(false);
                }
                filterOptionAdapter.notifyDataSetChanged();
                if (filterProSer == 1) {
                    txt_filter_option_title.setText("Enter " + getString(R.string.txt_rent_fee_range));
                } else if (filterProSer == 2) {
                    txt_filter_option_title.setText("Enter " + getString(R.string.txt_hire_fee_range));
                } else {
                    txt_filter_option_title.setText("Enter " + getString(R.string.txt_rent_hire));
                }
                layout_rent_hire.setBackgroundColor(getResources().getColor(R.color.color_white));
                txt_rent_hire_title.setTextColor(getResources().getColor(R.color.txt_orange));
                layout_min_max.setVisibility(View.VISIBLE);
                rc_filter.setVisibility(View.GONE);
                edt_search_suggestion.setVisibility(View.GONE);
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_max.setText("");
                edt_min.setText("");
            }
        });
        edt_min.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                minValue = edt_min.getText().toString().trim();
                if (filterSelectCount == 0 && minValue.equals("") && maxValue.equals("")) {
                    txt_page_action.setEnabled(false);
                    txt_page_action.setTextColor(getResources().getColor(R.color.disabled_color));
                    txt_clear.setEnabled(false);
                    txt_clear.setTextColor(getResources().getColor(R.color.disabled_color));
                } else {
                    txt_page_action.setEnabled(true);
                    txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
                    txt_clear.setEnabled(true);
                    txt_clear.setTextColor(getResources().getColor(R.color.color_white));
                }
            }
        });
        edt_max.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                maxValue = edt_max.getText().toString().trim();
                if (filterSelectCount == 0 && minValue.equals("") && maxValue.equals("")) {
                    txt_page_action.setEnabled(false);
                    txt_page_action.setTextColor(getResources().getColor(R.color.disabled_color));
                    txt_clear.setEnabled(false);
                    txt_clear.setTextColor(getResources().getColor(R.color.disabled_color));
                } else {
                    txt_page_action.setEnabled(true);
                    txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
                    txt_clear.setEnabled(true);
                    txt_clear.setTextColor(getResources().getColor(R.color.color_white));
                }
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void SetFilterOptions(final int pos, final TextView _txt_page_action, final TextView _txt_clear, boolean isSearch, EditText edt_search_suggestion) {
        if (filterList.get(pos).getFilterId().equals("5")) {
            filterSelectAdapter.isCategorySelect(true);
        } else {
            filterSelectAdapter.isCategorySelect(false);
        }
        filterSelectAdapter.notifyDataSetChanged();
        filterSelectAdapter.setCallback(new FilterSelectAdapter.Callback() {
            @Override
            public void itemClick(SortByModel model) {
                for (int i = 0; i < filterList.get(pos).getFilterValue().size(); i++) {
                    if (filterList.get(pos).getFilterValue().get(i).getSortId().equals(model.getSortId())) {
                        filterList.get(pos).getFilterValue().get(i).setSelect(true);
                    } else {
                        filterList.get(pos).getFilterValue().get(i).setSelect(false);
                    }
                }
                filterSelectAdapter.notifyDataSetChanged();
                isFilterSubCat = true;
                filterSubCatId = model.getSortId();
                filterList.get(pos).setSelectedValue(model.getSortValue());
                filterOptionAdapter.notifyDataSetChanged();

                getFilterData(true);
                _txt_page_action.setEnabled(true);
                _txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
                _txt_clear.setEnabled(true);
                _txt_clear.setTextColor(getResources().getColor(R.color.color_white));
            }

            @Override
            public void itemClickEnableButton(boolean _isSelect) {
                if (_isSelect) {
                    filterSelectCount++;
                } else {
                    filterSelectCount--;
                }
                if (filterSelectCount == 0 && minValue.equals("") && maxValue.equals("")) {
                    _txt_page_action.setEnabled(false);
                    _txt_page_action.setTextColor(getResources().getColor(R.color.disabled_color));
                    _txt_clear.setEnabled(false);
                    _txt_clear.setTextColor(getResources().getColor(R.color.disabled_color));
                } else {
                    _txt_page_action.setEnabled(true);
                    _txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
                    _txt_clear.setEnabled(true);
                    _txt_clear.setTextColor(getResources().getColor(R.color.color_white));
                }

            }
        });
        edt_search_suggestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterSelectAdapter.getFilter().filter(edt_search_suggestion.getText().toString());
            }
        });

        if (isSearch) {
            edt_search_suggestion.setVisibility(View.VISIBLE);
        } else {
            edt_search_suggestion.setVisibility(View.GONE);
        }
    }

    public void ShowPopupFilter(final int pos, final TextView _txt_page_action, final TextView _txt_clear, boolean isSearch) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_fillter_popup, null);
        filterPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        filterPopup.setOutsideTouchable(true);
        filterPopup.setFocusable(true);
        filterPopup.update();
        LinearLayout img_popup_back = view.findViewById(R.id.img_back);
        TextView txt_popup_title = view.findViewById(R.id.txt_page_title);
        TextView txt_page_action = view.findViewById(R.id.txt_page_action);
        EditText edt_search_suggestion = view.findViewById(R.id.edt_search_suggestion);

        RecyclerView rc_filter = view.findViewById(R.id.rc_filter);
        txt_page_action.setText(getString(R.string.txt_done));

        LinearLayoutManager listManager = new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, false);
        rc_filter.setLayoutManager(listManager);
        rc_filter.setItemAnimator(new DefaultItemAnimator());
        rc_filter.setAdapter(filterSelectAdapter);
        txt_page_action.setVisibility(View.VISIBLE);
        txt_popup_title.setText(getString(R.string.txt_filter));
        if (filterList.get(pos).getFilterId().equals("5")) {
            filterSelectAdapter.isCategorySelect(true);
            txt_page_action.setVisibility(View.GONE);
        } else {
            filterSelectAdapter.isCategorySelect(false);
            txt_page_action.setVisibility(View.VISIBLE);
        }
        filterSelectAdapter.setCallback(new FilterSelectAdapter.Callback() {
            @Override
            public void itemClick(SortByModel model) {
                isFilterSubCat = true;
                filterSubCatId = model.getSortId();
                if (filterPopup.isShowing()) {
                    filterPopup.dismiss();
                }
                filterList.get(pos).setSelectedValue(model.getSortValue());
                filterOptionAdapter.notifyDataSetChanged();
                filterSelectAdapter.getFilter().filter("");
                getFilterData(true);
                _txt_page_action.setEnabled(true);
                _txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
                _txt_clear.setEnabled(true);
                _txt_clear.setTextColor(getResources().getColor(R.color.color_white));
            }

            @Override
            public void itemClickEnableButton(boolean isSelect) {

            }
        });
        edt_search_suggestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterSelectAdapter.getFilter().filter(edt_search_suggestion.getText().toString());
            }
        });
        if (isSearch) {
            edt_search_suggestion.setVisibility(View.VISIBLE);
        } else {
            edt_search_suggestion.setVisibility(View.GONE);
        }
        img_popup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterPopup.isShowing()) {
                    filterPopup.dismiss();
                }
            }
        });

        txt_page_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Function.hideSoftKeyboard(HomeActivity.this, v);
                String selVal = "";
                filterList.get(pos).getFilterValue().clear();
                filterList.get(pos).getFilterValue().addAll(filterSelectList);
                for (int i = 0; i < filterList.get(pos).getFilterValue().size(); i++) {
                    if (filterList.get(pos).getFilterValue().get(i).isSelect()) {
                        if (selVal.equals("")) {
                            selVal = filterList.get(pos).getFilterValue().get(i).getSortValue();
                        } else {
                            selVal = selVal + "," + filterList.get(pos).getFilterValue().get(i).getSortValue();
                        }
                    }
                }
                if (selVal.equals("")) {
                    selVal = "Any";
                }
                filterList.get(pos).setSelectedValue(selVal);
                filterOptionAdapter.notifyDataSetChanged();
                for (int k = 0; k < filterList.size(); k++) {
                    if (!filterList.get(k).getSelectedValue().equals("Any")) {
                        _txt_page_action.setEnabled(true);
                        _txt_page_action.setTextColor(getResources().getColor(R.color.color_white));
                        _txt_clear.setEnabled(true);
                        _txt_clear.setTextColor(getResources().getColor(R.color.color_white));
                        break;
                    }
                }
                if (filterPopup.isShowing()) {
                    filterPopup.dismiss();
                }
                filterSelectAdapter.getFilter().filter("");

            }
        });

        filterPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    public void ShowPopupProsper() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_popup_home_prosper_id, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        TextView txt_prosperId_popup = view.findViewById(R.id.txt_prosper_id);
        TextView txt_content_one = view.findViewById(R.id.txt_content_one);
        TextView txt_content_two = view.findViewById(R.id.txt_content_two);
        TextView txt_content_three = view.findViewById(R.id.txt_content_three);
        TextView txt_content_four = view.findViewById(R.id.txt_content_four);
        ImageView img_cong_gif = view.findViewById(R.id.img_cong_gif);
        CardView layoutRoot = view.findViewById(R.id.layout_prosper_root);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        Button btn_demo = view.findViewById(R.id.btn_demo);

        Glide.with(HomeActivity.this)
                .load(R.drawable.ic_congrts)
                .placeholder(R.drawable.ic_congrts)
                .into(img_cong_gif);
        txt_prosperId_popup.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
        String contentOne = "<font color=#000000>Your Prosper ID is your website!<br><br>Pass this Prosper ID to your client circle.<br><br>We will show them only your page!!</font>";
        txt_content_one.setText(Html.fromHtml(contentOne));
//
        String contentTwo = "<font color=#F2672E><u>(eg: www.slowr.com/" + Sessions.getSession(Constant.ProsperId, getApplicationContext()) + ")</u></font>";
        contentTwo = contentTwo.replace("\n", "<br>");
        txt_content_two.setText(Html.fromHtml(contentTwo));
//
//        String contentThree = "<font color=#000000>Showcase only </font> <font color=#2B9109>Your offerings</font><font color=#000000> to\nCustomers!!</font> ";
//        contentThree = contentThree.replace("\n", "<br>");
//        txt_content_three.setText(Html.fromHtml(contentThree));
//
//        String contentFour = "<font color=#000000>Prosper ID can be made more\n</font> <font color=#FF7400>Fancier</font><font color=#000000>  too!!!</font> ";
//        contentFour = contentFour.replace("\n", "<br>");
//        txt_content_four.setText(Html.fromHtml(contentFour));

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRegister = false;
                spinnerPopup.dismiss();
            }
        });
        btn_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupProfileDemo();
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

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoPopup.dismiss();
                spinnerPopup.dismiss();
                isRegister = false;
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoPopup.dismiss();
                spinnerPopup.dismiss();
                isRegister = false;
            }
        });

        demoPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onRestart() {
        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.getMenu().getItem(0).setChecked(true);
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_AD_VIEW && clickRefresh) {
                currentPageNo = 1;
                isViewBack = true;
                getAdList(true);
            } else if (requestCode == LOGIN_VIEW) {
                if (requestFlrClick) {
                    requestFlrClick = false;
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    p.putExtra("ParId", "");
                    p.putExtra("RequstType", requestFlyType);
                    startActivity(p);
                    callInsigths(requestFlyType);
                } else {
                    if (isCategory) {
                        currentPageNo = 1;
                        isViewBack = true;
                        isRefresh = true;
                        getAdList(false);

                    } else {
                        if (_fun.isInternetAvailable(HomeActivity.this)) {
                            getHomeBannerDetails(false);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                                        @Override
                                        public void isInternet() {
                                            getHomeBannerDetails(false);
                                        }
                                    });
                                }
                            }, 200);

                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        Function.CoinTone(HomeActivity.this);
        if (isCategory) {
            currentPageNo = 1;
            getAdList(false);

        } else {
            if (_fun.isInternetAvailable(HomeActivity.this)) {
                getHomeBannerDetails(false);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                getHomeBannerDetails(false);
                            }
                        });
                    }
                }, 200);

            }
        }
    }

    public void ShowPopupAppVersion(String titleContent, int i) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_post_success, null);

        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(false);
        spinnerPopup.setFocusable(false);
        spinnerPopup.update();

        TextView popup_content = view.findViewById(R.id.txt_content_one);
        TextView txt_skip = view.findViewById(R.id.txt_skip);
        Button txt_done = view.findViewById(R.id.btn_ok);
        popup_content.setText(titleContent);

        txt_done.setText(getString(R.string.yes_take_me));
        if (i == 1) {
            txt_skip.setVisibility(View.GONE);
        } else {
            txt_skip.setVisibility(View.VISIBLE);
        }
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });

        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                /*try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }*/
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void ShowPopupDefauldBanner() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_post_success, null);

        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(false);
        spinnerPopup.setFocusable(false);
        spinnerPopup.update();

        TextView popup_content = view.findViewById(R.id.txt_content_one);
        TextView txt_skip = view.findViewById(R.id.txt_skip);
        Button txt_done = view.findViewById(R.id.btn_ok);
        String contentOne = "<font color=#000000>Go to  </font> <font color=#FF7400>My Banners" + "</font><font color=#000000> and create your own Banner to place it here.<br><br>Banners can be displayed to any chosen Cities!</font> ";
        popup_content.setText(Html.fromHtml(contentOne));
//        popup_content.setText(getString(R.string.txt_banner_popup_content));

        txt_done.setText(getString(R.string.txt_banner));
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
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent b = new Intent(HomeActivity.this, BannerActivity.class);
                    startActivity(b);
                } else {
                    Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(l, LOGIN_VIEW);
                }
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = false;
        for (int i = 0; i < Constant.LocationPermissions.length; i++) {
            int result1 = ContextCompat.checkSelfPermission(HomeActivity.this, Constant.LocationPermissions[i]);
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        if (result) {
            GetLocationDetails();
        }
    }


    @Override
    public void callCityChange() {
        layout_swipe_refresh.setRefreshing(true);
        if (isCategory) {
            isRefresh = true;
            currentPageNo = 1;
            isFilterClear = false;
            sortByList.clear();
            filterList.clear();
            minValue = "";
            maxValue = "";
            isFilterSubCat = false;
            filterSelectCount = 0;
            sortById = "";
            getFilterData(false);
            getAdList(false);

        } else {
            if (_fun.isInternetAvailable(HomeActivity.this)) {
                getHomeBannerDetails(false);
            } else {

                _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                    @Override
                    public void isInternet() {
                        getHomeBannerDetails(false);
                    }
                });


            }
        }
    }

    public void ShowPopupNoADCity() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_no_ad_cidy, null);

        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(false);
        spinnerPopup.update();
        Button btn_offer = view.findViewById(R.id.btn_offer);
        Button btn_need = view.findViewById(R.id.btn_need);
        LinearLayout layout_delete = view.findViewById(R.id.layout_delete);
        btn_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    p.putExtra("ParId", "");
                    startActivity(p);
                    spinnerPopup.dismiss();
                } else {
                    Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(l, LOGIN_VIEW);
                }
            }
        });
        btn_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 2);
                    p.putExtra("ParId", "");
                    startActivity(p);
                    spinnerPopup.dismiss();
                } else {
                    Intent l = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(l, LOGIN_VIEW);
                }
            }
        });
        layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void ShowPopupNoProsperPage(String prosperId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_no_proper_page, null);

        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(false);
        spinnerPopup.update();
        TextView txt_prosperId_no = view.findViewById(R.id.txt_prosperId_no);
        txt_prosperId_no.setText(prosperId);
        Button btn_home_page = view.findViewById(R.id.btn_home_page);
        btn_home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
