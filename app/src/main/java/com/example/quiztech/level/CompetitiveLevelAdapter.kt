package com.example.quiztech.level

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.R

// Explicitly import the data classes and adapters
import com.example.quiztech.level.CompetitiveLevelItemUi
import com.example.quiztech.level.CompetitiveLevelCategoryUi
import com.example.quiztech.level.CompetitiveLevelInnerAdapter

// --- Adapter for the Outer Vertical RecyclerView ---

// Define View Types for the outer adapter
private const val VIEW_TYPE_CATEGORY = 0 // Represents a category with its inner RecyclerView

// --- DiffUtil for efficient list updates ---
object CompetitiveLevelCategoryDiffCallback : DiffUtil.ItemCallback<CompetitiveLevelCategoryUi>() {
    override fun areItemsTheSame(oldItem: CompetitiveLevelCategoryUi, newItem: CompetitiveLevelCategoryUi): Boolean {
        return oldItem.title == newItem.title // Assuming title is unique enough for categories
    }

    override fun areContentsTheSame(oldItem: CompetitiveLevelCategoryUi, newItem: CompetitiveLevelCategoryUi): Boolean {
        return oldItem == newItem
    }
}

class CompetitiveLevelAdapter(private val onItemClick: (itemId: String) -> Unit) :
    ListAdapter<CompetitiveLevelCategoryUi, CompetitiveLevelAdapter.CategoryViewHolder>(CompetitiveLevelCategoryDiffCallback) { // Now uses CompetitiveLevelCategoryUi

    // --- ViewHolder for Category with Inner RecyclerView ---
    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewCategoryTitle: TextView = view.findViewById(R.id.textViewCategoryTitle)
        val recyclerViewInnerItems: RecyclerView = view.findViewById(R.id.recyclerViewInnerItems)
        private lateinit var innerAdapter: CompetitiveLevelInnerAdapter

        fun bind(category: CompetitiveLevelCategoryUi, onItemClick: (itemId: String) -> Unit) {
            textViewCategoryTitle.text = category.title

            // Initialize or reuse the inner adapter
            if (!::innerAdapter.isInitialized) {
                innerAdapter = CompetitiveLevelInnerAdapter(onItemClick)
            }

            // Set up the inner RecyclerView
            recyclerViewInnerItems.apply {
                // Ensure layout manager is set if not set in XML or if dynamic
                if (layoutManager == null) {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) // Horizontal scrolling
                }
                // Set the adapter only if it's not already set, or reassign if necessary
                if (adapter == null || adapter != innerAdapter) {
                    adapter = innerAdapter
                }
                // Submit list to the inner adapter
                innerAdapter.submitList(category.items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_CATEGORY // We only have one view type for categories in the outer list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_competitive_level_category, parent, false) // Use the new outer item layout
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category, onItemClick)
    }
}
