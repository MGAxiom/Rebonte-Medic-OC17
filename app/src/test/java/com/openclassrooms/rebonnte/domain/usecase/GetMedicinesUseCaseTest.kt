package com.openclassrooms.rebonnte.domain.usecase

import com.openclassrooms.rebonnte.domain.model.SortType
import com.openclassrooms.rebonnte.domain.repository.MedicineRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
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
    fun testInvoke() {
        every {
            repository.getMedicines(
                any(),
                any()
            )
        } returns flowOf(Result.success(emptyList()))
        useCase(SortType.NONE, "")
        verify { repository.getMedicines(SortType.NONE, "") }
    }
}
