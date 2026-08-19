package com.example.quiztech.enroll

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.ItemRankPrizeBinding
import com.example.quiztech.databinding.ItemSubjectLayoutBinding

class SubjectListAdapter : ListAdapter<Subject, SubjectListAdapter.SubjectListViewHolder>(RankPrizeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectListViewHolder {
        val binding = ItemSubjectLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectListViewHolder, position: Int) {
        val rankPrize = getItem(position)
        holder.bind(rankPrize)
    }

    inner class SubjectListViewHolder(private val binding: ItemSubjectLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rankPrize: Subject) {
            binding.textQuestionsCount.text = rankPrize.questionsCount
            binding.textTime.text = rankPrize.time
            binding.textMaxMarks.text = rankPrize.maxMarks
        }
    }

    class RankPrizeDiffCallback : DiffUtil.ItemCallback<Subject>() {
        override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
            return oldItem.maxMarks == newItem.maxMarks // Assuming rank is a unique identifier
        }

        override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
            return oldItem == newItem
        }
    }
}
