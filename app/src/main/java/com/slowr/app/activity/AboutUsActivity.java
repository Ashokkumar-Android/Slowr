package com.slowr.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
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
    TextView txt_about_content;

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
        txt_about_content = findViewById(R.id.txt_about_content);

        txt_page_title.setText(getString(R.string.nav_about_us));
        img_back.setOnClickListener(this);
        txt_web_site.setOnClickListener(this);
        img_twitter.setOnClickListener(this);
        img_face_book.setOnClickListener(this);
        img_instagram.setOnClickListener(this);
        txt_about_content.setText(Html.fromHtml("<!DOCTYPE html>\n" +
                "<html>" +
                "<body>" +
                "<h2>About Us</h2>" +
                "<p>World’s first Online Classifieds to Rent or Rent out the Products and Hire or Get Hired for Services - Temporarily.</p>" +
                "<h2>Vision</h2>" +
                "<p>To increase the Revenue of every Indian Household.</p>" +
                "<h2>Mission</h2>" +
                "<p>To make People realize that each idle product in their home can produce revenue and every person in the family can generate revenue on their idle time. To keep building this platform rigid to accommodate the entire Indians to exchange their Products and Services and thus increasing the per capita as well as happiness.</p>" +
                "</body>" +
                "</html>"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_web_site:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.slowr.com"));
                startActivity(browserIntent);
                break;
            case R.id.img_twitter:
                try {
                    Intent t = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/onboardslowr"));
                    startActivity(t);
                } catch (Exception e) {
                }

                break;
            case R.id.img_face_book:

                try {
                    Intent f = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/onboardslowr"));
                    startActivity(f);
                } catch (Exception e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/slowr.india.1")));
                }

                break;
            case R.id.img_instagram:
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/onboard_slowr?igshid=19grjpoyx6als"));
                    startActivity(i);
                } catch (Exception e) {
//                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/slowrindia/"));
//                    startActivity(i);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
