package com.kmaslowiec.lookgo.stops.repository

import com.kmaslowiec.lookgo.common.domain.LookgoError
import com.kmaslowiec.lookgo.common.domain.LookgoResult
import com.kmaslowiec.lookgo.common.domain.toStopsError
import com.kmaslowiec.lookgo.network.ApiService
import com.kmaslowiec.lookgo.stops.model.Stops
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StopsRepositoryImpl @Inject constructor(private val api: ApiService) : StopsRepository {

    override fun getStops(): Flow<LookgoResult<Stops, LookgoError>> = flow {
        try {
            val response = api.getStops()
            if (response.code() == 304) {
                emit(LookgoResult.NotModified)
                return@flow
            } else {
                response.body()?.let { stops ->
                    if (stops.data.isEmpty()) {
                        emit(LookgoResult.Error(LookgoError.EmptyList))
                        return@flow
                    } else {
                        emit(LookgoResult.Success(stops))
                        return@flow
                    }
                }
            }
        } catch (throwable: Throwable) {
            emit(LookgoResult.Error(throwable.toStopsError()))
        }
    }
}
