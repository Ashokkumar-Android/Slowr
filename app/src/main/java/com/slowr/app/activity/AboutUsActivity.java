package com.slowr.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.slowr.app.R;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_page_title;
    LinearLayout img_back;
    TextView txt_web_site;

    ImageView img_twitter;
    ImageView img_face_book;
    ImageView img_instagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        doDeclaration();
    }

    private void doDeclaration() {
        txt_page_title = findViewById(R.id.txt_page_title);
        img_back = findViewById(R.id.img_back);
        txt_web_site = findViewById(R.id.txt_web_site);
        img_twitter = findViewById(R.id.img_twitter);
        img_face_book = findViewById(R.id.img_face_book);
        img_instagram = findViewById(R.id.img_instagram);

        txt_page_title.setText(getString(R.string.nav_about_us));
        img_back.setOnClickListener(this);
        txt_web_site.setOnClickListener(this);
        img_twitter.setOnClickListener(this);
        img_face_book.setOnClickListener(this);
        img_instagram.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_web_site:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.slowr.in"));
                startActivity(browserIntent);
                break;
            case R.id.img_twitter:
                try {
                    Intent t = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/DevTorbit"));
                    startActivity(t);
                } catch (Exception e) {

                }

                break;
            case R.id.img_face_book:
                Intent f = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/torbit.dev.9"));
                startActivity(f);

                break;
            case R.id.img_instagram:
//                try {
//                } catch (Exception e) {
//                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/gioco.app/"));
//                    startActivity(i);
//                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
