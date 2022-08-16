package com.dicoding.picodiploma.treasurehunt_kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.treasurehunt_kotlin.api.ApiBase
import com.dicoding.picodiploma.treasurehunt_kotlin.api.RetrofitClient
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.AuthInterface
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.login.LoginBody
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.login.LoginResponse
import com.dicoding.picodiploma.treasurehunt_kotlin.api.auth.registration.RegisterBody
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.GameControlInterface
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.join_game.Join
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.join_game.JoinBody
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.me.Me
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.ready_check.ReadyCheck
import com.dicoding.picodiploma.treasurehunt_kotlin.api.game_control.start_game.StartGame
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.GameInterface
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.detail.Game
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.list.GameDatas
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.list.Games
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel : ViewModel() {
    private val listGame = MutableLiveData<ArrayList<GameDatas>>()
    private val login = MutableLiveData<LoginResponse>()
    private val readyCheck = MutableLiveData<ReadyCheck>()
    private val joinGame = MutableLiveData<Join>()
    private val startGame = MutableLiveData<StartGame>()
    private val me = MutableLiveData<Me>()

    fun listGameApi(token : String){
        ApiBase.apiInterface.getGameLists(token).enqueue(object : Callback<Games> {
            override fun onResponse(call: Call<Games>, response: Response<Games>) {
                listGame.postValue(response.body()?.data!!)
            }

            override fun onFailure(call: Call<Games>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getListGame() : LiveData<ArrayList<GameDatas>> = listGame

    fun join(tokenUser: String, tokenGame: String){
        RetrofitClient.init().create(GameControlInterface::class.java).join(tokenUser, JoinBody(tokenGame)).execute()
    }

    fun joinGame() : LiveData<Join>{
        return joinGame
    }

    fun start(tokenUser: String, tokenGame: String){
        RetrofitClient.init().create(GameControlInterface::class.java).startGame(tokenUser, tokenGame).execute()
    }

    fun startGame() : LiveData<StartGame>{
        return startGame
    }

    fun checkContinueGame(tokenUser: String, tokenGame: String){
        RetrofitClient.init().create(GameControlInterface::class.java).getUserInfoInGame(tokenUser, tokenGame).enqueue(object : Callback<Me>{
            override fun onResponse(call: Call<Me>, response: Response<Me>) {
                me.postValue(response.body())
            }

            override fun onFailure(call: Call<Me>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun responseContinueGame() : LiveData<Me>{
        return me
    }

    fun login(email : String, pass : String){
        RetrofitClient.init().create(AuthInterface::class.java).login(LoginBody(email, pass)).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                login.postValue(response.body())
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    fun loginResponse() : LiveData<LoginResponse>{
        return login
    }

    fun getReadyCheck(tokenUser : String, tokenGame : String){
        RetrofitClient.init().create(GameControlInterface::class.java).readyCheck(tokenUser, tokenGame).enqueue(object : Callback<ReadyCheck>{
            override fun onResponse(call: Call<ReadyCheck>, response: Response<ReadyCheck>) {
                readyCheck.postValue(response.body())
            }

            override fun onFailure(call: Call<ReadyCheck>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun readyCheckResponse() : LiveData<ReadyCheck>{
        return readyCheck
    }
}