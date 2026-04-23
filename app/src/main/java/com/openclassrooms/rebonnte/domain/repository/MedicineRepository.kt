package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MedicineRepository {
    fun getMedicines(sortType: SortType, searchQuery: String): Flow<List<Medicine>>
    fun addMedicine(medicine: Medicine)
    fun removeMedicine(medicineName: String)
    fun updateStock(medicineName: String, increment: Boolean)
}
