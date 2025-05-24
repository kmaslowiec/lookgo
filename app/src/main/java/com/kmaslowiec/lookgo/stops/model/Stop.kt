package com.kmaslowiec.lookgo.stops.model

data class Stop(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val number: String,
    val park_and_ride: Boolean,
    val railway_station_name: String,
    val request_stop: Boolean,
    val updated_at: String
)