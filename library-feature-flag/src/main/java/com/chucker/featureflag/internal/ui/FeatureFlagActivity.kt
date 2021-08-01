package com.chucker.featureflag.internal.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chucker.featureflag.api.FeatureFlagModule
import com.chuckerteam.chucker.databinding.ChuckerActivityFeatureFlagBinding
import com.chucker.featureflag.api.SimpleManagerFeatureFlag

internal class FeatureFlagActivity : AppCompatActivity() {

    private val store = FeatureFlagModule.store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ChuckerActivityFeatureFlagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        title = "Feature Flag"

        val adapter = FeatureFlagAdapter()
        binding.recyclerView.adapter = adapter

        adapter.submitList(
            store.getAll().map { SimpleManagerFeatureFlag(it.first) }
        )
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, FeatureFlagActivity::class.java)
            )
        }
    }
}