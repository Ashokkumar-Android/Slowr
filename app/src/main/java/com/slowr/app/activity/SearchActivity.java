package com.slowr.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import com.slowr.app.models.FiltersModel;
import com.slowr.app.models.HomeFilterAdModel;
import com.slowr.app.models.SearchSuggistonModel;
import com.slowr.app.models.SortByModel;
import com.slowr.app.models.SuggistionItem;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class SearchActivity extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    LinearLayout layout_ad_list;
    ImageView img_list;
    ImageView img_grid;
    LinearLayout img_back;
    LinearLayout layout_sort_by;
    LinearLayout layout_filter;
    LinearLayout layout_requirement_ad;
    LinearLayout layout_list_filter;
    LinearLayout layout_search_list;
    Button btn_requirement_ad;
    TextView txt_page_title;
    EditText edt_search_suggestion;
    SwipeRefreshLayout layout_swipe_refresh;

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
        btn_requirement_ad = findViewById(R.id.btn_requirement_ad);
        layout_requirement_ad = findViewById(R.id.layout_requirement_ad);
        layout_list_filter = findViewById(R.id.layout_list_filter);
        layout_search_list = findViewById(R.id.layout_search_list);
        txt_page_title = findViewById(R.id.txt_page_title);
        rc_filter = findViewById(R.id.rc_filter);
        edt_search_suggestion = findViewById(R.id.edt_search_suggestion);
        txt_page_title.setText(getString(R.string.txt_search));
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);
        gridManager = new GridLayoutManager(SearchActivity.this, 2);
        listManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
        rc_ad_list.setLayoutManager(listManager);
        rc_ad_list.setItemAnimator(new DefaultItemAnimator());
        homeAdListAdapter = new HomeAdListAdapter(adList, SearchActivity.this);
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
        btn_requirement_ad.setOnClickListener(this);
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
//                if (val.length() != 0) {
//                    edt_search_suggestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_image, 0);
//                } else {
//                    edt_search_suggestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);
//                }
            }
        });

