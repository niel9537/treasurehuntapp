package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PartyMember;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyViewHolder> {
    List<PartyMember> partyMembers;
    OnItemClickListener listener;
    private int checkedPosition = 0;
    private boolean select = false;
    public PlayerAdapter(List<PartyMember> partyMemberList, OnItemClickListener listener) {
        this.partyMembers = partyMemberList;
        this.listener = listener;
    }
    public void setPartyMembers(List<PartyMember> partyMemberList) {
        this.partyMembers = new ArrayList<>();
        this.partyMembers = partyMembers;
        notifyDataSetChanged();
    }
    public PlayerAdapter(List<PartyMember> partyMembers) {
    }


    @NonNull
    @Override
    public PlayerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        PlayerAdapter.MyViewHolder mViewHolder = new PlayerAdapter.MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.MyViewHolder holder, int position) {
        int player = position + 1;
        holder.txtName.setText(partyMembers.get(position).getUser().getProfile().getFullName());
        holder.txtPlayer.setText("Player "+player);
        holder.txtAsk.setVisibility(View.INVISIBLE);
        if(partyMembers.get(position).getBadge().equals("LEADER")){
            holder.txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.leader, 0);
            holder.txtKick.setVisibility(View.GONE);
        }
        holder.txtKick.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemKick(partyMembers.get(position));
            }
        });

        holder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(select == false ){
                        if(!partyMembers.get(position).getBadge().equals("LEADER")){
                            select = true;
                            holder.txtName.setBackgroundResource(R.drawable.player_selected);
                            listener.onItemChangeLeader(partyMembers.get(position));
                            holder.txtAsk.setVisibility(View.VISIBLE);
                            holder.txtAsk.setText("Promote "+partyMembers.get(position).getUser().getProfile().getFullName()+" as Lobby Leader?");
                        }

                    }
                }
            });


        //holder.bind();
/*        //idkuu = cabangList.get(position).getId_diskon();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(view.getContext(), EditCabang.class);
                mIntent.putExtra("id_cabang", cabangList.get(position).getId_cabang());
                mIntent.putExtra("nama_cabang", cabangList.get(position).getNama_cabang());
                mIntent.putExtra("notelp_cabang", cabangList.get(position).getNotelp_cabang());
                mIntent.putExtra("alamat_cabang", cabangList.get(position).getAlamat_cabang());
                mIntent.putExtra("tanggal_cabang", cabangList.get(position).getTanggal_cabang());
                view.getContext().startActivity(mIntent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return partyMembers.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtKick, txtPlayer, txtName, txtAsk;
        public MyViewHolder(View itemView) {
            super(itemView);
            txtKick = (TextView) itemView.findViewById(R.id.txtKick);
            txtPlayer = (TextView) itemView.findViewById(R.id.txtPlayer);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtAsk = (TextView) itemView.findViewById(R.id.txtAsk);
        }

       /* void bind(final PartyMember partyMember){
            if(checkedPosition ==-1){
                //Nothing change
            }else{
                if(checkedPosition == getAdapterPosition()){
                    txtPlayer.setBackgroundResource(R.drawable.player_selected);
                }else {
                    //nothing change
                }
            }

            txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkedPosition != ghe)
                }
            });
        }*/
    }
    public interface OnItemClickListener {
        void onItemKick(PartyMember item);
        void onItemChangeLeader(PartyMember item);
    }

}
