package com.example.quiztech.profile

data class Faq(
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)