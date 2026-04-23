package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository

class RemoveMedicineUseCase(private val repository: MedicineRepository) {
    operator fun invoke(medicineName: String) {
        repository.removeMedicine(medicineName)
    }
}