package com.z100.valentuesday.api.service

import com.android.volley.Request.Method.*
import com.android.volley.VolleyError
import com.z100.valentuesday.MainActivity
import com.z100.valentuesday.api.Callback
import com.z100.valentuesday.api.GsonRequest
import com.z100.valentuesday.api.components.JwtDTO
import com.z100.valentuesday.api.components.Preferences
import com.z100.valentuesday.api.components.Question
import com.z100.valentuesday.util.Const
import com.z100.valentuesday.util.Const.Factory.API_PARAM_ACT_KEY
import com.z100.valentuesday.util.Const.Factory.API_PARAM_AUTHORIZATION
import com.z100.valentuesday.util.Const.Factory.API_URL_CHECK_ACT_KEY
import com.z100.valentuesday.util.Const.Factory.API_URL_PREFERENCES
import com.z100.valentuesday.util.Const.Factory.API_URL_QUESTION
import java.lang.Exception

class ApiRequestService {

    fun checkActivationKey(activationKey: String, callback: Callback<JwtDTO>) {
        val req = GsonRequest(POST, API_URL_CHECK_ACT_KEY, JwtDTO::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            }).withHeader(API_PARAM_ACT_KEY, activationKey)
        MainActivity.requestQueue.add(req)
    }

    fun updatePreferences(preferences: Preferences, callback: Callback<Preferences>) {
        val req = GsonRequest(PUT, API_URL_PREFERENCES, Preferences::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            }).withBody(preferences)
        MainActivity.requestQueue.add(req)
    }

    fun getQuestion(id: Long, callback: Callback<Question>) {
        val req = GsonRequest(GET, "$API_URL_QUESTION/$id", Question::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            })
        MainActivity.requestQueue.add(req)
    }

    fun getNextQuestionFor(jwt: String, callback: Callback<Question>) {
        val req = GsonRequest(GET, "$API_URL_QUESTION/next-for", Question::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            }).withHeader(API_PARAM_AUTHORIZATION, "Bearer $jwt")
        MainActivity.requestQueue.add(req)
    }

    fun getAllQuestionsFor(jwt: String, callback: Callback<List<Question>>) {
        val req = GsonRequest(GET, "$API_URL_QUESTION/all-for-act-key", List::class.java,
            { res ->
                try {
                    callback.handle(res as List<Question>, null)
                } catch (e: Exception) {
                    callback.handle(null, VolleyError(e.message))
                }}, { err ->
                callback.handle(null, err)
            }).withHeader(API_PARAM_AUTHORIZATION, "Bearer $jwt")
        MainActivity.requestQueue.add(req)
    }

    fun getTotalQuestionProgress(jwt: String, callback: Callback<Long>) {
        val req = GsonRequest(GET, "$API_URL_QUESTION/progress", Long::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            }).withHeader(API_PARAM_AUTHORIZATION, "Bearer $jwt")
        MainActivity.requestQueue.add(req)
    }

    fun updateTotalQuestionProgress(jwt: String, callback: Callback<Long>) {
        val req = GsonRequest(PUT, "$API_URL_QUESTION/progress", Long::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            }).withHeader(API_PARAM_AUTHORIZATION, "Bearer $jwt")
        MainActivity.requestQueue.add(req)
    }

    fun resetTotalQuestionProgress(jwt: String, callback: Callback<Long>) {
        val req = GsonRequest(PATCH, "$API_URL_QUESTION/progress", Long::class.java,
            { res -> callback.handle(res, null) }, { err ->
                callback.handle(null, err)
            }).withHeader(API_PARAM_AUTHORIZATION, "Bearer $jwt")
        MainActivity.requestQueue.add(req)
    }
}