package com.slowr.app.activity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.slowr.app.R;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;
import com.slowr.app.models.PrivacyModel;

import retrofit2.Call;

public class PolicyActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_page_title;
    TextView txt_privacy_policy;
    TextView txt_terms_conditions;
    LinearLayout img_back;
    String pageFrom = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        doDeclaration();
    }

    private void doDeclaration() {
        if (getIntent().hasExtra("PageFrom")) {
            pageFrom = getIntent().getStringExtra("PageFrom");
        } else {
            pageFrom = "1";
        }
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        txt_terms_conditions = findViewById(R.id.txt_terms_conditions);
        txt_privacy_policy = findViewById(R.id.txt_privacy_policy);


        img_back.setOnClickListener(this);

        if (pageFrom.equals("1")) {
            txt_privacy_policy.setVisibility(View.VISIBLE);
            txt_terms_conditions.setVisibility(View.GONE);
            txt_page_title.setText(getString(R.string.nav_privacy_policy));
            getPolicy();
        } else {
            txt_privacy_policy.setVisibility(View.GONE);
            txt_terms_conditions.setVisibility(View.VISIBLE);
            txt_page_title.setText(getString(R.string.nav_terms_conditions));
            getTC();
        }


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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void getTC() {
        RetrofitClient.getClient().create(Api.class).getTC()
                .enqueue(new RetrofitCallBack(PolicyActivity.this, adListResponse, true,false));

    }

    private void getPolicy() {
        RetrofitClient.getClient().create(Api.class).getPrivacy()
                .enqueue(new RetrofitCallBack(PolicyActivity.this, adListResponse, true,false));

    }

    retrofit2.Callback<PrivacyModel> adListResponse = new retrofit2.Callback<PrivacyModel>() {
        @Override
        public void onResponse(Call<PrivacyModel> call, retrofit2.Response<PrivacyModel> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);
            PrivacyModel privacyModel = response.body();
            try {
                if (privacyModel.isStatus()) {


                    if (pageFrom.equals("1")) {
                        txt_privacy_policy.setText(Html.fromHtml(privacyModel.getPrivacyData()));
                    } else {
                        txt_terms_conditions.setText(Html.fromHtml(privacyModel.getPrivacyData()));
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
}
