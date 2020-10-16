package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.slowr.app.R;
import com.slowr.app.adapter.HomeAdListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.PopularAdModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rc_favorite;
    TextView txt_page_title;
    LinearLayout img_back;
    LinearLayout layout_no_result;
    Button btn_home_page;
    SwipeRefreshLayout layout_swipe_refresh;

    HomeAdListAdapter homeAdListAdapter;
    ArrayList<AdItemModel> adList = new ArrayList<>();
    LinearLayoutManager listManager;
    HashMap<String, Object> params = new HashMap<String, Object>();

    int favPosition = 0;
    int VIEW_POST_CODE = 1299;
    boolean isChange = false;
    private Function _fun = new Function();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        doDeclaration();
    }

    private void doDeclaration() {
        rc_favorite = findViewById(R.id.rc_favorite);
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        layout_no_result = findViewById(R.id.layout_no_result);
        btn_home_page = findViewById(R.id.btn_home_page);
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);
        txt_page_title.setText(getString(R.string.nav_my_favorite));

        listManager = new LinearLayoutManager(FavoriteActivity.this, RecyclerView.VERTICAL, false);
        rc_favorite.setLayoutManager(listManager);
        rc_favorite.setItemAnimator(new DefaultItemAnimator());
        homeAdListAdapter = new HomeAdListAdapter(adList, FavoriteActivity.this);
        rc_favorite.setAdapter(homeAdListAdapter);
        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        layout_swipe_refresh.setOnRefreshListener(this);
        if (_fun.isInternetAvailable(FavoriteActivity.this)) {
            getFavList(true);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(FavoriteActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getFavList(true);
                        }
                    });
                }
            }, 200);

        }

        callBackFunction();
        img_back.setOnClickListener(this);
        btn_home_page.setOnClickListener(this);
    }

    private void callBackFunction() {
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
                    favPosition = pos;
                    adList.get(pos).setProgress(true);
                    if (_fun.isInternetAvailable(FavoriteActivity.this)) {
                        callAddFavorite();
                    } else {
                        _fun.ShowNoInternetPopup(FavoriteActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                callAddFavorite();
                            }
                        });

                    }


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
                    .enqueue(new RetrofitCallBack(FavoriteActivity.this, addFavorite, true));
        } else {
            RetrofitClient.getClient().create(Api.class).deleteFavorite(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(FavoriteActivity.this, addFavorite, true));
        }
    }

    void changeFragment(String catId, String adId, String userId) {
        if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            Intent p = new Intent(FavoriteActivity.this, MyPostViewActivity.class);
            p.putExtra("CatId", catId);
            p.putExtra("AdId", adId);
            startActivityForResult(p, VIEW_POST_CODE);
        } else {
            Intent p = new Intent(FavoriteActivity.this, PostViewActivity.class);
            p.putExtra("CatId", catId);
            p.putExtra("AdId", adId);
            startActivityForResult(p, VIEW_POST_CODE);
        }


//        Intent p = new Intent(FavoriteActivity.this, PostViewActivity.class);
//        p.putExtra("CatId", catId);
//        p.putExtra("AdId", adId);
//        startActivityForResult(p, VIEW_POST_CODE);
    }

    private void getFavList(boolean isLoad) {
        RetrofitClient.getClient().create(Api.class).getFavorites(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(FavoriteActivity.this, adListResponse, isLoad));

    }

    retrofit2.Callback<PopularAdModel> adListResponse = new retrofit2.Callback<PopularAdModel>() {
        @Override
        public void onResponse(Call<PopularAdModel> call, retrofit2.Response<PopularAdModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            PopularAdModel dr = response.body();
            try {
                if (layout_swipe_refresh.isRefreshing()) {
                    layout_swipe_refresh.setRefreshing(false);
                }
                if (dr.isStatus()) {

//                    Log.i("ImagePathUrl", dr.getUrlPath());
//                    Sessions.saveSession(Constant.ImagePath, dr.getUrlPath(), FavoriteActivity.this);

                    adList.clear();
                    adList.addAll(dr.getAdList());
                    homeAdListAdapter.notifyDataSetChanged();
                    if (adList.size() == 0) {
                        layout_no_result.setVisibility(View.VISIBLE);
                        rc_favorite.setVisibility(View.GONE);
                    } else {
                        layout_no_result.setVisibility(View.GONE);
                        rc_favorite.setVisibility(View.VISIBLE);
                    }
                } else {
                    Function.CustomMessage(FavoriteActivity.this, dr.getMessage());
                    if (adList.size() == 0) {
                        layout_no_result.setVisibility(View.VISIBLE);
                        rc_favorite.setVisibility(View.GONE);
                    } else {
                        layout_no_result.setVisibility(View.GONE);
                        rc_favorite.setVisibility(View.VISIBLE);
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
                        adList.remove(favPosition);
                        homeAdListAdapter.notifyItemRemoved(favPosition);
                    }
                    Function.CustomMessage(FavoriteActivity.this, dr.getMessage());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    isChange = true;
                    if (adList.size() == 0) {
                        layout_no_result.setVisibility(View.VISIBLE);
                        rc_favorite.setVisibility(View.GONE);
                    } else {
                        layout_no_result.setVisibility(View.GONE);
                        rc_favorite.setVisibility(View.VISIBLE);
                    }
                } else {
                    Function.CustomMessage(FavoriteActivity.this, dr.getMessage());
                }


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
                if (isChange) {
                    Intent h = new Intent(FavoriteActivity.this, HomeActivity.class);
                    h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(h);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.btn_home_page:
                Intent h = new Intent(FavoriteActivity.this, HomeActivity.class);
                startActivity(h);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isChange) {
            Intent h = new Intent(FavoriteActivity.this, HomeActivity.class);
            h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(h);
            finish();
        } else {
            finish();
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == VIEW_POST_CODE) {
                if (_fun.isInternetAvailable(FavoriteActivity.this)) {
                    getFavList(true);
                } else {
                    _fun.ShowNoInternetPopup(FavoriteActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getFavList(true);
                        }
                    });

                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                isChange = false;
            }
        }
    }

    @Override
    public void onRefresh() {

        if (_fun.isInternetAvailable(FavoriteActivity.this)) {
            getFavList(false);
        } else {
            _fun.ShowNoInternetPopup(FavoriteActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    getFavList(false);
                }
            });

        }
    }
}
