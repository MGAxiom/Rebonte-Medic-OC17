package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddMedicineUseCaseTest {

    private lateinit var useCase: AddMedicineUseCase
    private val repository: MedicineRepository = mockk()

    @Before
    fun setup() {
        useCase = AddMedicineUseCase(repository)
    }

    @Test
    fun testInvoke() = runTest {
        val medicine = Medicine(name = "Test")
        coEvery { repository.addMedicine(medicine) } returns Result.success(Unit)
        val result = useCase(medicine)
        assertTrue(result.isSuccess)
    }
}
