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

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rc_adPost;
    TextView txt_page_title;
    LinearLayout img_back;
    TextView txt_total_ad_count;
    TextView txt_active_ad_count;
    TextView txt_in_active_count;
    NestedScrollView layout_root;
    LinearLayout layout_no_result;
    Button btn_add_post;
    EditText edt_search_ad;
    AdListAdapter adListAdapter;
    ArrayList<AdItemModel> adList = new ArrayList<>();

    int MY_POST_VIEW_CODE = 1299;
    boolean isChanges = false;
    private Function _fun = new Function();

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
        edt_search_ad = findViewById(R.id.edt_search_ad);

        txt_page_title.setText(getString(R.string.nav_dash_board));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_adPost.setLayoutManager(linearLayoutManager);
        rc_adPost.setItemAnimator(new DefaultItemAnimator());
        adListAdapter = new AdListAdapter(adList, getApplicationContext());
        rc_adPost.setAdapter(adListAdapter);
        img_back.setOnClickListener(this);
        btn_add_post.setOnClickListener(this);
//        getUserDetails();

        if (_fun.isInternetAvailable(DashBoardActivity.this)) {
            getAdList();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(DashBoardActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList();
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
//        adListAdapter.setCallback(new AdListAdapter.Callback() {
//            @Override
//            public void itemClick(int pos) {
//                String catId = adList.get(pos).getCatId();
//                String adId = adList.get(pos).getAdId();
//                Intent i = new Intent(DashBoardActivity.this, AddPostActivity.class);
//                i.putExtra("CatId", catId);
//                i.putExtra("AdId", adId);
//                i.putExtra("AdType", 1);
//                startActivity(i);
//            }
//
//            @Override
//            public void deleteClick(int pos) {
//                deletePos = pos;
//                final String catId = adList.get(pos).getCatId();
//                final String adId = adList.get(pos).getAdId();
//
//                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
//                        DashBoardActivity.this);
//
//                alertDialog2.setTitle("Delete Ad");
//
//                alertDialog2.setMessage("Are you sure you want delete this Ad?");
//
//                alertDialog2.setPositiveButton("YES",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                RetrofitClient.getClient().create(Api.class).deletePost(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
//                                        .enqueue(new RetrofitCallBack(DashBoardActivity.this, deleteAd, true));
//                            }
//                        });
//
//                alertDialog2.setNegativeButton("NO",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                dialog.cancel();
//                            }
//                        });
//
//                alertDialog2.show();
//
//
//            }
//        });
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

            }

            @Override
            public void onPromoteClick(AdItemModel model) {
                String catId = model.getCatId();
                String adId = model.getAdId();
                Intent p = new Intent(DashBoardActivity.this, UpgradeActivity.class);
                p.putExtra("PageFrom", "2");
                p.putExtra("CatId", catId);
                p.putExtra("AdId", adId);
                startActivityForResult(p, MY_POST_VIEW_CODE);
            }
        });
    }

    private void getAdList() {
        Log.i("Token", Sessions.getSession(Constant.UserToken, getApplicationContext()));
        RetrofitClient.getClient().create(Api.class).getPost(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(DashBoardActivity.this, adListResponse, true));
    }

    retrofit2.Callback<AdModel> adListResponse = new retrofit2.Callback<AdModel>() {
        @Override
        public void onResponse(Call<AdModel> call, retrofit2.Response<AdModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            AdModel dr = response.body();
            try {
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
                    layout_no_result.setVisibility(View.VISIBLE);
                } else {
                    layout_root.setVisibility(View.VISIBLE);
                    layout_no_result.setVisibility(View.GONE);
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
                    getAdList();
                } else {
                    _fun.ShowNoInternetPopup(DashBoardActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList();
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
}
