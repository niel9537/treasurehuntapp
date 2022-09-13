package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;

public class ActivitySplashBrace extends AppCompatActivity {
    private int delayValue=2500;

    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;
        decorView.setSystemUiVisibility(uiOptions);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent home=new Intent(ActivitySplashBrace.this, ActivityLobby.class);
                startActivity(home);
                finish();

            }
        },delayValue);
    }
}
