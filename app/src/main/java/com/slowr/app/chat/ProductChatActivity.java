package com.slowr.app.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class ProductChatActivity extends AppCompatActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    TextView txt_page_title;
    LinearLayout img_back;
    RecyclerView rc_chat_product;
    EditText edt_search_ad;
    LinearLayout layout_list;
    ImageView img_no_chat;
    SwipeRefreshLayout layout_swipe_refresh;

    ProductChatAdapter productChatAdapter;
    ArrayList<ProductChatItemModel> productList = new ArrayList<>();

    HashMap<String, String> params = new HashMap<String, String>();
    private Function _fun = new Function();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_chat);
        doDeclaration();
    }

    private void doDeclaration() {
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        rc_chat_product = findViewById(R.id.rc_chat_product);
        edt_search_ad = findViewById(R.id.edt_search_ad);
        layout_list = findViewById(R.id.layout_list);
        img_no_chat = findViewById(R.id.img_no_chat);
        layout_swipe_refresh = findViewById(R.id.layout_swipe_refresh);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rc_chat_product.setLayoutManager(linearLayoutManager);
        rc_chat_product.setItemAnimator(new DefaultItemAnimator());
        productChatAdapter = new ProductChatAdapter(productList, this);
        rc_chat_product.setAdapter(productChatAdapter);

        layout_swipe_refresh.setColorSchemeColors(getResources().getColor(R.color.txt_orange));
        layout_swipe_refresh.setOnRefreshListener(this);

        txt_page_title.setText(getString(R.string.nav_my_chat));
        img_back.setOnClickListener(this);
        getProductHistory();
        CallBackFunction();
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
                productChatAdapter.getFilter().filter(edt_search_ad.getText().toString());

            }
        });
    }

    private void CallBackFunction() {
        productChatAdapter.setCallBack(new ProductChatAdapter.CallBack() {
            @Override
            public void onItemClick(ProductChatItemModel model) {
                String catId = model.getCatId();
                String adId = model.getChatAdId();
                Intent i = new Intent(ProductChatActivity.this, ChatListActivity.class);
                i.putExtra("CatId", catId);
                i.putExtra("AdId", adId);
                startActivity(i);
            }
        });
    }

    private void getProductHistory() {


        if (_fun.isInternetAvailable(ProductChatActivity.this)) {
            RetrofitClient.getClient().create(Api.class).getProductChat(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                    .enqueue(new RetrofitCallBack(ProductChatActivity.this, productHistoryApi, true));
        } else {
            _fun.ShowNoInternetPopup(ProductChatActivity.this, new Function.NoInternetCallBack() {
                @Override
                public void isInternet() {
                    RetrofitClient.getClient().create(Api.class).getProductChat(Sessions.getSession(Constant.UserToken, getApplicationContext()))
                            .enqueue(new RetrofitCallBack(ProductChatActivity.this, productHistoryApi, true));
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
                    productChatAdapter.notifyDataSetChanged();
                    productList.addAll(dr.getProductChatList());
                    productChatAdapter.notifyDataSetChanged();
                    if(productList.size()!=0){
                        layout_list.setVisibility(View.VISIBLE);
                        img_no_chat.setVisibility(View.GONE);
                    }else {
                        layout_list.setVisibility(View.GONE);
                        img_no_chat.setVisibility(View.VISIBLE);
                    }

                } else {
                    Function.CustomMessage(ProductChatActivity.this, dr.getMessage());
                    if(productList.size()!=0){
                        layout_list.setVisibility(View.VISIBLE);
                        img_no_chat.setVisibility(View.GONE);
                    }else {
                        layout_list.setVisibility(View.GONE);
                        img_no_chat.setVisibility(View.VISIBLE);
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
        }
    };

    @Override
    protected void onRestart() {
        getProductHistory();
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

    @Override
    public void onRefresh() {
        if (layout_swipe_refresh.isRefreshing()) {
            layout_swipe_refresh.setRefreshing(false);
        }
        getProductHistory();
    }
}
