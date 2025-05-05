package com.kmaslowiec.lookgo.main.permissions

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.kmaslowiec.lookgo.main.permissions.states.LocationPermissionState

@OptIn(ExperimentalPermissionsApi::class)
internal fun getRuntimePermissionRequestState(
    locationPermissionsState: MultiplePermissionsState,
): LocationPermissionState = when {
    locationPermissionsState.allPermissionsGranted -> LocationPermissionState.AllGranted
    locationPermissionsState.permissions.size != locationPermissionsState.revokedPermissions.size -> LocationPermissionState.NotAllGranted
    locationPermissionsState.shouldShowRationale -> LocationPermissionState.BothDenied
    else -> LocationPermissionState.FirstTimeAndNeverAgain
}
