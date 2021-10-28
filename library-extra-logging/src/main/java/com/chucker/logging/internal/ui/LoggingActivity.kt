package com.chucker.logging.internal.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.chucker.logging.R
import com.chucker.logging.databinding.ChuckerActivityLoggingBinding
import com.chucker.logging.internal.support.shareAsFile
import com.chucker.logging.internal.support.shareAsUtf8Text
import com.chucker.logging.internal.support.showDialog
import kotlinx.coroutines.launch

internal class LoggingActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ChuckerActivityLoggingBinding.inflate(layoutInflater)
    }

    private val viewModel: LoggingViewModel by viewModels()
    private val loggingAdapter: LoggingAdapter by lazy {
        LoggingAdapter(::copyLog)
    }
    private val spinnerAdapter by lazy { ArrayAdapter<String>(this, R.layout.chucker_item_tag) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        title = "Logging"

        with(binding.recyclerView) {
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@LoggingActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = loggingAdapter
        }
        with(binding.editText) {
            setAdapter(spinnerAdapter)
            setText("(empty)", false)
            addTextChangedListener {
                it?.let { editText ->
                    viewModel.updateTag(editText.toString())
                }
            }
        }

        viewModel.logDatas.observe(this) {
            loggingAdapter.submitList(it)
        }

        viewModel.allTagLiveData.observe(this) {
            spinnerAdapter.clear()
            spinnerAdapter.add("(empty)")
            spinnerAdapter.addAll(it)
        }
        viewModel.init()
    }

    override fun onResume() {
        super.onResume()
        viewModel.doQuery()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chucker_logging_menu, menu)
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
                    message = getString(R.string.chucker_clear_logging),
                    positiveButtonText = getString(com.chuckerteam.chucker.R.string.chucker_clear),
                    negativeButtonText = getString(com.chuckerteam.chucker.R.string.chucker_cancel),
                    onPositiveClick = {
                        viewModel.clearLog()
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
        val logs = viewModel.logDatas.value
        if (logs.isNullOrEmpty()) {
            Toast.makeText(
                this@LoggingActivity,
                com.chuckerteam.chucker.R.string.chucker_export_empty_text,
                Toast.LENGTH_SHORT
            ).show()
            return@launch
        }
        val sharable = LogDataShareable(logs)
        lifecycleScope.launch {
            val shareIntent = sharable.shareAsUtf8Text(
                activity = this@LoggingActivity,
                intentTitle = getString(R.string.chucker_share_all_logging_title),
                intentSubject = getString(R.string.chucker_share_all_logging_subject)
            )
            startActivity(shareIntent)
        }
    }

    private fun shareTransactionAsFile() = lifecycleScope.launch {
        val logs = viewModel.logDatas.value
        if (logs.isNullOrEmpty()) {
            Toast.makeText(
                this@LoggingActivity,
                com.chuckerteam.chucker.R.string.chucker_export_empty_text,
                Toast.LENGTH_SHORT
            ).show()
            return@launch
        }

        val sharableTransactions = LogDataShareable(logs)
        val shareIntent = sharableTransactions.shareAsFile(
            activity = this@LoggingActivity,
            fileName = "logging.txt",
            intentTitle = getString(R.string.chucker_share_all_logging_title),
            intentSubject = getString(R.string.chucker_share_all_logging_subject),
            clipDataLabel = "logging"
        )
        if (shareIntent != null) {
            startActivity(shareIntent)
        }
    }

    private fun copyLog(logData: LogViewParam) = lifecycleScope.launch {
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Chucker Log", logData.logText)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this@LoggingActivity, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, LoggingActivity::class.java)
            )
        }
    }
}