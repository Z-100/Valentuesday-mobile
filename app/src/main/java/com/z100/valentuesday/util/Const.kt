package com.z100.valentuesday.util

class Const {
    companion object Factory {
        const val DEBUG = "debug"
        const val ERROR_IMPOSSIBLE_INPUT = "Input selected not possible"

        const val SP_NAME = "valentuesday"
        const val SP_ACTIVATION_KEY = "act-key"
        const val SP_ACCESS_TOKEN = "access-token"
        const val SP_ALL_QUESTIONS = "all-questions"
        const val SP_TOTAL_PROGRESS = "total-progress"

        const val API_URL_BASE = "https://valentuesday-backend-production.up.railway.app"
        const val API_URL_CHECK_ACT_KEY = "$API_URL_BASE/check-activation-key"
        const val API_URL_ACCOUNT = "$API_URL_BASE/account"
        const val API_URL_QUESTION = "$API_URL_BASE/question"
        const val API_URL_PREFERENCES = "$API_URL_BASE/preferences"
        const val API_PARAM_ACT_KEY = "activation-key"
        const val API_PARAM_AUTHORIZATION = "Authorization"
    }
}