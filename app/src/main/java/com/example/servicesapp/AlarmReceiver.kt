package com.example.servicesapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        context?.let { context ->
            val notificationManager =
                getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager


            createNotificationChannel(notificationManager)

            val notification = NotificationCompat.Builder(
                context,
                CHANNEL_ID
            ) // Создаём уведомление
                .setContentTitle("ForegroundService Title") // Заголовок
                .setContentText("ForegroundService Text") // Текст
                .setSmallIcon(R.drawable.ic_launcher_background) // Передаем иконку

            notificationManager.notify(NOTIFICATION_ID, notification.build())

        }

    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    companion object {
        private const val CHANNEL_ID = "channel_id_for_ForegroundService"
        private const val CHANNEL_NAME = "channel_for_ForegroundService"
        private const val NOTIFICATION_ID = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}