package com.slowr.app.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.slowr.app.R;
import com.slowr.app.api.Api;
import com.slowr.app.api.RetrofitCallBack;
import com.slowr.app.api.RetrofitClient;

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
        DeepLink();
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

    private void DeepLink() {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.slowr.in/"))
                .setDomainUriPrefix("https://devlink.slowr.in")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.slowr.ios.beta").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.i("Share Link", String.valueOf(dynamicLinkUri));


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Toast.makeText(getApplicationContext(), String.valueOf(deepLink), Toast.LENGTH_SHORT).show();
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
                .enqueue(new RetrofitCallBack(PolicyActivity.this, adListResponse, true));

    }

    private void getPolicy() {
        RetrofitClient.getClient().create(Api.class).getPrivacy()
                .enqueue(new RetrofitCallBack(PolicyActivity.this, adListResponse, true));

    }

    retrofit2.Callback<String> adListResponse = new retrofit2.Callback<String>() {
        @Override
        public void onResponse(Call<String> call, retrofit2.Response<String> response) {

            Log.d("Response", response.isSuccessful() + " : " + response.raw());//response.body()!=null);

            try {
                if (pageFrom.equals("1")) {
                    txt_privacy_policy.setText(response.body());
                } else {
                    txt_terms_conditions.setText(response.body());
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
