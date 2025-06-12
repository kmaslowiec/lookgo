package com.kmaslowiec.lookgo.main.view

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.location.view.LocationDisplay
import com.kmaslowiec.lookgo.location.view.NoLocationDisplay
import com.kmaslowiec.lookgo.location.viewmodel.LocationViewModel
import com.kmaslowiec.lookgo.main.viewmodel.MainScreenViewModel
import com.kmaslowiec.lookgo.permissions.states.PermissionState
import com.kmaslowiec.lookgo.permissions.toRuntimePermissionRequestState
import com.kmaslowiec.lookgo.stops.model.Stop

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    val isFirstTime by mainScreenViewModel.isFirstTime.collectAsState(false)
    val locationCoordinates by locationViewModel.currentLocation.collectAsState()
    val nearBusStops by locationViewModel.nearBusStops.collectAsState()
    locationViewModel.getNearBusStops()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HandleLocationPermissions(
            locationPermissionsStateResult = locationPermissionsState.toRuntimePermissionRequestState(
                isFirstTime
            ),
            allGrantedAction = {
                LaunchAndDisplayCurrentLocation(
                    location = locationCoordinates,
                    locationViewModel = locationViewModel
                )
                ShowNamesOfNearBusStops(nearBusStops)
            },
            neverAgainAction = {
                NoLocationDisplay {
                    context.goToApplicationSettings()
                }
            },
            firstTimeAction = {
                SimpleOkDialog(
                    title = stringResource(R.string.location_permission_first_attempt_title),
                    content = stringResource(
                        R.string.location_permission_first_attempt_content
                    ),
                    onDismiss = {
                        locationPermissionsState.launchMultiplePermissionRequest()
                        mainScreenViewModel.firstTimeAccess()
                    }
                )
            },
            bothDeniedAction = {
                SimpleOkDialog(
                    title = stringResource(R.string.location_permission_second_attempt_title),
                    content =
                        stringResource(R.string.location_permission_second_attempt_content),
                    onDismiss = { locationPermissionsState.launchMultiplePermissionRequest() }
                )
            },
        )
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
    allGrantedAction: @Composable () -> Unit,
    neverAgainAction: @Composable () -> Unit,
    firstTimeAction: @Composable () -> Unit,
    bothDeniedAction: @Composable () -> Unit,
) {
    when (locationPermissionsStateResult) {
        PermissionState.AllGranted -> {
            allGrantedAction()
        }

        PermissionState.NeverAgain -> {
            neverAgainAction()
        }

        PermissionState.FirstTime -> {
            firstTimeAction()
        }

        PermissionState.BothDenied -> {
            bothDeniedAction()
        }

        PermissionState.NotAllGranted -> TODO()
    }
}

@Composable
fun LaunchAndDisplayCurrentLocation(
    location: LocationCoordinates,
    locationViewModel: LocationViewModel
) {
    LaunchedEffect(Unit) {
        locationViewModel.startLocationUpdates()
    }
    LocationDisplay(
        latitude = location.latitude,
        longitude = location.longitude
    )
}

@Composable
fun ShowNamesOfNearBusStops(stops: List<Stop>) {
    LazyColumn {
        items(stops) { stop ->
            Text(
                text = stop.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            HorizontalDivider()
        }
    }
}
