package com.dicoding.picodiploma.treasurehunt_kotlin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.treasurehunt_kotlin.api.games.list.GameDatas
import com.dicoding.picodiploma.treasurehunt_kotlin.databinding.ItemListgameBinding

class ListGameAdapter: RecyclerView.Adapter<ListGameAdapter.ListGameViewHolder>() {
    private var list = arrayListOf<GameDatas>()
    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun setItemClicked(data : GameDatas)
    }

    fun setonItemClickCallback(onItemClickCallback : OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun listTransaksi(arrayList: ArrayList<GameDatas>) {

        this.list = arrayList
    }

    inner class ListGameViewHolder(itemview : ItemListgameBinding) : RecyclerView.ViewHolder(itemview.root) {
        private val binding = itemview

        fun bind(data: GameDatas){
            with(binding){
                /*
                Glide.with(itemView)
                    .load(data)
                    .into(imageView5)

                 */

  /*              textView9.text = data.title
                textView7.text = data.title
                textView8.text = data.description
*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListGameViewHolder {
        return ListGameViewHolder(ItemListgameBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ListGameViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailGameActivity::class.java)

            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.findViewById<Button>(R.id.btnPlayGame).setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailGameActivity::class.java)

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size
}