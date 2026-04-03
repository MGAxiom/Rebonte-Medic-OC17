package com.openclassrooms.rebonnte.domain.repository

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.Medicine
import kotlinx.coroutines.flow.StateFlow

interface MedicineRepository {
    val medicines: StateFlow<List<Medicine>>
    fun addMedicine(medicine: Medicine)
    fun removeMedicine(medicineName: String)
    fun updateStock(medicineName: String, increment: Boolean)
}
