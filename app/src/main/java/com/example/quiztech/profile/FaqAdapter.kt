package com.example.quiztech.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.R
import com.example.quiztech.databinding.ItemFaqBinding

class FaqAdapter(private var faqs: List<Faq>) :
    RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding =
            ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.bind(faqs[position])
    }

    override fun getItemCount(): Int = faqs.size

    fun filter(query: String) {
        // Not yet implemented
    }

    class FaqViewHolder(private val binding: ItemFaqBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(faq: Faq) {
            binding.tvQuestion.text = faq.question
            binding.tvAnswer.text = faq.answer

            binding.tvAnswer.visibility = if (faq.isExpanded) View.VISIBLE else View.GONE
            binding.ivArrow.setImageResource(if (faq.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)

            binding.llQuestion.setOnClickListener {
                faq.isExpanded = !faq.isExpanded
                binding.tvAnswer.visibility = if (faq.isExpanded) View.VISIBLE else View.GONE
                binding.ivArrow.setImageResource(if (faq.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
            }
        }
    }
}