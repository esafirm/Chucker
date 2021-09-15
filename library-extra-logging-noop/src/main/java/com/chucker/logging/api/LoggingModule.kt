package com.chucker.logging.api

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.extramodule.ChuckerExtraModule

class LoggingModule(
    app: Application,
) : ChuckerExtraModule {

    override val moduleName: String = "Tracker Log"

    override fun onNavigateToModule(context: Context) {
        // noop
    }
}