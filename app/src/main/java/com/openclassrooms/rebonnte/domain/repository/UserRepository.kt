package com.openclassrooms.rebonnte.domain.repository

import android.net.Uri

interface UserRepository {
    suspend fun uploadProfilePicture(uri: Uri): Result<String>
    fun isEmailLogin(): Boolean
}
