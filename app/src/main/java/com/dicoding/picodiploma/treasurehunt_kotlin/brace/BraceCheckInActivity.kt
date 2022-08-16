package com.dicoding.picodiploma.treasurehunt_kotlin.brace

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.dicoding.picodiploma.treasurehunt_kotlin.R
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityBraceCheckInBinding

class BraceCheckInActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBraceCheckInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBraceCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.openCamera.setOnClickListener{
            startActivity(Intent(this, ScanQRActivity::class.java))
        }
    }
}