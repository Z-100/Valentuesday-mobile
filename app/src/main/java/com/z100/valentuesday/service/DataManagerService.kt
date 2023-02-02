package com.z100.valentuesday.service

import android.content.SharedPreferences
import com.z100.valentuesday.rename.Const
import com.z100.valentuesday.rename.Const.Factory.spActivationKey

class DataManagerService(private val sp: SharedPreferences) {

    /**
     * Removes the activation key from the shared preferences
     * @return success of operation
     */
    fun clearLoginSharedPreferences(): Boolean {

        sp.edit().apply {
            remove(spActivationKey)
        }.apply()

        return sp.getString(spActivationKey, null) == null;
    }

    fun addLoginSharedPreferences(activationKey: String): Boolean {

        sp.edit().apply {
            putString(spActivationKey, activationKey)
        }.apply()

        return sp.getString(spActivationKey, null) != null;
    }
}