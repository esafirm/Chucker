package com.chuckerteam.chucker.api.extramodule

import android.content.Context

public interface ExtraModule {
    public val moduleName: String
    public fun onNavigateToModule(context: Context)
}