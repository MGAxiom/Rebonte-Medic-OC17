package com.openclassrooms.rebonnte.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ThemeRepositoryImplTest {

    @get:Rule
    val tmpFolder = TemporaryFolder()

    private lateinit var testDataStore: DataStore<Preferences>
    private lateinit var repository: ThemeRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @Before
    fun setup() {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { tmpFolder.newFile("test.preferences_pb") }
        )
        repository = ThemeRepositoryImpl(testDataStore)
    }

    @Test
    fun testInitialValueIsNull() = runTest(testDispatcher) {
        repository.isDarkTheme.test {
            assertEquals(null, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testSetDarkTheme() = runTest(testDispatcher) {
        repository.isDarkTheme.test {
            assertEquals(null, awaitItem())
            repository.setDarkTheme(true)
            assertEquals(true, awaitItem())
            repository.setDarkTheme(false)
            assertEquals(false, awaitItem())
        }
    }
}
