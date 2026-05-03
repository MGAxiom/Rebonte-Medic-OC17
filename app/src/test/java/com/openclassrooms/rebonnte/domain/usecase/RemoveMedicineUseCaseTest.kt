package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RemoveMedicineUseCaseTest {

    private lateinit var useCase: RemoveMedicineUseCase
    private val repository: MedicineRepository = mockk()

    @Before
    fun setup() {
        useCase = RemoveMedicineUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository succeeds`() = runTest {
        val medicineName = "Aspirin"
        coEvery { repository.removeMedicine(medicineName) } returns Result.success(Unit)

        val result = useCase(medicineName)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should return failure when repository fails`() = runTest {
        val medicineName = "Aspirin"
        val exception = Exception("Failed to remove medicine")
        coEvery { repository.removeMedicine(medicineName) } returns Result.failure(exception)

        val result = useCase(medicineName)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
