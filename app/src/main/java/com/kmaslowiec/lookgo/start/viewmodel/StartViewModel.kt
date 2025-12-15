package com.kmaslowiec.lookgo.start.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.start.uievent.StartUIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class StartViewModel : ViewModel() {

    private val _uiEvent = MutableSharedFlow<StartUIEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiEvent: SharedFlow<StartUIEvent> = _uiEvent

    fun onPermissionAllGranted() {
        handleUIEvent(StartUIEvent.NavigateToMainScreen)
    }

    fun onFirstTime() {
        handleUIEvent(StartUIEvent.NavigateToNavigateToWelcome)
    }

    fun onOtherPermissions() {
        handleUIEvent(StartUIEvent.NavigateToNavigateToLocationPermission)
    }

    private fun handleUIEvent(uiEvent: StartUIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }
}