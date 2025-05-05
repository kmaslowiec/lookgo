package com.kmaslowiec.lookgo.main.permissions.states

sealed class LocationPermissionState {
    data object AllGranted : LocationPermissionState()
    data object NotAllGranted : LocationPermissionState()
    data object BothDenied : LocationPermissionState()
    data object FirstTimeAndNeverAgain : LocationPermissionState()
}