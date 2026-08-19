package com.example.quiztech.enroll

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.ItemRankPrizeBinding

class RankPrizeAdapter : ListAdapter<RankPrize, RankPrizeAdapter.RankPrizeViewHolder>(RankPrizeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankPrizeViewHolder {
        val binding = ItemRankPrizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankPrizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankPrizeViewHolder, position: Int) {
        val rankPrize = getItem(position)
        holder.bind(rankPrize)
    }

    inner class RankPrizeViewHolder(private val binding: ItemRankPrizeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rankPrize: RankPrize) {
            binding.tvRankValue.text = rankPrize.rank
            binding.tvPrizeValue.text = rankPrize.prize
        }
    }

    class RankPrizeDiffCallback : DiffUtil.ItemCallback<RankPrize>() {
        override fun areItemsTheSame(oldItem: RankPrize, newItem: RankPrize): Boolean {
            return oldItem.rank == newItem.rank // Assuming rank is a unique identifier
        }

        override fun areContentsTheSame(oldItem: RankPrize, newItem: RankPrize): Boolean {
            return oldItem == newItem
        }
    }
}
