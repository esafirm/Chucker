package com.chucker.featureflag.api

import android.content.Context
import com.chucker.featureflag.api.store.FeatureFlagStore
import com.chucker.featureflag.internal.ui.FeatureFlagActivity
import com.chuckerteam.chucker.api.extramodule.ExtraModule

class FeatureFlagModule(
    passedStore: FeatureFlagStore,
    initialFlags: (FeatureFlagStore) -> Unit,
    onLoad: (FeatureFlagStore) -> Unit = {}
) : ExtraModule {

    companion object {
        internal lateinit var store: FeatureFlagStore

        internal const val KEY_INITIALIZED = "initialized"
    }

    init {
        store = passedStore
        val isInitialized = store.get(KEY_INITIALIZED)
        if (isInitialized.not()) {
            initialFlags(store)
        } else {
            onLoad(store)
        }
    }

    override val moduleName: String = "Feature Flag"

    override fun onNavigateToModule(context: Context) {
        FeatureFlagActivity.start(context)
    }
}