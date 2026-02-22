package com.kmaslowiec.lookgo.stops.repository

import com.kmaslowiec.lookgo.common.domain.LookgoError
import com.kmaslowiec.lookgo.common.domain.LookgoResult
import com.kmaslowiec.lookgo.stops.model.Stops
import kotlinx.coroutines.flow.Flow

interface StopsRepository {
    fun getStops(): Flow<LookgoResult<Stops, LookgoError>>
}
