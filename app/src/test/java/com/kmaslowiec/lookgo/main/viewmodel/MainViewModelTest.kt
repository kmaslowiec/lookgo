package com.kmaslowiec.lookgo.main.viewmodel

import app.cash.turbine.test
import com.kmaslowiec.lookgo.common.domain.LookgoError
import com.kmaslowiec.lookgo.common.domain.LookgoResult
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.location.usecase.impl.LocationDistanceCalculatorUseCaseImpl
import com.kmaslowiec.lookgo.location.usecase.impl.LocationUpdatesUseCaseImpl
import com.kmaslowiec.lookgo.main.view.uievent.MainUIEvent
import com.kmaslowiec.lookgo.main.view.uistate.MainScreenUiState
import com.kmaslowiec.lookgo.stops.model.Stop
import com.kmaslowiec.lookgo.stops.model.Stops
import com.kmaslowiec.lookgo.stops.repository.StopsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainViewModelTest {

    val locationUpdatesUseCase = mockk<LocationUpdatesUseCaseImpl>(relaxed = true) {
        every { locationUpdates() } returns emptyFlow()
    }

    val locationDistanceToUseCase = mockk<LocationDistanceCalculatorUseCaseImpl>(relaxed = true) {
        every { currentLocationDistanceTo(any(), any(), any()) } returns 0f
    }

    val repo = mockk<StopsRepository>() {
        every { getStops() } returns emptyFlow()
    }

    val viewModel = MainViewModel(
        locationUpdatesUseCase = locationUpdatesUseCase,
        locationDistanceCalculatorUseCase = locationDistanceToUseCase,
        repo = repo
    )

    @Test
    fun `onNavigateToLocationPermission should emit NavigateToLocationPermission event`() = runTest {
        viewModel.uiEvents.test {
            viewModel.onNavigateToLocationPermission()

            assertEquals(MainUIEvent.NavigateToLocationPermission, awaitItem())
        }
    }

    @Test
    fun `getNearBusStops should emit Loading when awaits`() = runTest {
        val locationUpdatesUseCase = mockk<LocationUpdatesUseCaseImpl>() {
            every { locationUpdates() } returns emptyFlow()
        }

        val locationDistanceToUseCase = mockk<LocationDistanceCalculatorUseCaseImpl>(relaxed = true) {
            every { currentLocationDistanceTo(any(), any(), any()) } returns 0f
        }

        val repo = mockk<StopsRepository>(relaxed = true)

        val viewModel = MainViewModel(
            locationUpdatesUseCase = locationUpdatesUseCase,
            locationDistanceToUseCase,
            repo = repo
        )

        viewModel.mainScreenUiState.test {
            viewModel.getNearBusStops()

            assertEquals(MainScreenUiState.Loading, awaitItem())
        }
    }

    @Test
    fun `getNearBusStops should emit Success when receives list of items`() = runTest {
        val stops = Stops(
            listOf(
                Stop(
                    id = 1,
                    latitude = 0.0,
                    longitude = 0.0,
                    name = "T",
                    number = "",
                    parkAndRide = false,
                    railwayStationName = "",
                    requestStop = false,
                    updatedAt = ""
                )
            )
        )
        val listOfStops = listOf(
            Stop(
                id = 1,
                latitude = 0.0,
                longitude = 0.0,
                name = "T",
                number = "",
                parkAndRide = false,
                railwayStationName = "",
                requestStop = false,
                updatedAt = ""
            )
        )
        val lookgoResult = LookgoResult.Success(stops)
        val location = mockk<LocationCoordinates>()
        every { locationUpdatesUseCase.locationUpdates() } returns flowOf(location)
        every { repo.getStops() } returns flowOf(lookgoResult)

        val viewModel = MainViewModel(
            locationUpdatesUseCase = locationUpdatesUseCase,
            locationDistanceCalculatorUseCase = locationDistanceToUseCase,
            repo = repo
        )

        viewModel.mainScreenUiState.test {
            viewModel.getNearBusStops()
            skipItems(1)

            assertEquals(MainScreenUiState.Success(listOfStops), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getNearBusStops should emits Loading when receives NotModified`() = runTest {
        val lookgoResult = LookgoResult.NotModified
        val location = mockk<LocationCoordinates>()
        every { locationUpdatesUseCase.locationUpdates() } returns flowOf(location)
        every { repo.getStops() } returns flowOf(lookgoResult)

        val viewModel = MainViewModel(
            locationUpdatesUseCase = locationUpdatesUseCase,
            locationDistanceCalculatorUseCase = locationDistanceToUseCase,
            repo = repo
        )

        viewModel.mainScreenUiState.test {
            viewModel.getNearBusStops()

            assertEquals(MainScreenUiState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getNearBusStops should emit Exception when receives exception`() = runTest {
        val lookgoResult = LookgoResult.Error(LookgoError.EmptyList)
        val location = mockk<LocationCoordinates>()
        every { locationUpdatesUseCase.locationUpdates() } returns flowOf(location)
        every { repo.getStops() } returns flowOf(lookgoResult)
        val viewModel = MainViewModel(
            locationUpdatesUseCase = locationUpdatesUseCase,
            locationDistanceCalculatorUseCase = locationDistanceToUseCase,
            repo = repo
        )

        viewModel.mainScreenUiState.test {
            viewModel.getNearBusStops()
            skipItems(1)

            assertEquals(MainScreenUiState.Exception(LookgoError.EmptyList), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
