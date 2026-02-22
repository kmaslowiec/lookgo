package com.kmaslowiec.lookgo.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import com.kmaslowiec.lookgo.CoroutinesUnconfinedDispatcherTestExtension
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
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesUnconfinedDispatcherTestExtension::class)
class AppPreferencesRepositoryImplTest {

    private var dataStore: DataStore<Preferences> = mockk(relaxed = true) {
        every { data } returns flowOf(preferencesOf(isFirstTimePreferencesKey to true))
    }
    private val tested: AppPreferencesRepositoryImpl = AppPreferencesRepositoryImpl(dataStore)

    @BeforeEach
    fun setup() {
        mockkStatic(DataStore<Preferences>::edit)
    }

    @AfterEach
    fun cleanUp() {
        unmockkAll()
    }

    @Test
    fun `isFirstTime returns true by default`() = runTest {
        assertTrue(tested.isFirstTime.first())
    }

    @Test
    fun `firstTimeAccess change value to false`() = runTest {
        val mutablePreferences: MutablePreferences = mockk(relaxed = true)
        val slot = slot<suspend (MutablePreferences) -> Unit>()
        coEvery { dataStore.edit(capture(slot)) } coAnswers {
            slot.captured(mutablePreferences)
            mutablePreferences
        }

        tested.firstTimeAccess()

        verify { mutablePreferences[isFirstTimePreferencesKey] = false }
    }
}
