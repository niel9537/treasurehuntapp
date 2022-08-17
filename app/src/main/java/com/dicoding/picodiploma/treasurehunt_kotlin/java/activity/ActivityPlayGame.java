package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCheckIn;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestNextFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPlayGame extends AppCompatActivity {
    AlertDialog dialog;
    Button skip, lanjut;
    TextView dialogContent;
    //VideoView videoView;
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    String FILE_ID = "";
    String FILE_TYPE = "";
    String FLOW_ID = "";
    String POST_ID = "";
    int STATUS = 0;
    Boolean next = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        sharedPreferences=  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        getKeyTokenGame=sharedPreferences.getString(KEY_TOKEN_GAME,null);
        Log.d("KEY TOKEN ", " : " + getKeyToken);
        Log.d("KEY TOKEN GAME", " : " + getKeyTokenGame);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FILE_ID= extras.getString("FILE_ID");
            POST_ID= extras.getString("POST_ID");
            FLOW_ID= extras.getString("FLOW_ID");
            STATUS= extras.getInt("STATUS");
            Log.d("FLOW_ID", " : " + FLOW_ID);
            //The key argument here must match that used in the other activity
        }

        switch(STATUS){
            case 1 :
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
                View mView= LayoutInflater.from(this).inflate(R.layout.dialog_intro_story,null);
                mBuilder.setView(mView);
                //String web = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
                skip = mView.findViewById(R.id.videoSkip);
                playerView = mView.findViewById(R.id.videoView);
                // Build a HttpDataSource.Factory with cross-protocol redirects enabled.
                HttpDataSource.Factory httpDataSourceFactory =
                        new DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true);
                // Wrap the HttpDataSource.Factory in a DefaultDataSource.Factory, which adds in
                // support for requesting data from other sources (e.g., files, resources, etc).
                DefaultDataSource.Factory dataSourceFactory = () -> {
                    HttpDataSource dataSource = httpDataSourceFactory.createDataSource();
                    // Set a custom authentication request header.
                    dataSource.setRequestProperty("Authorization", getKeyToken.toString());
                    return dataSource;
                };

                try {
                    simpleExoPlayer = new SimpleExoPlayer.Builder(this).setMediaSourceFactory(new DefaultMediaSourceFactory(dataSourceFactory)).build();
                    playerView.setPlayer(simpleExoPlayer);
                    MediaItem mediaItem = MediaItem.fromUri(Config.BASE_URL+"mobile/v1/file-uploads/"+FILE_ID);
                    //MediaItem mediaItem = MediaItem.fromUri(web);
                    simpleExoPlayer.addMediaItem(mediaItem);
                    simpleExoPlayer.prepare();
                    simpleExoPlayer.play();
                }catch (Exception e){

                }

                skip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        nextFlow(FLOW_ID);
                    }
                });

                dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
            case 2 :
                Log.d("POST_ID", " : " + POST_ID);
                Log.d("FLOW_ID", " : " + FLOW_ID);
                Log.d("STATUS", " : " + STATUS);
                checkIn(POST_ID);
                //posVideoDialog("",POST_ID);
                break;
        }




    }

    private void nextFlow(String flow_id) {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<PlayModel> playCall = apiInterface.next(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(flow_id));
        playCall.enqueue(new Callback<PlayModel>() {
            @Override
            public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                if(response.isSuccessful()){
                    String type = response.body().getData().getNextFlow().getFlowType().getName().toString();
                    FLOW_ID = response.body().getData().getNextFlow().getId();
                    Log.d("TYPE",""+type);
                    Log.d("FLOW_ID",""+FLOW_ID);
                    switch(type){
                        case "manohara-instruction":
                            String content = response.body().getData().getNextFlow().getContent().toString();
                            introInstructionDialog(FLOW_ID,content,"");
                            break;
                        case "manohara-map":
                            introMapDialog(FLOW_ID,"");
                            break;
                        case "checkin":
                            startActivity(new Intent(ActivityPlayGame.this,ActivityScan.class));
                            break;
                        default:
                            Toast.makeText(ActivityPlayGame.this,"Type : "+type,Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else{
                    Toast.makeText(ActivityPlayGame.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlayModel> call, Throwable t) {
                Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkIn(String post_id) {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<PlayModel> playCall = apiInterface.cekin(getKeyToken.toString(),getKeyTokenGame,new RequestCheckIn(post_id));
        playCall.enqueue(new Callback<PlayModel>() {
            @Override
            public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                if(response.isSuccessful()){
                    String type = response.body().getData().getNextFlow().getFlowType().getName().toString();
                    FLOW_ID = response.body().getData().getNextFlow().getId();
                    FILE_ID = response.body().getData().getNextFlow().getFile().getFileId();
                    Log.d("TYPE",""+type);
                    Log.d("FLOW_ID",""+FLOW_ID);
                    switch(type){
                        case "manohara-instruction":
                            String content = response.body().getData().getNextFlow().getContent().toString();
                            introInstructionDialog(FLOW_ID,content,"");
                            break;
                        case "manohara-map":
                            introMapDialog(FLOW_ID,"");
                            break;
                        case "checkin":
                            startActivity(new Intent(ActivityPlayGame.this,ActivityScan.class));
                            break;
                        case "video":
                            posVideoDialog(FLOW_ID,FILE_ID);
                            break;
                        default:
                            Toast.makeText(ActivityPlayGame.this,"Type : "+type,Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else{
                    Toast.makeText(ActivityPlayGame.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlayModel> call, Throwable t) {
                Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void posVideoDialog(String id,String file_id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_intro_story,null);
        mBuilder.setView(mView);
        //String web = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
        skip = mView.findViewById(R.id.videoSkip);
        playerView = mView.findViewById(R.id.videoView);
        // Build a HttpDataSource.Factory with cross-protocol redirects enabled.
        HttpDataSource.Factory httpDataSourceFactory =
                new DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true);
        // Wrap the HttpDataSource.Factory in a DefaultDataSource.Factory, which adds in
        // support for requesting data from other sources (e.g., files, resources, etc).
        DefaultDataSource.Factory dataSourceFactory = () -> {
            HttpDataSource dataSource = httpDataSourceFactory.createDataSource();
            // Set a custom authentication request header.
            dataSource.setRequestProperty("Authorization", getKeyToken.toString());
            return dataSource;
        };

        try {
            simpleExoPlayer = new SimpleExoPlayer.Builder(this).setMediaSourceFactory(new DefaultMediaSourceFactory(dataSourceFactory)).build();
            playerView.setPlayer(simpleExoPlayer);
            MediaItem mediaItem = MediaItem.fromUri(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id);
            //MediaItem mediaItem = MediaItem.fromUri(web);
            simpleExoPlayer.addMediaItem(mediaItem);
            simpleExoPlayer.prepare();
            simpleExoPlayer.play();
        }catch (Exception e){

        }

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                nextFlow(id);
            }
        });

        dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void introMapDialog(String id,String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_peta_lokasi,null);
        dBuilder.setView(mView);

        lanjut = mView.findViewById(R.id.dialogContinue_button);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                Call<PlayModel> playCall = apiInterface.next(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(id));
                playCall.enqueue(new Callback<PlayModel>() {
                    @Override
                    public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                        if(response.isSuccessful()){
                            //FLOW_ID = response.body().getData().getNextFlow().getId();
                            nextFlow(id);
                        }else{
                            Toast.makeText(ActivityPlayGame.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlayModel> call, Throwable t) {
                        Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void introInstructionDialog(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_narasi_pengantar,null);
        dBuilder.setView(mView);
        dialogContent = mView.findViewById(R.id.dialogContent);
        lanjut = mView.findViewById(R.id.dialogContinue_button);
        dialogContent.setText(content);
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                Call<PlayModel> playCall = apiInterface.next(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(id));
                playCall.enqueue(new Callback<PlayModel>() {
                    @Override
                    public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                        if(response.isSuccessful()){
                            //FLOW_ID = response.body().getData().getNextFlow().getId();
                            nextFlow(id);
                        }else{
                            Toast.makeText(ActivityPlayGame.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlayModel> call, Throwable t) {
                        Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
