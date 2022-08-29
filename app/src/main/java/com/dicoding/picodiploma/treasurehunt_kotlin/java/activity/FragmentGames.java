package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.adapter.ListGameAdapter;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.adapter.SliderAdapter;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.DataListGame;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.ListGameModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.checkprogress.CekProgressModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentGames extends Fragment {
    ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private ListGameAdapter listGameAdapter;
    private RecyclerView RecyclerView;
    private RecyclerView.LayoutManager LayoutManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    String getKeyToken = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games,container,false);
        viewPager2 = (ViewPager2) view.findViewById(R.id.view_pager_list);
        sharedPreferences=this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.manohara2));
        sliderItems.add(new SliderItem(R.drawable.manohara1));
        sliderItems.add(new SliderItem(R.drawable.manohara3));
        viewPager2.setAdapter(new SliderAdapter(sliderItems,viewPager2));
        RecyclerView = view.findViewById(R.id.list_game_rv);
        LayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        RecyclerView.setLayoutManager(LayoutManager);
        listGame();
        sliderBuild();
        return view;
    }
    private void listGame(){
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<ListGameModel> listGame = apiInterface.listgame(getKeyToken.toString());
        listGame.enqueue(new Callback<ListGameModel>() {
            @Override
            public void onResponse(Call<ListGameModel> call, Response<ListGameModel> response) {
                if(response.isSuccessful()){
                    List<DataListGame> dataListGames = response.body().getDataListGames();
                    listGameAdapter = new ListGameAdapter(dataListGames,getActivity(),getKeyToken);
                    RecyclerView.setAdapter(listGameAdapter);
                    Log.d("Size ListGame ",""+dataListGames.size());
                }
            }

            @Override
            public void onFailure(Call<ListGameModel> call, Throwable t) {

            }
        });
    }

    private void sliderBuild() {
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() +1);
        }
    };
}
