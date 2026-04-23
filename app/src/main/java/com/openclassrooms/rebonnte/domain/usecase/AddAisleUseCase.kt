package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.AisleRepository

class AddAisleUseCase(private val repository: AisleRepository) {
    fun addRandomAisle() {
        repository.addRandomAisle()
    }
}