package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.UserMeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAccount extends Fragment {
    CircleImageView imgProfil;
    TextView txtTermsCondition, txtEmail, txtName, txtAbout;
    Button sign_out;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    String getKeyToken = "";
    String getKeyTokenGame = "";
    public static Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);
        sharedPreferences=this.getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        getKeyTokenGame=sharedPreferences.getString(KEY_TOKEN_GAME,null);
        txtTermsCondition = view.findViewById(R.id.txtTermsCondition);
        context = getContext();
        sign_out = view.findViewById(R.id.sign_out);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        imgProfil = view.findViewById(R.id.imgProfil);
        txtAbout = view.findViewById(R.id.txtAbout);
        txtAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ActivityTermCondition.class));
            }
        });
        txtTermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ActivityTermCondition.class));
            }
        });

        meProfile();
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences =sharedPreferences;
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getActivity(),ActivityLogin.class));
            }
        });
        return view;

    }

    private void meProfile(){
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<UserMeModel> userMeModelCall = apiInterface.profilme(getKeyToken.toString());
        userMeModelCall.enqueue(new Callback<UserMeModel>() {
            @Override
            public void onResponse(Call<UserMeModel> call, Response<UserMeModel> response) {
                if(response.isSuccessful()){
                    txtName.setText(response.body().getData().getProfile().getFullName());
                    txtEmail.setText(response.body().getData().getProfile().getFullName());
                    Glide.with(context)
                            .load("http://brokenfortest")
                            .placeholder(R.drawable.cat)
                            .into(imgProfil);
                }else{
                    Toast.makeText(getActivity(), "Fail "+response.body().getResponseMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserMeModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Fail "+t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
