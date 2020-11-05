package com.slowr.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.slowr.app.R;
import com.slowr.app.adapter.CityListAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.chat.ProductChatActivity;
import com.slowr.app.models.CityItemModel;
import com.slowr.app.models.CityModel;
import com.slowr.app.models.CountModel;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout layout_login;
    LinearLayout layout_prosper;
    LinearLayout layout_location;
    LinearLayout layout_list;
    DrawerLayout drawer;
    NavigationView navigationView;
    View headerLayout;
    ImageView img_nav_menu;
    Button btn_add_post;
    EditText edt_list_search;
    TextView edt_search;
    RecyclerView rc_list;
    TextView txt_page_title;
    TextView txt_location;
    LinearLayout img_back;
    ImageView img_profile_pic;
    ImageView img_unverified_user_home;
    TextView txt_prosperId;
    TextView txt_prosperId_menu;
    TextView txt_total_count;
    LinearLayout layout_menu_header;
//    SearchCallBack searchCallBack;


    CityListAdapter cityListAdapter;

    ArrayList<CityItemModel> cityList = new ArrayList<>();

    private PopupWindow spinnerPopup;
    String cityId = "0";
    HashMap<String, Object> params = new HashMap<String, Object>();

    boolean isLocationVisible = false;
    ActionBarDrawerToggle toggle;

    MenuItem menuLogout;
//    MenuItem menuProfile;
    MenuItem menuFavorites;
    MenuItem menuMyAds;
    MenuItem menuInvoice;
    MenuItem menuNotification;
    MenuItem menuChat;
    MenuItem menuBanner;
    int MY_PROFILE_CODE = 1299;
    int MY_POST_CODE = 1288;
    private Function _fun = new Function();

    TextView txtChatUnread;
    TextView txtNotificationUnread;

    public static BaseActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.i("View call", "onCreate");
        doDeclaration();

    }

    private void doDeclaration() {
        instance = this;
        txt_page_title = findViewById(R.id.txt_page_title);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        layout_login = findViewById(R.id.layout_login);
        layout_prosper = findViewById(R.id.layout_prosper);
        img_nav_menu = findViewById(R.id.img_nav_menu);
        btn_add_post = findViewById(R.id.btn_add_post);
        layout_location = findViewById(R.id.layout_location);
        layout_list = findViewById(R.id.layout_list);
        img_back = findViewById(R.id.img_back);
        edt_list_search = findViewById(R.id.edt_list_search);
        edt_search = findViewById(R.id.edt_search);
        rc_list = findViewById(R.id.rc_list);
        txt_location = findViewById(R.id.txt_location);
        txt_prosperId = findViewById(R.id.txt_prosperId);
        img_unverified_user_home = findViewById(R.id.img_unverified_user_home);
        txt_total_count = findViewById(R.id.txt_total_count);
        txt_page_title.setText(getString(R.string.txt_select_city));
        headerLayout = navigationView.getHeaderView(0);
        txt_prosperId_menu = headerLayout.findViewById(R.id.txt_prosperId_menu);
        layout_menu_header = headerLayout.findViewById(R.id.layout_menu_header);
        img_profile_pic = headerLayout.findViewById(R.id.img_profile_pic);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_list.setLayoutManager(linearLayoutManager);
        rc_list.setItemAnimator(new DefaultItemAnimator());
        cityListAdapter = new CityListAdapter(cityList, getApplicationContext());
        rc_list.setAdapter(cityListAdapter);


        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();

        menuLogout = menu.findItem(R.id.nav_logout);
//        menuProfile = menu.findItem(R.id.nav_profile);
        menuFavorites = menu.findItem(R.id.nav_favorites);
        menuMyAds = menu.findItem(R.id.nav_dash_board);
        menuInvoice = menu.findItem(R.id.nav_invoice);
        menuNotification = menu.findItem(R.id.nav_notification);
        menuChat = menu.findItem(R.id.nav_my_chat);
        menuBanner = menu.findItem(R.id.nav_banners);
        LinearLayout viewChat = (LinearLayout) menuChat.getActionView();
        LinearLayout viewNotification = (LinearLayout) menuNotification.getActionView();

        txtChatUnread = viewChat.findViewById(R.id.txt_unread_count);
        txtNotificationUnread = viewNotification.findViewById(R.id.txt_unread_count);


        if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
            layout_login.setVisibility(View.GONE);
            layout_prosper.setVisibility(View.VISIBLE);
            menuLogout.setVisible(true);
//            menuProfile.setVisible(true);
            menuFavorites.setVisible(true);
            menuMyAds.setVisible(true);
            menuInvoice.setVisible(true);
            menuNotification.setVisible(true);
            menuChat.setVisible(true);
            menuBanner.setVisible(true);
            layout_menu_header.setVisibility(View.VISIBLE);
            txt_prosperId.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
            txt_prosperId_menu.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
            if (Sessions.getSession(Constant.UserVerified, getApplicationContext()).equals("0")) {
                img_unverified_user_home.setVisibility(View.VISIBLE);
            } else {
                img_unverified_user_home.setVisibility(View.GONE);
            }
            Glide.with(this)
                    .load(Sessions.getSession(Constant.UserProfile, getApplicationContext()))
                    .circleCrop()
                    .error(R.drawable.ic_default_profile)
                    .placeholder(R.drawable.ic_default_profile)
                    .into(img_profile_pic);
            callUnreadCount();
        } else {
            layout_login.setVisibility(View.VISIBLE);
            layout_prosper.setVisibility(View.GONE);
            layout_menu_header.setVisibility(View.GONE);
            menuLogout.setVisible(false);
//            menuProfile.setVisible(false);
            menuFavorites.setVisible(false);
            menuMyAds.setVisible(false);
            menuInvoice.setVisible(false);
            menuNotification.setVisible(false);
            menuChat.setVisible(false);
            menuBanner.setVisible(false);
        }

    }

    private void ClickFunction() {
        edt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(BaseActivity.this, SearchActivity.class);
                startActivity(s);
            }
        });
        img_nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        layout_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(l);
