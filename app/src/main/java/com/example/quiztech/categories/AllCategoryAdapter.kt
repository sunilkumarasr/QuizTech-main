package com.example.quiztech.categories

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quiztech.Category
import com.example.quiztech.R
import com.example.quiztech.databinding.ItemAllCategoryGridBinding
import com.example.quiztech.exam.ExamListActivity
import com.example.quiztech.subcategories.SubCategoryActivity

// Placeholder data class - In Clean Arch, this would be an Entity or a View-specific model
data class AllCategoryItem(val name: String, val imageName: Int, val backgroundColorRes: Int)

class AllCategoryAdapter(
    private val context: Context, // Context is often needed for resource access or navigation
    private var categoriesList: ArrayList<Category>
) : RecyclerView.Adapter<AllCategoryAdapter.CategoryViewHolder>() {
    var colors=arrayOf("#F3E5F5","#E1F5FE","#E8F5E9","#FFF3E0")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemAllCategoryGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoriesList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = categoriesList.size

    fun updateData(newCategories: ArrayList<Category>) {
        categoriesList.clear()
        categoriesList.addAll(newCategories)
        notifyDataSetChanged() // In a more complex app, use DiffUtil for better performance
    }

    fun addCategories(categories: ArrayList<Category>) {
        categoriesList.clear()
        categoriesList.addAll(categories)
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(private val binding: ItemAllCategoryGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedCategory = categoriesList[position]
                    val ctx=binding.root.context
                   var intent= Intent(ctx, SubCategoryActivity::class.java).apply {
                        putExtra("cat_id",selectedCategory.id)
                    }


                    ctx.startActivity(intent)

                }
            }
        }

        fun bind(category: Category) {
            binding.tvCategoryItemName.text = category.name
            category.image?.let {

                Glide.with(itemView.context).load(it).into(binding.ivCategoryItemImage)

            } ?: binding.ivCategoryItemImage.setImageResource(R.drawable.ic_placeholder_illustration) // Default placeholder


            (binding.root as View).setBackgroundColor(Color.parseColor(colors[position%4]))

           // (binding.root.background as View).setBackgroundColor(Color.parseColor(colors[position%4]))

           // binding.root.background = ContextCompat.getDrawable(context, category.backgroundColorRes)
        }
    }
}