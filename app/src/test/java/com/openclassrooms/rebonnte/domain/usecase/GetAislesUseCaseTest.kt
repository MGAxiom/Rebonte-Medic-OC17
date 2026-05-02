package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
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
    fun testInvoke() {
        every { repository.aisles } returns flowOf(Result.success(emptyList()))
        useCase()
        verify { repository.aisles }
    }
}
