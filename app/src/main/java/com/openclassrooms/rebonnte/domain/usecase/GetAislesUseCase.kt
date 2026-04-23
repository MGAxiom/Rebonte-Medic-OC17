package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import kotlinx.coroutines.flow.StateFlow

class GetAislesUseCase(private val repository: AisleRepository) {
    operator fun invoke(): StateFlow<List<Aisle>> = repository.aisles
}