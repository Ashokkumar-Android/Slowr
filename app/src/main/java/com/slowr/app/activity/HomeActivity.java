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
import com.slowr.app.adapter.BannerAdapter;
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
import com.slowr.app.models.FiltersModel;
import com.slowr.app.models.HomeAdsModel;
import com.slowr.app.models.HomeBannerModel;
import com.slowr.app.models.HomeDetailsModel;
import com.slowr.app.models.HomeFilterAdModel;
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
    Button btn_requirement_ad;
    SwipeRefreshLayout layout_swipe_refresh;
    CardView layout_filter_root;
    CardView layout_no_ad_city;
    Button btn_offer;
    Button btn_need;

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

    BannerAdapter bannerAdapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 3;
    boolean isCategory = false;
    boolean isSearch = false;
    boolean isGrid = false;
    int favPosition = 0;
    int currentPageNo = 1;
    int lastPageNo = 1;
    String sortById = "";
    String catId = "";
    String productType = "";
    String shareMessage = "";

    HashMap<String, Object> params = new HashMap<String, Object>();


    View rootView = null;

    GridLayoutManager gridManager;
    LinearLayoutManager listManager;
    boolean doubleBackToExitPressedOnce = false;
    int serachView = 0;
    private PopupWindow spinnerPopup, filterPopup, demoPopup;
    SortByAdapter sortByAdapter;
    FilterOptionAdapter filterOptionAdapter;
    FilterSelectAdapter filterSelectAdapter;
    SearchSuggistionAdapter searchSuggistionAdapter;
    HomeBannerAdapter homeBannerAdapter;
    boolean isFilterClear = false;
    int EDIT_AD_VIEW = 1266;
    boolean isRegister = false;
    private Function _fun = new Function();

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    boolean isLoading = false;
    boolean isRefresh = false;

    boolean isBannerStarted = false;
    boolean isViewBack = false;

    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private static final int REQUEST_CODE = 101;
    String cityName = "";
    BaseActivity homeActivity;
    CallBackCity callBackCity;

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
        btn_requirement_ad = findViewById(R.id.btn_requirement_ad);
        layout_requirement_ad = findViewById(R.id.layout_requirement_ad);
        layout_list_filter = findViewById(R.id.layout_list_filter);
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);
        rc_banner = findViewById(R.id.rc_banner);
        layout_filter_root = findViewById(R.id.layout_filter_root);
        layout_no_ad_city = findViewById(R.id.layout_no_ad_city);
        btn_offer = findViewById(R.id.btn_offer);
        btn_need = findViewById(R.id.btn_need);
        pb_circule = findViewById(R.id.pb_circule);

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


        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        img_back.setOnClickListener(this);
        img_list.setOnClickListener(this);
        img_grid.setOnClickListener(this);
        layout_sort_by.setOnClickListener(this);
        layout_filter.setOnClickListener(this);
        btn_need.setOnClickListener(this);
        btn_offer.setOnClickListener(this);
        btn_requirement_ad.setOnClickListener(this);
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
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            cityName = addresses.get(0).getLocality();
                            String stateName = addresses.get(0).getAdminArea();
                            String countryName = addresses.get(0).getCountryName();

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
                                .enqueue(new RetrofitCallBack(HomeActivity.this, deviceDetailsResponse, false));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    .enqueue(new RetrofitCallBack(HomeActivity.this, checkApp, false));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            RetrofitClient.getClient().create(Api.class).appVersionCheck(params)
                                    .enqueue(new RetrofitCallBack(HomeActivity.this, checkApp, false));
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
                    .enqueue(new RetrofitCallBack(HomeActivity.this, homeBannerResponse, isLoad));
        } else {
            _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getHomeBanners(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(HomeActivity.this, homeBannerResponse, isLoad));
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
                    .enqueue(new RetrofitCallBack(HomeActivity.this, homeFlyersResponse, isLoad));
        } else {
            _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getHomeFlyers(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(HomeActivity.this, homeFlyersResponse, isLoad));
                }
            });
        }

    }

    private void getHomeDetails(boolean isLoad) {


        if (_fun.isInternetAvailable(HomeActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getHomeCategory(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, homeDetailsResponse, isLoad));
        } else {
            _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getHomeCategory(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(HomeActivity.this, homeDetailsResponse, isLoad));
                }
            });
        }
    }

    private void CallBackFunction() {
        homeBannerAdapter.setCallback(new HomeBannerAdapter.Callback() {
            @Override
            public void itemClick(BannerItemModel model) {
                String userProsperId = model.getProsperId();
                if (userProsperId != null) {
                    Intent i = new Intent(HomeActivity.this, UserProfileActivity.class);
                    i.putExtra("prosperId", userProsperId);
                    startActivity(i);
                } else {
                    ShowPopupDefauldBanner();
                }
            }
        });
        homeAdGridAdapter.setCallback(new HomeAdGridAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                String catId = adList.get(pos).getCatId();
                String adId = adList.get(pos).getAdId();
                String userId = adList.get(pos).getUserId();
                changeFragment(catId, adId, userId);
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
                        Function.CustomMessage(HomeActivity.this, getString(R.string.txt_please_login));
                    }


                }
            }

            @Override
            public void onShareClick(int pos) {
                String catId = adList.get(pos).getCatId();
                String adId = adList.get(pos).getAdId();
                String adTitle = adList.get(pos).getAdTitle();
                String catGroup = adList.get(pos).getCatGroup();
                String url = adList.get(pos).getPhotoType();

                Function.ShareLink(HomeActivity.this, catId, adId, adTitle, catGroup, url);
            }
        });

        homeAdListAdapter.setCallback(new HomeAdListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                String catId = adList.get(pos).getCatId();
                String adId = adList.get(pos).getAdId();
                String userId = adList.get(pos).getUserId();
                changeFragment(catId, adId, userId);

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
                    Function.CustomMessage(HomeActivity.this, getString(R.string.txt_please_login));
                }

            }

            @Override
            public void onShareClick(int pos) {
                String catId = adList.get(pos).getCatId();
                String adId = adList.get(pos).getAdId();
                String adTitle = adList.get(pos).getAdTitle();
                String catGroup = adList.get(pos).getCatGroup();
                String url = adList.get(pos).getPhotoType();
                Function.ShareLink(HomeActivity.this, catId, adId, adTitle, catGroup, url);
            }
        });

        homeCustomListAdapter.setCallback(new HomeCustomListAdapter.Callback() {
            @Override
            public void itemClick(AdItemModel model) {
                String catId = model.getCatId();
                String adId = model.getAdId();
                String userId = model.getUserId();
                changeFragment(catId, adId, userId);
            }

            @Override
            public void viewMoreClick(int pos) {
                isCategory = true;
                isFilterClear = false;
                sortByList.clear();
                filterList.clear();
                productType = homeAdList.get(pos).getProductType();
                currentPageNo = 1;
                catId = "";
                sortById = "";
                if (_fun.isInternetAvailable(HomeActivity.this)) {

                    getAdList(true);
                } else {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList(true);
                        }
                    });
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
                productType = "";
                sortById = "";
                if (sortByAdapter != null) {
                    sortByAdapter.clearValues();
                }
                catId = categoryList.get(pos).getId();
                Constant.ParentId = catId;
                currentPageNo = 1;
                if (_fun.isInternetAvailable(HomeActivity.this)) {

                    getAdList(true);
                } else {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList(true);
                        }
                    });
                }

            }
        });
        serviceCategoryListAdapter.setCallback(new ServiceCategoryListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                isCategory = true;
                isFilterClear = false;
                sortByList.clear();
                filterList.clear();
                catId = serviceList.get(pos).getId();
                Constant.ParentId = catId;
                currentPageNo = 1;
                productType = "";
                sortById = "";
                if (sortByAdapter != null) {
                    sortByAdapter.clearValues();
                }
                if (_fun.isInternetAvailable(HomeActivity.this)) {
                    getAdList(true);
                } else {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList(true);
                        }
                    });
                }
            }
        });

    }


    private void callAddFavorite() {
        String catId = adList.get(favPosition).getCatId();
        String adId = adList.get(favPosition).getAdId();
        String isFav = adList.get(favPosition).getIsFavorite();
        if (isFav.equals("0")) {
            if (!params.isEmpty()) {
                params.clear();
            }
            params.put("ads_id", adId);
            params.put("category_id", catId);
            Log.i("Params", params.toString());
            RetrofitClient.getClient().create(Api.class).addFavorite(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, addFavorite, false));
        } else {
            RetrofitClient.getClient().create(Api.class).deleteFavorite(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, addFavorite, false));
        }
    }

    void changeFragment(String catId, String adId, String userId) {
        if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            Intent p = new Intent(HomeActivity.this, MyPostViewActivity.class);
            p.putExtra("CatId", catId);
            p.putExtra("AdId", adId);
            startActivityForResult(p, EDIT_AD_VIEW);
        } else {
            Intent p = new Intent(HomeActivity.this, PostViewActivity.class);
            p.putExtra("CatId", catId);
            p.putExtra("AdId", adId);
            startActivityForResult(p, EDIT_AD_VIEW);
        }
    }


    private void Pageination() {


        //Friends
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

                            if (_fun.isInternetAvailable(HomeActivity.this)) {
                                getAdList(true);
                            } else {
                                _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                                    @Override
                                    public void isInternet() {
                                        getAdList(true);
                                    }
                                });
                            }
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


        for (int i = 0; i < filterList.size(); i++) {
            JsonArray jsonArray = new JsonArray();
            for (int j = 0; j < filterList.get(i).getFilterValue().size(); j++) {
                if (filterList.get(i).getFilterValue().get(j).isSelect()) {
                    jsonArray.add(filterList.get(i).getFilterValue().get(j).getSortId());
                }
            }
            if (jsonArray.size() != 0) {
                paramsFilter.put(filterList.get(i).getFilterId(), jsonArray);
            }
        }

        params.put("categoryId", catId);
        params.put("subCategoryId", "");
        params.put("childCategoryId", "");
        params.put("cityId", Sessions.getSession(Constant.CityId, HomeActivity.this));
        params.put("search", "");
        params.put("page", String.valueOf(currentPageNo));
        params.put("sortBy", sortById);
        params.put("attributeFilterOptions", paramsFilter);
        params.put("productType", productType);
        Log.i("Params", params.toString());

        RetrofitClient.getClient().create(Api.class).getHomeAds(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(HomeActivity.this, adListResponse, isLoad));
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
                        rc_banner.start(3, TimeUnit.SECONDS);
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
                        layout_no_ad_city.setVisibility(View.VISIBLE);
                        rc_home_ad_list.setVisibility(View.GONE);
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
    retrofit2.Callback<HomeFilterAdModel> adListResponse = new retrofit2.Callback<HomeFilterAdModel>() {
        @Override
        public void onResponse(Call<HomeFilterAdModel> call, retrofit2.Response<HomeFilterAdModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            HomeFilterAdModel dr = response.body();
            try {
                if (layout_swipe_refresh.isRefreshing()) {
                    layout_swipe_refresh.setRefreshing(false);
                }
                if (dr.isStatus()) {
                    currentPageNo = dr.getAdListModel().getCurrentPage();
                    lastPageNo = dr.getAdListModel().getLastPage();
                    isLoading = false;
                    if (currentPageNo == 1) {
                        adList.clear();

                    }

                    if (isGrid) {
                        homeAdGridAdapter.notifyDataSetChanged();
                    } else {
                        homeAdListAdapter.notifyDataSetChanged();
                    }
                    adList.addAll(dr.getAdListModel().getAdList());
                    if (!isFilterClear) {
                        sortByList.clear();
                        sortByList.addAll(dr.getSortByModel());

                        filterList.clear();
                        filterList.addAll(dr.getFilterModel());
                    }
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
                    isSearch = true;
                    serachView = 1;
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
            case R.id.btn_requirement_ad:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 2);
                    p.putExtra("ParId", catId);
                    startActivity(p);
                } else {
                    Function.CustomMessage(HomeActivity.this, getString(R.string.txt_please_login));
                }
                break;
            case R.id.btn_offer:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    p.putExtra("ParId", "");
                    startActivity(p);
                } else {
                    Function.CustomMessage(HomeActivity.this, getString(R.string.txt_please_login));
                }
                break;
            case R.id.btn_need:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(HomeActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 2);
                    p.putExtra("ParId", "");
                    startActivity(p);
                } else {
                    Function.CustomMessage(HomeActivity.this, getString(R.string.txt_please_login));
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
        final TextView txt_page_action = view.findViewById(R.id.txt_page_action);
        final RecyclerView rc_filter = view.findViewById(R.id.rc_filter);
        final TextView txt_clear = view.findViewById(R.id.txt_clear);
        txt_page_action.setText(getString(R.string.txt_apply));
        txt_page_action.setEnabled(false);
        txt_page_action.setTextColor(getResources().getColor(R.color.disabled_color));
        txt_clear.setEnabled(false);
        txt_clear.setTextColor(getResources().getColor(R.color.disabled_color));
        txt_clear.setText(getString(R.string.txt_clear));
        LinearLayoutManager listManager = new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, false);
        rc_filter.setLayoutManager(listManager);
        rc_filter.setItemAnimator(new DefaultItemAnimator());
