package com.kmaslowiec.lookgo.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

val isFirstTimePreferencesKey = booleanPreferencesKey("firstTimePreferences")

val eTagPreferencesKey = stringPreferencesKey("eTagPreferences")
