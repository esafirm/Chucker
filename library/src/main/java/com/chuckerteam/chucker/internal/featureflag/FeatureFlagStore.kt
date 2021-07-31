package com.chuckerteam.chucker.internal.featureflag

import android.content.Context

public interface FeatureFlagStore {
    public fun set(feature: FeatureFlag, enabled: Boolean)
    public fun get(feature: FeatureFlag): Boolean

    public fun getAll(): List<FeatureFlag>
}

public class DefaultFeatureFlagStore(context: Context) : FeatureFlagStore {

    private val store by lazy {
        context.getSharedPreferences("ChuckerFeatureFlag", Context.MODE_PRIVATE)
    }

    override fun set(feature: FeatureFlag, enabled: Boolean) {
        store.edit()
            .putBoolean(feature.name, enabled)
            .apply()
    }

    override fun get(feature: FeatureFlag): Boolean {
        return store.getBoolean(feature.name, false)
    }

    override fun getAll(): List<FeatureFlag> {
        return store.all.map {
            SimpleFeatureFlag(it.key)
        }
    }
}
