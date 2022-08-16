package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityWelcomeGameBinding

class WelcomeGameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWelcomeGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.welcomeLayout.setOnClickListener {
            val intent = Intent(this, LobbyGameActivity::class.java)

            startActivity(intent)
        }


    }
}