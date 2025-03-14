package com.example.servicesapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyForegroundService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val builder = createNotificationBuilder()

    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    var onProgressChanged: ((Int) -> Unit)? = null

    // Чтобы активити подписалась на сервис
    override fun onBind(p0: Intent?): IBinder {
        log("onBind")
        return LocalBinder()
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            for (i in 0..100 step 5) {
                delay(1000)
                val notification = builder
                    .setProgress(100, i, false)
                    .build()
                notificationManager.notify(NOTIFICATION_ID, notification)
                onProgressChanged?.invoke(i) // установили сюда слушатель и можем реагировать на изменения
                log("Timer $i")
            }
            stopSelf()
        }
        return START_STICKY
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyForegroundService $msg")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotificationBuilder() =
        NotificationCompat.Builder(this, CHANNEL_ID) // Создаём уведомление
            .setContentTitle("ForegroundService Title") // Заголовок
            .setContentText("ForegroundService Text") // Текст
            .setSmallIcon(R.drawable.ic_launcher_background) // Передаем иконку
            .setOnlyAlertOnce(true)
            .setProgress(100, 0, true)

    inner class LocalBinder : Binder() {
        fun getService() = this@MyForegroundService
    }


    companion object {
        private const val CHANNEL_ID = "channel_id_for_ForegroundService"
        private const val CHANNEL_NAME = "channel_for_ForegroundService"
        private const val NOTIFICATION_ID = 1


        fun newIntent(context: Context): Intent {
            return Intent(context, MyForegroundService::class.java)
        }
    }
}