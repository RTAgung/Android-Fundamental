package com.example.customnotif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.customnotif.NotificationService.Companion.CHANNEL_ID
import com.example.customnotif.NotificationService.Companion.CHANNEL_NAME
import com.example.customnotif.NotificationService.Companion.REPLY_ACTION
import kotlinx.android.synthetic.main.activity_reply.*

class ReplyActivity : AppCompatActivity() {
    companion object{
        private const val KEY_MESSAGE_ID = "key_message_id"
        private const val KEY_NOTIF_ID = "key_notif_id"

        fun getReplyMessageIntent(context: Context, notifId: Int, messageId: Int): Intent {
            val intent = Intent(context, ReplyActivity::class.java)
            intent.action = REPLY_ACTION
            intent.putExtra(KEY_MESSAGE_ID, messageId)
            intent.putExtra(KEY_NOTIF_ID, notifId)
            return intent
        }
    }

    private var mMessageId: Int = 0
    private var mNotifyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)

        val intent = intent

        if (REPLY_ACTION == intent.action){
            mMessageId = intent.getIntExtra(KEY_MESSAGE_ID, 0)
            mNotifyId = intent.getIntExtra(KEY_NOTIF_ID, 0)
        }

        button_send.setOnClickListener{ sendMessage(mNotifyId, mMessageId) }
    }

    private fun sendMessage(mNotifyId: Int, messageId: Int) {
        updateNotification(mNotifyId)

        val message = edit_reply.text.toString().trim { it <= ' ' }
        Toast.makeText(this, "Message ID: $messageId\nMessage: $message",
            Toast.LENGTH_SHORT).show()

        finish()
    }

    private fun updateNotification(notifyId: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_48)
            .setContentTitle(getString(R.string.notif_title_sent))
            .setContentText(getString(R.string.notif_content_sent))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(notifyId, notification)
    }
}