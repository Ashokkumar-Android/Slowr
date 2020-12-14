package com.slowr.app.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.slowr.app.R;
import com.slowr.app.adapter.PopularAdListAdapter;
import com.slowr.app.adapter.ViewPostImageListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.chat.ChatActivity;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.EditAdDetailsModel;
import com.slowr.app.models.EditAdModel;
import com.slowr.app.models.UploadImageModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;

import retrofit2.Call;

public class PostViewActivity extends BaseActivity implements View.OnClickListener, OnLikeListener {

    TextView txt_ad_title;
    TextView txt_price;
    TextView txt_location_post;
    TextView txt_like_count;
    TextView txt_description;
    TextView txt_prosperId_post;
    TextView txt_phone;
    TextView txt_view_profile;
    TextView txt_guid_line;
    Button btn_chat_now;
    Button btn_call_now;
    ImageView img_ad_view;
    RecyclerView rc_related_ad_list;
    RecyclerView rc_image_list;
    ImageView img_share;
    LikeButton img_favorite;
    ImageView img_like;
    ImageView img_user_profile;
    ImageView img_unverified_user;
    LinearLayout layout_like;
    CardView layout_relative_ad;
    CardView layout_guide_lines;
    LinearLayout layout_promoted;
    ImageView img_top_page_mark;
    ImageView txt_premium_mark;
    LinearLayout layout_root;

    ArrayList<AdItemModel> relatedAdList = new ArrayList<>();
    ArrayList<UploadImageModel> shareImageList = new ArrayList<>();
    PopularAdListAdapter popularAdListAdapter;
    ViewPostImageListAdapter postImageListAdapter;
    private PopupWindow spinnerPopup;
    String catId = "";
    String adId = "";
    String userProUrl = "";
    String userPhone = "";
    String userId = "";
    String isFavorite = "0";
    String isLike = "0";
    int likeCount = 0;
    String PageFrom = "";
    boolean unVerified = false;
    String userProsperId = "";
    String chatId = "";
    String adTitle = "";
    String catGroup = "";
    String adShareUrl = "";

