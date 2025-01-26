package com.example.servicesapp

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyIntentService : IntentService(NAME_INTENT_SERVICE) {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        for (i in 0 until 10) {
            Thread.sleep( 1000)
            log("Timer $i")
        }
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyForegroundService $msg")
    }

    private fun createNotification(): Notification {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID) // Создаём уведомление
            .setContentTitle("ForegroundService Title") // Заголовок
            .setContentText("ForegroundService Text") // Текст
            .setSmallIcon(R.drawable.ic_launcher_background) // Передаем иконку
            .build() // Строим

        return notification
    }

    companion object {
        private const val NAME_INTENT_SERVICE = "MyIntentService"
        private const val CHANNEL_ID = "channel_id_for_ForegroundService"
        private const val CHANNEL_NAME = "channel_for_ForegroundService"
        private const val NOTIFICATION_ID = 1

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }
    }
}