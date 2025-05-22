package com.kmaslowiec.lookgo.location.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kmaslowiec.lookgo.R
import com.kmaslowiec.lookgo.common.view.SimpleOkDialog
import com.kmaslowiec.lookgo.permissions.states.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationRuntimePermission(
    isDialogVisible: Boolean,
    locationPermissionsState: PermissionState,
    isFirstTime: Boolean,
    firstAndBothDeniedAction: () -> Unit
) {
    if (locationPermissionsState == PermissionState.FirstTimeOrNeverAgain) {
        if (isDialogVisible && isFirstTime) {
            SimpleOkDialog(
                title = stringResource(R.string.location_permission_first_attempt_title),
                content = stringResource(
                    R.string.location_permission_first_attempt_content
                ),
                onDismiss = firstAndBothDeniedAction
            )
        }
    } else if (locationPermissionsState == PermissionState.BothDenied) {
        SimpleOkDialog(
            title = stringResource(R.string.location_permission_second_attempt_title),
            content =
                stringResource(R.string.location_permission_second_attempt_content),
            onDismiss = firstAndBothDeniedAction
        )
    }
}
