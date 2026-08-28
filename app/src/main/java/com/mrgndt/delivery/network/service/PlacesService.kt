package com.mrgndt.delivery.network.service

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class PlacesApi(
    private val key: String
) {

    private val client = OkHttpClient
        .Builder()
        .build()

    private val apiUrl = ""

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(apiUrl).build()

    private val retrofitService: ApiServiceInterfaceInterface by lazy {
        retrofit.create(ApiServiceInterfaceInterface::class.java)
    }

    suspend fun autocomplete(
        search: String,
        location: String? = null,
    ): AutoCompleteResponse? {
        return try {
            retrofitService.autocomplete(
                input = search,
//        location = location,
                key = key,
            )
        } catch (e: Exception) {
            Log.e("PlacesApi", "$e")
            null

        }
    }

    suspend fun getPlaceDetails(
        id: String,
    ): PlaceDetailsResponse? {
        return try {
            retrofitService.placeDetails(
                id = id,
                key = key,
            )
        } catch (e: Exception) {
            Log.e("PlacesApi", "$e")
            null

        }
    }

}

interface ApiServiceInterfaceInterface {
    @GET("autocomplete/json")
    suspend fun autocomplete(
        @Query("input") input: String,
        @Query("location") location: String? = null,
        @Query("radius") radius: Int = 500,
        @Query("key") key: String,
        @Query("language") language: String = "es",
        @Query("region") region: String = "ar",
    ): AutoCompleteResponse

    @GET("details/json")
    suspend fun placeDetails(
        @Query("place_id") id: String,
        @Query("fields") fields: String = "formatted_address,name,geometry",
        @Query("key") key: String,
        @Query("language") language: String = "es",
    ): PlaceDetailsResponse
}