package com.mrgndt.delivery.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Location")
data class LocationDB(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val label: String? = null
)
