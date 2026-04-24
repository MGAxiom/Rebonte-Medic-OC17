package com.openclassrooms.rebonnte.ui.screens.aisle

import androidx.lifecycle.ViewModel
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.usecase.AddAisleUseCase
import com.openclassrooms.rebonnte.domain.usecase.GetAislesUseCase
import com.openclassrooms.rebonnte.ui.state.AisleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map

class AisleViewModel(
    private val getAislesUseCase: GetAislesUseCase,
    private val addAisleUseCase: AddAisleUseCase
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val uiState: StateFlow<AisleUiState> = getAislesUseCase()
        .map { result ->
            result.fold(
                onSuccess = { AisleUiState.Success(it) },
                onFailure = { AisleUiState.Error(it.message ?: "Unknown error") }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AisleUiState.Loading
        )

    fun addRandomAisle() {
        viewModelScope.launch {
            addAisleUseCase().onFailure { error ->
                _error.value = error.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