//                finish();
            }
        });
        btn_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent p = new Intent(BaseActivity.this, AddPostActivity.class);
                    p.putExtra("AdType", 0);
                    startActivity(p);
                } else {
                    Function.CustomMessage(BaseActivity.this, getString(R.string.txt_please_login));
                }
            }
        });
        layout_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityList.size() == 0) {
                    GetCity();
                } else {
                    layout_list.setVisibility(View.VISIBLE);
                    isLocationVisible = true;
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_list.setVisibility(View.GONE);
                isLocationVisible = false;
                edt_list_search.setText("");
                Function.hideSoftKeyboard(BaseActivity.this, edt_list_search);
            }
        });

//        layout_prosper.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Sessions.clearSession(getApplicationContext());
//                layout_prosper.setVisibility(View.GONE);
//                layout_login.setVisibility(View.VISIBLE);
//            }
//        });
        img_unverified_user_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupProsperBase();
            }
        });

        txt_prosperId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sessions.getSession(Constant.UserVerified, getApplicationContext()).equals("0")) {
                    ShowPopupProsperBase();
                }
            }
        });

        layout_menu_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(BaseActivity.this, ProfileActivity.class);
                startActivityForResult(profile, MY_PROFILE_CODE);
            }
        });
    }

    public void ShowPopupProsperBase() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_popup_unverified_user, null);
        spinnerPopup = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        spinnerPopup.setOutsideTouchable(true);
        spinnerPopup.setFocusable(true);
        spinnerPopup.update();
        TextView txt_content = view.findViewById(R.id.txt_content_two);
        TextView txt_skip = view.findViewById(R.id.txt_skip);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        txt_content.setText(getString(R.string.txt_unverified_own));
        btn_ok.setText(getString(R.string.txt_verify));
        txt_skip.setVisibility(View.VISIBLE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(BaseActivity.this, ProfileActivity.class);
                startActivityForResult(profile, MY_PROFILE_CODE);
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

    private void filterFunction() {
        edt_list_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cityListAdapter.getFilter().filter(edt_list_search.getText().toString());
            }
        });
    }

    private void CallBackFunction() {
        cityListAdapter.setCallback(new CityListAdapter.Callback() {
            @Override
            public void itemClick(CityItemModel model) {
                cityId = model.getCityId();
                txt_location.setText(model.getCityName());
                Sessions.saveSession(Constant.CityId, cityId, getApplicationContext());
                Sessions.saveSession(Constant.CityName, model.getCityName(), getApplicationContext());
                layout_list.setVisibility(View.GONE);
                isLocationVisible = false;
                edt_list_search.setText("");
                Function.hideSoftKeyboard(BaseActivity.this, edt_list_search);
                Intent h = new Intent(BaseActivity.this, HomeActivity.class);
                h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(h);
            }
        });


    }

    public void GetCity() {
        RetrofitClient.getClient().create(Api.class).getCity()
                .enqueue(new RetrofitCallBack(BaseActivity.this, cityValue, false));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
//            case R.id.nav_home:
//                Intent h = new Intent(BaseActivity.this, HomeActivity.class);
//                h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(h);
//                break;
            case R.id.nav_dash_board:
                Intent d = new Intent(BaseActivity.this, DashBoardActivity.class);
                startActivityForResult(d, MY_POST_CODE);

                break;
            case R.id.nav_privacy_policy:
                Intent i = new Intent(BaseActivity.this, PolicyActivity.class);
                i.putExtra("PageFrom", "1");
                startActivity(i);
                break;
            case R.id.nav_terms_conditions:
                Intent t = new Intent(BaseActivity.this, PolicyActivity.class);
                t.putExtra("PageFrom", "2");
                startActivity(t);
                break;
            case R.id.nav_report_us:
                Intent ru = new Intent(BaseActivity.this, ReportUsActivity.class);
                startActivity(ru);
                break;
            case R.id.nav_about_us:
                Intent ab = new Intent(BaseActivity.this, AboutUsActivity.class);
                startActivity(ab);
                break;
            case R.id.nav_logout:

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        BaseActivity.this);

                alertDialog2.setTitle("Logout");

                alertDialog2.setMessage(getString(R.string.logout_message));

                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (_fun.isInternetAvailable(BaseActivity.this)) {
                                    callLogout();
                                } else {
                                    _fun.ShowNoInternetPopup(BaseActivity.this, new Function.NoInternetCallBack() {
                                        @Override
                                        public void isInternet() {
                                            callLogout();
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
//            case R.id.nav_profile:
//                Intent profile = new Intent(BaseActivity.this, ProfileActivity.class);
//                startActivityForResult(profile, MY_PROFILE_CODE);
//                break;
            case R.id.nav_favorites:
                Intent fav = new Intent(BaseActivity.this, FavoriteActivity.class);
                startActivityForResult(fav, MY_POST_CODE);
                break;
            case R.id.nav_invoice:
                Intent tr = new Intent(BaseActivity.this, TransactionActivity.class);
                startActivity(tr);
                break;
            case R.id.nav_notification:
                Intent n = new Intent(BaseActivity.this, NotificationActivity.class);
                startActivity(n);
                break;
            case R.id.nav_my_chat:
                Intent c = new Intent(BaseActivity.this, ProductChatActivity.class);
                startActivity(c);
                break;
            case R.id.nav_banners:
                Intent b = new Intent(BaseActivity.this, BannerActivity.class);
                startActivity(b);
                break;

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callLogout() {
        RetrofitClient.getClient().create(Api.class).callLogout(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(BaseActivity.this, logoutApi, true));
    }

    public void callUnreadCount() {
        if (_fun.isInternetAvailable(BaseActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getNotificationUnreadCount(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(BaseActivity.this, unReadApi, false));
        } else {
            _fun.ShowNoInternetPopup(BaseActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getNotificationUnreadCount(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(BaseActivity.this, unReadApi, false));
                }
            });
        }

    }

    retrofit2.Callback<DefaultResponse> logoutApi = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                DefaultResponse dr = response.body();
                if (dr.isStatus()) {
                    String cityId = Sessions.getSession(Constant.CityId, getApplicationContext());
                    String cityName = Sessions.getSession(Constant.CityName, getApplicationContext());

                    Sessions.clearSession(getApplicationContext());
                    Sessions.saveSession(Constant.CityId, cityId, getApplicationContext());
                    Sessions.saveSession(Constant.CityName, cityName, getApplicationContext());
                    Sessions.saveSessionBool(Constant.IsFirst, true, getApplicationContext());
                    Intent h = new Intent(BaseActivity.this, HomeActivity.class);
                    h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(h);
//                    layout_login.setVisibility(View.VISIBLE);
//                    layout_prosper.setVisibility(View.GONE);
//                    menuLogout.setVisible(false);
//                    menuProfile.setVisible(false);
//                    menuMyAds.setVisible(false);
//                    menuFavorites.setVisible(false);
//                    layout_menu_header.setVisibility(View.GONE);
                } else {
                    Function.CustomMessage(BaseActivity.this, dr.getMessage());
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

    retrofit2.Callback<CityModel> cityValue = new retrofit2.Callback<CityModel>() {
        @Override
        public void onResponse(Call<CityModel> call, retrofit2.Response<CityModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                CityModel dr = response.body();
                if (dr.isStatus()) {
                    if (cityList.size() != 0) {
                        cityList.clear();
                    }
                    CityItemModel cityItemModel = new CityItemModel("","All India","",false);

                    cityList.add(cityItemModel);
                    cityList.addAll(dr.getCityList());

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
    retrofit2.Callback<CountModel> unReadApi = new retrofit2.Callback<CountModel>() {
        @Override
        public void onResponse(Call<CountModel> call, retrofit2.Response<CountModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                CountModel dr = response.body();
                if (dr.isStatus()) {
                    if (dr.getUnreadCountModel().getChatCount() == null || dr.getUnreadCountModel().getChatCount().equals("0")) {
                        txtChatUnread.setVisibility(View.GONE);
                    } else {
                        txtChatUnread.setVisibility(View.VISIBLE);
                        txtChatUnread.setText(dr.getUnreadCountModel().getChatCount());
                    }

                    if (dr.getUnreadCountModel().getNotificationCount() == null || dr.getUnreadCountModel().getNotificationCount().equals("0")) {
                        txtNotificationUnread.setVisibility(View.GONE);
                    } else {
                        txtNotificationUnread.setVisibility(View.VISIBLE);
                        txtNotificationUnread.setText(dr.getUnreadCountModel().getNotificationCount());
                    }
                    if (dr.getUnreadCountModel().getChatCount().equals("0") && dr.getUnreadCountModel().getNotificationCount().equals("0")) {
                        txt_total_count.setVisibility(View.GONE);
                    } else {
                        txt_total_count.setVisibility(View.VISIBLE);
                        String totalCount = String.valueOf(Integer.valueOf(dr.getUnreadCountModel().getChatCount()) + Integer.valueOf(dr.getUnreadCountModel().getNotificationCount()));
                        txt_total_count.setText(totalCount);
                    }


                } else {
                    Function.CustomMessage(BaseActivity.this, dr.getMessage());
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();

        if (Sessions.getSession(Constant.CityId, getApplicationContext()).equals("")) {
            txt_location.setText("All India");
            cityId = "0";
        } else {
            txt_location.setText(Sessions.getSession(Constant.CityName, getApplicationContext()));
            cityId = Sessions.getSession(Constant.CityId, getApplicationContext());
        }
        CallBackFunction();
        filterFunction();
//        if (cityList.size() == 0) {
//            GetCity();
//        }
//        FragmentFunction();
        Log.i("View call", "onPostCreate");
        ClickFunction();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MY_PROFILE_CODE) {
                txt_prosperId.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
                txt_prosperId_menu.setText(Sessions.getSession(Constant.ProsperId, getApplicationContext()));
                Glide.with(this)
                        .load(Sessions.getSession(Constant.UserProfile, getApplicationContext()))
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_default_profile)
                        .placeholder(R.drawable.ic_default_profile)
                        .into(img_profile_pic);
                if (Sessions.getSession(Constant.UserVerified, getApplicationContext()).equals("0")) {
                    img_unverified_user_home.setVisibility(View.VISIBLE);
                } else {
                    img_unverified_user_home.setVisibility(View.GONE);
                }
            } else if (requestCode == MY_POST_CODE) {
//                Intent h = new Intent(BaseActivity.this, HomeActivity.class);
//                h.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(h);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    protected void onRestart() {
        if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
            callUnreadCount();
        }
        super.onRestart();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

//    public void setSearchCallBack(SearchCallBack searchCallBack) {
//        this.searchCallBack = searchCallBack;
//    }
//
//    interface SearchCallBack {
//        void onSearchCall(String searchText);
//    }
}
