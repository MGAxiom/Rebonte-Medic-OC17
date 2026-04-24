package com.openclassrooms.rebonnte.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await

class AisleRepositoryImpl(
    private val context: android.content.Context,
    private val firestore: FirebaseFirestore
) : AisleRepository {
    
    private val scope = CoroutineScope(Dispatchers.IO)
    private val aisleCollection = firestore.collection("aisles")

    override val aisles: Flow<Result<List<Aisle>>> = aisleCollection
        .snapshots()
        .map { snapshot ->
            Result.success(snapshot.toObjects(Aisle::class.java))
        }
        .catch {
            emit(Result.failure(Exception(context.getString(R.string.error_aisle_fetch_failed))))
        }

    override suspend fun addRandomAisle(): Result<Unit> {
        return try {
            val snapshot = aisleCollection.get().await()
            val currentSize = snapshot.size()
            val newAisleName = "Aisle ${currentSize + 1}"
            val docRef = aisleCollection.document()
            val aisle = Aisle(id = docRef.id, name = newAisleName)
            docRef.set(aisle).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception(context.getString(R.string.error_unknown)))
        }
    }
}