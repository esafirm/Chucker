package com.chucker.tracker.internal.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.chucker.tracker.internal.data.repository.TrackerLogRepositoryProvider
import kotlinx.coroutines.launch

internal class TrackerLogViewModel : ViewModel() {

    private val currentFilter = MutableLiveData("")

    val trackerLogs: LiveData<List<String>> = currentFilter.switchMap { searchQuery ->
        with(TrackerLogRepositoryProvider.get()) {
            when {
                searchQuery.isNullOrBlank() -> {
                    getAllLogLiveData()
                }
                else -> {
                    getFilteredLog(searchQuery)
                }
            }
        }
    }

    fun updateItemsFilter(searchQuery: String) {
        currentFilter.value = searchQuery
    }

    suspend fun getAllTrackerLog(): List<String> {
        return TrackerLogRepositoryProvider.get().getAllLog()
    }

    fun clearTransactions() {
        viewModelScope.launch {
            TrackerLogRepositoryProvider.get().deleteAllTransactions()
        }
    }
}