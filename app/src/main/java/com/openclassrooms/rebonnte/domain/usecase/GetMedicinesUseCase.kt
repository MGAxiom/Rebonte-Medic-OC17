package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.StateFlow

class GetMedicinesUseCase(private val repository: MedicineRepository) {
    operator fun invoke(): StateFlow<List<Medicine>> = repository.medicines
}