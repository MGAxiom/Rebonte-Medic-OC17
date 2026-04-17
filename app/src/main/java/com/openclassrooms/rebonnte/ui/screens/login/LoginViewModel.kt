package com.openclassrooms.rebonnte.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.rebonnte.domain.model.User
import com.openclassrooms.rebonnte.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _displayNameRequest = MutableStateFlow<String?>(null)

    val user: StateFlow<User?> = authRepository.currentUser
        .map { firebaseUser ->
            firebaseUser?.let {
                User(
                    id = it.uid,
                    name = it.displayName ?: "",
                    email = it.email ?: "",
                    photoUrl = it.photoUrl?.toString()?.takeIf { url -> url.isNotBlank() }
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            _displayNameRequest
                .debounce(500L)
                .filter { it != null && it != user.value?.name }
                .distinctUntilChanged()
                .collectLatest { name ->
                    name?.let { authRepository.updateDisplayName(it) }
                }
        }
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun onSignInResult(idToken: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = authRepository.signInWithGoogle(idToken)
            val firebaseUser = result.getOrNull()
            if (result.isSuccess && firebaseUser != null) {
                _loginState.value = LoginState.Success(
                    User(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        photoUrl = firebaseUser.photoUrl?.toString()?.takeIf { url -> url.isNotBlank() }
                    )
                )
            } else {
                _loginState.value =
                    LoginState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun updateDisplayName(name: String) {
        _displayNameRequest.value = name
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}
