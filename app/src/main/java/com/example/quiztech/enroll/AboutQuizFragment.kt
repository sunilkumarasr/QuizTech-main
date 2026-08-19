package com.example.quiztech.enroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quiztech.R
import com.example.quiztech.databinding.FragmentAboutQuizBinding

class AboutQuizFragment : Fragment() {

    private var _binding: FragmentAboutQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subjectListAdapter=SubjectListAdapter()
        binding.recyclerSubjects.adapter=subjectListAdapter
        val listPrize= ArrayList<Subject>()
        for ( i in 1..10)
        {
            listPrize.add(Subject(i.toString(),"Subject $i","20","60","200","60"))
        }
        subjectListAdapter.submitList(listPrize)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
