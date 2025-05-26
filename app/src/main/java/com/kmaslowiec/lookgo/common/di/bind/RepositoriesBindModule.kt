package com.kmaslowiec.lookgo.common.di.bind

import com.kmaslowiec.lookgo.stops.repository.StopsRepository
import com.kmaslowiec.lookgo.stops.repository.StopsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesBindModule {

    @Binds
    @Singleton
    abstract fun bindStopsRepository(
        impl: StopsRepositoryImpl
    ): StopsRepository
}
