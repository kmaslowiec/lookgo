package com.kmaslowiec.lookgo.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

val isFirstTimePreferencesKey = booleanPreferencesKey("firstTimePreferences")

val eTagPreferencesKeys = stringSetPreferencesKey("eTag_keys")
