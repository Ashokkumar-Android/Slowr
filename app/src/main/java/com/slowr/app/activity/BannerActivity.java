package com.slowr.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slowr.app.R;

public class BannerActivity extends AppCompatActivity {

    TextView txt_page_title;
    LinearLayout img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        doDeclaration();
    }

    private void doDeclaration() {
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);

        txt_page_title.setText(getString(R.string.txt_banner));
    }
}
