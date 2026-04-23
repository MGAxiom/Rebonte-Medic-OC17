package com.openclassrooms.rebonnte.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.SortType
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.utils.DateFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class MedicineRepositoryImpl(
    private val context: Context,
    private val firestore: FirebaseFirestore
) : MedicineRepository {

    private val medicineCollection = firestore.collection("medicines")

    override fun getMedicines(sortType: SortType, searchQuery: String): Flow<List<Medicine>> {
        var query: Query = medicineCollection

        if (searchQuery.isNotEmpty()) {
            query = query.whereGreaterThanOrEqualTo("name", searchQuery)
                .whereLessThanOrEqualTo("name", searchQuery + "\uf8ff")
        }

        query = when (sortType) {
            SortType.NAME -> query.orderBy(NAME_SORTING)
            SortType.STOCK -> query.orderBy(STOCK_SORTING)
            SortType.NONE -> {
                if (searchQuery.isNotEmpty()) {
                    query.orderBy(NAME_SORTING)
                } else {
                    query
                }
            }
        }

        return query.snapshots().map { snapshot ->
            snapshot.toObjects(Medicine::class.java)
        }
    }

    override fun addMedicine(medicine: Medicine) {
        val docRef = medicineCollection.document()
        val newMedicine = medicine.copy(id = docRef.id, histories = emptyList())
        docRef.set(newMedicine)
    }

    override fun removeMedicine(medicineName: String) {
        medicineCollection.whereEqualTo("name", medicineName).get().addOnSuccessListener { snapshot ->
            for (document in snapshot.documents) {
                document.reference.delete()
            }
        }
    }

    override fun updateStock(medicineName: String, increment: Boolean) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userName = currentUser?.displayName ?: "Unknown User"

        medicineCollection.whereEqualTo("name", medicineName).get().addOnSuccessListener { snapshot ->
            for (document in snapshot.documents) {
                val medicine = document.toObject(Medicine::class.java) ?: continue
                val newStock = if (increment) medicine.stock + 1 else (medicine.stock - 1).coerceAtLeast(0)
                
                val historyEntry = History(
                    id = UUID.randomUUID().toString(),
                    medicineName = medicine.name,
                    userId = userName,
                    date = DateFormatter.getCurrentFormattedDate(),
                    details = if (increment) 
                        context.getString(R.string.stock_incremented) else 
                            context.getString(R.string.stock_decremented)
                )

                document.reference.update(
                    "stock", newStock,
                    "histories", FieldValue.arrayUnion(historyEntry)
                )
            }
        }
    }
}

private const val STOCK_SORTING = "stock"
private const val NAME_SORTING = "name"