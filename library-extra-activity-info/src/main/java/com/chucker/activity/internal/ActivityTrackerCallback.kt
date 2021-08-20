package com.chucker.activity.internal

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chucker.activity.api.ActivityInfoProvider
import com.chuckerteam.chucker.api.Chucker

internal class ActivityTrackerCallback(
        app: Application,
        private val notificationHelper: NotificationHelper?
) : Application.ActivityLifecycleCallbacks {

    var currentInfo: ActivityInfo? = null

    init {
        app.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        if (Chucker.isChuckerActivity(activity).not()) {
            val info = activity.getActivityInfo()
            currentInfo = info
            notificationHelper?.show(info)
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    private fun Activity.getActivityInfo(): ActivityInfo {
        val activity = this

        val fragments: List<String> = when {
            activity is AppCompatActivity -> {
                activity.supportFragmentManager.fragments.map {
                    val visibility = it.isVisible.asString()
                    "${it.simpleName()} ($visibility) [${it.tag}]"
                }
            }
            Build.VERSION.SDK_INT >= 26 -> {
                activity.fragmentManager.fragments.map {
                    val visibility = it.isVisible.asString()
                    "${it.simpleName()} ($visibility) [${it.tag}]"
                }
            }
            else -> emptyList()
        }

        val otherInfo: Map<String, String> = if (activity is ActivityInfoProvider) {
            activity.getOtherInfo()
        } else {
            emptyMap()
        }

        return ActivityInfo(
                simpleName = activity.getActivitySimpleName(),
                packageName = activity.packageName,
                fragments = fragments,
                otherInfo = otherInfo
        )
    }

    private fun Boolean.asString() = if (this) "(visible)" else "(hidden)"

    private fun Activity.getActivitySimpleName(): String {
        if (Build.VERSION.SDK_INT >= 30) {
            return localClassName
        }
        return javaClass.simpleName ?: javaClass.toString()
    }

    private fun Any.simpleName(): String {
        return javaClass.canonicalName ?: toString()
    }
}