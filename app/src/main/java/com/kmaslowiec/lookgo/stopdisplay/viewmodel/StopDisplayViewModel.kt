package com.kmaslowiec.lookgo.stopdisplay.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.stopdisplay.model.Departure
import com.kmaslowiec.lookgo.stopdisplay.model.StopDisplay
import com.kmaslowiec.lookgo.stopdisplay.repository.StopDisplayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopDisplayViewModel @Inject constructor(private val stopDisplayRepository: StopDisplayRepository) :
    ViewModel() {

    private val _stopDisplay = MutableStateFlow<StopDisplay>(StopDisplay(
        departures = listOf(Departure(
            direction = "",
            lineNumber = "",
            timeReal = -1,
            timeScheduled = ""
        )),
        message = "",
        stopName = "",
        stopNumber = "",
        updatedAt = ""
    ))
    val stopDisplay: StateFlow<StopDisplay> = _stopDisplay

    init {
        viewModelScope.launch {
            _stopDisplay.value = stopDisplayRepository.getStopDisplay("10813")
        }
    }
}
