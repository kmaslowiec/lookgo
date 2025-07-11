package com.kmaslowiec.lookgo.stops.repository

import com.kmaslowiec.lookgo.api.ApiService
import com.kmaslowiec.lookgo.stops.model.Stop
import com.kmaslowiec.lookgo.stops.model.Stops
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class StopsRepositoryImplTest {

    private val api: ApiService = mockk()
    private lateinit var stopsRepository: StopsRepositoryImpl

    @BeforeEach
    fun setUp() {
        stopsRepository = StopsRepositoryImpl(api)
    }

    @Test
    fun `getStops emits Stops on success`() = runTest {
        val stops = Stops(
            data = listOf(
                Stop(
                    id = 1,
                    latitude = 0.0,
                    longitude = 0.0,
                    name = "",
                    number = "",
                    parkAndRide = false,
                    railwayStationName = "",
                    requestStop = false,
                    updatedAt = ""
                )
            )
        )
        coEvery { api.getStops() } returns stops

        val result = stopsRepository.getStops().toList()

        assertTrue(result.size == 1)
        assertTrue(result.first() == stops)
    }

    @Test
    fun `getStops throws exception on failure`() = runTest {
        val exception = Exception("")
        coEvery { api.getStops() } throws exception

        assertThrows<Exception> {
            stopsRepository.getStops().collect {}
        }
    }
}
