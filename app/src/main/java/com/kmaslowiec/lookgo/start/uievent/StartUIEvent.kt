package com.kmaslowiec.lookgo.start.uievent

sealed class StartUIEvent {
    data object NavigateToMainScreen : StartUIEvent()
    data object NavigateToNavigateToWelcome : StartUIEvent()
    data object NavigateToNavigateToLocationPermission : StartUIEvent()
}
