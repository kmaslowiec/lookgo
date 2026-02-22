package com.kmaslowiec.lookgo.stops.repository

import app.cash.turbine.test
import com.kmaslowiec.lookgo.api.ApiService
import com.kmaslowiec.lookgo.common.domain.LookgoError
import com.kmaslowiec.lookgo.common.domain.LookgoResult
import com.kmaslowiec.lookgo.stops.model.Stop
import com.kmaslowiec.lookgo.stops.model.Stops
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response.error
import java.io.IOException

class StopsRepositoryImplTest {
    private val api: ApiService = mockk()
    private lateinit var stopsRepository: StopsRepositoryImpl

    @BeforeEach
    fun setUp() {
        stopsRepository = StopsRepositoryImpl(api)
    }

    @Test
    fun `getStops emits result with not empty list`() = runTest {
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

        stopsRepository.getStops().test {
            val result = awaitItem()
            assertEquals(LookgoResult.Success(stops), result)
            awaitComplete()
        }
    }

    @Test
    fun `getStops emits LookgoResult Exception on failure`() = runTest {
        coEvery { api.getStops() } throws Throwable()

        stopsRepository.getStops().test {
            val result = awaitItem()
            assertTrue(result is LookgoResult.Error)
            awaitComplete()
        }
    }

    @Test
    fun `getStops emits EmptyList Exception when an empty list is given`() = runTest {
        val emptyStops = Stops(
            data = emptyList()
        )

        coEvery { api.getStops() } returns emptyStops

        stopsRepository.getStops().test {
            val result = awaitItem()
            assertEquals(LookgoResult.Error(LookgoError.EmptyList), result)
            awaitComplete()
        }

    }

    @Test
    fun `getStops emits Network Exception when IOException is thrown`() = runTest {
        coEvery { api.getStops() } throws IOException()

        stopsRepository.getStops().test {
            val result = awaitItem()
            assertTrue(result is LookgoResult.Error)
            assertEquals(LookgoResult.Error(LookgoError.Network), result)
            awaitComplete()
        }
    }

    @Test
    fun `getStops emits Server Exception when HttpException is thrown`() = runTest {
        val responseBody: ResponseBody = mockk(relaxed = true)
        coEvery { api.getStops() } throws HttpException(error<Unit>(404, responseBody))

        stopsRepository.getStops().test {
            val result = awaitItem()
            assertTrue(result is LookgoResult.Error)
            assertEquals(LookgoResult.Error(LookgoError.Server(404)), result)
            awaitComplete()
        }
    }

    @Test
    fun `getStops emits Unknown Exception when unknown exception is thrown`() = runTest {
        val cause: Throwable = mockk(relaxed = true)
        coEvery { api.getStops() } throws RuntimeException("message", cause)

        stopsRepository.getStops().test {
            val result = awaitItem()
            assertTrue(result is LookgoResult.Error)
            assertEquals(LookgoResult.Error(LookgoError.Unknown(cause = cause)), result)
            awaitComplete()
        }
    }
}
