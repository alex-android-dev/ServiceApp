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

class MyIntentServiceSecond : IntentService(NAME_INTENT_SERVICE) {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true) // Чтобы последний интент доставлялся при перезапуске сервиса
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onHandleIntent(params: Intent?) {
        log("onHandleIntent")

        val page = params?.getIntExtra(PAGE, 0) ?: 0 // получаем страницу

        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i Page: $page")
        }
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyForegroundService $msg")
    }


    companion object {
        private const val NAME_INTENT_SERVICE = "MyIntentService"
        private const val PAGE = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentServiceSecond::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}