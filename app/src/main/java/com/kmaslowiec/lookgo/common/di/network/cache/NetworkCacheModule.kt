package com.kmaslowiec.lookgo.common.di.network.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kmaslowiec.lookgo.network.cache.ETagCache
import com.kmaslowiec.lookgo.network.cache.ETagStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkCacheModule {

    @Provides
    @Singleton
    fun provideAppScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun provideETagStorage(preferences: DataStore<Preferences>) = ETagStorage(preferences)

    @Provides
    @Singleton
    fun provideETagCache(storage: ETagStorage, appScope: CoroutineScope): ETagCache =
        ETagCache(storage, appScope)
}
