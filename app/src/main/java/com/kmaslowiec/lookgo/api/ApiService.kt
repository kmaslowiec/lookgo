package com.kmaslowiec.lookgo.api

import com.kmaslowiec.lookgo.stopdisplay.model.StopDisplay
import com.kmaslowiec.lookgo.stops.model.Stops
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("stops")
    suspend fun getStops(): Stops

    @GET("displays/{stopNumber}")
    suspend fun getStopDisplay(
        @Path("stopNumber") stopNumber: String
    ): StopDisplay

}
