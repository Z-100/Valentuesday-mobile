package com.z100.valentuesday.api

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson

class GsonRequest<Clazz: Any>(
    method: Int,
    url: String,
    clazz: Class<Clazz>,
    listener: Response.Listener<Clazz>,
    errorListener: Response.ErrorListener
) : Request<Clazz>(method, url, errorListener) {

    private val mLock = Any()

    private val mClazz: Class<Clazz> = clazz

    private var mListener: Response.Listener<Clazz>? = listener

    private var mParams: MutableMap<String, String>? = null

    private var mBody: String? = null

    override fun parseNetworkResponse(response: NetworkResponse?): Response<Clazz> {

        val parsed: Clazz? = try {
            Gson().fromJson(response!!.data.toString(), mClazz)
        } catch (e: Exception) {
            throw RuntimeException("Could not parse object: " + (response?.data ?: "[data null]"), e)
        }

        return Response.success(parsed, null)
    }

    override fun deliverResponse(response: Clazz) {
        var kListener: Response.Listener<Clazz>?
        synchronized(mLock) {
            kListener = mListener
        }
        if (kListener != null) {
            kListener!!.onResponse(response)
        }
    }

    override fun cancel() {
        super.cancel()
        synchronized(mLock) {
            mListener = null
        }
    }

    fun withParam(name: String, value: String): GsonRequest<Clazz> {
        if (mParams == null)
            mParams = HashMap()
        mParams!![name] = value
        return this
    }

    override fun getParams(): MutableMap<String, String>? {
        return mParams?:super.getParams()
    }

    fun withBody(body: Any): GsonRequest<Clazz> {
        mBody = Gson().toJson(body)
        return this
    }

    override fun getBody(): ByteArray {
        return mBody?.toByteArray() ?: super.getBody()
    }
}
