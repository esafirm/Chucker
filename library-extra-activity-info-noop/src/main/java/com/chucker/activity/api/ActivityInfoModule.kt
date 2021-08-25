package com.chucker.activity.api

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.extramodule.ChuckerExtraModule

class ActivityInfoModule(
        app: Application,
        enableNotification: Boolean = true
) : ChuckerExtraModule {

    override val moduleName: String = "Activity Info"

    override fun onNavigateToModule(context: Context) {
        //NO-OP
    }
}