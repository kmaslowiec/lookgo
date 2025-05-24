package com.kmaslowiec.lookgo.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmaslowiec.lookgo.preferences.AppPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val preferencesRepository: AppPreferencesRepository,
) : ViewModel() {

    val isFirstTime = preferencesRepository.isFirstTime

    fun firstTimeAccess() {
        viewModelScope.launch {
            preferencesRepository.firstTimeAccess()
        }
    }
}
