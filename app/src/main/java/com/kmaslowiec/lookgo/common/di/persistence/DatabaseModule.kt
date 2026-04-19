package com.kmaslowiec.lookgo.common.di.persistence

import android.content.Context
import androidx.room.Room
import com.kmaslowiec.lookgo.persistence.AppDatabase
import com.kmaslowiec.lookgo.persistence.StopDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "stops_database"
    ).build()

    @Provides
    @Singleton
    fun provideStopDao(database: AppDatabase): StopDao {
        return database.stopDao()
    }
}
