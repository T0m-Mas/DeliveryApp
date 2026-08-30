package com.mrgndt.delivery.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mrgndt.delivery.DeliveryApplication
import com.mrgndt.delivery.network.service.PlacesService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val placesService: PlacesService
) : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private val _locationFormState = MutableStateFlow(LocationFormState())
    val locationFormState = _locationFormState.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DeliveryApplication)
                HomeViewModel(
                    placesService = application.placesService
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
    }

    fun processAutoComplete(search: String) {
        viewModelScope.launch {
            try {
                val response = placesService.autocomplete(search)
            } catch (e: Exception) {

            }
        }
    }

}