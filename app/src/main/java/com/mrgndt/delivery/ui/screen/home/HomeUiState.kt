package com.mrgndt.delivery.ui.screen.home

import com.mrgndt.delivery.model.Location
import com.mrgndt.delivery.model.Route

data class HomeUiState(
    val mode: Mode = Mode.Idled,
    val route: Route? = null,
    val selectedLocations: List<Location> = emptyList(),

){
    enum class Mode {
        NewRoute,
        NewLocation,
        Route,
        Idled
    }
}
