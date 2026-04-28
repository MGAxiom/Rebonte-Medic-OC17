package com.openclassrooms.rebonnte.ui

import app.cash.turbine.test
import com.openclassrooms.rebonnte.domain.repository.ThemeRepository
import com.openclassrooms.rebonnte.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ThemeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ThemeViewModel
    private val themeRepository: ThemeRepository = mockk()
    private val isDarkThemeFlow = MutableStateFlow<Boolean?>(null)

    @Before
    fun setup() {
        every { themeRepository.isDarkTheme } returns isDarkThemeFlow
        viewModel = ThemeViewModel(themeRepository)
    }

    @Test
    fun testIsDarkTheme() = runTest {
        viewModel.isDarkTheme.test {
            assertEquals(null, awaitItem())
            isDarkThemeFlow.value = true
            assertEquals(true, awaitItem())
            isDarkThemeFlow.value = false
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun testToggleTheme() = runTest {
        coEvery { themeRepository.setDarkTheme(any()) } returns Unit
        viewModel.toggleTheme(true)
        coVerify { themeRepository.setDarkTheme(true) }
        viewModel.toggleTheme(false)
        coVerify { themeRepository.setDarkTheme(false) }
    }
}
