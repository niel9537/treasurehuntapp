package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ItemBraceHomeBinding

class HomeBraceAdapter(private val item : List<BraceData>) : RecyclerView.Adapter<HomeBraceAdapter.BraceViewHolder>() {
    inner class BraceViewHolder (itemView : ItemBraceHomeBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = itemView
        fun bind(data: BraceData){
            with(binding){
                Glide.with(itemView)
                    .load(data.imageBrace)
                    .into(braceImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BraceViewHolder {
        return BraceViewHolder(ItemBraceHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BraceViewHolder, position: Int) {
        holder.bind(item[position])

        val context = holder.itemView.context.applicationContext

        holder.itemView.setOnClickListener { view ->
            //Navigation.findNavController().navigate(R.id.action_homeFragment_to_detailBraceFragment)

            //Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_detailBraceFragment)

            //view.findNavController().navigate(R.id.action_homeFragment_to_detailBraceFragment)

            val intent = Intent(holder.itemView.context, DetailBraceActivity::class.java)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = item.size
}