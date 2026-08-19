package com.example.quiztech.quiz

import com.google.gson.annotations.SerializedName

data class ExamResultResponse(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ExamResultData? = ExamResultData()
)

data class ExamResultData(
    @SerializedName("summary") var summary: ExamSummary? = ExamSummary(),
    @SerializedName("questions") var questions: ArrayList<ExamQuestionResult> = arrayListOf(),
    @SerializedName("sections") var sections: ArrayList<ExamSectionResult> = arrayListOf(),
    @SerializedName("examInfo") var examInfo: ExamInfo? = ExamInfo(),
    @SerializedName("winners") var winners: ArrayList<Winner>? = arrayListOf(),
    @SerializedName("myScore") var myScore: ExamMyScore? = ExamMyScore()
)

data class Winner(
    @SerializedName("full_name") var fullName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("correct_answers") var correctAnswers: String? = null,
    @SerializedName("user_rank") var userRank: String? = null,
    @SerializedName("prize") var prize: String? = null
)

data class ExamMyScore(
    @SerializedName("full_name") var fullName: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("correct_answers") var correctAnswers: String? = null,
    @SerializedName("user_rank") var userRank: String? = null,
    @SerializedName("prize") var prize: String? = null
)

data class ExamSummary(
    @SerializedName("total_questions") var totalQuestions: String? = null,
    @SerializedName("attempted_questions") var attemptedQuestions: String? = null,
    @SerializedName("correct_answers") var correctAnswers: String? = null,
    @SerializedName("percentage") var percentage: String? = null
)

data class ExamQuestionResult(
    @SerializedName("question_id") var questionId: String? = null,
    @SerializedName("question_text") var questionText: String? = null,
    @SerializedName("answer_id") var answerId: String? = null,
    @SerializedName("is_correct_answer") var isAnswerCorrect: String? = null,
    @SerializedName("given_answer") var givenAnswer: String? = null,
    @SerializedName("correct_answer") var correctAnswer: String? = null,
    @SerializedName("answer_status") var answerStatus: String? = null,
    @SerializedName("answer_description") var answer_description: String? = null
)

data class ExamSectionResult(
    @SerializedName("section_id") var sectionId: String? = null,
    @SerializedName("section_name") var sectionName: String? = null,
    @SerializedName("total_answered") var totalAnswered: String? = null,
    @SerializedName("total_correct") var totalCorrect: String? = null
)

data class ExamInfo(
    @SerializedName("exam_name") var examName: String? = null,
    @SerializedName("category_name") var categoryName: String? = null
)
