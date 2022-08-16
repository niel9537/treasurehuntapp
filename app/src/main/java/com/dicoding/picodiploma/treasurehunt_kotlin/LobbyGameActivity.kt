package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.treasurehunt_kotlin.api.RetrofitClient
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.GameControlInterface
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ActivityLobbyGameBinding

class LobbyGameActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val preferencesName = "treasureHunt" //copas ini
    private val tokenGame = "key_token_game"
    private val tokenKey = "key_token"
    private lateinit var binding : ActivityLobbyGameBinding
    private lateinit var viewModel : ViewModel
    private var readyValue : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLobbyGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE) //inisialisasi fitur shared preference

        viewModel = ViewModelProvider(this)[ViewModel::class.java]//inisialisasi fitur viewmodel

        binding.readyButton.setOnClickListener {
//            BELUM BERHASIL BY : WAHYU
//            val readyCheck = gameControl.readyCheck(getTokenUser().toString(), getTokenGame().toString())
//            Log.d(String(),readyCheck.body().toString())
//            if(readyCheck.isSuccessful){
//                Log.d("wnb-start: ", readyCheck.body().toString())
//                // DISABLE TOMBOL READY DAN UBAH WARNA MENJADI DISABLED
//            }else{
//                Log.d("gagal ready", readyCheck.body().toString())
//            }

            viewModel.getReadyCheck(getTokenUser().toString(), getTokenGame().toString())

            viewModel.loginResponse().observe(this) {
                if (it.responseCode == 200){
                    binding.readyButton.apply {
                        isClickable = false
                        text = StringBuilder("Sudah Ready!")

                    }
                }
                else {
                    binding.readyButton.apply {
                        text = StringBuilder("Ready!")
                        setBackgroundColor(ContextCompat.getColor(context, R.color.login_gray))
                    }
                }
            }
        }

        binding.playGameButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        binding.playGameButton.setOnClickListener {

//            BELUM BERHASIL BY : WAHYU
//            Log.d(String(), "wnb-start: start ditekan")
//            val startGame = gameControl.startGame(getTokenUser().toString(), getTokenGame().toString())
//
//            if(startGame.isSuccessful){
//                Log.d("wnb-start: ", startGame.body().toString())
//                // MASUK KE GAME ACTIVITY (MULAI PERMAINAN)
//            }
//            Log.d("gagal start", startGame.body().toString())

            //else toast = response message
//
            viewModel.start(getTokenUser().toString(), getTokenGame().toString())

            viewModel.startGame().observe(this){
                if (it != null){
                    if (it.responseCode == 200){
                        val intent = Intent(this, PlayGameActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this, it.responseMessage, Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getTokenUser() : String? = sharedPreferences.getString(tokenKey, null)
    private fun getTokenGame() : String? = sharedPreferences.getString(tokenGame, null)
}