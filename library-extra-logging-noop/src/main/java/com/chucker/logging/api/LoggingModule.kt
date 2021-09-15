package com.chucker.logging.api

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.extramodule.ChuckerExtraModule

class LoggingModule(
    app: Context,
) : ChuckerExtraModule {

    override val moduleName: String = "Log Logging"

    override fun onNavigateToModule(context: Context) {
        // noop
    }
}