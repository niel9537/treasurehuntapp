package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.picodiploma.treasurehunt_kotlin.api.RetrofitClient
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.GameControlInterface
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.join_game.JoinBody
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.FragmentHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class HomeFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences // deklarasi fitur shared preference
    private val preferencesName = "treasureHunt" //key shared preference app
    private val tokenKey = "key_token"//key shared preference token
    private val tokenGame = "key_token_game" //key game
    private lateinit var adapter: HomeBraceAdapter
    private val list = ArrayList<BraceData>()
    private lateinit var dot : ArrayList<TextView>
    private val gameControl = RetrofitClient.init().create(GameControlInterface::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false) // Inflate the layout for this fragment

        sharedPreferences = requireActivity().getSharedPreferences(preferencesName, Context.MODE_PRIVATE) //inisialisasi fitur shared preference

        activity?.actionBar?.hide()

        Log.d("CHECKING: ", getTokenUser().toString())

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
        binding.viewPagerHome.adapter = adapter
        dot = ArrayList()

        setIndicator()

        binding.viewPagerHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                selectedImage(position)
                super.onPageSelected(position)
            }
        })

        val inputCode = binding.inputCode
        val playButton = binding.playButton

        inputCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                playButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.login_gray))
                playButton.isClickable = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                playButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))

                play()
            }

            override fun afterTextChanged(s: Editable?) {
                playButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            }

        })

        return binding.root
    }

    private fun continueGame(){
        val playButton = view?.findViewById<Button>(R.id.play_button)
        playButton?.text = StringBuilder("Continue Game")
        playButton?.setOnClickListener{
            startActivity(Intent(requireActivity(), PlayGameActivity::class.java))
        }

    }

    private fun play() {
        val inputCode = view?.findViewById<EditText>(R.id.input_code)
        val playButton = view?.findViewById<Button>(R.id.play_button)

        val viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]//inisialisasi fitur viewmodel

        viewModel.checkContinueGame(getTokenUser().toString(), inputCode?.text.toString())

        viewModel.responseContinueGame().observe(viewLifecycleOwner){
            if (it != null){
                playButton?.setOnClickListener{
                    if (inputCode?.text.toString().isNotEmpty()){
                        Log.d("API-login: ", getTokenUser().toString()+"%%%%%"+inputCode?.text.toString())

                        viewModel.join(getTokenUser().toString(), inputCode?.text.toString())

                        viewModel.joinGame().observe(viewLifecycleOwner){
                            if (it!=null){
                                saveTokenGame(inputCode?.text.toString())

                                startActivity(Intent(requireActivity(), LobbyGameActivity::class.java))
                            }
                            else {
                                Toast.makeText(activity, "Kode Permainan salah!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(activity, "Masukkan Kode Permainan!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                continueGame()

                playButton?.setOnClickListener{
                    startActivity(Intent(requireActivity(), PlayGameActivity::class.java))
                }
            }
        }



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
            view?.findViewById<LinearLayout>(R.id.indikator_home)?.addView(dot[i])
        }
    }

    //manggil value token.. pastikan key nya bener dan dah di deklarasi di atas
    private fun getTokenUser() : String? = sharedPreferences.getString(tokenKey, null)

    private fun saveTokenGame(token : String) {
        val user: SharedPreferences.Editor = sharedPreferences.edit()

        user.putString(tokenGame, token)
        user.apply()
    }

}