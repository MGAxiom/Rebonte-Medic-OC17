package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.Aisle
import kotlinx.coroutines.flow.Flow

interface AisleRepository {
    val aisles: Flow<Result<List<Aisle>>>
    suspend fun addRandomAisle(): Result<Unit>
}
