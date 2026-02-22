package com.kmaslowiec.lookgo.location.data.impl

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kmaslowiec.lookgo.common.utils.toLocationCoordinates
import com.kmaslowiec.lookgo.location.data.LocationUpdatesClient
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationUpdatesClientImpl @Inject constructor(
    context: Context
) : LocationUpdatesClient {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    @Suppress("MissingPermission")
    override fun requestLocationUpdates(): Flow<LocationCoordinates> {
        return callbackFlow {
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                5_000L
            ).build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { location ->
                        trySend(location.toLocationCoordinates())
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                callback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(callback)
            }
        }
    }
}
