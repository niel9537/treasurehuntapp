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
    String TITLE = "";
    String SUBTITLE = "";
    //4000=4 detik
    Button btnContinue;
    TextView txtContent,txtTitle,txtSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_brace2022);
        Bundle extras = getIntent().getExtras();
        btnContinue = findViewById(R.id.txtContinue);
        txtContent = findViewById(R.id.txtContent);
        txtTitle = findViewById(R.id.txtTitle);
        txtSubtitle = findViewById(R.id.txtSubtitle);
        if (extras != null) {
            FLOW_ID= extras.getString("FLOW_ID");
            CONTENT= extras.getString("CONTENT");
            TITLE= extras.getString("TITLE");
            SUBTITLE = extras.getString("SUBTITLE");
            //The key argument here must match that used in the other activity
        }
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;
        decorView.setSystemUiVisibility(uiOptions);
        txtContent.setText(CONTENT);
        txtTitle.setText(TITLE);
        txtSubtitle.setText(SUBTITLE);
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
