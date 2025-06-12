package com.kmaslowiec.lookgo.permissions.states

sealed interface PermissionState {
    data object AllGranted : PermissionState
    data object NotAllGranted : PermissionState
    data object BothDenied : PermissionState
    data object FirstTime : PermissionState
    data object NeverAgain : PermissionState
}
