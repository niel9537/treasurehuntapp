package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ItemBraceGameBinding
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ItemBraceHomeBinding

class GameBraceAdapter(private val item : List<BraceGameData>) : RecyclerView.Adapter<GameBraceAdapter.BraceViewHolder>() {
    inner class BraceViewHolder (itemView : ItemBraceGameBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView
        fun bind(data: BraceGameData){
            with(binding){
                Glide.with(itemView)
                    .load(data.imageBrace)
                    .into(braceImage)

                detailTitle1.text = data.title1
                detailTitle2.text = data.title2
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BraceViewHolder {
        return BraceViewHolder(ItemBraceGameBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BraceViewHolder, position: Int) {
        holder.bind(item[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailGameActivity::class.java)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = item.size
}