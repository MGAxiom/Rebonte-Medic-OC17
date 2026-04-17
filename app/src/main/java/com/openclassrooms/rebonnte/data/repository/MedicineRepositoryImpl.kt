package com.openclassrooms.rebonnte.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.utils.DateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Random
import java.util.UUID

class MedicineRepositoryImpl(private val context: Context) : MedicineRepository {
    private val _medicines = MutableStateFlow<List<Medicine>>(emptyList())
    override val medicines: StateFlow<List<Medicine>> = _medicines.asStateFlow()

    override fun addMedicine(medicine: Medicine) {
        val currentList = _medicines.value.toMutableList()
        currentList.add(
            Medicine(
                name = medicine.name,
                stock = medicine.stock,
                nameAisle = medicine.nameAisle,
                histories = emptyList()
            )
        )
        _medicines.value = currentList
    }

    override fun removeMedicine(medicineName: String) {
        val currentList = _medicines.value.toMutableList()
        currentList.removeIf { it.name == medicineName }
        _medicines.value = currentList
    }

    override fun updateStock(medicineName: String, increment: Boolean) {
        val currentList = _medicines.value.toMutableList()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val index = currentList.indexOfFirst { it.name == medicineName }
        if (index != -1) {
            val medicine = currentList[index]
            val newStock = if (increment) medicine.stock + 1 else (medicine.stock - 1).coerceAtLeast(0)
            
            val newHistories = medicine.histories.toMutableList()
            currentUser?.let {
                newHistories.add(
                    History(
                        medicineName = medicine.name,
                        userId = it.displayName.toString(),
                        date = DateFormatter.getCurrentFormattedDate(),
                        details = if (increment) 
                            context.getString(R.string.stock_incremented) else 
                                context.getString(R.string.stock_decremented)
                    )
                )
            }

            currentList[index] = medicine.copy(stock = newStock, histories = newHistories)
            _medicines.value = currentList
        }
    }
}
