package com.kmaslowiec.lookgo.permissions.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.permissions.uievent.LocationPermissionUIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationPermissionViewModel : ViewModel() {

    private val _uiEvent = MutableSharedFlow<LocationPermissionUIEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiEvent: SharedFlow<LocationPermissionUIEvent> = _uiEvent

    private val _dialogState = MutableStateFlow(false)
    val dialogState: StateFlow<Boolean> = _dialogState

    fun onPermissionAllGranted() {
        handleUIEvent(LocationPermissionUIEvent.NavigateToMainScreen)
    }

    private fun handleUIEvent(uiEvent: LocationPermissionUIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }

    fun showDialog() {
        _dialogState.value = true
    }

    fun dismissDialog() {
        _dialogState.value = false
    }
}
