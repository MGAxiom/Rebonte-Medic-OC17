package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.Aisle
import kotlinx.coroutines.flow.StateFlow

interface AisleRepository {
    val aisles: StateFlow<List<Aisle>>
    fun addRandomAisle()
}
