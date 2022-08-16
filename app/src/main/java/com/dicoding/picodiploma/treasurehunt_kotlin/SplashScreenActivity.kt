package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val handler = Handler()
    private val delayValue : Int = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        handler.postDelayed({
            val intentSplash = Intent(this, OnBoardActivity::class.java)
            startActivity(intentSplash)
            finish()
        }, delayValue.toLong())

    }
}