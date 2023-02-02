package com.z100.valentuesday.util

class Const {
    companion object Factory {
        const val SP_NAME = "valentuesday"
        const val SP_ACTIVATION_KEY = "act-key"

        const val API_URL_BASE = "https://localhost:8080/api/v1" //TODO(Change)
        const val API_URL_ACCOUNT = "$API_URL_BASE/account"
        const val API_URL_QUESTION = "$API_URL_BASE/question"
        const val API_URL_PREFERENCES = "$API_URL_BASE/preferences"
        const val API_PARAM_ACT_KEY = "activation-key"
    }
}