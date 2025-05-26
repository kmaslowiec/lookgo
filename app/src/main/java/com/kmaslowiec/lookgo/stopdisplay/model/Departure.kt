package com.kmaslowiec.lookgo.stopdisplay.model

import com.google.gson.annotations.SerializedName

data class Departure(
    val direction: String,
    @SerializedName("line_number")
    val lineNumber: String,
    @SerializedName("time_real")
    val timeReal: Int,
    @SerializedName("time_scheduled")
    val timeScheduled: String
)