package com.kmaslowiec.lookgo.common.domain

fun Throwable.toStopsError(): LookgoError = when (this) {
    is java.io.IOException -> LookgoError.Network
    is retrofit2.HttpException -> LookgoError.Server(code())
    else -> LookgoError.Unknown(cause)
}