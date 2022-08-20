package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LobbyDetailModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.ReadyModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.checkprogress.CekProgressModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.socketresponse.GameStartedModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.socketresponse.MemberJoinModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.socketresponse.DataMemberReadyModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.socketresponse.MemberReadyModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;
import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLobby extends AppCompatActivity {
    TextView player1,player2,player3,player4,player5;
    Button ready,play;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    private static final String KEY_FILE_ID = "key_file_id";
    private static final String KEY_LOBBY_ID = "key_lobby_id";
    private static final String KEY_GAME_ID = "key_game_id";
    private static final String KEY_MEMBER_ID = "key_member_id";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    String getKeyLobbyId = "";
    String getKeyGameId = "";
    String getKeyMemberId = "";
    boolean isContinue;
    String FLOW_ID = "";
    boolean statusReady = false;
    public String message;
    private Socket mSocket;
    String Member_Id = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lobby_game);
        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        getKeyTokenGame=sharedPreferences.getString(KEY_TOKEN_GAME,null);
        getKeyLobbyId=sharedPreferences.getString(KEY_LOBBY_ID,null);
        getKeyGameId=sharedPreferences.getString(KEY_GAME_ID,null);
        getKeyMemberId=sharedPreferences.getString(KEY_MEMBER_ID,null);
        Log.d("KEY TOKEN ", " : " + getKeyToken);
        Log.d("KEY TOKEN GAME", " : " + getKeyTokenGame);
        editor = sharedPreferences.edit();
        player1 = findViewById(R.id.name_player1);
        player2 = findViewById(R.id.name_player2);
        player3 = findViewById(R.id.name_player3);
        player4 = findViewById(R.id.name_player4);
        player5 = findViewById(R.id.name_player5);
        ready = findViewById(R.id.ready_button);
        play = findViewById(R.id.play_game_button);
        play.setEnabled(false);
        //Initialize Socket




        me();
        lobbyDetail();

        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusReady = true;
                ready();
            }
        });




        //hit Socket
        Log.d("URL", ""+"https://th-main-api.kartala.id/mobile?member="+getKeyMemberId+"&lobby="+getKeyLobbyId+"");
        try {
            mSocket = IO.socket("https://th-main-api.kartala.id/mobile?member="+getKeyMemberId+"&lobby="+getKeyLobbyId+"");
            mSocket.connect();
            Log.d("IS_CONNECTED ", ""+mSocket.connected());
            setListening();
        } catch (URISyntaxException e) {
            Log.d("Error Socket :",""+e.getMessage());
        }
    }

    private void setListening() {
        mSocket.on("game-started", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JSONObject json = (JSONObject) args[0];
                        Log.d("JSON : Started",""+json.toString());
                        GameStartedModel data = gson.fromJson(json.toString(),GameStartedModel.class);
                        Log.d("Listen : ",""+data.getNextFlow().getFlowType().getName().toString());

                    }
                });

            }
        }).on("member-join", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JSONObject json = (JSONObject) args[0];
                        Log.d("JSON : Join",""+json.toString());
                        MemberJoinModel data = gson.fromJson(json.toString(),MemberJoinModel.class);
                        FancyToast.makeText(ActivityLobby.this,"Player bernama "+data.getUser().getProfile().getFullName().toString()+" telah bergabung !!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        startActivity(new Intent(ActivityLobby.this,ActivityLobby.class));
                    }
                });

            }
        }).on("member-leaved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JSONObject json = (JSONObject) args[0];
                        Log.d("JSON : Leaved",""+json.toString());
                        MemberReadyModel data = gson.fromJson(json.toString(), MemberReadyModel.class);
                        FancyToast.makeText(ActivityLobby.this,"Player bernama "+data.getMember().getUser().getProfile().getFullName().toString()+" telah meninggalkan permainan !!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        startActivity(new Intent(ActivityLobby.this,ActivityLobby.class));

                    }
                });

            }
        }).on("badge-change", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JSONObject json = (JSONObject) args[0];
                        Log.d("JSON : Badge Change",""+json.toString());


                    }
                });

            }
        }).on("member-kicked", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JSONObject json = (JSONObject) args[0];
                        Log.d("JSON : Kicked",""+json.toString());


                    }
                });

            }
        }).on("member-ready", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JSONObject json = (JSONObject) args[0];
                        Log.d("JSON : Ready",""+json.toString());
                        MemberReadyModel data = gson.fromJson(json.toString(), MemberReadyModel.class);
                        FancyToast.makeText(ActivityLobby.this,"Player bernama "+data.getMember().getUser().getProfile().getFullName().toString()+" telah ready !!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        //startActivity(new Intent(ActivityLobby.this,ActivityLobby.class));

                    }
                });

            }
        }).on("member-not-ready", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JSONObject json = (JSONObject) args[0];
                        Log.d("JSON : Not Ready",""+json.toString());
                        MemberReadyModel data = gson.fromJson(json.toString(), MemberReadyModel.class);
                        FancyToast.makeText(ActivityLobby.this,"Player bernama "+data.getMember().getUser().getProfile().getFullName().toString()+" belum ready !!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        startActivity(new Intent(ActivityLobby.this,ActivityLobby.class));


                    }
                });

            }
        }).on("member-offline", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JSONObject json = (JSONObject) args[0];
                        Log.d("JSON Offline: ",""+json.toString());
                        MemberReadyModel data = gson.fromJson(json.toString(), MemberReadyModel.class);
                        FancyToast.makeText(ActivityLobby.this,"Player bernama "+data.getMember().getUser().getProfile().getFullName().toString()+" telah offline !!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();


                    }
                });

            }
        }).on("member-online", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JSONObject json = (JSONObject) args[0];
                        Log.d("JSON Online: ",""+json.toString());
                        MemberReadyModel data = gson.fromJson(json.toString(), MemberReadyModel.class);
                        FancyToast.makeText(ActivityLobby.this,"Player bernama "+data.getMember().getUser().getProfile().getFullName().toString()+" telah online !!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    }
                });

            }
        });
    }

    private void lobbyDetail() {
        final LinearLayout ly = (LinearLayout) findViewById(R.id.ly);
        ly.setOrientation(LinearLayout.VERTICAL);
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<LobbyDetailModel> lobbydetailCall = apiInterface.lobbydetail(getKeyToken.toString(),getKeyLobbyId.toString(),getKeyTokenGame.toString());
        lobbydetailCall.enqueue(new Callback<LobbyDetailModel>() {
            @Override
            public void onResponse(Call<LobbyDetailModel> call, Response<LobbyDetailModel> response) {
                if(response.isSuccessful()){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,20,0,10);
                    for(int i=0; i<response.body().getData().getPartyMembers().size(); i++){
                        TextView textView = new TextView(ActivityLobby.this);
                        textView.setText(response.body().getData().getPartyMembers().get(i).getUser().getProfile().getFullName()+" - "+response.body().getData().getPartyMembers().get(i).getBadge().toString());
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        textView.setTextColor(ContextCompat.getColor(ActivityLobby.this, R.color.dark_blue));
                        textView.setPadding(15,10,15,10);
                        textView.setBackground(ContextCompat.getDrawable(ActivityLobby.this, R.drawable.player));
                        textView.setLayoutParams(params);
                        ly.addView(textView);
                    }
                }else{
                    Toast.makeText(ActivityLobby.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LobbyDetailModel> call, Throwable t) {
                Toast.makeText(ActivityLobby.this,"Fail : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
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
                        //send message
                    Intent intent = new Intent(ActivityLobby.this,ActivityPlayGame.class);
                    intent.putExtra("FILE_ID",response.body().getData().getNextFlow().getFile().getFileId().toString());
                    intent.putExtra("FLOW_ID",response.body().getData().getNextFlow().getId().toString());
                    intent.putExtra("CONTENT",response.body().getData().getNextFlow().getContent().toString());
                    intent.putExtra("GAME_ID", getKeyGameId);
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
                       ready.setText("UNREADY");
                       ready.setBackgroundColor(ContextCompat.getColor(ActivityLobby.this, R.color.login_gray));
                       ready.setEnabled(false);
                       play.setBackgroundColor(ContextCompat.getColor(ActivityLobby.this, R.color.green));
                       play.setEnabled(true);
                       cekProgress();
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
    private void unready() {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<ReadyModel> readyCall = apiInterface.unready(getKeyToken.toString(),getKeyTokenGame.toString());
        readyCall.enqueue(new Callback<ReadyModel>() {
            @Override
            public void onResponse(Call<ReadyModel> call, Response<ReadyModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getMessage().equals("I am Ready to Play")){
                        Log.d("STATUS ", " : " + response.body().getMessage().toString());
                        ready.setText("READY");
                        ready.setBackgroundColor(ContextCompat.getColor(ActivityLobby.this, R.color.green));
                        ready.setEnabled(true);
                        play.setBackgroundColor(ContextCompat.getColor(ActivityLobby.this, R.color.login_gray));
                        play.setEnabled(false);
                        ready.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ready();
                            }
                        });
                        //cekProgress();
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
                  ready.setEnabled(false);
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

                    //player1.setText(name);
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
