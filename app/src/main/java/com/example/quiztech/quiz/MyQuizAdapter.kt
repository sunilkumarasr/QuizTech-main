package com.example.quiztech.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.R

class MyQuizAdapter(
     var MyQuizs: ArrayList<MyQuiz>,
    private val onEnrollClick: (MyQuiz) -> Unit
) : RecyclerView.Adapter<MyQuizAdapter.MyQuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyQuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mock_test, parent, false)
        return MyQuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyQuizViewHolder, position: Int) {
        val MyQuiz = MyQuizs[position]
        holder.bind(MyQuiz)
    }

    override fun getItemCount(): Int = MyQuizs.size
    fun addTest(mockList: ArrayList<MyQuiz>) {
        MyQuizs.clear()
        MyQuizs.addAll(mockList)
        notifyDataSetChanged()


    }

    inner class MyQuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textExamName)
        private val textExamType: TextView = itemView.findViewById(R.id.textExamType)
        private val attempts: TextView = itemView.findViewById(R.id.textAttempts)
        private val textDate: TextView = itemView.findViewById(R.id.textDate)
        private val textMaxMarks: TextView = itemView.findViewById(R.id.textMaxMarks)
        private val textTime: TextView = itemView.findViewById(R.id.textTimeInfo)
        private val textMaxMembers: TextView = itemView.findViewById(R.id.textMaxMembers)
        private val textAttempts: TextView = itemView.findViewById(R.id.textAttempts)
        private val enrollButton: TextView = itemView.findViewById(R.id.buttonEnrollNow)
        private val tvTag: TextView = itemView.findViewById(R.id.tvTag)


        fun bind(myQuiz: MyQuiz) {
            title.text = myQuiz.title
            textMaxMembers.visibility=View.GONE
            textAttempts.visibility=View.GONE
            textDate.text = "Date: ${myQuiz.pDate ?: "N/A"}"
            textTime.text = "Time: ${myQuiz.pTime ?: "N/A"}"
            //textMaxMembers.text = "Max Marks: ${myQuiz ?: "0"}"
            //attempts.text = "Total Questions: ${myQuiz.questions ?: "0"}"



            if(myQuiz.isCompleted==1)
            {
                textExamType.text = "Completed on : ${myQuiz.completedAt}"
            }else
            {
                textExamType.text = "Enrolled on : ${myQuiz.enrolledAt}"
            }

             if(myQuiz.test_type=="Free Test")
                    {
                        tvTag.text = "Free"
                    }else
                    {
                        tvTag.text = "Paid"
                    }



            //enrollButton.text = MyQuiz.enrollButtonText

           /* val countHtml = HtmlCompat.fromHtml("Questions Count: <b>${MyQuiz.questionsCount}</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            val maxMarks = HtmlCompat.fromHtml("Max Marks: <b>${MyQuiz.maxMarks}</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            val timeInMinutes = HtmlCompat.fromHtml("Time: <b>${MyQuiz.timeInMinutes} minutes</b>", HtmlCompat.FROM_HTML_MODE_LEGACY)
textQuestionsCount.text=countHtml
textMaxMarks.text=maxMarks
textTime.text=timeInMinutes*/
            enrollButton.setOnClickListener {
                onEnrollClick(myQuiz)
            }
        }
    }
}