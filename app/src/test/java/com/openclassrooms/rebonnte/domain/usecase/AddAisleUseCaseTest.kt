package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddAisleUseCaseTest {

    private lateinit var useCase: AddAisleUseCase
    private val repository: AisleRepository = mockk()

    @Before
    fun setup() {
        useCase = AddAisleUseCase(repository)
    }

    @Test
    fun `invoke should call repository addRandomAisle`() = runTest {
        coEvery { repository.addRandomAisle() } returns Result.success(Unit)

        val result = useCase()

        coVerify { repository.addRandomAisle() }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke should propagate failure from repository`() = runTest {
        val exception = Exception("Failed to add random aisle")
        coEvery { repository.addRandomAisle() } returns Result.failure(exception)

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
