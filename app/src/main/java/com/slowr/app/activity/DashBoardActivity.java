package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.slowr.app.R;
import com.slowr.app.adapter.AdListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.models.AdModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;

import retrofit2.Call;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rc_adPost;
    TextView txt_page_title;
    LinearLayout img_back;
    TextView txt_total_ad_count;
    TextView txt_active_ad_count;
    TextView txt_in_active_count;
    NestedScrollView layout_root;
    LinearLayout layout_no_result;
    Button btn_add_post;
    Button btn_add_post_header;
    EditText edt_search_ad;
    SwipeRefreshLayout layout_swipe_refresh;
    AdListAdapter adListAdapter;
    ArrayList<AdItemModel> adList = new ArrayList<>();

    int MY_POST_VIEW_CODE = 1299;
    boolean isChanges = false;
    private Function _fun = new Function();
    String shareMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        doDeclaration();
    }

    private void doDeclaration() {
        rc_adPost = findViewById(R.id.rc_adPost);
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        txt_total_ad_count = findViewById(R.id.txt_total_ad_count);
        txt_active_ad_count = findViewById(R.id.txt_active_ad_count);
        txt_in_active_count = findViewById(R.id.txt_in_active_count);
        layout_root = findViewById(R.id.layout_root);
        layout_no_result = findViewById(R.id.layout_no_result);
        btn_add_post = findViewById(R.id.btn_add_post);
        btn_add_post_header = findViewById(R.id.btn_add_post_header);
        edt_search_ad = findViewById(R.id.edt_search_ad);
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);

        txt_page_title.setText(getString(R.string.nav_dash_board));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_adPost.setLayoutManager(linearLayoutManager);
        rc_adPost.setItemAnimator(new DefaultItemAnimator());
        adListAdapter = new AdListAdapter(adList, getApplicationContext());
        rc_adPost.setAdapter(adListAdapter);
        img_back.setOnClickListener(this);
        btn_add_post.setOnClickListener(this);
        btn_add_post_header.setOnClickListener(this);
        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        layout_swipe_refresh.setOnRefreshListener(this);
