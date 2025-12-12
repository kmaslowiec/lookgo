package com.kmaslowiec.lookgo.welcome.viemodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.welcome.uievent.WelcomePagerUIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class WelcomePagerViewModel : ViewModel() {

    private val _uiEvent = MutableSharedFlow<WelcomePagerUIEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiEvent: SharedFlow<WelcomePagerUIEvent> = _uiEvent

    fun onPermissionActionClick() {
        handleUIEvent(WelcomePagerUIEvent.NavigateToLocationPermissionScreen)
    }

    private fun handleUIEvent(uiEvent: WelcomePagerUIEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }
}
