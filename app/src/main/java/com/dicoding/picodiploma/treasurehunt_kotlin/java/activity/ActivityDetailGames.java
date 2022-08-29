package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.DetailGameModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.Gallery;
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
    private Handler slideHandler = new Handler();
    TextView txtTitleDetail, txtDetailDesc, txtSubtitle, txtTitle,txtTitle2, txtDescription;
    ViewPager2 view_pager_detail_game;
    Button play_game_detail_button;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    String getKeyToken = "";
    String id = "";
    List<Gallery> galleryList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_game);
        sharedPreferences=  getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
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
        detailGame();
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
