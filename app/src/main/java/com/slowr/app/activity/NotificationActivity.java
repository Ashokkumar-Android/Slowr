package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.slowr.app.R;
import com.slowr.app.adapter.NotificationAdapter;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.DefaultResponse;
import com.slowr.app.models.NotificationItemModel;
import com.slowr.app.models.NotificationModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    TextView txt_page_title;
    LinearLayout img_back;
    RecyclerView rc_notification;
    LinearLayout layout_no_result;
    Button btn_my_ads;
    SwipeRefreshLayout layout_swipe_refresh;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationItemModel> notificationList = new ArrayList<>();
    private Function _fun = new Function();
    LinearLayoutManager listManager;
    HashMap<String, Object> params = new HashMap<String, Object>();

    int readPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        doDeclaration();
    }

    private void doDeclaration() {

        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        rc_notification = findViewById(R.id.rc_notification);
        layout_no_result = findViewById(R.id.layout_no_result);
        btn_my_ads = findViewById(R.id.btn_my_ads);
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);

        listManager = new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL, false);
        rc_notification.setLayoutManager(listManager);
        rc_notification.setItemAnimator(new DefaultItemAnimator());
        notificationAdapter = new NotificationAdapter(notificationList, this);
        rc_notification.setAdapter(notificationAdapter);

        txt_page_title.setText(getString(R.string.nav_notification));
        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        layout_swipe_refresh.setOnRefreshListener(this);
        if (_fun.isInternetAvailable(NotificationActivity.this)) {
            getNotificationList(true);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(NotificationActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getNotificationList(true);
                        }
                    });
                }
            }, 200);

        }
        btn_my_ads.setOnClickListener(this);
        img_back.setOnClickListener(this);
        CallBackFunction();
    }

    private void CallBackFunction() {
        notificationAdapter.setCallback(new NotificationAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                readPos = pos;
                String noteId = notificationList.get(pos).getNotificationId();
                String isRead = notificationList.get(pos).getIsRead();
                if (notificationList.get(pos).getNotificationType().equals("2")) {
                    String catId = notificationList.get(pos).getCatId();
                    String adId = notificationList.get(pos).getAdId();
//                    String userId = notificationList.get(pos).getUserId();
                    Intent p = null;
//                    if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
//                        p = new Intent(NotificationActivity.this, MyPostViewActivity.class);
//                    } else {
//                        p = new Intent(NotificationActivity.this, PostViewActivity.class);
//                    }
                    p = new Intent(NotificationActivity.this, MyPostViewActivity.class);
                    p.putExtra("CatId", catId);
                    p.putExtra("AdId", adId);
                    startActivity(p);
                } else if (notificationList.get(pos).getNotificationType().equals("1")) {
                    Intent profile = new Intent(NotificationActivity.this, ProfileActivity.class);
                    startActivity(profile);
                } else if (notificationList.get(pos).getNotificationType().equals("4")) {
                    Intent not = new Intent(NotificationActivity.this, TransactionActivity.class);
                    startActivity(not);
                }
                if (isRead.equals("0")) {
                    ReadNotification(noteId);
                }
            }
        });
    }


    private void getNotificationList(boolean isLoad) {
        RetrofitClient.getClient().create(Api.class).getNotificationList(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(NotificationActivity.this, adListResponse, isLoad));

    }

    private void ReadNotification(String noteId) {
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("notification_id", noteId);
        Log.i("Params", params.toString());


        if (_fun.isInternetAvailable(NotificationActivity.this)) {
            RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(NotificationActivity.this, noteReadResponse, false));
        } else {
            _fun.ShowNoInternetPopup(NotificationActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(NotificationActivity.this, noteReadResponse, false));
                }
            });
        }
    }

    retrofit2.Callback<NotificationModel> adListResponse = new retrofit2.Callback<NotificationModel>() {
        @Override
        public void onResponse(Call<NotificationModel> call, retrofit2.Response<NotificationModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            NotificationModel dr = response.body();
            try {
                if (layout_swipe_refresh.isRefreshing()) {
                    layout_swipe_refresh.setRefreshing(false);
                }
                if (dr.isStatus()) {

//                    Log.i("ImagePathUrl", dr.getUrlPath());
//                    Sessions.saveSession(Constant.ImagePath, dr.getUrlPath(), FavoriteActivity.this);

                    notificationList.clear();
                    notificationList.addAll(dr.getNotificationItemModelArrayList());
                    notificationAdapter.notifyDataSetChanged();
                    if (notificationList.size() == 0) {
                        layout_no_result.setVisibility(View.VISIBLE);
                        rc_notification.setVisibility(View.GONE);
                    } else {
                        layout_no_result.setVisibility(View.GONE);
                        rc_notification.setVisibility(View.VISIBLE);
                    }
                } else {
                    Function.CustomMessage(NotificationActivity.this, dr.getMessage());
                    if (notificationList.size() == 0) {
                        layout_no_result.setVisibility(View.VISIBLE);
                        rc_notification.setVisibility(View.GONE);
                    } else {
                        layout_no_result.setVisibility(View.GONE);
                        rc_notification.setVisibility(View.VISIBLE);
                    }
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

    retrofit2.Callback<DefaultResponse> noteReadResponse = new retrofit2.Callback<DefaultResponse>() {
        @Override
        public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            DefaultResponse dr = response.body();
            try {
                if (dr.isStatus()) {
                    notificationList.get(readPos).setIsRead("1");
                    notificationAdapter.notifyItemChanged(readPos);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_my_ads:
                Intent d = new Intent(NotificationActivity.this, DashBoardActivity.class);
                startActivity(d);
                finish();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {

        if (_fun.isInternetAvailable(NotificationActivity.this)) {
            getNotificationList(false);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _fun.ShowNoInternetPopup(NotificationActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            getNotificationList(false);
                        }
                    });
                }
            }, 200);

        }
    }
}
