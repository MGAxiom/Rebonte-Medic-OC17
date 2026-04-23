package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository

class AddMedicineUseCase(private val repository: MedicineRepository) {
    operator fun invoke(medicine: Medicine) {
        repository.addMedicine(medicine)
    }
}