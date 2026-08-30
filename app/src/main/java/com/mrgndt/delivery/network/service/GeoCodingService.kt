package com.mrgndt.delivery.network.service

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.mrgndt.delivery.network.data.GeoCodingResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class GeoCodingService(
    private val key: String
) {

    private val client = OkHttpClient
        .Builder()
        .build()


    private val apiUrl = "https://maps.googleapis.com/maps/api/geocode/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(apiUrl).build()


    val retrofitService: GeoCodingServiceInterface by lazy {
        retrofit.create(GeoCodingServiceInterface::class.java)
    }

    suspend fun getAddressOfLatLng(
        latLng: LatLng
    ): GeoCodingResponse? {
        return try {
            retrofitService.getAddress(
                key = key,
                latlng = "${latLng.latitude},${latLng.longitude}"
            )
        } catch (e: Exception) {
            Log.d("getAddressOfLatLng", "$e")
            null
        }
    }
}


interface GeoCodingServiceInterface {
    @GET("json")
    suspend fun getAddress(
        @Query("key") key: String,
        @Query("language") language: String? = "es-419",
        @Query("latlng") latlng: String,

        ): GeoCodingResponse
}