package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.slowr.app.R;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Sessions;

public class HomeActivityTemp extends AppCompatActivity implements View.OnClickListener {

    LinearLayout img_back;
    TextView txt_page_title;
    TextView txt_page_action;
    FloatingActionButton fb_add;
    Button btn_dashboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doDeclaration();
    }

    private void doDeclaration() {
        img_back = findViewById(R.id.img_back);
        img_back.setVisibility(View.GONE);
        txt_page_title = findViewById(R.id.txt_page_title);
        txt_page_action = findViewById(R.id.txt_page_action);
        btn_dashboard = findViewById(R.id.btn_dashboard);
        txt_page_title.setText(getString(R.string.app_name));

        fb_add = findViewById(R.id.fb_add);

        fb_add.setOnClickListener(this);
        txt_page_action.setOnClickListener(this);
        btn_dashboard.setOnClickListener(this);
        if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
            txt_page_action.setText(getString(R.string.txt_log_out));
        } else {
            txt_page_action.setText(getString(R.string.txt_sigin));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_add:
                Intent p = new Intent(HomeActivityTemp.this, BaseActivity.class);
                p.putExtra("AdType", 0);
                startActivity(p);
                break;
            case R.id.txt_page_action:
                if (Sessions.getSessionBool(Constant.LoginFlag, getApplicationContext())) {
                    Intent l = new Intent(HomeActivityTemp.this, LoginActivity.class);
                    startActivity(l);
                } else {
                    Intent l = new Intent(HomeActivityTemp.this, LoginActivity.class);
                    startActivity(l);
                }

                break;
            case R.id.btn_dashboard:
                Intent d = new Intent(HomeActivityTemp.this, DashBoardActivity.class);
                startActivity(d);
                break;
        }
    }
}
