package com.mrgndt.delivery.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MainRepositoryDAO {
    @Insert
    suspend fun saveLocation(locationDB: LocationDB): Long

    @Query("SELECT * FROM Location")
    suspend fun getAllLocations(): List<LocationDB>

    @Update
    suspend fun updateLocation(location: LocationDB)

}