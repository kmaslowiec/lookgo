package com.kmaslowiec.lookgo.permissions

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.kmaslowiec.lookgo.permissions.states.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
fun MultiplePermissionsState.toRuntimePermissionRequestState(
): PermissionState {
    return when {
        allPermissionsGranted -> PermissionState.AllGranted
        permissions.size != revokedPermissions.size -> PermissionState.NotAllGranted
        shouldShowRationale -> PermissionState.BothDenied
        else -> PermissionState.FirstTimeOrNeverAgain
    }
}
