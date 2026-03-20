package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.Medicine
import kotlinx.coroutines.flow.StateFlow

interface MedicineRepository {
    val medicines: StateFlow<List<Medicine>>
    fun addRandomMedicine(aisles: List<Aisle>)
    fun updateStock(medicineName: String, increment: Boolean)
}
