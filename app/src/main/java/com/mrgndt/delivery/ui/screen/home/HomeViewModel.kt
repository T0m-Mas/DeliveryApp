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

    fun processAutoComplete(search: String) {
        viewModelScope.launch {
            try {
                val response = placesService.autocomplete(search)

                _locationFormState.update {
                    it.copy(
                        addressSuggestions = response.suggestions.map { suggestion ->
                            LocationFormState.AddressSuggestions(
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

    private fun canSave(): Boolean {
        return _locationFormState.value.latLng != null && _locationFormState.value.address.length >= 4
    }

    fun validateLocationForm() {
        _locationFormState.update {
            it.copy(
                canSave = canSave()
            )
        }
    }

    fun saveLocation() {


        if (canSave()) {


            var location = Location(
                latitude = _locationFormState.value.latLng!!.latitude,
                longitude = _locationFormState.value.latLng!!.longitude,
                label = _locationFormState.value.label,
                address = _locationFormState.value.address,
            )

            if (_locationFormState.value.isEditing) {
                viewModelScope.launch {
//                    mainRepository.updateLocation(
//                        Location(
//                            latitude = _locationFormState.value.latLng!!.latitude,
//                            longitude = _locationFormState.value.latLng!!.longitude,
//                            label = _locationFormState.value.label,
//                            address = _locationFormState.value.address,
//                        )
//                    )
                }
            } else {
                viewModelScope.launch {
                    try {
                        val newLocationId = mainRepository.saveLocation(location)

                        location = Location(
                            id = newLocationId,
                            latitude = _locationFormState.value.latLng!!.latitude,
                            longitude = _locationFormState.value.latLng!!.longitude,
                            label = _locationFormState.value.label,
                            address = _locationFormState.value.address,
                        )

                        _locationFormState.update { LocationFormState() }
                        _state.update {
                            it.copy(
                                mode = HomeUiState.Mode.Idled,
                                locations = it.locations.plus(location)
                            )
                        }
                    } catch (e: Exception) {
                        Log.d("saveLocation", "$e")
                    }
                }

            }

        }
    }

}