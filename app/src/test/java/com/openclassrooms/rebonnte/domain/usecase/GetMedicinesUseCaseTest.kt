package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.SortType
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMedicinesUseCaseTest {

    private lateinit var useCase: GetMedicinesUseCase
    private val repository: MedicineRepository = mockk()

    @Before
    fun setup() {
        useCase = GetMedicinesUseCase(repository)
    }

    @Test
    fun `invoke should call repository with correct parameters`() = runTest {
        val sortType = SortType.NAME
        val query = "Aspirin"
        val medicines = listOf(Medicine(name = "Aspirin"))
        every { repository.getMedicines(sortType, query) } returns flowOf(Result.success(medicines))

        val resultFlow = useCase(sortType, query)
        val result = resultFlow.first()

        verify { repository.getMedicines(sortType, query) }
        assertTrue(result.isSuccess)
        assertEquals(medicines, result.getOrNull())
    }

    @Test
    fun `invoke should propagate failure from repository`() = runTest {
        val sortType = SortType.STOCK
        val query = ""
        val exception = Exception("Network error")
        every { repository.getMedicines(sortType, query) } returns flowOf(Result.failure(exception))

        val resultFlow = useCase(sortType, query)
        val result = resultFlow.first()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
