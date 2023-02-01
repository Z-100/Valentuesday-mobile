package com.z100.valentuesday.rename.type

enum class Status(statusCode: StatusCode) {

    SUCCESS(StatusCode.Idk),
    ERROR(StatusCode.Idk),
    INFO(StatusCode.Idk);

    enum class StatusCode {
        Idk
    }
}