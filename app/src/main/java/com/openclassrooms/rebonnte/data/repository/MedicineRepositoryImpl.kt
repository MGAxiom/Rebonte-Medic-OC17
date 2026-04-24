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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class MedicineRepositoryImpl(
    private val context: Context,
    private val firestore: FirebaseFirestore
) : MedicineRepository {

    private val medicineCollection = firestore.collection("medicines")

    override fun getMedicines(sortType: SortType, searchQuery: String): Flow<Result<List<Medicine>>> {
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
            Result.success(snapshot.toObjects(Medicine::class.java))
        }.catch { e ->
            emit(Result.failure(Exception(context.getString(R.string.error_medicine_fetch_failed))))
        }
    }

    override suspend fun addMedicine(medicine: Medicine): Result<Unit> {
        return try {
            val docRef = medicineCollection.document()
            val newMedicine = medicine.copy(id = docRef.id, histories = emptyList())
            docRef.set(newMedicine).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(context.getString(R.string.error_medicine_add_failed)))
        }
    }

    override suspend fun removeMedicine(medicineName: String): Result<Unit> {
        return try {
            val snapshot = medicineCollection.whereEqualTo("name", medicineName).get().await()
            for (document in snapshot.documents) {
                document.reference.delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(context.getString(R.string.error_medicine_remove_failed)))
        }
    }

    override suspend fun updateStock(medicineName: String, increment: Boolean): Result<Unit> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userName = currentUser?.displayName ?: "Unknown User"

            val snapshot = medicineCollection.whereEqualTo("name", medicineName).get().await()
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
                ).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(context.getString(R.string.error_medicine_update_failed)))
        }
    }
}

private const val STOCK_SORTING = "stock"
private const val NAME_SORTING = "name"