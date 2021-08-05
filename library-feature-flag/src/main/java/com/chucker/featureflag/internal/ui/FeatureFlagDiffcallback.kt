package com.chucker.featureflag.internal.ui

import androidx.recyclerview.widget.DiffUtil
import com.chucker.featureflag.api.ManagedFeatureFlag

internal object FeatureFlagDiffcallback : DiffUtil.ItemCallback<ManagedFeatureFlag>() {
    override fun areItemsTheSame(
        oldItem: ManagedFeatureFlag,
        newItem: ManagedFeatureFlag
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ManagedFeatureFlag,
        newItem: ManagedFeatureFlag
    ): Boolean {
        return oldItem.name == newItem.name
    }
}