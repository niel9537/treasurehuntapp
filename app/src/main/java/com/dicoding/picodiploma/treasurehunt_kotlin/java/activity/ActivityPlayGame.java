package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import static com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config.MY_CAMERA_PERMISSION_CODE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCheckIn;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCheckOut;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestNextFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.OvjQRModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.util.TypeWritter;
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
    Drawable drawable;
    TextView dialogContent;
    //VideoView videoView;
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    private static final String KEY_LOBBY_ID = "key_lobby_id";
    private static final String KEY_MEMBER_ID = "key_member_id";
    private static final String KEY_BADGE = "key_badge";
    String getKeyLobbyId = "";
    String getKeyMemberId = "";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    String getKeyBadge = "";
    String FILE_ID = "";
    String FILE_TYPE = "";
    String FLOW_ID = "";
    String POST_ID = "";
    String GAME_ID = "";
    String CONTENT = "";
    int STATUS = 0;
    Boolean next = false;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        sharedPreferences=  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        getKeyTokenGame=sharedPreferences.getString(KEY_TOKEN_GAME,null);
        getKeyLobbyId=sharedPreferences.getString(KEY_LOBBY_ID,null);
        getKeyMemberId=sharedPreferences.getString(KEY_MEMBER_ID,null);
        getKeyBadge=sharedPreferences.getString(KEY_BADGE,null);
        linearLayout = findViewById(R.id.linearLayout);
        Log.d("KEY TOKEN ", " : " + getKeyToken);
        Log.d("KEY TOKEN GAME", " : " + getKeyTokenGame);
        Log.d("KEY LOBBY ", " : " + getKeyLobbyId);
        Log.d("KEY MEMBER", " : " + getKeyMemberId);
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
                String tipe = GAME_ID.toString();
                switch (tipe){
                    case Config.MANOHARA:
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
                                simpleExoPlayer.stop();
                                dialog.dismiss();
                                nextFlow(FLOW_ID);
                            }
                        });
                        dialog = mBuilder.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        break;
                    case Config.BRACE_2022:
                        transportInstruction(FLOW_ID,CONTENT,FILE_ID);
                        break;
                }
                break;
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
                Log.d("POST_ID 4", " : " + POST_ID);
                Log.d("FLOW_ID 4", " : " + FLOW_ID);
                Log.d("STATUS 4", " : " + STATUS);
                nextFlow(FLOW_ID);
                //braceGameInstruction(FLOW_ID,"tes","");
                break;
            case 5 :
                Log.d("POST_ID 5", " : " + POST_ID);
                Log.d("FLOW_ID 5", " : " + FLOW_ID);
                Log.d("STATUS 5", " : " + STATUS);
                checkOut(POST_ID, FLOW_ID);
                //posVideoDialog("",POST_ID);
                break;
            case 6 :
                Log.d("POST_ID 6", " : " + POST_ID);
                Log.d("FLOW_ID 6", " : " + FLOW_ID);
                Log.d("STATUS 6", " : " + STATUS);
                nextFlow(FLOW_ID);
                //posVideoDialog("",POST_ID);
                break;
            case 7 :
                Log.d("POST_ID 7", " : " + POST_ID);
                Log.d("FLOW_ID 7", " : " + FLOW_ID);
                Log.d("FILE_ID 7", " : " + FILE_ID);
                Log.d("STATUS 7", " : " + STATUS);
                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                Call<OvjQRModel> ovjQR = apiInterface.ovjQR(getKeyToken.toString(),POST_ID,getKeyTokenGame);
                ovjQR.enqueue(new Callback<OvjQRModel>() {
                    @Override
                    public void onResponse(Call<OvjQRModel> call, Response<OvjQRModel> response) {
                        if(response.isSuccessful()){
                            FILE_ID = response.body().getData().getFile().getFileId().toString();
                            Log.d("FILE_ID", " : " + FILE_ID+" "+response.body().getData().getGameClassification().toString());
                            ovjDialog(FLOW_ID,FILE_ID);
                        }else{
                            Toast.makeText(ActivityPlayGame.this,"Error "+response.message().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OvjQRModel> call, Throwable t) {
                        Toast.makeText(ActivityPlayGame.this,"Fail "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });

                //posVideoDialog("",POST_ID);
                break;
            case 8 :
                Log.d("POST_ID 8", " : " + POST_ID);
                Log.d("FLOW_ID 8", " : " + FLOW_ID);
                Log.d("STATUS 8", " : " + STATUS);
                nextFlow(FLOW_ID);
                //posVideoDialog("",POST_ID);
                break;
            case 9 :
                Log.d("POST_ID 9", " : " + POST_ID);
                Log.d("FLOW_ID 9", " : " + FLOW_ID);
                Log.d("STATUS 9", " : " + STATUS);
                nextFlow(FLOW_ID);
                //posVideoDialog("",POST_ID);
                break;
            case 11 :
                Log.d("POST_ID 11", " : " + POST_ID);
                Log.d("FLOW_ID 11", " : " + FLOW_ID);
                Log.d("STATUS 11", " : " + STATUS);
                nextFlow(FLOW_ID);
                //posVideoDialog("",POST_ID);
                break;
            case 20 :
                Log.d("POST_ID 20", " : " + POST_ID);
                Log.d("FLOW_ID 20", " : " + FLOW_ID);
                Log.d("STATUS 20", " : " + STATUS);
                nextFlow(FLOW_ID);
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
                            manoharaInstruction(FLOW_ID,content,"");
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
                            String fileId = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            manoharaDialog(FLOW_ID,content2,fileId);
                            break;
                        case "manohara-weding-rings":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            //manoharaWeddingRings(FLOW_ID,"Bantu Sudhana Menemukan Pecahan Cincin","");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-fighting":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-pottery":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            //manoharaPottery(FLOW_ID,"Pottery Unity","");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-archery":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            //manoharaArchery(FLOW_ID,"Archery Unity","");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-pick-me":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            //manoharaPickMe(FLOW_ID,"Pick Me","");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-come-back-home":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            //manoharaComeBackHome(FLOW_ID,"Come Back Home","");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-media-social":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaMediaSocial(FLOW_ID,"Share to Media Social","");
                            break;
                        case "transport-instruction":
                            String contentsTransportInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileTransportInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            transportInstruction(FLOW_ID,contentsTransportInstruction,fileTransportInstruction);
                            break;
                        case "checkin-instruction":
                            String contentsCheckinInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileCheckinInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            checkInInstructionDialog(FLOW_ID,contentsCheckinInstruction,fileCheckinInstruction);
                            break;
                        case "brace-post-desc":
                            if(response.body().getData().getNextFlow().getContent() != null){
                                String contentsBrace = response.body().getData().getNextFlow().getContent().toString();
                                braceDescDialog(FLOW_ID,contentsBrace,"");
                                break;
                            }else{
                                String contentsBrace = "Text";
                                braceDescDialog(FLOW_ID,contentsBrace,"");
                                break;
                            }
                        case "brace-game-instruction":
                            String contentsBraceInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileBraceInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            braceGameInstruction(FLOW_ID,contentsBraceInstruction,fileBraceInstruction);
                            break;
                        case "ovj-game":
                            Intent ovjIntent = new Intent(ActivityPlayGame.this,ActivityScanOVJ.class);
                            ovjIntent.putExtra("FLOW_ID",FLOW_ID);
                            startActivity(ovjIntent);
                            //ovjGame(FLOW_ID,"","");
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
                            manoharaFighting(FLOW_ID,"Fighting","");
                            //kainpercaGame(FLOW_ID,"","");
                            break;
                        case "gerabah-game":
                            gerabahGame(FLOW_ID,"","");
                            break;
                        case "gerabah-game-instruction":
                            String contentsGerabahInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileGerabahInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            gerabahGameInstruction(FLOW_ID,contentsGerabahInstruction,fileGerabahInstruction);
                            break;
                        case "socmed":
                            patiArenSocmed(FLOW_ID,"","");
                            break;
                        case "pati-aren-game":
                            Intent pati = new Intent(ActivityPlayGame.this,ActivityPatiArenGame.class);
                            pati.putExtra("FLOW_ID",FLOW_ID);
                            startActivity(pati);
                            break;
                        case "mie-pati-game-instruction":
                            String contentsMiePatiInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileMiePatiInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            miePatiArenGameInstruction(FLOW_ID,contentsMiePatiInstruction,fileMiePatiInstruction);
                            break;
                        case "kopi-game":
                            kopiGame(FLOW_ID,"","");
                            break;
                        case "ovj-game-instruction":
                            String contentsOvjInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileOvjInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            ovjGameInstruction(FLOW_ID,contentsOvjInstruction,fileOvjInstruction);
                            break;
                        case "kain-perca-game-instruction":
                            String kainPercaInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileKainPercaInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            kainpercaGameInstruction(FLOW_ID,kainPercaInstruction,fileKainPercaInstruction);
                            break;
                        case "brace-credit-title":
                            if(response.body().getData().getNextFlow().getContent() != null){
                                String contentsCredit = response.body().getData().getNextFlow().getContent().toString();
                                braceCreditTitle(FLOW_ID,contentsCredit,"");
                                break;
                            }else{
                                String contentsCredit = "Text";
                                braceCreditTitle(FLOW_ID,contentsCredit,"");
                                break;
                            }
                        default:
                            Toast.makeText(ActivityPlayGame.this,"Type : "+type,Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else{
                    Toast.makeText(ActivityPlayGame.this,"Nextflow Gagal : "+response.message().toString(),Toast.LENGTH_SHORT).show();
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
                            manoharaInstruction(FLOW_ID,content,"");
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
                            //manoharaWeddingRings(FLOW_ID,content3,"");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-fighting":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-pottery":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            //manoharaPottery(FLOW_ID,"Pottery Unity","");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-archery":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            //manoharaArchery(FLOW_ID,"Archery Unity","");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-pick-me":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            //manoharaPickMe(FLOW_ID,"Pick Me","");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-come-back-home":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            //manoharaComeBackHome(FLOW_ID,"Come Back Home","");
                            manoharaFighting(FLOW_ID,"Fighting","");
                            break;
                        case "manohara-media-social":
                            //String content3 = response.body().getData().getNextFlow().getContent().toString();
                            manoharaMediaSocial(FLOW_ID,"Share to Media Social","");
                            break;
                        case "transport-instruction":
                            String contentsTransportInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileTransportInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            transportInstruction(FLOW_ID,contentsTransportInstruction,fileTransportInstruction);
                            break;
                        case "checkin-instruction":
                            String contentsCheckinInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileCheckinInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            checkInInstructionDialog(FLOW_ID,contentsCheckinInstruction,fileCheckinInstruction);
                            break;
                        case "brace-post-desc":
                            //nextFlow(FLOW_ID);
                            if(response.body().getData().getNextFlow().getContent() != null){
                                String contentsBrace = response.body().getData().getNextFlow().getContent().toString();
                                braceDescDialog(FLOW_ID,contentsBrace,"");
                                break;
                            }else{
                                String contentsBrace = "Text";
                                braceDescDialog(FLOW_ID,contentsBrace,"");
                                break;
                            }
                        case "brace-game-instruction":
                            String contentsBraceInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileBraceInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            braceGameInstruction(FLOW_ID,contentsBraceInstruction,fileBraceInstruction);
                            break;
                        case "ovj-game":
                            Intent ovjIntent = new Intent(ActivityPlayGame.this,ActivityScanOVJ.class);
                            ovjIntent.putExtra("FLOW_ID",FLOW_ID);
                            startActivity(ovjIntent);
                            //ovjGame(FLOW_ID,"","");
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
                            manoharaFighting(FLOW_ID,"Fighting","");
                            //kainpercaGame(FLOW_ID,"","");
                            break;
                        case "gerabah-game":
                            gerabahGame(FLOW_ID,"","");
                            break;
                        case "gerabah-game-instruction":
                            String contentsGerabahInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileGerabahInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            gerabahGameInstruction(FLOW_ID,contentsGerabahInstruction,fileGerabahInstruction);
                            break;
                        case "socmed":
                            patiArenSocmed(FLOW_ID,"","");
                            break;
                        case "pati-aren-game":
                            Intent pati = new Intent(ActivityPlayGame.this,ActivityPatiArenGame.class);
                            pati.putExtra("FLOW_ID",FLOW_ID);
                            startActivity(pati);
                            break;
                        case "mie-pati-game-instruction":
                            String contentsMiePatiInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileMiePatiInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            miePatiArenGameInstruction(FLOW_ID,contentsMiePatiInstruction,fileMiePatiInstruction);
                            break;
                        case "kopi-game":
                            kopiGame(FLOW_ID,"","");
                            break;
                        case "ovj-game-instruction":
                            String contentsOvjInstruction = response.body().getData().getNextFlow().getContent().toString();
                            String fileOvjInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                            ovjGameInstruction(FLOW_ID,contentsOvjInstruction,fileOvjInstruction);
                            break;
                        case "kain-perca-game-instruction":
                            kainpercaGameInstruction(FLOW_ID,"","");
                            break;
                        case "brace-credit-title":
                            if(response.body().getData().getNextFlow().getContent() != null){
                                String contentsCredit = response.body().getData().getNextFlow().getContent().toString();
                                braceCreditTitle(FLOW_ID,contentsCredit,"");
                                break;
                            }else{
                                String contentsCredit = "Text";
                                braceCreditTitle(FLOW_ID,contentsCredit,"");
                                break;
                            }
                        default:
                            Toast.makeText(ActivityPlayGame.this,"Type : "+type,Toast.LENGTH_SHORT).show();
                            break;
                    }
                }else{
                    Toast.makeText(ActivityPlayGame.this,"Chekin gagal : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ActivityPlayGame.this,ActivityLobby.class));
                }
            }

            @Override
            public void onFailure(Call<PlayModel> call, Throwable t) {
                Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void miePatiArenGameInstruction(String flow_id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.brace_instruction,null);
        dBuilder.setView(mView);
        ImageView img_petunjuk = mView.findViewById(R.id.imgInstruction);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_petunjuk);
        TextView button_checkin = mView.findViewById(R.id.txtContinue);
        TextView desc_checkin = mView.findViewById(R.id.txtContent);
        desc_checkin.setText(content);
        button_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                    nextFlow(flow_id);
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void ovjDialog(String flow_id, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(ActivityPlayGame.this).inflate(R.layout.dialog_ovj_show_image,null);
        dBuilder.setView(mView);
        TextView btnContinue = mView.findViewById(R.id.txtContinue);
        ImageView imageOVJ = mView.findViewById(R.id.imgOvj);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+ file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(ActivityPlayGame.this)
                .load(glideUrl)
                .into(imageOVJ);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                nextFlow(flow_id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void gerabahDialog(String flow_id, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(ActivityPlayGame.this).inflate(R.layout.dialog_ovj_show_image,null);
        dBuilder.setView(mView);
        TextView btnContinue = mView.findViewById(R.id.txtContinue);
        ImageView imageGerabah = mView.findViewById(R.id.imgOvj);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+ file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(ActivityPlayGame.this)
                .load(glideUrl)
                .into(imageGerabah);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                nextFlow(flow_id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void gerabahPreview(String flow_id, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(ActivityPlayGame.this).inflate(R.layout.dialog_ovj_show_image,null);
        dBuilder.setView(mView);
        TextView btnContinue = mView.findViewById(R.id.txtContinue);
        ImageView imageGerabah = mView.findViewById(R.id.imgOvj);
        btnContinue.setText("Tutup");
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+ file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(ActivityPlayGame.this)
                .load(glideUrl)
                .into(imageGerabah);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
                    transportInstruction(FLOW_ID,response.body().getData().getNextFlow().getContent(),response.body().getData().getNextFlow().getFile().getFileId());
/*
                    AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
                    View mView= LayoutInflater.from(ActivityPlayGame.this).inflate(R.layout.dialog_transport,null);
                    dBuilder.setView(mView);
                    ImageView img_transport = mView.findViewById(R.id.imgTransport);
                    GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+ response.body().getData().getNextFlow().getFile().getFileId(),
                            new LazyHeaders.Builder()
                                    .addHeader("Authorization",getKeyToken)
                                    .build());

                    Glide.with(ActivityPlayGame.this)
                            .load(glideUrl)
                            .into(img_transport);
                    TextView button_berangkat = mView.findViewById(R.id.btnTransport);
                    TextView desc_berangkat = mView.findViewById(R.id.txtContent);
                    desc_berangkat.setText(response.body().getData().getNextFlow().getContent());
                    button_berangkat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            nextFlow(FLOW_ID);
                        }
                    });

                    dialog = dBuilder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
*/

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
        intent.putExtra("CONTENT",content);
        Log.d("FLOW_ID ID DESC",""+id);
        startActivity(intent);
    }
    private void braceCreditTitle(String id, String content, String file_id) {
        Intent intent = new Intent(ActivityPlayGame.this,ActivityBraceCredit.class);
        intent.putExtra("FLOW_ID",id);
        intent.putExtra("CONTENT",content);
        Log.d("CONTENT TITLE",""+id);
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
                simpleExoPlayer.stop();
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
        View mView= LayoutInflater.from(this).inflate(R.layout.manohara_map,null);
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
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        dBuilder.setView(mView);
        ImageView img_petunjuk = mView.findViewById(R.id.imgInstruction);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_petunjuk);
        TextView button_checkin = mView.findViewById(R.id.txtContinue);
        TextView desc_checkin = mView.findViewById(R.id.txtContent);
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
    private void ovjGameInstruction(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.brace_instruction,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        dBuilder.setView(mView);
        ImageView img_petunjuk = mView.findViewById(R.id.imgInstruction);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_petunjuk);
        TextView button_checkin = mView.findViewById(R.id.txtContinue);
        TextView desc_checkin = mView.findViewById(R.id.txtContent);
        desc_checkin.setText(content);
        button_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ovjOpenCamera(id);
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void gerabahGameInstruction(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_gerabah_instruction,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        dBuilder.setView(mView);
        ImageView img_petunjuk = mView.findViewById(R.id.imgInstruction);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_petunjuk);
        TextView button_checkin = mView.findViewById(R.id.txtContinue);
        TextView button_preview = mView.findViewById(R.id.button_preview_petunjuk);
        TextView desc_checkin = mView.findViewById(R.id.txtContent);
        desc_checkin.setText(content);
        button_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                Call<OvjQRModel> ovjQR = apiInterface.gerabah(getKeyToken.toString(),getKeyTokenGame);
                ovjQR.enqueue(new Callback<OvjQRModel>() {
                    @Override
                    public void onResponse(Call<OvjQRModel> call, Response<OvjQRModel> response) {
                        if(response.isSuccessful()){
                            FILE_ID = response.body().getData().getFile().getFileId().toString();
                            Log.d("FILE_ID", " : " + FILE_ID+" "+response.body().getData().getGameClassification().toString());
                            gerabahPreview("",FILE_ID);
                        }else{
                            Toast.makeText(ActivityPlayGame.this,"Error "+response.message().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OvjQRModel> call, Throwable t) {
                        Toast.makeText(ActivityPlayGame.this,"Fail "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        button_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gerabahOpenCamera(id);
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void ovjOpenCamera(String flow_id) {
        Intent intent = new Intent(ActivityPlayGame.this,ActivityCaptureImage.class);
        intent.putExtra("FLOW_ID",flow_id);
        Log.d("FLOW_ID CAPTURE IMAGE",""+flow_id);
        startActivity(intent);
    }
    private void gerabahOpenCamera(String flow_id) {
        Intent intent = new Intent(ActivityPlayGame.this,ActivityCaptureImage.class);
        intent.putExtra("FLOW_ID",flow_id);
        Log.d("FLOW_ID CAPTURE IMAGE",""+flow_id);
        startActivity(intent);
    }
    private void ovjCaptureImage(String flow_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_ovj_capture_image,null);
        dBuilder.setView(mView);
        ImageView imgOvj = mView.findViewById(R.id.imgOvj);
        TextView btnUpload = mView.findViewById(R.id.btnUpload);
        TextView btnOpen = mView.findViewById(R.id.btnOpen);
        btnOpen.setVisibility(View.INVISIBLE);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, Config.CAMERA_REQUEST);
                }
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void braceGameInstruction(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.brace_instruction,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        dBuilder.setView(mView);
        ImageView img_petunjuk = mView.findViewById(R.id.imgInstruction);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_petunjuk);
        TextView button_checkin = mView.findViewById(R.id.txtContinue);
        TextView desc_checkin = mView.findViewById(R.id.txtContent);
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
        View mView= LayoutInflater.from(this).inflate(R.layout.brace_instruction,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        dBuilder.setView(mView);
        ImageView img_petunjuk = mView.findViewById(R.id.imgInstruction);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(img_petunjuk);
        TextView button_checkin = mView.findViewById(R.id.txtContinue);
        TextView desc_checkin = mView.findViewById(R.id.txtContent);
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
    private void transportInstruction(String id, String content, String file_id) {
        Intent intent = new Intent(ActivityPlayGame.this,ActivityTransportInstruction.class);
        intent.putExtra("FLOW_ID",id);
        intent.putExtra("CONTENT",content);
        intent.putExtra("FILE_ID",file_id);
        Log.d("FLOW_ID ID TRANSPORT",""+id);
        startActivity(intent);
        /*AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
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
        TextView button_berangkat = mView.findViewById(R.id.btnTransport);
        TextView desc_berangkat = mView.findViewById(R.id.txtContent);
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
        dialog.show();*/
    }
    private void waitDialog(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.manohara_wait,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.hutan_phalaka));
        dBuilder.setView(mView);
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void manoharaInstruction(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.manohara_instruction,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.hutan_phalaka));
        dBuilder.setView(mView);
        dialogContent = mView.findViewById(R.id.txtInstruction);
        TextView lanjut = mView.findViewById(R.id.btnInstruction);
        ImageView imgInstruction = mView.findViewById(R.id.imgInstruction);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .placeholder(R.drawable.imginstruction)
                .into(imgInstruction);
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
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.hutan_phalaka));
        //View mView= LayoutInflater.from(this).inflate(R.layout.dialog_narasi_pengantar_v2,null);
        View mView= LayoutInflater.from(this).inflate(R.layout.manohara_dialog,null);
        dBuilder.setView(mView);

        TextView txtDialog = mView.findViewById(R.id.txtDialog);
        TextView next = mView.findViewById(R.id.btnDialog);
        //next.setVisibility(View.INVISIBLE);
        next.setText("Skip");
        ImageView img = mView.findViewById(R.id.imgDialog);
        Log.d("File Id Manohara Dialog",""+file_id+" "+Config.BASE_URL+"mobile/v1/file-uploads/"+file_id);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());
        Glide.with(this)
                .load(glideUrl)
                .into(img);
        //txtDialog.setText(content);
        final TypeWritter tw = mView.findViewById(R.id.txtDialog);
        tw.setCharacterDelay(50);
        tw.animateText(content);
        tw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tw.setCharacterDelay(0);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("Editable", ""+editable);
                if(editable.toString().equals(content.toString())){
                    next.setText("Next");
                    next.setVisibility(View.VISIBLE);
                    next.setOnClickListener(new View.OnClickListener() {
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
                }
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void manoharaFighting(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.manohara1));
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
        /*ImageView imgView = mView.findViewById(R.id.icon_kamera);
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
        });*/
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void patiArenSocmed(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_share,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        dBuilder.setView(mView);
        Button share = mView.findViewById(R.id.btnShare);
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
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_petunjuk,null);
        dBuilder.setView(mView);
        TextView descPetunjuk = mView.findViewById(R.id.txtContent);
        TextView btnContinue = mView.findViewById(R.id.txtContinue);
        descPetunjuk.setText("Kain Perca Game Instruction");
        btnContinue.setOnClickListener(new View.OnClickListener() {
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
    private void kainpercaGameInstruction(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_petunjuk,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        dBuilder.setView(mView);
        ImageView imgInstruction = mView.findViewById(R.id.imgInstruction);
        TextView descPetunjuk = mView.findViewById(R.id.txtContent);
        TextView btnContinue = mView.findViewById(R.id.txtContinue);
        descPetunjuk.setText(content);
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .into(imgInstruction);
        btnContinue.setOnClickListener(new View.OnClickListener() {
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
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<OvjQRModel> ovjQR = apiInterface.gerabah(getKeyToken.toString(),getKeyTokenGame);
        ovjQR.enqueue(new Callback<OvjQRModel>() {
            @Override
            public void onResponse(Call<OvjQRModel> call, Response<OvjQRModel> response) {
                if(response.isSuccessful()){
                    FILE_ID = response.body().getData().getFile().getFileId().toString();
                    Log.d("FILE_ID", " : " + FILE_ID+" "+response.body().getData().getGameClassification().toString());
                    gerabahDialog(id,FILE_ID);
                }else{
                    Toast.makeText(ActivityPlayGame.this,"Error "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OvjQRModel> call, Throwable t) {
                Toast.makeText(ActivityPlayGame.this,"Fail "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void kopiGame(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_kamera,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        //View mView= LayoutInflater.from(this).inflate(R.layout.activity_open_camera,null);
        dBuilder.setView(mView);
        //ImageView imgView = mView.findViewById(R.id.icon_kamera);
        TextView btnOpen = mView.findViewById(R.id.btnOpen);
        TextView btnUpload = mView.findViewById(R.id.btnUpload);
        btnUpload.setVisibility(View.INVISIBLE);
        btnOpen.setOnClickListener(new View.OnClickListener() {
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
