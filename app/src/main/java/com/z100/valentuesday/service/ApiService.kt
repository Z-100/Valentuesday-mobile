package com.z100.valentuesday.service

class ApiService {

    fun checkActivationKey(key: String): String? {

        val keyFromDb = "valid"

        return if (key == keyFromDb) keyFromDb else null
    }
}