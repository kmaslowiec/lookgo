package com.kmaslowiec.lookgo.welcome.uievent

sealed class WelcomePagerUIEvent {
    data object NavigateToLocationPermissionScreen : WelcomePagerUIEvent()
}
