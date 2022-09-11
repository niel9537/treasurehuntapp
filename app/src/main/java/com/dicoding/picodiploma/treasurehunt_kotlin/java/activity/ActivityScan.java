package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.google.zxing.Result;

public class ActivityScan extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    String FLOW_ID = "";
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            FLOW_ID= extras.getString("FLOW_ID");
            Log.d("FLOW_ID", " : " + FLOW_ID);
            //The key argument here must match that used in the other activity
        }
        setContentView(R.layout.activity_scan_qr_activity);
        btnBack = findViewById(R.id.btnBack);
            CodeScannerView scannerView = findViewById(R.id.scanner_view);
            mCodeScanner = new CodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ActivityScan.this,ActivityPlayGame.class);
                            intent.putExtra("POST_ID",result.getText().toString());
                            intent.putExtra("FLOW_ID",FLOW_ID);
                            intent.putExtra("STATUS", Config.CEK_IN);
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
                Intent intent = new Intent(ActivityScan.this,ActivityPlayGame.class);
                intent.putExtra("FLOW_ID",FLOW_ID);
                intent.putExtra("STATUS", Config.PREV);
                startActivity(intent);
            }
        });

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
