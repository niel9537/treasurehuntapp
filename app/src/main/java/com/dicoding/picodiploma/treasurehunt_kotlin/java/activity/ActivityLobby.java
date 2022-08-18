package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.InputGameCodeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.ReadyModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.checkprogress.CekProgressModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLobby extends AppCompatActivity {
    TextView player1;
    Button ready,play;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    private static final String KEY_FILE_ID = "key_file_id";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    boolean isContinue;
    String FLOW_ID = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_game);
        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        getKeyTokenGame=sharedPreferences.getString(KEY_TOKEN_GAME,null);
        Log.d("KEY TOKEN ", " : " + getKeyToken);
        Log.d("KEY TOKEN GAME", " : " + getKeyTokenGame);
        editor = sharedPreferences.edit();
        player1 = findViewById(R.id.name_player1);
        ready = findViewById(R.id.ready_button);
        play = findViewById(R.id.play_game_button);
        play.setEnabled(false);
        me();


        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ready();
            }
        });

    }

    private void play() {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<PlayModel> playCall = apiInterface.play(getKeyToken.toString(),getKeyTokenGame.toString());
        playCall.enqueue(new Callback<PlayModel>() {
            @Override
            public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ActivityLobby.this,"Sukses : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityLobby.this,ActivityPlayGame.class);
                    intent.putExtra("FILE_ID",response.body().getData().getNextFlow().getFile().getFileId().toString());
                    intent.putExtra("FLOW_ID",response.body().getData().getNextFlow().getId().toString());
                    intent.putExtra("STATUS", Config.PLAY_GAME);
                    startActivity(intent);
                }else{
                    Toast.makeText(ActivityLobby.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlayModel> call, Throwable t) {

            }
        });
    }
    private void lanjut() {
           Intent intent = new Intent(ActivityLobby.this,ActivityPlayGame.class);
           intent.putExtra("FLOW_ID",FLOW_ID);
           intent.putExtra("STATUS", Config.CONTINUE_GAME);
           startActivity(intent);
    }
    private void ready() {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<ReadyModel> readyCall = apiInterface.ready(getKeyToken.toString(),getKeyTokenGame.toString());
        readyCall.enqueue(new Callback<ReadyModel>() {
            @Override
            public void onResponse(Call<ReadyModel> call, Response<ReadyModel> response) {
                if(response.isSuccessful()){
                   if(response.body().getMessage().equals("I am Ready to Play")){
                       Log.d("STATUS ", " : " + response.body().getMessage().toString());
                       ready.setText(response.body().getMessage().toString());
                       ready.setBackgroundColor(ContextCompat.getColor(ActivityLobby.this, R.color.login_gray));
                       ready.setEnabled(false);
                       play.setBackgroundColor(ContextCompat.getColor(ActivityLobby.this, R.color.green));
                       play.setEnabled(true);
                       cekProgress();
                      /* if(cekProgress()){
                           Log.d("CEK PROGRESS ", " : " + isContinue);
                           play.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   lanjut();
                               }
                           });
                       }else{
                           ready.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   ready();
                               }
                           });
                           play.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   play();
                               }
                           });
                       }*/
                   }
                }else{
                    Toast.makeText(ActivityLobby.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReadyModel> call, Throwable t) {

            }
        });

    }

    private void cekProgress(){

        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<CekProgressModel> cekProgressCall = apiInterface.cekproses(getKeyToken.toString(),getKeyTokenGame.toString());
        cekProgressCall.enqueue(new Callback<CekProgressModel>() {
            @Override
            public void onResponse(Call<CekProgressModel> call, Response<CekProgressModel> response) {
              if(response.isSuccessful()){
                  play.setText("Continue");
                  FLOW_ID = response.body().getData().getLatestFlow().getId().toString();
                  Log.d("FLOW_ID ", " : " + FLOW_ID);
                  isContinue = true;
                  Log.d("CEK PROGRESS ", " : " + isContinue);
                  play.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          lanjut();
                      }
                  });
              }else{
                  ready.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          ready();
                      }
                  });
                  play.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          play();
                      }
                  });
                  //Toast.makeText(ActivityLobby.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                  isContinue = false;
              }
            }

            @Override
            public void onFailure(Call<CekProgressModel> call, Throwable t) {
                Toast.makeText(ActivityLobby.this,"Error Failure : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                isContinue = false;
            }
        });
        Log.d("RETURN PROGRESS ", " : " + isContinue);
       // return isContinue;
    }

    private void me() {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<MeModel> meCall = apiInterface.me(getKeyToken.toString(),getKeyTokenGame.toString());
        meCall.enqueue(new Callback<MeModel>() {
            @Override
            public void onResponse(Call<MeModel> call, Response<MeModel> response) {
                if(response.isSuccessful()){
                    Log.d("Status ", " : " + response.body().getDataMeModel().getStatus().toString());
                    Log.d("Badge ", " : " + response.body().getDataMeModel().getBadge().toString());
                    String name = response.body().getDataMeModel().getUser().getProfile().getFullName().toString();
                    player1.setText(name);
                }else{
                    Log.d("Status ", " : " + response.code());
                    Toast.makeText(ActivityLobby.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MeModel> call, Throwable t) {

            }
        });

    }
}
