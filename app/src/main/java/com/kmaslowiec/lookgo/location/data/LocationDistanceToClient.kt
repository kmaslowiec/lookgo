package com.kmaslowiec.lookgo.location.data

import com.kmaslowiec.lookgo.location.model.LocationCoordinates

interface LocationDistanceToClient {
    fun currentLocationDistanceTo(
        currentLocation: LocationCoordinates,
        latitude: Double,
        longitude: Double
    ): Float
}
