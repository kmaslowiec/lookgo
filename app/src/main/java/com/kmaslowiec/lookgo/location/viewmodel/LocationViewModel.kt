package com.kmaslowiec.lookgo.location.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kmaslowiec.lookgo.location.LocationUpdatesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val _locationState = MutableLiveData<Pair<Double, Double>>()
    val locationState: LiveData<Pair<Double, Double>> = _locationState

    private val locationManager: LocationUpdatesManager = LocationUpdatesManager(application) { location ->
        _locationState.postValue(Pair(location.latitude, location.longitude))
    }

    @Suppress("MissingPermission")
    fun startLocationUpdates() {
        locationManager.startLocationUpdates()
    }

    fun stopLocationUpdates() {
        locationManager.stopLocationUpdates()
    }
}
