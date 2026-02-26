package com.kmaslowiec.lookgo.common.di.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kmaslowiec.lookgo.preferences.repository.AppPreferencesRepository
import com.kmaslowiec.lookgo.preferences.repository.impl.AppPreferencesRepositoryImpl
import com.kmaslowiec.lookgo.util.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    fun provideAppPreferencesRepository(dataStore: DataStore<Preferences>): AppPreferencesRepository =
        AppPreferencesRepositoryImpl(dataStore)
}
