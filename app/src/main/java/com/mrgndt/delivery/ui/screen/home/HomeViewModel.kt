package com.mrgndt.delivery.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private val _locationFormState = MutableStateFlow(LocationFormState())
    val locationFormState = _locationFormState.asStateFlow()
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
//                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MiMunicipioApplication)
                HomeViewModel(
//                    ticketService = TicketService(application.baseContext,application.mainRepository,application::unauthorizedHandler),
//                    mainRepository = application.mainRepository,
//                    showToast = application::showToast,
//                    logout = application::logout
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

    fun updateLocationFormState(state: LocationFormState){
        _locationFormState.update {
            state
        }
    }

}