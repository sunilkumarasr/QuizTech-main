package com.example.quiztech.wallet

data class BankAccount(
    val bankName: String,
    val accountNumber: String,
    val accountHolderName: String,
    val ifscCode: String,
    var isSelected: Boolean = false
)