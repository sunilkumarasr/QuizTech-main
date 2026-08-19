package com.example.quiztech.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.WinningItemBinding

data class WinningsEntry(val name: String, val amount: String)

class WinningsAdapter(private val winnings: List<WinningsEntry>) :
    RecyclerView.Adapter<WinningsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WinningItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = winnings[position]
        holder.binding.tvName.text = entry.name
        holder.binding.tvPrize.text = entry.amount
    }

    override fun getItemCount() = winnings.size

    class ViewHolder(val binding: WinningItemBinding) : RecyclerView.ViewHolder(binding.root)
}