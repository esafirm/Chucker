package com.chuckerteam.chucker.api.extramodule

public object ChuckerExtraModuleRegistry {

    internal val extraModules = mutableListOf<ChuckerExtraModule>()

    public fun addExtraModule(extraModule: ChuckerExtraModule) {
        extraModules.add(extraModule)
    }
}