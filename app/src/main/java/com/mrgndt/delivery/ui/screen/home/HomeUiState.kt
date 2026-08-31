package com.mrgndt.delivery.ui.screen.home

import com.google.android.gms.maps.model.LatLng
import com.mrgndt.delivery.model.Location

data class HomeUiState(
    val mode: Mode = Mode.Idled,
    val locations: List<Location> = emptyList(),
    val selectedLocation: Location? = null,
) {
    enum class Mode {
        NewRoute,
        NewLocation,
        Route,
        Idled
    }
}

data class LocationFormState(
    val id: Int? = null,
    val label: String = "",
    val address: String = "",
    val latLng: LatLng? = null,
    val isEditing: Boolean = false,
    val canSave: Boolean = false,
    val addressSuggestions: List<AddressSuggestions> = emptyList()
) {
    data class AddressSuggestions(
        val label: String,
        val placeId: String
    )
}
