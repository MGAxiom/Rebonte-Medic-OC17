package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.repository.AisleRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    fun testInvoke() = runTest {
        coEvery { repository.addRandomAisle() } returns Result.success(Unit)
        val result = useCase()
        assertTrue(result.isSuccess)
    }
}
