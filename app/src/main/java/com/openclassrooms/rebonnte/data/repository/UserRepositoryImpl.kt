package com.openclassrooms.rebonnte.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.openclassrooms.rebonnte.R
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val context: android.content.Context) : UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override suspend fun uploadProfilePicture(uri: Uri): Result<String> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception(context.getString(R.string.error_permission_denied)))
            val storageRef = storage.reference.child("profile_pictures/${user.uid}.jpg")
            
            storageRef.putFile(uri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()
            
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(downloadUrl))
                .build()
            
            user.updateProfile(profileUpdates).await()
            user.reload().await()
            
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(Exception(context.getString(R.string.error_unknown)))
        }
    }

    override fun isEmailLogin(): Boolean {
        val user = auth.currentUser
        return user?.providerData?.any { it.providerId == "password" } ?: false
    }
}
