package com.kmaslowiec.lookgo.preferences.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.preferences.AppPreferencesRepository
import com.kmaslowiec.lookgo.preferences.state.PreferencesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: AppPreferencesRepository,
) : ViewModel() {

    private val _preferencesState = MutableStateFlow<PreferencesState>(PreferencesState.Loading)
    val preferencesState = _preferencesState

    init {
        viewModelScope.launch {
            preferencesRepository.isFirstTime.collect {
                _preferencesState.value = PreferencesState.Success(it)
            }
        }
    }

    fun firstTimeAccess() {
        viewModelScope.launch {
            preferencesRepository.firstTimeAccess()
        }
    }
}
