package com.z100.valentuesday.api.service

import com.android.volley.Request.Method.*
import com.android.volley.VolleyError
import com.z100.valentuesday.MainActivity
import com.z100.valentuesday.api.Callback
import com.z100.valentuesday.api.GsonRequest
import com.z100.valentuesday.api.components.Account
import com.z100.valentuesday.api.components.Preferences
import com.z100.valentuesday.api.components.Question
import com.z100.valentuesday.util.Const
import com.z100.valentuesday.util.Const.Factory.API_URL_ACCOUNT
import com.z100.valentuesday.util.Const.Factory.API_URL_PREFERENCES
import com.z100.valentuesday.util.Const.Factory.API_URL_QUESTION
import java.lang.Exception

class ApiRequestService {

    fun checkActivationKey(activationKey: String, callback: Callback<Account>) {
        val req = GsonRequest(GET, API_URL_ACCOUNT, Account::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            }).withParam(Const.API_PARAM_ACT_KEY, activationKey)
        MainActivity.requestQueue.add(req)
    }

    fun updatePreferences(preferences: Preferences, callback: Callback<Preferences>) {
        val req = GsonRequest(GET, API_URL_PREFERENCES, Preferences::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            }).withBody(preferences)
        MainActivity.requestQueue.add(req)
    }

    fun getQuestion(id: Long, callback: Callback<Question>) {
        val req = GsonRequest(GET, "$API_URL_PREFERENCES/$id", Question::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            })
        MainActivity.requestQueue.add(req)
    }

    fun updateQuestion(question: Question, callback: Callback<Question>) {
        val req = GsonRequest(GET, API_URL_PREFERENCES, Question::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            }).withBody(question)
        MainActivity.requestQueue.add(req)
    }

    fun getNextQuestionFor(activationKey: String, callback: Callback<Question>) {
        val req = GsonRequest(GET, "$API_URL_PREFERENCES/next-for/$activationKey", Question::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            })
        MainActivity.requestQueue.add(req)
    }

    fun getAllQuestionsFor(activationKey: String, callback: Callback<List<Question>>) {
        val req = GsonRequest(GET, "$API_URL_QUESTION/all-for-act-key/$activationKey", List::class.java,
            { res ->
                try {
                    callback.handle(res as List<Question>, null)
                } catch (e: Exception) {
                    callback.handle(null, VolleyError(e.message))
                }
            }, { err ->
                callback.handle(null, err)
            })
        MainActivity.requestQueue.add(req)
    }
}