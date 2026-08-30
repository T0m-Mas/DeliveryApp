package com.mrgndt.delivery.network.service

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val apiKey:String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Customize the request by adding global headers
        val requestWithHeaders = originalRequest.newBuilder()
            .header("X-Goog-Api-Key",apiKey)
            .build()

        return chain.proceed(requestWithHeaders)
    }
}