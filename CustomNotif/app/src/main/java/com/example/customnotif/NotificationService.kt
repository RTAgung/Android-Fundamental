package com.example.customnotif

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

class NotificationService : IntentService("NotificationService") {
    companion object {
        private const val KEY_REPLY = "key_reply_message"
        const val REPLY_ACTION = "com.example.notification.directreply.REPLY_ACTION"
        const val CHANNEL_ID = "channel_01"
        val CHANNEL_NAME: CharSequence = "dicoding channel"

        fun getReplyMessage(intent: Intent): CharSequence? {
            val remoteInput = RemoteInput.getResultsFromIntent(intent)
            return remoteInput?.getCharSequence(KEY_REPLY)
        }
    }

    private var mNotificationId: Int = 0
    private var mMessageId: Int = 0

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            showNotification()
        }
    }

    private fun showNotification() {
        mNotificationId = 1
        mMessageId = 123

        val replyLabel = getString(R.string.notif_action_reply)
        val remoteInput = RemoteInput.Builder(KEY_REPLY)
            .setLabel(replyLabel)
            .build()

        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.ic_baseline_reply_48,
            replyLabel,
            getReplyPendingIntent()
        )
            .addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_48)
            .setContentTitle(getString(R.string.notif_title))
            .setContentText(getText(R.string.notif_content))
            .setShowWhen(true)
            .addAction(replyAction)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(mNotificationId, notification)
    }

    private fun getReplyPendingIntent(): PendingIntent? {
        val intent: Intent
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent = NotificationBroadcastReceiver.getReplyMessageIntent(this, mNotificationId, mMessageId)
            PendingIntent.getBroadcast(applicationContext, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            intent = ReplyActivity.getReplyMessageIntent(this, mNotificationId, mMessageId)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    }
}
