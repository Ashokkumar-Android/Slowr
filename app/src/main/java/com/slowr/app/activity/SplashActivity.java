package com.slowr.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.slowr.app.R;
import com.slowr.app.utils.Function;

public class SplashActivity extends AppCompatActivity {
    private Function _fun = new Function();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
        }, 3000);
    }
}
