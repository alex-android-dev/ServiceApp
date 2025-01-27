package com.example.servicesapp

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService : JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        log("onCreate")
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")
        coroutineScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var workItem = params?.dequeueWork()
                // достаем сервис из стека

                while (workItem != null) {
                    val page = workItem.intent?.getIntExtra(PAGE, 0)
                    // получаем номер страницы
                    // будет работать до тех пор, пока dequeueWork не вернёт null

                    for (i in 0 until 5) {
                        delay(1000)
                        log("Timer $i, Page: $page")
                    }

                    params?.completeWork(workItem) // завершаем сервис, который находится в очереди
                    workItem = params?.dequeueWork()
                }
            }

        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyJobService $msg")
    }

    companion object {
        const val JOB_ID = 55
        private const val PAGE = "page" // ключ

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }

    }

}