package com.kmaslowiec.lookgo.preferences

import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {
    suspend fun firstTimeAccess()
    val isFirstTime: Flow<Boolean>
}
