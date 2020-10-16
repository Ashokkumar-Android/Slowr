package com.slowr.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.slowr.app.R;
import com.slowr.app.adapter.BannerAdapter;
import com.slowr.app.adapter.FilterOptionAdapter;
import com.slowr.app.adapter.FilterSelectAdapter;
import com.slowr.app.adapter.HomeAdGridAdapter;
import com.slowr.app.adapter.HomeAdListAdapter;
import com.slowr.app.adapter.HomeCustomListAdapter;
import com.slowr.app.adapter.ProductCategoryListAdapter;
import com.slowr.app.adapter.SearchSuggistionAdapter;
import com.slowr.app.adapter.ServiceCategoryListAdapter;
import com.slowr.app.adapter.SortByAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.AdItemModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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

    RecyclerView rc_product_list;
    RecyclerView rc_service_list;
    RecyclerView rc_ad_list;
    RecyclerView rc_home_ad_list;

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
    boolean isFilterClear = false;
    int EDIT_AD_VIEW = 1266;
    boolean isRegister = false;
    private Function _fun = new Function();

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    boolean isLoading = false;
    boolean isEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_home, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
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
        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        img_back.setOnClickListener(this);
        img_list.setOnClickListener(this);
        img_grid.setOnClickListener(this);
        layout_sort_by.setOnClickListener(this);
        layout_filter.setOnClickListener(this);
        btn_requirement_ad.setOnClickListener(this);
        layout_swipe_refresh.setOnRefreshListener(this);
        CallBackFunction();
//        getCategory();
        setBanner();
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

        if (isRegister) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ShowPopupProsper();
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
    }


    private void getHomeDetails(boolean isLoad) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("cityId", Sessions.getSession(Constant.CityId, HomeActivity.this));
        Log.i("Params", params.toString());
        RetrofitClient.getClient().create(Api.class).getHomeDetails(params)
                .enqueue(new RetrofitCallBack(HomeActivity.this, homeDetailsResponse, isLoad));
    }


    private void CallBackFunction() {

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
                    .enqueue(new RetrofitCallBack(HomeActivity.this, addFavorite, true));
        } else {
            RetrofitClient.getClient().create(Api.class).deleteFavorite(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(HomeActivity.this, addFavorite, true));
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
        bannerAdapter = new BannerAdapter(HomeActivity.this);
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
        }, 3000, 3000);
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
//                    Log.i("ImagePathUrl", dr.getImgPath());
//                    Sessions.saveSession(Constant.ImagePath, dr.getImgPath(), HomeActivity.this);
                    serviceList.clear();
                    serviceList.addAll(dr.getServiceList());
                    categoryList.clear();
                    categoryList.addAll(dr.getProductList());
                    cityList.clear();
                    CityItemModel cityItemModel = new CityItemModel();
                    cityItemModel.setCityId("");
                    cityItemModel.setCityName("All India");
                    cityList.add(cityItemModel);
                    cityList.addAll(dr.getCityList());
                    homeAdList.clear();
                    homeAdList.addAll(dr.getHomeAdsList());
                    productCategoryListAdapter.notifyDataSetChanged();
                    serviceCategoryListAdapter.notifyDataSetChanged();
                    homeCustomListAdapter.notifyDataSetChanged();
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

//                    Log.i("ImagePathUrl", dr.getUrlPath());
//                    Sessions.saveSession(Constant.ImagePath, dr.getUrlPath(), HomeActivity.this);
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
                    layout_search_list.setVisibility(View.VISIBLE);
                    layout_home.setVisibility(View.GONE);
                    isSearch = true;
                    serachView = 1;
                    if (adList.size() == 0) {
                        rc_ad_list.setVisibility(View.GONE);
                        layout_requirement_ad.setVisibility(View.VISIBLE);
                    } else {
                        rc_ad_list.setVisibility(View.VISIBLE);
                        layout_requirement_ad.setVisibility(View.GONE);
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
                if (isGrid) {
                    homeAdGridAdapter.notifyItemChanged(favPosition);
                } else {
                    homeAdListAdapter.notifyItemChanged(favPosition);
                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                layout_search_list.setVisibility(View.GONE);
                layout_home.setVisibility(View.VISIBLE);
                isCategory = false;
                edt_search.setText("");
                break;
            case R.id.img_list:
                if (isGrid) {
                    rc_ad_list.setLayoutManager(listManager);
                    rc_ad_list.setAdapter(homeAdListAdapter);
                    img_list.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
                    img_grid.setColorFilter(ContextCompat.getColor(this, R.color.txt_orange));
                    isGrid = false;
                }
                break;
            case R.id.img_grid:
                if (!isGrid) {
                    rc_ad_list.setLayoutManager(gridManager);
                    rc_ad_list.setAdapter(homeAdGridAdapter);
                    img_list.setColorFilter(ContextCompat.getColor(this, R.color.txt_orange));
                    img_grid.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
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
        Button btn_ok = view.findViewById(R.id.btn_ok);
        txt_prosperId_popup.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
        String contentOne = "<font color=#000000>Type </font> <font color=#FF7400>Slowr.com/" + Sessions.getSession(Constant.ProsperId, getApplicationContext()) + "</font><font color=#000000> on url. </font> ";
        txt_content_one.setText(Html.fromHtml(contentOne));

        String contentTwo = "<font color=#000000>This website will become\nyours </font> <font color=#0F4C81>exclusively!</font>";
        contentTwo = contentTwo.replace("\n", "<br>");
        txt_content_two.setText(Html.fromHtml(contentTwo));

        String contentThree = "<font color=#000000>Showcase only </font> <font color=#2B9109>Your offerings</font><font color=#000000> to\nCustomers!!</font> ";
        contentThree = contentThree.replace("\n", "<br>");
        txt_content_three.setText(Html.fromHtml(contentThree));

        String contentFour = "<font color=#000000>Prosper ID can be made more\n</font> <font color=#FF7400>Fancier</font><font color=#000000>  too!!!</font> ";
        contentFour = contentFour.replace("\n", "<br>");
        txt_content_four.setText(Html.fromHtml(contentFour));

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
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
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
}
