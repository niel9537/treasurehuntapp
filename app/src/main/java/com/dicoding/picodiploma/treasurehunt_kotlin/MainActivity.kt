package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityMainBinding
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
/*
        val navHost : NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_graph) as NavHostFragment
        val navController = navHost.navController

        NavigationUI.setupActionBarWithNavController(this, navController)

 */

        bottomNav = binding.bottomNav
        loadFragment(HomeFragment())

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.home -> {
                    loadFragment(HomeFragment())

                    true
                }
                R.id.games -> {
                    loadFragment(GamesFragment())

                    true
                }
                R.id.account -> {
                    loadFragment(AccountFragment())

                    true
                }
                else -> false
            }
        }



    }

    private fun loadFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}