package com.kmaslowiec.lookgo.stops.model

import com.google.gson.annotations.SerializedName

data class Stop(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val number: String,
    @SerializedName("park_and_ride")
    val parkAndRide: Boolean,
    @SerializedName("railway_station_name")
    val railwayStationName: String,
    @SerializedName("request_stop")
    val requestStop: Boolean,
    @SerializedName("updated_at")
    val updatedAt: String
)