package com.chuckerteam.chucker.internal.featureflag.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.chuckerteam.chucker.databinding.ChuckerActivityFeatureFlagBinding
import com.chuckerteam.chucker.internal.featureflag.FeatureFlagModule
import com.chuckerteam.chucker.internal.ui.BaseChuckerActivity

internal class FeatureFlagActivity : BaseChuckerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ChuckerActivityFeatureFlagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        title = "Feature Flag"

        val adapter = FeatureFlagAdapter()
        binding.recyclerView.adapter = adapter

        adapter.submitList(FeatureFlagModule.store.getAll())
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, FeatureFlagActivity::class.java)
            )
        }
    }
}