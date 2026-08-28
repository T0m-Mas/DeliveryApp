package com.mrgndt.delivery.ui.screen.home

import com.google.android.gms.maps.model.LatLng
import com.mrgndt.delivery.model.Location
import com.mrgndt.delivery.model.Route

data class HomeUiState(
    val mode: Mode = Mode.Idled,
    val route: Route? = null,
    val selectedLocations: List<Location> = emptyList()

){
    enum class Mode {
        NewRoute,
        NewLocation,
        Route,
        Idled
    }
}

data class LocationFormState(
    val label:String = "",
    val address: String = "",
    val latLng: LatLng? = null,
    val isEditing: Boolean = false,
)