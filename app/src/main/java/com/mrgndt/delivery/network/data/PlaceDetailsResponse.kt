package com.adox.worktrack.network.service.places.api.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetailsResponse(
  val result: Result,
)

@Serializable
data class Result(
  @SerializedName("formatted_address")
  val formattedAddress: String,
  val geometry: Geometry,
  val name: String,
)

@Serializable
data class Geometry(
  val location: Location,
)

@Serializable
data class Location(
  val lat: Double,
  val lng: Double,
)




