package com.kmaslowiec.lookgo.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import com.kmaslowiec.lookgo.CoroutinesUnconfinedDispatcherTestExtension
import com.kmaslowiec.lookgo.preferences.repository.impl.AppPreferencesRepositoryImpl
import com.kmaslowiec.lookgo.util.eTagPreferencesKey
import com.kmaslowiec.lookgo.util.isFirstTimePreferencesKey
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesUnconfinedDispatcherTestExtension::class)
class AppPreferencesRepositoryImplTest {

    @BeforeEach
    fun setup() {
        mockkStatic(DataStore<Preferences>::edit)
    }

    @AfterEach
    fun cleanUp() {
        unmockkAll()
    }

    private fun createTestDataStore(scope: TestScope): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            scope = scope.backgroundScope,
            produceFile = {
                File.createTempFile("test_preferences", ".preferences_pb")
            }
        )
    }

    @Test
    fun `isFirstTime returns true by default`() = runTest {
        val dataStore = createTestDataStore(this)
        val tested = AppPreferencesRepositoryImpl(dataStore)

        assertTrue(tested.isFirstTime.first())
    }

    @Test
    fun `firstTimeAccess change value to false`() = runTest {
        val dataStore: DataStore<Preferences> = mockk(relaxed = true) {
            every { data } returns flowOf(preferencesOf(isFirstTimePreferencesKey to true))
        }
        val tested = AppPreferencesRepositoryImpl(dataStore)
        val mutablePreferences: MutablePreferences = mockk(relaxed = true)
        val slot = slot<suspend (MutablePreferences) -> Unit>()
        coEvery { dataStore.edit(capture(slot)) } coAnswers {
            slot.captured(mutablePreferences)
            mutablePreferences
        }

        tested.firstTimeAccess()

        verify { mutablePreferences[isFirstTimePreferencesKey] = false }
    }

    @Test
    fun `getETag is called with empty default string`() = runTest {
        val dataStore = createTestDataStore(this)
        val tested = AppPreferencesRepositoryImpl(dataStore)

        assertTrue(tested.storedETag.first() == "")
    }

    @Test
    fun `storedETag returns saved value`() = runTest {
        val dataStore = createTestDataStore(this)
        dataStore.edit { preferences ->
            preferences[eTagPreferencesKey] = "eTag"
        }
        val tested = AppPreferencesRepositoryImpl(dataStore)

        val result = tested.storedETag.first()

        assertTrue(result == "eTag")
    }

    @Test
    fun `saveETag is called`() = runTest {
        val dataStore: DataStore<Preferences> = mockk(relaxed = true) {
            every { data } returns flowOf(preferencesOf(eTagPreferencesKey to ""))
        }
        val tested = AppPreferencesRepositoryImpl(dataStore)
        val mutablePreferences: MutablePreferences = mockk(relaxed = true)
        val slot = slot<suspend (MutablePreferences) -> Unit>()
        coEvery { dataStore.edit(capture(slot)) } coAnswers {
            slot.captured(mutablePreferences)
            mutablePreferences
        }

        tested.saveETag()

        verify { mutablePreferences[eTagPreferencesKey] = "" }
    }
}
