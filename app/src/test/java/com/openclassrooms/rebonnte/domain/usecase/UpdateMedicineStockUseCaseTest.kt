package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateMedicineStockUseCaseTest {

    private lateinit var useCase: UpdateMedicineStockUseCase
    private val repository: MedicineRepository = mockk()

    @Before
    fun setup() {
        useCase = UpdateMedicineStockUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository succeeds`() = runTest {
        val medicineName = "Aspirin"
        val increment = true
        coEvery { repository.updateStock(medicineName, increment) } returns Result.success(Unit)

        val result = useCase(medicineName, increment)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        val medicineName = "Aspirin"
        val increment = false
        val exception = Exception("Failed to update stock")
        coEvery { repository.updateStock(medicineName, increment) } returns Result.failure(exception)

        val result = useCase(medicineName, increment)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
