package com.kmaslowiec.lookgo.preferences.state

sealed class PreferencesState() {
    data class Success(val isFirstTime: Boolean) : PreferencesState()
    data object Loading : PreferencesState()
}