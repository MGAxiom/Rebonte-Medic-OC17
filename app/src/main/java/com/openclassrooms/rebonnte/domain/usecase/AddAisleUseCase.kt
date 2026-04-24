package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.AisleRepository

class AddAisleUseCase(private val repository: AisleRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.addRandomAisle()
    }
}