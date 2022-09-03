package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.request.RequestKick;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.KickModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.LobbyDetailModel;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PartyMember;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiHelper;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.network.ApiInterface;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLobbySetting extends AppCompatActivity {
    String TOKEN = "";
    String TOKENGAME = "";
    String LOBBY = "";
    TextView txtBack;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_setting);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_player_setting);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TOKEN= extras.getString("TOKEN");
            TOKENGAME= extras.getString("TOKENGAME");
            LOBBY= extras.getString("LOBBY");
            //The key argument here must match that used in the other activity
        }
        txtBack = findViewById(R.id.txtBack);
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLobbySetting.this,ActivityLobby.class));
            }
        });

        lobbyDetail();

    }
    private void lobbyDetail() {
        /*final LinearLayout ly = (LinearLayout) findViewById(R.id.ly);
        ly.setOrientation(LinearLayout.VERTICAL);*/
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<LobbyDetailModel> lobbydetailCall = apiInterface.lobbydetail(TOKEN,LOBBY,TOKENGAME);
        lobbydetailCall.enqueue(new Callback<LobbyDetailModel>() {
            @Override
            public void onResponse(Call<LobbyDetailModel> call, Response<LobbyDetailModel> response) {
                if(response.isSuccessful()){
                    List<PartyMember> partyMembers = response.body().getData().getPartyMembers();
/*                    mAdapter = new ListPlayerAdapter(partyMembers);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();*/
                    mRecyclerView.setAdapter(new PlayerAdapter(partyMembers, new PlayerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(PartyMember item) {
                            kick(item.getId());

                        }
                    }));
/*                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,20,0,10);
                    for(int i=0; i<response.body().getData().getPartyMembers().size(); i++){
                        TextView textView = new TextView(ActivityLobby.this);
                        textView.setText(response.body().getData().getPartyMembers().get(i).getUser().getProfile().getFullName()+" - "+response.body().getData().getPartyMembers().get(i).getBadge().toString());
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        textView.setTextColor(ContextCompat.getColor(ActivityLobby.this, R.color.dark_blue));
                        textView.setPadding(15,10,15,10);
                        textView.setBackground(ContextCompat.getDrawable(ActivityLobby.this, R.drawable.player));
                        textView.setLayoutParams(params);
                        ly.addView(textView);
                    }*/
                }else{
                    Toast.makeText(ActivityLobbySetting.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LobbyDetailModel> call, Throwable t) {
                Toast.makeText(ActivityLobbySetting.this,"Fail : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void kick(String id) {
        ApiInterface apiInterface = ApiHelper.getClient().create(ApiInterface.class);
        Call<KickModel> kickCall = apiInterface.kick(TOKEN,TOKENGAME,new RequestKick(id));
        kickCall.enqueue(new Callback<KickModel>() {
            @Override
            public void onResponse(Call<KickModel> call, Response<KickModel> response) {
                if(response.isSuccessful()){
                    FancyToast.makeText(ActivityLobbySetting.this,"Player bernama "+response.body().getData().getMember().getUser().getProfile().getFullName().toString()+" berhasil di kick !!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    startActivity(new Intent(ActivityLobbySetting.this,ActivityLobby.class));
                }else{
                    Toast.makeText(ActivityLobbySetting.this,"Error : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KickModel> call, Throwable t) {
                Toast.makeText(ActivityLobbySetting.this,"Fail : "+t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
