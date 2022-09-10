package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;
import com.dicoding.picodiploma.treasurehunt_kotlin.java.model.response.PartyMember;

import java.util.List;

public class ListPlayerAdapter extends RecyclerView.Adapter<ListPlayerAdapter.MyViewHolder> {
    List<PartyMember> partyMembers;

    public ListPlayerAdapter(List<PartyMember> partyMemberList) {
        this.partyMembers = partyMemberList;

    }

    @NonNull
    @Override
    public ListPlayerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        ListPlayerAdapter.MyViewHolder mViewHolder = new ListPlayerAdapter.MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListPlayerAdapter.MyViewHolder holder, int position) {
        int player = position + 1;
        holder.txtName.setText(partyMembers.get(position).getUser().getProfile().getFullName());
        holder.txtPlayer.setText("Player "+player);
        holder.txtAsk.setVisibility(View.INVISIBLE);
        if(partyMembers.get(position).getBadge().equals("LEADER")){
            holder.txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.leader, 0);
        }
        if(partyMembers.get(position).getStatus().equals("READY")){
            holder.txtKick.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ready, 0);
        }else{
            holder.txtKick.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_notready, 0);
        }

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
    }
    public interface OnItemClickListener {
        void onItemClick(PartyMember item);
    }
}
