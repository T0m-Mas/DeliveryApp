package com.mrgndt.delivery.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.maps.model.LatLng
import com.mrgndt.delivery.DeliveryApplication
import com.mrgndt.delivery.data.MainRepository
import com.mrgndt.delivery.model.Location
import com.mrgndt.delivery.network.service.PlacesService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val placesService: PlacesService,
    private val mainRepository: MainRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private val _locationFormState = MutableStateFlow(LocationFormState())
    val locationFormState = _locationFormState.asStateFlow()

    private val _routeFormState = MutableStateFlow(RouteFormState())
    val routeFormState = _routeFormState.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    locations = mainRepository.getAllLocations()
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DeliveryApplication)
                HomeViewModel(
                    placesService = application.placesService,
                    mainRepository = application.mainRepository
                )
            }
        }
    }

    fun updateMode(mode: HomeUiState.Mode) {
        if (mode == HomeUiState.Mode.Idle && _state.value.mode == HomeUiState.Mode.NewRoute) {
            viewModelScope.launch {
                _state.update {
                    it.copy(
                        locations = mainRepository.getAllLocations()
                    )
                }
            }
            _routeFormState.update {
                RouteFormState()
            }
        }
        _state.update {
            it.copy(
                mode = mode
            )
        }
    }

    fun updateLocationFormState(state: LocationFormState) {
        _locationFormState.update {
            state
        }
        validateLocationForm()
    }

    fun updateLocationFormLatLng(latLng: LatLng) {
        _locationFormState.update {
            it.copy(
                latLng = latLng
            )
        }
        validateLocationForm()
    }

    fun processAutoComplete(search: String) {
        viewModelScope.launch {
            try {
                val response = placesService.autocomplete(search)

                _locationFormState.update {
                    it.copy(
                        addressSuggestions = response.suggestions.map { suggestion ->
                            AddressSuggestion(
                                label = suggestion.placePrediction.text.text,
                                placeId = suggestion.placePrediction.placeId
                            )
                        }
                    )
                }

            } catch (e: Exception) {
                Log.d("processAutoComplete", "$e")
            }
        }
    }

    fun processSelectSuggestion(placeId: String) {
        viewModelScope.launch {
            try {
                val response = placesService.getPlaceDetails(placeId)
                _locationFormState.update {
                    it.copy(
                        latLng = LatLng(
                            response.location.latitude,
                            response.location.longitude,
                        )
                    )
                }
                validateLocationForm()
            } catch (e: Exception) {
                Log.d("processSelectSuggestion", "$e")
            }
        }
    }

    private fun isLocationFormValid(): Boolean {
        return _locationFormState.value.latLng != null && _locationFormState.value.address.length >= 4
    }

    fun validateLocationForm() {
        _locationFormState.update {
            it.copy(
                formIsValid = isLocationFormValid()
            )
        }
    }

    fun saveLocation() {


        if (isLocationFormValid()) {


            var location = Location(
                latitude = _locationFormState.value.latLng!!.latitude,
                longitude = _locationFormState.value.latLng!!.longitude,
                label = if (_locationFormState.value.label == "") null else _locationFormState.value.label,
                address = _locationFormState.value.address,
            )

            if (_locationFormState.value.isGhostLocation) {
                _locationFormState.update { LocationFormState() }

                _state.update {
                    it.copy(
                        mode = HomeUiState.Mode.NewRoute,
                        locations = it.locations.plus(location)
                    )
                }
                _routeFormState.update {
                    it.copy(
                        stops = it.stops.plus(location)
                    )
                }
            } else if (_locationFormState.value.isEditing) {
                viewModelScope.launch {

                    location = Location(
                        id = _locationFormState.value.id!!,
                        latitude = _locationFormState.value.latLng!!.latitude,
                        longitude = _locationFormState.value.latLng!!.longitude,
                        label = if (_locationFormState.value.label == "") null else _locationFormState.value.label,
                        address = _locationFormState.value.address,
                    )

                    mainRepository.updateLocation(
                        location
                    )

                    _locationFormState.update { LocationFormState() }

                    _state.update {
                        it.copy(
                            mode = HomeUiState.Mode.Idle,
                            selectedLocation = location,
                            locations = it.locations.filter { loc -> loc.id != location.id }
                                .plus(location)
                        )
                    }

                }
            } else {
                viewModelScope.launch {
                    try {
                        val newLocationId = mainRepository.saveLocation(location)
                        val cameFromNewRoute = _locationFormState.value.canBeGhost

                        location = Location(
                            id = newLocationId,
                            latitude = _locationFormState.value.latLng!!.latitude,
                            longitude = _locationFormState.value.latLng!!.longitude,
                            label = if (_locationFormState.value.label == "") null else _locationFormState.value.label,
                            address = _locationFormState.value.address,
                        )

                        _locationFormState.update { LocationFormState() }
                        _state.update {
                            it.copy(
                                mode = if (cameFromNewRoute) HomeUiState.Mode.NewRoute else HomeUiState.Mode.Idle,
                                locations = it.locations.plus(location)
                            )
                        }

                        if (cameFromNewRoute) {
                            _routeFormState.update {
                                it.copy(
                                    stops = it.stops.plus(location)
                                )
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("saveLocation", "$e")
                    }
                }

            }

        }
    }

    fun setSelectedLocation(location: Location?) {
        _state.update {
            it.copy(
                selectedLocation = location
            )
        }
    }

    fun deleteSelectedLocation() {
        val location = _state.value.selectedLocation ?: return

        viewModelScope.launch {
            mainRepository.deleteLocation(location.id)
            _state.update {
                it.copy(
                    selectedLocation = null,
                    locations = it.locations.filter { loc -> loc.id != location.id }
                )
            }
        }
    }

    fun toggleSelectStop(location: Location) {
        val contains = _routeFormState.value.stops.contains(location)
        _routeFormState.update {
            it.copy(
                stops = if (contains) it.stops.filter { loc -> loc.id != location.id }
                else it.stops.plus(location)
            )
        }
    }

    fun startNewOptionalLocationMode() {
        _state.update {
            it.copy(
                mode = HomeUiState.Mode.LocationForm
            )
        }
        _locationFormState.update {
            LocationFormState(canBeGhost = true)
        }
    }

    fun suggestLocations(search: String) {
        _routeFormState.update {
            it.copy(
                stopsSuggestions = _state.value.locations.filter { location ->
                    location.label?.contains(search, ignoreCase = true) == true ||
                            location.address.contains(search, ignoreCase = true)
                }.take(10)
            )
        }

    }

}
