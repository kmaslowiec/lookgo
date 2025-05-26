package com.kmaslowiec.lookgo.stops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.stops.model.Stops
import com.kmaslowiec.lookgo.stops.repository.StopsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopsViewModel @Inject constructor(private val stopsRepository: StopsRepository) :
    ViewModel() {

    private val _stops = MutableStateFlow<Stops>(Stops(emptyList()))
    var stops: StateFlow<Stops> = _stops

    init {
        viewModelScope.launch {
            stopsRepository.getStops().collect { result ->
                _stops.value = result
            }
        }
    }
}