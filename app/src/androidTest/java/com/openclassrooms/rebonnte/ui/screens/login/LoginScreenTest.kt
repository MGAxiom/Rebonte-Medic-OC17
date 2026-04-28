package com.openclassrooms.rebonnte.ui.screens.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.openclassrooms.rebonnte.ui.state.LoginUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<LoginViewModel>(relaxed = true)

    @Test
    fun loginScreen_displaysWelcomeText() {
        every { viewModel.loginState } returns MutableStateFlow(LoginUiState.Idle)
        every { viewModel.user } returns MutableStateFlow(null)

        composeTestRule.setContent {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {}
            )
        }

        composeTestRule.onNodeWithText("Welcome to Rebonnte").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign in with Google").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign in with credentials").assertIsDisplayed()
    }

    @Test
    fun loginScreen_googleLoginClick_callsViewModel() {
        every { viewModel.loginState } returns MutableStateFlow(LoginUiState.Idle)
        every { viewModel.user } returns MutableStateFlow(null)

        composeTestRule.setContent {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {}
            )
        }

        composeTestRule.onNodeWithText("Sign in with Google").performClick()
        
        verify { viewModel.signInWithGoogle(any()) }
    }
}
