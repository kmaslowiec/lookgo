package com.kmaslowiec.lookgo.api

import com.kmaslowiec.lookgo.stops.model.Stops
import retrofit2.http.GET

interface ApiService {

    @GET("stops")
    suspend fun getStops(): Stops
}
