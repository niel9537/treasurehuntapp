package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityOnBoardBinding
import java.lang.StringBuilder

class OnBoardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOnBoardBinding
    private lateinit var adapter: OnBoardAdapter
    private val list = ArrayList<OnBoardData>()
    private lateinit var dot : ArrayList<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        list.add(
            OnBoardData(
                R.drawable.illust1,
                "Rasakan Pengalaman Bermain\n" +
                        "Game yang Berbeda",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            )
        )

        list.add(
            OnBoardData(
                R.drawable.onboarding_illust_2,
                "Bermain sambil Belajar\n" +
                        "Hal-hal baru",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            )
        )

        list.add(
            OnBoardData(
                R.drawable.onboarding_illust_3,
                "Beragam Game dalam\n" +
                        "Satu Aplikasi",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            )
        )

        adapter = OnBoardAdapter(list)
        binding.slideImage.adapter = adapter
        dot = ArrayList()

        setIndicator()

        binding.slideImage.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                selectedImage(position)
                super.onPageSelected(position)
            }
        })

        binding.continueToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        }
    }


    private fun selectedImage(position: Int) {
        for (i in 0 until list.size){
            if (i == position){
                dot[i].setTextColor(ContextCompat.getColor(this, R.color.yellow))
            }
            else{
                dot[i].setTextColor(ContextCompat.getColor(this, R.color.text))
            }
        }
    }

    private fun setIndicator() {
        for(i in 0 until list.size){
            dot.add(TextView(this))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dot[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            }
            else {
                dot[i].text = Html.fromHtml("&#9679")
            }

            dot[i].textSize = 12f
            binding.indikator.addView(dot[i])
        }
    }
}