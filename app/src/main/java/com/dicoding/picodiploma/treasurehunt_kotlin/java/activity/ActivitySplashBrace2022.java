package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;

public class ActivitySplashBrace2022 extends AppCompatActivity {
    private int delayValue=2500;
    String FLOW_ID = "";
    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_brace2022);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FLOW_ID= extras.getString("FLOW_ID");
            //The key argument here must match that used in the other activity
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent home=new Intent(ActivitySplashBrace2022.this, ActivityPlayGame.class);
                home.putExtra("FLOW_ID",FLOW_ID);
                home.putExtra("STATUS", Config.DESK);
                startActivity(home);

            }
        },delayValue);
    }
}
