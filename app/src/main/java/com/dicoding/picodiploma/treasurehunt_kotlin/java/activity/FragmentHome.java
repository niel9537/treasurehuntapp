package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestJoinGame;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.InputGameCodeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.UserMeModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {
    EditText codeInput;
    Button playButton;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    SharedPreferences sharedPreferences;
    TextView username_welcome;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    private static final String KEY_LOBBY_ID = "key_lobby_id";
    private static final String KEY_GAME_ID = "key_game_id";
    private static final String KEY_MEMBER_ID = "key_member_id";
    String getKeyToken = "";
    ViewPager2 viewPager2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        sharedPreferences=this.getActivity().getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        getKeyToken=sharedPreferences.getString(KEY_TOKEN,null);
        Log.d("CHECKING: ", getKeyToken.toString());
        codeInput = view.findViewById(R.id.input_code);
        playButton = view.findViewById(R.id.play_button);
        username_welcome = view.findViewById(R.id.username_welcome);
/*        viewPager2 = (ViewPager2) view.findViewById(R.id.view_pager_home);
        RecyclerView.Adapter viewPagerAdapter = new R(getActivity());
        viewPager2.setAdapter(viewPagerAdapter);*/
        meProfile();
        codeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                playButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.login_gray));
                playButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                playButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
                playButton.setEnabled(true);
                play();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                playButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
                playButton.setEnabled(true);
                play();
            }
        });

        return view;
    }

    private void play() {

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!codeInput.getText().toString().isEmpty()){
                    ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
                    Call<InputGameCodeModel> joinGameCall = apiInterface.joinGame(getKeyToken.toString(),new RequestJoinGame(codeInput.getText().toString().toUpperCase()));
                    joinGameCall.enqueue(new Callback<InputGameCodeModel>() {
                        @Override
                        public void onResponse(Call<InputGameCodeModel> call, Response<InputGameCodeModel> response) {
                            if(response.isSuccessful()){
                                editor.putString(KEY_TOKEN_GAME,""+response.body().getData().getGameToken().toString());
                                editor.putString(KEY_LOBBY_ID,""+response.body().getData().getLobbyId().toString());
                                editor.putString(KEY_GAME_ID,""+response.body().getData().getGameId().toString());

                                editor.apply();
                                Log.d("API-login: ",  getKeyToken.toString()+"%%%%%"+codeInput.getText().toString());
                                Log.d("Token Game", " : " + response.body().getData().getGameToken().toString());
                                Log.d("Game Id", " : " + response.body().getData().getGameId().toString());
                                Log.d("Lobby Id", " : " + response.body().getData().getLobbyId().toString());
                                startActivity(new Intent(getActivity(),ActivitySplashBrace.class));
                            }else{
                                Toast.makeText(getActivity(), "Error "+response.body().getResponseMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<InputGameCodeModel> call, Throwable t) {
                            Toast.makeText(getActivity(), "Fail "+t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Masukkan Kode Permainan!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void meProfile(){
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<UserMeModel> userMeModelCall = apiInterface.profilme(getKeyToken.toString());
        userMeModelCall.enqueue(new Callback<UserMeModel>() {
            @Override
            public void onResponse(Call<UserMeModel> call, Response<UserMeModel> response) {
                if(response.isSuccessful()){
                    editor.putString(KEY_MEMBER_ID,""+response.body().getData().getId().toString());
                    editor.apply();
                    Log.d("Member ID", " : " + response.body().getData().getId().toString());
                    username_welcome.setText("Hallo, "+response.body().getData().getProfile().getFullName());
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
