package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.google.zxing.Result;

public class ActivityScanCheckOut extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String FLOW_ID = "";
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_activity);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FLOW_ID= extras.getString("FLOW_ID");
            Log.d("FLOW_ID SCAN CHECKOUT", " : " + FLOW_ID);
            //The key argument here must match that used in the other activity
        }
        btnBack = findViewById(R.id.btnBack);
            CodeScannerView scannerView = findViewById(R.id.scanner_view);
            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ActivityScanCheckOut.this,ActivityPlayGame.class);
                            intent.putExtra("POST_ID",result.getText().toString());
                            intent.putExtra("FLOW_ID",FLOW_ID);
                            intent.putExtra("STATUS", Config.CEK_OUT);
                            Log.d("POST ID SCAN",""+result.getText().toString());
                            startActivity(intent);
                            //Toast.makeText(ActivityScan.this, result.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            mCodeScanner.startPreview();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityScanCheckOut.this,ActivityPlayGame.class);
                intent.putExtra("FLOW_ID",FLOW_ID);
                intent.putExtra("STATUS", Config.PREV);
                startActivity(intent);
            }
        });
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
