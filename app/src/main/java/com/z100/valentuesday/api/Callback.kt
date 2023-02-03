package com.z100.valentuesday.api

import com.android.volley.VolleyError

fun interface Callback<T> {
    fun handle(success: T?, error: VolleyError?)
}
