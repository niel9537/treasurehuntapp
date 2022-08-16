package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityLobbyBinding

class LobbyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLobbyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.root.setOnClickListener {
            val intent = Intent(this, LobbyGameActivity::class.java)
            startActivity(intent)
        }
    }
}