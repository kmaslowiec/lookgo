package com.kmaslowiec.lookgo.network.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kmaslowiec.lookgo.util.eTagPreferencesKeys
import io.mockk.coEvery
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

@ExperimentalCoroutinesApi
class ETagStorageTest {

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
    fun `loadAll returns only valid etags`() = runTest {
        val dataStore = createTestDataStore(scope = this)
        val storage = ETagStorage(dataStore)
        dataStore.edit { prefs ->
            prefs[eTagPreferencesKeys] = setOf("valid", "blank", "missing")
            prefs[stringPreferencesKey("eTag_valid")] = "abc123"
            prefs[stringPreferencesKey("eTag_blank")] = ""
        }

        val result = storage.loadAll()

        assertEquals(mapOf("valid" to "abc123"), result)
    }

    @Test
    fun `eTag is null or blank`() = runTest {
        val dataStore = createTestDataStore(scope = this)
        coEvery { dataStore.edit(any()) } returns preferencesOf()
        val storage = ETagStorage(dataStore)

        val result = storage.loadAll()

        assertTrue { result["key1"].isNullOrBlank() }
    }

    @Test
    fun `loadAll returns map with data added with save`() = runTest {
        val dataStore = createTestDataStore(scope = this)
        val storage = ETagStorage(dataStore)
        storage.save("key1", "value1")
        storage.save("key2", "value2")

        val result = storage.loadAll()

        assertEquals(mapOf("key1" to "value1", "key2" to "value2"), result)
    }
}
