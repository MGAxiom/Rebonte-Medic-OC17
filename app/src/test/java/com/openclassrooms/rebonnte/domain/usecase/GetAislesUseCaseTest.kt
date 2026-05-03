package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.repository.AisleRepository
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

class GetAislesUseCaseTest {

    private lateinit var useCase: GetAislesUseCase
    private val repository: AisleRepository = mockk()

    @Before
    fun setup() {
        useCase = GetAislesUseCase(repository)
    }

    @Test
    fun `invoke should call repository and return aisles`() = runTest {
        val aisles = listOf(Aisle(name = "Aisle 1"))
        every { repository.aisles } returns flowOf(Result.success(aisles))

        val resultFlow = useCase()
        val result = resultFlow.first()

        verify { repository.aisles }
        assertTrue(result.isSuccess)
        assertEquals(aisles, result.getOrNull())
    }

    @Test
    fun `invoke should propagate failure from repository`() = runTest {
        val exception = Exception("Error fetching aisles")
        every { repository.aisles } returns flowOf(Result.failure(exception))

        val resultFlow = useCase()
        val result = resultFlow.first()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
