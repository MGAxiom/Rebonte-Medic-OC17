package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository

class UpdateMedicineStockUseCase(private val repository: MedicineRepository) {
    operator fun invoke(medicineName: String, increment: Boolean) {
        repository.updateStock(medicineName, increment)
    }
}