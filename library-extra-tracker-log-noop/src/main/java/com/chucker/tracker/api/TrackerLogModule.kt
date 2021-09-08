package com.chucker.tracker.api

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.extramodule.ChuckerExtraModule

class TrackerLogModule(
    app: Application,
    enableNotification: Boolean = true
) : ChuckerExtraModule {

    override val moduleName: String = "Tracker Log"

    override fun onNavigateToModule(context: Context) {
        // noop
    }
}