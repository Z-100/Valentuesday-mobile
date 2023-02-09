package com.z100.valentuesday.api.components

data class Account(
    val id: Long?,
    val username: String?,
    val password: String?,
    val activationKey: String?,
    val questionProgress: Long?,
    val preferences: Preferences?,
    val questions: List<Question>?
)
