package com.openclassrooms.rebonnte.ui.state

import com.openclassrooms.rebonnte.domain.model.Aisle

sealed class AisleUiState {
    data object Loading : AisleUiState()
    data class Success(val aisles: List<Aisle>) : AisleUiState()
    data class Error(val message: String) : AisleUiState()
}
