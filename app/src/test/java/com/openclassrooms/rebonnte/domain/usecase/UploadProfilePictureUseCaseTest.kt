package com.openclassrooms.rebonnte.domain.usecase

import android.net.Uri
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UploadProfilePictureUseCaseTest {

    private lateinit var useCase: UploadProfilePictureUseCase
    private val repository: UserRepository = mockk()

    @Before
    fun setup() {
        useCase = UploadProfilePictureUseCase(repository)
    }

    @Test
    fun testInvoke() = runTest {
        val uri: Uri = mockk()
        coEvery { repository.uploadProfilePicture(uri) } returns Result.success("url")
        every { repository.isEmailLogin() } returns true
        val result = useCase(uri)
        assertTrue(result.isSuccess)
    }
}
