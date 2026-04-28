package com.openclassrooms.rebonnte.ui.screens.aisle

import app.cash.turbine.test
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.domain.usecase.AddAisleUseCase
import com.openclassrooms.rebonnte.domain.usecase.GetAislesUseCase
import com.openclassrooms.rebonnte.ui.state.AisleUiState
import com.openclassrooms.rebonnte.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AisleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AisleViewModel
    private val getAislesUseCase: GetAislesUseCase = mockk()
    private val addAisleUseCase: AddAisleUseCase = mockk()
    private val aislesFlow = MutableStateFlow<Result<List<Aisle>>>(Result.success(emptyList()))

    @Before
    fun setup() {
        every { getAislesUseCase() } returns aislesFlow
        viewModel = AisleViewModel(getAislesUseCase, addAisleUseCase)
    }

    @Test
    fun testUiStateSuccess() = runTest {
        viewModel.uiState.test {
            val first = awaitItem()
            if (first is AisleUiState.Loading) {
                assertTrue(awaitItem() is AisleUiState.Success)
            } else {
                assertTrue(first is AisleUiState.Success)
            }

            val aisles = listOf(Aisle("1", "Aisle 1"))
            aislesFlow.value = Result.success(aisles)
            val state = awaitItem()
            assertTrue(state is AisleUiState.Success)
            assertEquals(aisles, (state as AisleUiState.Success).aisles)
        }
    }

    @Test
    fun testUiStateError() = runTest {
        viewModel.uiState.test {
            skipItems(1) 
            val errorMessage = "Error fetching aisles"
            aislesFlow.value = Result.failure(Exception(errorMessage))
            val state = awaitItem()
            assertTrue(state is AisleUiState.Error)
            assertEquals(errorMessage, (state as AisleUiState.Error).message)
        }
    }

    @Test
    fun testAddRandomAisle() = runTest {
        coEvery { addAisleUseCase() } returns Result.success(Unit)
        viewModel.addRandomAisle()
        coVerify { addAisleUseCase() }
    }
}