//        SortByAdapter sortByAdapter = new SortByAdapter(sortByList, getApplicationContext());
        if (type == 1) {
            rc_filter.setAdapter(sortByAdapter);
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
            rc_filter.setAdapter(filterOptionAdapter);
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
                ShowPopupFilter(pos, txt_page_action, txt_clear, filterList.get(pos).isSearch());
            }
        });
        txt_page_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerPopup.isShowing()) {
                    spinnerPopup.dismiss();
                }
                isFilterClear = true;
                currentPageNo = 1;
                if (_fun.isInternetAvailable(HomeActivity.this)) {
                    getAdList(true);
                } else {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList(true);
                        }
                    });
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
                    filterOptionAdapter.notifyDataSetChanged();

                }
                txt_page_action.setEnabled(false);
                txt_page_action.setTextColor(getResources().getColor(R.color.disabled_color));
                txt_clear.setEnabled(false);
                txt_clear.setTextColor(getResources().getColor(R.color.disabled_color));
                isFilterClear = true;
                currentPageNo = 1;
                if (_fun.isInternetAvailable(HomeActivity.this)) {
                    getAdList(false);
                } else {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList(false);
                        }
                    });
                }

            }

        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
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
        txt_page_title.setText("Demo Profile View");
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
            if (requestCode == EDIT_AD_VIEW) {
                currentPageNo = 1;
                isViewBack = true;
                if (_fun.isInternetAvailable(HomeActivity.this)) {
                    getAdList(true);
                } else {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList(true);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onRefresh() {

        if (isCategory) {
            currentPageNo = 1;

            if (_fun.isInternetAvailable(HomeActivity.this)) {
                getAdList(false);
            } else {
                _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                    @Override
                    public void isInternet() {
                        getAdList(false);
                    }
                });
            }
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
                    Function.CustomMessage(HomeActivity.this, getString(R.string.txt_please_login));
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
            sortById = "";
            if (_fun.isInternetAvailable(HomeActivity.this)) {
                getAdList(false);
            } else {
                _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                    @Override
                    public void isInternet() {
                        getAdList(false);
                    }
                });
            }
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
}
