package com.example.quiztech.wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.ItemBankAccountBinding

class BankAccountAdapter(private val bankAccounts: List<BankAccount>) :
    RecyclerView.Adapter<BankAccountAdapter.BankAccountViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankAccountViewHolder {
        val binding =
            ItemBankAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BankAccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankAccountViewHolder, position: Int) {
        holder.bind(bankAccounts[position], position == selectedPosition)
        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int = bankAccounts.size

    fun getSelectedPosition(): Int {
        return selectedPosition
    }

    class BankAccountViewHolder(private val binding: ItemBankAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bankAccount: BankAccount, isSelected: Boolean) {
            binding.tvBankName.text = bankAccount.bankName
            binding.tvAccountNumber.text = "A/C: ${bankAccount.accountNumber}"
            binding.ivCheckmark.isChecked = isSelected
        }
    }
}