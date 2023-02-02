package com.z100.valentuesday.api.service

import com.android.volley.Request.Method.*
import com.z100.valentuesday.MainActivity
import com.z100.valentuesday.api.Callback
import com.z100.valentuesday.api.GsonRequest
import com.z100.valentuesday.components.Account
import com.z100.valentuesday.components.Preferences
import com.z100.valentuesday.components.Question
import com.z100.valentuesday.util.Const
import com.z100.valentuesday.util.Const.Factory.API_URL_ACCOUNT
import com.z100.valentuesday.util.Const.Factory.API_URL_PREFERENCES
import java.lang.RuntimeException

class ApiService {

    fun checkActivationKey(activationKey: String, callback: Callback) {
        val req = GsonRequest(GET, API_URL_ACCOUNT, Account::class.java,
            { res -> callback.handle(res) }, { err ->
                callback.handle(err)
            }).withParam(Const.API_PARAM_ACT_KEY, activationKey)
        MainActivity.requestQueue.add(req)
    }

    fun updatePreferences(preferences: Preferences, callback: Callback<Preferences>) {
        val req = GsonRequest(GET, API_URL_PREFERENCES, Preferences::class.java,
            { res -> callback.handle(res) }, { err ->
                throw RuntimeException(err.cause)
            }).withBody(preferences)
        MainActivity.requestQueue.add(req)
    }

    fun getQuestion(id: Long, callback: Callback<Question>) {
        val req = GsonRequest(GET, "$API_URL_PREFERENCES/$id", Question::class.java,
            { res -> callback.handle(res) }, { err ->
                throw RuntimeException(err.cause)
            })
        MainActivity.requestQueue.add(req)
    }

    fun updateQuestion(question: Question, callback: Callback<Question>) {
        val req = GsonRequest(GET, API_URL_PREFERENCES, Question::class.java,
            { res -> callback.handle(res) }, { err ->
                throw RuntimeException(err.cause)
            }).withBody(question)
        MainActivity.requestQueue.add(req)
    }

    fun getAllQuestionsFor(activationKey: String, callback: Callback<List<Question>>) {
        val req = GsonRequest(GET, "$API_URL_PREFERENCES/$activationKey", List::class.java,
            { res -> callback.handle((res as List<Question>)) }, { err ->
                throw RuntimeException(err.cause)
            })
        MainActivity.requestQueue.add(req)
    }
}