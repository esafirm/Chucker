package com.chucker.featureflag.api.store

import java.util.concurrent.ConcurrentHashMap

class MapFeatureFlagStore : FeatureFlagStore {

    private val store = ConcurrentHashMap<String, Boolean>()

    override fun set(feature: String, enabled: Boolean) {
        store[feature] = enabled
    }

    override fun get(feature: String): Boolean {
        return store[feature] ?: false
    }

    override fun getAll(): List<Pair<String, Boolean>> {
        return store.toList()
    }
}