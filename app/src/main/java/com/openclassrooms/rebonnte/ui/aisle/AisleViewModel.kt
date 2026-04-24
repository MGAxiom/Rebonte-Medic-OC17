package com.openclassrooms.rebonnte.ui.aisle

import androidx.lifecycle.ViewModel
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.usecase.AddAisleUseCase
import com.openclassrooms.rebonnte.domain.usecase.GetAislesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class AisleViewModel(
    private val getAislesUseCase: GetAislesUseCase,
    private val addAisleUseCase: AddAisleUseCase
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val aisles: StateFlow<Result<List<Aisle>>> = getAislesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.success(emptyList())
        )

    fun addRandomAisle() {
        viewModelScope.launch {
            val result = addAisleUseCase()
            if (result.isFailure) {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}