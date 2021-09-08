package com.chucker.tracker.internal.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.chucker.tracker.R
import com.chucker.tracker.databinding.ChuckerActivityTrackerLogBinding
import com.chucker.tracker.internal.support.shareAsFile
import com.chucker.tracker.internal.support.shareAsUtf8Text
import com.chucker.tracker.internal.support.showDialog
import kotlinx.coroutines.launch

internal class TrackerLogActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ChuckerActivityTrackerLogBinding.inflate(layoutInflater)
    }

    private val viewModel: TrackerLogViewModel by viewModels()
    private val trackerLogAdapter: TrackerLogAdapter by lazy {
        TrackerLogAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        title = "Tracker log"

        with(binding.recyclerView) {
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@TrackerLogActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = trackerLogAdapter
        }

        viewModel.trackerLogs.observe(this) {
            trackerLogAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chucker_tracker_log_menu, menu)
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
                viewModel.updateItemsFilter(newText.orEmpty())
                return true
            }
        })
        searchView.setIconifiedByDefault(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.chuckerteam.chucker.R.id.clear -> {
                showDialog(
                    title = getString(com.chuckerteam.chucker.R.string.chucker_clear),
                    message = getString(R.string.chucker_clear_tracker_log),
                    positiveButtonText = getString(com.chuckerteam.chucker.R.string.chucker_clear),
                    negativeButtonText = getString(com.chuckerteam.chucker.R.string.chucker_cancel),
                    onPositiveClick = {
                        viewModel.clearTransactions()
                    },
                    onNegativeClick = null
                )
                true
            }
            R.id.share_text -> {
                shareTransactionAsText()
                true
            }
            R.id.share_file -> {
                shareTransactionAsFile()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun shareTransactionAsText() = lifecycleScope.launch {
        val logs = viewModel.getAllTrackerLog()
        if (logs.isNullOrEmpty()) {
            Toast.makeText(
                this@TrackerLogActivity,
                com.chuckerteam.chucker.R.string.chucker_export_empty_text,
                Toast.LENGTH_SHORT
            ).show()
            return@launch
        }
        val sharable = TrackerLogsShareable(logs)
        lifecycleScope.launch {
            val shareIntent = sharable.shareAsUtf8Text(
                activity = this@TrackerLogActivity,
                intentTitle = getString(R.string.chucker_share_all_tracker_log_title),
                intentSubject = getString(R.string.chucker_share_all_tracker_log_subject)
            )
            startActivity(shareIntent)
        }
    }

    private fun shareTransactionAsFile() = lifecycleScope.launch {
        val logs = viewModel.getAllTrackerLog()
        if (logs.isNullOrEmpty()) {
            Toast.makeText(
                this@TrackerLogActivity,
                com.chuckerteam.chucker.R.string.chucker_export_empty_text,
                Toast.LENGTH_SHORT
            ).show()
            return@launch
        }

        val sharableTransactions = TrackerLogsShareable(logs)
        val shareIntent = sharableTransactions.shareAsFile(
            activity = this@TrackerLogActivity,
            fileName = "tracker_log.txt",
            intentTitle = getString(R.string.chucker_share_all_tracker_log_title),
            intentSubject = getString(R.string.chucker_share_all_tracker_log_subject),
            clipDataLabel = "tracker_log"
        )
        if (shareIntent != null) {
            startActivity(shareIntent)
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, TrackerLogActivity::class.java)
            )
        }
    }
}