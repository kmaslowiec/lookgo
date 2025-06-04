package com.kmaslowiec.lookgo.stopdisplay.model

import com.google.gson.annotations.SerializedName

data class StopDisplay(
    val departures: List<Departure> = emptyList(),
    val message: String,
    @SerializedName("stop_name")
    val stopName: String,
    @SerializedName("stop_number")
    val stopNumber: String,
    @SerializedName("updated_at")
    val updatedAt: String
)