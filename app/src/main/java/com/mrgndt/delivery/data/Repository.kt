package com.mrgndt.delivery.data

import com.mrgndt.delivery.data.db.LocationDB
import com.mrgndt.delivery.data.db.MainRepositoryDAO
import com.mrgndt.delivery.model.Location


class MainRepository(private val dao: MainRepositoryDAO) {

    suspend fun saveLocation(location: Location): Long =
        dao.saveLocation(
            LocationDB(
                latitude = location.latitude,
                longitude = location.longitude,
                address = location.address,
                label = location.label,
            )
        )

    suspend fun getAllLocations(): List<Location> {
        return dao.getAllLocations().map {
            Location(
                it.id,
                it.latitude,
                it.longitude,
                it.address,
                it.label
            )
        }


    }
}
