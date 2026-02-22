package com.kmaslowiec.lookgo.stops.repository

import com.kmaslowiec.lookgo.api.ApiService
import com.kmaslowiec.lookgo.common.domain.LookgoError
import com.kmaslowiec.lookgo.common.domain.LookgoResult
import com.kmaslowiec.lookgo.common.domain.toStopsError
import com.kmaslowiec.lookgo.stops.model.Stops
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StopsRepositoryImpl @Inject constructor(private val api: ApiService) : StopsRepository {

    override fun getStops(): Flow<LookgoResult<Stops, LookgoError>> = flow {
        val stops = try {
            api.getStops()
        } catch (throwable: Throwable) {
            emit(LookgoResult.Error(throwable.toStopsError()))
            return@flow
        }

        if (stops.data.isEmpty()) {
            emit(LookgoResult.Error(LookgoError.EmptyList))
            return@flow
        }

        emit(LookgoResult.Success(stops))
    }.flowOn(Dispatchers.IO)
}