    View rootView = null;
    String imageStringArray = "";
    int imgSelectPos = 0;
    boolean isPageChange = false;
    private Function _fun = new Function();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.fragment_post_view, contentFrameLayout);

        doDeclaration();
    }

    private void doDeclaration() {
        catId = getIntent().getStringExtra("CatId");
        adId = getIntent().getStringExtra("AdId");
        if (getIntent().hasExtra("PageFrom")) {
            PageFrom = getIntent().getStringExtra("PageFrom");
        }
        txt_ad_title = findViewById(R.id.txt_ad_title);
        txt_price = findViewById(R.id.txt_price);
        txt_location_post = findViewById(R.id.txt_location_post);
        txt_like_count = findViewById(R.id.txt_like_count);
        btn_chat_now = findViewById(R.id.btn_chat_now);
        txt_description = findViewById(R.id.txt_description);
        txt_prosperId_post = findViewById(R.id.txt_prosperId_post);
        txt_phone = findViewById(R.id.txt_phone);
        txt_view_profile = findViewById(R.id.txt_view_profile);
        rc_related_ad_list = findViewById(R.id.rc_related_ad_list);
        rc_image_list = findViewById(R.id.rc_image_list);
        img_ad_view = findViewById(R.id.img_ad_view);
        img_share = findViewById(R.id.img_share);
        img_favorite = findViewById(R.id.img_favorite);
        layout_like = findViewById(R.id.layout_like);
        img_like = findViewById(R.id.img_like);
        layout_relative_ad = findViewById(R.id.layout_relative_ad);
        layout_guide_lines = findViewById(R.id.layout_guide_lines);
        layout_promoted = findViewById(R.id.layout_promoted);
        img_top_page_mark = findViewById(R.id.img_top_page_mark);
        txt_premium_mark = findViewById(R.id.txt_premium_mark);
        img_user_profile = findViewById(R.id.img_user_profile);
        layout_root = findViewById(R.id.layout_root);
        img_unverified_user = findViewById(R.id.img_unverified_user);
        btn_call_now = findViewById(R.id.btn_call_now);
        txt_guid_line = findViewById(R.id.txt_guid_line);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(PostViewActivity.this, RecyclerView.HORIZONTAL, false);
        rc_related_ad_list.setLayoutManager(linearLayoutManager3);
        rc_related_ad_list.setItemAnimator(new DefaultItemAnimator());
        popularAdListAdapter = new PopularAdListAdapter(relatedAdList, PostViewActivity.this);
        rc_related_ad_list.setAdapter(popularAdListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostViewActivity.this, RecyclerView.HORIZONTAL, false);
        rc_image_list.setLayoutManager(linearLayoutManager);
        rc_image_list.setItemAnimator(new DefaultItemAnimator());
        postImageListAdapter = new ViewPostImageListAdapter(PostViewActivity.this, shareImageList);
        rc_image_list.setAdapter(postImageListAdapter);
        img_favorite.setOnLikeListener(this);
        img_share.setOnClickListener(this);
        layout_like.setOnClickListener(this);
        img_ad_view.setOnClickListener(this);
        img_user_profile.setOnClickListener(this);
        img_unverified_user.setOnClickListener(this);
        btn_call_now.setOnClickListener(this);
        txt_prosperId_post.setOnClickListener(this);
        txt_view_profile.setOnClickListener(this);
        btn_chat_now.setOnClickListener(this);
        CallBackFunction();
        if (_fun.isInternetAvailable(PostViewActivity.this)) {
            GetAdDetails();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(PostViewActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            GetAdDetails();
                        }
                    });
                }
            }, 200);

        }


    }

    private void CallBackFunction() {
        postImageListAdapter.setCallBack(new ViewPostImageListAdapter.CallBack() {
            @Override
            public void itemClick(int pos) {
                imgSelectPos = pos;
                for (int i = 0; i < shareImageList.size(); i++) {
                    if (i == pos) {
                        shareImageList.get(i).setChanged(true);
                    } else {
                        shareImageList.get(i).setChanged(false);
                    }
                }
                postImageListAdapter.notifyDataSetChanged();
                setCurrentImage(shareImageList.get(pos).getImgURL());
            }
        });
        popularAdListAdapter.setCallback(new PopularAdListAdapter.Callback() {
            @Override
            public void itemClick(AdItemModel model) {
                String catId = model.getCatId();
                String adId = model.getAdId();
                String userId = model.getUserId();
                changeFragment(catId, adId, userId);
            }
        });
    }

    void changeFragment(String catId, String adId, String _userId) {

        if (_userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            Intent p = new Intent(PostViewActivity.this, MyPostViewActivity.class);
            p.putExtra("CatId", catId);
            p.putExtra("AdId", adId);
            startActivity(p);
        } else {
            Intent p = new Intent(PostViewActivity.this, PostViewActivity.class);
            p.putExtra("CatId", catId);
            p.putExtra("AdId", adId);
            startActivity(p);
        }
    }

    private void setCurrentImage(String url) {
        int defu = R.drawable.ic_no_image;

        if (catGroup.equals("1")) {
            defu = R.drawable.ic_no_image;
        } else {
            defu = R.drawable.ic_service_big;
        }
        Glide.with(this)
                .load(url)
                .placeholder(defu)
                .error(defu)
                .into(img_ad_view);
    }

    private void GetAdDetails() {
        Log.i("CATAD", catId + " , " + adId);
        RetrofitClient.getClient().create(Api.class).getHomeAdDetails(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(PostViewActivity.this, adDetails, true));

    }

    retrofit2.Callback<EditAdModel> adDetails = new retrofit2.Callback<EditAdModel>() {
        @Override
        public void onResponse(Call<EditAdModel> call, retrofit2.Response<EditAdModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                EditAdModel dr = response.body();
                if (response.code() == 200) {
                    if (dr.isStatus()) {
                        if (dr.getEditDataModel().getAdDetailsModel() != null) {
                            layout_root.setVisibility(View.VISIBLE);

                            EditAdDetailsModel editAdDetailsModel = dr.getEditDataModel().getAdDetailsModel();

                            catGroup = dr.getEditDataModel().getCatGroup();
//                            Sessions.saveSession(Constant.ImagePath, dr.getEditDataModel().getUrlPath(), PostViewActivity.this);
                            chatId = dr.getEditDataModel().getChatId();
                            txt_ad_title.setText(editAdDetailsModel.getAdTitle().trim());
                            adTitle = editAdDetailsModel.getAdTitle().trim();
                            isFavorite = editAdDetailsModel.getIsFavorite();
                            txt_like_count.setText(editAdDetailsModel.getLikeCount());
                            likeCount = Integer.valueOf(editAdDetailsModel.getLikeCount());
                            isLike = editAdDetailsModel.getIsLike();
                            txt_location_post.setText(editAdDetailsModel.getAreaName() + ", " + editAdDetailsModel.getCityName());
                            if (isFavorite.equals("0")) {
                                img_favorite.setLiked(false);
                            } else {
                                img_favorite.setLiked(true);
                            }
                            if (isLike.equals("0")) {
                                img_like.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                            } else {
                                img_like.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.txt_orange));
                            }
                            if (editAdDetailsModel.getRentalFee() != null) {
                                txt_price.setVisibility(View.VISIBLE);
                                String price = "";
                                if (editAdDetailsModel.getRentalFee().contains(".")) {
                                    String[] tempPrice = editAdDetailsModel.getRentalFee().split("\\.");
                                    price = tempPrice[0];
                                } else {
                                    price = editAdDetailsModel.getRentalFee();
                                }

                                if (price.equals("0") || editAdDetailsModel.getRentalDuration().equals("Custom")) {
                                    if (catGroup.equals("1")) {
                                        txt_price.setText(getString(R.string.custom_rent));
                                    } else {
                                        txt_price.setText(getString(R.string.custom_hire));
                                    }
                                } else {
                                    txt_price.setText("₹ " + price + " / " + editAdDetailsModel.getRentalDuration());
                                }

                            } else {
                                if (catGroup.equals("1")) {
                                    if (editAdDetailsModel.getRentalDuration().equals("Custom")) {
                                        txt_price.setText(getString(R.string.custom_rent));
                                    } else if (editAdDetailsModel.getRentalDuration().equals("Per Hour")) {
                                        txt_price.setText(getString(R.string.hour_rent));
                                    } else if (editAdDetailsModel.getRentalDuration().equals("Per Day")) {
                                        txt_price.setText(getString(R.string.day_rent));
                                    }
                                } else {
                                    if (editAdDetailsModel.getRentalDuration().equals("Custom")) {
                                        txt_price.setText(getString(R.string.custom_hire));
                                    } else if (editAdDetailsModel.getRentalDuration().equals("Per Hour")) {
                                        txt_price.setText(getString(R.string.hour_hire));
                                    } else if (editAdDetailsModel.getRentalDuration().equals("Per Day")) {
                                        txt_price.setText(getString(R.string.day_hire));
                                    }
                                }
                            }

                            txt_description.setText(editAdDetailsModel.getDescription());
                            userProsperId = dr.getEditDataModel().getUserDetailsModel().getProsperId();
                            Log.i("ProsperId", userProsperId);
                            txt_prosperId_post.setText(userProsperId);
                            userProUrl = dr.getEditDataModel().getUserDetailsModel().getUserPhoto();
                            userId = dr.getEditDataModel().getUserDetailsModel().getUserId();
                            Glide.with(PostViewActivity.this)
                                    .load(dr.getEditDataModel().getUserDetailsModel().getUserPhoto())
                                    .circleCrop()
                                    .placeholder(R.drawable.ic_default_profile)
                                    .error(R.drawable.ic_default_profile)
                                    .into(img_user_profile);
                            if (dr.getEditDataModel().getUserDetailsModel().getIsProfileVerified().equals("0")) {
                                img_unverified_user.setVisibility(View.GONE);
                                unVerified = true;
                            } else {
                                img_unverified_user.setVisibility(View.GONE);
                            }
//                            if (editAdDetailsModel.getIsMobileVisible().equals("1")) {
//                                txt_phone.setVisibility(View.GONE);
//                            } else {
                            txt_phone.setVisibility(View.VISIBLE);
                            txt_phone.setText(dr.getEditDataModel().getUserDetailsModel().getUserPhone());
                            userPhone = dr.getEditDataModel().getUserDetailsModel().getUserPhone();
                            if (userPhone.equals("")) {
                                btn_call_now.setVisibility(View.GONE);
                            }
//                            }

                            shareImageList.clear();
                            for (int p = 0; p < dr.getEditDataModel().getAdImage().size(); p++) {
                                if (p == 0) {
                                    shareImageList.add(new UploadImageModel(dr.getEditDataModel().getAdImage().get(p), true, "", ""));
                                    imageStringArray = dr.getEditDataModel().getAdImage().get(p);
                                    imgSelectPos = p;
                                } else {
                                    shareImageList.add(new UploadImageModel(dr.getEditDataModel().getAdImage().get(p), false, "", ""));
                                    imageStringArray = imageStringArray + "," + dr.getEditDataModel().getAdImage().get(p);
                                }
                            }
                            if (shareImageList.size() != 0) {
                                rc_image_list.setVisibility(View.VISIBLE);
                                setCurrentImage(shareImageList.get(0).getImgURL());
                                adShareUrl = shareImageList.get(0).getImgURL();
                            } else {
                                setCurrentImage("");
                                rc_image_list.setVisibility(View.GONE);
                            }
                            postImageListAdapter.notifyDataSetChanged();
                            relatedAdList.clear();
                            if (dr.getEditDataModel().getAdList() != null)
                                relatedAdList.addAll(dr.getEditDataModel().getAdList());
                            popularAdListAdapter.notifyDataSetChanged();
                            if (relatedAdList.size() != 0) {
                                layout_relative_ad.setVisibility(View.VISIBLE);
                            } else {
                                layout_relative_ad.setVisibility(View.GONE);
                            }
                            if (editAdDetailsModel.getAdPromotion().equals("1")) {
                                layout_promoted.setVisibility(View.VISIBLE);
                                txt_premium_mark.setVisibility(View.GONE);
                                img_top_page_mark.setVisibility(View.VISIBLE);
                            } else if (editAdDetailsModel.getAdPromotion().equals("2")) {
                                layout_promoted.setVisibility(View.VISIBLE);
                                txt_premium_mark.setVisibility(View.VISIBLE);
                                img_top_page_mark.setVisibility(View.GONE);
                            } else {
                                layout_promoted.setVisibility(View.INVISIBLE);
                            }
//                            String guideLineText = "";
//                            for (int g = 0; g < dr.getEditDataModel().getGuideLines().size(); g++) {
//                                if (guideLineText.equals("")) {
//                                    guideLineText = "\u2022 " + dr.getEditDataModel().getGuideLines().get(g);
//                                } else {
//                                    guideLineText = guideLineText + "\n\u2022 " + dr.getEditDataModel().getGuideLines().get(g);
//                                }
//                            }
                            if (dr.getEditDataModel().getGuideLines() != null && dr.getEditDataModel().getGuideLines().size() != 0)
                                txt_guid_line.setText(makeBulletList(dr.getEditDataModel().getGuideLines()));
                        }
                    } else {
                        Function.CustomMessage(PostViewActivity.this, dr.getMessage());
                    }
                } else if (response.code() == 500) {
                    finish();
                    Function.CustomMessage(PostViewActivity.this, "Try again.");
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

    public static CharSequence makeBulletList(ArrayList<String> lines) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            CharSequence line = lines.get(i) + (i < lines.size() - 1 ? "\n" : "");
            Spannable spannable = new SpannableString(line);
            spannable.setSpan(new BulletSpan(5), 0, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.append(spannable);
        }
        return sb;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (PageFrom.equals("2")) {
            Intent h = new Intent(PostViewActivity.this, HomeActivity.class);
            h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(h);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_share:
                Function.ShareLink(PostViewActivity.this, catId, adId, adTitle, catGroup,adShareUrl);
                break;
            case R.id.img_favorite:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {

                    if (_fun.isInternetAvailable(PostViewActivity.this)) {
                        callAddFavorite();
                    } else {
                        _fun.ShowNoInternetPopup(PostViewActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                callAddFavorite();
                            }
                        });
                    }
                } else {
                    Function.CustomMessage(PostViewActivity.this, getString(R.string.txt_please_login));
                }
                break;
            case R.id.layout_like:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    if (_fun.isInternetAvailable(PostViewActivity.this)) {
                        callAddLike();
                    } else {
                        _fun.ShowNoInternetPopup(PostViewActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                callAddLike();
                            }
                        });
                    }
                } else {
                    Function.CustomMessage(PostViewActivity.this, getString(R.string.txt_please_login));
                }

                break;
            case R.id.img_ad_view:
                if (!isPageChange) {
                    isPageChange = true;
                    Intent i = new Intent(PostViewActivity.this, ImageViewActivity.class);
                    i.putExtra("ImgURL", imageStringArray);
                    i.putExtra("ImgPos", imgSelectPos);
                    startActivity(i);
                }
                break;
            case R.id.img_user_profile:
                if (!isPageChange) {
                    isPageChange = true;
                    Intent a = new Intent(PostViewActivity.this, ImageViewActivity.class);
                    a.putExtra("ImgURL", userProUrl);
                    a.putExtra("ImgPos", 0);
                    startActivity(a);
                }
                break;
            case R.id.img_unverified_user:
