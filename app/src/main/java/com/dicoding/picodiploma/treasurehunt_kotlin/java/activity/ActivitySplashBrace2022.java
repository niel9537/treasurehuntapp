package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;

public class ActivitySplashBrace2022 extends AppCompatActivity {
    private int delayValue=2500;
    String FLOW_ID = "";
    String CONTENT = "";
    //4000=4 detik
    Button btnContinue;
    TextView txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_brace2022);
        Bundle extras = getIntent().getExtras();
        btnContinue = findViewById(R.id.txtContinue);
        txtContent = findViewById(R.id.txtContent);

        if (extras != null) {
            FLOW_ID= extras.getString("FLOW_ID");
            CONTENT= extras.getString("CONTENT");
            //The key argument here must match that used in the other activity
        }
        txtContent.setText(CONTENT);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home=new Intent(ActivitySplashBrace2022.this, ActivityPlayGame.class);
                home.putExtra("FLOW_ID",FLOW_ID);
                home.putExtra("STATUS", Config.DESK);
                startActivity(home);

            }
        });
    }
}
