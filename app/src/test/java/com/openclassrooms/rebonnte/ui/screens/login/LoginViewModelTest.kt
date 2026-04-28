package com.openclassrooms.rebonnte.ui.screens.login

import android.net.Uri
import app.cash.turbine.test
import com.google.firebase.auth.FirebaseUser
import com.openclassrooms.rebonnte.domain.repository.AuthRepository
import com.openclassrooms.rebonnte.domain.usecase.UploadProfilePictureUseCase
import com.openclassrooms.rebonnte.ui.state.LoginUiState
import com.openclassrooms.rebonnte.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private lateinit var viewModel: LoginViewModel
    private val authRepository: AuthRepository = mockk()
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase = mockk()
    private val currentUserFlow = MutableStateFlow<FirebaseUser?>(null)

    @Before
    fun setup() {
        every { authRepository.currentUser } returns currentUserFlow
        viewModel = LoginViewModel(authRepository, uploadProfilePictureUseCase)
    }

    @Test
    fun testSignOut() = runTest(testDispatcher) {
        coEvery { authRepository.signOut() } returns Unit
        viewModel.signOut()
        advanceUntilIdle()
        coVerify { authRepository.signOut() }
    }

    @Test
    fun testSignInWithEmailSuccess() = runTest(testDispatcher) {
        val mockUser: FirebaseUser = mockk {
            every { uid } returns "123"
            every { displayName } returns "Test"
            every { email } returns "test@test.com"
            every { photoUrl } returns null
            every { providerData } returns emptyList()
        }
        coEvery { authRepository.signInWithEmail(any(), any()) } returns Result.success(mockUser)

        viewModel.loginState.test {
            assertEquals(LoginUiState.Idle, awaitItem())
            viewModel.signInWithEmail("test@test.com", "pass")
            assertEquals(LoginUiState.Loading, awaitItem())
            val success = awaitItem()
            assertTrue(success is LoginUiState.Success)
            assertEquals("123", (success as LoginUiState.Success).user.id)
        }
    }

    @Test
    fun testUpdateDisplayName() = runTest(testDispatcher) {
        coEvery { authRepository.updateDisplayName(any()) } returns Result.success(Unit)
        coEvery { authRepository.refreshUser() } returns Result.success(Unit)

        viewModel.profileLoading.test {
            assertEquals(false, awaitItem())
            viewModel.updateDisplayName("New")
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun testUploadProfilePicture() = runTest(testDispatcher) {
        val uri: Uri = mockk()
        coEvery { uploadProfilePictureUseCase(any()) } returns Result.success("url")
        coEvery { authRepository.refreshUser() } returns Result.success(Unit)

        viewModel.profileLoading.test {
            assertEquals(false, awaitItem())
            viewModel.uploadProfilePicture(uri)
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
        }
    }
}
