package com.dicoding.picodiploma.treasurehunt_kotlin.java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.activity.ActivityDetailGames;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.activity.SliderItem;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.config.Config;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.Gallery;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.SliderViewHolder>{
    private List<Gallery> galleryList;
    private ViewPager2 viewPager2;
    private Context context;
    private String token;
    public GalleryAdapter(List<Gallery> galleries, ViewPager2 viewPager2, ActivityDetailGames context, String token) {
        this.galleryList = galleries;
        this.viewPager2 = viewPager2;
        this.context = context;
        this.token = token;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brace_home,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        //holder.setImage(galleryList.get(position));
        if(galleryList != null){
            if(!galleryList.isEmpty()){
                GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/"+galleryList.get(position).getFileId().toString(),
                        new LazyHeaders.Builder()
                                .addHeader("Authorization",token)
                                .build());
                Glide.with(holder.itemView.getContext())
                        .load(glideUrl)
                        .placeholder(R.drawable.banner_brace)
                        .into(holder.brace_image);
            }else{
                GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/",
                        new LazyHeaders.Builder()
                                .addHeader("Authorization",token)
                                .build());
                Glide.with(holder.itemView.getContext())
                        .load(glideUrl)
                        .placeholder(R.drawable.banner_brace)
                        .into(holder.brace_image);
            }
        }else{
            GlideUrl glideUrl = new GlideUrl(Config.BASE_URL+"mobile/v1/file-uploads/",
                    new LazyHeaders.Builder()
                            .addHeader("Authorization",token)
                            .build());
            Glide.with(holder.itemView.getContext())
                    .load(glideUrl)
                    .placeholder(R.drawable.banner_brace)
                    .into(holder.brace_image);
        }
        if(position == galleryList.size() -2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        ImageView brace_image;
        SliderViewHolder(@NonNull View itemView){
            super(itemView);
            brace_image = itemView.findViewById(R.id.brace_image);

        }

        void setImage(SliderItem sliderItem){
            brace_image.setImageResource(sliderItem.getImage());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            galleryList.addAll(galleryList);
            notifyDataSetChanged();
        }
    };
}
