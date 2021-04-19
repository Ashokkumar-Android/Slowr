package com.slowr.app.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.slowr.app.R;
import com.slowr.app.adapter.HomeBannerAdapter;
import com.slowr.app.adapter.UserAdListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.components.carouselview.CarouselView;
import com.slowr.app.models.AdItemModel;
import com.slowr.app.models.BannerItemModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.OtherProfileModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt_page_title;
    LinearLayout img_back;
    LinearLayout layout_no_result;
    LinearLayout layout_no_ads;
    NestedScrollView layout_root;
    RecyclerView rc_ad_list;
    TextView txt_prosperId_post;
    TextView txt_name;
    TextView txt_phone;
    TextView txt_email;
    TextView txt_verified;
    TextView txt_no_of_ads;
    ImageView img_unverified_user;
    ImageView img_user_profile;
    CarouselView rc_banner;
    EditText edt_search_ad;
    Button btn_share_business;
    Button btn_view_contact;
    Button btn_home_page;
    TextView txt_prosperId_no;
    ArrayList<BannerItemModel> bannerList = new ArrayList<>();


    UserAdListAdapter homeAdListAdapter;
    ArrayList<AdItemModel> adList = new ArrayList<>();
    HashMap<String, Object> params = new HashMap<String, Object>();

    int favPosition = 0;
    int VIEW_POST_CODE = 1299;
    boolean isChange = false;
    private Function _fun = new Function();
    String prosperId = "";
    String userId = "";
    String userPhone = "";
    String userEmail = "";
    String userName = "";
    private PopupWindow spinnerPopup, demoPopup;

    String shareMessage = "";
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    HomeBannerAdapter homeBannerAdapter;

    boolean isBannerStarted = false;
    boolean isPageChange = false;
    String userProUrl = "";
    String userProsperId = "";
    String pageFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        doDeclaration();
    }

    private void doDeclaration() {
        if (getIntent().hasExtra("prosperId")) {
            prosperId = getIntent().getStringExtra("prosperId");
            if (getIntent().hasExtra("PageFrom")) {
                pageFrom = getIntent().getStringExtra("PageFrom");
            }
        }
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        rc_ad_list = findViewById(R.id.rc_ad_list);
        txt_prosperId_post = findViewById(R.id.txt_prosperId_post);
        img_unverified_user = findViewById(R.id.img_unverified_user);
        txt_name = findViewById(R.id.txt_name);
        txt_phone = findViewById(R.id.txt_phone);
        img_user_profile = findViewById(R.id.img_user_profile);
        txt_verified = findViewById(R.id.txt_verified);
        txt_email = findViewById(R.id.txt_email);
        layout_root = findViewById(R.id.layout_root);
        rc_banner = findViewById(R.id.rc_banner);
        txt_no_of_ads = findViewById(R.id.txt_no_of_ads);
        btn_share_business = findViewById(R.id.btn_share_business);
        btn_view_contact = findViewById(R.id.btn_view_contact);
        layout_no_result = findViewById(R.id.layout_no_result);
        btn_home_page = findViewById(R.id.btn_home_page);
        layout_no_ads = findViewById(R.id.layout_no_ads);
        txt_prosperId_no = findViewById(R.id.txt_prosperId_no);

        edt_search_ad = findViewById(R.id.edt_search_ad);
        txt_page_title.setText("Prosper Page");
        reciveDeepLink();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_ad_list.setLayoutManager(linearLayoutManager);
        rc_ad_list.setItemAnimator(new DefaultItemAnimator());
        homeAdListAdapter = new UserAdListAdapter(adList, UserProfileActivity.this, false);
        rc_ad_list.setAdapter(homeAdListAdapter);


        homeBannerAdapter = new HomeBannerAdapter(bannerList, UserProfileActivity.this);
        rc_banner.setAdapter(homeBannerAdapter);
        homeBannerAdapter.hideText(false);
        getAdList();


        callBackFunction();
        img_back.setOnClickListener(this);
        txt_verified.setOnClickListener(this);
        txt_prosperId_post.setOnClickListener(this);
        txt_phone.setOnClickListener(this);
        txt_email.setOnClickListener(this);
        img_user_profile.setOnClickListener(this);
        btn_share_business.setOnClickListener(this);
        btn_view_contact.setOnClickListener(this);
        btn_home_page.setOnClickListener(this);

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
                homeAdListAdapter.getFilter().filter(edt_search_ad.getText().toString());

            }
        });
    }

    private void getAdList() {
        if (prosperId != null && !prosperId.equals("")) {
            if (_fun.isInternetAvailable(UserProfileActivity.this)) {
                if (pageFrom.equals("3")) {
                    RetrofitClient.getClient().create(Api.class).getUserAdDetailsSearch(prosperId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(UserProfileActivity.this, adListResponse, true, false));
                } else {
                    RetrofitClient.getClient().create(Api.class).getUserAdDetails(prosperId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(UserProfileActivity.this, adListResponse, true, false));
                }
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _fun.ShowNoInternetPopup(UserProfileActivity.this, new Function.NoInternetCallBack() {
                            @Override
                            public void isInternet() {
                                RetrofitClient.getClient().create(Api.class).getUserAdDetails(prosperId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                                        .enqueue(new RetrofitCallBack(UserProfileActivity.this, adListResponse, true, false));
                            }
                        });
                    }
                }, 200);
            }
        }
    }

    private void callBackFunction() {
        homeBannerAdapter.setCallback(new HomeBannerAdapter.Callback() {
            @Override
            public void itemClick(BannerItemModel model, boolean b) {

            }
        });
        homeAdListAdapter.setCallback(new UserAdListAdapter.Callback() {
            @Override
            public void itemClick(AdItemModel adItemModel) {
                String adId = adItemModel.getAdSlug();
                String userId = adItemModel.getUserId();
                changeFragment(adId, userId);
            }

            @Override
            public void onFavoriteClick(AdItemModel adItemModel) {
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    if (!adItemModel.isProgress()) {
//                        favPosition = pos;

                        for (int i = 0; i < adList.size(); i++) {
                            if (adList.get(i).getAdId().equals(adItemModel.getAdId())) {
                                favPosition = i;
                            }
                        }
                        adItemModel.setProgress(true);
                        if (_fun.isInternetAvailable(UserProfileActivity.this)) {
                            callAddFavorite(adItemModel);
                        } else {
                            _fun.ShowNoInternetPopup(UserProfileActivity.this, new Function.NoInternetCallBack() {
                                @Override
                                public void isInternet() {
                                    callAddFavorite(adItemModel);
                                }
                            });

                        }

                    }

                } else {
                    Intent l = new Intent(UserProfileActivity.this, LoginActivity.class);
                    startActivityForResult(l, VIEW_POST_CODE);
                }
            }

            @Override
            public void onShareClick(AdItemModel adItemModel) {
                String adId = adItemModel.getAdSlug();
                String adTitle = adItemModel.getAdTitle();
                String catGroup = adItemModel.getCatGroup();
                String url = adItemModel.getPhotoType();
                Function.ShareLink(UserProfileActivity.this, adId, adTitle, catGroup, url);
            }

            @Override
            public void onLoginClick(AdItemModel adItemModel) {
                Intent l = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivityForResult(l, VIEW_POST_CODE);
            }
        });
    }

    private void reciveDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String pageLink = String.valueOf(deepLink);
                            Log.i("Link", pageLink);
                            pageLink = pageLink.replaceAll("//", "~");
                            pageLink = pageLink.replaceAll("/", "~");
                            Log.i("Link", pageLink);
                            if (pageLink.contains("https:~www.slowr.com~")) {
                                pageLink = pageLink.replace("https:~www.slowr.com~", "");
                            }
                            Log.i("Link", pageLink);
                            prosperId = pageLink;
                            pageFrom = "2";
                            getAdList();
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "getDynamicLink:onFailure", e);
                    }
                });
    }

    private void callAddFavorite(AdItemModel adItemModel) {
        String adId = adItemModel.getAdSlug();
        String isFav = adItemModel.getIsFavorite();
        if (isFav.equals("0")) {
            if (!params.isEmpty()) {
                params.clear();
            }
            params.put("ads_id", adId);
            Log.i("Params", params.toString());
            RetrofitClient.getClient().create(Api.class).addFavorite(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UserProfileActivity.this, addFavorite, true, false));
        } else {
            RetrofitClient.getClient().create(Api.class).deleteFavorite(adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UserProfileActivity.this, addFavorite, true, false));
        }
    }

    void changeFragment(String adId, String userId) {
        if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            Intent p = new Intent(UserProfileActivity.this, MyPostViewActivity.class);
            p.putExtra("AdId", adId);
            startActivityForResult(p, VIEW_POST_CODE);
        } else {
            Intent p = new Intent(UserProfileActivity.this, PostViewActivity.class);
            p.putExtra("AdId", adId);
            startActivityForResult(p, VIEW_POST_CODE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:

                if (pageFrom.equals("2")) {
                    Intent h = new Intent(UserProfileActivity.this, HomeActivity.class);
                    h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(h);
                }

                finish();
                break;
            case R.id.txt_verified:
//                ShowPopupProsper();
                break;
            case R.id.txt_prosperId_post:
//                ShowPopupProsper();
                break;
            case R.id.txt_phone:
                if (_fun.checkPermission2(UserProfileActivity.this))
                    Function.CallNow(UserProfileActivity.this, userPhone);
                break;
            case R.id.txt_email:
//                if (!userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + txt_email.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(intent);
//                }
                break;
            case R.id.img_user_profile:
                if (userProUrl != null && !userProUrl.equals(""))
                    if (!isPageChange) {
                        isPageChange = true;
                        Intent a = new Intent(UserProfileActivity.this, ImageViewActivity.class);
                        a.putExtra("ImgURL", userProUrl);
                        a.putExtra("ImgPos", 0);
                        startActivity(a);
                    }
                break;
            case R.id.btn_share_business:
                Function.ShareProfileLink(UserProfileActivity.this, userProsperId, userProUrl);
                break;
            case R.id.btn_view_contact:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    ShowPopupContact();
                    ViewContact("1");
                } else {
                    Intent l = new Intent(UserProfileActivity.this, LoginActivity.class);
                    startActivityForResult(l, VIEW_POST_CODE);
                }
                break;
            case R.id.btn_home_page:
                Intent h = new Intent(UserProfileActivity.this, HomeActivity.class);
                h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(h);
                break;

        }
    }

    public void ShowPopupContact() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_popup_contact_view, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        TextView txt_phone = view.findViewById(R.id.txt_phone);
        TextView txt_email = view.findViewById(R.id.txt_email);
        TextView txt_name = view.findViewById(R.id.txt_name);
        TextView txt_whats_app = view.findViewById(R.id.txt_whats_app);
        LinearLayout layout_delete = view.findViewById(R.id.layout_delete);
        txt_phone.setText(userPhone);
        txt_email.setText(userEmail);
        txt_name.setText(userName);
        txt_whats_app.setText(userPhone);
        txt_whats_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+91" + userPhone));
                startActivity(intent);
            }
        });
        if (userPhone != null && !userPhone.equals("")) {
            txt_phone.setVisibility(View.VISIBLE);
            txt_whats_app.setVisibility(View.VISIBLE);
        } else {
            txt_phone.setVisibility(View.GONE);
            txt_whats_app.setVisibility(View.GONE);
        }
        if (userEmail != null && !userEmail.equals("")) {
            txt_email.setVisibility(View.VISIBLE);
        } else {
            txt_email.setVisibility(View.GONE);
        }
        txt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_fun.checkPermission2(UserProfileActivity.this)) {
                    Function.CallNow(UserProfileActivity.this, userPhone);
                    ViewContact("2");
                }
            }
        });
        txt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + txt_email.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(intent);
                ViewContact("4");
            }
        });
        layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPopup.dismiss();
            }
        });
        spinnerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void ViewContact(String type) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("platform", "3");
        params.put("type", type);
        params.put("prosperId", prosperId);

        Log.i("Params", params.toString());
        if (!userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
            RetrofitClient.getClient().create(Api.class).viewContact(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(UserProfileActivity.this, viewContactResponse, false, false));
        }
    }

    retrofit2.Callback<DefaultResponse> viewContactResponse = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();

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

    retrofit2.Callback<OtherProfileModel> adListResponse = new retrofit2.Callback<OtherProfileModel>() {
        @Override
        public void onResponse(Call<OtherProfileModel> call, retrofit2.Response<OtherProfileModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            OtherProfileModel dr = response.body();
            try {
                if (dr.isStatus()) {
                    layout_root.setVisibility(View.VISIBLE);
//                    Log.i("ImagePathUrl", dr.getUrlPath());
//                    Sessions.saveSession(Constant.ImagePath, dr.getUrlPath(), FavoriteActivity.this);
                    userId = dr.getUserDetailsModel().getUserId();
                    txt_prosperId_post.setText(dr.getUserDetailsModel().getProsperId());
                    userProsperId = dr.getUserDetailsModel().getProsperId();
                    txt_name.setText(dr.getUserDetailsModel().getUserName());
                    userName = dr.getUserDetailsModel().getUserName();
                    userPhone = dr.getUserDetailsModel().getUserPhone();
                    userProUrl = dr.getUserDetailsModel().getUserPhoto();
                    userEmail = dr.getUserDetailsModel().getUserEmail();
                    if (userPhone != null && !userPhone.equals("")) {
                        txt_phone.setText(dr.getUserDetailsModel().getUserPhone());
                        txt_phone.setVisibility(View.GONE);
                    } else {
                        txt_phone.setVisibility(View.GONE);
                    }
                    if (dr.getUserDetailsModel().getUserEmail() != null && !dr.getUserDetailsModel().getUserEmail().equals("")) {
                        txt_email.setText(dr.getUserDetailsModel().getUserEmail());
                        txt_email.setVisibility(View.GONE);
                    } else {
                        txt_email.setVisibility(View.GONE);
                    }
                    if (dr.getUserDetailsModel().getIsProfileVerified().equals("0")) {
                        txt_verified.setVisibility(View.GONE);
                    } else {
                        txt_verified.setVisibility(View.GONE);
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
                    txt_no_of_ads.setText(getString(R.string.txt_total_no_ad) + " " + adList.size());
                    if (adList.size() == 0) {
                        edt_search_ad.setVisibility(View.GONE);
                        rc_ad_list.setVisibility(View.GONE);
                        layout_no_ads.setVisibility(View.VISIBLE);
                    } else {
                        edt_search_ad.setVisibility(View.VISIBLE);
                        rc_ad_list.setVisibility(View.VISIBLE);
                        layout_no_ads.setVisibility(View.GONE);
                    }
                    bannerList.clear();
                    if (dr.getBannerList() != null) {
                        bannerList.addAll(dr.getBannerList());
                        homeBannerAdapter.notifyDataSetChanged();

                        if (!isBannerStarted && bannerList.size() != 0) {
                            isBannerStarted = true;
                            rc_banner.start(3, TimeUnit.SECONDS);
                        }
                        if (bannerList.size() == 0) {
                            rc_banner.setVisibility(View.GONE);
                        } else {
                            rc_banner.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
//                    Function.CustomMessage(UserProfileActivity.this, dr.getMessage());
                    txt_prosperId_no.setText(prosperId.toUpperCase());
                    layout_no_result.setVisibility(View.VISIBLE);
                    layout_root.setVisibility(View.GONE);

                }
            } catch (Exception e) {
                layout_no_result.setVisibility(View.VISIBLE);
                layout_root.setVisibility(View.GONE);
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
//                        homeAdListAdapter.notifyItemChanged(favPosition);
                    } else {
                        adList.get(favPosition).setIsFavorite("0");
//                        homeAdListAdapter.notifyItemChanged(favPosition);
                    }
                    Function.CustomMessage(UserProfileActivity.this, dr.getMessage());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    isChange = true;

                } else {
                    Function.CustomMessage(UserProfileActivity.this, dr.getMessage());
                }
                adList.get(favPosition).setProgress(false);

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
                getAdList();

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                isChange = false;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean result = false;
        for (int i = 0; i < Constant.Permissions2.length; i++) {
            int result1 = ContextCompat.checkSelfPermission(UserProfileActivity.this, Constant.Permissions2[i]);
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        if (result) {
            Function.CallNow(UserProfileActivity.this, userPhone);
            ViewContact("2");
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

    @Override
    protected void onResume() {
        isPageChange = false;
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (pageFrom.equals("2")) {
            Intent h = new Intent(UserProfileActivity.this, HomeActivity.class);
            h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(h);
        }

        finish();
    }
}
