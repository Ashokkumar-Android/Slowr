package com.slowr.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.slowr.app.R;
import com.slowr.app.adapter.FilterOptionAdapter;
import com.slowr.app.adapter.FilterSelectAdapter;
import com.slowr.app.adapter.HomeAdGridAdapter;
import com.slowr.app.adapter.HomeAdListAdapter;
import com.slowr.app.adapter.SearchSuggistionAdapter;
import com.slowr.app.adapter.SortByAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.Fillter.FilterModel;
import com.slowr.app.models.FilterResult.FilterResult;
import com.slowr.app.models.FiltersModel;
import com.slowr.app.models.SearchSuggistonModel;
import com.slowr.app.models.SortByModel;
import com.slowr.app.models.SuggistionItem;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    LinearLayout layout_ad_list;
    ImageView img_list;
    ImageView img_grid;
    LinearLayout img_back;
    LinearLayout layout_sort_by;
    LinearLayout layout_filter;
    LinearLayout layout_requirement_ad;
    LinearLayout layout_list_filter;
    LinearLayout layout_search_list;
    Button btn_need_req;
    TextView txt_page_title;
    EditText edt_search_suggestion;
    SwipeRefreshLayout layout_swipe_refresh;
    ImageView img_search;
    Button btn_add_post_header;
    Button btn_offer_req;

    RecyclerView rc_ad_list;
    RecyclerView rc_filter;
    HomeAdListAdapter homeAdListAdapter;
    HomeAdGridAdapter homeAdGridAdapter;

    ArrayList<AdItemModel> adList = new ArrayList<>();
    ArrayList<SortByModel> sortByList = new ArrayList<>();
    ArrayList<FiltersModel> filterList = new ArrayList<>();
    ArrayList<SortByModel> filterSelectList = new ArrayList<>();
    ArrayList<SuggistionItem> searchSuggestionList = new ArrayList<>();

    boolean isCategory = false;
    boolean isSearch = false;
    boolean isGrid = false;
    int favPosition = 0;
    int currentPageNo = 1;
    int lastPageNo = 1;
    String sortById = "";
    String catId = "";
    String searchCatId = "";
    String searchSubCatId = "";
    String searchChildCatId = "";
    String searchSugeistionValue = "";

    HashMap<String, Object> params = new HashMap<String, Object>();


    GridLayoutManager gridManager;
    LinearLayoutManager listManager;
    int serachView = 0;
    private PopupWindow spinnerPopup, filterPopup;
    SortByAdapter sortByAdapter;
    FilterOptionAdapter filterOptionAdapter;
    FilterSelectAdapter filterSelectAdapter;
    SearchSuggistionAdapter searchSuggistionAdapter;
    boolean isFilterClear = false;
    int POST_VIEW_CODE = 1299;

    boolean isChanges = false;
    private Function _fun = new Function();
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    boolean isLoading = false;
    String shareMessage = "";
    boolean isFilterSubCat = false;

    int filterSelectCount = 0;
    String maxValue = "";
    String minValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        doDeclaration();
    }

    private void doDeclaration() {
        rc_ad_list = findViewById(R.id.rc_ad_list);
        layout_ad_list = findViewById(R.id.layout_ad_list);
        img_list = findViewById(R.id.img_list);
        img_grid = findViewById(R.id.img_grid);
        img_back = findViewById(R.id.img_back);
        layout_sort_by = findViewById(R.id.layout_sort_by);
        layout_filter = findViewById(R.id.layout_filter);
        btn_need_req = findViewById(R.id.btn_need_req);
        layout_requirement_ad = findViewById(R.id.layout_requirement_ad);
        layout_list_filter = findViewById(R.id.layout_list_filter);
        layout_search_list = findViewById(R.id.layout_search_list);
        txt_page_title = findViewById(R.id.txt_page_title);
        rc_filter = findViewById(R.id.rc_filter);
        edt_search_suggestion = findViewById(R.id.edt_search_suggestion);
        img_search = findViewById(R.id.img_search);
        btn_add_post_header = findViewById(R.id.btn_add_post_header);
        btn_offer_req = findViewById(R.id.btn_offer_req);


        txt_page_title.setText(getString(R.string.txt_search));
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);
        gridManager = new GridLayoutManager(SearchActivity.this, 2);
        listManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
        rc_ad_list.setLayoutManager(listManager);
        rc_ad_list.setItemAnimator(new DefaultItemAnimator());
        homeAdListAdapter = new HomeAdListAdapter(adList, SearchActivity.this, true);
        homeAdGridAdapter = new HomeAdGridAdapter(adList, SearchActivity.this);
        rc_ad_list.setAdapter(homeAdListAdapter);

        sortByAdapter = new SortByAdapter(sortByList, getApplicationContext());
        filterOptionAdapter = new FilterOptionAdapter(filterList, getApplicationContext());
        filterSelectAdapter = new FilterSelectAdapter(filterSelectList, getApplicationContext());
        LinearLayoutManager listManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
        rc_filter.setLayoutManager(listManager);
        rc_filter.setItemAnimator(new DefaultItemAnimator());
        searchSuggistionAdapter = new SearchSuggistionAdapter(searchSuggestionList, getApplicationContext());
        rc_filter.setAdapter(searchSuggistionAdapter);
        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        layout_swipe_refresh.setOnRefreshListener(this);

        img_back.setOnClickListener(this);
        img_list.setOnClickListener(this);
        img_grid.setOnClickListener(this);
        layout_sort_by.setOnClickListener(this);
        layout_filter.setOnClickListener(this);
        btn_need_req.setOnClickListener(this);
        img_search.setOnClickListener(this);
        btn_add_post_header.setOnClickListener(this);
        btn_offer_req.setOnClickListener(this);

        if (_fun.isInternetAvailable(SearchActivity.this)) {
            getRecentSearch();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(SearchActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getRecentSearch();
                        }
                    });
                }
            }, 200);

        }

        CallBackFunction();
        SearchFunction();
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

    private void SearchFunction() {

        edt_search_suggestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String val = edt_search_suggestion.getText().toString();
                if (val.length() != 0) {

                    if (_fun.isInternetAvailable(SearchActivity.this)) {
                        getSearchSuggestion(val);
                    } else {
                        _fun.ShowNoInternetPopup(SearchActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                getSearchSuggestion(val);
                            }
                        });
                    }
                } else {
                    if (_fun.isInternetAvailable(SearchActivity.this)) {
                        getRecentSearch();
                    } else {
                        _fun.ShowNoInternetPopup(SearchActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                getRecentSearch();
                            }
                        });
                    }
                }
