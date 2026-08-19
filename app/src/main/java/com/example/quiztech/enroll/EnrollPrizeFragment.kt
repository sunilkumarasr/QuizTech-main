package com.example.quiztech.enroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiztech.databinding.FragmentQuizWinningsBinding
import com.example.quiztech.databinding.FragmentWinningsBinding

class EnrollPrizeFragment : Fragment() {

    private var _binding: FragmentQuizWinningsBinding? = null
    private val binding get() = _binding!!

    private lateinit var rankPrizeAdapter: RankPrizeAdapter // We'll create this adapter next

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizWinningsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        // TODO: Load actual rank and prize data and submit to the adapter
        // Example: 
        // val ranks = listOf(RankPrize("1", "₹ 10,000"), RankPrize("2", "₹ 5,000"))
        // rankPrizeAdapter.submitList(ranks)
    }

    private fun setupRecyclerView() {
        rankPrizeAdapter = RankPrizeAdapter()
        val listPrize= ArrayList<RankPrize>()
        for ( i in 1..10)
        {
            listPrize.add(RankPrize(i.toString(),"₹ 5,000"))
        }
        rankPrizeAdapter.submitList(listPrize)
        binding.rvRankPrizes.apply {
            adapter = rankPrizeAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
