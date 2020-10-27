package com.slowr.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.slowr.app.R;
import com.slowr.app.adapter.BannerListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.BannerItemModel;
import com.slowr.app.models.BannerModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;

import retrofit2.Call;

public class BannerActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    TextView txt_page_title;
    LinearLayout img_back;
    RecyclerView rc_banner;
    FloatingActionButton fb_add_banner;
    SwipeRefreshLayout layout_swipe_refresh;
    LinearLayoutManager listManager;
    ArrayList<BannerItemModel> bannerList = new ArrayList<BannerItemModel>();
    BannerListAdapter bannerListAdapter;
    private Function _fun = new Function();

    int EDIT_CODE = 1299;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        doDeclaration();
    }

    private void doDeclaration() {
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        rc_banner = findViewById(R.id.rc_banner);
        fb_add_banner = findViewById(R.id.fb_add_banner);
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);
        listManager = new LinearLayoutManager(BannerActivity.this, RecyclerView.VERTICAL, false);
        rc_banner.setLayoutManager(listManager);
        rc_banner.setItemAnimator(new DefaultItemAnimator());
        bannerListAdapter = new BannerListAdapter(bannerList, this);
        rc_banner.setAdapter(bannerListAdapter);
        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        layout_swipe_refresh.setOnRefreshListener(this);
        txt_page_title.setText(getString(R.string.txt_banner));
        img_back.setOnClickListener(this);
        fb_add_banner.setOnClickListener(this);
        getBannerList(true);
        callBackFunction();
    }

    private void callBackFunction() {
        bannerListAdapter.setCallback(new BannerListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                String id = bannerList.get(pos).getBannerId();
                Intent i = new Intent(BannerActivity.this, AddBannerActivity.class);
                i.putExtra("BannerID", id);
                i.putExtra("Type", "2");
                startActivityForResult(i, EDIT_CODE);
            }

            @Override
            public void itemDelete(int pos) {
                String id = bannerList.get(pos).getBannerId();
                DeleteBanner(id);
            }
        });
    }

    private void DeleteBanner(String id) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                BannerActivity.this);

        alertDialog2.setTitle("Delete Banner");

        alertDialog2.setMessage(getString(R.string.banner_delete_message));

        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (_fun.isInternetAvailable(BannerActivity.this)) {

                            RetrofitClient.getClient().create(Api.class).getBannerDelete( id, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                    .enqueue(new RetrofitCallBack(BannerActivity.this, deleteAd, true));

                        } else {
                            _fun.ShowNoInternetPopup(BannerActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {

                                    RetrofitClient.getClient().create(Api.class).getBannerDelete(id, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                            .enqueue(new RetrofitCallBack(BannerActivity.this, deleteAd, true));
                                }
                            });

                        }


                    }
                });

        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog2.show();
    }

    private void getBannerList(boolean isLoad) {
        if (_fun.isInternetAvailable(BannerActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getBannerList(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(BannerActivity.this, bannerListResponse, isLoad));
        } else {
            _fun.ShowNoInternetPopup(BannerActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getBannerList(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(BannerActivity.this, bannerListResponse, isLoad));
                }
            });
        }
    }

    retrofit2.Callback<BannerModel> bannerListResponse = new retrofit2.Callback<BannerModel>() {
        @Override
        public void onResponse(Call<BannerModel> call, retrofit2.Response<BannerModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);
            if (layout_swipe_refresh.isRefreshing()) {
                layout_swipe_refresh.setRefreshing(false);
            }
            BannerModel dr = response.body();
            try {
                if (dr.isStatus()) {

                    bannerList.clear();
                    bannerList.addAll(dr.getBannerItemModelArrayList());
                    bannerListAdapter.notifyDataSetChanged();
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
            if (layout_swipe_refresh.isRefreshing()) {
                layout_swipe_refresh.setRefreshing(false);
            }
        }
    };
    retrofit2.Callback<DefaultResponse> deleteAd = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    getBannerList(true);
                } else {
                    Function.CustomMessage(BannerActivity.this, dr.getMessage());
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.fb_add_banner:
                Intent i = new Intent(BannerActivity.this, AddBannerActivity.class);
                i.putExtra("Type", "1");
                startActivityForResult(i, EDIT_CODE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_CODE) {
                getBannerList(true);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);

            }
        }
    }

    @Override
    public void onRefresh() {
        getBannerList(false);
    }
}
