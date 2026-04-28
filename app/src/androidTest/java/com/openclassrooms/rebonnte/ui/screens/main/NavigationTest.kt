package com.openclassrooms.rebonnte.ui.screens.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.openclassrooms.rebonnte.ui.screens.aisle.AisleViewModel
import com.openclassrooms.rebonnte.ui.screens.login.LoginViewModel
import com.openclassrooms.rebonnte.ui.screens.medicine.MedicineViewModel
import com.openclassrooms.rebonnte.ui.state.AisleUiState
import com.openclassrooms.rebonnte.ui.state.MedicineUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val loginViewModel = mockk<LoginViewModel>(relaxed = true)
    private val aisleViewModel = mockk<AisleViewModel>(relaxed = true)
    private val medicineViewModel = mockk<MedicineViewModel>(relaxed = true)

    @Test
    fun navigation_startAtAisleScreen() {
        setupMocks()

        composeTestRule.setContent {
            MainScreen(
                loginViewModel = loginViewModel,
                aisleViewModel = aisleViewModel,
                medicineViewModel = medicineViewModel
            )
        }

        composeTestRule.onNodeWithText("Rebonnte").assertIsDisplayed()
    }

    @Test
    fun navigation_clickMedicineTab_navigatesToMedicineScreen() {
        setupMocks()

        composeTestRule.setContent {
            MainScreen(
                loginViewModel = loginViewModel,
                aisleViewModel = aisleViewModel,
                medicineViewModel = medicineViewModel
            )
        }

        composeTestRule.onNodeWithText("Medicine").performClick()

        composeTestRule.onNodeWithText("Medicines").assertIsDisplayed()
    }

    @Test
    fun navigation_clickAccountTab_navigatesToProfileScreen() {
        setupMocks()

        composeTestRule.setContent {
            MainScreen(
                loginViewModel = loginViewModel,
                aisleViewModel = aisleViewModel,
                medicineViewModel = medicineViewModel
            )
        }

        composeTestRule.onNodeWithText("Account").performClick()

        composeTestRule.onNodeWithText("Account").assertIsDisplayed()
    }

    private fun setupMocks() {
        every { loginViewModel.user } returns MutableStateFlow(null)
        every { aisleViewModel.uiState } returns MutableStateFlow(AisleUiState.Success(emptyList()))
        every { medicineViewModel.uiState } returns MutableStateFlow(MedicineUiState.Success(emptyList()))
    }
}
