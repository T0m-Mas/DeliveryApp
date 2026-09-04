package com.mrgndt.delivery.ui.screen.home

import com.google.android.gms.maps.model.LatLng
import com.mrgndt.delivery.model.Location

data class HomeUiState(
    val mode: Mode = Mode.Idle,
    val locations: List<Location> = emptyList(),
    val selectedLocation: Location? = null,
) {
    enum class Mode {
        NewRoute,
        LocationForm,
        Route,
        Idle
    }
}

data class LocationFormState(
    val id: Long? = null,
    val label: String = "",
    val address: String = "",
    val latLng: LatLng? = null,
    val isEditing: Boolean = false,
    val formIsValid: Boolean = false,
    val addressSuggestions: List<AddressSuggestion> = emptyList(),
    val canBeGhost: Boolean = false,
    val isGhostLocation: Boolean = false
)

data class AddressSuggestion(
    val label: String,
    val placeId: String
)

data class RouteFormState(
    val id: Long? = null,
    val stops: List<Location> = emptyList(),
    val stage: Stage = Stage.StopsSelection,
    val stopsSuggestions: List<Location> = emptyList(),
    val startPoint: Location? = null,
    val endPoint: Location? = null,
    val addressSuggestions: List<AddressSuggestion> = emptyList()
) {
    enum class Stage {
        StopsSelection,
        StartNEndSelection,
    }
}
