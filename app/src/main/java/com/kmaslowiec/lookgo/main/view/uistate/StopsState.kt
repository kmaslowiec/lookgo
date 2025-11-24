package com.kmaslowiec.lookgo.main.view.uistate

import com.kmaslowiec.lookgo.stops.model.Stop


sealed class StopsState {
    data class Success(val stops: List<Stop>) : StopsState()
    data class Error(val exception: Throwable) : StopsState()
    data object Loading : StopsState()
}
