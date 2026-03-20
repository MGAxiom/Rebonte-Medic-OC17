package com.openclassrooms.rebonnte.data.repository

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AisleRepositoryImpl : AisleRepository {
    private val _aisles = MutableStateFlow<List<Aisle>>(listOf(Aisle("Main Aisle")))
    override val aisles: StateFlow<List<Aisle>> = _aisles.asStateFlow()

    override fun addRandomAisle() {
        val currentList = _aisles.value.toMutableList()
        currentList.add(Aisle("Aisle ${currentList.size + 1}"))
        _aisles.value = currentList
    }
}
