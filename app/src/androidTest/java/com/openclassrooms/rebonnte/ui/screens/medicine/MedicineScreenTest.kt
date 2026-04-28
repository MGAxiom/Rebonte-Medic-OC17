package com.openclassrooms.rebonnte.ui.screens.medicine

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.ui.state.MedicineUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class MedicineScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<MedicineViewModel>(relaxed = true)

    @Test
    fun medicineScreen_displaysMedicines() {
        val medicines = listOf(
            Medicine(name = "Paracetamol", stock = 10),
            Medicine(name = "Ibuprofen", stock = 5)
        )
        every { viewModel.uiState } returns MutableStateFlow(MedicineUiState.Success(medicines))
        every { viewModel.error } returns MutableStateFlow(null)

        composeTestRule.setContent {
            MedicineScreen(
                viewModel = viewModel,
                onDetailClick = {}
            )
        }

        composeTestRule.onNodeWithText("Paracetamol").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stock: 10").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ibuprofen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stock: 5").assertIsDisplayed()
    }
}
