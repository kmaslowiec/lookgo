package com.kmaslowiec.lookgo.location.usecase.impl

import com.kmaslowiec.lookgo.location.data.LocationUpdatesClient
import com.kmaslowiec.lookgo.location.usecase.LocationUpdatesUseCase
import javax.inject.Inject

class LocationUpdatesUseCaseImpl @Inject constructor(
    val locationUpdatesClient: LocationUpdatesClient,
) : LocationUpdatesUseCase {

    override fun locationUpdates() = locationUpdatesClient.requestLocationUpdates()
}
