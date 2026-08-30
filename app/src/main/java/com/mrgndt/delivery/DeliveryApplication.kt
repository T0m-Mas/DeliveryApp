package com.mrgndt.delivery

import android.app.Application
import com.mrgndt.delivery.network.service.PlacesService

class DeliveryApplication: Application(){

    lateinit var placesService: PlacesService

    override fun onCreate() {
        super.onCreate()
        val googleMapsApiKey = BuildConfig.GOOGLE_MAPS_API_KEY
        placesService = PlacesService(key = googleMapsApiKey)
    }
}
