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

import androidx.appcompat.app.AppCompatActivity;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestNextFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.FinishModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MiePatiArenData;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MiePatiArenModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBraceCredit extends AppCompatActivity {
    private int delayValue=2500;
    String FLOW_ID = "";
    String CONTENT = "";
    //4000=4 detik
    Button btnFinish;
    TextView txtContent;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        sharedPreferences=  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        getKeyTokenGame=sharedPreferences.getString(KEY_TOKEN_GAME,null);
        Bundle extras = getIntent().getExtras();
        btnFinish = findViewById(R.id.btnFinish);
        txtContent = findViewById(R.id.txtContent);
        if (extras != null) {
            FLOW_ID= extras.getString("FLOW_ID");
            CONTENT= extras.getString("CONTENT");
            Log.d("FLOW_ID BRACE CREDIT",""+FLOW_ID);
            //The key argument here must match that used in the other activity
        }
        txtContent.setText(CONTENT);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                Call<FinishModel> finishCall = apiInterface.finish(getKeyToken.toString(),getKeyTokenGame,new RequestNextFlow(FLOW_ID));
                finishCall.enqueue(new Callback<FinishModel>() {
                    @Override
                    public void onResponse(Call<FinishModel> call, Response<FinishModel> response) {
                        if(response.isSuccessful()){
                            startActivity(new Intent(ActivityBraceCredit.this,ActivityHome.class));
                        }else{
                            Toast.makeText(ActivityBraceCredit.this,"Error "+response.message().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FinishModel> call, Throwable t) {
                        Toast.makeText(ActivityBraceCredit.this,"Fail "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
