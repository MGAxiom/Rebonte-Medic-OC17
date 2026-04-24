package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository

class UpdateMedicineStockUseCase(private val repository: MedicineRepository) {
    suspend operator fun invoke(medicineName: String, increment: Boolean): Result<Unit> {
        return repository.updateStock(medicineName, increment)
    }
}