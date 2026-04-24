package com.openclassrooms.rebonnte.ui.screens.medicine

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.SortType
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.ui.state.MedicineUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicineViewModel(
    private val getMedicinesUseCase: com.openclassrooms.rebonnte.domain.usecase.GetMedicinesUseCase,
    private val addMedicineUseCase: com.openclassrooms.rebonnte.domain.usecase.AddMedicineUseCase,
    private val updateMedicineStockUseCase: com.openclassrooms.rebonnte.domain.usecase.UpdateMedicineStockUseCase,
    private val removeMedicineUseCase: com.openclassrooms.rebonnte.domain.usecase.RemoveMedicineUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _sortType = MutableStateFlow(SortType.NONE)

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val detailListState = LazyListState()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MedicineUiState> = combine(
        _searchQuery,
        _sortType
    ) { query, sortType ->
        Pair(query, sortType)
    }.flatMapLatest { (query, sortType) ->
        getMedicinesUseCase(sortType, query).map { result ->
            result.fold(
                onSuccess = { MedicineUiState.Success(it) },
                onFailure = { MedicineUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, MedicineUiState.Loading)

    fun filterByName(name: String) {
        _searchQuery.value = name
    }

    fun sortByNone() {
        _sortType.value = SortType.NONE
    }

    fun sortByName() {
        _sortType.value = SortType.NAME
    }

    fun sortByStock() {
        _sortType.value = SortType.STOCK
    }

    fun addMedicine(medicine: Medicine) {
        viewModelScope.launch {
            addMedicineUseCase(medicine).onFailure { error ->
                _error.value = error.message
            }
        }
    }

    fun updateStock(medicineName: String, increment: Boolean) {
        viewModelScope.launch {
            updateMedicineStockUseCase(medicineName, increment).onFailure { error ->
                _error.value = error.message
            }
        }
    }

    fun removeMedicine(medicineName: String) {
        viewModelScope.launch {
            removeMedicineUseCase(medicineName).onFailure { error ->
                _error.value = error.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
