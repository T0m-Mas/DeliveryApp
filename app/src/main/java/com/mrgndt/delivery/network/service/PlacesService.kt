package com.mrgndt.delivery.network.service

import com.mrgndt.delivery.network.data.AutoCompleteBody
import com.mrgndt.delivery.network.data.AutoCompleteResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST


class PlacesService(
    key: String
) {

    private val headerInterceptor = HeaderInterceptor(key)

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(headerInterceptor)
        .build()

    private val apiUrl = "https://places.googleapis.com/v1/places"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(apiUrl)
        .build()

    private val retrofitService: ApiServiceInterfaceInterface by lazy {
        retrofit.create(ApiServiceInterfaceInterface::class.java)
    }

    suspend fun autocomplete(
        search: String,
    ): AutoCompleteResponse {

        val body = AutoCompleteBody(
            input = search,
        )


        return retrofitService.autocomplete(body)

    }

//    suspend fun getPlaceDetails(
//        id: String,
//    ): PlaceDetailsResponse? {
//        return try {
//            retrofitService.placeDetails(
//                id = id,
//                key = key,
//            )
//        } catch (e: Exception) {
//            Log.e("PlacesApi", "$e")
//            null
//
//        }
//    }

}

interface ApiServiceInterfaceInterface {
    @POST("places:autocomplete")
    suspend fun autocomplete(body: AutoCompleteBody): AutoCompleteResponse

//    @GET("details/json")
//    suspend fun placeDetails(
//        @Query("place_id") id: String,
//        @Query("fields") fields: String = "formatted_address,name,geometry",
//        @Query("key") key: String,
//        @Query("language") language: String = "es",
//    ): PlaceDetailsResponse
}