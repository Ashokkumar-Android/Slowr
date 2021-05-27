package com.slowr.app.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.slowr.app.models.BannerDetailsModel;
import com.slowr.app.models.BannerItemModel;
import com.slowr.app.models.BannerModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.EditBannerModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

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
    Button btn_add_banner;
    LinearLayout layout_banner_bg;
    LinearLayout layout_preview;
    FrameLayout layout_list;
    LinearLayout layout_no_result;
    TextView txt_no_banner_content_one;
    TextView txt_no_banner_content_two;

    private Function _fun = new Function();

    int EDIT_CODE = 1299;
    String bannerId = "";
    boolean isPreview = false;
    boolean isAddBanner = false;

    String PageFrom = "";
    String NotificationId = "";

    HashMap<String, String> params = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        doDeclaration();
    }

    private void doDeclaration() {
        if (getIntent().hasExtra("PageFrom")) {
            PageFrom = getIntent().getStringExtra("PageFrom");
            NotificationId = getIntent().getStringExtra("NotificationId");
            NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            ReadNotification(NotificationId);
        }
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
        layout_no_result = findViewById(R.id.layout_no_result);
        btn_add_banner = findViewById(R.id.btn_add_banner);
        txt_no_banner_content_one = findViewById(R.id.txt_no_banner_content_one);
        txt_no_banner_content_two = findViewById(R.id.txt_no_banner_content_two);


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
        btn_add_banner.setOnClickListener(this);
        getBannerList(true);
        callBackFunction();
    }

    private void callBackFunction() {
        bannerListAdapter.setCallback(new BannerListAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                try {
                    bannerId = bannerList.get(pos).getBannerId();
                    getBannerDetails(bannerId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void itemDelete(int pos) {
                String id = bannerList.get(pos).getBannerId();
                DeleteBanner(id);
            }
        });
    }

    private void getBannerDetails(String _bannerId) {
        try {
            if (_fun.isInternetAvailable(BannerActivity.this)) {
                RetrofitClient.getClient().create(Api.class).getBannerDetails(_bannerId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                        .enqueue(new RetrofitCallBack(BannerActivity.this, bannerDetailsResponse, true, false));
            } else {
                _fun.ShowNoInternetPopup(BannerActivity.this, new Function.NoInternetCallBack() {
                    @Override
                    public void isInternet() {
                        RetrofitClient.getClient().create(Api.class).getBannerDetails(_bannerId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                .enqueue(new RetrofitCallBack(BannerActivity.this, bannerDetailsResponse, true, false));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                                    .enqueue(new RetrofitCallBack(BannerActivity.this, deleteAd, true, false));

                        } else {
                            _fun.ShowNoInternetPopup(BannerActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {

                                    RetrofitClient.getClient().create(Api.class).getBannerDelete(id, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                            .enqueue(new RetrofitCallBack(BannerActivity.this, deleteAd, true, false));
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
                    .enqueue(new RetrofitCallBack(BannerActivity.this, bannerListResponse, isLoad, false));
        } else {
            _fun.ShowNoInternetPopup(BannerActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getBannerList(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(BannerActivity.this, bannerListResponse, isLoad, false));
                }
            });
        }
    }

    private void ReadNotification(String noteId) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("notification_id", noteId);
        Log.i("Params", params.toString());

        if (_fun.isInternetAvailable(BannerActivity.this)) {
            RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(BannerActivity.this, noteReadResponse, false, false));
        } else {
            _fun.ShowNoInternetPopup(BannerActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(BannerActivity.this, noteReadResponse, false, false));
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
                isAddBanner = dr.isCreateBanner();
                if (dr.isStatus()) {

                    bannerList.clear();
                    bannerList.addAll(dr.getBannerItemModelArrayList());
                    bannerListAdapter.notifyDataSetChanged();
                } else {

                }
                if (!isAddBanner) {
                    txt_no_banner_content_one.setText(getString(R.string.txt_no_ad_banner));
                    txt_no_banner_content_two.setText(getString(R.string.txt_no_ad_content));
                    btn_add_banner.setText(getString(R.string.post_ad));
//                    fb_add_banner.setVisibility(View.GONE);
                }
                if (bannerList.size() == 0) {
                    layout_list.setVisibility(View.GONE);
                    layout_no_result.setVisibility(View.VISIBLE);
                } else {
                    layout_list.setVisibility(View.VISIBLE);
                    layout_no_result.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (bannerList.size() == 0) {
                    layout_list.setVisibility(View.GONE);
                    layout_no_result.setVisibility(View.VISIBLE);
                } else {
                    layout_list.setVisibility(View.VISIBLE);
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
            if (bannerList.size() == 0) {
                layout_list.setVisibility(View.GONE);
                layout_no_result.setVisibility(View.VISIBLE);
            } else {
                layout_list.setVisibility(View.VISIBLE);
                layout_no_result.setVisibility(View.GONE);
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
    retrofit2.Callback<DefaultResponse> noteReadResponse = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            DefaultResponse dr = response.body();
            try {
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
    retrofit2.Callback<EditBannerModel> bannerDetailsResponse = new retrofit2.Callback<EditBannerModel>() {
        @Override
        public void onResponse(Call<EditBannerModel> call, retrofit2.Response<EditBannerModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            EditBannerModel dr = response.body();
            try {
                if (dr.isStatus()) {
                    layout_list.setVisibility(View.GONE);
                    layout_preview.setVisibility(View.VISIBLE);
                    isPreview = true;
                    BannerDetailsModel detailsModel = dr.getEditBannerDataModel().getBannerDetailsModel();
                    txt_preview_title.setText(detailsModel.getBannerTitle());
                    txt_preview_description.setText(detailsModel.getDescription());
                    txt_prosperId.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
//                layout_banner_bg.setBackgroundColor(Color.parseColor(bannerList.get(pos).getColorCode()));
                    String[] col = detailsModel.getColorCode().split(",");
                    Function.GradientBgSet(layout_banner_bg, col[0], col[1]);
                    Glide.with(BannerActivity.this)
                            .load(dr.getEditBannerDataModel().getBannerImage())
                            .placeholder(R.drawable.ic_no_image)
                            .error(R.drawable.ic_no_image)
                            .into(img_banner_preview);
                    if (detailsModel.getAdStatus().equals("1")) {
                        btn_edit.setText(getString(R.string.txt_edit));
                    } else if (detailsModel.getAdStatus().equals("3")) {
                        btn_edit.setText(getString(R.string.txt_renew));
                    } else {
                        btn_edit.setText(getString(R.string.txt_edit));
                    }

                    if (detailsModel.getAdStatus().equals("9") || detailsModel.getAdStatus().equals("7")) {
                        btn_edit.setVisibility(View.GONE);
                    } else {
                        btn_edit.setVisibility(View.VISIBLE);
                    }

                } else {
                    Function.CustomMessage(BannerActivity.this, dr.getMessage());
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
                if (isPreview) {
                    isPreview = false;
                    layout_list.setVisibility(View.VISIBLE);
                    layout_preview.setVisibility(View.GONE);
                } else {
                    if (PageFrom.equals("2")) {
                        Intent h = new Intent(BannerActivity.this, HomeActivity.class);
                        h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(h);
                    }
                    finish();
                }

                break;
            case R.id.fb_add_banner:
                if (isAddBanner) {
                    Intent i = new Intent(BannerActivity.this, AddBannerActivity.class);
                    i.putExtra("Type", "1");
                    startActivityForResult(i, EDIT_CODE);
                } else {
                    if (bannerList.size() != 0)
                        Function.CustomMessage(BannerActivity.this, getString(R.string.txt_no_ad_banner));
                    Intent p = new Intent(BannerActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    startActivity(p);
                }
                break;
            case R.id.btn_add_banner:
                fb_add_banner.performClick();
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
            if (PageFrom.equals("2")) {
                Intent h = new Intent(BannerActivity.this, HomeActivity.class);
                h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(h);
            }
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
        Function.CoinTone(BannerActivity.this);
        getBannerList(false);
    }
}
