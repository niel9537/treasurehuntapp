package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestNextFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTransportInstruction extends AppCompatActivity {
    Button button_berangkat;
    TextView desc_berangkat;
    ImageView imgTransport;
    String FILE_ID = "";
    String FILE_TYPE = "";
    String FLOW_ID = "";
    String POST_ID = "";
    String GAME_ID = "";
    String CONTENT = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_instruction);
        imgTransport = findViewById(R.id.imgTransport);
        button_berangkat = findViewById(R.id.btnTransport);
        desc_berangkat = findViewById(R.id.txtContent);
        sharedPreferences=  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        getKeyTokenGame=sharedPreferences.getString(KEY_TOKEN_GAME,null);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            FLOW_ID= extras.getString("FLOW_ID");
            FILE_ID= extras.getString("FILE_ID");
            CONTENT= extras.getString("CONTENT");
            Log.d("FLOW_ID 11", " : " + FLOW_ID);
            Log.d("CONTENT 11", " : " + CONTENT);
            Log.d("FILE_ID 11", " : " + FILE_ID);
            //The key argument here must match that used in the other activity
        }
        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+FILE_ID,
                new LazyHeaders.Builder()
                        .addHeader("Authorization",getKeyToken)
                        .build());

        Glide.with(this)
                .load(glideUrl)
                .placeholder(R.drawable.vw)
                .into(imgTransport);
        desc_berangkat.setText(CONTENT);
        button_berangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                Call<PlayModel> playCall = apiInterface.next(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(FILE_ID));
                playCall.enqueue(new Callback<PlayModel>() {
                    @Override
                    public void onResponse(Call<PlayModel> call, Response<PlayModel> response) {
                        if(response.isSuccessful()){
                            //FLOW_ID = response.body().getData().getNextFlow().getId();*/
                            Intent intent = new Intent(ActivityTransportInstruction.this,ActivityPlayGame.class);
                            intent.putExtra("FLOW_ID",FLOW_ID);
                            intent.putExtra("STATUS",Config.TRANSPORT_INSTRUCTION);
                            startActivity(intent);
                        /*}else{
                            Toast.makeText(ActivityTransportInstruction.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlayModel> call, Throwable t) {
                        Toast.makeText(ActivityTransportInstruction.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });*/
            }
        });
    }
}
