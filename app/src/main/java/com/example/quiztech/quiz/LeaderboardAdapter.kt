package com.example.quiztech.quiz

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.LeaderboardItemBinding
import java.util.Random


class LeaderboardAdapter(private val leaderboard: ArrayList<Leaderboard>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LeaderboardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = leaderboard[position]
        holder.binding.userName.text = entry.fullName
        holder.binding.userScore.text = "Rank: ${entry.userRank}"

        val name = entry.fullName ?: "U"
        val initial = if (name.isNotEmpty()) name[0].uppercaseChar().toString() else "U"
        
        holder.binding.tvUserInitial.text = initial
        holder.binding.tvUserInitial.visibility = View.VISIBLE
        
        // Set a random background color for the initial
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        holder.binding.userImage.apply {
            setImageResource(0) // Remove default avatar
            setBackgroundColor(color)
        }
    }

    override fun getItemCount() = leaderboard.size

    class ViewHolder(val binding: LeaderboardItemBinding) : RecyclerView.ViewHolder(binding.root)
}
