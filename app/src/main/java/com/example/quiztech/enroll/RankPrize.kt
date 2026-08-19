package com.example.quiztech.enroll

data class RankPrize(
    val rank: String,
    val prize: String
)

data class Subject(
    val examName: String,
    val examType: String,
    val attempts: String,
    val questionsCount: String,
    val maxMarks: String,
    val time: String
)