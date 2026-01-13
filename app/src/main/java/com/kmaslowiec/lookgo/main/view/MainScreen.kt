package com.kmaslowiec.lookgo.main.view

import android.Manifest
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kmaslowiec.lookgo.permissions.view.CurrentLocationDisplay
import com.kmaslowiec.lookgo.location.viewmodel.LocationViewModel
import com.kmaslowiec.lookgo.main.view.uistate.StopsState.Error
import com.kmaslowiec.lookgo.main.view.uistate.StopsState.Loading
import com.kmaslowiec.lookgo.main.view.uistate.StopsState.Success
import com.kmaslowiec.lookgo.permissions.state.PermissionState
import com.kmaslowiec.lookgo.permissions.toRuntimePermissionRequestState
import com.kmaslowiec.lookgo.stops.model.Stop

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    onNavigateToLocationPermission: () -> Unit,
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    val currentLocation = locationViewModel.currentLocation.collectAsState()
    val stopsState by locationViewModel.stopsState.collectAsState()
    LaunchedEffect(locationPermissionsState.toRuntimePermissionRequestState()) { }
    when (locationPermissionsState.toRuntimePermissionRequestState()) {
        PermissionState.BothDenied, PermissionState.NeverAgain -> {
            LaunchedEffect(Unit) {
                onNavigateToLocationPermission()
            }
        }

        else -> {
            LaunchedEffect(currentLocation.value) {
                locationViewModel.getNearBusStops()
            }

            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CurrentLocationDisplay(
                    latitude = currentLocation.value.latitude,
                    longitude = currentLocation.value.longitude,
                )
                when (stopsState) {
                    is Success -> {
                        NearBusStopsList((stopsState as Success).stops)
                    }

                    is Error -> {} // TODO: Handle Stop List error
                    Loading -> CircularProgressIndicator()
                }
            }
        }
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
                    .padding(16.dp),
            )
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentLocationDisplayPreview() {
    CurrentLocationDisplay(
        latitude = 13.0,
        longitude = 666.0
    )
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
            ),
        )
    )
}
