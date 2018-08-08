package com.example.nfukunaga.androidtwitterapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends Activity {
    final Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_app_splash);
        ImageView splashImage =findViewById(R.id.imageView);
        splashImage.setImageResource(R.drawable.icon);
        changeActivityWait();
    }
    public void changeActivityWait(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(getApplicationContext(),OAuthActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
