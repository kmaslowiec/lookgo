package com.kmaslowiec.lookgo.main.view

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kmaslowiec.lookgo.common.utils.goToApplicationSettings
import com.kmaslowiec.lookgo.location.view.LocationDisplay
import com.kmaslowiec.lookgo.location.view.NoLocationDisplay
import com.kmaslowiec.lookgo.location.view.RequestLocationRuntimePermission
import com.kmaslowiec.lookgo.location.viewmodel.LocationViewModel
import com.kmaslowiec.lookgo.main.viewmodel.MainScreenViewModel
import com.kmaslowiec.lookgo.permissions.states.PermissionState
import com.kmaslowiec.lookgo.permissions.toRuntimePermissionRequestState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    viewModel: MainScreenViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val isFirstTime by viewModel.isFirstTime.collectAsState(false)
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    val location = locationViewModel.locationState.observeAsState()
    var isDialogVisible by remember { mutableStateOf(true) }
    val locationPermissionsStateResult = locationPermissionsState.toRuntimePermissionRequestState()
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HandleLocationPermissions(
            locationPermissionsStateResult = locationPermissionsStateResult,
            isFirstTime = isFirstTime,
            location = location.value,
            locationViewModel = locationViewModel,
            context = context,
            isDialogVisible = isDialogVisible
        ) {
            locationPermissionsState.launchMultiplePermissionRequest()
            viewModel.firstTimeAccess()
        }
        DisposableEffect(Unit) {
            onDispose {
                locationViewModel.stopLocationUpdates()
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HandleLocationPermissions(
    locationPermissionsStateResult: PermissionState,
    isFirstTime: Boolean,
    location: Pair<Double, Double>?,
    locationViewModel: LocationViewModel,
    context: Context,
    isDialogVisible: Boolean,
    firstAndBothDeniedAction: () -> Unit
) {
    when {
        locationPermissionsStateResult == PermissionState.AllGranted -> {
            RunAndShowLocation(
                location = location,
                locationViewModel = locationViewModel
            )
        }

        locationPermissionsStateResult == PermissionState.FirstTimeOrNeverAgain && !isFirstTime -> {
            NoLocationDisplay {
                context.goToApplicationSettings()
            }
        }

        else -> {
            RequestLocationRuntimePermission(
                isDialogVisible = isDialogVisible,
                locationPermissionsState = locationPermissionsStateResult,
                isFirstTime = isFirstTime,
                firstAndBothDeniedAction = {
                    firstAndBothDeniedAction()
                }
            )
        }
    }
}

@Composable
fun RunAndShowLocation(
    location: Pair<Double?, Double?>?,
    locationViewModel: LocationViewModel
) {
    LaunchedEffect(Unit) {
        locationViewModel.startLocationUpdates()
    }
    LocationDisplay(
        latitude = location?.first ?: 0.0,
        longitude = location?.second ?: 0.0
    )
}
