package com.mrgndt.delivery.network.service

import com.mrgndt.delivery.network.data.AutoCompleteBody
import com.mrgndt.delivery.network.data.AutoCompleteResponse
import com.mrgndt.delivery.network.data.PlaceDetailsResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


class PlacesService(
    key: String
) {

    private val headerInterceptor = HeaderInterceptor(key)

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(headerInterceptor)
        .build()

    private val apiUrl = "https://places.googleapis.com/"

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

    suspend fun getPlaceDetails(
        placeId: String,
    ): PlaceDetailsResponse {
        return retrofitService.placeDetails(
            placeId = placeId
        )
    }

}

interface ApiServiceInterfaceInterface {
    @POST("v1/places:autocomplete")
    suspend fun autocomplete(@Body body: AutoCompleteBody): AutoCompleteResponse

    @GET("v1/places/{placeId}")
    suspend fun placeDetails(
        @Path("placeId") placeId: String,
        @Query("fields") fields: String = "location"
    ): PlaceDetailsResponse
}