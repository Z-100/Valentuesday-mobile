package com.z100.valentuesday.service

import android.content.SharedPreferences
import com.z100.valentuesday.util.Const.Factory.SP_ACTIVATION_KEY

class DataManagerService(private val sp: SharedPreferences) {

    /**
     * Removes the activation key from the shared preferences
     * @return success of operation
     */
    fun clearLoginSharedPreferences(): Boolean {

        sp.edit().apply {
            remove(SP_ACTIVATION_KEY)
        }.apply()

        return sp.getString(SP_ACTIVATION_KEY, null) == null;
    }

    fun addLoginSharedPreferences(activationKey: String): Boolean {

        sp.edit().apply {
            putString(SP_ACTIVATION_KEY, activationKey)
        }.apply()

        return sp.getString(SP_ACTIVATION_KEY, null) != null;
    }
}