package com.kmaslowiec.lookgo.permissions.view

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kmaslowiec.lookgo.R
import com.kmaslowiec.lookgo.common.utils.goToApplicationSettings
import com.kmaslowiec.lookgo.common.view.SimpleOkDialog
import com.kmaslowiec.lookgo.main.viewmodel.PreferencesViewModel
import com.kmaslowiec.lookgo.permissions.state.PermissionState
import com.kmaslowiec.lookgo.permissions.toRuntimePermissionRequestState
import com.kmaslowiec.lookgo.preferences.state.PreferencesState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    modifier: Modifier,
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit,
) {
    val context = LocalContext.current
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    val isFirstTimeState by preferencesViewModel.preferencesState.collectAsState()
    val isFirstTimeDialogVisible = remember { mutableStateOf(true) }
    val isRationaleDialogVisible = remember { mutableStateOf(true) }
    when (isFirstTimeState) {
        is PreferencesState.Loading -> {
            CircularProgressIndicator()
        }

        is PreferencesState.Success -> {
            when (locationPermissionsState.toRuntimePermissionRequestState(
                isFirstTime = (isFirstTimeState as PreferencesState.Success).isFirstTime
            )
            ) {
                PermissionState.AllGranted -> {
                    onNavigateToMain()
                }

                PermissionState.NeverAgain -> {
                    NeverAgainView(
                        modifier = modifier,
                        context = context
                    )
                }

                PermissionState.FirstTime -> {
                    FirstTimeView(
                        isFirstTimeDialogVisible = isFirstTimeDialogVisible.value,
                        onDismiss = {
                            locationPermissionsState.launchMultiplePermissionRequest()
                            isFirstTimeDialogVisible.value = false
                        }
                    )
                }

                PermissionState.BothDenied -> {
                    BothDeniedView(
                        isRationaleDialogVisible = isRationaleDialogVisible.value,
                        preferencesViewModel = preferencesViewModel,
                        onDismiss = {
                            locationPermissionsState.launchMultiplePermissionRequest()
                            isRationaleDialogVisible.value = false
                        }
                    )
                }

                PermissionState.NotAllGranted -> TODO()
            }
        }
    }
}

@Composable
private fun NeverAgainView(
    modifier: Modifier,
    context: Context
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Permissions Declined")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                context.goToApplicationSettings()
            }
            ) {
                Text(text = "Go to Settings")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FirstTimeView(
    isFirstTimeDialogVisible: Boolean,
    onDismiss: () -> Unit
) {
    if (isFirstTimeDialogVisible) {
        SimpleOkDialog(
            title = stringResource(R.string.location_permission_first_attempt_title),
            content = stringResource(
                R.string.location_permission_first_attempt_content
            ),
            onDismiss = onDismiss
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun BothDeniedView(
    isRationaleDialogVisible: Boolean,
    preferencesViewModel: PreferencesViewModel,
    onDismiss: () -> Unit
) {
    if (isRationaleDialogVisible) {
        preferencesViewModel.firstTimeAccess()
        SimpleOkDialog(
            title = stringResource(R.string.location_permission_second_attempt_title),
            content = stringResource(R.string.location_permission_second_attempt_content),
            onDismiss = onDismiss
        )
    }
}
