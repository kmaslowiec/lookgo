package com.kmaslowiec.lookgo.main.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kmaslowiec.lookgo.R
import com.kmaslowiec.lookgo.main.permissions.getRuntimePermissionRequestState
import com.kmaslowiec.lookgo.main.permissions.states.LocationPermissionState
import com.kmaslowiec.lookgo.main.viewmodel.MainScreenViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val isFirstTime by viewModel.isFirstTime.collectAsState(false)
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RequestPermissionButtonAndText(getRuntimePermissionRequestState(locationPermissionsState)) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
        CircleButton(
            onClick = { viewModel.firstTimeAccess() },
            isFirstTime = isFirstTime
        )
    }
}

@Composable
fun CircleButton(
    onClick: () -> Unit,
    isFirstTime: Boolean
) {
    Button(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFirstTime) Color.Green else Color.Red
        )
    ) {
        Text(
            text = if (isFirstTime) stringResource(R.string.button_first_time) else stringResource(R.string.button_second_time),
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
fun RequestPermissionButtonAndText(
    state: LocationPermissionState,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = when (state) {
                LocationPermissionState.NotAllGranted -> stringResource(R.string.not_all_permissions_granted)
                LocationPermissionState.BothDenied -> stringResource(R.string.both_permissions_denied)
                LocationPermissionState.FirstTimeAndNeverAgain -> stringResource(R.string.never_again)
                LocationPermissionState.AllGranted -> stringResource(R.string.all_permissions_granted)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onClick) {
            Text(
                stringResource(
                    if (state == LocationPermissionState.NotAllGranted) {
                        R.string.button_allow_precise_location
                    } else {
                        R.string.button_request_permissions
                    }
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "Permission first time and never again"
)
@Composable
fun RequestPermissionButtonAndTextFirstTimeAndNeverAgainPreview() {
    RequestPermissionButtonAndText(LocationPermissionState.FirstTimeAndNeverAgain, {})
}

@Preview(
    showBackground = true,
    name = "Permission not all granted"
)
@Composable
fun RequestPermissionButtonAndTextNotAllGrantedPreview() {
    RequestPermissionButtonAndText(LocationPermissionState.NotAllGranted, {})
}

@Preview(
    showBackground = true,
    name = "First Time Access"
)
@Composable
fun CircleButtonFirstTimePreview() {
    CircleButton(
        isFirstTime = true,
        onClick = {}
    )
}

@Preview(
    showBackground = true,
    name = "Second Time Access"
)
@Composable
fun CircleButtonSecondTimePreview() {
    CircleButton(
        isFirstTime = false,
        onClick = {}
    )
}
