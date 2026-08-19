package com.example.quiztech.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.R

class WalletTransactionAdapter(
     val mockTests: ArrayList<Transactions>?,
    private val onEnrollClick: (Transactions) -> Unit
) : RecyclerView.Adapter<WalletTransactionAdapter.WalletTransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletTransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return WalletTransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalletTransactionViewHolder, position: Int) {
        val mockTest = mockTests?.get(position)
        holder.bind(mockTest)
    }

    override fun getItemCount(): Int = mockTests!!.size

    inner class WalletTransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTransactionType=itemView.findViewById<TextView>(R.id.tvTransactionType)
        val tvTransactionDate=itemView.findViewById<TextView>(R.id.tvTransactionDate)
        val tvTransactionAmount=itemView.findViewById<TextView>(R.id.tvTransactionAmount)
        val tvTransactionStatus=itemView.findViewById<TextView>(R.id.tvTransactionStatus)
        val ivTransactionIcon=itemView.findViewById<ImageView>(R.id.ivTransactionIcon)

        fun bind(mockTest: Transactions?) {

            if (mockTest!!.type!!.contains("withdraw")) {

                tvTransactionType.text = "Withdraw"
                tvTransactionAmount.text="- ₹ ${mockTest!!.amount}"

                tvTransactionAmount.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))

                ivTransactionIcon.setImageResource(R.drawable.ic_withdraw_circle)
            }else if (mockTest!!.type!!.contains("credit")||mockTest!!.type!!.contains("Credit")) {
                tvTransactionType.text = "Added Money"
                tvTransactionAmount.text="+ ₹ ${mockTest!!.amount}"
                tvTransactionAmount.setTextColor(itemView.context.resources.getColor(R.color.colorPrimary))
                ivTransactionIcon.setImageResource(R.drawable.ic_add_circle)
            }else if (mockTest!!.type!!.contains("reward")||mockTest!!.type!!.contains("Reward")) {
                tvTransactionType.text = "Reward Added"
                tvTransactionAmount.text="+ ₹ ${mockTest!!.amount}"
                tvTransactionAmount.setTextColor(itemView.context.resources.getColor(R.color.colorPrimary))
                ivTransactionIcon.setImageResource(R.drawable.ic_add_circle)
            } else if (mockTest!!.type!!.contains("Subscription purchase")|| mockTest!!.type!!.contains("subscription purchase")) {
                tvTransactionType.text = "Subscription purchase"
                tvTransactionAmount.text="- ₹ ${mockTest!!.amount}"
                tvTransactionAmount.setTextColor(itemView.context.resources.getColor(android.R.color.holo_red_dark))
                ivTransactionIcon.setImageResource(R.drawable.ic_withdraw_circle)


            }




            tvTransactionDate.text="${mockTest!!.createdAt}"

            if(mockTest!!.status=="success")
            {
                tvTransactionStatus.setTextColor(itemView.context.resources.getColor(R.color.colorPrimary))

            }else
            {
                tvTransactionStatus.setTextColor(itemView.context.resources.getColor(R.color.enroll_btn_color))
            }
            tvTransactionStatus.text="${mockTest!!.status}"
            itemView.setOnClickListener {
                onEnrollClick(mockTest!!)
            }
        }
    }
}