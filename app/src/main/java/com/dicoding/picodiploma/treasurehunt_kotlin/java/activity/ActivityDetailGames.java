package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.adapter.GalleryAdapter;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.adapter.SliderAdapter;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestJoinGame;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestNextFlow;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.DetailGameModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.Gallery;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.InputGameCodeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PlayModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.UserMeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailGames extends AppCompatActivity {

    FloatingActionButton back;
    ImageView imgDetail,imgGame;
    AlertDialog dialog;
    private Handler slideHandler = new Handler();
    EditText input_code;
    Button play_button;
    TextView txtTitleDetail, txtDetailDesc, txtSubtitle, txtTitle,txtTitle2, txtDescription;
    ViewPager2 view_pager_detail_game;
    Button play_game_detail_button;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    private static final String KEY_LOBBY_ID = "key_lobby_id";
    private static final String KEY_GAME_ID = "key_game_id";
    private static final String KEY_MEMBER_ID = "key_member_id";
    String getKeyToken = "";
    String id = "";
    List<Gallery> galleryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_game);
        sharedPreferences=  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        back = findViewById(R.id.back);
        imgDetail = findViewById(R.id.imgDetail);
        imgGame = findViewById(R.id.imgGame);
        txtTitleDetail = findViewById(R.id.txtTitleDetail);
        txtDetailDesc = findViewById(R.id.txtDetailDesc);
        txtSubtitle = findViewById(R.id.txtSubtitle);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle2 = findViewById(R.id.txtTitle2);
        txtDescription = findViewById(R.id.txtDescription);
        view_pager_detail_game = findViewById(R.id.view_pager_detail_game);
        play_game_detail_button = findViewById(R.id.play_game_detail_button);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id= extras.getString("id");
            Log.d("ID GAME ", " : " + id);
            //The key argument here must match that used in the other activity
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        play_game_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlay();
            }
        });
        detailGame();
    }

    public void dialogPlay(){
        AlertDialog.Builder dBuilder = new AlertDialog.Builder(ActivityDetailGames.this);
        View mView= LayoutInflater.from(this).inflate(R.layout.dialog_play_game,null);
        dBuilder.setView(mView);
        input_code = mView.findViewById(R.id.input_code);
        play_button = mView.findViewById(R.id.play_button);
        input_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                play_button.setBackgroundColor(ContextCompat.getColor(ActivityDetailGames.this, R.color.login_gray));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                play_button.setBackgroundColor(ContextCompat.getColor(ActivityDetailGames.this, R.color.green));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                play_button.setBackgroundColor(ContextCompat.getColor(ActivityDetailGames.this, R.color.green));
            }
        });
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!input_code.getText().toString().isEmpty()){
                    dialog.dismiss();
                    ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                    Call<InputGameCodeModel> joinGameCall = apiInterface.joinGame(getKeyToken.toString(),new RequestJoinGame(input_code.getText().toString().toUpperCase()));
                    joinGameCall.enqueue(new Callback<InputGameCodeModel>() {
                        @Override
                        public void onResponse(Call<InputGameCodeModel> call, Response<InputGameCodeModel> response) {
                            if(response.isSuccessful()){
                                editor.putString(KEY_TOKEN_GAME,""+response.body().getData().getGameToken().toString());
                                editor.putString(KEY_LOBBY_ID,""+response.body().getData().getLobbyId().toString());
                                editor.putString(KEY_GAME_ID,""+response.body().getData().getGameId().toString());
                                editor.apply();
                                Log.d("API-login: ",  getKeyToken.toString()+"%%%%%"+input_code.getText().toString());
                                Log.d("Token Game", " : " + response.body().getData().getGameToken().toString());
                                Log.d("Game Id", " : " + response.body().getData().getGameId().toString());
                                Log.d("Lobby Id", " : " + response.body().getData().getLobbyId().toString());
                                meGame(response.body().getData().getGameToken().toString());

                            }else{
                                Toast.makeText(ActivityDetailGames.this, "Error "+response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<InputGameCodeModel> call, Throwable t) {
                            Toast.makeText(ActivityDetailGames.this, "Fail "+t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(ActivityDetailGames.this, "Masukkan Kode Permainan!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog = dBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void meGame(String tokenGame) {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<MeModel> meCall = apiInterface.me(getKeyToken.toString(),tokenGame);
        meCall.enqueue(new Callback<MeModel>() {
            @Override
            public void onResponse(Call<MeModel> call, Response<MeModel> response) {
                if(response.isSuccessful()){
                    Log.d("Status ", " : " + response.body().getDataMeModel().getStatus().toString());
                    Log.d("Badge ", " : " + response.body().getDataMeModel().getBadge().toString());
                    String name = response.body().getDataMeModel().getUser().getProfile().getFullName().toString();
                    editor.putString(KEY_MEMBER_ID,""+response.body().getDataMeModel().getId().toString());
                    editor.apply();
                    startActivity(new Intent(ActivityDetailGames.this,ActivitySplashBrace.class));
                    //player1.setText(name);
                }else{
                    Log.d("Status ", " : " + response.code());
                    Toast.makeText(ActivityDetailGames.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MeModel> call, Throwable t) {

            }
        });

    }
    private void detailGame(){
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<DetailGameModel> detailGameCall = apiInterface.detailgame(getKeyToken.toString(),id);
        detailGameCall.enqueue(new Callback<DetailGameModel>() {
            @Override
            public void onResponse(Call<DetailGameModel> call, Response<DetailGameModel> response) {
                if(response.isSuccessful()){
                    txtTitleDetail.setText(response.body().getData().getTitle());
                    txtDetailDesc.setText(response.body().getData().getDescription());
                    txtSubtitle.setText(response.body().getData().getSubTitle());
                    txtTitle.setText(response.body().getData().getTitle());
                    txtTitle2.setText(response.body().getData().getTitle());
                    txtDescription.setText(response.body().getData().getDescription());
                    if(response.body().getData().getBanner()!=null){
                        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+response.body().getData().getBanner().getFileId(),
                                new LazyHeaders.Builder()
                                        .addHeader("Authorization",getKeyToken)
                                        .build());
                        Glide.with(ActivityDetailGames.this)
                                .load(glideUrl)
                                .into(imgDetail);
                        Glide.with(ActivityDetailGames.this)
                                .load(glideUrl)
                                .into(imgGame);
                    }else{
                        GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/",
                                new LazyHeaders.Builder()
                                        .addHeader("Authorization",getKeyToken)
                                        .build());
                        Glide.with(ActivityDetailGames.this)
                                .load(glideUrl)
                                .placeholder(R.drawable.banner_brace)
                                .into(imgDetail);
                        Glide.with(ActivityDetailGames.this)
                                .load(glideUrl)
                                .placeholder(R.drawable.banner_brace)
                                .into(imgGame);
                    }
                    galleryList = response.body().getData().getGalleries();
                    view_pager_detail_game.setAdapter(new GalleryAdapter(galleryList,view_pager_detail_game,ActivityDetailGames.this,getKeyToken));
                    sliderBuild();
                }
            }

            @Override
            public void onFailure(Call<DetailGameModel> call, Throwable t) {

            }
        });
    }
    private void sliderBuild() {
        view_pager_detail_game.setClipToPadding(false);
        view_pager_detail_game.setClipChildren(false);
        view_pager_detail_game.setOffscreenPageLimit(3);
        view_pager_detail_game.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        view_pager_detail_game.setPageTransformer(compositePageTransformer);
        view_pager_detail_game.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable,2000);
            }
        });
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            view_pager_detail_game.setCurrentItem(view_pager_detail_game.getCurrentItem() +1);
        }
    };
}
