package com.example.quiztech

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class CategoryAdapter(
    private val categoriesList: ArrayList<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var colors=arrayOf("#F3E5F5","#E1F5FE","#E8F5E9","#FFF3E0")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoriesList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = categoriesList.size
    fun addCategories(categories: ArrayList<Category>) {

        categoriesList.addAll(categories)
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        private val categoryImage: ImageView = itemView.findViewById(R.id.ivCategoryImage)
        // private val cardView: MaterialCardView = itemView as MaterialCardView // If item_category root is MaterialCardView

        fun bind(category: Category) {
            categoryName.text = category.name
            category.image?.let {

                Glide.with(itemView.context).load(it).into(categoryImage)

            } ?: categoryImage.setImageResource(R.drawable.ic_placeholder_illustration) // Default placeholder

            (categoryImage.parent as View).setBackgroundColor(Color.parseColor(colors[position%4]))

            itemView.setOnClickListener {
                onItemClick(category)
            }
        }
    }
}