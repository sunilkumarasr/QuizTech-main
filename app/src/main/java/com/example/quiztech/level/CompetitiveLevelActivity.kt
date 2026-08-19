package com.example.quiztech.level

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.R
import com.example.quiztech.databinding.ActivityCompetitiveLevelBinding
import com.example.quiztech.level.CompetitiveLevelAdapter
import com.example.quiztech.level.CompetitiveLevelAdapter.CategoryViewHolder // Explicit import for clarity
import com.example.quiztech.level.CompetitiveLevelInnerAdapter // Import the inner adapter

class CompetitiveLevelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompetitiveLevelBinding
    private lateinit var competitiveLevelAdapter: CompetitiveLevelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompetitiveLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadCompetitiveLevels() // Load data after setup
    }

    private fun setupToolbar() {
        binding.layout.txtHeader.setText("Competitive Level")
        binding.layout.imgBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupRecyclerView() {
        // Instantiate the outer adapter with the item click listener
        competitiveLevelAdapter = CompetitiveLevelAdapter { itemId ->
            // Handle item click from the inner adapter
            Toast.makeText(this, "Clicked item: $itemId", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to details screen or perform other action
        }

        // Set up the outer RecyclerView
        binding.recyclerViewCompetitiveLevels.apply {
            layoutManager = LinearLayoutManager(this@CompetitiveLevelActivity, LinearLayoutManager.VERTICAL, false)
            adapter = competitiveLevelAdapter
        }
    }

    private fun loadCompetitiveLevels() {
        // --- Placeholder Data preparation for Adapter ---
        val placeholderIllustrationResId = R.drawable.icon_splash_logo // Ensure this drawable exists

        // Structure the data to match CompetitiveLevelCategoryUi for the outer adapter
        val sampleCategories = listOf(
            CompetitiveLevelCategoryUi(
                title = "RRB",
                items = listOf(
                    CompetitiveLevelItemUi(
                        id = "rrb_1",
                        name = "RRB",
                        description = "Lorem Ipsum is simply dummy text",
                        illustrationResId = placeholderIllustrationResId
                    )
                )
            ),
            CompetitiveLevelCategoryUi(
                title = "SSC",
                items = listOf(
                    CompetitiveLevelItemUi(
                        id = "ssc_1",
                        name = "SSC",
                        description = "Lorem Ipsum is simply dummy text",
                        illustrationResId = placeholderIllustrationResId
                    )
                )
            ),
            CompetitiveLevelCategoryUi(
                title = "Banking & Insurance",
                items = listOf(
                    CompetitiveLevelItemUi(
                        id = "banking_1",
                        name = "SBI",
                        description = "Lorem Ipsum is simply dummy text",
                        illustrationResId = placeholderIllustrationResId
                    ),
                    CompetitiveLevelItemUi(
                        id = "banking_2",
                        name = "IPBS",
                        description = "Lorem Ipsum is simply dummy text",
                        illustrationResId = placeholderIllustrationResId
                    )
                )
            ),
            CompetitiveLevelCategoryUi(
                title = "Civil Services",
                items = listOf(
                    CompetitiveLevelItemUi(
                        id = "civil_1",
                        name = "Civil Services",
                        description = "Lorem Ipsum is simply dummy text",
                        illustrationResId = placeholderIllustrationResId
                    )
                )
            )
        )
        
        // Submit the structured data to the outer adapter
        competitiveLevelAdapter.submitList(sampleCategories)
    }

    // Optional: Override onSupportNavigateUp if not handled by setNavigationOnClickListener
    // override fun onSupportNavigateUp(): Boolean {
    //     onBackPressedDispatcher.onBackPressed()
    //     return true
    // }
}
