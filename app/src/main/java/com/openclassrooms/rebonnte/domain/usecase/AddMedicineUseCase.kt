package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository

class AddMedicineUseCase(private val repository: MedicineRepository) {
    suspend operator fun invoke(medicine: Medicine): Result<Unit> {
        return repository.addMedicine(medicine)
    }
}