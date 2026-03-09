package com.kmaslowiec.lookgo.stopdisplay.repository

import com.kmaslowiec.lookgo.network.ApiService
import javax.inject.Inject

class StopDisplayRepositoryImpl @Inject constructor(private val api: ApiService) :
    StopDisplayRepository {

    override suspend fun getStopDisplay(stopNumber: String) = api.getStopDisplay(stopNumber)
}