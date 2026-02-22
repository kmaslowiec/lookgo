package com.kmaslowiec.lookgo.location.usecase

import com.kmaslowiec.lookgo.location.model.LocationCoordinates

interface LocationDistanceToUseCase {
    fun currentLocationDistanceTo(
        currentLocation: LocationCoordinates,
        latitude: Double,
        longitude: Double
    ): Float
}
