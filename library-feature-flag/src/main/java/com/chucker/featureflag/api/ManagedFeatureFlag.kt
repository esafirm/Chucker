package com.chucker.featureflag.api

interface ManagedFeatureFlag {
    val name: String

    fun isEnabled(): Boolean = FeatureFlagModule.store.get(name)
    fun setEnabled(isEnabled: Boolean): Unit = FeatureFlagModule.store.set(name, isEnabled)
}
