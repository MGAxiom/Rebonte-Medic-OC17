package com.openclassrooms.rebonnte.ui.medicine

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MedicineViewModel(private val repository: MedicineRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _sortType = MutableStateFlow(SortType.NONE)

    val detailListState = LazyListState()

    val medicines: StateFlow<List<Medicine>> = combine(
        repository.medicines,
        _searchQuery,
        _sortType
    ) { medicines, query, sortType ->
        var filtered = if (query.isEmpty()) {
            medicines
        } else {
            medicines.filter { it.name.contains(query, ignoreCase = true) }
        }

        when (sortType) {
            SortType.NAME -> filtered = filtered.sortedBy { it.name }
            SortType.STOCK -> filtered = filtered.sortedBy { it.stock }
            SortType.NONE -> { /* keep as is */ }
        }
        filtered
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addMedicine(medicine: Medicine) {
        repository.addMedicine(medicine)
    }

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

    fun updateStock(medicineName: String, increment: Boolean) {
        repository.updateStock(medicineName, increment)
    }

    fun removeMedicine(medicineName: String) {
        repository.removeMedicine(medicineName)
    }


    enum class SortType {
        NONE, NAME, STOCK
    }
}
