package com.kmaslowiec.lookgo.location.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.location.LocationUpdatesManager
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.main.view.uistate.StopsState
import com.kmaslowiec.lookgo.stops.model.Stop
import com.kmaslowiec.lookgo.stops.repository.StopsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application,
    private val repo: StopsRepository
) : AndroidViewModel(application) {

    private val _currentLocation =
        MutableStateFlow<LocationCoordinates>(LocationCoordinates(0.0, 0.0))
    val currentLocation: StateFlow<LocationCoordinates> = _currentLocation
    private val _stopsState = MutableStateFlow<StopsState<List<Stop>>>(StopsState.Loading)
    val stopsState: StateFlow<StopsState<List<Stop>>> = _stopsState

    private val locationManager: LocationUpdatesManager = LocationUpdatesManager(application) { location ->
        _currentLocation.value = LocationCoordinates(location.latitude, location.longitude)
    }

    fun getNearBusStops() {
        viewModelScope.launch {
            repo.getStops()
                .catch { exception -> _stopsState.value = StopsState.Error(exception) }
                .collect { stopsList ->
                    val nearbyStops = stopsList.data.filter { stop ->
                        calculateRange(
                            _currentLocation.value,
                            LocationCoordinates(stop.latitude, stop.longitude)
                        ) <= 500.0
                    }
                    _stopsState.value = StopsState.Success(nearbyStops)
                }
        }
    }

    private fun calculateRange(
        currentLocation: LocationCoordinates,
        busStopLocation: LocationCoordinates
    ): Double {
        val latitudeDistance = Math.toRadians(busStopLocation.latitude - currentLocation.latitude)
        val longitudeDistance =
            Math.toRadians(busStopLocation.longitude - currentLocation.longitude)
        val sinLat = sin(latitudeDistance / 2)
        val sinLon = sin(longitudeDistance / 2)

        val centralAngleComponent = sinLat * sinLat +
                cos(Math.toRadians(currentLocation.latitude)) *
                cos(Math.toRadians(busStopLocation.latitude)) *
                sinLon * sinLon

        val angularDistance =
            2 * atan2(sqrt(centralAngleComponent), sqrt(1 - centralAngleComponent))

        return 6371000.0 * angularDistance
    }

    override fun onCleared() {
        stopLocationUpdates()
    }

    fun startLocationUpdates() {
        locationManager.startLocationUpdates()
    }

    fun stopLocationUpdates() {
        locationManager.stopLocationUpdates()
    }
}
