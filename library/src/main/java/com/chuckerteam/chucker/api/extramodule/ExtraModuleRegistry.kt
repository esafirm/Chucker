package com.chuckerteam.chucker.api.extramodule

public object ExtraModuleRegistry {

    internal val extraModules = mutableListOf<ExtraModule>()

    public fun addExtraModule(extraModule: ExtraModule) {
        extraModules.add(extraModule)
    }
}