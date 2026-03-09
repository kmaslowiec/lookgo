package com.kmaslowiec.lookgo.network.interceptor

import com.kmaslowiec.lookgo.network.cache.ETagCache
import okhttp3.Interceptor
import okhttp3.Response

class ETagInterceptor(
    private val cache: ETagCache,
) : Interceptor {

    private val urlKeySelector: (okhttp3.HttpUrl) -> String = { it.encodedPath }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlKey = urlKeySelector(request.url)
        val eTag = cache.getETag(urlKey)
        val conditionalRequest = if (!eTag.isNullOrBlank()) {
            request.newBuilder()
                .header("If-None-Match", eTag)
                .build()
        } else request
        val response = chain.proceed(conditionalRequest)

        response.header("ETag")?.takeIf { it.isNotBlank() }?.let { newETag ->
            cache.putETag(urlKey, newETag)
        }

        return response
    }
}
