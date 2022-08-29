package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityDetailBraceBinding

class DetailBraceActivity : AppCompatActivity() {
    private  lateinit var binding : ActivityDetailBraceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBraceBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        supportActionBar?.hide()

        binding.backDetail.setOnClickListener{
            val intentSplash = Intent(this, MainActivity::class.java)
            startActivity(intentSplash)
        }*/
    }
}