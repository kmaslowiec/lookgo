package com.kmaslowiec.lookgo.network.cache

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class ETagCache(
    val storage: ETagStorage,
    val scope: CoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val map = ConcurrentHashMap<String, String>()

    init {
        scope.launch(dispatcher) {
            val restored = storage.loadAll()
            map.putAll(restored)
        }
    }

    fun getETag(urlKey: String): String? = map[urlKey]

    fun putETag(urlKey: String, eTag: String) {
        map[urlKey] = eTag
        scope.launch(Dispatchers.IO) {
            storage.save(urlKey, eTag)
        }
    }
}
