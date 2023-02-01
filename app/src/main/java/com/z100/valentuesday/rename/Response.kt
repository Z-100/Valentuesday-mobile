package com.z100.valentuesday.rename

import com.z100.valentuesday.rename.type.Status
import java.util.*

data class Response(var head: Any, var body: Any) {

    private var status: Status = Status.SUCCESS;

    fun isValid(): Boolean {
        return status == Status.SUCCESS
    }

    fun isInvalid(): Boolean {
        return !isValid()
    }
}