//        getUserDetails();

        if (_fun.isInternetAvailable(DashBoardActivity.this)) {
            getAdList(true);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(DashBoardActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList(true);
                        }
                    });
                }
            }, 200);

        }

        callBackFunction();
        doFilter();
    }

    private void doFilter() {
        edt_search_ad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adListAdapter.getFilter().filter(edt_search_ad.getText().toString());

            }
        });
    }

    private void callBackFunction() {
        adListAdapter.setCallback(new AdListAdapter.Callback() {
            @Override
            public void itemClick(AdItemModel model) {
                String catId = model.getCatId();
                String adId = model.getAdId();
                Intent p = new Intent(DashBoardActivity.this, MyPostViewActivity.class);
                p.putExtra("CatId", catId);
                p.putExtra("AdId", adId);
                startActivityForResult(p, MY_POST_VIEW_CODE);
            }

            @Override
            public void onShareClick(AdItemModel model) {
                String catId = model.getCatId();
                String adId = model.getAdId();
                String adTitle = model.getAdTitle();
                String catGroup = model.getCatGroup();
                Function.ShareLink(DashBoardActivity.this, catId, adId, adTitle, catGroup);
            }

            @Override
            public void onPromoteClick(AdItemModel model) {
                String catId = model.getCatId();
                String adId = model.getAdId();
                String adTitle = model.getAdTitle();
                Intent p = new Intent(DashBoardActivity.this, UpgradeActivity.class);
                p.putExtra("PageFrom", "2");
                p.putExtra("CatId", catId);
                p.putExtra("AdId", adId);
                p.putExtra("AdTitle", adTitle);
                startActivityForResult(p, MY_POST_VIEW_CODE);
            }
        });
    }


    private void getAdList(boolean isLoader) {
        Log.i("Token", Sessions.getSession(Constant.UserToken, getApplicationContext()));

        RetrofitClient.getClient().create(Api.class).getPost(Sessions.getSession(Constant.UserToken, getApplicationContext()))

                .enqueue(new RetrofitCallBack(DashBoardActivity.this, adListResponse, isLoader));
    }

    retrofit2.Callback<AdModel> adListResponse = new retrofit2.Callback<AdModel>() {
        @Override
        public void onResponse(Call<AdModel> call, retrofit2.Response<AdModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            AdModel dr = response.body();
            try {
                if (layout_swipe_refresh.isRefreshing()) {
                    layout_swipe_refresh.setRefreshing(false);
                }
                if (dr.isStatus()) {
                    adList.clear();
                    adList.addAll(dr.getAdList());
                    adListAdapter.notifyDataSetChanged();
                    txt_total_ad_count.setText(dr.getAdCountModel().getTotalAdCount());
                    txt_active_ad_count.setText(dr.getAdCountModel().getActiveAdCount());
                    txt_in_active_count.setText(dr.getAdCountModel().getInActiveAdCount());
                } else {
                    adList.clear();
                    adListAdapter.notifyDataSetChanged();
                    txt_total_ad_count.setText("0");
                    txt_active_ad_count.setText("0");
                    txt_in_active_count.setText("0");

                }
                if (adList.size() == 0) {
                    layout_root.setVisibility(View.GONE);
                    btn_add_post_header.setVisibility(View.GONE);
                    layout_no_result.setVisibility(View.VISIBLE);
                } else {
                    layout_root.setVisibility(View.VISIBLE);
                    btn_add_post_header.setVisibility(View.VISIBLE);
                    layout_no_result.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (adList.size() == 0) {
                    layout_root.setVisibility(View.GONE);
                    btn_add_post_header.setVisibility(View.GONE);
                    layout_no_result.setVisibility(View.VISIBLE);
                } else {
                    layout_root.setVisibility(View.VISIBLE);
                    btn_add_post_header.setVisibility(View.VISIBLE);
                    layout_no_result.setVisibility(View.GONE);
                }

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
            if (adList.size() == 0) {
                layout_root.setVisibility(View.GONE);
                btn_add_post_header.setVisibility(View.GONE);
                layout_no_result.setVisibility(View.VISIBLE);
            } else {
                layout_root.setVisibility(View.VISIBLE);
                btn_add_post_header.setVisibility(View.VISIBLE);
                layout_no_result.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
//                if (isChanges) {
                Intent h = new Intent(DashBoardActivity.this, HomeActivity.class);
                h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(h);
                finish();
//                } else {
//                    finish();
//                }
                break;
            case R.id.btn_add_post:
                Intent p = new Intent(DashBoardActivity.this, AddPostActivity.class);
                p.putExtra("AdType", 0);
                startActivityForResult(p, MY_POST_VIEW_CODE);
                break;

            case R.id.btn_add_post_header:
                Intent a = new Intent(DashBoardActivity.this, AddPostActivity.class);
                a.putExtra("AdType", 0);
                startActivityForResult(a, MY_POST_VIEW_CODE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        if (isChanges) {
        Intent h = new Intent(DashBoardActivity.this, HomeActivity.class);
        h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(h);
        finish();

//        } else {
//            finish();
        super.onBackPressed();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MY_POST_VIEW_CODE) {
                if (_fun.isInternetAvailable(DashBoardActivity.this)) {
                    getAdList(true);
                } else {
                    _fun.ShowNoInternetPopup(DashBoardActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList(true);
                        }
                    });
                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                isChanges = true;
                edt_search_ad.setText("");
            }
        }
    }

    @Override
    public void onRefresh() {

        if (_fun.isInternetAvailable(DashBoardActivity.this)) {
            getAdList(false);
        } else {
            _fun.ShowNoInternetPopup(DashBoardActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    getAdList(false);
                }
            });
        }
    }
}
