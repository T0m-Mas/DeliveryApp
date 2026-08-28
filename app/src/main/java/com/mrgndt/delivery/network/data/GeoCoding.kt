package com.mrgndt.delivery.network.data

import kotlinx.serialization.Serializable

@Serializable
data class GeoCodingResponse(
    val results: List<GeoCodingResult>
)

@Serializable
data class GeoCodingResult(
    val address_components: List<AddressComponent>
)

@Serializable
data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String> = listOf()
)