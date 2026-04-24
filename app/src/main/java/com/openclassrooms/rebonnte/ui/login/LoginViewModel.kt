package com.openclassrooms.rebonnte.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.repository.AuthRepository
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.ui.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val currentUser = authRepository.currentUser

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    fun onSignInResult(idToken: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            val result = authRepository.signInWithGoogle(idToken)
            val firebaseUser = result.getOrNull()
            if (result.isSuccess && firebaseUser != null) {
                _loginState.value = LoginUiState.Success(
                    User(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        photoUrl = firebaseUser.photoUrl?.toString()?.takeIf { url -> url.isNotBlank() }
                    )
                )
            } else {
                _loginState.value = LoginUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}
