package com.kmaslowiec.lookgo.location.usecase.impl

import com.kmaslowiec.lookgo.location.data.LocationDistanceToClient
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LocationDistanceToUseCaseImplTest {

    @Test
    fun `currentLocationDistanceTo should return correct distance`() {
        val currentLocation = LocationCoordinates(10.0, 15.0)
        val locationDistanceToClient = mockk<LocationDistanceToClient>()
        every {
            locationDistanceToClient.currentLocationDistanceTo(
                any(), any(), any(),
            )
        } returns 15f
        val useCase = LocationDistanceToUseCaseImpl(locationDistanceToClient)

        val tested = useCase.currentLocationDistanceTo(currentLocation, 52.0, 21.0)

        assertEquals(15f, tested)
    }
}
