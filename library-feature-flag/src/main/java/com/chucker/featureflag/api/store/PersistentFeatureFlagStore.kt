package com.chucker.featureflag.api.store

import android.content.Context
import com.chucker.featureflag.api.FeatureFlagModule

class PersistentFeatureFlagStore(context: Context) : FeatureFlagStore {

    private val store by lazy {
        context.getSharedPreferences("ChuckerFeatureFlag", Context.MODE_PRIVATE)
    }

    override fun set(feature: String, enabled: Boolean) {
        store.edit()
            .putBoolean(feature, enabled)
            .apply()
    }

    override fun get(feature: String): Boolean {
        return store.getBoolean(feature, false)
    }

    override fun getAll(): List<Pair<String, Boolean>> {
        return store.all
            .filter { it.key != FeatureFlagModule.KEY_INITIALIZED }
            .map { it.key to it.value as Boolean }
    }
}