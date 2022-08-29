package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.MiePatiArenData;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewModel>
        implements RecyclerRowMoveCallback.RecyclerViewRowTouchHelperContract {
    private List<MiePatiArenData> dataList;

    public void setDataList(List<MiePatiArenData> dataList) {
        this.dataList = dataList;

    }

    @NonNull
    @Override
    public MyViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_step, parent, false);
        return new MyViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewModel holder, int position) {
        holder.judul_step.setText(dataList.get(position).getName());
        holder.desc_step.setText(dataList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onRowMoved(int from, int to) {
        if(from < to) {
            for(int i = from; i < to; i++) {
                Collections.swap(dataList, i, i+1);
            }
        } else {
            for (int i = from; i > to; i--) {
                Collections.swap(dataList, i, i-1);
            }
        }
        notifyItemMoved(from, to);
    }

    @Override
    public void onRowSelected(MyViewModel myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.BLUE);
    }

    @Override
    public void onRowClear(MyViewModel myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    class MyViewModel extends RecyclerView.ViewHolder {

        TextView judul_step,desc_step;

        public MyViewModel(View view) {
            super(view);
            judul_step = view.findViewById(R.id.judul_step);
            desc_step = view.findViewById(R.id.desc_step);
        }
    }
}