//        edt_search_suggestion.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int DRAWABLE_RIGHT = 2;
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (edt_search_suggestion.getRight() - edt_search_suggestion.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//
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
                layout_ad_list.setVisibility(View.VISIBLE);
                layout_search_list.setVisibility(View.GONE);

                isCategory = true;
                if (searchSuggestionList.get(pos).getCatId() != null) {
                    if (!searchSuggestionList.get(pos).getCatId().equals("")) {
                        searchCatId = searchSuggestionList.get(pos).getCatId();
                    }
                } else {
                    searchCatId = "";
                }
                if (searchSuggestionList.get(pos).getSubCatId() != null) {
                    if (!searchSuggestionList.get(pos).getSubCatId().equals("")) {
                        searchSubCatId = searchSuggestionList.get(pos).getSubCatId();
                    }
                } else {
                    searchSubCatId = "";
                }
                if (searchSuggestionList.get(pos).getChildCatId() != null) {
                    if (!searchSuggestionList.get(pos).getChildCatId().equals("")) {
                        searchChildCatId = searchSuggestionList.get(pos).getChildCatId();
                    }
                } else {
                    searchChildCatId = "";
                }
                searchSugeistionValue = searchSuggestionList.get(pos).getSearchValue();
                txt_page_title.setText(searchSugeistionValue);
                currentPageNo = 1;
                getAdList(true);
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
                        callAddFavorite();
                    } else {
                        Function.CustomMessage(SearchActivity.this, getString(R.string.txt_please_login));
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

                if (!adList.get(pos).isProgress()) {
                    if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                        favPosition = pos;
                        adList.get(pos).setProgress(true);
                        callAddFavorite();
                    } else {
                        Function.CustomMessage(SearchActivity.this, getString(R.string.txt_please_login));
                    }

                }
            }
        });

    }

    private void callAddFavorite() {
        final String catId = adList.get(favPosition).getCatId();
        final String adId = adList.get(favPosition).getAdId();
        final String isFav = adList.get(favPosition).getIsFavorite();


        if (_fun.isInternetAvailable(SearchActivity.this)) {
            if (isFav.equals("0")) {
                if (!params.isEmpty()) {
                    params.clear();
                }
                params.put("ads_id", adId);
                params.put("category_id", catId);
                Log.i("Params", params.toString());
                RetrofitClient.getClient().create(Api.class).addFavorite(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(SearchActivity.this, addFavorite, true));
            } else {
                RetrofitClient.getClient().create(Api.class).deleteFavorite(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(SearchActivity.this, addFavorite, true));
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
                        params.put("category_id", catId);
                        Log.i("Params", params.toString());
                        RetrofitClient.getClient().create(Api.class).addFavorite(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                .enqueue(new RetrofitCallBack(SearchActivity.this, addFavorite, true));
                    } else {
                        RetrofitClient.getClient().create(Api.class).deleteFavorite(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                .enqueue(new RetrofitCallBack(SearchActivity.this, addFavorite, true));
                    }
                }
            });
        }
    }

    void changeFragment(String catId, String adId, String userId) {
        if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            Intent p = new Intent(SearchActivity.this, MyPostViewActivity.class);
            p.putExtra("CatId", catId);
            p.putExtra("AdId", adId);
            startActivityForResult(p, POST_VIEW_CODE);
        } else {
            Intent p = new Intent(SearchActivity.this, PostViewActivity.class);
            p.putExtra("CatId", catId);
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

        params.put("categoryId", searchCatId);
        params.put("subCategoryId", searchSubCatId);
        params.put("childCategoryId", searchChildCatId);
        params.put("cityId", Sessions.getSession(Constant.CityId, SearchActivity.this));
        params.put("search", searchSugeistionValue);
        params.put("page", String.valueOf(currentPageNo));
        params.put("sortBy", sortById);
        params.put("attributeFilterOptions", paramsFilter);
        Log.i("Params", params.toString());


        if (_fun.isInternetAvailable(SearchActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getHomeAds(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(SearchActivity.this, adListResponse, isLoad));
        } else {
            _fun.ShowNoInternetPopup(SearchActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getHomeAds(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(SearchActivity.this, adListResponse, isLoad));
                }
            });
        }
    }


    retrofit2.Callback<HomeFilterAdModel> adListResponse = new retrofit2.Callback<HomeFilterAdModel>() {
        @Override
        public void onResponse(Call<HomeFilterAdModel> call, retrofit2.Response<HomeFilterAdModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            HomeFilterAdModel dr = response.body();
            try {
                if (dr.isStatus()) {

//                    Log.i("ImagePathUrl", dr.getUrlPath());
//                    Sessions.saveSession(Constant.ImagePath, dr.getUrlPath(), SearchActivity.this);

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
                    layout_search_list.setVisibility(View.GONE);
                    layout_ad_list.setVisibility(View.VISIBLE);
                    txt_page_title.setText(searchSugeistionValue);
                    isSearch = true;
                    serachView = 1;
                    if (adList.size() == 0) {
                        layout_list_filter.setVisibility(View.GONE);
                        layout_requirement_ad.setVisibility(View.VISIBLE);
                    } else {
                        layout_list_filter.setVisibility(View.VISIBLE);
                        layout_requirement_ad.setVisibility(View.GONE);
                    }

                } else {
                    Function.CustomMessage(SearchActivity.this,dr.getMessage());
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
                if (isGrid) {
                    homeAdGridAdapter.notifyItemChanged(favPosition);
                } else {
                    homeAdListAdapter.notifyItemChanged(favPosition);
                }
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
                Intent p = new Intent(SearchActivity.this, AddPostActivity.class);
                p.putExtra("AdType", 2);
                startActivity(p);
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
        TextView txt_page_action = view.findViewById(R.id.txt_page_action);
        final RecyclerView rc_filter = view.findViewById(R.id.rc_filter);
        TextView txt_clear = view.findViewById(R.id.txt_clear);
        txt_page_action.setText(getString(R.string.txt_apply));
        txt_clear.setText(getString(R.string.txt_clear));
        LinearLayoutManager listManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
        rc_filter.setLayoutManager(listManager);
        rc_filter.setItemAnimator(new DefaultItemAnimator());
//        SortByAdapter sortByAdapter = new SortByAdapter(sortByList, getApplicationContext());
        if (type == 1) {
            rc_filter.setAdapter(sortByAdapter);
            txt_popup_title.setText(getString(R.string.txt_sort_by));
        } else if (type == 2) {
            rc_filter.setAdapter(filterOptionAdapter);
            txt_popup_title.setText(getString(R.string.txt_filter));
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
                ShowPopupFilter(pos);
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
                getAdList(true);
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
                isFilterClear = true;
                currentPageNo = 1;
                getAdList(false);

            }

        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void ShowPopupFilter(final int pos) {
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

        LinearLayoutManager listManager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
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
                filterList.get(pos).setSelectedValue(selVal);
                filterOptionAdapter.notifyDataSetChanged();
                if (filterPopup.isShowing()) {
                    filterPopup.dismiss();
                }
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
                .enqueue(new RetrofitCallBack(SearchActivity.this, searchResult, false));

    }

    private void getRecentSearch() {

        RetrofitClient.getClient().create(Api.class).getRecentSearch(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(SearchActivity.this, searchResult, false));
    }

    retrofit2.Callback<SearchSuggistonModel> searchResult = new retrofit2.Callback<SearchSuggistonModel>() {
        @Override
        public void onResponse(Call<SearchSuggistonModel> call, retrofit2.Response<SearchSuggistonModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

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
        if (layout_swipe_refresh.isRefreshing()) {
            layout_swipe_refresh.setRefreshing(false);
        }
        if (isCategory) {
            currentPageNo = 1;
            getAdList(true);
        }else {

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
}
