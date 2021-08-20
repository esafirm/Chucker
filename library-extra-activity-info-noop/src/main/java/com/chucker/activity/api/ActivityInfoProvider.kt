package com.chucker.activity.api

interface ActivityInfoProvider {
    fun getActivityName(): String
    fun getOtherInfo(): Map<String, String>
}