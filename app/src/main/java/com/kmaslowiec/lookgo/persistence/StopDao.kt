package com.kmaslowiec.lookgo.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stops: List<StopEntity>)

    @Query("SELECT * FROM stops")
    suspend fun getAllStops(): List<StopEntity>

    @Query("DELETE FROM stops")
    suspend fun deleteAll()
}
