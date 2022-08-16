package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.picodiploma.treasurehunt_kotlin.api.RetrofitClient
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.GameInterface
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.detail.Game
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.FragmentGamesBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GamesFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences // deklarasi fitur shared preference
    private val preferencesName = "treasureHunt" //key shared preference app
    private val tokenKey = "key_token" //key shared preference token
    private lateinit var adapter: GameBraceAdapter
    private lateinit var listAdapter: ListGameAdapter
    private val list = ArrayList<BraceGameData>()
    private lateinit var dot : ArrayList<TextView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGamesBinding.inflate(inflater, container, false)

        activity?.actionBar?.hide()

        sharedPreferences = requireActivity().getSharedPreferences(preferencesName, Context.MODE_PRIVATE) //inisialisasi fitur shared preference

        val viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]//inisialisasi fitur viewmodel

        list.add(
            BraceGameData(
                R.drawable.banner_brace,
                "BRACE",
                "2022"
            )
        )

        list.add(
            BraceGameData(
                R.drawable.banner_manohara,
                "MANOHARA",
                ""
            )
        )

        adapter = GameBraceAdapter(list)
        binding.viewPagerList.adapter = adapter
        dot = ArrayList()

        viewModel.listGameApi(getTokenUser().toString())

        viewModel.getListGame().observe(requireActivity()) {
            listAdapter = ListGameAdapter()

            listAdapter.listTransaksi(it)
            binding.listGameRv.adapter = listAdapter
            binding.listGameRv.layoutManager = LinearLayoutManager(context)

        }

        setIndicator()

        binding.viewPagerList.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                selectedImage(position)
                super.onPageSelected(position)
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun selectedImage(position: Int) {
        for (i in 0 until list.size){
            if (i == position){
                dot[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.yellow))
            }
            else{
                dot[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.text))
            }
        }
    }

    private fun setIndicator() {

        for(i in 0 until list.size){
            dot.add(TextView(requireContext()))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dot[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            }
            else {
                dot[i].text = Html.fromHtml("&#9679")
            }

            dot[i].textSize = 12f
            view?.findViewById<LinearLayout>(R.id.indikator_game)?.addView(dot[i])
        }
    }

    private fun getTokenUser() : String? = sharedPreferences.getString(tokenKey, null)

}