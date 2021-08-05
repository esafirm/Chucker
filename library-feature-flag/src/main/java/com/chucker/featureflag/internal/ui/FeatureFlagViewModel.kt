package com.chucker.featureflag.internal.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chucker.featureflag.api.FeatureFlagModule
import com.chucker.featureflag.api.ManagedFeatureFlag
import com.chucker.featureflag.api.SimpleManagerFeatureFlag

class FeatureFlagViewModel : ViewModel() {

    private val originalFeatureFlag by lazy {
        FeatureFlagModule.store.getAll().map {
            SimpleManagerFeatureFlag(it.first)
        }
    }

    val featureFlags = MutableLiveData<List<ManagedFeatureFlag>>()

    fun loadFlags(filter: String = "") {
        featureFlags.postValue(originalFeatureFlag.filter { flag ->
            val name = flag.name.lowercase()
            name.contains(filter.lowercase())
        })
    }
}