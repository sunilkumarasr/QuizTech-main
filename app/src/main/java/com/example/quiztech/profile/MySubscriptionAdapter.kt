package com.example.quiztech.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.ItemMySubscriptionBinding
import com.example.quiztech.model.SubscriptionPlan
import java.util.ArrayList

class MySubscriptionAdapter(private val subscriptions: ArrayList<SubscriptionPlan>) :
    RecyclerView.Adapter<MySubscriptionAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMySubscriptionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMySubscriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subscription = subscriptions[position]
        holder.binding.tvTitle.text = subscription.planTitle
       // holder.binding.tvPrice.text = "Price: ₹${subscription.shortDescriptions}"
        holder.binding.tvPrice.text = "Remaining Days: ${subscription.remainingDays}"
        var string="Price: ₹ ${subscription.price}"
        string=string+"\n\nQuantity: ${subscription.paidQnty}"
        holder.binding.tvTestType.text = string

    }

    override fun getItemCount(): Int = subscriptions.size
}
