package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.slowr.app.R;
import com.slowr.app.components.GifDrawableImageViewTarget;
import com.slowr.app.utils.Constant;
import com.slowr.app.utils.Function;

public class SplashActivity extends AppCompatActivity {
    private Function _fun = new Function();
    ImageView img_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        img_splash = findViewById(R.id.img_splash);
        Glide.with(this)
                .load(R.drawable.ic_splash_load)

                .into(new GifDrawableImageViewTarget(img_splash, 1));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Constant.ParentId = "";
                if (_fun.isInternetAvailable(SplashActivity.this)) {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {

                    _fun.ShowNoInternetPopup(SplashActivity.this, new Function.NoInternetCallBack() {
                        @Override
                        public void isInternet() {
                            Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

                }

            }
        }, 5000);
        Log.i("AppVersionCode", String.valueOf(Function.getAppVersionCode(SplashActivity.this)));
        Log.i("AppVersionName", Function.getAppVersionName(SplashActivity.this));
    }
}
