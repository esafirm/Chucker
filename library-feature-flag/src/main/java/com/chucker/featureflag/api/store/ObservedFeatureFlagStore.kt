package com.chucker.featureflag.api.store

import com.chucker.featureflag.api.FeatureFlagModule

class ObservedFeatureFlagStore(
    private val backingStore: FeatureFlagStore,
    private val onUpdate: (String, Boolean) -> Unit
) : FeatureFlagStore by backingStore {

    override fun set(feature: String, enabled: Boolean) {
        backingStore.set(feature, enabled)

        if (feature != FeatureFlagModule.KEY_INITIALIZED) {
            onUpdate(feature, enabled)
        }
    }
}