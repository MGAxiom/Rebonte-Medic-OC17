package com.openclassrooms.rebonnte.integration

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.snapshots
import com.openclassrooms.rebonnte.data.repository.MedicineRepositoryImpl
import com.openclassrooms.rebonnte.domain.usecase.AddMedicineUseCase
import com.openclassrooms.rebonnte.domain.usecase.GetMedicinesUseCase
import com.openclassrooms.rebonnte.domain.usecase.RemoveMedicineUseCase
import com.openclassrooms.rebonnte.domain.usecase.UpdateMedicineStockUseCase
import com.openclassrooms.rebonnte.ui.screens.medicine.MedicineViewModel
import com.openclassrooms.rebonnte.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.verify

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineFlowIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context: Context = mockk(relaxed = true)
    private val firestore: FirebaseFirestore = mockk()
    private val collectionReference: CollectionReference = mockk(relaxed = true)
    
    private lateinit var repository: MedicineRepositoryImpl
    private lateinit var getMedicinesUseCase: GetMedicinesUseCase
    private lateinit var addMedicineUseCase: AddMedicineUseCase
    private lateinit var updateMedicineStockUseCase: UpdateMedicineStockUseCase
    private lateinit var removeMedicineUseCase: RemoveMedicineUseCase
    private lateinit var viewModel: MedicineViewModel

    @Before
    fun setup() {
        mockkStatic("com.google.firebase.firestore.FirestoreKt")
        every { firestore.collection("medicines") } returns collectionReference
        every { collectionReference.snapshots() } returns flowOf(mockk(relaxed = true))

        repository = MedicineRepositoryImpl(context, firestore)
        getMedicinesUseCase = GetMedicinesUseCase(repository)
        addMedicineUseCase = AddMedicineUseCase(repository)
        updateMedicineStockUseCase = UpdateMedicineStockUseCase(repository)
        removeMedicineUseCase = RemoveMedicineUseCase(repository)
        
        viewModel = MedicineViewModel(
            getMedicinesUseCase,
            addMedicineUseCase,
            updateMedicineStockUseCase,
            removeMedicineUseCase
        )
    }

    @Test
    fun viewModel_filterByName_reloadsData() = runTest {
        viewModel.filterByName("Aspi")
        advanceUntilIdle()
        // We verify that firestore.collection("medicines") was accessed during initialization and reload
        verify { firestore.collection("medicines") }
    }
}
