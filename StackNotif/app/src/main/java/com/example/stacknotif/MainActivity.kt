package com.example.stacknotif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var idNotification = 0
    private val stackNotif = ArrayList<NotificationItem>()

    companion object {
        private const val CHANNEL_NAME = "dicoding channel"
        private const val GROUP_KEY_EMAILS = "group_key_emails"
        private const val NOTIFICATION_REQUEST_CODE = 200
        private const val MAX_NOTIFICATION = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSend.setOnClickListener {
            val sender = edtSender.text.toString().trim()
            val message = edtMessage.text.toString().trim()
            if (sender.isEmpty() || message.isEmpty()) {
                Toast.makeText(this@MainActivity, "Data harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                stackNotif.add(NotificationItem(idNotification, sender, message))
                sendNotif()
                idNotification++
                edtSender.setText("")
                edtMessage.setText("")

                val methodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                methodManager.hideSoftInputFromWindow(edtMessage.windowToken, 0)
            }
        }
    }

    private fun sendNotif() {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_notifications_48)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder: NotificationCompat.Builder

        val CHANNEL_ID = "channel_01"
        if (idNotification < MAX_NOTIFICATION){
            builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("New Email from " + stackNotif[idNotification].sender)
                    .setContentText(stackNotif[idNotification].message)
                    .setSmallIcon(R.drawable.ic_baseline_mail_48)
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
        } else {
            val inboxStyle = NotificationCompat.InboxStyle()
                    .addLine("New Email from " + stackNotif[idNotification].sender)
                    .addLine("New Email from " + stackNotif[idNotification - 1].sender)
                    .setBigContentTitle("$idNotification new emails")
                    .setSummaryText("mail@dicoding")
            builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("$idNotification new emails")
                    .setContentText("mail@dicoding.com")
                    .setSmallIcon(R.drawable.ic_baseline_mail_48)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        mNotificationManager.notify(idNotification, notification)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        stackNotif.clear()
        idNotification = 0
    }
}