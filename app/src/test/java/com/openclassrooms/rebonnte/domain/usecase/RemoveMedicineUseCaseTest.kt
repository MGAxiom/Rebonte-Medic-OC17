package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun testInvoke() = runTest {
        coEvery { repository.removeMedicine("Med") } returns Result.success(Unit)
        val result = useCase("Med")
        assertTrue(result.isSuccess)
    }
}
