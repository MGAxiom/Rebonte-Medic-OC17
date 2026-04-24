package com.openclassrooms.rebonnte.ui.state

import com.openclassrooms.rebonnte.domain.model.Medicine

sealed class MedicineUiState {
    data object Loading : MedicineUiState()
    data class Success(val medicines: List<Medicine>) : MedicineUiState()
    data class Error(val message: String) : MedicineUiState()
}
