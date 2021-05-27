package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import java.util.Collections;

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
    LinearLayout layout_total_ads;
    LinearLayout layout_active_ads;
    LinearLayout layout_inactive_ads;
    Button btn_add_post;
    Button btn_add_post_header;
    EditText edt_search_ad;
    SwipeRefreshLayout layout_swipe_refresh;
    AdListAdapter adListAdapter;
    ArrayList<AdItemModel> adList = new ArrayList<>();
    ArrayList<AdItemModel> tempAdList = new ArrayList<>();
    int totalCount = 0;
    int activeCount = 0;
    int inActiveCount = 0;
    int tabNo = 1;

    int MY_POST_VIEW_CODE = 1299;
    boolean isChanges = false;
    private Function _fun = new Function();
    private PopupWindow spinnerPopup;

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
        layout_total_ads = findViewById(R.id.layout_total_ads);
        layout_active_ads = findViewById(R.id.layout_active_ads);
        layout_inactive_ads = findViewById(R.id.layout_inactive_ads);

        txt_page_title.setText(getString(R.string.nav_dash_board));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_adPost.setLayoutManager(linearLayoutManager);
        rc_adPost.setItemAnimator(new DefaultItemAnimator());
        adListAdapter = new AdListAdapter(adList, getApplicationContext());
        rc_adPost.setAdapter(adListAdapter);
        img_back.setOnClickListener(this);
        btn_add_post.setOnClickListener(this);
        btn_add_post_header.setOnClickListener(this);
        layout_total_ads.setOnClickListener(this);
        layout_active_ads.setOnClickListener(this);
        layout_inactive_ads.setOnClickListener(this);
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
                String adId = model.getAdSlug();
                Intent p = new Intent(DashBoardActivity.this, MyPostViewActivity.class);
                p.putExtra("AdId", adId);
                startActivityForResult(p, MY_POST_VIEW_CODE);
            }

            @Override
            public void onShareClick(AdItemModel model) {
                String adId = model.getAdSlug();
                String adTitle = model.getAdTitle();
                String catGroup = model.getCatGroup();
                String url = model.getPhotoType();
                Function.ShareLink(DashBoardActivity.this, adId, adTitle, catGroup, url);
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

            @Override
            public void onAdViewClick(AdItemModel model) {
                ShowPopupViewCount(model);
            }
        });
    }


    private void getAdList(boolean isLoader) {
        Log.i("Token", Sessions.getSession(Constant.UserToken, getApplicationContext()));

        RetrofitClient.getClient().create(Api.class).getPost(Sessions.getSession(Constant.UserToken, getApplicationContext()))

                .enqueue(new RetrofitCallBack(DashBoardActivity.this, adListResponse, isLoader, false));
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
                    tempAdList.clear();
                    activeCount = 0;
                    inActiveCount = 0;
                    adList.addAll(dr.getAdList());
                    tempAdList.addAll(dr.getAdList());
                    adListAdapter.notifyDataSetChanged();
                    if (adList.size() != 0) {
                        totalCount = adList.size();
                        for (int i = 0; i < adList.size(); i++) {
                            if (adList.get(i).getAdStatus().equals("2")) {
                                inActiveCount++;
                            } else {
                                activeCount++;
                            }
                        }
                        txt_total_ad_count.setText(String.valueOf(totalCount));
                        txt_active_ad_count.setText(String.valueOf(activeCount));
                        txt_in_active_count.setText(String.valueOf(inActiveCount));
                    }
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
            case R.id.layout_total_ads:
                if (tabNo != 1) {
                    tabNo = 1;
                    adList.clear();
                    adList.addAll(tempAdList);
                    adListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.layout_active_ads:
                if (tabNo != 2 && activeCount != 0) {
                    tabNo = 2;
                    adList.clear();
                    for (int i = 0; i < tempAdList.size(); i++) {
                        if (!tempAdList.get(i).getAdStatus().equals("2")) {
                            adList.add(tempAdList.get(i));
                        }
                    }
                    Collections.reverse(adList);
                    adListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.layout_inactive_ads:
                if (tabNo != 3 && inActiveCount != 0) {
                    tabNo = 3;
                    adList.clear();
                    for (int i = 0; i < tempAdList.size(); i++) {
                        if (tempAdList.get(i).getAdStatus().equals("2")) {
                            adList.add(tempAdList.get(i));
                        }
                    }

                    adListAdapter.notifyDataSetChanged();
                } else if (inActiveCount == 0) {
                    Function.CustomMessage(DashBoardActivity.this, getString(R.string.txt_inactive_no_content));
                }
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
        Function.CoinTone(DashBoardActivity.this);
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


    public void ShowPopupViewCount(AdItemModel model) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_popup_ads_view, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(false);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        LinearLayout layout_delete = view.findViewById(R.id.layout_delete);
        LinearLayout layout_ad_view = view.findViewById(R.id.layout_ad_view);
        LinearLayout layout_no_count = view.findViewById(R.id.layout_no_count);
        TextView txt_ad_view_count = view.findViewById(R.id.txt_ad_view_count);
        TextView txt_page_view_count = view.findViewById(R.id.txt_page_view_count);
        TextView txt_contact_view_count = view.findViewById(R.id.txt_contact_view_count);
        txt_ad_view_count.setText(model.getUserViewCount());
        txt_page_view_count.setText(model.getProsperPageViewCount());
        txt_contact_view_count.setText(model.getUserContactViewCount());
        if (model.getAdStatus().equals("0") || model.getAdStatus().equals("9")) {
            layout_ad_view.setVisibility(View.GONE);
            layout_no_count.setVisibility(View.VISIBLE);
        } else {
            layout_ad_view.setVisibility(View.VISIBLE);
            layout_no_count.setVisibility(View.GONE);
        }
        layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
