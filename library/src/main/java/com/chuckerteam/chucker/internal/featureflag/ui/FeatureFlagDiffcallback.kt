package com.chuckerteam.chucker.internal.featureflag.ui

import androidx.recyclerview.widget.DiffUtil
import com.chuckerteam.chucker.internal.featureflag.FeatureFlag

internal object FeatureFlagDiffcallback : DiffUtil.ItemCallback<FeatureFlag>() {
    override fun areItemsTheSame(oldItem: FeatureFlag, newItem: FeatureFlag): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FeatureFlag, newItem: FeatureFlag): Boolean {
        return oldItem == newItem
    }
}