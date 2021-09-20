package com.chucker.logging.api

import android.content.Context
import com.chucker.logging.internal.data.repository.LoggingRepositoryProvider
import com.chucker.logging.internal.ui.LoggingActivity
import com.chuckerteam.chucker.api.extramodule.ChuckerExtraModule

class LoggingModule(
    app: Context
) : ChuckerExtraModule {

    override val moduleName: String = "Logging"

    init {
        LoggingRepositoryProvider.initialize(app)
    }

    override fun onNavigateToModule(context: Context) {
        LoggingActivity.start(context)
    }
}