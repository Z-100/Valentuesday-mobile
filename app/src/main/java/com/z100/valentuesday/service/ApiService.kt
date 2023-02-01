package com.z100.valentuesday.service

class ApiService {

    fun checkActivationKey(key: String): Boolean {

        return key == "valid"
    }
}