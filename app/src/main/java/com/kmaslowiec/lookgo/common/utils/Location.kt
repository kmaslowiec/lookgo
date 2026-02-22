package com.kmaslowiec.lookgo.common.utils

import android.location.Location
import com.kmaslowiec.lookgo.location.model.LocationCoordinates

fun Location.toLocationCoordinates() = LocationCoordinates(
    latitude = this.latitude,
    longitude = this.longitude
)
