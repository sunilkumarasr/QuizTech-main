package com.example.quiztech.level

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.R

// --- Inner Adapter for Horizontal RecyclerView ---

object CompetitiveLevelItemDiffCallback : DiffUtil.ItemCallback<CompetitiveLevelItemUi>() {
    override fun areItemsTheSame(oldItem: CompetitiveLevelItemUi, newItem: CompetitiveLevelItemUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CompetitiveLevelItemUi, newItem: CompetitiveLevelItemUi): Boolean {
        return oldItem == newItem
    }
}

class CompetitiveLevelInnerAdapter(private val onItemClick: (itemId: String) -> Unit) :
    ListAdapter<CompetitiveLevelItemUi, CompetitiveLevelInnerAdapter.ItemViewHolder>(CompetitiveLevelItemDiffCallback) {

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewIllustration: ImageView = view.findViewById(R.id.imageViewSubject)
        val textViewName: TextView = view.findViewById(R.id.textViewName)
        val textViewDescription: TextView = view.findViewById(R.id.textViewDescription)

        fun bind(item: CompetitiveLevelItemUi) {
            imageViewIllustration.setImageResource(item.illustrationResId)
            textViewName.text = item.name
            textViewDescription.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_competitive_level, parent, false)
        return ItemViewHolder(view).apply {
            itemView.setOnClickListener { // Click listener on the card itself
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    onItemClick(item.id)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
