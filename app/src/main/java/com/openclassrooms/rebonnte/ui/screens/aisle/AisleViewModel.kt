package com.openclassrooms.rebonnte.ui.screens.aisle

import androidx.lifecycle.ViewModel
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import kotlinx.coroutines.flow.StateFlow

class AisleViewModel(private val repository: AisleRepository) : ViewModel() {
    val aisles: StateFlow<List<Aisle>> = repository.aisles

    fun addRandomAisle() {
        repository.addRandomAisle()
    }
}
