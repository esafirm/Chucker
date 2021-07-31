package com.chuckerteam.chucker.internal.featureflag

public interface FeatureFlag {
    public val name: String

    public fun isEnabled(): Boolean = FeatureFlagModule.store.get(this)
    public fun setEnabled(isEnabled: Boolean): Unit = FeatureFlagModule.store.set(this, isEnabled)
}
