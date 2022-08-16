package com.dicoding.picodiploma.treasurehunt_kotlin.manohara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.treasurehunt_kotlin.R
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityManoharaMainBinding

class ManoharaMainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityManoharaMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManoharaMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }
}