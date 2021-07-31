package com.chuckerteam.chucker.internal.featureflag

import android.content.Context
import com.chuckerteam.chucker.api.extramodule.ExtraModule
import com.chuckerteam.chucker.internal.featureflag.ui.FeatureFlagActivity

public class FeatureFlagModule(
    passedStore: FeatureFlagStore,
    initialFlags: (FeatureFlagStore) -> Unit
) : ExtraModule {

    public companion object {
        internal lateinit var store: FeatureFlagStore
    }

    init {
        store = passedStore
        val isInitialized = store.get(SimpleFeatureFlag("initialized"))
        if (isInitialized.not()) {
            initialFlags(store)
        }
    }

    override val moduleName: String = "Feature Flag"

    override fun onNavigateToModule(context: Context) {
        FeatureFlagActivity.start(context)
    }
}