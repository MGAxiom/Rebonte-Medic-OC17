package com.openclassrooms.rebonnte.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.History
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import com.openclassrooms.rebonnte.utils.DateFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

class MedicineRepositoryImpl(
    private val context: Context,
    private val firestore: FirebaseFirestore
) : MedicineRepository {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val medicineCollection = firestore.collection("medicines")

    override val medicines: StateFlow<List<Medicine>> = medicineCollection
        .snapshots()
        .map { snapshot ->
            snapshot.toObjects(Medicine::class.java)
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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