package com.mrgndt.delivery.network.service

import com.google.android.gms.maps.model.LatLng
import com.mrgndt.delivery.model.Location
import com.mrgndt.delivery.network.data.ComputeRouteBody
import com.mrgndt.delivery.network.data.ComputeRouteResponse
import com.mrgndt.delivery.network.data.Point
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


class RoutesService(
    key: String
) {

    private val headerInterceptor = HeaderInterceptor(key)

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(headerInterceptor)
        .build()

    private val apiUrl = "https://routes.googleapis.com/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(apiUrl)
        .build()

    private val retrofitService: RoutesServiceInterface by lazy {
        retrofit.create(RoutesServiceInterface::class.java)
    }

    suspend fun computeRoute(
        startPoint: LatLng,
        endPoint: LatLng,
        stops: List<Location>

    ): ComputeRouteResponse {

        val body = ComputeRouteBody(
            origin = Point(startPoint.latitude, startPoint.longitude),
            destination = Point(endPoint.latitude, endPoint.longitude),
            intermediates = stops.map { Point(it.latitude, it.longitude) }
        )

        return retrofitService.computeRoute(body)

    }

}

interface RoutesServiceInterface {
    @POST("directions/v2:computeRoutes")
    @Headers(
        "X-Goog-FieldMask: routes.distanceMeters,routes.duration,routes.polyline.encodedPolyline,routes.optimized_intermediate_waypoint_index"
    )
    suspend fun computeRoute(@Body body: ComputeRouteBody): ComputeRouteResponse


}