package com.example.quiztech.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quiztech.databinding.FragmentQuizWinningsBinding
import com.example.quiztech.databinding.FragmentWinningsBinding

class QuizWinningsFragment : Fragment() {

    private var _binding: FragmentQuizWinningsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizWinningsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize UI elements and set up listeners here
        // For example: binding.textViewWinnings.text = "Winnings"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}