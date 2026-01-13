package com.kmaslowiec.lookgo.permissions.uievent

sealed class LocationPermissionUIEvent {
    data object NavigateToMainScreen : LocationPermissionUIEvent()
}
