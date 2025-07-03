package com.kmaslowiec.lookgo.location.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.location.LocationUpdatesManager
import com.kmaslowiec.lookgo.main.view.uistate.StopsState
import com.kmaslowiec.lookgo.stops.model.Stop
import com.kmaslowiec.lookgo.stops.repository.StopsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application,
    private val repo: StopsRepository
) : AndroidViewModel(application) {

    private val _currentLocation =
        MutableStateFlow<Location>(Location(""))
    val currentLocation: StateFlow<Location> = _currentLocation
    private val _stopsState = MutableStateFlow<StopsState<List<Stop>>>(StopsState.Loading)
    val stopsState: StateFlow<StopsState<List<Stop>>> = _stopsState

    private val locationManager: LocationUpdatesManager = LocationUpdatesManager(application) { location ->
        _currentLocation.value = Location("GPS").apply {
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    fun getNearBusStops() {
        viewModelScope.launch {
            repo.getStops()
                .catch { exception -> _stopsState.value = StopsState.Error(exception) }
                .collect { stopsList ->
                    val nearbyStops = stopsList.data.filter { stop ->
                        _currentLocation.value.distanceTo(Location("ZDiTM").apply {
                            latitude = stop.latitude
                            longitude = stop.longitude
                        }) <= 500.0
                    }
                    _stopsState.value = StopsState.Success(nearbyStops)
                }
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
