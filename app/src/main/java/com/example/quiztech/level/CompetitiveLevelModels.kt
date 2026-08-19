package com.example.quiztech.level

import androidx.annotation.DrawableRes

// Represents a single competitive level category (e.g., RRB, SSC)
data class CompetitiveLevelCategoryUi(
    val title: String,
    val items: List<CompetitiveLevelItemUi>
)

// Represents a single item within a competitive level category (e.g., RRB, SBI)
data class CompetitiveLevelItemUi(
    val id: String, // Unique identifier
    val name: String,
    val description: String,
    @DrawableRes val illustrationResId: Int // Drawable resource ID for the image
)
