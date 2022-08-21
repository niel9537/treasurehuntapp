package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.google.zxing.Result;

public class ActivityScanOVJ extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String FLOW_ID = "";
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
            CodeScannerView scannerView = findViewById(R.id.scanner_view);
            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ActivityScanOVJ.this,ActivityPlayGame.class);
                            intent.putExtra("POST_ID",result.getText().toString());
                            intent.putExtra("FLOW_ID",FLOW_ID);
                            intent.putExtra("STATUS", Config.OVJ);
                            Log.d("FILE_ID ID OVJ",""+result.getText().toString());
                            startActivity(intent);
                            //Toast.makeText(ActivityScan.this, result.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            mCodeScanner.startPreview();


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
