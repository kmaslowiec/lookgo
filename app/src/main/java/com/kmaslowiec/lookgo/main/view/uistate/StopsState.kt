package com.kmaslowiec.lookgo.main.view.uistate

sealed class StopsState<out T> {
    data class Success<T>(val data: T) : StopsState<T>()
    data class Error(val exception: Throwable) : StopsState<Nothing>()
    data object Loading : StopsState<Nothing>()
}
