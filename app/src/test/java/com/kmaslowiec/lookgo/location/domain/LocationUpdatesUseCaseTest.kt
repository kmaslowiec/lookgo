package com.kmaslowiec.lookgo.location.domain

import app.cash.turbine.test
import com.kmaslowiec.lookgo.location.data.LocationUpdatesClient
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.location.usecase.impl.LocationUpdatesUseCaseImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LocationUpdatesUseCaseTest {

    @Test
    fun `emits location when callback receives update`() = runTest {
        val locationCoordinates = LocationCoordinates(52.0, 21.0)
        val locationUpdatesClient = mockk<LocationUpdatesClient>()
        every { locationUpdatesClient.requestLocationUpdates() } returns flowOf(locationCoordinates)
        val useCase = LocationUpdatesUseCaseImpl(
            locationUpdatesClient = locationUpdatesClient
        )

        useCase.locationUpdates().test {
            val tested = awaitItem()
            assertEquals(52.0, tested.latitude)
            assertEquals(21.0, tested.longitude)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `does not emit location when callback receives no update`() = runTest {
        val locationUpdatesClient = mockk<LocationUpdatesClient>()
        every { locationUpdatesClient.requestLocationUpdates() } returns flow {
            awaitCancellation()
        }
        val useCase = LocationUpdatesUseCaseImpl(
            locationUpdatesClient = locationUpdatesClient
        )

        useCase.locationUpdates().test {
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
