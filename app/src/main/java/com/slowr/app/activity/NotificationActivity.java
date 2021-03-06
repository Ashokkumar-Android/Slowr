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
    LinearLayout layout_delete;
    Button btn_my_ads;
    SwipeRefreshLayout layout_swipe_refresh;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationItemModel> notificationList = new ArrayList<>();
    private Function _fun = new Function();
    LinearLayoutManager listManager;
    HashMap<String, Object> params = new HashMap<String, Object>();

    int readPos = 0;
    boolean isDeleteRefresh = false;
    boolean isDeleteVisible = false;

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
        layout_delete = findViewById(R.id.layout_delete);

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
        layout_delete.setOnClickListener(this);
        CallBackFunction();
        ReadNotification("");
    }

    private void CallBackFunction() {
        notificationAdapter.setCallback(new NotificationAdapter.Callback() {
            @Override
            public void itemClick(int pos) {
                readPos = pos;
                String noteId = notificationList.get(pos).getNotificationId();
                String isRead = notificationList.get(pos).getIsRead();
                if (notificationList.get(pos).getNotificationType().equals("2")) {
                    String adId = notificationList.get(pos).getAdId();
//                    String userId = notificationList.get(pos).getUserId();
                    Intent p = null;
//                    if (userId.equals(Sessions.getSession(Constant.UserId, getApplicationContext()))) {
//                        p = new Intent(NotificationActivity.this, MyPostViewActivity.class);
//                    } else {
//                        p = new Intent(NotificationActivity.this, PostViewActivity.class);
//                    }
                    p = new Intent(NotificationActivity.this, MyPostViewActivity.class);
                    p.putExtra("AdId", adId);
                    startActivity(p);
                } else if (notificationList.get(pos).getNotificationType().equals("1")||notificationList.get(pos).getNotificationType().equals("6")) {
                    Intent profile = new Intent(NotificationActivity.this, ProfileActivity.class);
                    startActivity(profile);
                } else if (notificationList.get(pos).getNotificationType().equals("4")) {
                    Intent not = new Intent(NotificationActivity.this, TransactionActivity.class);
                    startActivity(not);
                } else if (notificationList.get(pos).getNotificationType().equals("5")) {
                    Intent not = new Intent(NotificationActivity.this, BannerActivity.class);
                    startActivity(not);
                }
//                if (isRead.equals("0")) {
//                    ReadNotification(noteId);
//                }
            }

            @Override
            public void deleteVisible(boolean isDelete) {
                if (isDelete) {
                    layout_delete.setVisibility(View.VISIBLE);
                    isDeleteVisible = true;
                } else {
                    layout_delete.setVisibility(View.GONE);
                    isDeleteVisible = false;
                }
            }
        });
    }


    private void getNotificationList(boolean isLoad) {
        RetrofitClient.getClient().create(Api.class).getNotificationList(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                .enqueue(new RetrofitCallBack(NotificationActivity.this, adListResponse, isLoad, false));

    }

    private void DeleteNotification() {

        String noteId = "";
        for (int i = 0; i < notificationList.size(); i++) {
            if (notificationList.get(i).isCheck()) {
                if (noteId.equals("")) {
                    noteId = notificationList.get(i).getNotificationId();
                } else {
                    noteId = noteId + "," + notificationList.get(i).getNotificationId();
                }
            }
        }
        if (!params.isEmpty()) {
            params.clear();
        }
        params.put("notification_ids", noteId);
        Log.i("Params", params.toString());


        if (_fun.isInternetAvailable(NotificationActivity.this)) {
            isDeleteRefresh = true;
            RetrofitClient.getClient().create(Api.class).deleteNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(NotificationActivity.this, noteReadResponse, true, false));

        } else {
            isDeleteRefresh = true;
            _fun.ShowNoInternetPopup(NotificationActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).deleteNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(NotificationActivity.this, noteReadResponse, true, false));
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


        if (_fun.isInternetAvailable(NotificationActivity.this)) {
            RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(NotificationActivity.this, noteReadResponse, false, false));
        } else {
            _fun.ShowNoInternetPopup(NotificationActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).ReadNotification(params, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(NotificationActivity.this, noteReadResponse, false, false));
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
//                    notificationList.get(readPos).setIsRead("1");
//                    notificationAdapter.notifyItemChanged(readPos);
                    if (isDeleteRefresh) {
                        isDeleteRefresh = false;
                        isDeleteVisible = false;
                        layout_delete.setVisibility(View.GONE);
                        notificationAdapter.RemoveSelection(false);
                        getNotificationList(true);
                    }
                } else {
                    if (isDeleteRefresh) {
                        isDeleteRefresh = false;
                        isDeleteVisible = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (isDeleteRefresh) {
                    isDeleteRefresh = false;
                    isDeleteVisible = false;
                }
            }
        }
//        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Log.d("TAG", t.getMessage());
            call.cancel();
            if (isDeleteRefresh) {
                isDeleteRefresh = false;
                isDeleteVisible = false;
            }
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
                if (isDeleteVisible) {
                    notificationAdapter.RemoveSelection(false);
                    for (int i = 0; i < notificationList.size(); i++) {
                        if (notificationList.get(i).isCheck()) {
                            notificationList.get(i).setCheck(false);
                        }
                    }
                    notificationAdapter.notifyDataSetChanged();
                    layout_delete.setVisibility(View.GONE);
                } else {
                    finish();
                }
                break;
            case R.id.layout_delete:
                DeleteNotification();
                break;
        }
    }

    @Override
    public void onRefresh() {
        Function.CoinTone(NotificationActivity.this);
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

    @Override
    protected void onRestart() {
        getNotificationList(true);
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        if (isDeleteVisible) {
            notificationAdapter.RemoveSelection(false);
            for (int i = 0; i < notificationList.size(); i++) {
                if (notificationList.get(i).isCheck()) {
                    notificationList.get(i).setCheck(false);
                }
            }
            notificationAdapter.notifyDataSetChanged();
            layout_delete.setVisibility(View.GONE);
        } else {
            finish();
            super.onBackPressed();
        }

    }
}
