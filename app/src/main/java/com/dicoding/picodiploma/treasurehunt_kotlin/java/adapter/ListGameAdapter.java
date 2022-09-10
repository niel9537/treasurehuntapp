package com.dicoding.picodiploma.treasurehunt_kotlin.java.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.activity.ActivityDetailGames;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.DataListGame;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.ListGameModel;

import java.util.List;

public class ListGameAdapter extends RecyclerView.Adapter<ListGameAdapter.MyViewHolder> {
    private List<DataListGame> dataListGames;
    private Context context;
    private String token;

    public ListGameAdapter(List<DataListGame> dataListGames, Context context, String token) {
        this.dataListGames = dataListGames;
        this.context = context;
        this.token = token;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtSubtitle, txtDescription;
        public Button btnPlayGame;
        public ImageView imgGame;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubtitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            btnPlayGame = (Button) itemView.findViewById(R.id.btnPlayGame);
            imgGame = (ImageView) itemView.findViewById(R.id.imgGame);
        }
    }

    @Override
    public ListGameAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listgame, parent, false);
        ListGameAdapter.MyViewHolder mViewHolder = new ListGameAdapter.MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListGameAdapter.MyViewHolder holder, int position) {
        final DataListGame dataListGame = dataListGames.get(position);
        holder.txtTitle.setText(dataListGame.getTitle());
        holder.txtDescription.setText(dataListGame.getDescription());
        holder.txtSubtitle.setText(dataListGame.getSubTitle());
        if(dataListGame.getBanner() != null){
            GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+dataListGame.getBanner().getFileId().toString(),
                    new LazyHeaders.Builder()
                            .addHeader("Authorization",token)
                            .build());
            Glide.with(holder.itemView.getContext())
                    .load(glideUrl)
                    .placeholder(R.drawable.banner_brace)
                    .into(holder.imgGame);
        }else{
            GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/",
                    new LazyHeaders.Builder()
                            .addHeader("Authorization",token)
                            .build());
            Glide.with(holder.itemView.getContext())
                    .load(glideUrl)
                    .placeholder(R.drawable.banner_brace)
                    .into(holder.imgGame);
        }
        holder.btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ActivityDetailGames.class);
                intent.putExtra("id",dataListGame.getId());
                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataListGames.size();
    }
}
