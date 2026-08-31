package com.mrgndt.delivery.network.data

import kotlinx.serialization.Serializable

@Serializable
data class AutoCompleteBody(
    val input: String,
    val languageCode: String = "es-419"
)

@Serializable
data class AutoCompleteResponse(
    val suggestions: List<Suggestion>
) {
    @Serializable
    data class Suggestion(
        val placePrediction: PlacePrediction
    ) {
        @Serializable
        data class PlacePrediction(
            val place: String,
            val placeId: String,
            val text: Text
        ) {
            @Serializable
            data class Text(
                val text: String
            )
        }
    }
}

@Serializable
data class PlaceDetailsResponse(
    val location: Location
) {
    data class Location(
        val latitude: Double,
        val longitude: Double
    )
}