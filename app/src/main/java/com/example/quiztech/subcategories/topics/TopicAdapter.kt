package com.example.quiztech.subcategories.topics

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.R
import com.example.quiztech.databinding.ItemTopicBinding
import com.example.quiztech.model.Topic
import com.example.quiztech.exam.ExamListActivity

class TopicAdapter() :
    RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {
    private var colors=arrayOf("#F3E5F5","#E1F5FE","#E8F5E9","#FFF3E0")
    private var topics = mutableListOf<Topic>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
       Log.e("binding","binding $binding")
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = topics[position]
        holder.bind(topic)
        (holder.view as View).setBackgroundColor(Color.parseColor(colors[position % colors.size]))
        
        holder.view.setOnClickListener {
            val ctx = holder.view.context
            val intent = Intent(ctx, ExamListActivity::class.java).apply {
                putExtra("cat_id", "-1") // Or pass the actual catId if needed
                putExtra("sub_cat_id", topic.id)
                putExtra("topic_id", topic.id)
            }
            ctx.startActivity(intent)
        }
    }

    override fun getItemCount() = topics.size

    inner class TopicViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
            val tvSubcategoryName=view.findViewById<TextView>(R.id.tv_subcategory_name)
        fun bind(topic: Topic) {
            tvSubcategoryName.text = topic.name
        }
    }

    fun addTopics(newTopics: List<Topic>) {
        topics.clear()
        topics.addAll(newTopics)
        notifyDataSetChanged()
    }
}
