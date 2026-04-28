package com.openclassrooms.rebonnte.ui.screens.medicine

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.SortType
import com.openclassrooms.rebonnte.domain.usecase.AddMedicineUseCase
import com.openclassrooms.rebonnte.domain.usecase.GetMedicinesUseCase
import com.openclassrooms.rebonnte.domain.usecase.RemoveMedicineUseCase
import com.openclassrooms.rebonnte.domain.usecase.UpdateMedicineStockUseCase
import com.openclassrooms.rebonnte.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MedicineViewModel
    private val getMedicinesUseCase: GetMedicinesUseCase = mockk()
    private val addMedicineUseCase: AddMedicineUseCase = mockk()
    private val updateMedicineStockUseCase: UpdateMedicineStockUseCase = mockk()
    private val removeMedicineUseCase: RemoveMedicineUseCase = mockk()
    private val medicinesFlow = MutableStateFlow<Result<List<Medicine>>>(Result.success(emptyList()))

    @Before
    fun setup() {
        every { getMedicinesUseCase(any(), any()) } returns medicinesFlow
        viewModel = MedicineViewModel(getMedicinesUseCase, addMedicineUseCase, updateMedicineStockUseCase, removeMedicineUseCase)
    }

    @Test
    fun testFilterByName() = runTest {
        val job = launch { viewModel.uiState.collect {} }
        viewModel.filterByName("Aspi")
        advanceUntilIdle()
        verify { getMedicinesUseCase(any(), "Aspi") }
        job.cancel()
    }

    @Test
    fun testSortByName() = runTest {
        val job = launch { viewModel.uiState.collect {} }
        viewModel.sortByName()
        advanceUntilIdle()
        verify { getMedicinesUseCase(SortType.NAME, any()) }
        job.cancel()
    }

    @Test
    fun testAddMedicine() = runTest {
        val med = Medicine(name = "New")
        coEvery { addMedicineUseCase(any()) } returns Result.success(Unit)
        viewModel.addMedicine(med)
        advanceUntilIdle()
        coVerify { addMedicineUseCase(med) }
    }

    @Test
    fun testUpdateStock() = runTest {
        coEvery { updateMedicineStockUseCase(any(), any()) } returns Result.success(Unit)
        viewModel.updateStock("Med", true)
        advanceUntilIdle()
        coVerify { updateMedicineStockUseCase("Med", true) }
    }

    @Test
    fun testRemoveMedicine() = runTest {
        coEvery { removeMedicineUseCase(any()) } returns Result.success(Unit)
        viewModel.removeMedicine("Med")
        advanceUntilIdle()
        coVerify { removeMedicineUseCase("Med") }
    }

    @Test
    fun testErrorHandling() = runTest {
        val errorMsg = "Fail"
        coEvery { removeMedicineUseCase(any()) } returns Result.failure(Exception(errorMsg))
        viewModel.removeMedicine("Med")
        advanceUntilIdle()
        assertEquals(errorMsg, viewModel.error.value)
        viewModel.clearError()
        assertEquals(null, viewModel.error.value)
    }
}
