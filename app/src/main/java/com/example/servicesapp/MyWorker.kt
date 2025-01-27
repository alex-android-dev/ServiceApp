package com.example.servicesapp

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) :
    Worker(context, workerParameters) {

    // вся работа выполняется в методе doWork
    // метод работает асинхронно
    override fun doWork(): Result {
        log("doWork")

        val page = workerParameters.inputData.getInt(PAGE, 0)
        // inputData работает по аналогии с Bundle

        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i, Page: $page")
        }
        return Result.success()
    }

    private fun log(msg: String) {
        Log.d("SERVICE_TAG", "MyWorker $msg")
    }

    companion object {
        private const val PAGE = "page"

        const val WORK_NAME = "work name"

        // создаём Request
        fun makeRequest(page: Int): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(workDataOf(PAGE to page)) // Создаем пару ключ значения, которые будут в реквесте
                .setConstraints(makeConstraints()) // Устанавливаем ограничения
                .build()

        private fun makeConstraints(): Constraints =
            Constraints.Builder()
                .setRequiresCharging(true)
                .build()
    }
}