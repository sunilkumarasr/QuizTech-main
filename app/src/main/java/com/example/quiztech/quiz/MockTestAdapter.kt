package com.example.quiztech.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.quiztech.MockTest
import com.example.quiztech.R
import com.example.quiztech.model.MockList

class MockTestAdapter(
     var mockTests: ArrayList<MockTest>,
    private val onEnrollClick: (MockTest) -> Unit
) : RecyclerView.Adapter<MockTestAdapter.MockTestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MockTestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mock_test, parent, false)
        if (parent.parent is ViewPager2) {
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return MockTestViewHolder(view)
    }

    override fun onBindViewHolder(holder: MockTestViewHolder, position: Int) {
        val mockTest = mockTests[position]
        holder.bind(mockTest)
    }

    override fun getItemCount(): Int = mockTests.size
    fun addTest(mockList: ArrayList<MockTest>) {
        mockTests.clear()
        mockTests.addAll(mockList)
        notifyDataSetChanged()


    }

    inner class MockTestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textExamName)
        private val textExamType: TextView = itemView.findViewById(R.id.textExamType)
        private val textDate: TextView = itemView.findViewById(R.id.textDate)
        private val textTimeInfo: TextView = itemView.findViewById(R.id.textTimeInfo)
        private val textMaxMembers: TextView = itemView.findViewById(R.id.textMaxMembers)
        private val enrollButton: TextView = itemView.findViewById(R.id.buttonEnrollNow)
        private val textAttempts: TextView = itemView.findViewById(R.id.textAttempts)
        private val tvTag: TextView = itemView.findViewById(R.id.tvTag)

        fun bind(mockTest: MockTest) {
            title.text = mockTest.title
            
            if (mockTest.testType?.contains("free", ignoreCase = true) == true) {
                tvTag.text = "Free"
                tvTag.setBackgroundResource(R.drawable.bg_tag_free)
                tvTag.visibility = View.VISIBLE
            } else if (mockTest.testType?.contains("paid", ignoreCase = true) == true) {
                tvTag.text = "Paid"
                tvTag.setBackgroundResource(R.drawable.bg_tag_paid)
                tvTag.visibility = View.VISIBLE
            } else {
                tvTag.visibility = View.GONE
            }
            if (!mockTest.testType.isNullOrEmpty()) {
                textExamType.visibility = View.VISIBLE
                textExamType.text = mockTest.testType
            } else {
                textExamType.visibility = View.GONE
            }
            textDate.text = "Date: ${mockTest.pDate ?: "N/A"}"
            textTimeInfo.text = "Time: ${mockTest.pTime ?: "N/A"}"
            textMaxMembers.text = "Max Marks: ${mockTest.marks ?: "0"}"
            textAttempts.text = "Total Questions: ${mockTest.questions ?: "0"}"

            enrollButton.setOnClickListener {
                onEnrollClick(mockTest)
            }
        }
    }
}