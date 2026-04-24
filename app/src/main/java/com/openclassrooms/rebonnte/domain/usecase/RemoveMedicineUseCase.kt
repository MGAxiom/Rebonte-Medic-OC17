package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository

class RemoveMedicineUseCase(private val repository: MedicineRepository) {
    suspend operator fun invoke(medicineName: String): Result<Unit> {
        return repository.removeMedicine(medicineName)
    }
}