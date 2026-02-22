package com.kmaslowiec.lookgo.common.di.location

import android.content.Context
import com.kmaslowiec.lookgo.location.data.LocationDistanceToClient
import com.kmaslowiec.lookgo.location.data.impl.LocationDistanceToClientImpl
import com.kmaslowiec.lookgo.location.data.LocationUpdatesClient
import com.kmaslowiec.lookgo.location.data.impl.LocationUpdatesClientImpl
import com.kmaslowiec.lookgo.location.usecase.LocationDistanceToUseCase
import com.kmaslowiec.lookgo.location.usecase.LocationUpdatesUseCase
import com.kmaslowiec.lookgo.location.usecase.impl.LocationDistanceToUseCaseImpl
import com.kmaslowiec.lookgo.location.usecase.impl.LocationUpdatesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    fun provideLocationUpdatesClient(
        @ApplicationContext context: Context
    ): LocationUpdatesClient {
        return LocationUpdatesClientImpl(context)
    }

    @Provides
    fun provideLocationUpdatesUseCase(
        locationUpdatesClient: LocationUpdatesClient
    ): LocationUpdatesUseCase {
        return LocationUpdatesUseCaseImpl(locationUpdatesClient)
    }

    @Provides
    fun provideLocationDistanceToClient(
    ): LocationDistanceToClient {
        return LocationDistanceToClientImpl()
    }

    @Provides
    fun provideLocationDistanceToUseCase(
        locationUpdatesUseCase: LocationDistanceToClient
    ): LocationDistanceToUseCase {
        return LocationDistanceToUseCaseImpl(locationUpdatesUseCase)
    }
}
