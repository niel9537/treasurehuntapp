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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestJoinGame;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestLogin;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.GameModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LoginModel;
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
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "treasureHunt";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_TOKEN_GAME = "key_token_game";
    String getKeyToken = "";

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
                    Call<GameModel> joinGameCall = apiInterface.joinGame(getKeyToken.toString(),new RequestJoinGame(codeInput.getText().toString()));
                    joinGameCall.enqueue(new Callback<GameModel>() {
                        @Override
                        public void onResponse(Call<GameModel> call, Response<GameModel> response) {
                            Log.d("API-login: ",  getKeyToken.toString()+"%%%%%"+codeInput.getText().toString());
                            editor.putString(KEY_TOKEN_GAME,""+response.body().getData().getGameToken().toString());
                            editor.apply();

                            Log.d("Token Game", " : " + response.body().getData().getGameToken().toString());
                            
                        }

                        @Override
                        public void onFailure(Call<GameModel> call, Throwable t) {

                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Masukkan Kode Permainan!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
