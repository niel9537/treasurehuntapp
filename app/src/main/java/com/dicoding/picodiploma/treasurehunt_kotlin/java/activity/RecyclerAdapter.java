package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MiePatiArenData;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    List<MiePatiArenData> miePatiArenData;
    public static String idkuu = "";
    public RecyclerAdapter(List<MiePatiArenData> miePatiArenDataList) {
        miePatiArenData = miePatiArenDataList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_step, parent, false);
        RecyclerAdapter.MyViewHolder mViewHolder = new RecyclerAdapter.MyViewHolder(mView);
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder (RecyclerAdapter.MyViewHolder holder, final int position){
        holder.judul_step.setText(miePatiArenData.get(position).getName());
        holder.desc_step.setText(miePatiArenData.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return miePatiArenData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView judul_step, desc_step;
        public MyViewHolder(View itemView) {
            super(itemView);
            judul_step = (TextView) itemView.findViewById(R.id.judul_step);
            desc_step = (TextView) itemView.findViewById(R.id.desc_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), ""+miePatiArenData.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }
}
