package com.kmaslowiec.lookgo.main.permissions

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.kmaslowiec.lookgo.main.permissions.states.LocationPermissionState

@OptIn(ExperimentalPermissionsApi::class)
fun MultiplePermissionsState.toRuntimePermissionRequestState(
): LocationPermissionState = when {
    allPermissionsGranted -> LocationPermissionState.AllGranted
    permissions.size != revokedPermissions.size -> LocationPermissionState.NotAllGranted
    shouldShowRationale -> LocationPermissionState.BothDenied
    else -> LocationPermissionState.FirstTimeAndNeverAgain
}
