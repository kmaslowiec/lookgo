package com.kmaslowiec.lookgo.persistence

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var stopDao: StopDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        stopDao = db.stopDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAll_and_getAllStops_returnsInsertedStops() = runTest {
        val stops = listOf(
            StopEntity(
                id = 1,
                latitude = 53.1,
                longitude = 14.5,
                name = "Stop A",
                number = "100",
                parkAndRide = false,
                railwayStationName = "",
                requestStop = false,
                updatedAt = "2026-03-31"
            ),
            StopEntity(
                id = 2,
                latitude = 53.2,
                longitude = 14.6,
                name = "Stop B",
                number = "101",
                parkAndRide = true,
                railwayStationName = "Central",
                requestStop = true,
                updatedAt = "2026-03-31"
            )
        )

        stopDao.insertAll(stops)
        val result = stopDao.getAllStops()

        assertEquals(2, result.size)
        assertEquals(stops, result)
    }

    @Test
    fun deleteAll_removesEverything() = runTest {
        val stops = listOf(
            StopEntity(
                id = 1,
                latitude = 53.1,
                longitude = 14.5,
                name = "Stop A",
                number = "100",
                parkAndRide = false,
                railwayStationName = "",
                requestStop = false,
                updatedAt = "2026-03-31"
            )
        )

        stopDao.insertAll(stops)
        stopDao.deleteAll()
        val result = stopDao.getAllStops()

        assertTrue(result.isEmpty())
    }

    @Test
    fun insertAll_withSameId_replacesExistingItem() = runTest {
        val oldStop = StopEntity(
            id = 1,
            latitude = 53.1,
            longitude = 14.5,
            name = "Old Stop",
            number = "100",
            parkAndRide = false,
            railwayStationName = "",
            requestStop = false,
            updatedAt = "2026-03-31"
        )

        val newStop = StopEntity(
            id = 1,
            latitude = 53.9,
            longitude = 14.9,
            name = "New Stop",
            number = "999",
            parkAndRide = true,
            railwayStationName = "Updated",
            requestStop = true,
            updatedAt = "2026-04-01"
        )

        stopDao.insertAll(listOf(oldStop))
        stopDao.insertAll(listOf(newStop))
        val result = stopDao.getAllStops()

        assertEquals(1, result.size)
        assertEquals(newStop, result.first())
    }
}
