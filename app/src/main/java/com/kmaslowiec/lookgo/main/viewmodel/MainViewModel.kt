package com.kmaslowiec.lookgo.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.common.domain.LookgoResult
import com.kmaslowiec.lookgo.location.model.LocationCoordinates
import com.kmaslowiec.lookgo.location.usecase.LocationDistanceCalculatorUseCase
import com.kmaslowiec.lookgo.location.usecase.LocationUpdatesUseCase
import com.kmaslowiec.lookgo.main.view.uievent.MainUIEvent
import com.kmaslowiec.lookgo.main.view.uistate.MainScreenUiState
import com.kmaslowiec.lookgo.stops.model.Stop
import com.kmaslowiec.lookgo.stops.repository.StopsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationUpdatesUseCase: LocationUpdatesUseCase,
    private val locationDistanceCalculatorUseCase: LocationDistanceCalculatorUseCase,
    private val repo: StopsRepository
) : ViewModel() {
    private val _currentLocation = MutableStateFlow(LocationCoordinates(0.0, 0.0))
    val currentLocation: StateFlow<LocationCoordinates> = _currentLocation
    private val _uiEvents = MutableSharedFlow<MainUIEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val uiEvents: SharedFlow<MainUIEvent> = _uiEvents
    private val _mainScreenUiState = MutableStateFlow<MainScreenUiState>(MainScreenUiState.Loading)
    val mainScreenUiState: StateFlow<MainScreenUiState> = _mainScreenUiState

    init {
        observeLocationUpdate()
    }

    fun getNearBusStops() {
        viewModelScope.launch {
            _mainScreenUiState.value = MainScreenUiState.Loading
            repo.getStops()
                .collect { stopsResult ->
                    when (stopsResult) {
                        is LookgoResult.Success -> {
                            _mainScreenUiState.value = MainScreenUiState.Success(
                                stopsResult
                                    .value
                                    .data.filter { stop ->
                                        isBusStopNearby(stop)
                                    })
                        }

                        is LookgoResult.NotModified -> {}
                        is LookgoResult.Error -> {
                            _mainScreenUiState.value = MainScreenUiState.Exception(stopsResult.exception)
                        }
                    }
                }
        }
    }

    fun onNavigateToLocationPermission() {
        handleUIEvent(MainUIEvent.NavigateToLocationPermission)
    }

    private fun observeLocationUpdate() {
        viewModelScope.launch {
            locationUpdatesUseCase.locationUpdates().collect { locationDomain ->
                _currentLocation.value = locationDomain
            }
        }
    }

    private fun isBusStopNearby(stop: Stop): Boolean = locationDistanceCalculatorUseCase.currentLocationDistanceTo(
        currentLocation = currentLocation.value,
        latitude = stop.latitude,
        longitude = stop.longitude
    ) <= 500.0

    private fun handleUIEvent(uiEvent: MainUIEvent) {
        viewModelScope.launch {
            _uiEvents.emit(uiEvent)
        }
    }
}
