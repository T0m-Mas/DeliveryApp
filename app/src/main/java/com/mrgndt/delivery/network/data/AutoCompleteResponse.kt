package com.adox.worktrack.network.service.places.api.data

import kotlinx.serialization.Serializable

@Serializable
data class AutoCompleteResponse(
  val predictions: List<Prediction>
)

@Serializable
data class Prediction(
  val description: String,
  val place_id: String,
)
