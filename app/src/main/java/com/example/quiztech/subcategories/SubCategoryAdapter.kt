package com.example.quiztech.subcategories

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.ItemSubcategoryBinding
import com.example.quiztech.exam.ExamListActivity
import com.example.quiztech.model.SubCategory
import com.example.quiztech.subcategories.topics.TopicActivity

class SubCategoryAdapter(private var subCategories: ArrayList<SubCategory>,var cat_id: String) :
    RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {
    var colors=arrayOf("#F3E5F5","#E1F5FE","#E8F5E9","#FFF3E0")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val binding = ItemSubcategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(subCategories[position])
        (holder.binding.root as View).setBackgroundColor(Color.parseColor(colors[position%4]))
        holder.  binding.root.setOnClickListener {
            val position = position
            if (position != RecyclerView.NO_POSITION) {
                val selectedCategory = subCategories[position]
                val ctx=holder.binding.root.context
                var intent= Intent(ctx, TopicActivity::class.java).apply {
                    putExtra("cat_id",cat_id)
                    putExtra("sub_cat_id",selectedCategory.id)
                }


                ctx.startActivity(intent)

            }
        }

    }

    override fun getItemCount() = subCategories.size
    fun addCategories(categories: ArrayList<SubCategory>) {
        subCategories.clear()
        subCategories.addAll(categories)
        notifyDataSetChanged()
    }

    inner class SubCategoryViewHolder( val binding: ItemSubcategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subCategory: SubCategory) {
            binding.tvSubcategoryName.text = subCategory.name
        }

    }
}