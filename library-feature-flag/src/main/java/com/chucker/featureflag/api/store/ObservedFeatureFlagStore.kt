package com.chucker.featureflag.api.store

class ObservedFeatureFlagStore(
    private val backingStore: FeatureFlagStore,
    private val onUpdate: (String, Boolean) -> Unit
) : FeatureFlagStore by backingStore {

    override fun set(feature: String, enabled: Boolean) {
        backingStore.set(feature, enabled)
        onUpdate(feature, enabled)
    }
}