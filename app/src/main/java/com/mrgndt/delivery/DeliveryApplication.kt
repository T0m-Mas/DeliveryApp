package com.mrgndt.delivery

import android.app.Application
import com.mrgndt.delivery.data.MainRepository
import com.mrgndt.delivery.data.db.MainDataBase
import com.mrgndt.delivery.network.service.PlacesService
import kotlinx.coroutines.runBlocking

class DeliveryApplication : Application() {

    lateinit var placesService: PlacesService
    lateinit var mainRepository: MainRepository

    override fun onCreate() {
        super.onCreate()
        val googleMapsApiKey = BuildConfig.GOOGLE_MAPS_API_KEY
        placesService = PlacesService(key = googleMapsApiKey)
        mainRepository = runBlocking {
            MainRepository(MainDataBase.getDatabase(this@DeliveryApplication).dao())
        }
    }
}
