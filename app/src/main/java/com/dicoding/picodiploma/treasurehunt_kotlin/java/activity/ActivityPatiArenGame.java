package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import static com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config.MY_CAMERA_PERMISSION_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.codescanner.CodeScanner;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestNextFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.Data;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MiePatiArenData;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MiePatiArenModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPatiArenGame extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button submit_step;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    String FLOW_ID = "";
    private ArrayList<MiePatiArenData> list = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list_step);
        sharedPreferences=  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        getKeyTokenGame=sharedPreferences.getString(KEY_TOKEN_GAME,null);
        Button submit_step = findViewById(R.id.submit_step);
        Log.d("KEY TOKEN ", " : " + getKeyToken);
        Log.d("KEY TOKEN GAME", " : " + getKeyTokenGame);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FLOW_ID= extras.getString("FLOW_ID");
            Log.d("FLOW_ID PATI", " : " + FLOW_ID);
            //The key argument here must match that used in the other activity
        }
        buildData();
        initRecyclerView();
        submit_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPatiArenGame.this,ActivityPlayGame.class);
                intent.putExtra("FLOW_ID",FLOW_ID);
                intent.putExtra("STATUS", Config.PATI_GAME);
                startActivity(intent);
            }
        });

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        adapter.setDataList(list);

        ItemTouchHelper.Callback callback = new RecyclerRowMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
    }

    private void buildData() {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<MiePatiArenModel> playCall = apiInterface.miepati(getKeyToken.toString(),getKeyTokenGame);
        playCall.enqueue(new Callback<MiePatiArenModel>() {
            @Override
            public void onResponse(Call<MiePatiArenModel> call, Response<MiePatiArenModel> response) {
                if(response.isSuccessful()){
                    List<MiePatiArenData> data = response.body().getData();
                    for(int i=0;i<response.body().getData().size();i++){
                        list.add(new MiePatiArenData(data.get(i).getId(),data.get(i).getContent(),data.get(i).getName(),data.get(i).getGameClassification(),data.get(i).getOrder(),data.get(i).getCreatedAt(),data.get(i).getUpdatedAt()));

                    }

                }else{
                    Toast.makeText(ActivityPatiArenGame.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MiePatiArenModel> call, Throwable t) {
                Toast.makeText(ActivityPatiArenGame.this,"Error : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }



}
