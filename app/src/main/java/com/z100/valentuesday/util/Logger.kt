package com.z100.valentuesday.util

class Logger {
    companion object Factory {
        fun log(msg: String, caller: Class<*>) {
            println("@${caller.simpleName}: $msg")
        }
    }
}