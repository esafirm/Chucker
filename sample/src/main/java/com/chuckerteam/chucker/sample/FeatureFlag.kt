package com.chuckerteam.chucker.sample

enum class SampleFeatureFlag : EnumFeatureFlag {
    FEATURE_A,
    FEATURE_B,
    FEATURE_C
}

interface EnumFeatureFlag {

    companion object {
        internal val backingMap = mutableMapOf<String, Boolean>()
    }

    val name: String
    var isEnabled: Boolean
        get() = backingMap[name] ?: false
        set(value) {
            backingMap[name] = value
        }
}