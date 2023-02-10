package com.codizcdp.foodbanksurplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.codizcdp.foodbanksurplus.provider.ProviderMainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Thread thread = new Thread(){

            public void run() {
                try {

                    sleep(2000);

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {

                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }

        };
        thread.start();
    }
    public void nxtActivity(View view) {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
    }

}