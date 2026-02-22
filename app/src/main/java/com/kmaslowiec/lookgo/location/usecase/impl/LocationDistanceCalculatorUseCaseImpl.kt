package com.kmaslowiec.lookgo.location.usecase.impl

import com.kmaslowiec.lookgo.location.data.LocationDistanceCalculatorClient
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.location.usecase.LocationDistanceCalculatorUseCase
import javax.inject.Inject

class LocationDistanceCalculatorUseCaseImpl @Inject constructor(
    private val locationDistanceCalculatorClient: LocationDistanceCalculatorClient
) : LocationDistanceCalculatorUseCase {

    override fun currentLocationDistanceTo(currentLocation: LocationCoordinates, latitude: Double, longitude: Double): Float =
        locationDistanceCalculatorClient.currentLocationDistanceTo(
            currentLocation = currentLocation,
            latitude = latitude,
            longitude = longitude
        )
}
