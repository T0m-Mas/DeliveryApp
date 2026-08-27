package com.mrgndt.delivery.ui.screen.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel(){
    private val _state = MutableStateFlow(
        HomeUiState()
    )

    val state = _state.asStateFlow()
}