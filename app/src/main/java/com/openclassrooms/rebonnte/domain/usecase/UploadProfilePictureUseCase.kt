package com.openclassrooms.rebonnte.domain.usecase

import android.net.Uri
import com.openclassrooms.rebonnte.domain.repository.UserRepository

class UploadProfilePictureUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(uri: Uri): Result<String> {
        return if (userRepository.isEmailLogin()) {
            userRepository.uploadProfilePicture(uri)
        } else {
            Result.failure(Exception("Feature only available for email/password users"))
        }
    }
}
