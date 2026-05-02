package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun testInvoke() = runTest {
        coEvery { repository.updateStock("Med", true) } returns Result.success(Unit)
        val result = useCase("Med", true)
        assertTrue(result.isSuccess)
    }
}
