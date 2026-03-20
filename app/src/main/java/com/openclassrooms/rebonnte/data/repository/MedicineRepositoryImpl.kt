package com.openclassrooms.rebonnte.data.repository

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import java.util.Random

class MedicineRepositoryImpl : MedicineRepository {
    private val _medicines = MutableStateFlow<List<Medicine>>(emptyList())
    override val medicines: StateFlow<List<Medicine>> = _medicines.asStateFlow()

    override fun addRandomMedicine(aisles: List<Aisle>) {
        if (aisles.isEmpty()) return
        val currentList = _medicines.value.toMutableList()
        currentList.add(
            Medicine(
                name = "Medicine ${currentList.size + 1}",
                stock = Random().nextInt(100),
                nameAisle = aisles[Random().nextInt(aisles.size)].name,
                histories = emptyList()
            )
        )
        _medicines.value = currentList
    }

    override fun updateStock(medicineName: String, increment: Boolean) {
        val currentList = _medicines.value.toMutableList()
        val index = currentList.indexOfFirst { it.name == medicineName }
        if (index != -1) {
            val medicine = currentList[index]
            val newStock = if (increment) medicine.stock + 1 else (medicine.stock - 1).coerceAtLeast(0)
            
            val newHistories = medicine.histories.toMutableList()
            newHistories.add(
                History(
                    medicineName = medicine.name,
                    userId = "user_id_placeholder",
                    date = Date().toString(),
                    details = if (increment) "Stock incremented" else "Stock decremented"
                )
            )
            
            currentList[index] = medicine.copy(stock = newStock, histories = newHistories)
            _medicines.value = currentList
        }
    }
}
