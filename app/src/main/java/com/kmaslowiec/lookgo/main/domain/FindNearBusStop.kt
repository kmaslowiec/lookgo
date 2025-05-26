package com.kmaslowiec.lookgo.main.domain

import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.stops.repository.StopsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class FindNearBusStop @Inject constructor(
    private val stopsRepository: StopsRepository,
) {

    suspend operator fun invoke(currentLocation: LocationCoordinates) = stopsRepository.getStops()
        .first()
        .data
        .filter { location ->
            calculateRange(
                currentLocation,
                LocationCoordinates(location.latitude, location.longitude)
            ) <= 500.0
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
}
