package com.example.servicesapp

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleWork")
        val page = intent.getIntExtra(PAGE, 0)

        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i Page: $page")
        }
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyJobIntentService $msg")
    }


    companion object {
        private const val PAGE = "page"
        private const val JOB_ID = 105

        fun enqueue(context: Context, page: Int) {
            JobIntentService.enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}