package com.chucker.activity.internal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.chuckerteam.chucker.R

internal class NotificationHelper(private val context: Context) {

    companion object {
        private const val NOTIFICATION_CHANNEL = "chucker_transactions"
        private const val NOTIFICATION_ID = 1139
    }

    private val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val transactionsChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL,
                    context.getString(R.string.chucker_network_notification_category),
                    NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannels(listOf(transactionsChannel))
        }
    }

    private fun createIntent(activityInfo: ActivityInfo) = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            ActivityInfoActivity.createIntent(context, activityInfo),
            PendingIntent.FLAG_UPDATE_CURRENT or immutableFlag()
    )

    fun show(activityInfo: ActivityInfo) {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setContentIntent(createIntent(activityInfo))
                .setLocalOnly(true)
                .setSmallIcon(R.drawable.chucker_ic_decoded_url_white)
                .setColor(ContextCompat.getColor(context, R.color.chucker_color_primary))
                .setContentTitle(activityInfo.simpleName)
                .setContentText(activityInfo.packageName)
                .setAutoCancel(true)

        val inboxStyle = NotificationCompat.InboxStyle()

        activityInfo.fragments.forEach { fragmentText ->
            inboxStyle.addLine(fragmentText)
        }
        builder.setStyle(inboxStyle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setSubText(activityInfo.fragments.size.toString())
        } else {
            builder.setNumber(activityInfo.fragments.size)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun immutableFlag() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        0
    }
}
