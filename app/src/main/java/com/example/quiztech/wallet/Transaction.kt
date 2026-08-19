package com.example.quiztech.wallet

data class Transaction(
    val type: String,
    val date: String,
    val amount: String,
    val status: String,
    val transactionType: TransactionType
)

enum class TransactionType {
    ADDED,
    WITHDRAW
}