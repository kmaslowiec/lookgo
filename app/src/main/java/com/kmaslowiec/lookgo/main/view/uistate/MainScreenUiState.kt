package com.kmaslowiec.lookgo.main.view.uistate

import com.kmaslowiec.lookgo.common.domain.LookgoError
import com.kmaslowiec.lookgo.stops.model.Stop

sealed class MainScreenUiState {
    data class Success(val stops: List<Stop>) : MainScreenUiState()
    data object Loading : MainScreenUiState()
    data class Exception(val exception: LookgoError) : MainScreenUiState()
}
