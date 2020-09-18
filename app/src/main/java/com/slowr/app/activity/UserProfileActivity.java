package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.adapter.HomeAdListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.OtherProfileModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt_page_title;
    LinearLayout img_back;
    RecyclerView rc_ad_list;
    TextView txt_prosperId_post;
    TextView txt_name;
    TextView txt_phone;
    ImageView img_unverified_user;
    ImageView img_user_profile;

    HomeAdListAdapter homeAdListAdapter;
    ArrayList<AdItemModel> adList = new ArrayList<>();
    HashMap<String, Object> params = new HashMap<String, Object>();

    int favPosition = 0;
    int VIEW_POST_CODE = 1299;
    boolean isChange = false;
    private Function _fun = new Function();
    String prosperId = "";
    String userId = "";
    private PopupWindow spinnerPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        doDeclaration();
    }

    private void doDeclaration() {
        prosperId = getIntent().getStringExtra("prosperId");
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        rc_ad_list = findViewById(R.id.rc_ad_list);
        txt_prosperId_post = findViewById(R.id.txt_prosperId_post);
        img_unverified_user = findViewById(R.id.img_unverified_user);
        txt_name = findViewById(R.id.txt_name);
        txt_phone = findViewById(R.id.txt_phone);
        img_user_profile = findViewById(R.id.img_user_profile);
        txt_page_title.setText("Profile");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_ad_list.setLayoutManager(linearLayoutManager);
        rc_ad_list.setItemAnimator(new DefaultItemAnimator());
        homeAdListAdapter = new HomeAdListAdapter(adList, getApplicationContext());
        rc_ad_list.setAdapter(homeAdListAdapter);
        if (_fun.isInternetAvailable(UserProfileActivity.this)) {
            getAdList();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(UserProfileActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList();
                        }
                    });
                }
            }, 200);

        }

        callBackFunction();
        img_back.setOnClickListener(this);
        img_unverified_user.setOnClickListener(this);
        txt_prosperId_post.setOnClickListener(this);

    }

    private void getAdList() {
        RetrofitClient.getClient().create(Api.class).getUserAdDetails(prosperId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(UserProfileActivity.this, adListResponse, true));
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
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    if (!adList.get(pos).isProgress()) {
                        favPosition = pos;
                        adList.get(pos).setProgress(true);
                        if (_fun.isInternetAvailable(UserProfileActivity.this)) {
                            callAddFavorite();
                        } else {
                            _fun.ShowNoInternetPopup(UserProfileActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {
                                    callAddFavorite();
                                }
                            });

                        }

                    }

                } else {
                    Function.CustomMessage(UserProfileActivity.this, getString(R.string.txt_please_login));
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
                    .enqueue(new RetrofitCallBack(UserProfileActivity.this, addFavorite, true));
        } else {
            RetrofitClient.getClient().create(Api.class).deleteFavorite(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UserProfileActivity.this, addFavorite, true));
        }
    }

    void changeFragment(String catId, String adId, String userId) {
        if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            Intent p = new Intent(UserProfileActivity.this, MyPostViewActivity.class);
            p.putExtra("CatId", catId);
            p.putExtra("AdId", adId);
            startActivityForResult(p, VIEW_POST_CODE);
        } else {
            Intent p = new Intent(UserProfileActivity.this, PostViewActivity.class);
            p.putExtra("CatId", catId);
            p.putExtra("AdId", adId);
            startActivityForResult(p, VIEW_POST_CODE);
        }


//        Intent p = new Intent(FavoriteActivity.this, PostViewActivity.class);
//        p.putExtra("CatId", catId);
//        p.putExtra("AdId", adId);
//        startActivityForResult(p, VIEW_POST_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_unverified_user:
                ShowPopupProsper();
                break;
            case R.id.txt_prosperId_post:
                ShowPopupProsper();
                break;

        }
    }

    retrofit2.Callback<OtherProfileModel> adListResponse = new retrofit2.Callback<OtherProfileModel>() {
        @Override
        public void onResponse(Call<OtherProfileModel> call, retrofit2.Response<OtherProfileModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            OtherProfileModel dr = response.body();
            try {
                if (dr.isStatus()) {

//                    Log.i("ImagePathUrl", dr.getUrlPath());
//                    Sessions.saveSession(Constant.ImagePath, dr.getUrlPath(), FavoriteActivity.this);
                    userId = dr.getUserDetailsModel().getUserId();
                    txt_prosperId_post.setText(dr.getUserDetailsModel().getProsperId());
                    txt_name.setText(dr.getUserDetailsModel().getUserName());
                    txt_phone.setText(dr.getUserDetailsModel().getUserPhone());
                    if (dr.getUserDetailsModel().getIsProfileVerified().equals("0")) {
                        img_unverified_user.setVisibility(View.VISIBLE);
                    } else {
                        img_unverified_user.setVisibility(View.GONE);
                    }
                    Glide.with(UserProfileActivity.this)
                            .load(dr.getUserDetailsModel().getUserPhoto())
                            .circleCrop()
                            .placeholder(R.drawable.ic_default_profile)
                            .error(R.drawable.ic_default_profile)
                            .into(img_user_profile);
                    adList.clear();
                    adList.addAll(dr.getAdList());
                    homeAdListAdapter.notifyDataSetChanged();


                } else {
                    Function.CustomMessage(UserProfileActivity.this, dr.getMessage());

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
                        homeAdListAdapter.notifyItemChanged(favPosition);
                    } else {
                        adList.get(favPosition).setIsFavorite("0");
                        homeAdListAdapter.notifyItemChanged(favPosition);
                    }
                    Function.CustomMessage(UserProfileActivity.this, dr.getMessage());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    isChange = true;

                } else {
                    Function.CustomMessage(UserProfileActivity.this, dr.getMessage());
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == VIEW_POST_CODE) {
                if (_fun.isInternetAvailable(UserProfileActivity.this)) {
                    getAdList();
                } else {
                    _fun.ShowNoInternetPopup(UserProfileActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getAdList();
                        }
                    });

                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                isChange = false;
            }
        }
    }

    public void ShowPopupProsper() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_popup_unverified_user, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        TextView txt_content_two = view.findViewById(R.id.txt_content_two);
        TextView txt_content = view.findViewById(R.id.txt_content);
        TextView txt_skip = view.findViewById(R.id.txt_skip);
        LinearLayout layout_content_one = view.findViewById(R.id.layout_content_one);
        final Button btn_ok = view.findViewById(R.id.btn_ok);
        if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            txt_content_two.setText(getString(R.string.txt_unverified_own));
            txt_skip.setVisibility(View.VISIBLE);
            btn_ok.setText(getString(R.string.txt_verify));
        } else {
            layout_content_one.setVisibility(View.VISIBLE);
            txt_content.setText(getString(R.string.txt_unverified_other));
            txt_content_two.setText(getString(R.string.txt_unverified_other_two));
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_ok.getText().toString().equals(getString(R.string.txt_verify))) {
                    Intent profile = new Intent(UserProfileActivity.this, ProfileActivity.class);
                    startActivityForResult(profile, VIEW_POST_CODE);
                }
                spinnerPopup.dismiss();
            }
        });
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
