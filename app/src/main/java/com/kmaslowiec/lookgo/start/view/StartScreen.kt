package com.kmaslowiec.lookgo.start.view

import android.Manifest
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kmaslowiec.lookgo.main.viewmodel.PreferencesViewModel
import com.kmaslowiec.lookgo.permissions.state.PermissionState
import com.kmaslowiec.lookgo.permissions.toRuntimePermissionRequestState
import com.kmaslowiec.lookgo.preferences.state.PreferencesState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StartScreen(
    onNavigateToWelcome: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToLocationPermission: () -> Unit,
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
) {
    val isFirstTimeState by preferencesViewModel.preferencesState.collectAsState()
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    if (isFirstTimeState is PreferencesState.Loading) {
        CircularProgressIndicator()
    } else {
        when (locationPermissionsState.toRuntimePermissionRequestState((isFirstTimeState as PreferencesState.Success).isFirstTime)) {
            PermissionState.AllGranted -> {
                LaunchedEffect(Unit) {
                    onNavigateToMain()
                }

            }

            PermissionState.FirstTime -> {
                LaunchedEffect(Unit) {
                    onNavigateToWelcome()
                }
            }

            else -> {
                LaunchedEffect(Unit) {
                    onNavigateToLocationPermission()
                }
            }
        }
    }
}
