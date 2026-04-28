package com.openclassrooms.rebonnte.ui.screens.aisle

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.openclassrooms.rebonnte.domain.model.Aisle
import com.openclassrooms.rebonnte.ui.state.AisleUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class AisleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<AisleViewModel>(relaxed = true)

    @Test
    fun aisleScreen_displaysAisles() {
        val aisles = listOf(
            Aisle("Pharmacy"),
            Aisle("First Aid")
        )
        every { viewModel.uiState } returns MutableStateFlow(AisleUiState.Success(aisles))
        every { viewModel.error } returns MutableStateFlow(null)

        composeTestRule.setContent {
            AisleScreen(
                viewModel = viewModel,
                onAisleClick = {}
            )
        }

        composeTestRule.onNodeWithText("Pharmacy").assertIsDisplayed()
        composeTestRule.onNodeWithText("First Aid").assertIsDisplayed()
    }
}
