package com.example.quiztech.exam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.ItemCompetitiveLevelBinding
import com.example.quiztech.databinding.ItemExamListBinding

class ExamListAdapter(private val exams: List<Exam>) : RecyclerView.Adapter<ExamListAdapter.ExamViewHolder>() {

    inner class ExamViewHolder(private val binding: ItemExamListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(exam: Exam) {
            binding.textExamName.text = exam.examName
            binding.textExamType.text = exam.examType
            binding.textAttempts.text = exam.attempts
            binding.textQuestionsCount.text = "Questions Count: " + exam.questionsCount
            binding.textMaxMarks.text = "Max Marks: " + exam.maxMarks
            binding.textTime.text = "Time: " + exam.time

            // Handle button click if needed
            // binding.buttonEnrollNow.setOnClickListener { ... }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val binding = ItemExamListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        holder.bind(exams[position])
    }

    override fun getItemCount(): Int = exams.size
}
