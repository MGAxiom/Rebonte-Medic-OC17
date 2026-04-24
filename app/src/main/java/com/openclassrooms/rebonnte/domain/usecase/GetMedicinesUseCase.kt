package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.SortType
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.Flow

class GetMedicinesUseCase(private val repository: MedicineRepository) {
    operator fun invoke(sortType: SortType = SortType.NONE, query: String = ""): Flow<Result<List<Medicine>>> =
        repository.getMedicines(sortType, query)
}
