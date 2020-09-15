package com.slowr.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.adapter.ImageFullviewPagerAdapter;
import com.slowr.app.models.UploadImageModel;

import java.util.ArrayList;

public class ImageViewActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager vp_image_view;
    LinearLayout layout_back;
    ImageView img_profile_pic;
    ImageFullviewPagerAdapter eventViewPagerAdapter;
    ArrayList<UploadImageModel> shareImageList = new ArrayList<>();
    String imgUrl = "";
    String imgPath = "";
    boolean isImageLocal = false;
    int imgPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        doDeclaration();
    }

    private void doDeclaration() {
        imgUrl = getIntent().getStringExtra("ImgURL");
        imgPos = getIntent().getIntExtra("ImgPos", 0);
        vp_image_view = findViewById(R.id.vp_image_view);
        layout_back = findViewById(R.id.layout_back);
        img_profile_pic = findViewById(R.id.img_profile_pic);
        layout_back.setOnClickListener(this);
        if (!isImageLocal) {
            vp_image_view.setVisibility(View.VISIBLE);
            img_profile_pic.setVisibility(View.GONE);
            if (shareImageList.size() != 0) {
                shareImageList.clear();
            }
            if (imgUrl.contains(",")) {
                String[] separated = imgUrl.split(",");

                for (int i = 0; i < separated.length; i++) {
                    shareImageList.add(new UploadImageModel(separated[i], false, "", "1"));
                }
            } else {
                shareImageList.add(new UploadImageModel(imgUrl, false, "", "1"));
            }

            eventViewPagerAdapter = new ImageFullviewPagerAdapter(ImageViewActivity.this, shareImageList);
            vp_image_view.setAdapter(eventViewPagerAdapter);
            vp_image_view.setCurrentItem(imgPos);
        } else {
            vp_image_view.setVisibility(View.GONE);
            img_profile_pic.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(imgPath)
                    .into(img_profile_pic);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;
        }
    }
}
