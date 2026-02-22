package com.kmaslowiec.lookgo.location.data.impl

import android.location.Location
import com.kmaslowiec.lookgo.location.data.LocationDistanceCalculatorClient
import com.kmaslowiec.lookgo.location.model.LocationCoordinates

class LocationDistanceToClientImpl() : LocationDistanceCalculatorClient {

    override fun currentLocationDistanceTo(
        currentLocation: LocationCoordinates,
        latitude: Double,
        longitude: Double
    ): Float = Location("").apply {
        this.latitude = currentLocation.latitude
        this.longitude = currentLocation.longitude
    }.distanceTo(Location("").apply {
        this.latitude = latitude
        this.longitude = longitude
    })
}
