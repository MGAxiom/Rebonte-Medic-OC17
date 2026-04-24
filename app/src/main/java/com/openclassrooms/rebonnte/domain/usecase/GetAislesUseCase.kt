package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import kotlinx.coroutines.flow.Flow

class GetAislesUseCase(private val repository: AisleRepository) {
    operator fun invoke(): Flow<Result<List<Aisle>>> = repository.aisles
}