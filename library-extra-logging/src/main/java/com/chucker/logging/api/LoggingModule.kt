package com.chucker.logging.api

import android.app.Application
import android.content.Context
import com.chucker.logging.internal.ui.LoggingActivity
import com.chucker.logging.internal.data.repository.LoggingRepositoryProvider
import com.chuckerteam.chucker.api.extramodule.ChuckerExtraModule

class LoggingModule(
    app: Application
) : ChuckerExtraModule {

    override val moduleName: String = "Tracker Log"

    init {
        LoggingRepositoryProvider.initialize(app)
    }

    override fun onNavigateToModule(context: Context) {
        LoggingActivity.start(context)
    }
}