//                if (val.length() != 0) {
////                    edt_search_suggestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_image, 0);
////                } else {
////                    edt_search_suggestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);
////                }
            }
        });
        edt_search_suggestion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchSugeistionValue = edt_search_suggestion.getText().toString();
                    if (searchSugeistionValue.isEmpty()) {
                        Function.CustomMessage(SearchActivity.this, getString(R.string.enter_something_search));
                    } else {
                        if (Function.ProsperIdValidation(searchSugeistionValue)) {
                            Intent i = new Intent(SearchActivity.this, UserProfileActivity.class);
                            i.putExtra("prosperId", searchSugeistionValue);
                            i.putExtra("PageFrom", "3");
                            i.putExtra("PageID", "3");
                            startActivity(i);
                        } else {
                            Function.hideSoftKeyboard(SearchActivity.this, edt_search_suggestion);
                            layout_ad_list.setVisibility(View.VISIBLE);
                            layout_search_list.setVisibility(View.GONE);

                            isCategory = true;

                            searchCatId = "";

                            searchSubCatId = "";


                            searchChildCatId = "";

                            txt_page_title.setText(searchSugeistionValue);
                            currentPageNo = 1;
                            minValue = "";
                            maxValue = "";
                            isFilterSubCat = false;
                            getFilterData(false);
                            getAdList(true);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

//        edt_search_suggestion.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_RIGHT = 2;
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (edt_search_suggestion.getRight() - edt_search_suggestion.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                        searchSugeistionValue = edt_search_suggestion.getText().toString();
//                        if (searchSugeistionValue.isEmpty()) {
//                            Function.CustomMessage(SearchActivity.this, getString(R.string.enter_something_search));
//                        } else {
//                            Function.hideSoftKeyboard(SearchActivity.this, edt_search);
//                            layout_ad_list.setVisibility(View.VISIBLE);
//                            layout_search_list.setVisibility(View.GONE);
//
//                            isCategory = true;
//
//                            searchCatId = "";
//
//                            searchSubCatId = "";
//
//
//                            searchChildCatId = "";
//
//                            txt_page_title.setText(searchSugeistionValue);
//                            currentPageNo = 1;
//                            getAdList(true);
//                        }
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
    }


    private void CallBackFunction() {
        searchSuggistionAdapter.setCallback(new SearchSuggistionAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                if (searchSuggestionList.get(pos).getIsProsper() != null && searchSuggestionList.get(pos).getIsProsper().equals("1")) {
                    Intent i = new Intent(SearchActivity.this, UserProfileActivity.class);
                    i.putExtra("prosperId", searchSuggestionList.get(pos).getSearchValue());
                    i.putExtra("PageFrom", "3");
                    startActivity(i);
                } else {
                    layout_ad_list.setVisibility(View.VISIBLE);
                    layout_search_list.setVisibility(View.GONE);

                    isCategory = true;
                    if (searchSuggestionList.get(pos).getCatId() != null) {
                        if (!searchSuggestionList.get(pos).getCatId().equals("")) {
                            searchCatId = searchSuggestionList.get(pos).getCatId();
                        } else {
                            searchCatId = "";
                        }
                    } else {
                        searchCatId = "";
                    }
                    if (searchSuggestionList.get(pos).getSubCatId() != null) {
                        if (!searchSuggestionList.get(pos).getSubCatId().equals("")) {
                            searchSubCatId = searchSuggestionList.get(pos).getSubCatId();
                        } else {
                            searchSubCatId = "";
                        }
                    } else {
                        searchSubCatId = "";
                    }
                    if (searchSuggestionList.get(pos).getChildCatId() != null) {
                        if (!searchSuggestionList.get(pos).getChildCatId().equals("")) {
                            searchChildCatId = searchSuggestionList.get(pos).getChildCatId();
                        } else {
                            searchChildCatId = "";
                        }
                    } else {
                        searchChildCatId = "";
                    }
                    searchSugeistionValue = searchSuggestionList.get(pos).getSearchValue();
                    txt_page_title.setText(searchSugeistionValue);
                    currentPageNo = 1;
                    minValue = "";
                    maxValue = "";
                    isFilterSubCat = false;
                    getFilterData(false);
                    getAdList(true);
                }
            }
        });
        homeAdGridAdapter.setCallback(new HomeAdGridAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                String adId = adList.get(pos).getAdSlug();
                String userId = adList.get(pos).getUserId();
                changeFragment(adId, userId);
            }

            @Override
            public void onFavoriteClick(int pos) {

                if (!adList.get(pos).isProgress()) {
                    if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                        favPosition = pos;
                        adList.get(pos).setProgress(true);
                        callAddFavorite();
                    } else {
                        Intent l = new Intent(SearchActivity.this, LoginActivity.class);
                        startActivityForResult(l, POST_VIEW_CODE);
                    }

                }
            }

            @Override
            public void onShareClick(int pos) {
                String adId = adList.get(pos).getAdSlug();
                String adTitle = adList.get(pos).getAdTitle();
                String catGroup = adList.get(pos).getCatGroup();
                String url = adList.get(pos).getPhotoType();
//                ShareLink(catId, adId, adTitle,catGroup);
                Function.ShareLink(SearchActivity.this, adId, adTitle, catGroup, url);
            }

            @Override
            public void onLoginClick(int pos) {
                Intent l = new Intent(SearchActivity.this, LoginActivity.class);
                startActivityForResult(l, POST_VIEW_CODE);
            }
        });

        homeAdListAdapter.setCallback(new HomeAdListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                String adId = adList.get(pos).getAdSlug();
                String userId = adList.get(pos).getUserId();
                changeFragment(adId, userId);

            }

            @Override
            public void onFavoriteClick(int pos) {

                if (!adList.get(pos).isProgress()) {
                    if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                        favPosition = pos;
                        adList.get(pos).setProgress(true);
                        callAddFavorite();
                    } else {
                        Intent l = new Intent(SearchActivity.this, LoginActivity.class);
                        startActivityForResult(l, POST_VIEW_CODE);
                    }

                }
            }

            @Override
            public void onShareClick(int pos) {
                String adId = adList.get(pos).getAdSlug();
                String adTitle = adList.get(pos).getAdTitle();
                String catGroup = adList.get(pos).getCatGroup();
                String url = adList.get(pos).getPhotoType();
                Function.ShareLink(SearchActivity.this, adId, adTitle, catGroup, url);
            }

            @Override
            public void onLoginClick(int pos) {
                Intent l = new Intent(SearchActivity.this, LoginActivity.class);
                startActivityForResult(l, POST_VIEW_CODE);
            }
        });

    }


    private void callAddFavorite() {
        final String adId = adList.get(favPosition).getAdSlug();
        final String isFav = adList.get(favPosition).getIsFavorite();


        if (_fun.isInternetAvailable(SearchActivity.this)) {
            if (isFav.equals("0")) {
                if (!params.isEmpty()) {
                    params.clear();
                }
                params.put("ads_id", adId);
                Log.i("Params", params.toString());
                RetrofitClient.getClient().create(Api.class).addFavorite(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(SearchActivity.this, addFavorite, true, false));
            } else {
                RetrofitClient.getClient().create(Api.class).deleteFavorite(adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(SearchActivity.this, addFavorite, true, false));
            }
        } else {
            _fun.ShowNoInternetPopup(SearchActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    if (isFav.equals("0")) {
                        if (!params.isEmpty()) {
                            params.clear();
                        }
                        params.put("ads_id", adId);
                        Log.i("Params", params.toString());
                        RetrofitClient.getClient().create(Api.class).addFavorite(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                .enqueue(new RetrofitCallBack(SearchActivity.this, addFavorite, true, false));
                    } else {
                        RetrofitClient.getClient().create(Api.class).deleteFavorite(adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                .enqueue(new RetrofitCallBack(SearchActivity.this, addFavorite, true, false));
                    }
                }
            });
        }
    }

    void changeFragment(String adId, String userId) {
        if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            Intent p = new Intent(SearchActivity.this, MyPostViewActivity.class);
            p.putExtra("AdId", adId);
            startActivityForResult(p, POST_VIEW_CODE);
        } else {
            Intent p = new Intent(SearchActivity.this, PostViewActivity.class);
            p.putExtra("AdId", adId);
            startActivityForResult(p, POST_VIEW_CODE);
        }
    }

    private void getAdList(final boolean isLoad) {
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

        paramsFilter.put("category_id", searchCatId);
        paramsFilter.put("subcategory_id", searchSubCatId);
        paramsFilter.put("rental_duration", rentalDuration);
        paramsFilter.put("ad_type", adType);
        paramsFilter.put("fee", fee);
        paramsFilter.put("attribute_value", attribute);
        paramsFilter.put("sort_by", sortById);
        paramsFilter.put("locality_id", locality);
        paramsFilter.put("fee_min", minValue);
        paramsFilter.put("fee_max", maxValue);
        paramsFilter.put("city_id", Sessions.getSession(Constant.CityId, getApplicationContext()));
        params.put("filter", paramsFilter);
        params.put("searchTerm", searchSugeistionValue);
        params.put("page", String.valueOf(currentPageNo));


        Log.i("Params", params.toString());

        if (_fun.isInternetAvailable(SearchActivity.this)) {

            RetrofitClient.getClient().create(Api.class).getHomeAdsNew(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(SearchActivity.this, adListResponse, isLoad, false));
        } else {
            _fun.ShowNoInternetPopup(SearchActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getHomeAdsNew(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(SearchActivity.this, adListResponse, isLoad, false));
                }
            });
        }
    }

    private void getFilterData(boolean isLoad) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("parent_id", searchCatId);
        params.put("subcategory_id", searchSubCatId);
        params.put("city_id", Sessions.getSession(Constant.CityId, getApplicationContext()));
        Log.i("Params", params.toString());
        RetrofitClient.getClient().create(Api.class).getFilter(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(SearchActivity.this, filterResponse, isLoad, false));
    }

    retrofit2.Callback<FilterResult> adListResponse = new retrofit2.Callback<FilterResult>() {
        @Override
        public void onResponse(Call<FilterResult> call, retrofit2.Response<FilterResult> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);
            if (layout_swipe_refresh.isRefreshing()) {
                layout_swipe_refresh.setRefreshing(false);
            }
            FilterResult dr = response.body();
            try {

                if (dr.getStatus()) {
                    currentPageNo = dr.getCurrentPage();
                    lastPageNo = dr.getLastPage();
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
                            if (dr.getData().get(i).getLocality().getArea() != null)
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
                    }//                    layout_search_list.setVisibility(View.GONE);
                    layout_ad_list.setVisibility(View.VISIBLE);
//                    txt_page_title.setText(searchSugeistionValue);
//                    isSearch = true;
                    serachView = 1;
                    if (adList.size() == 0) {
                        layout_ad_list.setVisibility(View.GONE);
//                        layout_list_filter.setVisibility(View.VISIBLE);
//                        layout_requirement_ad.setVisibility(View.VISIBLE);
                        layout_ad_list.setVisibility(View.GONE);
                        layout_search_list.setVisibility(View.VISIBLE);
                        txt_page_title.setText(getString(R.string.txt_search));
                        ShowPopupNoADCity();
                    } else {
                        layout_list_filter.setVisibility(View.VISIBLE);
                        layout_requirement_ad.setVisibility(View.GONE);
                    }

                } else {
                    Function.CustomMessage(SearchActivity.this, dr.getMessage());
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
                    Function.CustomMessage(SearchActivity.this, dr.getMessage());
                } else {
                    Function.CustomMessage(SearchActivity.this, dr.getMessage());

                }
                adList.get(favPosition).setProgress(false);
//                if (isGrid) {
//                    homeAdGridAdapter.notifyItemChanged(favPosition);
//                } else {
//                    homeAdListAdapter.notifyItemChanged(favPosition);
//                }
                isChanges = true;

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
                if (isCategory) {
                    layout_ad_list.setVisibility(View.GONE);
                    layout_search_list.setVisibility(View.VISIBLE);
                    txt_page_title.setText(getString(R.string.txt_search));
                    isCategory = false;

                    sortById = "";

                    filterList.clear();
                    searchSubCatId = "";

                    minValue = "";
                    maxValue = "";
                    filterSelectAdapter.notifyDataSetChanged();
                    isFilterClear = true;
                    currentPageNo = 1;
                    filterSelectCount = 0;
                } else {
                    if (isChanges) {
                        Intent h = new Intent(SearchActivity.this, HomeActivity.class);
                        h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(h);
                        finish();
                    } else {
                        finish();
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
                    Intent p = new Intent(SearchActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 2);
                    p.putExtra("ParId", "");
                    startActivity(p);
                } else {
                    Intent l = new Intent(SearchActivity.this, LoginActivity.class);
                    startActivityForResult(l, POST_VIEW_CODE);
                }
                break;
            case R.id.img_search:
                searchSugeistionValue = edt_search_suggestion.getText().toString();
                if (searchSugeistionValue.isEmpty()) {
                    Function.CustomMessage(SearchActivity.this, getString(R.string.enter_something_search));
                } else {
                    if (Function.ProsperIdValidation(searchSugeistionValue)) {
                        Intent i = new Intent(SearchActivity.this, UserProfileActivity.class);
                        i.putExtra("prosperId", searchSugeistionValue);
                        i.putExtra("PageFrom", "3");
                        i.putExtra("PageID", "3");
                        startActivity(i);
                    } else {
                        Function.hideSoftKeyboard(SearchActivity.this, edt_search_suggestion);
                        layout_ad_list.setVisibility(View.VISIBLE);
                        layout_search_list.setVisibility(View.GONE);
                        isCategory = true;
                        searchCatId = "";
                        searchSubCatId = "";
                        searchChildCatId = "";
                        txt_page_title.setText(searchSugeistionValue);
                        currentPageNo = 1;
                        minValue = "";
                        maxValue = "";
                        isFilterSubCat = false;
                        getFilterData(false);
                        getAdList(true);
                    }
                }
                break;
            case R.id.btn_add_post_header:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent a = new Intent(SearchActivity.this, AddPostActivity.class);
                    a.putExtra("AdType", 0);
                    startActivity(a);
                } else {
                    Intent l = new Intent(SearchActivity.this, LoginActivity.class);
                    startActivityForResult(l, POST_VIEW_CODE);
                }
                break;
            case R.id.btn_offer_req:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(SearchActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    p.putExtra("ParId", "");
                    startActivity(p);
                } else {
                    Intent l = new Intent(SearchActivity.this, LoginActivity.class);
                    startActivityForResult(l, POST_VIEW_CODE);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isCategory) {
            layout_ad_list.setVisibility(View.GONE);
            layout_search_list.setVisibility(View.VISIBLE);
            txt_page_title.setText(getString(R.string.txt_search));
            isCategory = false;
            sortById = "";

            filterList.clear();
            filterOptionAdapter.notifyDataSetChanged();
            searchSubCatId = "";

            minValue = "";
            maxValue = "";
            filterSelectAdapter.notifyDataSetChanged();
            isFilterClear = true;
            currentPageNo = 1;
            filterSelectCount = 0;

        } else {
            if (isChanges) {
                Intent h = new Intent(SearchActivity.this, HomeActivity.class);
                h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(h);
                finish();
            } else {
                finish();
                super.onBackPressed();
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
        LinearLayoutManager listManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
        rc_filter_option.setLayoutManager(listManager);
        rc_filter_option.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager listManager2 = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
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
                Function.hideSoftKeyboard(SearchActivity.this, layout_min_max);
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
                    Function.CustomMessage(SearchActivity.this, getString(R.string.min_max_valitaion_msg));
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
                    searchSubCatId = "";
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
//                txt_filter_option_title.setText(getString(R.string.ent_rental_hire_range));
                txt_filter_option_title.setText("Enter " + getString(R.string.txt_rent_hire));
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
                searchSubCatId = model.getSortId();
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

        LinearLayoutManager listManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
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
                searchSubCatId = model.getSortId();
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
                Function.hideSoftKeyboard(SearchActivity.this, v);
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

    private void getSearchSuggestion(String searchVal) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("term", searchVal);
        Log.i("Params", params.toString());
        RetrofitClient.getClient().create(Api.class).getSearchSuggestion(params)
                .enqueue(new RetrofitCallBack(SearchActivity.this, searchResult, false, true));

    }

    private void getRecentSearch() {

        RetrofitClient.getClient().create(Api.class).getRecentSearch(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(SearchActivity.this, searchResult, false, true));
    }

    retrofit2.Callback<SearchSuggistonModel> searchResult = new retrofit2.Callback<SearchSuggistonModel>() {
        @Override
        public void onResponse(Call<SearchSuggistonModel> call, retrofit2.Response<SearchSuggistonModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);
            if (layout_swipe_refresh.isRefreshing()) {
                layout_swipe_refresh.setRefreshing(false);
            }
            SearchSuggistonModel dr = response.body();
            try {
                if (dr.isStatus()) {
                    searchSuggestionList.clear();
                    searchSuggestionList.addAll(dr.getSearchSugList());
                    searchSuggistionAdapter.notifyDataSetChanged();
                } else {
                    Function.CustomMessage(SearchActivity.this, dr.getMessage());
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == POST_VIEW_CODE) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                currentPageNo = 1;
                getAdList(true);
                isChanges = true;
            }
        }
    }

    @Override
    public void onRefresh() {
        Function.CoinTone(SearchActivity.this);
        if (isCategory) {
            currentPageNo = 1;
            getAdList(false);
        } else {

            final String val = edt_search_suggestion.getText().toString();
            if (val.length() > 1) {

                if (_fun.isInternetAvailable(SearchActivity.this)) {
                    getSearchSuggestion(val);
                } else {
                    _fun.ShowNoInternetPopup(SearchActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getSearchSuggestion(val);
                        }
                    });
                }
            } else {
                if (_fun.isInternetAvailable(SearchActivity.this)) {
                    getRecentSearch();
                } else {
                    _fun.ShowNoInternetPopup(SearchActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getRecentSearch();
                        }
                    });
                }
            }

        }
    }

    public void ShowPopupNoADCity() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_no_search_list, null);

        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(false);
        spinnerPopup.update();
        Button btn_offer = view.findViewById(R.id.btn_offer_req);
        Button btn_need = view.findViewById(R.id.btn_need_req);
        LinearLayout layout_delete = view.findViewById(R.id.layout_delete);
        btn_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(SearchActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    p.putExtra("ParId", "");
                    startActivity(p);
                    spinnerPopup.dismiss();
                } else {
                    Intent l = new Intent(SearchActivity.this, LoginActivity.class);
                    startActivityForResult(l, POST_VIEW_CODE);
                }
            }
        });
        btn_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(SearchActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 2);
                    p.putExtra("ParId", "");
                    startActivity(p);
                    spinnerPopup.dismiss();
                } else {
                    Intent l = new Intent(SearchActivity.this, LoginActivity.class);
                    startActivityForResult(l, POST_VIEW_CODE);
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
}
