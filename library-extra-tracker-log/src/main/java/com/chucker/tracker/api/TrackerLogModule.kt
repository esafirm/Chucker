package com.chucker.tracker.api

import android.app.Application
import android.content.Context
import com.chucker.tracker.internal.ui.TrackerLogActivity
import com.chucker.tracker.internal.data.repository.TrackerLogRepositoryProvider
import com.chuckerteam.chucker.api.extramodule.ChuckerExtraModule

class TrackerLogModule(
    app: Application,
    enableNotification: Boolean = true
) : ChuckerExtraModule {

    override val moduleName: String = "Tracker Log"

    init {
        TrackerLogRepositoryProvider.initialize(app)
    }

    override fun onNavigateToModule(context: Context) {
        TrackerLogActivity.start(context)
    }
}