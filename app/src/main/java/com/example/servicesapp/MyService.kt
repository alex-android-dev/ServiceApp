package com.example.servicesapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        log("onCreate")
        super.onCreate()
    }

    override fun onDestroy() {
        log("onDestroy")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        for (i in 0 until 100) {
            Thread.sleep(1000)
            log("Timer $i")
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyService $msg")
    }

    companion object {
        fun newIntent(context: Context) : Intent {
            return Intent(context, MyService::class.java)
        }
    }
}