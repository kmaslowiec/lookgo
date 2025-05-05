package com.kmaslowiec.lookgo.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kmaslowiec.lookgo.util.isFirstTimePreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : AppPreferencesRepository {

    override suspend fun firstTimeAccess() {
        dataStore.edit { preferences ->
            preferences[isFirstTimePreferencesKey] = false
        }
    }

    override val isFirstTime: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[isFirstTimePreferencesKey] ?: true
        }
}
