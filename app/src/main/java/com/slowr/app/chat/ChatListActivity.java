package com.slowr.app.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.slowr.app.R;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.ProductChatItemModel;
import com.slowr.app.models.ProductChatModel;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;
import com.slowr.app.utils.Sessions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class ChatListActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_page_title;
    LinearLayout img_back;
    RecyclerView rc_chat_list;

    ArrayList<ProductChatItemModel> productList = new ArrayList<>();
    ChatListAdapter chatListAdapter;

    HashMap<String, String> params = new HashMap<String, String>();
    private Function _fun = new Function();

    String catId = "";
    String adId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        doDeclaration();
    }

    private void doDeclaration() {
        catId = getIntent().getStringExtra("CatId");
        adId = getIntent().getStringExtra("AdId");
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        rc_chat_list = findViewById(R.id.rc_chat_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_chat_list.setLayoutManager(linearLayoutManager);
        rc_chat_list.setItemAnimator(new DefaultItemAnimator());
        chatListAdapter = new ChatListAdapter(productList, this);
        rc_chat_list.setAdapter(chatListAdapter);

        txt_page_title.setText(getString(R.string.nav_my_chat));
        img_back.setOnClickListener(this);
        getChatList();
        CallBackFunction();
    }

    private void CallBackFunction() {
        chatListAdapter.setCallBack(new ChatListAdapter.CallBack() {
            @Override
            public void onItemClick(int pos) {
                String _catId = productList.get(pos).getCatId();
                String adId = productList.get(pos).getAdId();
                String userId = productList.get(pos).getRenderPassId();
                String userProsperId = productList.get(pos).getProsperId();
                String userProUrl = productList.get(pos).getAdImage();
                String lastId = productList.get(pos).getLastId();
                Intent c = new Intent(ChatListActivity.this, ChatActivity.class);
                c.putExtra("CatId", _catId);
                c.putExtra("AdId", adId);
                c.putExtra("RenterId", userId);
                c.putExtra("ProsperId", userProsperId);
                c.putExtra("ProURL", userProUrl);
                c.putExtra("LastId", lastId);
                startActivity(c);
            }
        });
    }

    private void getChatList() {
        if (_fun.isInternetAvailable(ChatListActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getChatList(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ChatListActivity.this, productHistoryApi, true));
        } else {
            _fun.ShowNoInternetPopup(ChatListActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getChatList(catId, adId, Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ChatListActivity.this, productHistoryApi, true));
                }
            });
        }
    }

    retrofit2.Callback<ProductChatModel> productHistoryApi = new retrofit2.Callback<ProductChatModel>() {
        @Override
        public void onResponse(Call<ProductChatModel> call, retrofit2.Response<ProductChatModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);


            try {
                ProductChatModel dr = response.body();
                if (dr.isStatus()) {
                    productList.clear();
                    productList.addAll(dr.getProductChatList());
                    chatListAdapter.notifyDataSetChanged();

                } else {
                    Function.CustomMessage(ChatListActivity.this, dr.getMessage());
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
    protected void onRestart() {
        getChatList();
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
