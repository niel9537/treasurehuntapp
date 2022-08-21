package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCheckIn;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCheckOut;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestNextFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    String GAME_ID = "";
    String CONTENT = "";
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
            GAME_ID=extras.getString("GAME_ID");
            CONTENT= extras.getString("CONTENT");
            STATUS= extras.getInt("STATUS");
            Log.d("FLOW_ID", " : " + FLOW_ID);
            //The key argument here must match that used in the other activity
        }

        switch(STATUS){
            case 1 :
                if(GAME_ID.equals(Config.MANOHARA)){
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
                    DefaultDataSource.Factory dataSourceFactory = ( ) -> {
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
                }else{
                    transportInstructionDialog(FLOW_ID,CONTENT,"");
                }
            case 2 :
                Log.d("POST_ID 2", " : " + POST_ID);
                Log.d("FLOW_ID 2", " : " + FLOW_ID);
                Log.d("STATUS 2", " : " + STATUS);
                checkIn(POST_ID);
                //posVideoDialog("",POST_ID);
                break;
            case 3 :
                Log.d("POST_ID 3", " : " + POST_ID);
                Log.d("FLOW_ID 3", " : " + FLOW_ID);
                Log.d("STATUS 3", " : " + STATUS);
                nextFlow(FLOW_ID);
                //posVideoDialog("",POST_ID);
                break;
            case 4 :
                Log.d("POST_ID 3", " : " + POST_ID);
                Log.d("FLOW_ID 3", " : " + FLOW_ID);
                Log.d("STATUS 3", " : " + STATUS);
                nextFlow(FLOW_ID);
                //braceGameInstruction(FLOW_ID,"tes","");
                break;
            case 5 :
                Log.d("POST_ID 2", " : " + POST_ID);
                Log.d("FLOW_ID 2", " : " + FLOW_ID);
                Log.d("STATUS 2", " : " + STATUS);
                checkOut(POST_ID, FLOW_ID);
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
//                    if (FILE_ID != null && !FILE_ID.isEmpty() && !FILE_ID.equals("null")){
//                        FILE_ID = response.body().getData().getNextFlow().getFile().getFileId();
//                    }else{
//                        FILE_ID = "NOT_FOUND";
//                    }
                    if(response.body().getData().getNextFlow().getLast() == true){
                        //Toast.makeText(ActivityPlayGame.this,"End Game",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityPlayGame.this,ActivityHome.class));
                    }
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
                        case "manohara-dialogs":
                            String content2 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaDialog(FLOW_ID,content2,"");
                            break;
                        case "manohara-weding-rings":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaWeddingRings(FLOW_ID,"Bantu Sudhana Menemukan Pecahan Cincin","");
                            break;
                        case "manohara-fighting":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-pottery":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaPottery(FLOW_ID,"Pottery Unity","");
                            break;
                        case "manohara-archery":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaArchery(FLOW_ID,"Archery Unity","");
                            break;
                        case "manohara-pick-me":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaPickMe(FLOW_ID,"Pick Me","");
                            break;
                        case "manohara-come-back-home":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaComeBackHome(FLOW_ID,"Come Back Home","");
                            break;
                        case "manohara-media-social":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaMediaSocial(FLOW_ID,"Share to Media Social","");
                            break;
                        case "transport-instruction":
                            String contentsTransportInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileTransportInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            transportInstructionDialog(FLOW_ID,contentsTransportInstruction,fileTransportInstruction);
                            break;
                        case "checkin-instruction":
                            String contentsCheckinInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileCheckinInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            checkInInstructionDialog(FLOW_ID,contentsCheckinInstruction,fileCheckinInstruction);
                            break;
                        case "brace-post-desc":
                            //nextFlow(FLOW_ID);
                            braceDescDialog(FLOW_ID,"","");
                            break;
                        case "brace-game-instruction":
                            String contentsBraceInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileBraceInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            braceGameInstruction(FLOW_ID,contentsBraceInstruction,fileBraceInstruction);
                            break;
                        case "ovj-game":
                            ovjGame(FLOW_ID,"","");
                            break;
                        case "checkout-instruction":
                            String contentsCheckoutInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileCheckoutInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            checkoutInstruction(FLOW_ID,contentsCheckoutInstruction,fileCheckoutInstruction);
                            break;
                        case "checkout":
                            Intent intent = new Intent(ActivityPlayGame.this,ActivityScanCheckOut.class);
                            intent.putExtra("FLOW_ID",FLOW_ID);
                            Log.d("FLOW_ID CEKOUT",""+FLOW_ID);
                            startActivity(intent);
                            break;
                        case "kain-perca-game":
                            kainpercaGame(FLOW_ID,"","");
                            break;
                        case "gerabah-game":
                            gerabahGame(FLOW_ID,"","");
                            break;
                        case "pati-aren-socmed":
                            patiArenSocmed(FLOW_ID,"","");
                            break;
                        case "pati-aren-game":
                            patiArenGame(FLOW_ID,"","");
                            break;
                        case "kopi-game":
                            kopiGame(FLOW_ID,"","");
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
//                    if (FILE_ID != null && !FILE_ID.isEmpty() && !FILE_ID.equals("null")){
//                        FILE_ID = response.body().getData().getNextFlow().getFile().getFileId();
//                    }else{
//                        FILE_ID = "NOT_FOUND";
//                    }
                    if(response.body().getData().getNextFlow().getLast() == true){
                        //Toast.makeText(ActivityPlayGame.this,"End Game",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityPlayGame.this,ActivityHome.class));
                    }
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
                        case "manohara-dialogs":
                            String content2 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaDialog(FLOW_ID,content2,"");
                            break;
                        case "manohara-weding-rings":
                            String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaWeddingRings(FLOW_ID,content3,"");
                            break;
                        case "manohara-fighting":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-pottery":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaPottery(FLOW_ID,"Pottery Unity","");
                            break;
                        case "manohara-archery":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaArchery(FLOW_ID,"Archery Unity","");
                            break;
                        case "manohara-pick-me":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaPickMe(FLOW_ID,"Pick Me","");
                            break;
                        case "manohara-come-back-home":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaComeBackHome(FLOW_ID,"Come Back Home","");
                            break;
                        case "manohara-media-social":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaMediaSocial(FLOW_ID,"Share to Media Social","");
                            break;
                        case "transport-instruction":
                            String contentsTransportInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileTransportInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            transportInstructionDialog(FLOW_ID,contentsTransportInstruction,fileTransportInstruction);
                            break;
                        case "checkin-instruction":
                            String contentsCheckinInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileCheckinInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            checkInInstructionDialog(FLOW_ID,contentsCheckinInstruction,fileCheckinInstruction);
                            break;
                        case "brace-post-desc":
                            //nextFlow(FLOW_ID);
                            braceDescDialog(FLOW_ID,"","");
                            break;
                        case "brace-game-instruction":
                            String contentsBraceInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileBraceInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            braceGameInstruction(FLOW_ID,contentsBraceInstruction,fileBraceInstruction);
                            break;
                        case "ovj-game":
                            ovjGame(FLOW_ID,"","");
                            break;
                        case "checkout-instruction":
                            String contentsCheckoutInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileCheckoutInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            checkoutInstruction(FLOW_ID,contentsCheckoutInstruction,fileCheckoutInstruction);
                            break;
                        case "checkout":
                            Intent intent = new Intent(ActivityPlayGame.this,ActivityScanCheckOut.class);
                            intent.putExtra("FLOW_ID",FLOW_ID);
                            Log.d("FLOW_ID CEKOUT",""+FLOW_ID);
                            startActivity(intent);
                            break;
                        case "kain-perca-game":
                            kainpercaGame(FLOW_ID,"","");
                            break;
                        case "gerabah-game":
                            gerabahGame(FLOW_ID,"","");
                            break;
                        case "pati-aren-socmed":
                            patiArenSocmed(FLOW_ID,"","");
                            break;
                        case "pati-aren-game":
                            patiArenGame(FLOW_ID,"","");
                            break;
                        case "kopi-game":
                            kopiGame(FLOW_ID,"","");
                            break;
                        default:
                            Toast.makeText(ActivityPlayGame.this,"Type : "+type,Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else{
                    //Toast.makeText(ActivityPlayGame.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlayModel> call, Throwable t) {
                Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkOut(String post_id, String flow_id) {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<PlayModel> playCall = apiInterface.cekout(getKeyToken.toString(),getKeyTokenGame,new RequestCheckOut(post_id,flow_id));
        playCall.enqueue(new Callback<PlayModel>() {
            @Override
            public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                if(response.isSuccessful()){
                    String type = response.body().getData().getNextFlow().getFlowType().getName().toString();
                    FLOW_ID = response.body().getData().getNextFlow().getId();
                    nextFlow(FLOW_ID);
                }else {
                    Toast.makeText(ActivityPlayGame.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PlayModel> call, Throwable t) {
                Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void braceDescDialog(String id, String content, String file_id) {
        Intent intent = new Intent(ActivityPlayGame.this,ActivitySplashBrace2022.class);
        intent.putExtra("FLOW_ID",id);
        Log.d("FLOW_ID ID DESC",""+id);
        startActivity(intent);
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
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_peta,null);
        dBuilder.setView(mView);

        TextView lanjut = mView.findViewById(R.id.continue_peta);
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
    private void checkInInstructionDialog(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_petunjuk,null);
        dBuilder.setView(mView);
        ImageView img_petunjuk = mView.findViewById(R.id.img_petunjuk);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_petunjuk);
        TextView button_checkin = mView.findViewById(R.id.button_continue_petunjuk);
        TextView desc_checkin = mView.findViewById(R.id.desc_petunjuk);
        desc_checkin.setText(content);
        button_checkin.setOnClickListener(new View.OnClickListener() {
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
        if(!isFinishing()){
            dialog.show();
        }

    }
    private void braceGameInstruction(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_brace_petunjuk,null);
        dBuilder.setView(mView);
        ImageView img_petunjuk = mView.findViewById(R.id.img_petunjuk);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_petunjuk);
        TextView button_checkin = mView.findViewById(R.id.button_continue_petunjuk);
        TextView desc_checkin = mView.findViewById(R.id.desc_petunjuk);
        desc_checkin.setText(content);
        button_checkin.setOnClickListener(new View.OnClickListener() {
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
                        //Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void checkoutInstruction(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_brace_petunjuk,null);
        dBuilder.setView(mView);
        ImageView img_petunjuk = mView.findViewById(R.id.img_petunjuk);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_petunjuk);
        TextView button_checkin = mView.findViewById(R.id.button_continue_petunjuk);
        TextView desc_checkin = mView.findViewById(R.id.desc_petunjuk);
        desc_checkin.setText(content);
        button_checkin.setOnClickListener(new View.OnClickListener() {
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
                        //Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void transportInstructionDialog(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_transport,null);
        dBuilder.setView(mView);
        ImageView img_transport = mView.findViewById(R.id.img_transport);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_transport);
        TextView button_berangkat = mView.findViewById(R.id.button_berangkat);
        TextView desc_berangkat = mView.findViewById(R.id.desc_berangkat);
        desc_berangkat.setText(content);
        button_berangkat.setOnClickListener(new View.OnClickListener() {
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
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_petunjuk,null);
        dBuilder.setView(mView);
        dialogContent = mView.findViewById(R.id.desc_petunjuk);
        TextView lanjut = mView.findViewById(R.id.button_continue_petunjuk);
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
    private void manoharaDialog(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_narasi_pengantar_v2,null);
        dBuilder.setView(mView);
        dialogContent = mView.findViewById(R.id.desc_dialog);
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
    private void manoharaWeddingRings(String id, String content, String file_id) {
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
    private void manoharaFighting(String id, String content, String file_id) {
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
    private void manoharaPottery(String id, String content, String file_id) {
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
    private void manoharaArchery(String id, String content, String file_id) {
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
    private void manoharaPickMe(String id, String content, String file_id) {
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
    private void manoharaComeBackHome(String id, String content, String file_id) {
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
    private void manoharaMediaSocial(String id, String content, String file_id) {
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

    private void ovjGame(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_kamera,null);
        dBuilder.setView(mView);
        ImageView imgView = mView.findViewById(R.id.icon_kamera);
        Button buka_camera = mView.findViewById(R.id.buka_camera);
        buka_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    private void patiArenSocmed(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_share,null);
        dBuilder.setView(mView);
        Button share = mView.findViewById(R.id.share_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showBottomSheetDialog(id);
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showBottomSheetDialog(String FLOW_ID) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        LinearLayout instagram = bottomSheetDialog.findViewById(R.id.instagram);
        LinearLayout whatsapp = bottomSheetDialog.findViewById(R.id.whatsapp);
        bottomSheetDialog.show();
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                nextFlow(FLOW_ID);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                nextFlow(FLOW_ID);
            }
        });

    }

    private void kainpercaGame(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_kamera,null);
        dBuilder.setView(mView);
        ImageView imgView = mView.findViewById(R.id.icon_kamera);
        Button buka_camera = mView.findViewById(R.id.buka_camera);
        buka_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    private void gerabahGame(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_kamera,null);
        dBuilder.setView(mView);
        ImageView imgView = mView.findViewById(R.id.icon_kamera);
        Button buka_camera = mView.findViewById(R.id.buka_camera);
        buka_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    private void patiArenGame(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_kamera,null);
        dBuilder.setView(mView);
        ImageView imgView = mView.findViewById(R.id.icon_kamera);
        Button buka_camera = mView.findViewById(R.id.buka_camera);
        buka_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    private void kopiGame(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_kamera,null);
        dBuilder.setView(mView);
        ImageView imgView = mView.findViewById(R.id.icon_kamera);
        Button buka_camera = mView.findViewById(R.id.buka_camera);
        buka_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
