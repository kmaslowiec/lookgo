package com.kmaslowiec.lookgo.stops.repository

import com.kmaslowiec.lookgo.api.ApiService
import com.kmaslowiec.lookgo.stops.model.Stops
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StopsRepositoryImpl @Inject constructor(private val api: ApiService) : StopsRepository {

    override fun getStops(): Flow<Stops> = flow {
        try {
            emit(api.getStops())
        } catch (exception: Exception) {
            throw exception
        }
    }.flowOn(Dispatchers.IO)
}
