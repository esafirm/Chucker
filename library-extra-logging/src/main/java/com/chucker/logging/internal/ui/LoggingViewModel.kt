package com.chucker.logging.internal.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chucker.logging.internal.data.entity.LogData
import com.chucker.logging.internal.data.repository.LoggingRepositoryProvider
import com.chucker.logging.internal.support.formatDate
import com.chucker.logging.internal.support.formatLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class LoggingViewModel : ViewModel() {

    private var currentFilter = ""
    private var currentSelectedTag = ""
    val allTagLiveData = MutableLiveData(emptyList<String>())

    val logDatas = MutableLiveData(emptyList<LogViewParam>())
    private var jobSearch: Job? = null

    fun init() {
        doQuery()
        getAllTag()
    }

    fun updateItemsFilter(searchQuery: String) {
        currentFilter = searchQuery
        doQuery()
    }

    private fun getAllTag() {
        viewModelScope.launch {
            allTagLiveData.value = withContext(Dispatchers.IO) { LoggingRepositoryProvider.get().getAllTag() }
        }
    }

    fun updateTag(selectedTag: String) {
        val tmpSelectedTag = if (selectedTag == "(empty)") {
            ""
        } else selectedTag
        currentSelectedTag = tmpSelectedTag
        doQuery()
    }

    fun clearLog() {
        viewModelScope.launch {
            LoggingRepositoryProvider.get().deleteAllLog()
            doQuery()
        }
    }

    fun doQuery() {
        jobSearch?.cancel()
        jobSearch = viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                with(LoggingRepositoryProvider.get()) {
                    when {
                        currentFilter.isBlank() && currentSelectedTag.isBlank() -> {
                            getAllLogLiveData()
                        }
                        else -> {
                            getFilteredLog(currentSelectedTag, currentFilter)
                        }
                    }
                }
            }.map {
                LogViewParam(
                    tag = it.tag, logText = it.logString.formatLog(), dateText = it.timeStamp.formatDate()
                )
            }

            logDatas.value = result
        }
    }
}