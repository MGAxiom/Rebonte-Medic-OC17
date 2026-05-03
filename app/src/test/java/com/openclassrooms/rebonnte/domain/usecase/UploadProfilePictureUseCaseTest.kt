package com.openclassrooms.rebonnte.domain.usecase

import android.net.Uri
import com.openclassrooms.rebonnte.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    fun `invoke should return success when user is email login and upload succeeds`() = runTest {
        val uri: Uri = mockk()
        val expectedUrl = "https://example.com/profile.jpg"
        every { repository.isEmailLogin() } returns true
        coEvery { repository.uploadProfilePicture(uri) } returns Result.success(expectedUrl)

        val result = useCase(uri)

        assertTrue(result.isSuccess)
        assertEquals(expectedUrl, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when user is not email login`() = runTest {
        val uri: Uri = mockk()
        every { repository.isEmailLogin() } returns false

        val result = useCase(uri)

        assertTrue(result.isFailure)
        assertEquals("Feature only available for email/password users", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return failure when repository upload fails`() = runTest {
        val uri: Uri = mockk()
        val exception = Exception("Upload failed")
        every { repository.isEmailLogin() } returns true
        coEvery { repository.uploadProfilePicture(uri) } returns Result.failure(exception)

        val result = useCase(uri)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
