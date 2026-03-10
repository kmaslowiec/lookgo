package com.kmaslowiec.lookgo.network.cache

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ETagCacheTest {

    @Test
    fun `getETag returns correct value`() = runTest {
        val storage = mockk<ETagStorage>()
        coEvery { storage.loadAll() } returns mapOf("key1" to "value1", "key2" to "value2")
        val tested = ETagCache(
            storage = storage,
            scope = this,
            dispatcher = StandardTestDispatcher(testScheduler)
        )
        advanceUntilIdle()

        val result = tested.getETag("key1")

        assertEquals("value1", result)
    }

    @Test
    fun `putETag saves value correctly`() = runTest {
        val storage = mockk<ETagStorage>(relaxed = true)
        coEvery { storage.loadAll() } returns emptyMap()
        val tested = ETagCache(
            storage = storage,
            scope = this,
            dispatcher = StandardTestDispatcher(testScheduler)
        )
        tested.putETag("key1", "value1")

        val result = tested.getETag("key1")

        assertEquals("value1", result)
    }
}
