package com.dicoding.picodiploma.treasurehunt_kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ItemOnBoardBinding

class OnBoardAdapter(private val item : List<OnBoardData>) : RecyclerView.Adapter<OnBoardAdapter.OnBoardViewHolder>(){
    inner class OnBoardViewHolder (itemView : ItemOnBoardBinding) : RecyclerView.ViewHolder(itemView.root){
        private val binding = itemView
        fun bind(data: OnBoardData){
            with(binding){
                Glide.with(itemView)
                    .load(data.image)
                    .into(itemImage)

                binding.textTitle.text = data.title
                binding.textDesc.text = data.desc
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardViewHolder {
        return OnBoardViewHolder(ItemOnBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: OnBoardViewHolder, position: Int) {
        holder.bind(item[position])
    }

    override fun getItemCount(): Int = item.size

}