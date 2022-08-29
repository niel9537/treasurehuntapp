package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityDetailGameBinding

class DetailGameActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailGameBinding
    private lateinit var adapter: HomeBraceAdapter
    private val list = ArrayList<BraceData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        list.add(
            BraceData(
                R.drawable.brace1
            )
        )

        list.add(
            BraceData(
                R.drawable.brace2
            )
        )

        list.add(
            BraceData(
                R.drawable.brace3
            )
        )

        adapter = HomeBraceAdapter(list)
        binding.viewPagerDetailGame.adapter = adapter

        binding.playGameDetailButton.setOnClickListener {
            val intent = Intent(this, WelcomeGameActivity::class.java)

            startActivity(intent)
        }
/*
        binding.backDetail.setOnClickListener{
            val intentSplash = Intent(this, MainActivity::class.java)
            startActivity(intentSplash)
        }*/
    }
}