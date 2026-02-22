package com.kmaslowiec.lookgo.main.view.uievent

sealed class MainUIEvent {
    data object NavigateToLocationPermission : MainUIEvent()
}
