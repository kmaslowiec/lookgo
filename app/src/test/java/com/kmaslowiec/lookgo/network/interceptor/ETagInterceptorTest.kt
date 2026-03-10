package com.kmaslowiec.lookgo.network.interceptor

import com.kmaslowiec.lookgo.network.cache.ETagCache
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test

class ETagInterceptorTest {

    @Test
    fun `adds ETag header if stored`() = runTest {
        val cache = mockk<ETagCache>(relaxed = true)
        every { cache.putETag("/", "etag123") } just Runs
        every { cache.getETag("/") } returns "etag123"
        val chain = mockk<Interceptor.Chain>(relaxed = true)
        val request = Request.Builder().url("https://test.com").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK").addHeader("eTag", "etag123")
            .body("{}".toResponseBody("application/json".toMediaType()))
            .build()
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response

        val interceptor = ETagInterceptor(cache = cache)
        interceptor.intercept(chain)

        verify {
            chain.proceed(match {
                it.header("If-None-Match") == "etag123"
            })
        }
    }

    @Test
    fun `no header if eTag is null`() = runTest {
        val cache = mockk<ETagCache>(relaxed = true)
        every { cache.getETag(any()) } returns null
        val chain = mockk<Interceptor.Chain>(relaxed = true)
        val request = Request.Builder().url("https://test.com").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK").addHeader("eTag", "etag123")
            .body("{}".toResponseBody("application/json".toMediaType()))
            .build()
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response

        val interceptor = ETagInterceptor(cache = cache)
        interceptor.intercept(chain)

        verify {
            chain.proceed(match {
                it.header("If-None-Match") == null
            })
        }
    }

    @Test
    fun `no header if eTag is blank`() = runTest {
        val cache = mockk<ETagCache>(relaxed = true)
        every { cache.getETag(any()) } returns ""
        val chain = mockk<Interceptor.Chain>(relaxed = true)
        val request = Request.Builder().url("https://test.com").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK").addHeader("eTag", "etag123")
            .body("{}".toResponseBody("application/json".toMediaType()))
            .build()
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response

        val interceptor = ETagInterceptor(cache = cache)
        interceptor.intercept(chain)

        verify {
            chain.proceed(match {
                it.header("If-None-Match") == null
            })
        }
    }
}
