package com.kmaslowiec.lookgo.common.domain

sealed interface LookgoResult<out T, out E> {
    data class Success<T>(val value: T) : LookgoResult<T, Nothing>
    data object NotModified : LookgoResult<Nothing, Nothing>
    data class Error<E>(val exception: E) : LookgoResult<Nothing, E>
}
