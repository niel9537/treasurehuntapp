package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;

public class ActivityTransportInstruction extends AppCompatActivity {
    Button button_berangkat;
    TextView desc_berangkat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_transport_instruction);
        button_berangkat = findViewById(R.id.button_berangkat);
        desc_berangkat = findViewById(R.id.desc_berangkat);

    }
}
