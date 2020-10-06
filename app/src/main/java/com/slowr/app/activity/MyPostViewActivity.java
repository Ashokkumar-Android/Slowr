package com.slowr.app.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.slowr.app.R;
import com.slowr.app.adapter.ViewPostImageListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.chat.ChatActivity;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.EditAdDetailsModel;
import com.slowr.app.models.EditAdModel;
import com.slowr.app.models.UploadImageModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class MyPostViewActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_ad_title;
    TextView txt_price;
    TextView txt_location;
    TextView txt_like_count;
    TextView txt_description;
    TextView txt_prosperId;
    TextView txt_phone;
    TextView txt_view_profile;
    TextView txt_fav_count;
    ImageView img_ad_view;
    RecyclerView rc_image_list;
    ImageView img_share;
    ImageView img_favorite;
    ImageView img_like;
    LinearLayout layout_like;
    LinearLayout layout_action_button;
    TextView txt_page_title;
    TextView txt_active_status;
    LinearLayout img_back;
    Button btn_edit;
    Button btn_delete;
    Button btn_promote;
    Button btn_call_now;
    LinearLayout layout_promoted;
    ImageView img_top_page_mark;
    ImageView txt_premium_mark;
    ImageView img_user_profile;
    ImageView img_unverified_user;
    LinearLayout layout_root;
    LinearLayout layout_chat_call;

    ArrayList<UploadImageModel> shareImageList = new ArrayList<>();
    HashMap<String, Object> params = new HashMap<String, Object>();
    ViewPostImageListAdapter postImageListAdapter;
    String catId = "";
    String adId = "";
    String PageFrom = "";
    String isFavorite = "0";
    String isLike = "0";
    String AdType = "";
    String AdStatus = "";
    String changeAdStatus = "";
    String userProUrl = "";
    String userId = "";
    String userProsperId = "";
    int EDIT_POST_CODE = 1299;
    int likeCount = 0;
    int favCount = 0;
    private PopupWindow spinnerPopup;
    View rootView = null;
    String imageStringArray = "";
    int imgSelectPos = 0;
    String userPhone = "";

    boolean isUnverified = false;

    boolean isPageChange = false;
    private Function _fun = new Function();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_post_view);
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
        txt_location = findViewById(R.id.txt_location);
        txt_like_count = findViewById(R.id.txt_like_count);
        txt_description = findViewById(R.id.txt_description);
        txt_prosperId = findViewById(R.id.txt_prosperId);
        txt_phone = findViewById(R.id.txt_phone);
        txt_view_profile = findViewById(R.id.txt_view_profile);
        rc_image_list = findViewById(R.id.rc_image_list);
        img_ad_view = findViewById(R.id.img_ad_view);
        img_share = findViewById(R.id.img_share);
        img_favorite = findViewById(R.id.img_favorite);
        layout_like = findViewById(R.id.layout_like);
        img_like = findViewById(R.id.img_like);
        layout_action_button = findViewById(R.id.layout_action_button);
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        txt_fav_count = findViewById(R.id.txt_fav_count);
        btn_promote = findViewById(R.id.btn_promote);
        txt_active_status = findViewById(R.id.txt_active_status);
        layout_promoted = findViewById(R.id.layout_promoted);
        img_top_page_mark = findViewById(R.id.img_top_page_mark);
        txt_premium_mark = findViewById(R.id.txt_premium_mark);
        img_user_profile = findViewById(R.id.img_user_profile);
        layout_root = findViewById(R.id.layout_root);
        img_unverified_user = findViewById(R.id.img_unverified_user);
        btn_call_now = findViewById(R.id.btn_call_now);
        layout_chat_call = findViewById(R.id.layout_chat_call);
        txt_page_title.setText(getString(R.string.nav_dash_board));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyPostViewActivity.this, RecyclerView.HORIZONTAL, false);
        rc_image_list.setLayoutManager(linearLayoutManager);
        rc_image_list.setItemAnimator(new DefaultItemAnimator());
        postImageListAdapter = new ViewPostImageListAdapter(MyPostViewActivity.this, shareImageList);
        rc_image_list.setAdapter(postImageListAdapter);
        img_favorite.setOnClickListener(this);
        img_share.setOnClickListener(this);
        layout_like.setOnClickListener(this);
