package com.example.quiztech.wallet

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.R
import com.example.quiztech.databinding.ItemTransactionBinding

class TransactionHistoryAdapter(
    private var transactions: ArrayList<Transactions>,
    private val onItemClick: (Transactions) -> Unit
) : RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    fun updateTransactions(newTransactions: List<Transactions>) {
        transactions.clear()
        transactions.addAll(newTransactions)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transactions) {
            val type = transaction.type?.lowercase() ?: ""
            
            if (type.contains("withdraw")) {
                binding.tvTransactionType.text = "Withdrawal"
                binding.tvTransactionAmount.text = "- ₹ ${transaction.amount}"
                binding.tvTransactionAmount.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
                binding.ivTransactionIcon.setImageResource(R.drawable.ic_withdraw_circle)
            }else if (type.contains("credit")||type.contains("Credit")) {
                binding.tvTransactionType.text = "Added Money"
                binding.tvTransactionAmount.text = "+ ₹ ${transaction.amount}"
                binding.tvTransactionAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.success_color))
                binding.ivTransactionIcon.setImageResource(R.drawable.ic_add_circle)
            }else if (type.contains("reward")||type.contains("Reward")) {
                binding.tvTransactionType.text = "Reward Received"
                binding.tvTransactionAmount.text = "+ ₹ ${transaction.amount}"
                binding.tvTransactionAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.success_color))
                binding.ivTransactionIcon.setImageResource(R.drawable.ic_add_circle)
            } else if (type.contains("Subscription purchase")|| type.contains("subscription purchase")) {
                binding.tvTransactionType.text = "Subscription purchase"
                binding.tvTransactionAmount.text = "- ₹ ${transaction.amount}"
                binding.tvTransactionAmount.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
                binding.ivTransactionIcon.setImageResource(R.drawable.ic_withdraw_circle)
            }
            else {
                binding.tvTransactionType.text = if (type == "reward") "Reward Received" else "Added Money"
                binding.tvTransactionAmount.text = "+ ₹ ${transaction.amount}"
                binding.tvTransactionAmount.setTextColor(ContextCompat.getColor(itemView.context, R.color.success_color))
                binding.ivTransactionIcon.setImageResource(R.drawable.ic_add_circle)
            }

            binding.tvTransactionDate.text = transaction.createdAt
            binding.tvTransactionStatus.text = transaction.status

            if (transaction.status?.lowercase() == "success") {
                binding.tvTransactionStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.success_color))
            } else if (transaction.status?.lowercase() == "failed") {
                binding.tvTransactionStatus.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
            } else {
                binding.tvTransactionStatus.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_orange_dark))
            }

            binding.root.setOnClickListener {
                onItemClick(transaction)
            }
        }
    }
}
