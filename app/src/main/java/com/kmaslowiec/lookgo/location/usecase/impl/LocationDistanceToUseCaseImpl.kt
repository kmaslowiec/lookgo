package com.kmaslowiec.lookgo.location.usecase.impl

import com.kmaslowiec.lookgo.location.data.LocationDistanceToClient
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.location.usecase.LocationDistanceToUseCase
import javax.inject.Inject

class LocationDistanceToUseCaseImpl @Inject constructor(
    private val locationDistanceToClient: LocationDistanceToClient
) : LocationDistanceToUseCase {

    override fun currentLocationDistanceTo(currentLocation: LocationCoordinates, latitude: Double, longitude: Double): Float =
        locationDistanceToClient.currentLocationDistanceTo(
            currentLocation = currentLocation,
            latitude = latitude,
            longitude = longitude
        )
}
