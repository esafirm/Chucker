package com.chucker.featureflag.api.store

interface FeatureFlagStore {
    fun set(feature: String, enabled: Boolean)
    fun get(feature: String): Boolean

    fun getAll(): List<Pair<String, Boolean>>
}