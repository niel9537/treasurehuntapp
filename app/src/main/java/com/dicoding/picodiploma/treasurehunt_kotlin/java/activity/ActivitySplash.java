package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;

public class ActivitySplash extends AppCompatActivity {
    private int delayValue=2500;

    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_splash_screen);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;
        decorView.setSystemUiVisibility(uiOptions);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(FragmentLogin.SHARED_PREF_NAME,0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);
                if(hasLoggedIn){
                    //setelah loading maka akan langsung berpindah ke home activity
                    Intent home=new Intent(ActivitySplash.this, ActivityHome.class);
                    startActivity(home);
                    finish();
                }else{
                    //setelah loading maka akan langsung berpindah ke login activity
                    Intent home=new Intent(ActivitySplash.this, ActivityLogin.class);
                    startActivity(home);
                    finish();
                }


            }
        },delayValue);
    }
}
