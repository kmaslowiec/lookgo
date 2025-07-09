package com.kmaslowiec.lookgo.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.preferences.AppPreferencesRepository
import com.kmaslowiec.lookgo.preferences.state.PreferencesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val preferencesRepository: AppPreferencesRepository,
) : ViewModel() {

    val preferencesState = MutableStateFlow<PreferencesState>(PreferencesState.Loading)

    init {
        viewModelScope.launch {
            preferencesRepository.isFirstTime.collect {
                preferencesState.value = PreferencesState.Success(it)
            }
        }
    }

    fun firstTimeAccess() {
        viewModelScope.launch {
            preferencesRepository.firstTimeAccess()
        }
    }
}
