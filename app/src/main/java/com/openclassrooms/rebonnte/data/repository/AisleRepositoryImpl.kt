package com.openclassrooms.rebonnte.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AisleRepositoryImpl(private val firestore: FirebaseFirestore) : AisleRepository {
    
    private val scope = CoroutineScope(Dispatchers.IO)
    private val aisleCollection = firestore.collection("aisles")

    override val aisles: StateFlow<List<Aisle>> = aisleCollection
        .snapshots()
        .map { snapshot ->
            snapshot.toObjects(Aisle::class.java)
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun addRandomAisle() {
        val currentSize = aisles.value.size
        val newAisleName = "Aisle ${currentSize + 1}"
        val docRef = aisleCollection.document()
        val aisle = Aisle(id = docRef.id, name = newAisleName)
        docRef.set(aisle)
    }
}