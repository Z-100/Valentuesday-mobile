package com.z100.valentuesday.service

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.z100.valentuesday.api.components.Question
import com.z100.valentuesday.util.Const.Factory.SP_ACCESS_TOKEN
import com.z100.valentuesday.util.Const.Factory.SP_ACTIVATION_KEY
import com.z100.valentuesday.util.Const.Factory.SP_ALL_QUESTIONS
import com.z100.valentuesday.util.Const.Factory.SP_TOTAL_PROGRESS
import com.z100.valentuesday.util.Logger

class DataManagerService(private val sp: SharedPreferences) {

    fun clearAll(): Boolean {
        sp.edit().apply {
            remove(SP_ACTIVATION_KEY)
            remove(SP_ALL_QUESTIONS)
            remove(SP_ACCESS_TOKEN)
            remove(SP_TOTAL_PROGRESS)
        }.apply()

        return sp.getString(SP_ACTIVATION_KEY, null) == null
    }

    fun addActivationKey(activationKey: String): Boolean {

        sp.edit().apply {
            putString(SP_ACTIVATION_KEY, activationKey)
        }.apply()

        return sp.getString(SP_ACTIVATION_KEY, null) != null
    }

    fun getActivationKey(): String? {
        return sp.getString(SP_ACTIVATION_KEY, null)
    }

    fun updateTotalQuestionProgress(totalProgress: Long): Boolean {
        sp.edit().apply {
            putLong(SP_TOTAL_PROGRESS, totalProgress)
        }.apply()

        return sp.getLong(SP_TOTAL_PROGRESS, -1) != -1L
    }

    fun getTotalQuestionProgress(): Long? {
        val long = sp.getLong(SP_TOTAL_PROGRESS, -1)
        return if (long != -1L) long else null
    }

    fun addAccessToken(jwt: String): Boolean {
        sp.edit().apply {
            putString(SP_ACCESS_TOKEN, jwt)
        }.apply()

        return sp.getString(SP_ACCESS_TOKEN, null) != null
    }

    fun getAccessToken(): String? {
        return sp.getString(SP_ACCESS_TOKEN, null)
    }

    fun addAllQuestions(allQuestions: List<Question>): Boolean {

        val json = Gson().toJson(allQuestions)

        sp.edit().apply {
            putString(SP_ALL_QUESTIONS, json)
        }.apply()

        return sp.getString(SP_ALL_QUESTIONS, null) != null
    }

    fun getAllQuestions(): List<Question>? {
        val json = sp.getString(SP_ALL_QUESTIONS, null) ?: return null

        val type = TypeToken.getParameterized(List::class.java, Question::class.java).type

        return Gson().fromJson(json, type)
    }

    fun getSpecificQuestion(id: Int): Question? {
        val json = sp.getString(SP_ALL_QUESTIONS, null) ?: return null

        Logger.log("#getSpecificQuestion: $json", this.javaClass)

        val type = TypeToken.getParameterized(List::class.java, Question::class.java).type

        val questions: List<Question> = Gson().fromJson(json, type)

        return questions[id]
    }

    fun allQuestionsExists(): Boolean {
        return sp.getString(SP_ALL_QUESTIONS, null) != null
    }
}