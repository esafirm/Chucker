package com.chucker.featureflag.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chucker.featureflag.api.ManagedFeatureFlag
import com.chuckerteam.chucker.databinding.ChuckerListItemFeatureFlagBinding

internal class FeatureFlagAdapter :
    ListAdapter<ManagedFeatureFlag, FeatureFlagAdapter.FeatureFlagViewHolder>(
        FeatureFlagDiffcallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureFlagViewHolder {
        val viewBinding = ChuckerListItemFeatureFlagBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeatureFlagViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: FeatureFlagViewHolder, position: Int) =
        holder.bind(getItem(position))

    class FeatureFlagViewHolder(
        private val itemBinding: ChuckerListItemFeatureFlagBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {


        fun bind(featureFlag: ManagedFeatureFlag) {
            itemBinding.itemSwitch.run {
                text = featureFlag.name
                setOnCheckedChangeListener(null)
                isChecked = featureFlag.isEnabled()
                setOnCheckedChangeListener { _, isChecked ->
                    featureFlag.setEnabled(isChecked)
                }
            }
        }
    }
}
