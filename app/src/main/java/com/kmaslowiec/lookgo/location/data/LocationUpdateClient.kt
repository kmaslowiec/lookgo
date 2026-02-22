package com.kmaslowiec.lookgo.location.data

import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import kotlinx.coroutines.flow.Flow

interface LocationUpdatesClient {
    fun requestLocationUpdates() : Flow<LocationCoordinates>
}
