package com.kmaslowiec.lookgo.location.usecase

import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import kotlinx.coroutines.flow.Flow

interface LocationUpdatesUseCase {
    fun locationUpdates(): Flow<LocationCoordinates>
}
