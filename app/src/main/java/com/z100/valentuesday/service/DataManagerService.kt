package com.z100.valentuesday.service

import android.content.SharedPreferences
import com.z100.valentuesday.util.Const.Factory.SP_ACTIVATION_KEY

class DataManagerService(private val sp: SharedPreferences) {

    fun clearActivationKey(): Boolean {

        sp.edit().apply {
            remove(SP_ACTIVATION_KEY)
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
}