package com.kmaslowiec.lookgo.location.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.location.LocationUpdatesManager
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.main.domain.FindNearBusStop
import com.kmaslowiec.lookgo.stops.model.Stop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application, private val findNearBusStop: FindNearBusStop
) : AndroidViewModel(application) {

    private val _currentLocation =
        MutableStateFlow<LocationCoordinates>(LocationCoordinates(0.0, 0.0))
    val currentLocation: StateFlow<LocationCoordinates> = _currentLocation
    private val _nearBusStops = MutableStateFlow<List<Stop>>(emptyList<Stop>())
    val nearBusStops: StateFlow<List<Stop>> = _nearBusStops
    private val locationManager: LocationUpdatesManager = LocationUpdatesManager(application) { location ->
        _currentLocation.value = LocationCoordinates(location.latitude, location.longitude)
    }

    fun getNearBusStops() {
        viewModelScope.launch {
            _nearBusStops.value = findNearBusStop(
                _currentLocation.value
            )
        }
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
