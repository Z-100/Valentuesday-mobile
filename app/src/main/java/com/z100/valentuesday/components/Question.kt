package com.z100.valentuesday.components

data class Question(
    val id: Long,
    val question: String,
    val solution: Long,
    val answerOne: String,
    val answerTwo: String,
    val answerThree: String
)