//        img_like.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_promote.setOnClickListener(this);
        img_ad_view.setOnClickListener(this);
        txt_active_status.setOnClickListener(this);
        img_user_profile.setOnClickListener(this);
        txt_view_profile.setOnClickListener(this);
        img_unverified_user.setOnClickListener(this);
        btn_call_now.setOnClickListener(this);
        txt_prosperId.setOnClickListener(this);
        CallBackFunction();
        if (_fun.isInternetAvailable(MyPostViewActivity.this)) {
            GetAdDetails();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(MyPostViewActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            GetAdDetails();
                        }
                    });
                }
            }, 200);

        }
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
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

    }


    private void setCurrentImage(String url) {
        Glide.with(this)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(img_ad_view);
    }

    private void GetAdDetails() {
        Log.i("CATAD", catId + " , " + adId);
        RetrofitClient.getClient().create(Api.class).getHomeAdDetails(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(MyPostViewActivity.this, adDetails, true));

    }

    retrofit2.Callback<EditAdModel> adDetails = new retrofit2.Callback<EditAdModel>() {
        @Override
        public void onResponse(Call<EditAdModel> call, retrofit2.Response<EditAdModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                if (response.code() == 200) {
                    EditAdModel dr = response.body();
                    if (dr.isStatus()) {
                        if (dr.getEditDataModel().getAdDetailsModel() != null) {
                            layout_root.setVisibility(View.VISIBLE);
                            EditAdDetailsModel editAdDetailsModel = dr.getEditDataModel().getAdDetailsModel();
                            Sessions.saveSession(Constant.ImagePath, dr.getEditDataModel().getUrlPath(), MyPostViewActivity.this);
                            AdType = editAdDetailsModel.getAdType();
                            txt_ad_title.setText(editAdDetailsModel.getAdTitle().trim());
                            isFavorite = editAdDetailsModel.getIsFavorite();
                            txt_like_count.setText(editAdDetailsModel.getLikeCount());
                            likeCount = Integer.valueOf(editAdDetailsModel.getLikeCount());
                            txt_fav_count.setText(editAdDetailsModel.getFavCount());
                            favCount = Integer.valueOf(editAdDetailsModel.getFavCount());
                            isLike = editAdDetailsModel.getIsLike();
                            txt_location.setText(editAdDetailsModel.getAreaName() + ", " + editAdDetailsModel.getCityName());
                            if (isFavorite.equals("0")) {
                                img_favorite.setImageResource(R.drawable.ic_favorite);
                            } else {
                                img_favorite.setImageResource(R.drawable.ic_fav_select);
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
                                    String tempPrice[] = editAdDetailsModel.getRentalFee().split("\\.");
                                    price = tempPrice[0];
                                } else {
                                    price = editAdDetailsModel.getRentalFee();
                                }

//                            txt_price.setText("₹ " + price + " / " + editAdDetailsModel.getRentalDuration());
                                if (price.equals("0") || editAdDetailsModel.getRentalDuration().equals("Custom")) {
                                    txt_price.setText(editAdDetailsModel.getRentalDuration());
                                } else {
                                    txt_price.setText("₹ " + price + " / " + editAdDetailsModel.getRentalDuration());
                                }
                            } else {
                                txt_price.setVisibility(View.GONE);
                            }
                            txt_description.setText(editAdDetailsModel.getDescription());
                            userProsperId = dr.getEditDataModel().getUserDetailsModel().getProsperId();
                            txt_prosperId.setText(dr.getEditDataModel().getUserDetailsModel().getProsperId());
                            userProUrl = dr.getEditDataModel().getUserDetailsModel().getUserPhoto();
                            Glide.with(MyPostViewActivity.this)
                                    .load(userProUrl)
                                    .circleCrop()
                                    .placeholder(R.drawable.ic_default_profile)
                                    .error(R.drawable.ic_default_profile)
                                    .into(img_user_profile);

                            if (dr.getEditDataModel().getUserDetailsModel().getIsProfileVerified().equals("0")) {
                                img_unverified_user.setVisibility(View.VISIBLE);
                                isUnverified = true;
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
                            } else {
                                rc_image_list.setVisibility(View.GONE);
                            }
                            postImageListAdapter.notifyDataSetChanged();
                            if (AdType.equals("1")) {
                                btn_promote.setVisibility(View.VISIBLE);
                            } else {
                                btn_promote.setVisibility(View.GONE);
                            }
                            AdStatus = editAdDetailsModel.getAdStatus();

                            if (editAdDetailsModel.getAdPromotion().equals("1")) {
                                layout_promoted.setVisibility(View.VISIBLE);
                                txt_premium_mark.setVisibility(View.GONE);
                                img_top_page_mark.setVisibility(View.VISIBLE);
                                btn_promote.setVisibility(View.GONE);
                            } else if (editAdDetailsModel.getAdPromotion().equals("2")) {
                                layout_promoted.setVisibility(View.VISIBLE);
                                txt_premium_mark.setVisibility(View.VISIBLE);
                                img_top_page_mark.setVisibility(View.GONE);
                                btn_promote.setVisibility(View.GONE);
                            } else {
                                layout_promoted.setVisibility(View.INVISIBLE);
                            }
                            if (editAdDetailsModel.getAdStatus().equals("1")) {
                                txt_active_status.setText(getString(R.string.txt_active));
                            } else if (editAdDetailsModel.getAdStatus().equals("2")) {
                                txt_active_status.setText(getString(R.string.txt_in_active));
                                btn_promote.setVisibility(View.GONE);
                            } else {
                                txt_active_status.setText(getString(R.string.txt_in_review));
                                btn_promote.setVisibility(View.GONE);
                            }
                            userId = editAdDetailsModel.getUserId();
                            if (!editAdDetailsModel.getUserId().equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
                                txt_active_status.setVisibility(View.GONE);
                                layout_action_button.setVisibility(View.GONE);
                                layout_chat_call.setVisibility(View.VISIBLE);
                            }
                        }


                    } else {
                        Function.CustomMessage(MyPostViewActivity.this, dr.getMessage());
                    }
                } else if (response.code() == 500) {
                    Function.CustomMessage(MyPostViewActivity.this, "Try again.");
                    finish();
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
    public void onBackPressed() {

        if (PageFrom.equals("2")) {
            Intent h = new Intent(MyPostViewActivity.this, HomeActivity.class);
            h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(h);
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_share:
                break;
            case R.id.img_favorite:
                if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
                    Function.CustomMessage(MyPostViewActivity.this, getString(R.string.my_ad_favorite));
                } else {
                    callAddFavorite();
                }
//                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
//                    callAddFavorite();
//                } else {
//                }

                break;
            case R.id.layout_like:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    if (_fun.isInternetAvailable(MyPostViewActivity.this)) {
                        callAddLike();
                    } else {
                        _fun.ShowNoInternetPopup(MyPostViewActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                callAddLike();
                            }
                        });

                    }

                } else {
                    Function.CustomMessage(MyPostViewActivity.this, getString(R.string.txt_please_login));
                }

                break;
            case R.id.img_back:
                if (PageFrom.equals("2")) {
                    Intent h = new Intent(MyPostViewActivity.this, HomeActivity.class);
                    h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(h);
                }
                finish();

                break;
            case R.id.btn_edit:
                Intent i = new Intent(MyPostViewActivity.this, AddPostActivity.class);
                i.putExtra("CatId", catId);
                i.putExtra("AdId", adId);
                i.putExtra("AdType", 1);
                i.putExtra("EditType", AdType);
                startActivityForResult(i, EDIT_POST_CODE);

                break;
            case R.id.btn_delete:

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        MyPostViewActivity.this);

                alertDialog2.setTitle("Delete Ad");

                alertDialog2.setMessage(getString(R.string.ad_delete_message));

                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (_fun.isInternetAvailable(MyPostViewActivity.this)) {
                                    if (AdType.equals("1")) {
                                        RetrofitClient.getClient().create(Api.class).deletePost(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                                .enqueue(new RetrofitCallBack(MyPostViewActivity.this, deleteAd, true));
                                    } else {
                                        RetrofitClient.getClient().create(Api.class).deleteRequirementPost(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                                .enqueue(new RetrofitCallBack(MyPostViewActivity.this, deleteAd, true));
                                    }
                                } else {
                                    _fun.ShowNoInternetPopup(MyPostViewActivity.this, new Function.NoInternetCallBack() {
                                        @Override
                                        public void isInternet() {
                                            if (AdType.equals("1")) {
                                                RetrofitClient.getClient().create(Api.class).deletePost(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                                        .enqueue(new RetrofitCallBack(MyPostViewActivity.this, deleteAd, true));
                                            } else {
                                                RetrofitClient.getClient().create(Api.class).deleteRequirementPost(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                                        .enqueue(new RetrofitCallBack(MyPostViewActivity.this, deleteAd, true));
                                            }
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
                break;
            case R.id.btn_promote:
                Intent p = new Intent(MyPostViewActivity.this, UpgradeActivity.class);
                p.putExtra("PageFrom", "2");
                p.putExtra("CatId", catId);
                p.putExtra("AdId", adId);
                startActivityForResult(p, EDIT_POST_CODE);
                break;
            case R.id.img_ad_view:
                if (!isPageChange) {
                    isPageChange = true;
                    Intent a = new Intent(MyPostViewActivity.this, ImageViewActivity.class);
                    a.putExtra("ImgURL", imageStringArray);
                    a.putExtra("ImgPos", imgSelectPos);
                    startActivity(a);
                }
                break;
            case R.id.txt_active_status:
                if (AdStatus.equals("1")) {
                    changeAdStatus = "2";
                } else if (AdStatus.equals("2")) {
                    changeAdStatus = "1";
                } else {
                    changeAdStatus = "2";
                    return;
                }

                if (!params.isEmpty()) {
                    params.clear();
                }
                params.put("ads_id", adId);
                params.put("category_id", catId);
                params.put("status", changeAdStatus);
                Log.i("Params", params.toString());

                if (_fun.isInternetAvailable(MyPostViewActivity.this)) {
                    RetrofitClient.getClient().create(Api.class).changeAdStatus(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(MyPostViewActivity.this, adStatusApi, true));
                } else {
                    _fun.ShowNoInternetPopup(MyPostViewActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            RetrofitClient.getClient().create(Api.class).changeAdStatus(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                    .enqueue(new RetrofitCallBack(MyPostViewActivity.this, adStatusApi, true));
                        }
                    });

                }


                break;
            case R.id.img_user_profile:
                if (!isPageChange) {
                    isPageChange = true;
                    Intent a = new Intent(MyPostViewActivity.this, ImageViewActivity.class);
                    a.putExtra("ImgURL", userProUrl);
                    a.putExtra("ImgPos", 0);
                    startActivity(a);
                }
                break;
            case R.id.txt_view_profile:
                if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
                    Intent profile = new Intent(MyPostViewActivity.this, ProfileActivity.class);
                    startActivity(profile);
                } else {
                    Intent j = new Intent(MyPostViewActivity.this, UserProfileActivity.class);
                    j.putExtra("prosperId", userProsperId);
                    startActivity(j);
                }

                break;
            case R.id.img_unverified_user:
                ShowPopupProsper();
                break;
            case R.id.btn_call_now:
                if (_fun.checkPermission2(MyPostViewActivity.this))
                    Function.CallNow(MyPostViewActivity.this, userPhone);
                break;
            case R.id.txt_prosperId:
                if (isUnverified) {
                    ShowPopupProsper();
                }
                break;
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
                    Intent profile = new Intent(MyPostViewActivity.this, ProfileActivity.class);
                    startActivityForResult(profile, EDIT_POST_CODE);
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
                    .enqueue(new RetrofitCallBack(MyPostViewActivity.this, addFavorite, true));
        } else {
            RetrofitClient.getClient().create(Api.class).deleteFavorite(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(MyPostViewActivity.this, addFavorite, true));
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
                    .enqueue(new RetrofitCallBack(MyPostViewActivity.this, addLike, true));
        } else {
//            RetrofitClient.getClient().create(Api.class).deleteLike(catId, adId, "Bearer " + Sessions.getSession(Constant.UserToken, getApplicationContext()))
//                    .enqueue(new RetrofitCallBack(PostViewActivity.this, addLike, true));
            Function.CustomMessage(MyPostViewActivity.this, getString(R.string.txt_already_liked));
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
                        img_favorite.setImageResource(R.drawable.ic_fav_select);
                        favCount++;
                        txt_fav_count.setText(String.valueOf(favCount));
                    } else {
                        isFavorite = "0";
                        img_favorite.setImageResource(R.drawable.ic_favorite);
                        favCount--;
                        if (favCount == 0) {
                            txt_fav_count.setText("0");
                        } else {
                            txt_fav_count.setText(String.valueOf(favCount));
                        }
                    }
                    Function.CustomMessage(MyPostViewActivity.this, dr.getMessage());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                } else {
                    Function.CustomMessage(MyPostViewActivity.this, dr.getMessage());
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
                            txt_like_count.setText("0");
                        }
                    }
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                } else {
                    Function.CustomMessage(MyPostViewActivity.this, dr.getMessage());
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

    retrofit2.Callback<DefaultResponse> deleteAd = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    Function.CustomMessage(MyPostViewActivity.this, dr.getMessage());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Function.CustomMessage(MyPostViewActivity.this, dr.getMessage());
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

    retrofit2.Callback<DefaultResponse> adStatusApi = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    AdStatus = changeAdStatus;
                    if (AdStatus.equals("1")) {
                        txt_active_status.setText(getString(R.string.txt_active));
                    } else if (AdStatus.equals("2")) {
                        txt_active_status.setText(getString(R.string.txt_in_active));
                    } else {
                        txt_active_status.setText(getString(R.string.txt_in_review));
                    }
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                } else {
                    Function.CustomMessage(MyPostViewActivity.this, dr.getMessage());

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

    @Override
    protected void onResume() {
        isPageChange = false;
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_POST_CODE) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                if (_fun.isInternetAvailable(MyPostViewActivity.this)) {
                    GetAdDetails();
                } else {
                    _fun.ShowNoInternetPopup(MyPostViewActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            GetAdDetails();
                        }
                    });

                }
            }
        }
    }
}
