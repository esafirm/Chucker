package com.chucker.featureflag.internal.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.chuckerteam.chucker.R
import com.chuckerteam.chucker.databinding.ChuckerActivityFeatureFlagBinding

internal class FeatureFlagActivity : AppCompatActivity() {

    private val viewModel: FeatureFlagViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ChuckerActivityFeatureFlagBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        title = "Feature Flag"

        val adapter = FeatureFlagAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.loadFlags()
        viewModel.featureFlags.observe(this) {
            adapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chucker_feature_flag, menu)
        setUpSearch(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setUpSearch(menu: Menu?) {
        checkNotNull(menu)

        val searchMenuItem = menu.findItem(R.id.search)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.loadFlags(newText.orEmpty())
                return true
            }
        })
        searchView.setIconifiedByDefault(true)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, FeatureFlagActivity::class.java)
            )
        }
    }
}