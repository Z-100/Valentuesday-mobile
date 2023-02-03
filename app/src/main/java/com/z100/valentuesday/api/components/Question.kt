package com.z100.valentuesday.api.components

data class Question(
    val id: Long,
    val question: String,
    val solution: Int,
    val answerOne: String,
    val answerTwo: String,
    val answerThree: String
)
