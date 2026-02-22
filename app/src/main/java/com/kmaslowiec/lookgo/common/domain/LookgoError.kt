package com.kmaslowiec.lookgo.common.domain

sealed class LookgoError {
    object EmptyList : LookgoError()
    object Network : LookgoError()
    data class Server(val code: Int) : LookgoError()
    data class Unknown(val cause: Throwable?) : LookgoError()
}
