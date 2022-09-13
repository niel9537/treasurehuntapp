package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCarCheck;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCheckIn;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestCheckOut;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestNextFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.CarCheckModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.FinishModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.KainPercaModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.OvjQRModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.socketresponse.GameStartedModel;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPlayGame extends AppCompatActivity {
    boolean isBack = false;
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
    private Socket mSocket;
    String getKeyLobbyId = "";
    String getKeyMemberId = "";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    String getKeyBadge = "";
    String FILE_ID = "";
    String FILE_TYPE = "";
    String FLOW_ID = "";
    String FLOW_TYPE = "";
    String POST_ID = "";
    String GAME_ID = "";
    String CONTENT = "";
    int STATUS = 0;
    Boolean next = false;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    AlertClass alertClass;
    private int result = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        alertClass = new AlertClass(this);
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
            FLOW_TYPE= extras.getString("FLOW_TYPE");
            GAME_ID=extras.getString("GAME_ID");
            CONTENT= extras.getString("CONTENT");
            STATUS= extras.getInt("STATUS");
            Log.d("FLOW_ID", " : " + FLOW_ID);
            //The key argument here must match that used in the other activity
        }
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;
        decorView.setSystemUiVisibility(uiOptions);
        //hit Socket
        Log.d("URL", ""+"https://th-main-api.kartala.id/mobile?member="+getKeyMemberId+"&lobby="+getKeyLobbyId+"");
        try {
            mSocket = IO.socket("https://th-main-api.kartala.id/mobile?member="+getKeyMemberId+"&lobby="+getKeyLobbyId+"");
            mSocket.connect();
            Log.d("IS_CONNECTED ", ""+mSocket.connected());
            mSocket.on("checked-in", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    JSONObject json = (JSONObject) args[0];
                                    Log.d("JSON Online: ", "" + json.toString());
                                    GameStartedModel data = gson.fromJson(json.toString(), GameStartedModel.class);
                                    Log.d("Listen Play Game : ", "" + data.getCurrentFlow().getFlowType().getName().toString());
                                    //FancyToast.makeText(ActivityPlayGame.this,"Listen Play Game : "+data.getCurrentFlow().getFlowType().getName().toString(),FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                    String type = data.getCurrentFlow().getFlowType().getName();
                                    switch (type) {
                                        case "video":
                                            FILE_ID = data.getCurrentFlow().getFile().getFileId();
                                            FLOW_ID = data.getCurrentFlow().getId();
                                            dialog.dismiss();
                                            //FancyToast.makeText(ActivityPlayGame.this, "Play Flow ID : " + data.getCurrentFlow().getId() + " FILE ID :" + data.getCurrentFlow().getFile().getFileId(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                            manoharaVideoDialog(FLOW_ID, FILE_ID);
                                            break;
                                        case "manohara-dialogs":
                                            FILE_ID = data.getCurrentFlow().getFile().getFileId();
                                            FLOW_ID = data.getCurrentFlow().getId();
                                            CONTENT = data.getCurrentFlow().getContent();
                                            String TITLE = data.getCurrentFlow().getTitle();
                                            dialog.dismiss();
                                            //FancyToast.makeText(ActivityPlayGame.this, "Play Flow ID : " + data.getCurrentFlow().getId() + " FILE ID :" + data.getCurrentFlow().getFile().getFileId(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                            manoharaDialog(FLOW_ID,CONTENT,FILE_ID,TITLE);
                                            break;
                                        case "brace-post-desc":
                                            FLOW_ID = data.getCurrentFlow().getId();
                                            if (data.getCurrentFlow().getContent() != null) {
                                                String contentsBrace = data.getCurrentFlow().getContent().toString();
                                                braceDescDialog(FLOW_ID,data.getCurrentFlow().getTitle().toString(),data.getCurrentFlow().getSubTitle().toString(), contentsBrace, "");
                                                break;
                                            } else {
                                                String contentsBrace = "Text";
                                                braceDescDialog(FLOW_ID,data.getCurrentFlow().getTitle().toString(),data.getCurrentFlow().getSubTitle().toString(), contentsBrace, "");
                                                break;
                                            }
                                        default:
                                            break;
                                    }
                                }
                            });

                        }
            }).on("checked-out", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            JSONObject json = (JSONObject) args[0];
                            Log.d("JSON Online: ", "" + json.toString());
                            GameStartedModel data = gson.fromJson(json.toString(), GameStartedModel.class);
                            Log.d("Listen Play Game : ", "" + data.getCurrentFlow().getFlowType().getName().toString());
                            //FancyToast.makeText(ActivityPlayGame.this,"Listen Play Game : "+data.getCurrentFlow().getFlowType().getName().toString(),FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                            String type = data.getCurrentFlow().getFlowType().getName();
                            switch (type) {
                                case "transport-instruction":
                                    String contentsTransportInstruction = data.getCurrentFlow().getContent().toString();
                                    String fileTransportInstruction =data.getCurrentFlow().getFile().getFileId().toString();
                                    transportInstruction(FLOW_ID, contentsTransportInstruction, fileTransportInstruction);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                }
            }).on("mobilize-party", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            JSONObject json = (JSONObject) args[0];
                            Log.d("JSON Online: ",""+json.toString());
                            GameStartedModel data = gson.fromJson(json.toString(),GameStartedModel.class);
                            Log.d("Listen Play Game : ",""+data.getCurrentFlow().getFlowType().getName().toString());
                            //FancyToast.makeText(ActivityPlayGame.this,"Listen Play Game : "+data.getCurrentFlow().getFlowType().getName().toString(),FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                            String type = data.getCurrentFlow().getFlowType().getName();
                            switch(type){
                                case "checkin-instruction":
                                    dialog.dismiss();
                                    FILE_ID = data.getCurrentFlow().getFile().getFileId();
                                    FLOW_ID = data.getCurrentFlow().getId();
                                    CONTENT = data.getCurrentFlow().getContent();
                                    //FancyToast.makeText(ActivityPlayGame.this,"Play Flow ID : "+data.getCurrentFlow().getId() +" FILE ID :"+data.getCurrentFlow().getFile().getFileId(),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                                    checkInInstructionDialog(FLOW_ID,CONTENT,FILE_ID);
                                    break;
                                case "brace-credit-title":
                                    FLOW_ID = data.getCurrentFlow().getId();
                                    if (data.getCurrentFlow().getContent() != null) {
                                        String contentsCredit = data.getCurrentFlow().getContent().toString();
                                        braceCreditTitle(FLOW_ID, contentsCredit, "");
                                        break;
                                    } else {
                                        String contentsCredit = "Text";
                                        braceCreditTitle(FLOW_ID, contentsCredit, "");
                                        break;
                                    }
                                default:
                                    break;
                            }
                        }
                    });

                }
            });
        } catch (URISyntaxException e) {
            Log.d("Error Socket :",""+e.getMessage());
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
                        Button btnPrev = mView.findViewById(R.id.btnPrev);
                        skip = mView.findViewById(R.id.videoSkip);
                        playerView = mView.findViewById(R.id.videoView);
                        btnPrev.setVisibility(View.INVISIBLE);
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
                checkIn(POST_ID,FLOW_ID);
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
                            alertClass.showAlertScanOVJ("Scan OVJ Gagal","Silahkan scan barcode ovj image yang benar",FLOW_ID);
//                            Intent ovjIntent = new Intent(ActivityPlayGame.this, ActivityScanOVJ.class);
//                            ovjIntent.putExtra("FLOW_ID", FLOW_ID);
//                            startActivity(ovjIntent);
//                            Toast.makeText(ActivityPlayGame.this,"Error "+response.message().toString(),Toast.LENGTH_SHORT).show();
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
            case 77 :
                Log.d("POST_ID 20", " : " + POST_ID);
                Log.d("FLOW_ID 20", " : " + FLOW_ID);
                Log.d("STATUS 20", " : " + STATUS);
                carCheck(POST_ID,FLOW_ID);
                //posVideoDialog("",POST_ID);
                break;
            case 88 :
                Log.d("POST_ID 88", " : " + POST_ID);
                Log.d("FLOW_ID 88", " : " + FLOW_ID);
                Log.d("STATUS 88", " : " + STATUS);
                prevFlow(FLOW_ID);
                //posVideoDialog("",POST_ID);
                break;
            case 65 :
                Log.d("FLOW_TYPE 65", " : " + FLOW_TYPE);
                Log.d("FLOW_ID 65", " : " + FLOW_ID);
                Log.d("FILE_ID 65", " : " + FILE_ID);
                Log.d("STATUS 65", " : " + STATUS);
                currentFlow(FLOW_ID,FLOW_TYPE,FILE_ID);
                //posVideoDialog("",POST_ID);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        Log.d("Back"," Nothing");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSocket.disconnect();
        Log.d("Pause Socket : "," Stopped");

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        mSocket.connect();
        Log.d("Restart Socket : "," Restart");
    }
    @Override
    protected void onStop() {
        super.onStop();
        mSocket.disconnect();
        Log.d("Stop Socket : "," Stopped");
    }

    private void carCheck(String post_id, String flow_id) {
        String flowid = flow_id;
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<CarCheckModel> playCall = apiInterface.carCheck(getKeyToken.toString(),getKeyTokenGame,new RequestCarCheck(post_id,flow_id));
        playCall.enqueue(new Callback<CarCheckModel>() {
            @Override
            public void onResponse(Call<CarCheckModel> call, Response<CarCheckModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("Wait for your party member")) {
                        waitDialog();
                    } else {
                        FLOW_ID = response.body().getData().getCurrentFlow().getId();
                        Log.d("FLOW_ID ", " : " + FLOW_ID);

                        String type = response.body().getData().getCurrentFlow().getFlowType().getName();
                        switch (type) {
                            case "checkin-instruction":
                                checkInInstructionDialog(FLOW_ID, response.body().getData().getCurrentFlow().getContent().toString(), response.body().getData().getCurrentFlow().getFile().getFileId());
                                break;
                            case "brace-credit-title":
                                if (response.body().getData().getCurrentFlow().getContent() != null) {
                                    String contentsCredit = response.body().getData().getCurrentFlow().getContent().toString();
                                    braceCreditTitle(FLOW_ID, contentsCredit, "");
                                    break;
                                } else {
                                    String contentsCredit = "Text";
                                    braceCreditTitle(FLOW_ID, contentsCredit, "");
                                    break;
                                }
                        }

                    }
                }else{
                    Log.d("Response", "" + response.code());
                    alertClass.showAlertCarCheck("Gagal CarCheck","Silahkan scan barcode mobil yang benar",flowid);
//                    Intent intentCar = new Intent(ActivityPlayGame.this, ActivityScanCar.class);
//                    intentCar.putExtra("FLOW_ID", flowid);
//                    Log.d("FLOW_ID CAR", "" + flowid);
//                    startActivity(intentCar);
                }

            }

            @Override
            public void onFailure(Call<CarCheckModel> call, Throwable t) {
                Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void nextFlow(String flow_id) {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<PlayModel> playCall = apiInterface.next(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(flow_id));
        playCall.enqueue(new Callback<PlayModel>() {
            @Override
            public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getMessage().equals("Wait for your party member")){
                        waitDialog();
                    }else {
                        //dialog.dismiss();
                        String type = response.body().getData().getNextFlow().getFlowType().getName().toString();
                        FLOW_ID = response.body().getData().getNextFlow().getId();

//                    if (FILE_ID != null && !FILE_ID.isEmpty() && !FILE_ID.equals("null")){
//                        FILE_ID = response.body().getData().getNextFlow().getFile().getFileId();
//                    }else{
//                        FILE_ID = "NOT_FOUND";
//                    }
                        if (response.body().getData().getNextFlow().getLast() == true
                                && response.body().getData().getNextFlow().getPost().getLast() == true
                                && !response.body().getData().getNextFlow().getFlowType().getName().equals("manohara-credit-title")) {
                            //Toast.makeText(ActivityPlayGame.this,"End Game",Toast.LENGTH_SHORT).show();
                            ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                            Call<FinishModel> finishCall = apiInterface.finish(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(FLOW_ID));
                            finishCall.enqueue(new Callback<FinishModel>() {
                                @Override
                                public void onResponse(Call<FinishModel> call, Response<FinishModel> response) {
                                    if(response.isSuccessful()){
                                        startActivity(new Intent(ActivityPlayGame.this, ActivityHome.class));
                                    }
                                }

                                @Override
                                public void onFailure(Call<FinishModel> call, Throwable t) {

                                }
                            });

                        }
                        isBack = response.body().getData().getNextFlow().getPrev();
                        Log.d("TYPE", "" + type);
                        Log.d("FLOW_ID", "" + FLOW_ID);
                        switch (type) {
                            case "manohara-instruction":
                                String content = response.body().getData().getNextFlow().getContent().toString();
                                String file_id = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                manoharaInstruction(FLOW_ID, content, file_id);
                                break;
                            case "manohara-map":
                                String imgMap = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                manoharaMapDialog(FLOW_ID, imgMap);
                                break;
                            case "checkin":
                                Intent checkinIntent = new Intent(ActivityPlayGame.this, ActivityScan.class);
                                checkinIntent.putExtra("FLOW_ID", FLOW_ID);
                                startActivity(checkinIntent);
                                break;
                            case "video":
                                FILE_ID = response.body().getData().getNextFlow().getFile().getFileId();
                                Log.d("Next Flow ID : ",""+response.body().getData().getNextFlow().getId() +" FILE ID :"+response.body().getData().getNextFlow().getFile().getFileId());
                                manoharaVideoDialog(FLOW_ID, FILE_ID);
                                break;
                            case "manohara-dialogs":
                                String content2 = response.body().getData().getNextFlow().getContent().toString();
                                String fileId = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                String title = response.body().getData().getNextFlow().getTitle().toString();
                                manoharaDialog(FLOW_ID, content2, fileId, title);
                                break;
                            case "manohara-weding-rings":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaWeddingRings(FLOW_ID,"Bantu Sudhana Menemukan Pecahan Cincin","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-fighting":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-pottery":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaPottery(FLOW_ID,"Pottery Unity","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-archery":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaArchery(FLOW_ID,"Archery Unity","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-pick-me":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaPickMe(FLOW_ID,"Pick Me","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-come-back-home":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaComeBackHome(FLOW_ID,"Come Back Home","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-media-social":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                manoharaMediaSocial(FLOW_ID, "Share to Media Social", "");
                                break;
                            case "transport-instruction":
                                Log.d("isBack ", "" + response.body().getData().getNextFlow().getPrev());
                                String contentsTransportInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileTransportInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                transportInstruction(FLOW_ID, contentsTransportInstruction, fileTransportInstruction);
                                break;
                            case "transport-claim":
                                Intent intentCar = new Intent(ActivityPlayGame.this, ActivityScanCar.class);
                                intentCar.putExtra("FLOW_ID", FLOW_ID);
                                Log.d("FLOW_ID CAR", "" + FLOW_ID);
                                startActivity(intentCar);
                                break;
                            case "checkin-instruction":
                                Log.d("isBack ", "" + response.body().getData().getNextFlow().getPrev());
                                String contentsCheckinInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileCheckinInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                checkInInstructionDialog(FLOW_ID, contentsCheckinInstruction, fileCheckinInstruction);
                                break;
                            case "brace-post-desc":
                                if (response.body().getData().getNextFlow().getContent() != null) {
                                    String contentsBrace = response.body().getData().getNextFlow().getContent().toString();
                                    braceDescDialog(FLOW_ID,response.body().getData().getNextFlow().getTitle().toString(),response.body().getData().getNextFlow().getSubTitle().toString(), contentsBrace, "");
                                    break;
                                } else {
                                    String contentsBrace = "Text";
                                    braceDescDialog(FLOW_ID,response.body().getData().getNextFlow().getTitle().toString(),response.body().getData().getNextFlow().getSubTitle().toString(), contentsBrace, "");
                                    break;
                                }
                            case "brace-game-instruction":
                                Log.d("isBack ", "" + response.body().getData().getNextFlow().getPrev());
                                String contentsBraceInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileBraceInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                braceGameInstruction(FLOW_ID, contentsBraceInstruction, fileBraceInstruction);
                                break;
                            case "ovj-game":
                                Log.d("isBack ", "" + response.body().getData().getNextFlow().getPrev());
                                Intent ovjIntent = new Intent(ActivityPlayGame.this, ActivityScanOVJ.class);
                                ovjIntent.putExtra("FLOW_ID", FLOW_ID);
                                startActivity(ovjIntent);
                                //ovjGame(FLOW_ID,"","");
                                break;
                            case "checkout-instruction":
                                Log.d("isBack ", "" + response.body().getData().getNextFlow().getPrev());
                                String contentsCheckoutInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileCheckoutInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                checkoutInstruction(FLOW_ID, contentsCheckoutInstruction, fileCheckoutInstruction);
                                break;
                            case "checkout":
                                Intent intent = new Intent(ActivityPlayGame.this, ActivityScanCheckOut.class);
                                intent.putExtra("FLOW_ID", FLOW_ID);
                                Log.d("FLOW_ID CEKOUT", "" + FLOW_ID);
                                startActivity(intent);
                                break;
                            case "kain-perca-game":
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                //kainpercaGame(FLOW_ID,"","");
                                break;
                            case "gerabah-game":
                                Log.d("isBack ", "" + response.body().getData().getNextFlow().getPrev());
                                gerabahGame(FLOW_ID, "", "");
                                break;
                            case "gerabah-game-instruction":
                                Log.d("isBack ", "" + response.body().getData().getNextFlow().getPrev());
                                String contentsGerabahInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileGerabahInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                gerabahGameInstruction(FLOW_ID, contentsGerabahInstruction, fileGerabahInstruction);
                                break;
                            case "socmed":
                                Socmed(FLOW_ID, "", "");
                                break;
                            case "pati-aren-game":
                                Intent pati = new Intent(ActivityPlayGame.this, ActivityPatiArenGame.class);
                                pati.putExtra("FLOW_ID", FLOW_ID);
                                startActivity(pati);
                                break;
                            case "mie-pati-game-instruction":
                                String contentsMiePatiInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileMiePatiInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                miePatiArenGameInstruction(FLOW_ID, contentsMiePatiInstruction, fileMiePatiInstruction);
                                break;
                            case "kopi-game":
                                kopiGame(FLOW_ID, "", "");
                                break;
                            case "mie-pati-socmed":
                                openCamera(FLOW_ID);
                                break;
                            case "ovj-game-instruction":
                                String contentsOvjInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileOvjInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                ovjGameInstruction(FLOW_ID, contentsOvjInstruction, fileOvjInstruction);
                                break;
                            case "kain-perca-game-instruction":
                                String kainPercaInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileKainPercaInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                kainpercaGameInstruction(FLOW_ID, kainPercaInstruction, fileKainPercaInstruction);
                                break;
                            case "brace-credit-title":
                                if (response.body().getData().getNextFlow().getContent() != null) {
                                    String contentsCredit = response.body().getData().getNextFlow().getContent().toString();
                                    braceCreditTitle(FLOW_ID, contentsCredit, "");
                                    break;
                                } else {
                                    String contentsCredit = "Text";
                                    braceCreditTitle(FLOW_ID, contentsCredit, "");
                                    break;
                                }
                            case "manohara-credit-title":
                                    String fileCredit = response.body().getData().getNextFlow().getFile().getFileId();
                                    manoharaCreditTitle(FLOW_ID,fileCredit);
                                    break;
                            default:
                                Toast.makeText(ActivityPlayGame.this, "Type : " + type, Toast.LENGTH_SHORT).show();
                                break;
                        }
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
    private void checkIn(String post_id, String flow_id) {
        Log.d("Checkin UUU","post_id :"+post_id+" flow_id :"+flow_id);
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<PlayModel> playCall = apiInterface.cekin(getKeyToken.toString(),getKeyTokenGame,new RequestCheckIn(post_id,flow_id));
        playCall.enqueue(new Callback<PlayModel>() {
            @Override
            public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                if(response.isSuccessful()){
                    Log.d("Response ",""+response.body().getMessage().toString());
                    if(response.body().getMessage().equals("Wait for your party member")){
                        waitDialog();
                    }else {
                        //dialog.dismiss();
                        String type = response.body().getData().getNextFlow().getFlowType().getName().toString();
                        FLOW_ID = response.body().getData().getNextFlow().getId();

//                    if (FILE_ID != null && !FILE_ID.isEmpty() && !FILE_ID.equals("null")){
//                        FILE_ID = response.body().getData().getNextFlow().getFile().getFileId();
//                    }else{
//                        FILE_ID = "NOT_FOUND";
//                    }
                        if (response.body().getData().getNextFlow().getLast() == true
                                && response.body().getData().getNextFlow().getPost().getLast() == true
                                && !response.body().getData().getNextFlow().getFlowType().getName().equals("manohara-credit-title")) {
                            //Toast.makeText(ActivityPlayGame.this,"End Game",Toast.LENGTH_SHORT).show();
                            ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                            Call<FinishModel> finishCall = apiInterface.finish(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(FLOW_ID));
                            finishCall.enqueue(new Callback<FinishModel>() {
                                @Override
                                public void onResponse(Call<FinishModel> call, Response<FinishModel> response) {
                                    if(response.isSuccessful()){
                                        startActivity(new Intent(ActivityPlayGame.this, ActivityHome.class));
                                    }
                                }

                                @Override
                                public void onFailure(Call<FinishModel> call, Throwable t) {

                                }
                            });

                        }
                        isBack = response.body().getData().getNextFlow().getPrev();
                        Log.d("TYPE", "" + type);
                        Log.d("FLOW_ID", "" + FLOW_ID);

                        switch (type) {
                            case "manohara-instruction":
                                String content = response.body().getData().getNextFlow().getContent().toString();
                                String file_id = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                manoharaInstruction(FLOW_ID, content, file_id);
                                break;
                            case "manohara-map":
                                String imgMap = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                manoharaMapDialog(FLOW_ID, imgMap);
                                break;
                            case "checkin":
                                Intent checkinIntent = new Intent(ActivityPlayGame.this, ActivityScan.class);
                                checkinIntent.putExtra("FLOW_ID", FLOW_ID);
                                startActivity(checkinIntent);
                                break;
                            case "video":
                                FILE_ID = response.body().getData().getNextFlow().getFile().getFileId();
                                Log.d("Checkin Flow ID : ",""+response.body().getData().getNextFlow().getId() +" FILE ID :"+response.body().getData().getNextFlow().getFile().getFileId());
                                manoharaVideoDialog(FLOW_ID, FILE_ID);
                                break;
                            case "manohara-dialogs":
                                String content2 = response.body().getData().getNextFlow().getContent().toString();
                                String fileId = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                String title = response.body().getData().getNextFlow().getTitle().toString();
                                manoharaDialog(FLOW_ID, content2, fileId, title);
                                break;
                            case "manohara-weding-rings":
                                String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaWeddingRings(FLOW_ID,content3,"");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-fighting":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-pottery":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaPottery(FLOW_ID,"Pottery Unity","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-archery":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaArchery(FLOW_ID,"Archery Unity","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-pick-me":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaPickMe(FLOW_ID,"Pick Me","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-come-back-home":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaComeBackHome(FLOW_ID,"Come Back Home","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-media-social":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                manoharaMediaSocial(FLOW_ID, "Share to Media Social", "");
                                break;
                            case "transport-instruction":
                                String contentsTransportInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileTransportInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                transportInstruction(FLOW_ID, contentsTransportInstruction, fileTransportInstruction);
                                break;
                            case "checkin-instruction":
                                String contentsCheckinInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileCheckinInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                checkInInstructionDialog(FLOW_ID, contentsCheckinInstruction, fileCheckinInstruction);
                                break;
                            case "brace-post-desc":
                                //nextFlow(FLOW_ID);
                                if (response.body().getData().getNextFlow().getContent() != null) {
                                    String contentsBrace = response.body().getData().getNextFlow().getContent().toString();
                                    braceDescDialog(FLOW_ID,response.body().getData().getNextFlow().getTitle().toString(),response.body().getData().getNextFlow().getSubTitle().toString(), contentsBrace, "");
                                    break;
                                } else {
                                    String contentsBrace = "Text";
                                    braceDescDialog(FLOW_ID,response.body().getData().getNextFlow().getTitle().toString(),response.body().getData().getNextFlow().getSubTitle().toString(), contentsBrace, "");
                                    break;
                                }
                            case "brace-game-instruction":
                                String contentsBraceInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileBraceInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                braceGameInstruction(FLOW_ID, contentsBraceInstruction, fileBraceInstruction);
                                break;
                            case "ovj-game":
                                Intent ovjIntent = new Intent(ActivityPlayGame.this, ActivityScanOVJ.class);
                                ovjIntent.putExtra("FLOW_ID", FLOW_ID);
                                startActivity(ovjIntent);
                                //ovjGame(FLOW_ID,"","");
                                break;
                            case "checkout-instruction":
                                String contentsCheckoutInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileCheckoutInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                checkoutInstruction(FLOW_ID, contentsCheckoutInstruction, fileCheckoutInstruction);
                                break;
                            case "checkout":
                                Intent intent = new Intent(ActivityPlayGame.this, ActivityScanCheckOut.class);
                                intent.putExtra("FLOW_ID", FLOW_ID);
                                Log.d("FLOW_ID CEKOUT", "" + FLOW_ID);
                                startActivity(intent);
                                break;
                            case "kain-perca-game":
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                //kainpercaGame(FLOW_ID,"","");
                                break;
                            case "gerabah-game":
                                gerabahGame(FLOW_ID, "", "");
                                break;
                            case "gerabah-game-instruction":
                                String contentsGerabahInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileGerabahInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                gerabahGameInstruction(FLOW_ID, contentsGerabahInstruction, fileGerabahInstruction);
                                break;
                            case "socmed":
                                Socmed(FLOW_ID, "", "");
                                break;
                            case "pati-aren-game":
                                Intent pati = new Intent(ActivityPlayGame.this, ActivityPatiArenGame.class);
                                pati.putExtra("FLOW_ID", FLOW_ID);
                                startActivity(pati);
                                break;
                            case "mie-pati-game-instruction":
                                String contentsMiePatiInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileMiePatiInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                miePatiArenGameInstruction(FLOW_ID, contentsMiePatiInstruction, fileMiePatiInstruction);
                                break;
                            case "kopi-game":
                                kopiGame(FLOW_ID, "", "");
                                break;
                            case "ovj-game-instruction":
                                String contentsOvjInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileOvjInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                ovjGameInstruction(FLOW_ID, contentsOvjInstruction, fileOvjInstruction);
                                break;
                            case "kain-perca-game-instruction":
                                kainpercaGameInstruction(FLOW_ID, "", "");
                                break;
                            case "brace-credit-title":
                                if (response.body().getData().getNextFlow().getContent() != null) {
                                    String contentsCredit = response.body().getData().getNextFlow().getContent().toString();
                                    braceCreditTitle(FLOW_ID, contentsCredit, "");
                                    break;
                                } else {
                                    String contentsCredit = "Text";
                                    braceCreditTitle(FLOW_ID, contentsCredit, "");
                                    break;
                                }
                            case "manohara-credit-title":
                                    String fileIdCredit = response.body().getData().getNextFlow().getFile().getFileId();
                                    manoharaCreditTitle(FLOW_ID, fileIdCredit);
                                    break;
                            default:
                                Toast.makeText(ActivityPlayGame.this, "Type : " + type, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }else{
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
                    View mView=ActivityPlayGame.this.getLayoutInflater().inflate(R.layout.registration_failed_dialog_layout, null);
                    mBuilder.setView(mView);
                    Button OK = (Button) mView.findViewById(R.id.dialogOK_button);
                    TextView txtTitle = (TextView) mView.findViewById(R.id.txtTitle);
                    TextView txtSubtitle = (TextView) mView.findViewById(R.id.txtSubtitle);
                    txtTitle.setText("Checkin Gagal");
                    txtSubtitle.setText("Silahkan scan ulang menggunakan barcode yang benar");
                    OK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent checkinIntent = new Intent(ActivityPlayGame.this, ActivityScan.class);
                            checkinIntent.putExtra("FLOW_ID", flow_id);
                            startActivity(checkinIntent);
                        }
                    });

                    dialog = mBuilder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

//                    Intent checkinIntent = new Intent(ActivityPlayGame.this, ActivityScan.class);
//                    checkinIntent.putExtra("FLOW_ID", flow_id);
//                    startActivity(checkinIntent);
                }
            }

            @Override
            public void onFailure(Call<PlayModel> call, Throwable t) {
                Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void prevFlow(String flow_id) {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<PlayModel> playCall = apiInterface.prev(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(flow_id));
        playCall.enqueue(new Callback<PlayModel>() {
            @Override
            public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                if(response.isSuccessful()){
                    if(response.body().getMessage().equals("Wait for your party member")){
                        waitDialog();
                    }else {
                        //dialog.dismiss();
                        String type = response.body().getData().getNextFlow().getFlowType().getName().toString();
                        FLOW_ID = response.body().getData().getNextFlow().getId();

//                    if (FILE_ID != null && !FILE_ID.isEmpty() && !FILE_ID.equals("null")){
//                        FILE_ID = response.body().getData().getNextFlow().getFile().getFileId();
//                    }else{
//                        FILE_ID = "NOT_FOUND";
//                    }
                        if (response.body().getData().getNextFlow().getLast() == true
                                && response.body().getData().getNextFlow().getPost().getLast() == true
                                && !response.body().getData().getNextFlow().getFlowType().getName().equals("manohara-credit-title")) {
                            //Toast.makeText(ActivityPlayGame.this,"End Game",Toast.LENGTH_SHORT).show();
                            ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                            Call<FinishModel> finishCall = apiInterface.finish(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(FLOW_ID));
                            finishCall.enqueue(new Callback<FinishModel>() {
                                @Override
                                public void onResponse(Call<FinishModel> call, Response<FinishModel> response) {
                                    if(response.isSuccessful()){
                                        startActivity(new Intent(ActivityPlayGame.this, ActivityHome.class));
                                    }
                                }

                                @Override
                                public void onFailure(Call<FinishModel> call, Throwable t) {

                                }
                            });

                        }
                        isBack = response.body().getData().getNextFlow().getPrev();
                        Log.d("TYPE", "" + type);
                        Log.d("FLOW_ID", "" + FLOW_ID);
                        switch (type) {
                            case "manohara-instruction":
                                String content = response.body().getData().getNextFlow().getContent().toString();
                                String file_id = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                manoharaInstruction(FLOW_ID, content, file_id);
                                break;
                            case "manohara-map":
                                String imgMap = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                manoharaMapDialog(FLOW_ID, imgMap);
                                break;
                            case "checkin":
                                Intent checkinIntent = new Intent(ActivityPlayGame.this, ActivityScan.class);
                                checkinIntent.putExtra("FLOW_ID", FLOW_ID);
                                startActivity(checkinIntent);
                                break;
                            case "video":
                                FILE_ID = response.body().getData().getNextFlow().getFile().getFileId();
                                Log.d("Prev Flow ID : ",""+response.body().getData().getNextFlow().getId() +" FILE ID :"+response.body().getData().getNextFlow().getFile().getFileId());
                                manoharaVideoDialog(FLOW_ID, FILE_ID);
                                break;
                            case "manohara-dialogs":
                                String content2 = response.body().getData().getNextFlow().getContent().toString();
                                String fileId = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                String title = response.body().getData().getNextFlow().getTitle().toString();
                                manoharaDialog(FLOW_ID, content2, fileId, title);
                                break;
                            case "manohara-weding-rings":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaWeddingRings(FLOW_ID,"Bantu Sudhana Menemukan Pecahan Cincin","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-fighting":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-pottery":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaPottery(FLOW_ID,"Pottery Unity","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-archery":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaArchery(FLOW_ID,"Archery Unity","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-pick-me":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaPickMe(FLOW_ID,"Pick Me","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-come-back-home":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                //manoharaComeBackHome(FLOW_ID,"Come Back Home","");
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                break;
                            case "manohara-media-social":
                                //String content3 = response.body().getData().getNextFlow().getContent().toString();
                                manoharaMediaSocial(FLOW_ID, "Share to Media Social", "");
                                break;
                            case "transport-instruction":
                                String contentsTransportInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileTransportInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                transportInstruction(FLOW_ID, contentsTransportInstruction, fileTransportInstruction);
                                break;
                            case "checkin-instruction":
                                String contentsCheckinInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileCheckinInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                checkInInstructionDialog(FLOW_ID, contentsCheckinInstruction, fileCheckinInstruction);
                                break;
                            case "brace-post-desc":
                                if (response.body().getData().getNextFlow().getContent() != null) {
                                    String contentsBrace = response.body().getData().getNextFlow().getContent().toString();
                                    braceDescDialog(FLOW_ID,response.body().getData().getNextFlow().getTitle().toString(),response.body().getData().getNextFlow().getSubTitle().toString(),contentsBrace, "");
                                    break;
                                } else {
                                    String contentsBrace = "Text";
                                    braceDescDialog(FLOW_ID,response.body().getData().getNextFlow().getTitle().toString(),response.body().getData().getNextFlow().getSubTitle().toString(), contentsBrace, "");
                                    break;
                                }
                            case "brace-game-instruction":
                                String contentsBraceInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileBraceInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                braceGameInstruction(FLOW_ID, contentsBraceInstruction, fileBraceInstruction);
                                break;
                            case "ovj-game":
                                Intent ovjIntent = new Intent(ActivityPlayGame.this, ActivityScanOVJ.class);
                                ovjIntent.putExtra("FLOW_ID", FLOW_ID);
                                startActivity(ovjIntent);
                                //ovjGame(FLOW_ID,"","");
                                break;
                            case "checkout-instruction":
                                String contentsCheckoutInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileCheckoutInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                checkoutInstruction(FLOW_ID, contentsCheckoutInstruction, fileCheckoutInstruction);
                                break;
                            case "checkout":
                                Intent intent = new Intent(ActivityPlayGame.this, ActivityScanCheckOut.class);
                                intent.putExtra("FLOW_ID", FLOW_ID);
                                Log.d("FLOW_ID CEKOUT", "" + FLOW_ID);
                                startActivity(intent);
                                break;
                            case "kain-perca-game":
                                manoharaFighting(FLOW_ID, "Fighting", "");
                                //kainpercaGame(FLOW_ID,"","");
                                break;
                            case "gerabah-game":
                                gerabahGame(FLOW_ID, "", "");
                                break;
                            case "gerabah-game-instruction":
                                String contentsGerabahInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileGerabahInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                gerabahGameInstruction(FLOW_ID, contentsGerabahInstruction, fileGerabahInstruction);
                                break;
                            case "socmed":
                                Socmed(FLOW_ID, "", "");
                                break;
                            case "pati-aren-game":
                                Intent pati = new Intent(ActivityPlayGame.this, ActivityPatiArenGame.class);
                                pati.putExtra("FLOW_ID", FLOW_ID);
                                startActivity(pati);
                                break;
                            case "mie-pati-game-instruction":
                                String contentsMiePatiInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileMiePatiInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                miePatiArenGameInstruction(FLOW_ID, contentsMiePatiInstruction, fileMiePatiInstruction);
                                break;
                            case "kopi-game":
                                kopiGame(FLOW_ID, "", "");
                                break;
                            case "ovj-game-instruction":
                                String contentsOvjInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileOvjInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                ovjGameInstruction(FLOW_ID, contentsOvjInstruction, fileOvjInstruction);
                                break;
                            case "kain-perca-game-instruction":
                                String kainPercaInstruction = response.body().getData().getNextFlow().getContent().toString();
                                String fileKainPercaInstruction = response.body().getData().getNextFlow().getFile().getFileId().toString();
                                kainpercaGameInstruction(FLOW_ID, kainPercaInstruction, fileKainPercaInstruction);
                                break;
                            case "brace-credit-title":
                                if (response.body().getData().getNextFlow().getContent() != null) {
                                    String contentsCredit = response.body().getData().getNextFlow().getContent().toString();
                                    braceCreditTitle(FLOW_ID, contentsCredit, "");
                                    break;
                                } else {
                                    String contentsCredit = "Text";
                                    braceCreditTitle(FLOW_ID, contentsCredit, "");
                                    break;
                                }
                            case "manohara-credit-title":
                                    String fileIdCredit = response.body().getData().getNextFlow().getFile().getFileId();
                                    manoharaCreditTitle(FLOW_ID, fileIdCredit);
                                    break;
                            default:
                                Toast.makeText(ActivityPlayGame.this, "Type : " + type, Toast.LENGTH_SHORT).show();
                                break;
                        }
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
    private void currentFlow(String flow_id, String flow_type, String file_id){
        switch (flow_type) {
            case "checkin":
                dialog.dismiss();
                //FancyToast.makeText(ActivityPlayGame.this, "Play Flow ID : " + data.getCurrentFlow().getId() + " FILE ID :" + data.getCurrentFlow().getFile().getFileId(), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                manoharaVideoDialog(flow_id, file_id);
                break;
            case "brace-post-desc":
                braceDescDialog(FLOW_ID,"POS","Brace", "Text", "");
                break;
            default:
                break;
        }
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
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
        desc_checkin.setText(content);
        button_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                    nextFlow(flow_id);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(flow_id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void ovjDialog(String flow_id, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(ActivityPlayGame.this).inflate(R.layout.dialog_ovj_show_image,null);
        dBuilder.setView(mView);
        TextView btnContinue = mView.findViewById(R.id.txtContinue);
        ImageView imageOVJ = mView.findViewById(R.id.imgOvj);
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
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
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(flow_id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void gerabahDialog(String flow_id, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(ActivityPlayGame.this).inflate(R.layout.dialog_ovj_show_image,null);
        dBuilder.setView(mView);
        TextView btnContinue = mView.findViewById(R.id.txtContinue);
        ImageView imageGerabah = mView.findViewById(R.id.imgOvj);
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
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
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(flow_id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
        dialog.setCancelable(false);
        dialog.show();
    }
    private void checkOut(String post_id, String flow_id) {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<PlayModel> playCall = apiInterface.cekout(getKeyToken.toString(),getKeyTokenGame,new RequestCheckOut(post_id,flow_id));
        playCall.enqueue(new Callback<PlayModel>() {
            @Override
            public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                if (response.isSuccessful()) {
                    if(response.body().getMessage().equals("Wait for your party member")){
                        waitDialog();
                    }else {
                        String type = response.body().getData().getNextFlow().getFlowType().getName().toString();
                        FLOW_ID = response.body().getData().getNextFlow().getId();
                        transportInstruction(FLOW_ID, response.body().getData().getNextFlow().getContent(), response.body().getData().getNextFlow().getFile().getFileId());
                    }
                }else {
                    alertClass.showAlerCheckOut("Checkout Gagal","Silahkan scan barcode checkout yang benar",flow_id);
//                    Intent intent = new Intent(ActivityPlayGame.this, ActivityScanCheckOut.class);
//                    intent.putExtra("FLOW_ID", flow_id);
//                    Log.d("FLOW_ID CEKOUT", "" + flow_id);
//                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<PlayModel> call, Throwable t) {
                Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void braceDescDialog(String id,String title, String subtitle, String content, String file_id) {
        Intent intent = new Intent(ActivityPlayGame.this,ActivitySplashBrace2022.class);
        intent.putExtra("FLOW_ID",id);
        intent.putExtra("CONTENT",content);
        intent.putExtra("TITLE",title);
        intent.putExtra("SUBTITLE",subtitle);
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
    private void manoharaCreditTitle(String id, String file_id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_intro_story,null);
        mBuilder.setView(mView);
        //String web = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
        skip = mView.findViewById(R.id.videoSkip);
        playerView = mView.findViewById(R.id.videoView);
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
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
            Log.d("VIDEO URL ",""+Config.BASE_URL+"mobile/v1/file-uploads/"+file_id);
            //MediaItem mediaItem = MediaItem.fromUri(web);
            simpleExoPlayer.addMediaItem(mediaItem);
            simpleExoPlayer.prepare();
            simpleExoPlayer.play();
        }catch (Exception e){
            Log.d("VIDEO EXCEPTION ",""+e.getMessage().toString());
        }

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayer.stop();
                dialog.dismiss();
              //  nextFlow(id);
                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                Call<FinishModel> finishCall = apiInterface.finish(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(FLOW_ID));
                finishCall.enqueue(new Callback<FinishModel>() {
                    @Override
                    public void onResponse(Call<FinishModel> call, Response<FinishModel> response) {
                        if(response.isSuccessful()){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(KEY_TOKEN_GAME);
                            editor.apply();
                            startActivity(new Intent(ActivityPlayGame.this,ActivityHome.class));
                        }else{
                            Toast.makeText(ActivityPlayGame.this,"Error "+response.message().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FinishModel> call, Throwable t) {
                        Toast.makeText(ActivityPlayGame.this,"Fail "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void manoharaVideoDialog(String id, String file_id) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_intro_story,null);
        mBuilder.setView(mView);
        //String web = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
        skip = mView.findViewById(R.id.videoSkip);
        playerView = mView.findViewById(R.id.videoView);
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
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
            Log.d("VIDEO URL ",""+Config.BASE_URL+"mobile/v1/file-uploads/"+file_id);
            //MediaItem mediaItem = MediaItem.fromUri(web);
            simpleExoPlayer.addMediaItem(mediaItem);
            simpleExoPlayer.prepare();
            simpleExoPlayer.play();
        }catch (Exception e){
            Log.d("VIDEO EXCEPTION ",""+e.getMessage().toString());
        }

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayer.stop();
                dialog.dismiss();
                nextFlow(id);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void manoharaMapDialog(String id, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.manohara_map,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.hutan_phalaka));
        dBuilder.setView(mView);

        TextView lanjut = mView.findViewById(R.id.continue_peta);
        ImageView img = mView.findViewById(R.id.img_peta);
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .placeholder(R.drawable.empty_image_state)
                .into(img);
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
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
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
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
        desc_checkin.setText(content);
        button_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCamera(id);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
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
                //gerabahOpenCamera(id);
                openCamera(id);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void openCamera(String flow_id) {
        Intent intent = new Intent(ActivityPlayGame.this,ActivityCaptureImage.class);
        intent.putExtra("FLOW_ID",flow_id);
        Log.d("FLOW_ID CAPTURE IMAGE",""+flow_id);
        startActivity(intent);
    }
/*    private void gerabahOpenCamera(String flow_id) {
        Intent intent = new Intent(ActivityPlayGame.this,ActivityCaptureImage.class);
        intent.putExtra("FLOW_ID",flow_id);
        Log.d("FLOW_ID CAPTURE IMAGE",""+flow_id);
        startActivity(intent);
    }*/

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
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
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
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
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
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void transportInstruction(String id, String content, String file_id) {
        Intent intent = new Intent(ActivityPlayGame.this,ActivityTransportInstruction.class);
        intent.putExtra("FLOW_ID",id);
        intent.putExtra("CONTENT",content);
        intent.putExtra("FILE_ID",file_id);
        Log.d("FLOW_ID ID TRANSPORT",""+id);
        startActivity(intent);
    }
    private void waitDialog() {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.manohara_wait,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.manohara2));
        dBuilder.setView(mView);
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void manoharaInstruction(String id, String content, String file_id) {
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.manohara_instruction,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.hutan_phalaka));
        dBuilder.setView(mView);
        dialogContent = mView.findViewById(R.id.txtInstruction);
        TextView lanjut = mView.findViewById(R.id.btnInstruction);
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
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
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void manoharaDialog(String id, String content, String file_id, String title) {

        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.hutan_phalaka));
        //View mView= LayoutInflater.from(this).inflate(R.layout.dialog_narasi_pengantar_v2,null);
        View mView= LayoutInflater.from(this).inflate(R.layout.manohara_dialog,null);
        dBuilder.setView(mView);

        TextView txtTitle = mView.findViewById(R.id.txtTitle);
        TextView txtDialog = mView.findViewById(R.id.txtDialog);
        TextView next = mView.findViewById(R.id.btnDialog);
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
        //next.setVisibility(View.INVISIBLE);
        next.setText("Skip");
        txtTitle.setText(title.toUpperCase());
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
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private int kainPercaGroup(){
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<KainPercaModel> kainperca = apiInterface.kainperca(getKeyToken.toString(),getKeyTokenGame);
        kainperca.enqueue(new Callback<KainPercaModel>() {
            @Override
            public void onResponse(Call<KainPercaModel> call, Response<KainPercaModel> response) {
                if(response.isSuccessful()){
                  result =  response.body().getData().getGroup();
                }
            }

            @Override
            public void onFailure(Call<KainPercaModel> call, Throwable t) {

            }
        });
        return result;
    }

    private void manoharaFighting(String id, String content, String file_id) {
        int kainPercaGroup = kainPercaGroup();
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
        dialog.setCancelable(false);
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
        dialog.setCancelable(false);
        dialog.show();
    }
    private void Socmed(String id, String content, String file_id){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_share,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        dBuilder.setView(mView);
        Button share = mView.findViewById(R.id.btnShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                nextFlow(id);
                //showBottomSheetDialog(id);
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
    private void kainpercaGameInstruction(String id, String content, String file_id){
        int kainGame = 1;
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_petunjuk,null);
        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
        dBuilder.setView(mView);
        ImageView imgInstruction = mView.findViewById(R.id.imgInstruction);
        TextView descPetunjuk = mView.findViewById(R.id.txtContent);
        TextView btnContinue = mView.findViewById(R.id.txtContinue);
        Button btnPrev = mView.findViewById(R.id.btnPrev);
        if(isBack == false){
            btnPrev.setVisibility(View.INVISIBLE);
        }else{
            btnPrev.setVisibility(View.VISIBLE);
        }
        descPetunjuk.setText(content);
        switch (kainGame){
            case 1 :
                Glide.with(this)
                        .load(R.drawable.satu_klipoh)
                        .into(imgInstruction);
                break;
            case 2 :
                Glide.with(this)
                        .load(R.drawable.dua_mendalan)
                        .into(imgInstruction);
                break;
            case 3 :
                Glide.with(this)
                        .load(R.drawable.tiga_ngasem)
                        .into(imgInstruction);
                break;
            case 4 :
                Glide.with(this)
                        .load(R.drawable.empat_punthuk)
                        .into(imgInstruction);
                break;
            case 5 :
                Glide.with(this)
                        .load(R.drawable.lima_tanjung)
                        .into(imgInstruction);
                break;
            case 6 :
                Glide.with(this)
                        .load(R.drawable.enam_terminal)
                        .into(imgInstruction);
                break;
        }
//        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+file_id,
//                new LazyHeaders.Builder()
//                        .addHeader("Authorization",getKeyToken)
//                        .build());
//
//        Glide.with(this)
//                .load(glideUrl)
//                .into(imgInstruction);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                openCamera(id);
//                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
//                Call<PlayModel> playCall = apiInterface.next(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(id));
//                playCall.enqueue(new Callback<PlayModel>() {
//                    @Override
//                    public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
//                        if(response.isSuccessful()){
//                            //FLOW_ID = response.body().getData().getNextFlow().getId();
//                            nextFlow(id);
//                        }else{
//                            Toast.makeText(ActivityPlayGame.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<PlayModel> call, Throwable t) {
//                        Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                prevFlow(id);
            }
        });
        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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
        openCamera(id);
//        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityPlayGame.this);
//        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_kamera,null);
//        linearLayout.setBackground(ContextCompat.getDrawable(ActivityPlayGame.this, R.drawable.bg_brace));
//        //View mView= LayoutInflater.from(this).inflate(R.layout.activity_open_camera,null);
//        dBuilder.setView(mView);
//        //ImageView imgView = mView.findViewById(R.id.icon_kamera);
//        TextView btnOpen = mView.findViewById(R.id.btnOpen);
//        TextView btnUpload = mView.findViewById(R.id.btnUpload);
//        btnUpload.setVisibility(View.INVISIBLE);
//        btnOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
//                Call<PlayModel> playCall = apiInterface.next(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(id));
//                playCall.enqueue(new Callback<PlayModel>() {
//                    @Override
//                    public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
//                        if(response.isSuccessful()){
//                            //FLOW_ID = response.body().getData().getNextFlow().getId();
//                            nextFlow(id);
//                        }else{
//                            Toast.makeText(ActivityPlayGame.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<PlayModel> call, Throwable t) {
//                        Toast.makeText(ActivityPlayGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//        dialog = dBuilder.create();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
    }

}
