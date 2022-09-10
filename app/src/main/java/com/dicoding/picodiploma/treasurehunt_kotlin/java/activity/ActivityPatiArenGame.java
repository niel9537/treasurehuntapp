package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import static com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config.MY_CAMERA_PERMISSION_CODE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Entity;
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
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.google.common.collect.Comparators;
import com.google.common.collect.Ordering;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import kotlin.jvm.internal.Lambda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPatiArenGame extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    Button submit_step;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    String FLOW_ID = "";
    List order;
    List<MiePatiArenData> patiArenDataList;
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
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        buildData();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //initRecyclerView();
        submit_step.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                order = patiArenDataList.stream().map(MiePatiArenData::getOrder).collect(Collectors.toList());
                Log.d("Size List",""+order);
                boolean sorted = Ordering.natural().isOrdered(order);
                if(sorted){
                    Intent intent = new Intent(ActivityPatiArenGame.this,ActivityPlayGame.class);
                    intent.putExtra("FLOW_ID",FLOW_ID);
                    intent.putExtra("STATUS", Config.PATI_GAME);
                    startActivity(intent);
                }else{
                    FancyToast.makeText(ActivityPatiArenGame.this,"Urutan masih salah ayo coba lagi !!!",FancyToast.LENGTH_SHORT,FancyToast.WARNING,true).show();
                }

            }
        });

    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Log.d("FromPosition",""+fromPosition);
            Log.d("toPosition",""+toPosition);
            Collections.swap(patiArenDataList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
   /* private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter();
        adapter.setDataList(list);

        ItemTouchHelper.Callback callback = new RecyclerRowMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
    }
*/
    private void buildData() {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<MiePatiArenModel> playCall = apiInterface.miepati(getKeyToken.toString(),getKeyTokenGame);
        playCall.enqueue(new Callback<MiePatiArenModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<MiePatiArenModel> call, Response<MiePatiArenModel> response) {
                if(response.isSuccessful()){
                    patiArenDataList = response.body().getData();
                    Collections.shuffle(patiArenDataList);
                    order = patiArenDataList.stream().map(MiePatiArenData::getOrder).collect(Collectors.toList());
                    Log.d("Size List",""+order);
                    recyclerAdapter = new RecyclerAdapter(patiArenDataList);
                    recyclerView.setAdapter(recyclerAdapter);


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
