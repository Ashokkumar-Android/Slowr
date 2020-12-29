package com.slowr.app.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.tasks.Task;
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
import com.slowr.app.models.HomeDetailsModel;
import com.slowr.app.models.HomeFilterAdModel;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

public class HomeActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
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

    BaseActivity homeActivity;
    View rootView = null;

    GridLayoutManager gridManager;
    LinearLayoutManager listManager;
    boolean doubleBackToExitPressedOnce = false;
    int serachView = 0;
    private PopupWindow spinnerPopup, filterPopup;
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
    boolean isEnd = false;

    boolean isBannerStarted = false;

    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    private static final int REQUEST_CODE = 101;

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
        homeAdListAdapter = new HomeAdListAdapter(adList, HomeActivity.this);
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

//        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(HomeActivity.this, RecyclerView.HORIZONTAL, false);
//        rc_banner.setLayoutManager(linearLayoutManager5);
//        rc_banner.setItemAnimator(new DefaultItemAnimator());
        homeBannerAdapter = new HomeBannerAdapter(bannerList, HomeActivity.this);
        rc_banner.setAdapter(homeBannerAdapter);
//        SnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(rc_banner);


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
            getHomeDetails(true);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getHomeDetails(true);
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        saveUserDetails();
    }

    private void saveUserDetails() {
        GetLocationDetails();
    }

    private void GetLocationDetails() {
        Log.i("User Location", "Step1");
        if (_fun.checkPermissionLocation(HomeActivity.this)) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
            }
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
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
                        String cityName = addresses.get(0).getLocality();
                        String stateName = addresses.get(0).getAdminArea();
                        String countryName = addresses.get(0).getCountryName();
                        if (!params.isEmpty()) {
                            params.clear();
                        }


                        params.put("city", cityName);
                        params.put("longitude", location.getLongitude());
                        params.put("latitude", location.getLatitude());
                        params.put("device_details", "Samsung");
                        params.put("action", "1");

                        RetrofitClient.getClient().create(Api.class).deviceDetails(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                .enqueue(new RetrofitCallBack(HomeActivity.this, deviceDetailsResponse, false));
                    } else {
                        Log.i("User Location", "Step4");
                    }
                }
            });
        }
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


    private void getHomeDetails(boolean isLoad) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("cityId", Sessions.getSession(Constant.CityId, HomeActivity.this));
        Log.i("Params", params.toString());
        RetrofitClient.getClient().create(Api.class).getHomeDetails(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(HomeActivity.this, homeDetailsResponse, isLoad));
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

    private void setBanner() {

        bannerAdapter = new BannerAdapter(HomeActivity.this, bannerList);
        vp_banner.setAdapter(bannerAdapter);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                vp_banner.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 2000);
        vp_banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bannerAdapter.setCallBack(new BannerAdapter.CallBack() {
            @Override
            public void onItemClick(int pos) {
                String userProsperId = bannerList.get(pos).getProsperId();
                if (userProsperId != null) {
                    Intent i = new Intent(HomeActivity.this, UserProfileActivity.class);
                    i.putExtra("prosperId", userProsperId);
                    startActivity(i);
                }
            }
        });
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
                    serviceList.addAll(dr.getServiceList());
                    categoryList.clear();
                    categoryList.addAll(dr.getProductList());
                    cityList.clear();
                    CityItemModel cityItemModel = new CityItemModel("", "All India", "", false);
                    cityList.add(cityItemModel);
                    cityList.addAll(dr.getCityList());
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
                    bannerList.clear();
                    bannerList.addAll(dr.getBannerList());
                    NUM_PAGES = bannerList.size();
                    currentPage = 0;
                    productCategoryListAdapter.notifyDataSetChanged();
                    serviceCategoryListAdapter.notifyDataSetChanged();
                    homeCustomListAdapter.notifyDataSetChanged();
//                    bannerAdapter.notifyDataSetChanged();
                    homeBannerAdapter.notifyDataSetChanged();

                    if (!isBannerStarted) {
                        isBannerStarted = true;
                        rc_banner.start(3, TimeUnit.SECONDS);
                    }

                    if (isRegister) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ShowPopupProsper();
                            }
                        }, 200);
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
                    if (currentPageNo == 1 && adList.size() != 0) {
                        rc_ad_list.smoothScrollToPosition(0);

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
                ShowPopupFilter(pos, txt_page_action, txt_clear);
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

    public void ShowPopupFilter(final int pos, final TextView _txt_page_action, final TextView _txt_clear) {
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

        RecyclerView rc_filter = view.findViewById(R.id.rc_filter);
        txt_page_action.setText(getString(R.string.txt_done));

        LinearLayoutManager listManager = new LinearLayoutManager(HomeActivity.this, RecyclerView.VERTICAL, false);
        rc_filter.setLayoutManager(listManager);
        rc_filter.setItemAnimator(new DefaultItemAnimator());
        rc_filter.setAdapter(filterSelectAdapter);
        txt_page_action.setVisibility(View.VISIBLE);
        txt_popup_title.setText(getString(R.string.txt_filter));


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

        Glide.with(HomeActivity.this)
                .load(R.drawable.ic_congrts)
                .placeholder(R.drawable.ic_congrts)
                .into(img_cong_gif);
        txt_prosperId_popup.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
//        String contentOne = "<font color=#000000>Type </font> <font color=#FF7400>Slowr.com/" + Sessions.getSession(Constant.ProsperId, getApplicationContext()) + "</font><font color=#000000> on url. </font> ";
//        txt_content_one.setText(Html.fromHtml(contentOne));
//
//        String contentTwo = "<font color=#000000>This website will become\nyours </font> <font color=#0F4C81>exclusively!</font>";
//        contentTwo = contentTwo.replace("\n", "<br>");
//        txt_content_two.setText(Html.fromHtml(contentTwo));
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
                spinnerPopup.dismiss();
            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
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
                getHomeDetails(false);
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _fun.ShowNoInternetPopup(HomeActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                getHomeDetails(false);
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
}
