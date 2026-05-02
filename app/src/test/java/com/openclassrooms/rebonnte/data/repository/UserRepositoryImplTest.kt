package com.openclassrooms.rebonnte.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.storage.FirebaseStorage
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.mockk.coEvery
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import android.net.Uri

class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl
    private val context: Context = mockk()
    private val auth: FirebaseAuth = mockk()
    private val storage: FirebaseStorage = mockk()

    @Before
    fun setup() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns auth
        mockkStatic(FirebaseStorage::class)
        every { FirebaseStorage.getInstance() } returns storage
        repository = UserRepositoryImpl(context)
    }

    @Test
    fun isEmailLogin_true() {
        val mockUser: FirebaseUser = mockk()
        val mockUserInfo: UserInfo = mockk()
        every { auth.currentUser } returns mockUser
        every { mockUser.providerData } returns listOf(mockUserInfo)
        every { mockUserInfo.providerId } returns "password"

        assertTrue(repository.isEmailLogin())
    }

    @Test
    fun isEmailLogin_false() {
        val mockUser: FirebaseUser = mockk()
        val mockUserInfo: UserInfo = mockk()
        every { auth.currentUser } returns mockUser
        every { mockUser.providerData } returns listOf(mockUserInfo)
        every { mockUserInfo.providerId } returns "google.com"

        assertFalse(repository.isEmailLogin())
    }

    @Test
    fun isEmailLogin_noUser_false() {
        every { auth.currentUser } returns null
        assertFalse(repository.isEmailLogin())
    }

    @Test
    fun uploadProfilePicture_noUser_failure() = runTest {
        every { auth.currentUser } returns null
        every { context.getString(any()) } returns "Permission denied"

        val result = repository.uploadProfilePicture(mockk())
        assertTrue(result.isFailure)
    }

    @Test
    fun uploadProfilePicture_exception_failure() = runTest {
        val mockUser: FirebaseUser = mockk()
        every { auth.currentUser } returns mockUser
        every { mockUser.uid } returns "uid123"
        every { context.getString(any()) } returns "Error"
        
        val storageRef: StorageReference = mockk()
        every { storage.reference } returns storageRef
        every { storageRef.child(any()) } throws Exception("Storage error")

        val result = repository.uploadProfilePicture(mockk())
        assertTrue(result.isFailure)
    }
}
