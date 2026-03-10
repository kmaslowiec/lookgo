package com.kmaslowiec.lookgo.network.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kmaslowiec.lookgo.util.eTagPreferencesKeys
import kotlinx.coroutines.flow.first

class ETagStorage(
    private val dataStore: DataStore<Preferences>
) {

    private fun eTagKey(urlKey: String) = stringPreferencesKey("eTag_$urlKey")

    suspend fun loadAll(): Map<String, String> {
        val prefs = dataStore.data.first()
        val keys = prefs[eTagPreferencesKeys].orEmpty()

        return keys.mapNotNull { key ->
            val eTag = prefs[eTagKey(key)]
            if (eTag.isNullOrBlank()) null else key to eTag
        }.toMap()
    }

    suspend fun save(urlKey: String, eTag: String) {
        dataStore.edit { prefs ->
            val currentKeys = prefs[eTagPreferencesKeys].orEmpty()
            prefs[eTagPreferencesKeys] = currentKeys + urlKey
            prefs[eTagKey(urlKey)] = eTag
        }
    }
}
