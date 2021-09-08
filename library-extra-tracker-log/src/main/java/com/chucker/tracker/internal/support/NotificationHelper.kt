package com.chucker.tracker.internal.support

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.chucker.tracker.internal.service.ClearTrackerLogJobIntentServiceReceiver
import com.chucker.tracker.internal.ui.TrackerLogActivity
import com.chuckerteam.chucker.R

internal class NotificationHelper(private val context: Context) {

    companion object {
        private const val NOTIFICATION_CHANNEL = "chucker_tracker_log"
        private const val NOTIFICATION_ID = 1140
        private const val INTENT_REQUEST_CODE = 12
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

    private fun createIntent() = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        Intent(context, TrackerLogActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or immutableFlag()
    )

    fun show(count: Int) {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setContentIntent(createIntent())
            .setLocalOnly(true)
            .setSmallIcon(R.drawable.chucker_ic_decoded_url_white)
            .setColor(ContextCompat.getColor(context, R.color.chucker_color_primary))
            .setContentTitle("Tracker Log")
            .setContentText("Total Log: $count")
            .setAutoCancel(true)
            .addAction(createClearAction())

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createClearAction(): NotificationCompat.Action {
        val clearTitle = context.getString(R.string.chucker_clear)
        val clearTransactionsBroadcastIntent =
            Intent(context, ClearTrackerLogJobIntentServiceReceiver::class.java)
        val pendingBroadcastIntent = PendingIntent.getBroadcast(
            context,
            INTENT_REQUEST_CODE,
            clearTransactionsBroadcastIntent,
            PendingIntent.FLAG_ONE_SHOT or immutableFlag()
        )
        return NotificationCompat.Action(
            R.drawable.chucker_ic_delete_white,
            clearTitle,
            pendingBroadcastIntent
        )
    }

    fun dismissNotifications() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun immutableFlag() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        0
    }
}