//                ShowPopupProsper();
                break;
            case R.id.btn_call_now:
                if (_fun.checkPermission2(PostViewActivity.this))
                    Function.CallNow(PostViewActivity.this, userPhone);

                break;
            case R.id.txt_prosperId_post:
                if (unVerified) {
//                    ShowPopupProsper();
                }
                break;
            case R.id.txt_view_profile:
                Intent i = new Intent(PostViewActivity.this, UserProfileActivity.class);
                i.putExtra("prosperId", userProsperId);
                startActivity(i);
                break;
            case R.id.btn_chat_now:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent c = new Intent(PostViewActivity.this, ChatActivity.class);
                    c.putExtra("CatId", catId);
                    c.putExtra("AdId", adId);
                    c.putExtra("RenterId", userId);
                    c.putExtra("ProsperId", userProsperId);
                    c.putExtra("ProURL", userProUrl);
                    c.putExtra("LastId", chatId);
                    c.putExtra("UnVerified", unVerified);
                    startActivity(c);
                } else {
                    Function.CustomMessage(PostViewActivity.this, getString(R.string.txt_please_login));
                }
                break;
        }
    }

    private void callAddFavorite() {
        img_favorite.setEnabled(false);
        if (isFavorite.equals("0")) {
            if (!params.isEmpty()) {
                params.clear();
            }
            params.put("ads_id", adId);
            params.put("category_id", catId);
            Log.i("Params", params.toString());
            RetrofitClient.getClient().create(Api.class).addFavorite(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(PostViewActivity.this, addFavorite, true));
        } else {
            RetrofitClient.getClient().create(Api.class).deleteFavorite(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(PostViewActivity.this, addFavorite, true));
        }
    }

    private void callAddLike() {

        if (isLike.equals("0")) {
            layout_like.setEnabled(false);
            if (!params.isEmpty()) {
                params.clear();
            }
            params.put("ads_id", adId);
            params.put("category_id", catId);
            Log.i("Params", params.toString());
            RetrofitClient.getClient().create(Api.class).addLike(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(PostViewActivity.this, addLike, true));
        } else {
            Function.CustomMessage(PostViewActivity.this, getString(R.string.txt_already_liked));
//            RetrofitClient.getClient().create(Api.class).deleteLike(catId, adId, "Bearer " + Sessions.getSession(Constant.UserToken, getApplicationContext()))
//                    .enqueue(new RetrofitCallBack(PostViewActivity.this, addLike, true));
        }
    }

    retrofit2.Callback<DefaultResponse> addFavorite = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    if (isFavorite.equals("0")) {
                        isFavorite = "1";
//                        img_favorite.setImageResource(R.drawable.ic_fav_select);
                    } else {
                        isFavorite = "0";
//                        img_favorite.setImageResource(R.drawable.ic_favorite);
                    }
                    Function.CustomMessage(PostViewActivity.this, dr.getMessage());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                } else {
                    Function.CustomMessage(PostViewActivity.this, dr.getMessage());
                }

                img_favorite.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
                img_favorite.setEnabled(true);
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            img_favorite.setEnabled(true);
            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };

    retrofit2.Callback<DefaultResponse> addLike = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    if (isLike.equals("0")) {
                        isLike = "1";
                        img_like.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.txt_orange));
                        likeCount++;
                        txt_like_count.setText(String.valueOf(likeCount));
                    } else {
                        isLike = "0";
                        img_like.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        likeCount--;
                        if (likeCount != 0) {
                            txt_like_count.setText(String.valueOf(likeCount));
                        } else {
                            txt_like_count.setText("");
                        }
                    }
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                } else {
                    Function.CustomMessage(PostViewActivity.this, dr.getMessage());
                }
                layout_like.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
                layout_like.setEnabled(true);
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            layout_like.setEnabled(true);
            Log.d("TAG", t.getMessage());
            call.cancel();
        }
    };

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
        LinearLayout layout_content_one = view.findViewById(R.id.layout_content_one);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        layout_content_one.setVisibility(View.VISIBLE);
        txt_content.setText(getString(R.string.txt_unverified_other));
        txt_content_two.setText(getString(R.string.txt_unverified_other_two));
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });

        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onResume() {
        isPageChange = false;
        super.onResume();
    }

    @Override
    public void liked(LikeButton likeButton) {
        if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
            if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
                Function.CustomMessage(PostViewActivity.this, getString(R.string.my_ad_favorite));
                likeButton.setLiked(false);
            } else {
                callAddFavorite();
            }
        } else {
            Function.CustomMessage(PostViewActivity.this, getString(R.string.txt_please_login));
            likeButton.setLiked(false);
        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
            if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
                Function.CustomMessage(PostViewActivity.this, getString(R.string.my_ad_favorite));
                likeButton.setLiked(false);
            } else {
                callAddFavorite();
            }
        } else {
            Function.CustomMessage(PostViewActivity.this, getString(R.string.txt_please_login));
            likeButton.setLiked(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = false;
        for (int i = 0; i < Constant.Permissions2.length; i++) {
            int result1 = ContextCompat.checkSelfPermission(PostViewActivity.this, Constant.Permissions2[i]);
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        if (result) {
            Function.CallNow(PostViewActivity.this, userPhone);
        }
    }
}
