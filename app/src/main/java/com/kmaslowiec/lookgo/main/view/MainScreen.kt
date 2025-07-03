package com.kmaslowiec.lookgo.main.view

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kmaslowiec.lookgo.R
import com.kmaslowiec.lookgo.common.utils.goToApplicationSettings
import com.kmaslowiec.lookgo.common.view.SimpleOkDialog
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.location.view.CurrentLocationDisplay
import com.kmaslowiec.lookgo.location.view.NoLocationDisplay
import com.kmaslowiec.lookgo.location.viewmodel.LocationViewModel
import com.kmaslowiec.lookgo.main.view.uistate.StopsState
import com.kmaslowiec.lookgo.main.view.uistate.StopsState.Error
import com.kmaslowiec.lookgo.main.view.uistate.StopsState.Loading
import com.kmaslowiec.lookgo.main.view.uistate.StopsState.Success
import com.kmaslowiec.lookgo.main.viewmodel.MainScreenViewModel
import com.kmaslowiec.lookgo.permissions.state.PermissionState
import com.kmaslowiec.lookgo.permissions.toRuntimePermissionRequestState
import com.kmaslowiec.lookgo.preferences.state.PreferencesState
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
    val isFirstTimeState by mainScreenViewModel.preferencesState.collectAsState()
    val currentLocation by locationViewModel.currentLocation.collectAsState()
    val stopsState = locationViewModel.stopsState.collectAsState()
    var isFirstTimeDialogVisible = remember { mutableStateOf(true) }
    var isRationaleDialogVisible = remember { mutableStateOf(true) }

    if (isFirstTimeState is PreferencesState.Loading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HandleLocationPermissions(
                context = context,
                locationPermissionsStateResult = locationPermissionsState.toRuntimePermissionRequestState(
                    (isFirstTimeState as PreferencesState.Success).isFirstTime
                ),
                locationViewModel = locationViewModel,
                mainScreenViewModel = mainScreenViewModel,
                currentLocation = currentLocation,
                isFirstTimeDialogVisible = isFirstTimeDialogVisible.value,
                isRationaleDialogVisible = isRationaleDialogVisible.value,
                stopsState = stopsState.value,
                rationaleDialogAction = {
                    locationPermissionsState.launchMultiplePermissionRequest()
                    isRationaleDialogVisible.value = false
                },
                firstTimeDialogAction = {
                    locationPermissionsState.launchMultiplePermissionRequest()
                    isFirstTimeDialogVisible.value = false
                }
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HandleLocationPermissions(
    context: Context,
    locationPermissionsStateResult: PermissionState,
    currentLocation: LocationCoordinates,
    locationViewModel: LocationViewModel,
    mainScreenViewModel: MainScreenViewModel,
    isFirstTimeDialogVisible: Boolean,
    isRationaleDialogVisible: Boolean,
    stopsState: StopsState<List<Stop>>,
    rationaleDialogAction: () -> Unit,
    firstTimeDialogAction: () -> Unit,
) {
    when (locationPermissionsStateResult) {
        PermissionState.AllGranted -> {
            locationViewModel.startLocationUpdates()
            locationViewModel.getNearBusStops()
            CurrentLocationDisplay(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude
            )
            when (stopsState) {
                is Success<List<Stop>> -> {
                    NearBusStopsList(stopsState.data)
                }

                is Error -> {}
                Loading -> CircularProgressIndicator()
            }

        }

        PermissionState.NeverAgain -> {
            NoLocationDisplay {
                context.goToApplicationSettings()
            }
        }

        PermissionState.FirstTime -> {
            if (isFirstTimeDialogVisible) {
                SimpleOkDialog(
                    title = stringResource(R.string.location_permission_first_attempt_title),
                    content = stringResource(
                        R.string.location_permission_first_attempt_content
                    ),
                    onDismiss = firstTimeDialogAction
                )
            }
        }

        PermissionState.BothDenied -> {
            if (isRationaleDialogVisible) {
                mainScreenViewModel.firstTimeAccess()
                SimpleOkDialog(
                    title = stringResource(R.string.location_permission_second_attempt_title),
                    content =
                        stringResource(R.string.location_permission_second_attempt_content),
                    onDismiss = rationaleDialogAction
                )
            }

        }

        PermissionState.NotAllGranted -> TODO()
    }
}

@Composable
fun NearBusStopsList(stops: List<Stop>) {
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

@Preview(showBackground = true)
@Composable
fun NearBusStopsListPreview() {
    NearBusStopsList(
        stops = listOf(
            Stop(
                id = 1,
                latitude = 52.2297,
                longitude = 21.0122,
                name = "Centrum",
                number = "01",
                parkAndRide = false,
                railwayStationName = "Warszawa Centralna",
                requestStop = false,
                updatedAt = "2023-10-27T10:00:00Z"
            ),
            Stop(
                id = 2,
                latitude = 52.2319,
                longitude = 21.0067,
                name = "Metro Świętokrzyska",
                number = "02",
                parkAndRide = false,
                railwayStationName = "",
                requestStop = false,
                updatedAt = "2023-10-27T10:05:00Z"
            )
        )
    )
}
