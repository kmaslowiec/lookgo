package com.kmaslowiec.lookgo.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stops")
data class StopEntity(

    @PrimaryKey
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val number: String,

    @ColumnInfo(name = "park_and_ride")
    val parkAndRide: Boolean,

    @ColumnInfo(name = "railway_station_name")
    val railwayStationName: String,

    @ColumnInfo(name = "request_stop")
    val requestStop: Boolean,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)
