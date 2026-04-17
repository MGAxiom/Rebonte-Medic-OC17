package com.openclassrooms.rebonnte.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<FirebaseUser?>
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?>
    suspend fun updateDisplayName(name: String): Result<Unit>
    suspend fun signOut()
}
