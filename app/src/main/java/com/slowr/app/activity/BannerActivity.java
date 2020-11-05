package com.slowr.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
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

    TextView txt_preview_title;
    TextView txt_prosperId;
    TextView txt_preview_description;
    ImageView img_banner_preview;
    Button btn_edit;
    Button btn_delete;
    LinearLayout layout_banner_bg;
    LinearLayout layout_preview;
    FrameLayout layout_list;

    private Function _fun = new Function();

    int EDIT_CODE = 1299;
    String bannerId = "";
    boolean isPreview = false;

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
        txt_preview_title = findViewById(R.id.txt_preview_title);
        txt_prosperId = findViewById(R.id.txt_prosperId);
        txt_preview_description = findViewById(R.id.txt_preview_description);
        img_banner_preview = findViewById(R.id.img_banner_preview);
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        layout_preview = findViewById(R.id.layout_preview);
        layout_list = findViewById(R.id.layout_list);
        layout_banner_bg = findViewById(R.id.layout_banner_bg);


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
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        getBannerList(true);
        callBackFunction();
    }

    private void callBackFunction() {
        bannerListAdapter.setCallback(new BannerListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                bannerId = bannerList.get(pos).getBannerId();
                layout_list.setVisibility(View.GONE);
                layout_preview.setVisibility(View.VISIBLE);
                isPreview = true;
                txt_preview_title.setText(bannerList.get(pos).getBannerTitle());
                txt_preview_description.setText(bannerList.get(pos).getDescription());
                txt_prosperId.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
                layout_banner_bg.setBackgroundColor(Color.parseColor(bannerList.get(pos).getColorCode()));
                Glide.with(BannerActivity.this)
                        .load(bannerList.get(pos).getBannerImage())
                        .placeholder(R.drawable.ic_default_vertical)
                        .error(R.drawable.ic_default_vertical)
                        .into(img_banner_preview);
                if (bannerList.get(pos).getBannerStatus().equals("1")) {
                    btn_edit.setText(getString(R.string.txt_edit));
                } else if (bannerList.get(pos).getBannerStatus().equals("3")) {
                    btn_edit.setText(getString(R.string.txt_renew));
                } else {
                    btn_edit.setText(getString(R.string.txt_edit));
                }
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

                            RetrofitClient.getClient().create(Api.class).getBannerDelete(id, Sessions.getSession(Constant.UserToken, getApplicationContext()))
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
                    isPreview = false;
                    layout_list.setVisibility(View.VISIBLE);
                    layout_preview.setVisibility(View.GONE);
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
                if (isPreview) {
                    isPreview = false;
                    layout_list.setVisibility(View.VISIBLE);
                    layout_preview.setVisibility(View.GONE);
                } else {
                    finish();
                }

                break;
            case R.id.fb_add_banner:
                Intent i = new Intent(BannerActivity.this, AddBannerActivity.class);
                i.putExtra("Type", "1");
                startActivityForResult(i, EDIT_CODE);
                break;
            case R.id.btn_edit:
                Intent e = new Intent(BannerActivity.this, AddBannerActivity.class);
                e.putExtra("BannerID", bannerId);
                e.putExtra("Type", "2");
                startActivityForResult(e, EDIT_CODE);
//                isPreview = false;
//                layout_list.setVisibility(View.VISIBLE);
//                layout_preview.setVisibility(View.GONE);
                break;
            case R.id.btn_delete:
                DeleteBanner(bannerId);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isPreview) {
            isPreview = false;
            layout_list.setVisibility(View.VISIBLE);
            layout_preview.setVisibility(View.GONE);
        } else {
            finish();
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_CODE) {
                getBannerList(true);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                isPreview = false;
                layout_list.setVisibility(View.VISIBLE);
                layout_preview.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRefresh() {
        getBannerList(false);
    }
}
