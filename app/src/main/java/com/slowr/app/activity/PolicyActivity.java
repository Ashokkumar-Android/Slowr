package com.slowr.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.slowr.app.R;

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
        pageFrom = getIntent().getStringExtra("PageFrom");
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        txt_terms_conditions = findViewById(R.id.txt_terms_conditions);
        txt_privacy_policy = findViewById(R.id.txt_privacy_policy);


        img_back.setOnClickListener(this);

        if(pageFrom.equals("1")){
            txt_privacy_policy.setVisibility(View.VISIBLE);
            txt_terms_conditions.setVisibility(View.GONE);
            txt_page_title.setText(getString(R.string.nav_privacy_policy));
        }else {
            txt_privacy_policy.setVisibility(View.GONE);
            txt_terms_conditions.setVisibility(View.VISIBLE);
            txt_page_title.setText(getString(R.string.nav_terms_conditions));
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
}
